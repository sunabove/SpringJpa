package com.example.demo.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Component
@Data @EqualsAndHashCode(callSuper=false)
public class SysConfig extends JsonObject {

	private static final long serialVersionUID = 34910595661725712L;
	
	public String defaultSupserUserId = "admin";
	public String defaultSupserUserPasswd = "123456";
	public Long defaultBoardId = 1L ;
	public boolean showLoginId = false ;
	public String defaultAdminId = "admin" ;
	
	public SysConfig() {
	}

}
