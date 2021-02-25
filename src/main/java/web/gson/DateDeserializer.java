package web.gson;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.joestelmach.natty.Parser;

public class DateDeserializer implements JsonDeserializer<Date> {
	private static final Logger				log		= LoggerFactory.getLogger( DateDeserializer.class );
	
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
	public static Date parseDate( String dateText ) throws JsonParseException {
		
		boolean useNatty = true ;  
		
		if( useNatty ) {
			return parseDateUsingNatty( dateText );
		} else {
			return parseDateUsingFormatter( dateText );
		}
	}
	
	public static Date parseDateUsingFormatter( String dateText ) {
		// cannot parse the data like below.
		// java.text.ParseException: Unparseable date: "Oct 18, 2007 9:56:52 AM"
		
		if( null == dateText || 1 > (dateText = dateText.trim()).length() ) {
			return null ; 
		}

		format.setTimeZone( TimeZone.getTimeZone("GMT") );

		try {
			return format.parse(dateText);
		} catch ( Exception exp) {
			exp.printStackTrace(); 
			return null;
		}
	}
	
	public static Timestamp parseDateUsingNatty( String dateText ) throws JsonParseException { 
		
		boolean dbg = true ; 
		
		if( null == dateText || 1 > (dateText = dateText.trim()).length() ) {
			return null ; 
		}

		Date date = null ; 
		
		try {
			List<Date> dates = new Parser().parse( dateText ).get(0).getDates();
	        
			if( null != dates && 0 < dates.size() ) { 
				date = dates.get( 0 );
			} 
		} catch ( Exception exp) {
			exp.printStackTrace(); 
		}
		
		if( dbg ) {
			log.info( "" );
			log.info( "Natty parser input  = " + dateText );
			log.info( "Natty parser result = " + ( null == date ? "" : date ) );
			log.info( "" );
		}
		
		if( date instanceof Timestamp ) {
			return (Timestamp) date ; 
		} else {
			return new Timestamp( date.getTime() );
		}
	}

	@Override
	public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		String dateText = element.getAsString();
		
		return parseDate( dateText );
	}
}