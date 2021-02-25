package web.model;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;

public class PageImpl<T extends EntityCommon> extends org.springframework.data.domain.PageImpl<T> { 

	private static final long serialVersionUID = 5204235340716867626L;

	public PageImpl(ArrayList<T> content) {
		super(content); 
	}
	
	public PageImpl(ArrayList<T> content, Pageable pageable, long total) {
		super(content, pageable, total); 
	}
	
	public ArrayList<T> getContent() {
		List<T> list = super.getContent();
		
		ArrayList<T> arrayList = new ArrayList<>();
		
		for( T item : list ) {
			arrayList.add( item );
		}

		return arrayList ;
	}
	
	public void setRowNumbers( HttpServletRequest request ) {
		ArrayList<T> list = this.getContent() ;
		if( null != list ) {
			list.setRowNumbers(request);
		}
	}
	
	public void setRowNumbers( HttpServletRequest request, Integer size ) {
		ArrayList<T> list = this.getContent() ;
		if( null != list ) {
			list.setRowNumbers(request, size);
		}
	}
	
}
