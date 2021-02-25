package web.gson;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class TimestampDeserializer implements JsonDeserializer<Date> {
	
	@Override
	public Timestamp deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		String dateText = element.getAsString();
		
		Date date = DateDeserializer.parseDate( dateText );

		if( null == date ) {
			return null ; 
		} else if( date instanceof Timestamp ) {
			return (Timestamp) date ; 
		} else {
			return new Timestamp( date.getTime() );
		}
	}
}