package web;

import java.beans.PropertyEditorSupport;

import web.gson.DateDeserializer; 

public class CustomTimestampEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue( DateDeserializer.parseDateUsingNatty( text ) );
	}

	@Override
	public String getAsText() throws IllegalArgumentException {
		
		Object value = getValue();
		
		return null == value ? "" : "" + value ; 
	}
}