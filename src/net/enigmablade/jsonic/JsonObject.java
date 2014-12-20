package net.enigmablade.jsonic;

import java.util.*;
import net.enigmablade.jsonic.ValueUtil.*;

/**
 * <p>An unordered collection or key-value pairings.<p>
 * <p>Basic format:</p>
 * <code>{ "key":"value", "required":"Hello World!" }</code>
 * <p>All access methods check the parse state of the object, so there is the possibility they may fail if parsing was delayed on invalid JSON.</p>
 * 
 * @author Enigma
 */
public class JsonObject extends JsonElement implements Cloneable
{
	private static final long serialVersionUID = -212184197241566516L;
	
	//Object data
	private HashMap<String, Value> values;
	
	/*********************************
	 * Constructors for JSON Creation*
	 *********************************/
	
	/**
	 * Creates a new empty JsonObject.
	 */
	public JsonObject()
	{
		super(ParserUtil.OBJECT_OPEN, ParserUtil.OBJECT_CLOSE);
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
		super(objStr, startIndex, delayed, ParserUtil.OBJECT_OPEN, ParserUtil.OBJECT_CLOSE);
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
		boolean seenElement = false;
		do
		{
			//Move to the start of the next key
			index = ParserUtil.nextNonWhitespace(json, index);
			
			//Get information on the key type
			char startChar = json.charAt(index);
			
			//Make sure it's an allowable character
			//--Separation point (',')
			if(startChar == ParserUtil.SPLIT)
			{
				index = ParserUtil.nextNonWhitespace(json, index+1);
				startChar = json.charAt(index);
			}
			//--Or end of the object ('}')
			else if(startChar == ParserUtil.OBJECT_CLOSE)
			{
				break;
			}
			//--Or someone is bad at formatting their JSON!
			else if(seenElement)
			{
				//System.out.println("ERROR ON: "+json.substring(startIndex, startIndex+getRawLength()));
				throw new JsonParseException(JsonParseException.Type.INVALID_CHAR, index, startChar);
			}
			
			
			//Get the key
			String key;
			//--String type
			if(ParserUtil.isStringChar(startChar))
			{
				key = ParserUtil.getStringBlock(json, index);
				index += key.length()+2;
			}
			//--Unknown type
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
			int len;
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
					if((len = object.getRawLength()) < 2)
						throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, index);
					index += len;
					break;
				
				//Array
				case ParserUtil.ARRAY_OPEN: 
					JsonArray array = new JsonArray(json, index, delayed);
					value = ValueUtil.createValue(array);
					if((len = array.getRawLength()) < 2)
						throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, index);
					index += len;
					break;
				case ParserUtil.ARRAY_CLOSE:
					throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, index);
				
				//Unknown: boolean, number, or null
				default: 
					String valueStr = ParserUtil.getUnknownBlock(json, index);
					value = ParserUtil.parseUnknown(valueStr);
					index += valueStr.length();
			}
			
			//Add the value
			values.put(key, value);
			seenElement = true;
			
		}while(index < json.length()-1);
		
		//Check the very last character to make sure the object was closed
		if(json.charAt(index) != ParserUtil.OBJECT_CLOSE)
			throw new JsonParseException(JsonParseException.Type.BAD_END, index);
		
		return index-startIndex+1;
	}
	
	/********************
	 * Accessor methods *
	 ********************/
	
	/**
	 * Returns the number of elements in the object, parsing the it if required.
	 * @return The size of the object
	 * @throws JsonException if an exception occurred during parsing
	 */
	public int size()
	{
		verifyParseState();
		
		return values.size();
	}
	
	/**
	 * Returns whether or not this object contains any keys or values.
	 * @return <code>true</code> if it contains keys or values, otherwise <code>false</code>
	 * @throws JsonException if an exception occurred during parsing
	 */
	public boolean isEmpty()
	{
		verifyParseState();
		return values.isEmpty();
	}
	
	/**
	 * Returns the set of keys stored by this object.
	 * @return The set of keys
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#keySet()
	 */
	public Set<String> keySet()
	{
		verifyParseState();
		
		return values.keySet();
	}
	
	/**
	 * Returns whether or not this object contains the given key.
	 * @param key The key
	 * @return <code>true</code> if this object contains the key, otherwise <code>false</code>
	 * @throws JsonException  if an exception occurred during parsing
	 * @see java.util.Map#containsKey(Object)
	 */
	public boolean containsKey(String key)
	{
		verifyParseState();
		return values.containsKey(key);
	}
	
	/**
	 * Puts a value into this object mapped to the given key.
	 * @param key The key
	 * @param value The value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#put(Object, Object)
	 */
	public void put(String key, Object value)
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	/**
	 * Puts a JsonObject into this object mapped to the given key.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param key The key
	 * @param value The JsonObject value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#put(Object, Object)
	 */
	public void put(String key, JsonObject value)
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	/**
	 * Puts a JsonArray into this object mapped to the given key.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param key The key
	 * @param value The JsonArray value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#put(Object, Object)
	 */
	public void put(String key, JsonArray value)
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	/**
	 * Puts a String into this object mapped to the given key.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param key The key
	 * @param value The String value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#put(Object, Object)
	 */
	public void put(String key, String value)
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	/**
	 * Puts a long into this object mapped to the given key.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param key The key
	 * @param value The long value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#put(Object, Object)
	 */
	public void put(String key, long value)
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	/**
	 * Puts a int into this object mapped to the given key.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param key The key
	 * @param value The int value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#put(Object, Object)
	 */
	public void put(String key, int value)
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	/**
	 * Puts a double into this object mapped to the given key.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param key The key
	 * @param value The double value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#put(Object, Object)
	 */
	public void put(String key, double value)
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	/**
	 * Puts a float into this object mapped to the given key.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param key The key
	 * @param value The float value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#put(Object, Object)
	 */
	public void put(String key, float value)
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	/**
	 * Puts a boolean into this object mapped to the given key.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param key The key
	 * @param value The boolean value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#put(Object, Object)
	 */
	public void put(String key, boolean value)
	{
		verifyParseState();
		values.put(key, ValueUtil.createValue(value));
	}
	
	/**
	 * Returns the value associated with the given key.
	 * @param key The key
	 * @return The value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#get(Object)
	 */
	public Object get(String key)
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null)
			return null;
		return value.value;
	}
	
	/**
	 * Returns the JsonObject value associated with the given key.<br>
	 * This method is for convenience.
	 * @param key The key
	 * @return The JsonObject value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#get(Object)
	 */
	public JsonObject getObject(String key)
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.OBJECT)
			throw new JsonTypeException(value.value.getClass(), JsonObject.class);
		return (JsonObject)value.value;
	}
	
	/**
	 * Returns the JsonArray value associated with the given key.<br>
	 * This method is for convenience.
	 * @param key The key
	 * @return The JsonArray value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#get(Object)
	 */
	public JsonArray getArray(String key)
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.ARRAY)
			throw new JsonTypeException(value.value.getClass(), JsonArray.class);
		return (JsonArray)value.value;
	}
	
	/**
	 * Returns the String value associated with the given key.<br>
	 * This method is for convenience.
	 * @param key The key
	 * @return The String value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#get(Object)
	 */
	public String getString(String key)
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.STRING)
			throw new JsonTypeException(value.value.getClass(), String.class);
		return (String)value.value;
	}
	
	/**
	 * Returns the long value associated with the given key.<br>
	 * This method is for convenience.
	 * @param key The key
	 * @return The long value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#get(Object)
	 */
	public Long getLong(String key)
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.LONG)
			throw new JsonTypeException(value.value.getClass(), Long.class);
		return (Long)value.value;
	}
	
	/**
	 * Returns the integer value associated with the given key.<br>
	 * This method is for convenience.
	 * @param key The key
	 * @return The integer value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#get(Object)
	 */
	public Integer getInt(String key)
	{
		Long l = getLong(key);
		if(l == null)
			return null;
		return l.intValue();
	}
	
	/**
	 * Returns the double value associated with the given key.<br>
	 * This method is for convenience.
	 * @param key The key
	 * @return The double value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#get(Object)
	 */
	public Double getDouble(String key)
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.DOUBLE)
			throw new JsonTypeException(value.value.getClass(), Double.class);
		return (Double)value.value;
	}
	
	/**
	 * Returns the float value associated with the given key.<br>
	 * This method is for convenience.
	 * @param key The key
	 * @return The float value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#get(Object)
	 */
	public Float getFloat(String key)
	{
		Double d = getDouble(key);
		if(d == null)
			return null;
		return d.floatValue();
	}
	
	/**
	 * Returns the boolean value associated with the given key.<br>
	 * This method is for convenience.
	 * @param key The key
	 * @return The boolean value
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#get(Object)
	 */
	public Boolean getBoolean(String key)
	{
		verifyParseState();
		
		Value value = values.get(key);
		if(value == null || value.value == null)
			return null;
		if(value.type != ValueType.BOOLEAN)
			throw new JsonTypeException(value.value.getClass(), Boolean.class);
		return (Boolean)value.value;
	}
	
	/**
	 * Removes the value associated with the specified key.
	 * @param key The key
	 * @return The removed value, or <code>null</code> if nothing was removed
	 * @throws JsonException if an exception occurred during parsing
	 * @see java.util.Map#remove(Object)
	 */
	public Object remove(String key)
	{
		verifyParseState();
		return values.remove(key);
	}
	
	/**************************
	 * Object to JSON methods *
	 **************************/
	
	/**
	 * Returns this object and its unordered contents in JSON format.
	 * @return The JSON formatted object
	 * @see JsonElement#getJSON()
	 */
	@Override
	protected String toJSON()
	{
		return getJSON(null);
	}
	
	/**
	 * Returns this object and its contents ordered using the given comparator in JSON format.
	 * @param comparator The comparator with which to sort the keys
	 * @return The JSON formatted object
	 * @see JsonElement#getJSON()
	 */
	public String getJSON(Comparator<String> comparator)
	{
		if(isParsingDelayed())
			return getDelayedString();
		
		StringBuilder json = new StringBuilder();
		json.append(ParserUtil.OBJECT_OPEN);
		if(comparator == null)
			json.append(getJSONHelper());
		else
			json.append(getJSONSortedHelper(comparator));
		json.append(ParserUtil.OBJECT_CLOSE);
		return json.toString();
	}
	
	//Helper methods for building the object string
	
	private StringBuilder getJSONHelper()
	{
		StringBuilder json = new StringBuilder();
		int n = 0;
		for(String key : values.keySet())
			appendValue(json, key, n++);
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
		json.append(ParserUtil.STRING_1).append(key).append(ParserUtil.STRING_1);
		json.append(ParserUtil.OBJECT_MAP);
		
		Value value = values.get(key);
		if(value == null)
			json.append("null");
		else
			json.append(value.toString());
		
		if(index < values.size()-1)
			json.append(ParserUtil.SPLIT);
	}
	
	/********************
	 * Object overrides *
	 ********************/
	
	/**
	 * Checks whether this array and and its contents are equal.
	 * If parsing is delayed, uses the same method as in JsonElement.
	 * 
	 * @param o The array to check against.
	 * @return <code>true</code> if the two arrays are equal, otherwise <code>false</code>.
	 * 
	 * @see JsonElement#equals(Object)
	 */
	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof JsonObject))
			return false;
		
		JsonObject j = (JsonObject)o;
		return values.equals(j.values);
	}
	
	/**
	 * Returns the hash code of this JSON object, which is equivalent to the sum of the hash codes of each entry.
	 * 
	 * @returns This object's hash code.
	 * 
	 * @see Map#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return values.hashCode();
	}
	
	/**
	 * Clones this object based on HashMap's clone method.
	 * If parsed, the map will be "shallow copied".
	 * 
	 * @return The cloned object.
	 * 
	 * @see HashMap#clone()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object clone() throws CloneNotSupportedException
	{
		JsonObject newArray = (JsonObject)super.clone();
		newArray.values = (HashMap<String, Value>)values.clone();
		return newArray;
	}
}
