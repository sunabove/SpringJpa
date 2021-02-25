package web.model;

import lombok.Data;

@Data
public class Html extends WebObject {

	private static final long serialVersionUID = -4453059108163709106L;

	public String 	title			= null ;
	public String	currUrlPath		= null ;  
	
	public User 	loginUser 		= null ; 
	
	public String 	successMessage 	= null ; 
	public String 	errorMessage	= null ; 
	public String	crud			= null ; 
	
	public boolean  editable		= false ; 

	public Html() {
	}

}
