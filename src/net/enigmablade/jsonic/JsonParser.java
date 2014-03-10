package net.enigmablade.jsonic;

import java.io.*;
import java.nio.charset.*;

/**
 * <p>The entry point for JSON parsing.<p>
 * <p>Two approaches are provided for use of the parser: static parsing methods and non-static processing methods.
 * Both are functionally equivalent and the use of either one is dependent on the user's coding style.<p>
 * 
 * @author Enigma
 */
public class JsonParser
{
	/******************************
	 * Non-static parsing methods *
	 ******************************/
	
	private boolean delayed;
	private Charset charset;
	
	//Constructors
	
	/**
	 * Creates a new instance of a non-delayed parser.
	 */
	public JsonParser()
	{
		this(false);
	}
	
	/**
	 * Creates a new instances of a parser that can be delayed.
	 * @param delayed Whether or not to delay parsing.
	 */
	public JsonParser(boolean delayed)
	{
		this(delayed, Charset.defaultCharset());
	}
	
	/**
	 * Creates a new instances of a parser that can be delayed and use the given charset.
	 * @param delayed Whether or not to delay parsing.
	 * @param charset The charset to use in input stream decoding.
	 */
	public JsonParser(boolean delayed, Charset charset)
	{
		this.delayed = delayed;
		this.charset = charset;
	}
	
	//Accessor methods
	
	/**
	 * Returns whether or not this parser is set to use the delayed parsing mode.
	 * @return <code>true</code> if parsing is set to be delayed, otherwise <code>false</code>.
	 */
	public boolean isDelayed()
	{
		return delayed;
	}
	
	/**
	 * Sets whether or not this parser should use the delayed parsing mode.
	 * @param delayed <code>true</code> if the parser should delay parsing.
	 */
	public void setDelayed(boolean delayed)
	{
		this.delayed = delayed;
	}
	
	/**
	 * Returns the charset being used to decode input streams.
	 * @return The charset.
	 */
	public Charset getCharset()
	{
		return charset;
	}
	
	/**
	 * Sets the charset to use for decoding input streams.
	 * @param charset The charset.
	 */
	public void setCharset(Charset charset)
	{
		this.charset = charset;
	}
	
	//Basic string methods
	
	/**
	 * Parses the given JSON using the parser settings.
	 * @param json The JSON
	 * @return A generic JSON element, or <code>null</code> if a JSON object or array was not given.
	 * @throws JsonParseException if an exception occurred during parsing
	 */
	public JsonElement process(String json) throws JsonParseException
	{
		return parse(json, delayed);
	}
	
	//Input stream methods
	
	public JsonElement process(InputStream stream) throws JsonParseException, IOException
	{
		return parse(stream, charset, delayed);
	}
	
	public JsonElement process(InputStream stream, Charset charset) throws JsonParseException, IOException
	{
		return parse(stream, charset, delayed);
	}
	
	//Object load methods
	
	public JsonObject processObject(String json) throws JsonParseException
	{
		return parseObject(json, delayed);
	}
	
	public JsonObject processObject(InputStream stream) throws JsonParseException, IOException
	{
		return parseObject(stream, charset, delayed);
	}
	
	public JsonObject processObject(InputStream stream, Charset charset) throws JsonParseException, IOException
	{
		return parseObject(stream, charset, delayed);
	}
	
	//Array load methods
	
	public JsonArray processArray(String json) throws JsonParseException
	{
		return parseArray(json, delayed);
	}
	
	public JsonArray processArray(InputStream stream) throws JsonParseException, IOException
	{
		return parseArray(stream, charset, delayed);
	}
	
	public JsonArray processArray(InputStream stream, Charset charset) throws JsonParseException, IOException
	{
		return parseArray(stream, charset, delayed);
	}
	
	/**************************
	 * Static parsing methods *
	 **************************/
	
	//Basic string methods
	
	/**
	 * Parses the given JSON.
	 * @param json The JSON
	 * @return A generic JSON element, or <code>null</code> if a JSON object or array was not given.
	 * @throws JsonParseException if an exception occurred during parsing
	 */
	public static JsonElement parse(String json) throws JsonParseException
	{
		return parse(json, false);
	}
	
