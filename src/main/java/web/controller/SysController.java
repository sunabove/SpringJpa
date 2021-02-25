package web.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import web.model.*;

@RequestMapping("/sys")
@Controller
@Slf4j
public class SysController extends ComController {

	private static final long serialVersionUID = 7435308736508795619L;

	public SysController() {
		this.loginRequire = true ;
		this.adminRequire = true ; 
	}

	@RequestMapping(value = { "index.html", "main.html", "monitor.html" })
	public String sysMonitor(HttpServletRequest request, RedirectAttributes ra) {
		var loginRequire = this.loginRequire ;
		var adminRequire = true ; 

		String forward = this.processRequest(request, loginRequire, adminRequire, ra );

		if (null != forward) {
			return forward;
		}
		
		String search_date = request.getParameter( "search_date" );
		
		if( isEmpty( search_date) ) {
			search_date = this.getTodayText();
		}
		
		final String gubun = "HOUR";
		final String fileLogId = "TOT_DOWN_NO-" + search_date ; 
		
		DbFileLogList dbFileLogListOrg = this.dbFileLogRepository.findAllByGubunAndFileLogIdStartingWithOrderByFileLogIdAsc(gubun, fileLogId);
		
		HashMap<String, DbFileLog> hashMap = new HashMap<>();
		for( DbFileLog dbFileLog : dbFileLogListOrg ) {
			var fileLogIdTemp = dbFileLog.fileLogId ; 
			if( null != fileLogIdTemp ) { 
				hashMap.put( fileLogIdTemp, dbFileLog );
			}
		}
		
		DbFileLogList dbFileLogList = new DbFileLogList() ;
		
		for( int i = 0, iLen = 24 ; i < iLen ; i ++ ) {
			String fileLogIdTemp = fileLogId + " %02d" ;
			fileLogIdTemp = String.format( fileLogIdTemp, i );
			
			DbFileLog dbFileLog = hashMap.get( fileLogIdTemp );
			if( null == dbFileLog ) {
				dbFileLog = new DbFileLog();
				dbFileLog.fileLogId = fileLogIdTemp ; 
			} 
			
			dbFileLogList.add( dbFileLog );
		}
		
		request.setAttribute( "dbFileLogList", dbFileLogList );
		request.setAttribute( "dbFileLogs", dbFileLogList );
		
		request.setAttribute( "search_date", search_date );

		return "420_sys_monitor.html";
	}

	@RequestMapping("user_stat.html")
	public String userStat(HttpServletRequest request, RedirectAttributes ra) {
		var loginRequire = this.loginRequire ;
		var adminRequire = true ; 

		String forward = this.processRequest(request, loginRequire, adminRequire, ra );

		if (null != forward) {
			return forward;
		}

		return "430_user_sys_stat.html";
	}
	
	@PostMapping("setting.html")
	public String settingByPost(HttpServletRequest request,
			@RequestParam( value="sys_bg_img_01_file", required=false ) MultipartFile sys_bg_img_01_file,
			@RequestParam( value="sys_bg_img_02_file", required=false ) MultipartFile sys_bg_img_02_file,
			RedirectAttributes ra ) {
		var debug = true ;
		if( debug ) {
			log.info( "LINE" );
			log.info( "settingByPost(...)" );
			log.info( "LINE" );
		}
		
		var loginRequire = this.loginRequire ;
		var adminRequire = true ; 

		String forward = this.processRequest(request, loginRequire, adminRequire, ra );
		
		if (null != forward) {
			return forward;
		}
		
		if( null != sys_bg_img_01_file ) {
			DbFile dbFile = this.getSysBgImg_01(request);
			
			MultipartFile file =  sys_bg_img_01_file ;
			
			dbFile.fileName = file.getName();
			try { 
				dbFile.content = file.getBytes();
			} catch( Exception e ) {
				dbFile.content = null ; 
			}
			
			this.dbFileService.saveDbFile( dbFile );
		}
		
		if( null != sys_bg_img_02_file ) {
			DbFile dbFile = this.getSysBgImg_02(request);
			
			MultipartFile file =  sys_bg_img_02_file ;
			
			dbFile.fileName = file.getName();
			try { 
				dbFile.content = file.getBytes();
			} catch( Exception e ) {
				dbFile.content = null ; 
			}
			
			this.dbFileService.saveDbFile( dbFile );
		}
		
		return "redirect:/sys/setting.html";
	}

	@RequestMapping("setting.html")
	public String settingByGet(HttpServletRequest request, RedirectAttributes ra ) {
		
		var loginRequire = this.loginRequire ;
		var adminRequire = true ; 

		String forward = this.processRequest(request, loginRequire, adminRequire, ra );

		if (null != forward) {
			return forward;
		}

		// system name properties
		Prop sysName_01 = propService.getProp("SYS_NAME_01", "경기 지역 본부");
		Prop sysName_02 = propService.getProp("SYS_NAME_02", "성남 전력 지사");
		Prop sysName_03 = propService.getProp("SYS_NAME_03", "154KV 중원변전소");

		var rowCount = 0  ; 
		
		if (true) {
			var sysName_01_txt = request.getParameter("sysName_01");
			var sysName_02_txt = request.getParameter("sysName_02");
			var sysName_03_txt = request.getParameter("sysName_03");
			
			if( isValid( sysName_01_txt ) ) {
				sysName_01.value = sysName_01_txt ; 
				
				this.propService.saveProp( sysName_01 );
				
				rowCount ++ ;
			}
			
			if( isValid( sysName_02_txt ) ) {
				sysName_02.value = sysName_02_txt ; 
				
				this.propService.saveProp( sysName_02 );
				
				rowCount ++ ;
			}
			
			if( isValid( sysName_03_txt ) ) {
				sysName_03.value = sysName_03_txt ; 
				
				this.propService.saveProp( sysName_03 );
				
				rowCount ++ ;
			}
		}

		if (true) {

			if (this.isEmpty(request.getParameter("sysName_01"))) {
				request.setAttribute("sysName_01_txt", sysName_01.value);
			}

			if (this.isEmpty(request.getParameter("sysName_02"))) {
				request.setAttribute("sysName_02_txt", sysName_02.value);
			}

			if (this.isEmpty(request.getParameter("sysName_03"))) {
				request.setAttribute("sysName_03_txt", sysName_03.value);
			}
		}
		
		if( 0 < rowCount ) {
			return "redirect:/sys/setting.html" ; 
		} else { 
			return "440_sys_setting.html";
		}
	}

}