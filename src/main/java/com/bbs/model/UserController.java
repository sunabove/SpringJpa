package com.bbs.model;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/user")
@Controller
public class UserController extends ComController {

	private static final long serialVersionUID = 1096136683760111201L;
	
	boolean debug = true ;
	
	@Autowired public UserRepository userRepository;
	
	public UserController() {
		this.loginRequire = true ; 
	}
	
	@RequestMapping( value = { "index.html" , "main.html" , "regi.html" } )
	public String userRegi( HttpServletRequest request, RedirectAttributes ra ) { 
		
		var loginRequire = false ;
		var adminRequire = false ; 
		
		String forward = this.processRequest( request , loginRequire, adminRequire, ra ) ;  
		
		if( this.isValid( forward ) ) {
			return forward ; 
		}
		
		String userId 	= request.getParameter( "user_id" );
		String email 	= request.getParameter( "user_email" );
		String passwd 	= request.getParameter( "user_pass" );
		
		String error = null ; 
		
		if( this.isValid( userId ) && this.isValid( email ) && this.isValid( passwd ) ) { 
			User user = userRepository.findByUserId( userId ) ;
			if( null != user ) {
				error = "이미 존재하는 아이디 입니다.";
			} else { 
				user = userService.createUser( request );
				
				if( null != user ) {
					return "redirect:/main/index.html" ;
				} else {
					error = "잘못된 사용자 정보입니다." ;
				}
			}
		} 
		
		request.setAttribute( "login_error_msg", error );
		request.setAttribute( "error_msg", error );
		request.setAttribute( "error", error );
		
		return "311_user_regi.html";
	}
	
	@RequestMapping( value = { "manage_role.html", "manage.html" } )
	public String manage( HttpServletRequest request, RedirectAttributes ra, @PageableDefault(size = 10) Pageable pageable ) { 
		var loginRequire = true ;
		var adminRequire = true ; 
		
		String forward = this.processRequest( request , loginRequire, adminRequire, ra ) ;  
		
		if( this.isValid( forward ) ) {
			return forward ; 
		}
		
		String user_id 		= request.getParameter( "user_id" );
		String user_role 	= request.getParameter( "user_role" );
		String user_delete 	= request.getParameter( "user_delete" );
		
		if( isValid( user_id ) && ( isValid( user_role) || isValid( user_delete ) ) ) {
			User user = this.userRepository.findByUserId( user_id );
			
			if( null != user ) {
				if( isValid( user_role ) ) {
					var code = this.codeRepository.findByCodeId( user_role ); ; 
					user.role = null != code ? code : user.role ;
				}
				
				if( isValid( user_delete ) ) {
					user.deleted = "1".equalsIgnoreCase( user_delete );
				}
				
				user = this.userService.saveUserInfo( user, request );
			}
		}
		
		String user_id_search = request.getParameter( "user_id_search" );
		
		Page<User> userPage = null ; 
		
		if( isEmpty( user_id_search ) ) { 
			userPage = this.userRepository.findAllByOrderByUserIdAsc( pageable ); 
		} else {
			userPage = this.userRepository.findAllByUserIdContainingOrderByUserIdAsc( user_id_search, pageable );
		}
		
		UserList users = new UserList( userPage );
		
		if( null != users ) {
			users.setRowNumbers( request );
		}
		
		request.setAttribute( "page", userPage );
		request.setAttribute( "users", users );
		request.setAttribute( "user_id_search", user_id_search );
		
		return "410_manage_user_role.html";
	}
	
	@RequestMapping( value = { "login.html" } )
	public String userLogin( HttpServletRequest request ) {
		
		request.setAttribute( "showLoginId", sysConfig.showLoginId );
		
		return "312_user_login.html";
	} 
	
	@RequestMapping( value = { "find_id.html" } )
	public String findId( HttpServletRequest request, RedirectAttributes ra) { 
		
		var loginRequire = true ;
		var adminRequire = false ; 
		
		String forward = this.processRequest( request , loginRequire, adminRequire, ra ) ;  
		
		if( this.isValid( forward ) ) {
			return forward ; 
		}
		
		return "313_user_find_id.html";
	}
	
	@RequestMapping( value = { "info.html" } )
	public String userInfo( HttpServletRequest request ) {
		
		var loginRequire = this.loginRequire ;
		
		String forward = this.processRequest( request, loginRequire ) ; 
		
		if( null != forward ) {
			return forward ; 
		} else {
			User loginUser = this.getLoginUser( request );
			
			userService.saveUserInfo( loginUser, request );
		}
		
		return "314_user_info.html";
	}
	
	@RequestMapping( value = { "logout.html" } )
	public String userLogOut( HttpServletRequest request ) {		
		String id 		= request.getParameter( "user_id" );
		String passwd 	= request.getParameter( "user_pass" ); 
		
		if( this.isValid( id ) && this.isValid( passwd ) ) {
			var loginRequire = true ; 
			this.processRequest(request, loginRequire );
		} else {
			this.setLoginUser( request, null );
			
			var loginRequire = false ;
			this.processRequest(request, loginRequire );
		}

		return "redirect:/main/main.html";
	}

}