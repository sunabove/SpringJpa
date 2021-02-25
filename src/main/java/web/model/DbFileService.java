package web.model;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import web.controller.ComController;

@Service
@Transactional
@Slf4j
public class DbFileService extends ServicCommon { 

	private static final long serialVersionUID = -1229350067174850078L;

	public DbFileService() {
	} 
	
	public int deleteIfNotExist( HttpServletRequest request , Page<DbFile> dbFilePage ) {
		var debug = true ; 
		if( debug ) { 
			log.info( "deleteIfNotExist" );
		}
		
		int count = 0 ; 
		
		java.util.List<DbFile> dbFileList = dbFilePage.getContent() ;
		
		if( null != dbFileList ) { 
			for( DbFile dbFile : dbFileList ) {
				var fileExist = dbFile.isFileExist() ; 
				if( ! fileExist ) {
					dbFile.deleted = true ; 
					
					dbFile.updateUpUser( request ); 
					
					dbFile = this.dbFileRepository.save( dbFile );
					
					count ++ ; 
				}
			}
		}
		
		return count; 
	}
	
	public void checkPsDrFileList( HttpServletRequest request ) {
		this.checkPsDrFileList( "Comtrade" , request);
		this.checkPsDrFileList( "Fault" , request);
	}
	
	private void checkPsDrFileList( String gubunCode, HttpServletRequest request ) { 
		
		File [] filesNotChecked = this.getFileListNotChecked(gubunCode);
		
		for( File file : filesNotChecked ) {
			String fileId = null;
			String filePath = null ; 
			
			try {
				fileId = file.getCanonicalPath() ;
				filePath = file.getCanonicalPath() ;
			} catch (IOException e) {  
			}
			
			if( null != fileId ) {
				DbFile dbFile = this.dbFileRepository.findByFileId( fileId );
				
				if( null == dbFile ) {
					dbFile = new DbFile();
					String fileName = file.getName();
					String fileExt = "";
					
					if( this.isValid( fileName ) ) {
						fileExt = fileName.substring( fileName.lastIndexOf( "." ) + 1 ) ;
						fileExt = fileExt.toUpperCase();
					}
					
					dbFile.fileId = fileId ; 
					dbFile.fileNo = this.createUuid();
					
					dbFile.gubunCode = gubunCode ;  
					dbFile.fileName = fileName ;
					dbFile.filePath = filePath ;
					dbFile.fileExt  = fileExt ;
					
					dbFile.updateUpUser( request );
					dbFile.fileModDt = new Timestamp( file.lastModified() );
					
					dbFile = this.dbFileRepository.save( dbFile );
				} else if( dbFile.deleted ) {
					dbFile.deleted = false ; 
					
					dbFile.updateUpUser( request );
					dbFile.fileModDt = new Timestamp( file.lastModified() );
					
					dbFile = this.dbFileRepository.save( dbFile );
				}
			}
			
		} 
	}
	
	private File [] getFileListNotChecked( String gubunCode ) {
		
		File f = new File( "/home/psdmts/PSDR-XU/" + gubunCode );

		File [] files = f.listFiles(); 

		Arrays.sort( files, new Comparator<File>()
		{
		    public int compare(File o1, File o2) {

		        if ( o1.lastModified() > o2.lastModified()) {
		            return -1;
		        } else if (o1.lastModified() < o2.lastModified()) {
		            return +1;
		        } else {
		            return 0;
		        }
		    }

		});
		
		return files ; 
	}
	
	public DbFile getSystemDbFileByFileId( String fileId , ComController controller , HttpServletRequest request ) {
		DbFile dbFile = this.dbFileRepository.findByFileId( fileId ) ; 
		
		User loginUser = controller.getLoginUser(request);
		
		if( null == dbFile ) {
			dbFile = new DbFile() ;
			dbFile.fileId = fileId ; 
			dbFile.upUser = loginUser ;
			dbFile.gubunCode = "SYS-FILE" ;
			dbFile = this.dbFileRepository.save( dbFile );
		}
		
		dbFile.upUser = loginUser ;
		
		return dbFile;
	}
	
	public DbFile saveDbFile( DbFile dbFile ) {
		if( null != dbFile ) {
			dbFile = this.dbFileRepository.save( dbFile );
		}
		
		return dbFile ; 
	}

}