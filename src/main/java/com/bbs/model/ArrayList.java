package com.bbs.model;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

public class ArrayList<T extends EntityCommon> extends java.util.ArrayList<T>{

	private static final long serialVersionUID = -2280116744144438187L;

	public ArrayList() {
		super();
	}

	public ArrayList(Collection<? extends T> c) {
		super(c);
	}

	public ArrayList(int initialCapacity) {
		super(initialCapacity);
	}
	
	public final Integer getTotalRowCount() {
		return this.size() ;
	}
	
	public final int getSize() {
		return this.size() ; 
	}
	
	public final T get( int index ) {		
		if( 0 > index ) {
			index = this.size() - index ;
		}
		
		if( 0 > index ) {
			return null ; 
		}
		
		return super.get( index );
	}
	
	public Integer parseInt( String text, Integer def ) {
		try { 
			Double d = Double.parseDouble( text );
			
			return d.intValue();
		} catch ( Exception e ) {
			return def; 
		} 
	}
	
	public void setRowNumbers( HttpServletRequest request ) {
		this.setRowNumbers(request, 10);
	}
	
	public void setRowNumbers( HttpServletRequest request , Integer size ) {
		Integer page = this.parseInt( request.getParameter( "page"), 0 );
		
		if( null == size ) {
			size = this.parseInt( request.getParameter( "size"), 10 );
		}
		
		int index = 1 ; 
		for( EntityCommon entity : this ) {
			entity.setRowNumer( page*size + index );
			index ++ ; 
		}
	}
	
	public int [] getPageList( Page<?> page ) {
		if( null == page ) {
			return this.getPageList( 1, 1 );
		} else {
			return this.getPageList(page.getNumber(), page.getTotalPages() );
		} 
	}
	
	public int [] getPageList( String currPage ) {
		Integer page = this.parseInt(currPage, 0) ; 
		
		return this.getPageList( page, null );
	}
	
	public int [] getPageList( Integer page , Integer maxPage ) {		
		page = null == page ? 0 : page ; 
		
		page = page < 0 ? 0 : page ;
		
		int from = (page/10)*10;
		int to = from + 9 ; 
		
		if( null != maxPage ) {
			to = Math.min( to,  maxPage );
		}
		
		if( to <= from ) {
			to = from + 1 ; 
		}
		
		int [] pages = new int[ to - from ] ;
		
		for( int i = 0 , iLen = pages.length ; i < iLen ; i ++ ) {
			pages[ i ] = from + i ; 
		}
		
		return pages;
	}
	
	public int [] getPageEmptyRowSequence( ) {
		return this.getPageEmptyRowSequence( 10 );
	}
	public int [] getPageEmptyRowSequence( String minSizeText ) {
		Integer minSize = this.parseInt( minSizeText, 10 );
		
		return this.getPageEmptyRowSequence( minSize );
	}
	
	public int [] getPageEmptyRowSequence( int minSize ) {
		minSize = 1 > minSize ? 10 : minSize ;
		
		int size = this.size() ; 
		
		size = minSize - size ; 
		
		size = size < 0 ? 0 : size ; 
		
		int [] seq = new int[ size ] ;
		
		for( int i = 0, iLen = size ; i < iLen ; i ++ ) {
			seq[ i ] = i ;
		}
		
		return seq;
	}
	
	public ArrayList<T> getListByOffsetSkip( int offset, int skip ) {
		offset = 0 > offset ? 0 : offset ;
		skip = 1 > skip ? 1 : skip ; 
		
		int i = offset ;
		
		ArrayList<T> list = new ArrayList<>();
		
		final int size = this.size() ; 

		while( i < size ) {
			list.add( this.get( i ) ) ; 
			i += skip ; 
		}
		
		return list ;  
	}
}
