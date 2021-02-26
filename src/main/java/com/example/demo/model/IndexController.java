package com.example.demo.model;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController extends ComController {

	private static final long serialVersionUID = -1215549637589312065L;
	
	@Autowired UserService userService ;
	
	@RequestMapping( value = { "/" , } )
	public String index( HttpServletRequest request, RedirectAttributes ra ) {
		log.info( "I am here." );
		
		return "redirect:/index.html";
	}

	@RequestMapping( value = { "/main/index_02.html" , "/main/main.html" } )
	public String indexMain( HttpServletRequest request, RedirectAttributes ra ) {
		
		var loginRequire = true ;
		
		String forward = this.processRequest( userService, request, loginRequire, ra ) ; 
		
		String user_id = request.getParameter( "user_id" );
		String user_pass = request.getParameter( "user_pass" );
		
		var loginUser = this.getLoginUser( request );
		
		if( this.isValid( user_id ) || this.isValid( user_pass ) ) {
			return "redirect:/data/index.html" ; 
		} else if( null != forward ) {
			return "110_main.html" ;  
		} else if( null != loginUser ) {
			return "redirect:/data/index.html" ; 
		} else {
			return "110_main.html" ;  
		}
	} 
	
	@RequestMapping( "/main/list.html" )
	public String mainList( HttpServletRequest request ) { 
		var loginRequire = true ;
		
		this.processRequest( userService, request, loginRequire ) ; 
		
		return "000_index.html";
	} 

}