package web.model;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface DbFileLogRepository extends PagingAndSortingRepository<DbFileLog, String> {
	
	DbFileLog findByFileLogId(String fileLogId);
	
	DbFileLog findByDownloadFile( DbFile downloadFile ); 
	
	DbFileLogList findAllByGubunAndFileLogIdStartingWithOrderByFileLogIdAsc(String gubun, String fileLogId);
	
}