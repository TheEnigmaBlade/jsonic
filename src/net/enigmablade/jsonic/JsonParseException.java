package net.enigmablade.jsonic;

/**
 * An exception thrown if there was a problem during JSON parsing.
 * 
 * @author Enigma
 */
public class JsonParseException extends JsonException
{
	protected enum Type { BAD_START, BAD_END, INVALID_FORMAT, INVALID_CHAR, UNKNOWN_VALUE_TYPE };
	
	private Type type;
	private int location;
	private Object thing;
	
	protected JsonParseException(Type type, int location)
	{
		this.type = type;
		this.location = location;
	}
	
	protected JsonParseException(Type type, Object thing)
	{
		this.type = type;
		this.thing = thing;
	}
	
	protected JsonParseException(Type type, int location, Object thing)
	{
		this.type = type;
		this.location = location;
		this.thing = thing;
	}
	
	/**
	 * Returns the message associated with the exception.
	 * @return The exception message
	 */
	@Override
	public String getMessage()
	{
		switch(type)
		{
			case BAD_START: return "Invalid element start at location "+location;
			case BAD_END: return "Invalid element terminator at location "+location;
			case INVALID_FORMAT: return "Invalid element format at location "+location;
			case INVALID_CHAR: return "Invalid control character '"+thing+"' at location "+location;
			case UNKNOWN_VALUE_TYPE: return "Unknown type of value \""+thing+"\"";
			
			default: return null;
		}
	}
}
