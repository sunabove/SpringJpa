package com.example.demo.model;

import java.sql.Timestamp;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity @Table( name = "db_file_log_tbl" )
@Data @EqualsAndHashCode(callSuper=false)
public class DbFileLog extends EntityCommon {  

	private static final long serialVersionUID = 4024002681670288781L;

	@Id
	@Column(length=191)
	public String fileLogId ;

	@OneToOne 
	public User downloadUser ;  
	
	@OneToOne 
	public DbFile downloadFile ;  
	
	@Column(length=191)
	public String gubun ;  
	
	@Column(length=191)
	public String filePath ;  
	
	public Integer fizeSize ;
	public int accessCount = 0 ;
	public int downloadCount = 0 ;
	public Boolean dowloadResult ;  
	
	@Column(length=191)
	public String ipAddr ;  
	
	public DbFileLog() {
	}  
	
	public String getHourIntervalDesc() {
		String fileLogId = this.fileLogId ;
		if( null == fileLogId ) {
			return null; 
		}
		
		String [] data = fileLogId.replaceAll( "TOT-DOWN-NO-", "" ).split( " " ); 
		
		if( null == data ) {
			return null ;  
		} if( 1 == data.length ) {
			return data[ 0 ] ; 
		}
		
		String desc = data[ data.length - 1 ] ;
		
		int hour = this.parseInt( desc , 0 );
		
		int toHour = hour + 1 ;
		
		desc = desc + ":00" + " ~ " + ( 10 > toHour ? "0" : "" ) + toHour + ":00";
		
		return desc ; 
	}

}