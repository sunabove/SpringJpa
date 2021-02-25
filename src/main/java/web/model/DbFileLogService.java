package web.model;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional

public class DbFileLogService extends ServicCommon {  

	private static final long serialVersionUID = -4698866693431211836L;
	
	private static final String TOT_DOWN_NO = "TOT_DOWN_NO-" ; 

	public DbFileLogService() {
	} 
	
	public DbFileLog save(DbFileLog dbFileLog) {
		if( null != dbFileLog ) {
			dbFileLog = this.dbFileLogRepository.save( dbFileLog );
		}
		
		return dbFileLog ; 
	}
	
	public DbFileLog getTodayDownLog() { 
		String fileLogId = TOT_DOWN_NO  + this.getTodayText() ;
		String gubun = "DAY";
		
		return this.getDbFileLogCreateIfNotExist(fileLogId, gubun);
	}
	
	public DbFileLog getCurrHourFileLog() { 
		String fileLogId = TOT_DOWN_NO  + this.getCurrHourText();
		String gubun  = "HOUR" ; 
		
		return this.getDbFileLogCreateIfNotExist(fileLogId, gubun);
	}
	
	private DbFileLog getDbFileLogCreateIfNotExist( String fileLogId , String gubun ) {
		
		var dbFileLog = this.dbFileLogRepository.findByFileLogId(fileLogId);
		
		if( null == dbFileLog ) {
			dbFileLog = new DbFileLog();
			dbFileLog.fileLogId = fileLogId ;
			dbFileLog.gubun = gubun ; 
			dbFileLog.downloadCount = 0 ; 
			
			dbFileLog = this.dbFileLogRepository.save( dbFileLog );
		}

		return dbFileLog ;
	}

}