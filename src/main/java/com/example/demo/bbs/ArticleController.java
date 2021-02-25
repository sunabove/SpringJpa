package com.example.demo.bbs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j; 

@RequestMapping("/article")
@Controller
@Slf4j
public class ArticleController extends ComController {

	private static final long serialVersionUID = -5704084995590809168L;

	@RequestMapping(value = { "index.html", "main.html", "list.html" })
	public String articleList(HttpServletRequest request , @PageableDefault(size = 10) Pageable pageable ) {
		var loginRequire = true;

		String forward = this.processRequest(request, loginRequire);
		
		if( this.isValid( forward ) ) {
			return forward ; 
		}
		
		String article_title_search = request.getParameter( "article_title_search" );
		
		if( null == article_title_search ) {
			article_title_search = "";
		}
		
		Page<Article> articlePage = this.articleRepository.findAllByTitleContainingAndDeletedOrderByNoticeDescSaveDtDescArticleIdAsc( article_title_search, false, pageable );
		
		ArticleList articleList = new ArticleList( articlePage );
		
		if( null != articleList ) {
			articleList.setRowNumbers(request);
		}
		
		request.setAttribute( "page", articlePage );
		request.setAttribute( "articleList", articleList );
		request.setAttribute( "articles", articleList );

		return "530_article_list.html";
	}

	// articleView
	@RequestMapping(value = { "view.html" })
	public String articleView(HttpServletRequest request, RedirectAttributes ra ) {
		var debug = true ;
		var loginRequire = true;
		String forward = this.processRequest(request, loginRequire);
		
		if( this.isValid( forward ) ) {
			ra.addAttribute( "id", request.getParameter( "id") );
			
			return forward ; 
		}
		
		Article article = null ; 
		
		Long article_id = this.parseLong( request.getParameter( "article_id" ) );
		
		if( isEmpty( article_id ) ) {
			article_id = this.parseLong( request.getParameter( "id" ) );
		}
		
		if( isValid( article_id ) ) {
			article = this.articleRepository.findByArticleId( article_id );
		}
		
		String cmd = request.getParameter( "cmd" );
		
		if( "save".equalsIgnoreCase( cmd ) ) {
			article = this.articleService.saveArticleCreateIfNotExist(article, request);
		} else if( "delete".equalsIgnoreCase( cmd ) ) {
			article = this.articleService.deleteArticle(article, request);
			
			forward = "redirect:/article/list.html";
			
		} else if( null != article ) {
			if( debug ) {
				log.info( "prev article saveDt = " + article.saveDt );
			}
			
			article.viewCount = null == article.viewCount ? 1 : article.viewCount + 1 ; 
			
			this.articleService.saveArticleOnly(article);
			
			if( debug ) {
				log.info( "curr article saveDt = " + article.saveDt );
			}
		}
		
		if( null != article && article.deleted ) {
			article = null ; 
		} 
		
		if( null == article ) {
			var loginUser = this.getLoginUser(request);
			article = new Article();
			article.updateUpUser( request );
			article.writer = loginUser ; 
			article.notice = null == loginUser ? false : loginUser.isAdmin() ; 
		} else {
			var articleId = article.articleId;
			var deleted = false ; 
			Article articlePrev = this.articleRepository.findFirstByArticleIdLessThanAndDeletedOrderByArticleIdDesc(articleId, deleted);
			Article articleNext = this.articleRepository.findFirstByArticleIdGreaterThanAndDeletedOrderByArticleIdAsc(articleId, deleted);
			
			request.setAttribute( "articlePrev", articlePrev );
			request.setAttribute( "articleNext", articleNext );
		}
		
		if( isValid( forward ) ) {
			return forward ; 
		} else { 
			request.setAttribute( "article", article );

			return "540_article_view.html";
		}
	}
	// -- articleView

}