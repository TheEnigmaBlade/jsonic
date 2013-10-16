package net.enigmablade.jsonic;

import java.util.*;

import net.enigmablade.jsonic.ValueUtil.*;

/**
 * <p>An ordered sequence of values.<p>
 * <p>Basic format:</p>
 * <code>[ "value", "Hello World!" ]</code>
 * <p>All access methods check the parse state of the object, so there is the possibility they may fail if parsing was delayed on invalid JSON.</p>
 * 
 * @author Enigma
 */
public class JsonArray extends JsonElement
{
	private static final long serialVersionUID = -7960304694583142305L;
	
	//Default values
	private static final int INITIAL_CAPACITY = 10;
	
	//Array data
	private List<Value> values;
	
	/*********************************
	 * Constructors for JSON Creation*
	 *********************************/
	
	/**
	 * Creates a new empty JsonArray with an initial size of 10.
	 */
	public JsonArray()
	{
		this(INITIAL_CAPACITY);
	}
	
	/**
	 * Creates a new empty JsonArray with the given initial size.
	 * @param initialCapacity The initial size of the array
	 * @see java.util.ArrayList#ArrayList(int)
	 */
	public JsonArray(int initialCapacity)
	{
		super();
		setup(initialCapacity);
	}
	
	/**
	 * Creates a new JsonArray with the same stored information as the given object.
	 * @param a The array to clone
	 * @throws JsonException if an exception occurred during parsing
	 */
	public JsonArray(JsonArray a) throws JsonException
	{
		super(a);
		setup(a.size());
		
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
	private void setup(int initialCapacity)
	{
		values = new ArrayList<>(initialCapacity);
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
		setup(INITIAL_CAPACITY);
		
		//Verify what is being parsed is indeed an array
		if(json.charAt(startIndex) != ParserUtil.ARRAY_OPEN)
			throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, 0);
		
		int index = startIndex+1;
		boolean seenElement = false;
		do
		{
			//Move to the start of the next value
			index = ParserUtil.nextNonWhitespace(json, index);
			
			//Get information on the value type
			char startChar = json.charAt(index);
			
			//Make sure it's an allowable character
			//--Separation point (','), skip to start of next element
			if(startChar == ParserUtil.SPLIT)
			{
				index = ParserUtil.nextNonWhitespace(json, index+1);
				startChar = json.charAt(index);
			}
			//--End of array (']'), stop parsing
			else if(startChar == ParserUtil.ARRAY_CLOSE)
			{
				break;
			}
			//--Or someone is bad at formatting their JSON!
			else if(seenElement)
			{
				throw new JsonParseException(JsonParseException.Type.INVALID_CHAR, index, startChar);
			}
			
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
				case ParserUtil.OBJECT_CLOSE:
				case ParserUtil.OBJECT_MAP:
					throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, index);
				
				//Array
				case ParserUtil.ARRAY_OPEN: 
					JsonArray array = new JsonArray(json, index, delayed);
					value = ValueUtil.createValue(array);
					if((len = array.getRawLength()) < 2)
						throw new JsonParseException(JsonParseException.Type.INVALID_FORMAT, index);
					index += len;
					break;
				
				//Unknown: boolean, number, or null
				default: 
					String valueStr = ParserUtil.getUnknownBlock(json, index);
					value = ParserUtil.parseUnknown(valueStr);
					index += valueStr.length();
			}
			
