package com.example.demo.model; 
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter; 

@Entity 
@Table(name = "code_tbl")
@Data @EqualsAndHashCode(callSuper=false)
public class Code extends EntityCommon {  

	private static final long serialVersionUID = -5392925777521538251L;

	@Id @Column( length = 191 )
	public String codeId ;  
	
	@ManyToOne @JoinColumn( name="grp_code_id" ) 
	public Code grpCode ;
	
	public String textValue ;	
	public Number numValue ; 
	public Integer ord ; 
	
	public Code() {
	}
	
	@PreUpdate @PrePersist
    protected void onUpdate() {
		super.onUpdate();
		
		if( null != this.textValue ) {
			this.numValue = this.parseDouble( this.textValue );
		} else if ( null != this.numValue ) {
			this.textValue = "" + this.numValue ;
		}
	} 

	
}