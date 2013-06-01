package net.enigmablade.jsonic;

public class JsonTypeException extends JsonException
{
	protected JsonTypeException(Class<?> returned, Class<?> expected)
	{
		super("Invalid type conversion of "+returned.getName()+" to "+expected.getName());
	}
}
