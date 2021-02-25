package com.bbs.model; 
import java.sql.Timestamp;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter; 

@Entity 
@Table(name = "article_tbl")

public class Article extends EntityCommon { 

	private static final long serialVersionUID = 7669363100960406954L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column( updatable = false, nullable = false)
	@Getter @Setter public Long articleId ;
	
	@ManyToOne
    @JoinColumn( name="board_id" ) 
	@Getter @Setter public Board board ;
	
	@OneToOne
	@Getter @Setter public User writer ;
	
	@Column( name="is_notice" )
	@Getter @Setter public Boolean notice ; 
	
	@Getter @Setter public String title ;
	
	@Lob
	@Getter @Setter public String content ; 
	
	@Column(name = "content_type")
	@Getter @Setter public String type = "TXT" ;
	
	@Getter @Setter public Integer viewCount = 0 ;
	
	@Getter @Setter public Timestamp saveDt ;
	
	public Article() {
	}
	
	public boolean isReadonly( HttpServletRequest request ) {
		return this.isReadOnly( request );
	}
	
	public boolean isReadOnly( HttpServletRequest request ) {
		return ! this.isUpdatable(request);
	}
	
	public boolean isUpdatable( HttpServletRequest request ) {
		if( null == this.articleId ) {
			return true ; 
		}
		
		User loginUser = this.getLoginUser(request); 
		
		if( null == loginUser ) {
			return false ; 
		} else if( null != loginUser && loginUser.isAdmin() ) {
			return true ; 
		}
		
		User upUser = this.upUser ; 
		
		if( null == upUser ) {
			return false ; 
		} else if( upUser == loginUser || this.isEqualString( upUser.userId, loginUser.userId ) ) {
			return true ; 
		}
		
		return false ; 
	}
	
	public boolean isCancellable( HttpServletRequest request ) {
		if( null == this.articleId ) {
			return false ; 
		}
		
		return this.isUpdatable(request); 
	}
	
	public boolean isDeletable( HttpServletRequest request ) {
		if( null == this.articleId ) {
			return false ; 
		}
		
		return this.isUpdatable(request); 
	}
	
	public String getWriterId() {
		if( null != this.writer ) {
			return this.writer.userId ;
		}
		return null;
	}
	
	public String getTitleFormat( int maxSize ) {
		String title = this.title ; 
		
		if( null == title ) {
			return title; 
		}
		
		var endIndex = maxSize - 4 ; 
		
		if( maxSize < title.length() && 0 < endIndex ) {
			
			title = title.substring( 0 , endIndex );
			title += " ...";
		}
		
		return title;
	}
	
}