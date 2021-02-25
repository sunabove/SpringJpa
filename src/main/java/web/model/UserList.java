package web.model;

import java.util.List;

import org.springframework.data.domain.Page;

public class UserList extends ArrayList<User> {

	private static final long serialVersionUID = -6877377985101019251L;
	
	public UserList() {
		
	}
	
	public UserList(Page<User> page) {
		List<User> list = page.getContent() ;
		if( null != list ) {
			this.addAll( list );
		}
	}
	
	public UserList(java.util.List<User> list) {
		super( list );
	}

}
