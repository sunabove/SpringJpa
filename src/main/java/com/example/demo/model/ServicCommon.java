package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServicCommon extends WebObject { 
	
	private static final long serialVersionUID = 7177373740030139060L;

	@Autowired public SysConfig sysConfig ;
	
	public ServicCommon() {
	} 

}