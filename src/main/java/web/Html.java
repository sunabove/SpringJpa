package web;

import lombok.Getter;
import lombok.Setter;
import web.model.User;

public class Html extends WebObject {

	private static final long serialVersionUID = -4453059108163709106L;

	@Getter @Setter public String 	title			= null ;
	@Getter @Setter public String	currUrlPath		= null ;  
	
	@Getter @Setter public User 	loginUser 		= null ; 
	
	@Getter @Setter public String 	successMessage 	= null ; 
	@Getter @Setter public String 	errorMessage	= null ; 
	@Getter @Setter public String	crud			= null ; 
	
	@Getter @Setter public boolean editable		= false ; 

	public Html() {
	}

}
