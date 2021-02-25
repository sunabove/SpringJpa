package web.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleRepository extends PagingAndSortingRepository<Article, String> {
	
	Article findByArticleId(Long articleId);   
	
	Article findFirstByArticleIdLessThanAndDeletedOrderByArticleIdDesc( Long articleId, Boolean deleted );
	
	Article findFirstByArticleIdGreaterThanAndDeletedOrderByArticleIdAsc( Long articleId, Boolean deleted );
	
	Article findFirstByNoticeAndDeletedOrderBySaveDtDesc( Boolean notice , Boolean deleted );
	
	Page<Article> findAllByTitleContainingAndDeletedOrderByNoticeDescSaveDtDescArticleIdAsc(String title, Boolean deleted, Pageable pageable); 
	
}