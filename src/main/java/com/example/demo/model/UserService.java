package com.example.demo.model;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserService extends ServicCommon {

	private static final long serialVersionUID = 2420319221881431764L; 
	
	@Autowired UserRepository userRepository;
	@Autowired CodeService codeSerivce ; 

	public UserService() {
	}
	
	public User createUser( HttpServletRequest request ) {
		String userId 	= request.getParameter( "user_id" );
		String email 	= request.getParameter( "user_email" );
		String passwd 	= request.getParameter( "user_pass" );
		
		User user = null ; 
		
		if( this.isValid( userId ) && this.isValid( email ) && this.isValid( passwd ) ) { 
			user = userRepository.findByUserId( userId ) ;
			
			if( null != user ) {
				return null ; 
			} else if( null == user ) { 
				Code userRoleNormal = codeSerivce.getCode( "USER-ROLE-NORMAL", "사용자", 1 );
				
				user = new User( userId, passwd , userRoleNormal ); 
				user.email = email ;
				
				user = this.userRepository.save( user );
			}
		}
		
		return user ; 
	}
	
	public User saveUserInfo( User user, HttpServletRequest request ) {
		String email 	= request.getParameter( "user_email" );
		String passwd 	= request.getParameter( "user_pass" );
		
		boolean updated = false ;
		
		if( this.isValid( email ) && ! email.equals( user.email ) ) {
			user.email = email ; 
			
			updated = true ; 
		}
		
		if( this.isValid( passwd ) ) {
			user.passwd = passwd ; 
			
			updated = true ; 
		}
		
		if( updated ) {
			user = this.userRepository.save( user );
		}
		
		return user ;
	}

	public User getLoginUserCreateIfNotExist( HttpServletRequest request ) {
		
		var debug = true ; 
		
		if( true ) { 
			User rootUser = userRepository.findByUserId( sysConfig.defaultAdminId );
			
			if (null == rootUser) {
				Code userRoleAdmin  = codeSerivce.getCode( "USER-ROLE-ADMIN", "관리자" , 0 );
				//Code userRoleNormal = this.getCode( "USER-ROLE-NORMAL", "사용자", 1 );
				
				User newUser = new User( sysConfig.defaultSupserUserId, sysConfig.defaultSupserUserPasswd , userRoleAdmin ); 
				newUser.email = "sunabove@nate.com";
				
				this.userRepository.save( newUser );
	
				rootUser = userRepository.findByUserId( sysConfig.defaultSupserUserId );
			}
		}
		
		String id 		= request.getParameter( "user_id" );
		String passwd 	= request.getParameter( "user_pass" );
		
		User loginUser = null;
		
		if( this.isEmpty( id ) ) {
			if( debug ) { 
				log.info( "loginUse id is empty. set is as a default admin id." );
			}
			id = sysConfig.defaultAdminId ;
		} 
		
		if( this.isEmpty( passwd ) ) {
			if( debug ) log.info( "loginUse password is empty." );
		} else if( this.isValid( id ) && this.isValid( passwd ) ) { 
			loginUser = userRepository.findByUserIdAndPasswd(id, passwd) ;
			
			if( debug ) log.info( "loginUser by id and passwd = " + loginUser );
			
			if( null == loginUser ) {
				loginUser = userRepository.findByEmailAndPasswd(id, passwd);
				
				if( debug ) log.info( "loginUser by email and passwd = " + loginUser );
				
				if( null != loginUser && loginUser.deleted ) {
					loginUser = null ;
					
					if( debug ) log.info( "The loginUser is deleted." );
				}
				
				if( null == loginUser ) {
					request.setAttribute( "login_error_msg", "사용자 정보가 잘못되었습니다." );
				} 
			}
		}
		
		if( null != loginUser ) {
			request.setAttribute( "loginUser", loginUser );
		}
		
		return loginUser ;
	}
	
	public void createTestData( HttpServletRequest request ) {
		log.info( "createTestData" );
		
		var count = this.userRepository.count();
		
		log.info( "count = " + count );
		
		if( 200 > count ) {
			Code userRoleNormal = codeSerivce.getCode( "USER-ROLE-NORMAL", "사용자", 1 );
			
			for( long i = 0 , iLen = 170 - count ; i < iLen ; i ++ ) {
				User user = new User(); 
				user.userId = "test_" + ( i < 10 ? "0" : "" ) + i ;
				user.email = "test_" + ( i < 10 ? "0" : "" )  + i + "@test.com" ;
				user.passwd = "test_"  + ( i < 10 ? "0" : "" ) + i ;
				user.role = userRoleNormal ; 
				
				log.info( "save user = " + user );
				
				this.userRepository.save( user );
			}
		}
		
	}

}