package net.enigmablade.jsonic;

import java.util.*;

import net.enigmablade.jsonic.ValueUtil.*;

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
		values = new HashMap<>();
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
		
		//System.out.println("Parsing object (delayed="+delayed+"): "+json+", starting at "+startIndex);
		
		if(json.charAt(startIndex) != ParserUtil.OBJECT_OPEN)
			throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, 0);
		
		int index = startIndex+1;
		do
		{
			//Move to the start of the next key
			index = ParserUtil.nextNonWhitespace(json, index);
			//System.out.println("\tKey start index: "+startIndex);
			
			//Get information on the key type
			char startChar = json.charAt(index);
			//System.out.println("\tKey start char: "+startChar);
			
			//End of the object
			if(startChar == ParserUtil.OBJECT_CLOSE)
				break;
			//Or break point
			else if(startChar == ParserUtil.SPLIT)
			{
				index = ParserUtil.nextNonWhitespace(json, index+1);
				startChar = json.charAt(index);
			}
			
			//Get the key
			String key;
			//String type
			if(ParserUtil.isStringChar(startChar))
			{
				//System.out.println("\tKey is string");
				key = ParserUtil.getStringBlock(json, index);
				index += key.length()+2;
			}
			//Unknown type
			else
			{
				//System.out.println("\tKey is unknown");
				key = ParserUtil.getUnknownBlock(json, index);
				index += key.length();
			}
			//System.out.println("\tKey: "+key);
			
			//Move to the start of the value
			if(json.charAt(index) != ParserUtil.OBJECT_MAP)
			{
				index = ParserUtil.nextNonWhitespace(json, index+1);
				if(json.charAt(index) != ParserUtil.OBJECT_MAP)
					throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, index);
			}
			index = ParserUtil.nextNonWhitespace(json, index+1);
			//System.out.println("\tValue start index: "+index);
			
			//Get the value and store it
			startChar = json.charAt(index);
			//System.out.println("\tValue start char: "+startChar);
			
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
			values.put(key, value);
			//System.out.println(values);
			
		}while(index < json.length()-1);
		
		if(json.charAt(index) != ParserUtil.OBJECT_CLOSE)
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
			
			if(c == ParserUtil.OBJECT_CLOSE)
				objCount--;
			else if(c == ParserUtil.OBJECT_OPEN)
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
	
	public Set<String> keySet() throws JsonException
	{
		verifyParseState();
		
		return values.keySet();
	}
	
	public void put(String key, Object value) throws JsonException
	{
		verifyParseState();
		
		Value newValue;
		
		if(value == null)
			newValue = ValueUtil.createNullValue();
		else
			newValue = ValueUtil.createValue(value);
		
		values.put(key, newValue);
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
