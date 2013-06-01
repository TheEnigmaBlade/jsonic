package net.enigmablade.jsonic;

import java.util.*;
import net.enigmablade.jsonic.ValueUtil.*;

public class JsonArray extends JsonElement
{
	//Array data
	private List<Value> values;
	
	/*********************************
	 * Constructors for JSON Creation*
	 *********************************/
	
	/**
	 * Creates a new empty JsonArray.
	 */
	public JsonArray()
	{
		super();
		setup();
		values = new LinkedList<>();
	}
	
	/**
	 * Creates a new JsonArray with the same stored information as the given object.
	 * @param o The object to clone
	 */
	public JsonArray(JsonArray a)
	{
		super(a);
		setup();
		
		values.addAll(a.values);
	}
	
	/*********************************
	 * Constructors for JSON parsing *
	 *********************************/
	
	/**
	 * Creates a new JsonObject and parses the string immediately if not delayed.
	 * @param arrayStr The string to parse
	 * @param startIndex The starting index of the element in the string
	 * @param delayed Whether or not the parsing is delayed
	 * @throws JsonParseException if there was an error when parsing the string
	 */
	protected JsonArray(String arrayStr, int startIndex, boolean delayed) throws JsonParseException
	{
		super(arrayStr, startIndex, delayed);
	}
	
	/*******************
	 * Parsing methods *
	 *******************/
	
	/**
	 * Sets up required data structures.
	 */
	private void setup()
	{
		values = new ArrayList<>();
	}
	
	/**
	 * Parses the string that represents this object.
	 * @param json The string
	 * @param startIndex The starting index of the element in the string
	 * @param delayed Whether or not the parsing was delayed
	 * @throws JsonParseException if there was an error when parsing the string
	 */
	@Override
	protected int parse(String json, int startIndex, boolean delayed) throws JsonParseException
	{
		setup();
		
		//System.out.println("Parsing array (delayed="+delayed+"): "+json+", starting at "+startIndex);
		
		if(json.charAt(startIndex) != ParserUtil.ARRAY_OPEN)
			throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, 0);
		
		int index = startIndex+1;
		do
		{
			//Move to the start of the next value
			index = ParserUtil.nextNonWhitespace(json, index);
			//System.out.println("\tValue start index: "+startIndex);
			
			//Get information on the value type
			char startChar = json.charAt(index);
			//System.out.println("\tValue start char: "+startChar);
			
			//End of the object
			if(startChar == ParserUtil.ARRAY_CLOSE)
				break;
			//Or break point
			else if(startChar == ParserUtil.SPLIT)
			{
				index = ParserUtil.nextNonWhitespace(json, index+1);
				startChar = json.charAt(index);
			}
			else if(startChar == ParserUtil.OBJECT_MAP)
			{
				throw new JsonParseException(JsonParseException.Type.INVALID_CHAR, index, startChar);
			}
			
			//Parse the value based on type
			Value value = null;
			switch(startChar)
			{
				//String
				case ParserUtil.STRING_1: 
				case ParserUtil.STRING_2: 
					//System.out.println("\tValue is string");
					String str = ParserUtil.getStringBlock(json, index);
					//System.out.println("\tValue str: "+str);
					value = ValueUtil.createValue(str);
					index += str.length()+2;
					break;
				
				//Object
				case ParserUtil.OBJECT_OPEN: 
					//System.out.println("\tValue is object");
					//String objectStr = ParserUtil.getObjectBlock(json, index);
					//System.out.println("\tValue str: "+objectStr);
					JsonObject object = new JsonObject(json, index, delayed);
					value = ValueUtil.createValue(object);
					index += object.getRawLength();
					break;
				
				//Array
				case ParserUtil.ARRAY_OPEN: 
					//System.out.println("\tValue is array");
					//String arrayStr = ParserUtil.getArrayBlock(json, index);
					//System.out.println("\tValue str: "+arrayStr);
					JsonArray array = new JsonArray(json, index, delayed);
					value = ValueUtil.createValue(array);
					index += array.getRawLength();
					break;
				
				//Unknown
				default: 
					//System.out.println("\tValue is unknown");
					String valueStr = ParserUtil.getUnknownBlock(json, index);
					//System.out.println("\tValue str: "+valueStr);
					
					index += valueStr.length();
					
					value = ParserUtil.parseUnknown(valueStr);
			}
			//System.out.println("\tValue: "+value);
			
			//Add the value
			values.add(value);
			
		}while(index < json.length()-1);
		
		if(json.charAt(index) != ParserUtil.ARRAY_CLOSE)
			throw new JsonParseException(JsonParseException.Type.BAD_END, index);
		
		return index-startIndex+1;
	}
	
	protected int getRawLength(String json, int startIndex)
	{
		char c;
		int n = 1;
		for(int objCount = 0; startIndex < json.length(); startIndex++, n++)
		{
			c = json.charAt(startIndex);
			
			if(c == ParserUtil.ARRAY_CLOSE)
				objCount--;
			else if(c == ParserUtil.ARRAY_OPEN)
				objCount++;
			
			if(objCount <= 0)
				break;
		}
		return n;
	}
	
	/********************
	 * Accessor methods *
	 ********************/
	
	public int size() throws JsonException
	{
		verifyParseState();
		
		return values.size();
	}
	
	public Object get(int index) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(index);
		if(value == null)
			return null;
		return value.value;
	}
	
	public JsonObject getObject(int index) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(index);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.OBJECT)
			throw new JsonTypeException(value.value.getClass(), JsonObject.class);
		return (JsonObject)value.value;
	}
	
	public JsonArray getArray(int index) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(index);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.ARRAY)
			throw new JsonTypeException(value.value.getClass(), JsonArray.class);
		return (JsonArray)value.value;
	}
	
	public String getString(int index) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(index);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.STRING)
			throw new JsonTypeException(value.value.getClass(), String.class);
		return (String)value.value;
	}
	
	public Long getLong(int index) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(index);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.LONG)
			throw new JsonTypeException(value.value.getClass(), Long.class);
		return (Long)value.value;
	}
	
	public Double getDouble(int index) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(index);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.DOUBLE)
			throw new JsonTypeException(value.value.getClass(), Double.class);
		return (Double)value.value;
	}
	
	public Boolean getBoolean(int index) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(index);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.BOOLEAN)
			throw new JsonTypeException(value.value.getClass(), Boolean.class);
		return (Boolean)value.value;
	}
	
	/**************************
	 * Object to JSON methods *
	 **************************/
	
	@Override
	public String getJSON()
	{
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(int n = 0; n < values.size(); n++)
		{
			Value value = values.get(n);
			if(value == null)
				json.append("null");
			else
				json.append(values.toString());
			
			if(n < values.size()-1)
				json.append(',');
		}
		json.append(']');
		return json.toString();
	}
}
