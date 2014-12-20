package net.enigmablade.jsonic;

import java.io.*;

/**
 * A basic parsable JSON element for which the parsing can be delayed.
 * 
 * @author Enigma
 */
public abstract class JsonElement implements Cloneable, Serializable
{
	private static final long serialVersionUID = 8291596876632597802L;
	
	//Parsing info
	private String delayedString = null;
	private int delayedIndex = -1;
	
	private char openingChar, closingChar;
	
	//Other info
	private int length = -1;
	
	/*********************************
	 * Constructors for JSON Creation*
	 *********************************/
	
	/**
	 * Creates an empty JsonElement.
	 * @param openChar The first char of the element
	 * @param closingChar The last char of the element
	 */
	public JsonElement(char openChar, char closingChar)
	{
		delayedString = null;
		
		this.openingChar = openChar;
		this.closingChar = closingChar;
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
		
		openingChar = e.openingChar;
		closingChar = e.closingChar;
	}
	
	/*********************************
	 * Constructors for JSON parsing *
	 *********************************/
	
	/**
	 * Creates a new JsonElement and parses the string immediately if not delayed.
	 * @param str The string to parse
	 * @param startIndex The starting index of the element in the string
	 * @param delayed Whether or not the parsing is delayed
	 * @param openChar The first char of the element
	 * @param closingChar The last char of the element
	 * @throws JsonParseException if there was an error when parsing the string
	 * @throws IllegalArgumentException if the string is null
	 */
	protected JsonElement(String str, int startIndex, boolean delayed, char openChar, char closingChar) throws JsonParseException
	{
		this(openChar, closingChar);
		
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
	 * Returns the raw (character) length of the object, starting at the starting index.
	 * @param json The JSON being checked
	 * @param startIndex The starting index in the JSON
	 * @return The length of the object
	 * @see JsonElement#getRawLength(String, int)
	 */
	private int getRawLength(String json, int startIndex)
	{
		char c;
		int n = 1, objCount = 0;
		for(; startIndex < json.length(); startIndex++, n++)
		{
			c = json.charAt(startIndex);
			
			if(c == closingChar)
				objCount--;
			else if(c == openingChar)
				objCount++;
			
			if(objCount <= 0)
				break;
		}
		
		//Not all elements were closed (UH OH!)
		if(objCount > 0)
			return 0;
		
		//Otherwise return the length
		return n;
	}
	
	/**
	 * Verifies this element is parsed and otherwise parses it.
	 * @throws JsonParseException if there was an error when parsing
	 */
	protected void verifyParseState() throws JsonParseException
	{
		if(isParsingDelayed())
			parseDelayed();
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
	
	/**
	 * Returns the string being stored by this element if it's delayed.
	 * @return This element's string if delayed, otherwise <code>null</code>.
	 */
	protected String getDelayedString()
	{
		if(isParsingDelayed())
			return delayedString.substring(delayedIndex, delayedIndex+getRawLength(delayedString, delayedIndex));
		return null;
	}
	
	/**************************
	 * Object to JSON methods *
	 **************************/
	
	/**
	 * Returns this element and its contents in JSON format.<br>
	 * Equivalent to @link #getJSON().
	 * @return The JSON formatted element
	 */
	@Override
	public String toString()
	{
		return getJSON();
	}
	
	/**
	 * Returns this element and its contents in JSON format.<br>
	 * Equivalent to @link #toString().
	 * @return The JSON formatted element
	 */
	public String getJSON()
	{
		if(isParsingDelayed())
			return getDelayedString();
		else
			return toJSON();
	}
	
	/**
	 * Returns this element and its contents in JSON format.<br>
	 * To be overridden by subclasses for use in @link #getJSON() and @link #toString().
	 * Implementations should ignore the delayed state of the element.
	 * @return The JSON formatted element
	 */
	protected abstract String toJSON();
	
	/********************
	 * Object overrides *
	 ********************/
	
	/**
	 * Checks whether this element and the given element are equal.
	 * Two elements are equal if and only if they are delayed and represented by identical delayed strings.
	 * Other element states cannot reliably be used to determine equality.
	 * 
	 * @param o The element to check against.
	 * @return <code>true</code> if the two elements are equal, otherwise <code>false</code>.
	 * 
	 * @see #getDelayedString()
	 */
	@Override
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof JsonElement))
			return false;
		
		JsonElement e = (JsonElement)o;
		if(isParsingDelayed() && e.isParsingDelayed())
			return getDelayedString().equals(e.getDelayedString());
		return false;
	}
	
	/**
	 * If this element is delayed, returns the hash code of its delayed string.
	 * Otherwise the default hash code method is used.
	 * 
	 * @return The element's hash code.
	 * 
	 * @see #getDelayedString()
	 * @see String#hashCode()
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		if(isParsingDelayed())
			return getDelayedString().hashCode();
		return super.hashCode();
	}
}
