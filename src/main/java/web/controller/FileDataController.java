package web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import web.model.DbFile;
import web.model.DbFileList;
import web.model.DbFileLog;
import web.model.DbFileRepository;

@RequestMapping("/data")
@Controller
@Slf4j
public class FileDataController extends ComController {

	private static final long serialVersionUID = 3131679599458841886L;
	
	private static int TEMP_FILE_ID = 1 ; 

	public FileDataController() {
		this.loginRequire = true;
	}

	// dataList
	@RequestMapping(value = { "index.html", "main.html", "down.html", "list.html" })
	public String dataList(HttpServletRequest request, 
			@PageableDefault(size = 20) Pageable pageable ,
			@RequestParam( value="search_date" , required = false ) @DateTimeFormat(pattern="yyyy-MM-dd") Timestamp search_date 
		) {
		var loginRequire = true;

		String forward = this.processRequest(request, loginRequire);

		String user_id = request.getParameter("user_id");

		if (isValid(user_id) || this.isValid(forward)) {
			return forward;
		}

		this.dbFileService.checkPsDrFileList(request);

		String gubun_code = request.getParameter("gubun_code");

		if (isEmpty(gubun_code)) {
			gubun_code = "Comtrade";
		} 
		
		boolean isComtrade = "Comtrade".equalsIgnoreCase( gubun_code ) ; 
		
		Page<DbFile> dbFilePage = null ;		
		int deleteCount = 1 ;
		
		while( 0 < deleteCount ) {
			dbFilePage = this.searchDbFileList(request, search_date, pageable);
			
			deleteCount = this.dbFileService.deleteIfNotExist(request, dbFilePage);
		}
		
		DbFileList dbFileList = new DbFileList( dbFilePage );
		
		if (null != dbFileList) { 
			Integer size = null ;
			if( null != pageable ) {
				size = pageable.getPageSize();
			}
			if( null == size ) {
				size = 20 ; 
			} 
			
			dbFileList.setRowNumbers(request, size);
			
			if( isComtrade ) {
				for( DbFile dbFile : dbFileList ) {
					DbFileRepository dbFileRepository = this.dbFileRepository ;
					String pairFileId = dbFile.fileId ;
					int index = pairFileId.lastIndexOf( "." );
					if( -1 < index ) { 
						pairFileId = pairFileId.substring( 0, index ) + ".cfg" ; 
						DbFile pairDbFile = dbFileRepository.findByFileId( pairFileId ) ;
						if( null != pairDbFile ) {
							dbFile.setPairDbFile(pairDbFile); 
						}
					}			
				}
			}
		}

		request.setAttribute("gubun_code", gubun_code);
		request.setAttribute("page", dbFilePage);
		request.setAttribute("dbFilePage", dbFilePage);
		request.setAttribute("dbFileList", dbFileList);
		request.setAttribute("dbFiles", dbFileList); 

		return "210_data_list.html";
	} 
	// -- dataList
	
	// searchDbFileList
	private Page<DbFile> searchDbFileList(HttpServletRequest request, Timestamp search_date, Pageable pageable) {
		var debug = true ; 
		String gubun_code = request.getParameter("gubun_code");

		if (isEmpty(gubun_code)) {
			gubun_code = "Comtrade";
		} 
		
		Page<DbFile> dbFilePage = null;
		
		List<String> fileExts = new java.util.ArrayList<>();
		fileExts.add( "DAT" );
		fileExts.add( "TRGL" );
		
		boolean deleted = false ; 
		
		if( null == search_date ) { 
			dbFilePage = this.dbFileRepository.findAllByGubunCodeAndDeletedAndFileExtInOrderByFileModDtDescFileName(gubun_code, deleted, fileExts, pageable);
		} else if( null != search_date ) {
			if( debug ) {
				log.info( "LINE" );
				log.info( "search_date org = " + search_date );
				search_date = this.getDateAfterDays( search_date, 0, 0, 1 );
				search_date.setHours( 0 );
				search_date.setMinutes( 0 );
				search_date.setSeconds( 0 );
				log.info( "search_date new = " + search_date );
				log.info( "LINE" );
			}
			dbFilePage = this.dbFileRepository.findAllByGubunCodeAndFileModDtLessThanEqualAndDeletedAndFileExtInOrderByFileModDtDescFileName(gubun_code, search_date, deleted, fileExts, pageable);
		}
		
		return dbFilePage; 
	}
	// -- searchDbFileList
	
