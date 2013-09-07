package net.enigmablade.jsonic;

/**
 * Mostly non-public utilities for element type checking and conversion.
 * 
 * @author Enigma
 */
public class ValueUtil
{
	protected enum ValueType { OBJECT, ARRAY, STRING, LONG, DOUBLE, BOOLEAN, NULL };
	
	protected static class Value
	{
		public ValueType type;
		public Object value;
		
		public Value(ValueType type, Object value)
		{
			this.type = type;
			this.value = value;
		}
		
		@Override
		public String toString()
		{
			if(type == ValueType.STRING)
				return new StringBuilder().append('"').append(value).append('"').toString();
			return value.toString();
		}
	}
	
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
	
	public static boolean isJsonObject(Object o)
	{
		return o instanceof JsonObject;
	}
	
	public static boolean isJsonArray(Object o)
	{
		return o instanceof JsonArray;
	}
	
	public static boolean isString(Object o)
	{
		return o instanceof String;
	}
	
	public static boolean isLong(Object o)
	{
		return o instanceof Long;
	}
	
	public static boolean isInteger(Object o)
	{
		return o instanceof Integer;
	}
	
	public static boolean isDouble(Object o)
	{
		return o instanceof Double;
	}
	
	public static boolean isFloat(Object o)
	{
		return o instanceof Float;
	}
	
	public static boolean isBoolean(Object o)
	{
		return o instanceof Boolean;
	}
}
