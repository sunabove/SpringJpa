package com.example.demo.model;

import java.io.File;
import java.sql.Timestamp;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "db_file_tbl"
 , indexes = { @Index(name = "file_no_idx", columnList = "file_no") ,
		 @Index(name = "file_no_ext", columnList = "file_ext")
 		}
)
@Data @EqualsAndHashCode(callSuper=false)
public class DbFile extends EntityCommon { 
	private static final long serialVersionUID = -8745797345335307150L;

	@Id	@Column( length = 191 )
	public String fileId ;

	@Column( length = 191 )
	public String gubunCode ;
	
	@Column( name="file_no", length = 191 )
	public String fileNo ;

	@Column(length=191)
	public String fileName ;
	
	@Column(length=1000)
	public String filePath ;
	
	@Column( name="file_ext", length=191)
	public String fileExt ;
	
	public Timestamp fileModDt ;

	@Lob
	public byte [] content;
	
	public transient DbFile pairDbFile ;

	public DbFile() {
	}
	
	public String getPairFileNo() {
		DbFile pairDbFile = this.pairDbFile ;
		
		if( null == pairDbFile ) {
			return null ; 
		} else {
			return pairDbFile.getFileNo() ;
		}
	}
	
	public boolean isFileExist() {
		String filePath = this.filePath ;
		
		if( isEmpty( filePath ) ) {
			return false ; 
		}
		
		var valid = false ; 
		
		File file = new File( filePath );
		
		valid = file.exists();
		
		return valid; 
	}
	
	public String getSysFileUrl() {
		return "/file/sys/" + fileId ; 
	}

}