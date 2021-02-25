package com.bbs.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional

public class PropService extends ServicCommon { 

	private static final long serialVersionUID = -8919069162905199897L;

	public PropService() {

	} 
	
	@Autowired public PropRepository propRepository; 
	public Prop getProp( String propId , String def ) {
		Prop prop = this.propRepository.findByPropId( propId );
		
		if( null == prop && propId != null ) {
			prop = new Prop();
			prop.propId = propId;
			prop.value = def ;
			
			propRepository.save( prop );
		}
		
		return prop;
	} 
	
	public Prop saveProp( String propId, String value ) {
		 
		Prop prop = propRepository.findByPropId( propId );
		
		if( null != prop ) {
			prop.value = value ;
			
			prop = propRepository.save( prop );
		}
		
		return prop ; 
	} 
	
	public Prop saveProp( Prop prop ) {
		prop = propRepository.save( prop );
		return prop;
	}

}