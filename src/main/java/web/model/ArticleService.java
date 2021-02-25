package web.model;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ArticleService extends ServicCommon { 

	private static final long serialVersionUID = -8906663530256522388L;

	public ArticleService() {
	} 
	
	public Article saveArticleOnly( Article article ) {
		if( null != article ) { 
			article = this.articleRepository.save( article );
		}
		
		return article;
	}
	
	public Article saveArticleCreateIfNotExist( Article article, HttpServletRequest request ) {
		Long   article_id 		= this.parseLong( request.getParameter( "article_id" ) ); 
		String article_notice 	= request.getParameter( "article_notice" );
		String article_title 	= request.getParameter( "article_title" );
		String article_content 	= request.getParameter( "article_content" );
		String article_type		= request.getParameter( "article_type" ); 
		
		if( null == article && isValid( article_id ) ) {
			article = this.articleRepository.findByArticleId( article_id ); 
		} else if( null == article && isEmpty( article_id ) ) {
			article = new Article();
		}
		
		if( null == article ) {
			article = new Article();
		} 
		
		if( null == article.board ) {
			var board = this.getBoardCreateIfNotExist( 0L , request );
			
			article.board = board ;
		}
		
		if( isValid( article_title ) ) {
			article.title = article_title ; 
		}
		
		article.type = isEmpty( article_type ) ? "TXT" : article_type ;
		
		article.content = article_content ;
		
		if( isValid( article_notice ) ) { 
			article.notice = -1 < "1,on".indexOf( article_notice ) ; 
		} else {
			article.notice = false ; 
		}
		
		if( null == article.writer ) {
			article.writer = this.getLoginUser(request);
		}
		
		article.updateUpUser(request);
		
		article.saveDt = this.getNow();
		
		article = this.articleRepository.save( article );
		
		return article ;
	} 
	
	public Article deleteArticle( Article article, HttpServletRequest request ) {
		Long article_id = this.parseLong( request.getParameter( "article_id" ) ); 
		
		if( null == article && isValid( article_id ) ) {
			article = this.articleRepository.findByArticleId( article_id ); 
		}      
		
		if( null != article ) { 
			article.deleted = true ; 
			article.updateUpUser(request);
			
			article = this.articleRepository.save( article );
		}
		
		return article ;
	} 
	
	public Article getNoticeArticleCreateIfNotExist( HttpServletRequest request ) {
		var notice = true ;
		var deleted = false ; 
		Article article = this.articleRepository.findFirstByNoticeAndDeletedOrderBySaveDtDesc( notice, deleted );
		
		if( null == article ) {
			article = new Article();
			
			article.notice = true ; 
			article.board = this.getBoardCreateIfNotExist( sysConfig.defaultBoardId , request );
			article.title = "PSDR FDW 홈페이지에 오신 것을 환영합니다." ;  
			article.content = "PSDR FDW 홈페이지에 오신 것을 진심으로 환영합니다."; 
			article.writer = this.getDefaultSuperUser(request);
			
			article.updateUpUser(request);
			
			if( null == article.upUser ) {
				article.upUser = this.getDefaultSuperUser(request);
			}
			
			article = this.articleRepository.save( article );
			
			log.info( "save article = " + article );
		}
		
		return article;
	}
	
	public Board getBoardCreateIfNotExist( Long boardId , HttpServletRequest request ) {
		if( isEmpty( boardId ) ) {
			boardId = 0L ; 
		}
		
		var board = this.boardRepository.findByBoardId( boardId );
		
		if( null == board ) {
			board = new Board();
			board.boardId = boardId ;
			board.name = "게시판" ; 
			board.updateUpUser( request );
			
			board = this.boardRepository.save( board );
		}
		
		return board; 
	}
	
	public void createTestData( HttpServletRequest request ) {
		log.info( "createTestData" );
		
		this.getNoticeArticleCreateIfNotExist( request );
		
		var count = this.articleRepository.count();
		
		log.info( "count = " + count );
		
		if( 200 > count ) {
			Board board = this.getBoardCreateIfNotExist( sysConfig.defaultBoardId , request );
			
			for( long i = 1 , iLen = 180 - count ; i < iLen ; i ++ ) {
				Article article = new Article(); 
				
				article.notice = 0 == i ; 
				if( 1 > i ) {
					article.title = "PSDR FDW 홈페이지에 오신 것을 환영합니다." ; 
				} else { 
					article.title = "test_" + ( i < 10 ? "0" : "" ) + i ;
				}
				article.content = "test_" + ( i < 10 ? "0" : "" )  + i + "conent" ; 
				article.board = board ; 
				article.writer = this.getDefaultSuperUser(request);
				
				article.updateUpUser(request);
				
				article = this.articleRepository.save( article );
				
				log.info( "save article = " + article );
			}
		}
		
	}

}