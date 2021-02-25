package web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/main")
@Controller
public class IndexController extends ComController {

	private static final long serialVersionUID = -1215549637589312065L;

	@RequestMapping( value = { "index_02.html" , "main.html" } )
	public String index( HttpServletRequest request, RedirectAttributes ra ) {
		
		var loginRequire = true ;
		
		String forward = this.processRequest( request, loginRequire, ra ) ; 
		
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
	
	@RequestMapping( "list.html" )
	public String list( HttpServletRequest request ) { 
		
		var loginRequire = true ;
		
		this.processRequest( request, loginRequire ) ; 
		
		return "000_index.html";
	} 

}