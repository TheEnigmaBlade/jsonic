package net.enigmablade.jsonic;

/**
 * An exception thrown if there was a problem with value type conversion.
 * 
 * @author Enigma
 */
public class JsonTypeException extends JsonException
{
	protected JsonTypeException(Class<?> returned, Class<?> expected)
	{
		super("Invalid type conversion of "+returned.getName()+" to "+expected.getName());
	}
}