	/**
	 * Parses the given JSON, which can be delayed.
	 * Later exceptions may be thrown if parsing is delayed.
	 * @param json The JSON
	 * @param delayed Whether or not the parsing is delayed
	 * @return A generic JSON element, or <code>null</code> if a JSON object or array was not given.
	 * @throws JsonParseException if an exception occurred during parsing
	 */
	public static JsonElement parse(String json, boolean delayed) throws JsonParseException
	{
		if(json == null)
			throw new IllegalArgumentException("JSON string cannot be null");
		
		json = json.trim();
		
		switch(json.charAt(0))
		{
			//Object
			case ParserUtil.OBJECT_OPEN: return parseObject(json, delayed);
			//Array
			case ParserUtil.ARRAY_OPEN: return parseArray(json, delayed);
			//Invalid
			default: return null;
		}
	}
	
	//Input stream methods
	
	public static JsonElement parse(InputStream stream) throws JsonParseException, IOException
	{
		return parse(stream, false);
	}
	
	public static JsonElement parse(InputStream stream, boolean delayed) throws JsonParseException, IOException
	{
		return parse(stream, Charset.defaultCharset(), delayed);
	}
	
	public static JsonElement parse(InputStream stream, Charset charset) throws JsonParseException, IOException
	{
		return parse(stream, charset, false);
	}
	
	public static JsonElement parse(InputStream stream, Charset charset, boolean delayed) throws JsonParseException, IOException
	{
		return parse(readString(stream, charset), delayed);
	}
	
	//Object load methods
	
	public static JsonObject parseObject(String json) throws JsonParseException
	{
		return new JsonObject(json, 0, false);
	}
	
	public static JsonObject parseObject(String json, boolean delayed) throws JsonParseException
	{
		return new JsonObject(json, 0, delayed);
	}
	
	public static JsonObject parseObject(InputStream stream) throws JsonParseException, IOException
	{
		return parseObject(stream, false);
	}
	
	public static JsonObject parseObject(InputStream stream, boolean delayed) throws JsonParseException, IOException
	{
		return parseObject(stream, Charset.defaultCharset(), delayed);
	}
	
	public static JsonObject parseObject(InputStream stream, Charset charset) throws JsonParseException, IOException
	{
		return parseObject(stream, charset, false);
	}
	
	public static JsonObject parseObject(InputStream stream, Charset charset, boolean delayed) throws JsonParseException, IOException
	{
		return parseObject(readString(stream, charset), delayed);
	}
	
	//Array load methods
	
	public static JsonArray parseArray(String json) throws JsonParseException
	{
		return new JsonArray(json, 0, false);
	}
	
	public static JsonArray parseArray(String json, boolean delayed) throws JsonParseException
	{
		return new JsonArray(json, 0, delayed);
	}
	
	public static JsonArray parseArray(InputStream stream) throws JsonParseException, IOException
	{
		return parseArray(stream, false);
	}
	
	public static JsonArray parseArray(InputStream stream, boolean delayed) throws JsonParseException, IOException
	{
		return parseArray(stream, Charset.defaultCharset(), delayed);
	}
	
	public static JsonArray parseArray(InputStream stream, Charset charset) throws JsonParseException, IOException
	{
		return parseArray(stream, charset, false);
	}
	
	public static JsonArray parseArray(InputStream stream, Charset charset, boolean delayed) throws JsonParseException, IOException
	{
		return parseArray(readString(stream, charset), delayed);
	}
	
	/******************
	 * Helper methods *
	 ******************/
	
	/**
	 * Reads the entire input stream into a string.
	 * @param stream The stream to read
	 * @param charset The character set to use
	 * @return The string read from the stream
	 * @throws IOException if the input stream couldn't be read
	 */
	private static String readString(InputStream stream, Charset charset) throws IOException
	{
		StringBuilder json = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset)))
		{
			char[] buf = new char[512];
			while(reader.read(buf) > 0)
				json.append(buf);
		}
		return json.toString();
	}
}
