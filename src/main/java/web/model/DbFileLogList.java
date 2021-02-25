package web.model;

public class DbFileLogList extends ArrayList<DbFileLog> {  

	private static final long serialVersionUID = 7987756610062459543L;

	public DbFileLogList() {
	}
	
	public double getDownloadCountAvg() {
		int size = this.size() ; 
		
		return this.getDownloadCountSum()/( size + 0.0 );
	}
	
	public double getAccessCountAvg() {
		int size = this.size() ; 
		
		return this.getAccessCountSum()/( size + 0.0 );
	}
	
	public int getDownloadCountSum() {
		int sum = 0 ;
		
		for( DbFileLog item : this ) {
			if( null != item ) { 
				sum += item.downloadCount ; 
			}
		}
		
		return sum;  
	}
	
	public int getAccessCountSum() {
		int sum = 0 ;
		
		for( DbFileLog item : this ) {
			if( null != item ) { 
				sum += item.accessCount ; 
			}
		}
		
		return sum;  
	}

}