	// downloadZipFile
	@GetMapping("/download_zip/{file_no:.+}")
	public ResponseEntity<Resource> downloadZipFile(@PathVariable String file_no, HttpServletRequest request) throws Exception {
		var debug = true ; 
		
		DbFile dbFile = null ; 
		DbFile pairDbFile = null ; 

		if (this.isValid( file_no )) {
			dbFile = this.dbFileRepository.findByFileNo( file_no );
			if (null != dbFile) {
				String pairFileId = dbFile.fileId ;
				int index = pairFileId.lastIndexOf( "." );
				if( -1 < index ) {
					pairFileId = pairFileId.substring( 0, index ) + ".cfg" ; 
					
					DbFileRepository dbFileRepository = this.dbFileRepository ;
					
					pairDbFile = dbFileRepository.findByFileId( pairFileId ) ;
					if( null != pairDbFile ) {
						dbFile.setPairDbFile(pairDbFile); 
					}
				}
			}
		}
		
		String zipFileName = null ; 
		
		if( null != dbFile ) {
			var fileName = dbFile.fileName ; 
			zipFileName = fileName.substring( 0, fileName.lastIndexOf( "." ) ) + ".zip" ; 
		}
		
		File file = File.createTempFile( "" + ( 100 + TEMP_FILE_ID ++ ) , ".zip" );  
		
		ArrayList<String> srcFiles = new ArrayList<>();
		
		if( null != dbFile ) {
			srcFiles.add( dbFile.fileId );
		}
		
		if( null != pairDbFile ) {
			srcFiles.add( pairDbFile.fileId );
		}
		
        FileOutputStream fos = new FileOutputStream( file );
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);
 
            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        
        zipOut.close();
        fos.close();
		
		Resource resource = new UrlResource( file.toURI() );

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType( resource.getFile().getAbsolutePath() );
		} catch (Exception ex) {
			log.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		
		var fileName = zipFileName ; 
		fileName = this.getEncodedDownLoadFileName(request, fileName);
		
		if( debug ) { 
			log.info( LINE );
			log.info( "fileName = " + fileName ); 
			log.info( LINE );
		}
		
		var totDownNo = this.getTotDownNo() ; 
		
		// set today connection user number
		if (null != totDownNo) {
			totDownNo.increaseBy(1);
			this.propService.saveProp( totDownNo );
		}
		
		DbFileLog todayDownLog = this.getTodayDownLog() ; 
		if( null != todayDownLog ) {
			todayDownLog.downloadCount += 1;
			
			this.dbFileLogService.save( todayDownLog );
		}
		
		DbFileLog currHour = this.getCurrHourDownLog();
		if( null != currHour ) { 
			currHour.downloadCount += 1;
			
			this.dbFileLogService.save( currHour );
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(resource);
	}
	// -- downloadZipFile

	// downloadFile
	@GetMapping("/download/{file_no:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String file_no, HttpServletRequest request) throws Exception {
		var debug = true ; 
		
		String filePath = null ; 
		
		DbFile dbFile = null ; 

		if (this.isValid( file_no )) {
			dbFile = this.dbFileRepository.findByFileNo( file_no );
			if (null != dbFile) {
				filePath = dbFile.filePath ; 
			}
		}
		
		File file = new File( filePath ) ; 
		
		Resource resource = new UrlResource( file.toURI() );

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType( resource.getFile().getAbsolutePath() );
		} catch (Exception ex) {
			log.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		
		var fileName = resource.getFilename() ; 
		fileName = dbFile.fileName ; 
		fileName = this.getEncodedDownLoadFileName(request, fileName);
		
		if( debug ) { 
			log.info( LINE );
			log.info( "fileName = " + fileName ); 
			log.info( LINE );
		}
		
		var totDownNo = this.getTotDownNo() ; 
		
		// set today connection user number
		if (null != totDownNo) {
			totDownNo.increaseBy(1);
			this.propService.saveProp( totDownNo );
		}
		
		DbFileLog todayDownLog = this.getTodayDownLog() ; 
		if( null != todayDownLog ) {
			todayDownLog.downloadCount += 1;
			
			this.dbFileLogService.save( todayDownLog );
		}
		
		DbFileLog currHour = this.getCurrHourDownLog();
		if( null != currHour ) { 
			currHour.downloadCount += 1;
			
			this.dbFileLogService.save( currHour );
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(resource);
	}
	// -- downloadFile

}