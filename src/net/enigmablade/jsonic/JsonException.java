package net.enigmablade.jsonic;

/**
 * A generic exception thrown with messing with JSON.
 * 
 * @author Enigma
 */
public class JsonException extends RuntimeException
{
	private static final long serialVersionUID = -3723591975062307468L;
	
	/**
	 * Create a basic and non-special JsonException. How boring!
	 */
	public JsonException()
	{
		super();
	}
	
	/**
	 * Create a basic JsonException with a message. Better than the other constructor!
	 * @param message The exception message
	 */
	public JsonException(String message)
	{
		super(message);
	}
}
