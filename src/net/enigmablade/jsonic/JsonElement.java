package net.enigmablade.jsonic;

/**
 * A basic parsable JSON element for which the parsing can be delayed.
 * 
 * @author Enigma
 */
public abstract class JsonElement
{
	//Parsing info
	private String delayedString = null;
	private int delayedIndex = -1;
	
	//Other info
	private int length = -1;
	
	/*********************************
	 * Constructors for JSON Creation*
	 *********************************/
	
	/**
	 * Creates an empty JsonElement.
	 */
	public JsonElement()
	{
		delayedString = null;
	}
	
	/**
	 * Creates a new JsonElement with the same stored information as the given object.
	 * @param e The element to clone
	 * @throws IllegalArgumentException if the object is <code>null</code>
	 */
	public JsonElement(JsonElement e)
	{
		if(e == null)
			throw new IllegalArgumentException("The clone argument cannot be null");
		
		delayedString = e.delayedString;
		delayedIndex = e.delayedIndex;
	}
	
	/*********************************
	 * Constructors for JSON parsing *
	 *********************************/
	
	/**
	 * Creates a new JsonElement and parses the string immediately if not delayed.
	 * @param str The string to parse
	 * @param startIndex The starting index of the element in the string
	 * @param delayed Whether or not the parsing is delayed
	 * @throws JsonParseException if there was an error when parsing the string
	 * @throws IllegalArgumentException if the string is null
	 */
	protected JsonElement(String str, int startIndex, boolean delayed) throws JsonParseException
	{
		if(str == null)
			throw new IllegalArgumentException("The JSON string cannot be null");
		
		//The parsing is delayed, so store it
		if(delayed)
		{
			delayedString = str;
			delayedIndex = startIndex;
		}
		//Otherwise parse the string
		else
		{
			length = parse(str, startIndex, false);
		}
	}
	
	/*******************
	 * Parsing methods *
	 *******************/
	
	/**
	 * Parses the string that represents this object.
	 * @param json The string
	 * @param startIndex The starting index of the element in the string
	 * @param delayed Whether or not the parsing was delayed
	 * @throws JsonParseException if there was an error when parsing
	 */
	protected abstract int parse(String json, int startIndex, boolean delayed) throws JsonParseException;
	
	/**
	 * Parses the stored delayed string representation of this object.
	 * @throws JsonParseException if there was an error when parsing
	 */
	private void parseDelayed() throws JsonParseException
	{
		length = parse(delayedString, delayedIndex, true);
		
		//Parsing no longer delayed
		delayedString = null;
		delayedIndex = -1;
	}
	
	/**************************
	 * Parsing helper methods *
	 **************************/
	
	/**
	 * Returns the raw (character) length of this element (the length of the original string).
	 * @return The raw length
	 */
	protected int getRawLength()
	{
		if(length < 0)
			length = getRawLength(delayedString, delayedIndex);
		return length;
	}
	
	/**
	 * To be overridden to provide the raw (character) length of the element starting at the starting index.
	 * @param json The JSON being checked
	 * @param startIndex The starting index in the JSON
	 * @return The raw length
	 */
	protected abstract int getRawLength(String json, int startIndex);
	
	/**
	 * Verifies this element is parsed and otherwise parses it.
	 * @throws JsonParseException if there was an error when parsing
	 */
	protected void verifyParseState() throws JsonParseException
	{
		if(isParsingDelayed())
		{
			parseDelayed();
		}
	}
	
	/**
	 * Returns whether or not the parsing of this object is delayed.
	 * Once an object is parse, even after a delayed parse, it is no longer delayed.
	 * @return <code>true</code> if the parsing of this object is delayed, otherwise <code>false</code>
	 */
	public boolean isParsingDelayed()
	{
		return delayedString != null;
	}
	
	/**************************
	 * Object to JSON methods *
	 **************************/
	
	/**
	 * Returns this element and its contents in JSON format.
	 * @return The JSON formatted element
	 */
	@Override
	public String toString()
	{
		if(isParsingDelayed())
			return delayedString;
		else
			return getJSON();
	}
	
	/**
	 * Returns this element and its contents in JSON format.
	 * @return The JSON formatted element
	 */
	public abstract String getJSON();
}
