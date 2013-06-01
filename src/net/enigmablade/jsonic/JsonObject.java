package net.enigmablade.jsonic;

import java.util.*;

import net.enigmablade.jsonic.ValueUtil.*;

/**
 * <p>An unordered collection or key-value pairings.<p>
 * <p>Basic format:</p>
 * <code>{ "key":"value", "required":"Hello World!" }</code>
 * 
 * @author Enigma
 */
public class JsonObject extends JsonElement
{
	//Object data
	private Map<String, Value> values;
	
	/*********************************
	 * Constructors for JSON Creation*
	 *********************************/
	
	/**
	 * Creates a new empty JsonObject.
	 */
	public JsonObject()
	{
		super();
		setup();
	}
	
	/**
	 * Creates a new JsonObject with the same stored information as the given object.
	 * @param o The object to clone
	 * @throws IllegalArgumentException if the object is <code>null</code>
	 */
	public JsonObject(JsonObject o)
	{
		super(o);
		setup();
		
		values.putAll(o.values);
	}
	
	/*********************************
	 * Constructors for JSON parsing *
	 *********************************/
	
	/**
	 * Creates a new JsonObject and parses the string immediately if not delayed.
	 * @param objStr The string to parse
	 * @param startIndex The starting index of the element in the string
	 * @param delayed Whether or not the parsing is delayed
	 * @throws JsonParseException if there was an error when parsing the string
	 */
	protected JsonObject(String objStr, int startIndex, boolean delayed) throws JsonParseException
	{
		super(objStr, startIndex, delayed);
	}
	
	/*******************
	 * Parsing methods *
	 *******************/
	
	/**
	 * Sets up required data structures.
	 */
	private void setup()
	{
		if(values == null)
			values = new HashMap<>();
		else
			values.clear();
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
		
		//Verify what is being parsed is indeed an object
		if(json.charAt(startIndex) != ParserUtil.OBJECT_OPEN)
			throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, 0);
		
		int index = startIndex+1;
		boolean seenObj = false;
		do
		{
			//Move to the start of the next key
			index = ParserUtil.nextNonWhitespace(json, index);
			
			//Get information on the key type
			char startChar = json.charAt(index);
			
			//Make sure it's an allowable character
			///Separation point (',')
			if(startChar == ParserUtil.SPLIT)
			{
				index = ParserUtil.nextNonWhitespace(json, index+1);
				startChar = json.charAt(index);
			}
			///Or end of the object ('}')
			else if(startChar == ParserUtil.OBJECT_CLOSE)
			{
				break;
			}
			///Or someone is bad at formatting their JSON!
			else if(seenObj)
			{
				throw new JsonParseException(JsonParseException.Type.INVALID_CHAR, index, startChar);
			}
			
			
			//Get the key
			String key;
			///String type
			if(ParserUtil.isStringChar(startChar))
			{
				key = ParserUtil.getStringBlock(json, index);
				index += key.length()+2;
			}
			///Unknown type
			else
			{
				key = ParserUtil.getUnknownBlock(json, index);
				index += key.length();
			}
			
			//Move to the start of the value
			if(json.charAt(index) != ParserUtil.OBJECT_MAP)
			{
				index = ParserUtil.nextNonWhitespace(json, index+1);
				if(json.charAt(index) != ParserUtil.OBJECT_MAP)
					throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, index);
			}
			index = ParserUtil.nextNonWhitespace(json, index+1);
			
			//Get the value and store it
			startChar = json.charAt(index);
			
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
			values.put(key, value);
			seenObj = true;
			
		}while(index < json.length()-1);
		
		//Check the very last character to make sure the object was closed
		if(json.charAt(index) != ParserUtil.OBJECT_CLOSE)
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
	
	public Set<String> keySet() throws JsonException
	{
		verifyParseState();
		
		return values.keySet();
	}
	
	public void put(String key, Object value) throws JsonException
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	public void put(String key, JsonObject value) throws JsonException
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	public void put(String key, JsonArray value) throws JsonException
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	public void put(String key, String value) throws JsonException
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	public void put(String key, long value) throws JsonException
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	public void put(String key, int value) throws JsonException
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	public void put(String key, double value) throws JsonException
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	public void put(String key, float value) throws JsonException
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	public void put(String key, boolean value) throws JsonException
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	public Object get(String key) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null)
			return null;
		return value.value;
	}
	
	public JsonObject getObject(String key) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.OBJECT)
			throw new JsonTypeException(value.value.getClass(), JsonObject.class);
		return (JsonObject)value.value;
	}
	
	public JsonArray getArray(String key) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.ARRAY)
			throw new JsonTypeException(value.value.getClass(), JsonArray.class);
		return (JsonArray)value.value;
	}
	
	public String getString(String key) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.STRING)
			throw new JsonTypeException(value.value.getClass(), String.class);
		return (String)value.value;
	}
	
	public Long getLong(String key) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.LONG)
			throw new JsonTypeException(value.value.getClass(), Long.class);
		return (Long)value.value;
	}
	
	public Double getDouble(String key) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.DOUBLE)
			throw new JsonTypeException(value.value.getClass(), Double.class);
		return (Double)value.value;
	}
	
	public Boolean getBoolean(String key) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.BOOLEAN)
			throw new JsonTypeException(value.value.getClass(), Boolean.class);
		return (Boolean)value.value;
	}
	
	public Object remove(String key) throws JsonException
	{
		verifyParseState();
		return values.remove(key);
	}
	
	/**************************
	 * Object to JSON methods *
	 **************************/
	
	@Override
	public String getJSON()
	{
		return getJSON(null);
	}
	
	public String getJSON(Comparator<String> comparator)
	{
		StringBuilder json = new StringBuilder();
		json.append('{');
		if(comparator == null)
			json.append(getJSONHelper());
		else
			json.append(getJSONSortedHelper(comparator));
		json.append('}');
		return json.toString();
	}
	
	private StringBuilder getJSONHelper()
	{
		StringBuilder json = new StringBuilder();
		int n = 0;
		for(String key : values.keySet())
		{
			appendValue(json, key, n++);
		}
		return json;
	}
	
	private StringBuilder getJSONSortedHelper(Comparator<String> comparator)
	{
		List<String> keys = new ArrayList<>(values.keySet());
		Collections.sort(keys, comparator);
		
		StringBuilder json = new StringBuilder();
		for(int n = 0; n < keys.size(); n++)
		{
			appendValue(json, keys.get(n), n);
		}
		return json;
	}
	
	private void appendValue(StringBuilder json, String key, int index)
	{
		json.append('"').append(key).append('"');
		json.append(':');
		
		Value value = values.get(key);
		if(value == null)
			json.append("null");
		else
			json.append(value.toString());
		
		if(index < values.size()-1)
			json.append(',');
	}
}
