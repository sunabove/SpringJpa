package com.example.demo.model; 
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter; 

@Entity
@Table(name = "prop_tbl")

public class Prop extends EntityCommon { 

	private static final long serialVersionUID = -2672345814320804684L;

	@Id
	@Column( length = 191 )
	@Getter @Setter public String propId ;  
	
	@Getter @Setter public String value ;	
	
	public Prop() {
	}
	
	public final String getValueFormat() {
		Integer i = this.parseInt( this.value ) ;
		
		if( null != i ) {
			return this.format( "%,d", i.intValue() );
		}
		
		return "";
	}
	
	public boolean increaseBy( int i ) {
		Integer d = this.parseInt( value ) ;
		
		if( null != d ) {
			d += i ;
			
			this.value = "" + d ;
			
			return true;  
		} else {
			return false; 
		}
	}
	
	public boolean increaseBy( double d ) {
		Double num = this.parseDouble( value ) ;
		
		if( null != num ) {
			num += d ;
			
			this.value = "" + num ;
			
			return true;  
		} else {
			return false; 
		}
	}
	
}