package com.example.demo.model; 
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter; 

@Entity 
@Table(name = "code_tbl")

public class Code extends EntityCommon {  

	private static final long serialVersionUID = -5392925777521538251L;

	@Id
	@Column( length = 191 )
	@Getter @Setter public String codeId ;  
	
	@ManyToOne
    @JoinColumn( name="grp_code_id" ) 
	@Getter @Setter public Code grpCode ;
	
	@Getter @Setter public String textValue ;	
	@Getter @Setter public Number numValue ; 
	@Getter @Setter public Integer ord ; 
	
	public Code() {
	}
	
	@PreUpdate
    @PrePersist
    protected void onUpdate() {
		super.onUpdate();
		
		if( null != this.textValue ) {
			this.numValue = this.parseDouble( this.textValue );
		} else if ( null != this.numValue ) {
			this.textValue = "" + this.numValue ;
		}
	} 

	
}