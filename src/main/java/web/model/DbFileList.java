package web.model;

import java.util.List;

import org.springframework.data.domain.Page;

public class DbFileList extends ArrayList<DbFile> {  

	private static final long serialVersionUID = -2088360923277869862L;

	public DbFileList() {
	}
	
	public DbFileList(Page<DbFile> page) {
		List<DbFile> list = page.getContent() ;
		if( null != list ) {
			this.addAll( list );
		}
	}
	
	public DbFileList(java.util.List<DbFile> list) {
		super( list );
	}

}
