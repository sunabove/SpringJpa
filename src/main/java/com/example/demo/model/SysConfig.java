package com.example.demo.model;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class SysConfig extends JsonObject {

	private static final long serialVersionUID = 34910595661725712L;
	
	@Getter @Setter public String defaultSupserUserId = "admin";
	
	@Getter @Setter public String defaultSupserUserPasswd = "12345678";
	
	@Getter @Setter public Long defaultBoardId = 1L ;
	
	@Getter @Setter public boolean showLoginId = false ;
	
	@Getter @Setter public String defaultAdminId = "admin" ;
	
	public SysConfig() {
	}

}
