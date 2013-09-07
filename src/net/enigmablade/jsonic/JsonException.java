package net.enigmablade.jsonic;

/**
 * A generic exception thrown with messing with JSON.
 * 
 * @author Enigma
 */
public class JsonException extends Exception
{
	public JsonException()
	{
		super();
	}
	
	public JsonException(String message)
	{
		super(message);
	}
}
