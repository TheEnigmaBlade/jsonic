package net.enigmablade.jsonic;

import java.util.*;
import net.enigmablade.jsonic.ValueUtil.*;

/**
 * <p>An ordered sequence of values.<p>
 * <p>Basic format:</p>
 * <code>[ "value", "Hello World!" ]</code>
 * 
 * @author Enigma
 */
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
		//Verify all required data structures exist
		setup();
		
		//Verify what is being parsed is indeed an array
		if(json.charAt(startIndex) != ParserUtil.ARRAY_OPEN)
			throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, 0);
		
		int index = startIndex+1;
		boolean seenObj = false;
		do
		{
			//Move to the start of the next value
			index = ParserUtil.nextNonWhitespace(json, index);
			
			//Get information on the value type
			char startChar = json.charAt(index);
			
			//Make sure it's an allowable character
			///Separation point (',')
			if(startChar == ParserUtil.SPLIT)
			{
				index = ParserUtil.nextNonWhitespace(json, index+1);
				startChar = json.charAt(index);
			}
			///End of array (']')
			else if(startChar == ParserUtil.ARRAY_CLOSE)
			{
				break;
			}
			///Or someone is bad at formatting their JSON!
			else if(seenObj)
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
					String str = ParserUtil.getStringBlock(json, index);
					value = ValueUtil.createValue(str);
					index += str.length()+2;
					break;
				
				//Object
				case ParserUtil.OBJECT_OPEN: 
					JsonObject object = new JsonObject(json, index, delayed);
					value = ValueUtil.createValue(object);
					index += object.getRawLength();
					break;
				
				//Array
				case ParserUtil.ARRAY_OPEN: 
					JsonArray array = new JsonArray(json, index, delayed);
					value = ValueUtil.createValue(array);
					index += array.getRawLength();
					break;
				
				//Unknown: boolean, number, or null
				default: 
					String valueStr = ParserUtil.getUnknownBlock(json, index);
					value = ParserUtil.parseUnknown(valueStr);
					index += valueStr.length();
			}
			
			//Add the value
			values.add(value);
			seenObj = true;
			
		}while(index < json.length()-1);
		
		//Check the very last character to make sure the object was closed
		if(json.charAt(index) != ParserUtil.ARRAY_CLOSE)
			throw new JsonParseException(JsonParseException.Type.BAD_END, index);
		
		return index-startIndex+1;
	}
	
	/********************
	 * Accessor methods *
	 ********************/
	
	public int size() throws JsonException
	{
		verifyParseState();
		
		return values.size();
	}
	
	public void add(String key, Object value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	public void add(String key, JsonObject value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	public void add(String key, JsonArray value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	public void add(String key, String value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	public void add(String key, long value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	public void add(String key, int value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	public void add(String key, double value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	public void add(String key, float value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	public void add(String key, boolean value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
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
