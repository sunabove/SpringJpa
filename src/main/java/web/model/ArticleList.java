package web.model;

import java.util.List;

import org.springframework.data.domain.Page;

public class ArticleList extends ArrayList<Article> { 
	
	private static final long serialVersionUID = -4059339497002917210L;

	public ArticleList() {
	}
	
	public ArticleList(Page<Article> page) {
		List<Article> list = page.getContent() ;
		if( null != list ) {
			this.addAll( list );
		}
	}
	
	public ArticleList(java.util.List<Article> list) {
		super( list );
	}

}
