package web.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DbFileRepository extends PagingAndSortingRepository<DbFile, String> {
	
	DbFile findByFileId(String fileId);
	
	DbFile findByFileNo(String fileNo);
	
	DbFile findFirstByGubunCodeOrderByUpDtDesc( String gubunCode );
	
	Page<DbFile> findAllByGubunCodeAndDeletedAndFileExtInOrderByFileModDtDescFileName( String gubunCode , boolean deleted, List<String> fileExts, Pageable pageable );
	
	Page<DbFile> findAllByGubunCodeAndFileModDtLessThanEqualAndDeletedAndFileExtInOrderByFileModDtDescFileName( String gubunCode , Timestamp upDt, boolean deleted, List<String> fileExts, Pageable pageable );
	
	//findByEndLessThanEqual 
	
}