			//Add the value
			values.add(value);
			seenElement = true;
			
		}while(index < json.length()-1);
		
		//Check the very last character to make sure the object was closed
		if(json.charAt(index) != ParserUtil.ARRAY_CLOSE)
			throw new JsonParseException(JsonParseException.Type.BAD_END, index);
		
		return index-startIndex+1;
	}
	
	/**
	 * Returns the raw (character) length of the array, starting at the starting index.
	 * @param json The JSON being checked
	 * @param startIndex The starting index in the JSON
	 * @return The length of the array
	 * @see JsonElement#getRawLength(String, int)
	 */
	@Override
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
	
	/**
	 * Returns the number of elements in the array, parsing the it if required.
	 * @return The size of the array
	 * @throws JsonException if an exception occurred during parsing
	 */
	public int size() throws JsonException
	{
		verifyParseState();
		return values.size();
	}
	
	/**
	 * Adds a value to the array, parsing the array if required.
	 * @param value The object to add
	 * @throws JsonException if an exception occurred during parsing
	 */
	public void add(Object value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	/**
	 * Adds a value to the array, parsing the array if required.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param value The JsonObject to add
	 * @throws JsonException if an exception occurred during parsing
	 */
	public void add(JsonObject value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	/**
	 * Adds a value to the array, parsing the array if required.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param value The JsonArray to add
	 * @throws JsonException if an exception occurred during parsing
	 */
	public void add(JsonArray value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	/**
	 * Adds a value to the array, parsing the array if required.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param value The String to add
	 * @throws JsonException if an exception occurred during parsing
	 */
	public void add(String value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	/**
	 * Adds a value to the array, parsing the array if required.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param value The long to add
	 * @throws JsonException if an exception occurred during parsing
	 */
	public void add(long value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	/**
	 * Adds a value to the array, parsing the array if required.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param value The int to add
	 * @throws JsonException if an exception occurred during parsing
	 */
	public void add(int value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	/**
	 * Adds a value to the array, parsing the array if required.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param value The double to add
	 * @throws JsonException if an exception occurred during parsing
	 */
	public void add(double value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	/**
	 * Adds a value to the array, parsing the array if required.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param value The float to add
	 * @throws JsonException if an exception occurred during parsing
	 */
	public void add(float value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	/**
	 * Adds a value to the array, parsing the array if required.<br>
	 * This method is for convenience and a <i>slight</i> speed gain.
	 * @param value The boolean to add
	 * @throws JsonException if an exception occurred during parsing
	 */
	public void add(boolean value) throws JsonException
	{
		verifyParseState();
		values.add(ValueUtil.createValue(value));
	}
	
	/**
	 * Returns the value at the specified index in the array.
	 * @param index The index in the array
	 * @return The value
	 * @throws JsonException if an exception occurred during parsing
	 */
	public Object get(int index) throws JsonException
	{
		verifyParseState();
		
		Value value = values.get(index);
		if(value == null)
			return null;
		return value.value;
	}
	
	/**
	 * Returns the value at the specified index in the array.<br>
	 * This method is for convenience.
	 * @param index The index in the array
	 * @return The JsonObject value
	 * @throws JsonException if an exception occurred during parsing
	 */
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
	
	/**
	 * Returns the value at the specified index in the array.<br>
	 * This method is for convenience.
	 * @param index The index in the array
	 * @return The JsonArray value
	 * @throws JsonException if an exception occurred during parsing
	 */
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
	
	/**
	 * Returns the value at the specified index in the array.<br>
	 * This method is for convenience.
	 * @param index The index in the array
	 * @return The String value
	 * @throws JsonException if an exception occurred during parsing
	 */
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
	
	/**
	 * Returns the value at the specified index in the array.<br>
	 * This method is for convenience.
	 * @param index The index in the array
	 * @return The long value
	 * @throws JsonException if an exception occurred during parsing
	 */
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
	
	/**
	 * Returns the value at the specified index in the array.<br>
	 * This method is for convenience.
	 * @param index The index in the array
	 * @return The double value
	 * @throws JsonException if an exception occurred during parsing
	 */
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
	
	/**
	 * Returns the value at the specified index in the array.<br>
	 * This method is for convenience.
	 * @param index The index in the array
	 * @return The boolean value
	 * @throws JsonException if an exception occurred during parsing
	 */
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
	
	/**
	 * Removes and returns the value at the specified index.
	 * @param index The index of the value to be removed
	 * @return The removed value
	 * @throws JsonException if an exception occurred during parsing 
	 * @throws IndexOutOfBoundsException if the index is out of range (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	public Object remove(int index) throws JsonException
	{
		verifyParseState();
		return values.remove(index);
	}
	
	/**
	 * Removes all elements from the array.
	 */
	public void clear()
	{
		values.clear();
	}
	
	/**************************
	 * Object to JSON methods *
	 **************************/
	
	/**
	 * Returns this array and its contents in JSON format.
	 * @return The JSON formatted array
	 */
	@Override
	protected String toJSON()
	{
		StringBuilder json = new StringBuilder();
		json.append(ParserUtil.ARRAY_OPEN);
		for(int n = 0; n < values.size(); n++)
		{
			Value value = values.get(n);
			if(value == null)
				json.append("null");
			else
				json.append(values.toString());
			
			if(n < values.size()-1)
				json.append(ParserUtil.SPLIT);
		}
		json.append(ParserUtil.ARRAY_CLOSE);
		return json.toString();
	}

}
