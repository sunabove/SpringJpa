package web.model;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional

public class PropService extends ServicCommon { 

	private static final long serialVersionUID = -8919069162905199897L;

	public PropService() {

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