package com.bbs.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public abstract class ComObject implements Serializable {
	
	private static final long serialVersionUID = -1306037807300118182L;

	public static final String LINE = "#####################################################################################################################";
	
	public boolean isEqualString( String a , String b ) {
		if( null == a || null == b ) {
			return a == b ; 
		} else if( null != a ) {
			return a.equals( b ) ; 
		}

		return false ; 
	} 
	
	public int getCurrYear() {
		
		int currYear = 1900 + new java.util.Date().getYear() ; 
		
		return currYear ; 
		
	}
	
	private static final SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final SimpleDateFormat currHourFormat = new SimpleDateFormat("yyyy-MM-dd HH");
	
	public String getTodayText() {
		return todayFormat.format( this.getNow() );
	}
	
	public String getCurrHourText() {
		return currHourFormat.format( this.getNow() );
	}

	public Timestamp getNow() {
		
		return new Timestamp( System.currentTimeMillis() );
		
	}
	
	public Timestamp getDateAfterDays( Integer years, Integer months, Integer days ) {
		
		return getDateAfterDays( this.getNow() , years, months, days );
		
	}
	
	public Timestamp getDateAfterDays( Timestamp date, Integer years, Integer months, Integer days  ) {
		
		if( date == null ) {
			return null;
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime( date );
		
		if( null != years ) { 
			c.add( Calendar.YEAR, years) ;
		}
		
		if( null !=  months ) { 
			c.add( Calendar.MONTH, months );
		}
		
		if( null != days ) { 
			c.add( Calendar.DATE, days );
		}

		Timestamp timestamp = new Timestamp( c.getTimeInMillis() );
		
		return timestamp;
		
	}
	
	public boolean equals( Number i, Number k ) {
		
		return i != null && k != null && i.equals( k );
		
	} 
	
	public String getAlphanumeric( String a, String b) {
		
		if( this.isAlphanumeric(a)) {
			return a;
		} else if( this.isAlphanumeric( b ) ) {
			return b;
		} else {
			return a;
		}
		
	} 
	
	public boolean isAlphanumeric( String text ) {
		
		return StringUtils.isAsciiPrintable( text );
		
	}
	
	public String removeSqlWildcardCharacters( String text ) {
		
		text = text.replaceAll( "%", "" );  
		text = text.replaceAll( "\\?", "" );
		
		return text ;
		
	}
	
	public boolean isDateDiffLessMonth( String dateDiff , final int month ) {
		
		boolean isDiff = true ; 
		
		if( dateDiff == null ) {
			isDiff = true;
		} else if( dateDiff != null && dateDiff.startsWith( "-" ) ) { 
			isDiff = true; 
		} else if( dateDiff != null ) {
			dateDiff = dateDiff.trim();
			
			String hms [] = dateDiff.split( ":" );
			
			int h = 0; 
			
			if( hms != null && hms.length > 0 ) {
				h = this.parseInt( hms[0] , 0 );
			}
			
			int mon = h/(24*30); 
			
			isDiff = ( mon < month ) ; 
		} 

		return isDiff ;
		
	}
	
	public boolean isValid( Object obj ) {
		if( null == obj ) {
			return false ; 
		} 
		
		String text = null ;
		
		if( obj instanceof String ) { 
			text = (String) obj ; 
		} else if( obj instanceof StringBuffer ) {
			text = obj.toString() ; 
		} else if( obj instanceof Collection ){
			Collection<?> collection = (Collection<?>) obj ;
			
			return 0 < collection.size() ; 
		} else {
			text = obj.toString() ; 
		}
		
		return text != null && text.trim().length() > 0 ;
	}
	
	public boolean isOrValid( Object ... objs ) {
		if( null == objs ) {
			return false ; 
		}
		
		for( Object obj : objs ) {
			if( this.isValid( obj ) ) {
				return true ; 
			}
		}
		
		 return false ;
	} 
	
	public boolean isValid( Object ... objs ) {
		return this.isAndValid( objs );
	}
	
	public boolean isAndValid( Object ... objs ) {
		if( null == objs ) {
			return false ; 
		}
		
		for( Object obj : objs ) {
			if( ! this.isValid( obj ) ) {
				return false ; 
			}
		}
		
		 return true ;
	} 
	
	public boolean isValid( ArrayList<?> list ) {
		
		return null != list && 0 < list.size() ; 
	}
	
	public boolean isEmpty( Object obj ) {
		return ! this.isValid( obj ); 
	}
	
	public boolean isEmpty( ArrayList<?> list ) {
		return ! this.isValid( list ); 
	} 
	
	public Double parseDouble( String text ) {
		return parseDouble( text, null );
	}
	
	public Double parseDouble( String text, Double def ) {
		try {
			return Double.parseDouble( text.trim() );
		} catch ( Exception e) {
			return def ; 
		}
	}
	
	public Long parseLong( String text ) {
		return parseLong( text, null );
	}
	
	public Long parseLong( String text, Long def ) {
		try {
			return Long.parseLong( text.trim() );
		} catch ( Exception e) {
			return def ; 
		}
	}
	
	public Integer parseInt( String text ) {
		return this.parseInt( text, null );
	}
	
	public Integer parseInt( String text, Integer def ) {
		Double d = parseDouble( text );
		
		if( d != null ) {
			return d.intValue() ;
		} else {
			return def ;
		}
	}
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	public java.sql.Date parseDate(String dateText) {
		try {
			dateText = null == dateText ? "" : dateText.trim() ; 
			
			if( 1 > dateText.length() ) {
				return null ; 
			}
			
			SimpleDateFormat format = ComObject.dateFormat ;
			
			if (dateText.contains("/")) {
				format = new SimpleDateFormat("MM/dd/yyyy");
			} else if (dateText.contains("-")) {
				format = new SimpleDateFormat("yyyy-MM-dd");
			} else if( dateText.length() == 8 ) {
				format = new SimpleDateFormat("yyyyMMdd");
			} else if( dateText.length() == 6 ) {
				format = new SimpleDateFormat("yyyyMM");
			} else if( dateText.length() == 4 ) {
				format = new SimpleDateFormat("yyyy");
			} else if( dateText.length() == 2 ) {
				format = new SimpleDateFormat("yy");
			}
			
			java.util.Date date = format.parse( dateText );
			
			java.sql.Date sqlDate = new java.sql.Date( date.getTime() );
			
			return sqlDate;
			
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
	} 
	
	public Timestamp parseTimestamp( String dateText ) {
		
		java.sql.Date sqlDate = this.parseDate( dateText ) ;
		
		if( null == sqlDate ) {
			
			return null ; 
			
		} else { 
		
			java.sql.Timestamp timeStamp = new Timestamp( sqlDate.getTime() );
			
			return timeStamp ; 
		}
		
	}
	
	public String getCreatedUuid() { 
		return this.createUuid(); 
	}
	
	public String createUuid() {
		String uuid = UUID.randomUUID().toString();

		if (null == uuid) {
			return "";
		} else {
			uuid = uuid.replaceAll("-|\\{|\\}", "");

			uuid = uuid.toUpperCase();

			return uuid;
		}
	}
	
	public String format(String format, Object... args) {
        return String.format(format, args) ;
    }
	
	public String f( String format, Object ... args ) {
		return this.format( format, args);
	}

}
