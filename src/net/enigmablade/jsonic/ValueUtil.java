package net.enigmablade.jsonic;

/**
 * Mostly non-public utilities for element type checking and conversion.
 * 
 * @author Enigma
 */
public class ValueUtil
{
	/**
	 * Supported value types.
	 * @author EnigmaBlade
	 */
	protected enum ValueType { OBJECT, ARRAY, STRING, LONG, DOUBLE, BOOLEAN, NULL };
	
	/**
	 * A basic wrapper for an object that also stores the object's type.<br>
	 * DOES NOT TYPE-CHECK and assumes value is non-null unless the type is NULL.
	 * @author EnigmaBlade
	 */
	public static class Value
	{
		/**
		 * The value type.
		 * @see ValueType
		 */
		public ValueType type;
		/**
		 * The value.
		 */
		public Object value;
		
		/**
		 * Creates a new value wrapper.
		 * @param type The value type
		 * @param value The value
		 */
		public Value(ValueType type, Object value)
		{
			this.type = type;
			this.value = value;
		}
		
		/**
		 * Returns a string representation of this value:<br>
		 * - If type is NULL, returns "<code>null</code>"<br>
		 * - If type is STRING, returns "<code>"value"</code>"<br>
		 * - Otherwise, returns the result of <code>value.toString()</code>
		 * @return The string representation
		 */
		@Override
		public String toString()
		{
			switch(type)
			{
				case NULL:
					return "null";
					
				case STRING:
					return new StringBuilder().append(ParserUtil.STRING_1).append(value).append(ParserUtil.STRING_1).toString();
					
				default:
					return value.toString();
			}
		}
	}
	
	/**
	 * Creates a new Value from the given value with the appropriate ValueType.
	 * @param value The value converted
	 * @return The new Value wrapper
	 */
	protected static Value createValue(Object value)
	{
		ValueType type = ValueType.NULL;
		if(value != null)
		{
			if(isJsonObject(value))
				type = ValueType.OBJECT;
			else if(isJsonArray(value))
				type = ValueType.ARRAY;
			else if(isString(value))
				type = ValueType.STRING;
			else if(isLong(value) || isInteger(value))
				type = ValueType.LONG;
			else if(isDouble(value) || isFloat(value))
				type = ValueType.DOUBLE;
			else if(isBoolean(value))
				type = ValueType.BOOLEAN;
		}
		
		return new Value(type, value);
	}
	
	protected static Value createNullValue()
	{
		return new Value(ValueType.NULL, null);
	}
	
	protected static Value andThenThereWereNaN()
	{
		return new Value(ValueType.DOUBLE, Double.NaN);
	}
	
	protected static Value createValue(JsonObject value)
	{
		return new Value(ValueType.OBJECT, value);
	}
	
	protected static Value createValue(JsonArray value)
	{
		return new Value(ValueType.ARRAY, value);
	}
	
	protected static Value createValue(String value)
	{
		return new Value(ValueType.STRING, value);
	}
	
	protected static Value createValue(long value)
	{
		return new Value(ValueType.LONG, new Long(value));
	}
	
	protected static Value createValue(int value)
	{
		return new Value(ValueType.LONG, new Long(value));
	}
	
	protected static Value createValue(double value)
	{
		return new Value(ValueType.DOUBLE, new Double(value));
	}
	
	protected static Value createValue(float value)
	{
		return new Value(ValueType.DOUBLE, new Double(value));
	}
	
	protected static Value createValue(boolean value)
	{
		return new Value(ValueType.BOOLEAN, new Boolean(value));
	}
	
	/*
	 * Public helper methods
	 */
	
	/**
	 * Returns whether or not the given object is an instance of JsonObject.
	 * This method is for convenience.
	 * @param o The object
	 * @return <code>true</code> if the object is a JsonObject, otherwise <code>false</code>
	 */
	public static boolean isJsonObject(Object o)
	{
		return o instanceof JsonObject;
	}
	
	/**
	 * Returns whether or not the given object is an instance of JsonArray.
	 * This method is for convenience.
	 * @param o The object
	 * @return <code>true</code> if the object is a JsonArray, otherwise <code>false</code>
	 */
	public static boolean isJsonArray(Object o)
	{
		return o instanceof JsonArray;
	}
	
	/**
	 * Returns whether or not the given object is an instance of String.
	 * This method is for convenience.
	 * @param o The object
	 * @return <code>true</code> if the object is a JsonObject, otherwise <code>false</code>
	 */
	public static boolean isString(Object o)
	{
		return o instanceof String;
	}
	
	/**
	 * Returns whether or not the given object is an instance of Long.
	 * This method is for convenience.
	 * @param o The object
	 * @return <code>true</code> if the object is a JsonObject, otherwise <code>false</code>
	 */
	public static boolean isLong(Object o)
	{
		return o instanceof Long;
	}
	
	/**
	 * Returns whether or not the given object is an instance of Integer.
	 * This method is for convenience.
	 * @param o The object
	 * @return <code>true</code> if the object is a JsonObject, otherwise <code>false</code>
	 */
	public static boolean isInteger(Object o)
	{
		return o instanceof Integer;
	}
	
	/**
	 * Returns whether or not the given object is an instance of Double.
	 * This method is for convenience.
	 * @param o The object
	 * @return <code>true</code> if the object is a JsonObject, otherwise <code>false</code>
	 */
	public static boolean isDouble(Object o)
	{
		return o instanceof Double;
	}
	
	/**
	 * Returns whether or not the given object is an instance of Float.
	 * This method is for convenience.
	 * @param o The object
	 * @return <code>true</code> if the object is a JsonObject, otherwise <code>false</code>
	 */
	public static boolean isFloat(Object o)
	{
		return o instanceof Float;
	}
	
	/**
	 * Returns whether or not the given object is an instance of Boolean.
	 * This method is for convenience.
	 * @param o The object
	 * @return <code>true</code> if the object is a JsonObject, otherwise <code>false</code>
	 */
	public static boolean isBoolean(Object o)
	{
		return o instanceof Boolean;
	}
}
