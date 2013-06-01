package net.enigmablade.jsonic;

import java.io.*;
import java.nio.charset.*;

public class JsonParser
{
	/**************************
	 * Static parsing methods *
	 **************************/
	
	//Basic string methods
	
	public static JsonElement parse(String json) throws JsonParseException
	{
		return parse(json, false);
	}
	
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
	
	//Stream helper methods
	
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
