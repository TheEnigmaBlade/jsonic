package net.enigmablade.jsonic;

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
			if(value instanceof JsonObject)
				type = ValueType.OBJECT;
			else if(value instanceof JsonArray)
				type = ValueType.ARRAY;
			else if(value instanceof String)
				type = ValueType.STRING;
			else if(value instanceof Long || value instanceof Integer)
				type = ValueType.LONG;
			else if(value instanceof Double || value instanceof Float)
				type = ValueType.DOUBLE;
			else if(value instanceof Boolean)
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
		return new Value(ValueType.LONG, value);
	}
	
	protected static Value createValue(int value)
	{
		return new Value(ValueType.LONG, value);
	}
	
	protected static Value createValue(double value)
	{
		return new Value(ValueType.DOUBLE, value);
	}
	
	protected static Value createValue(float value)
	{
		return new Value(ValueType.DOUBLE, value);
	}
	
	protected static Value createValue(boolean value)
	{
		return new Value(ValueType.BOOLEAN, value);
	}
	
	/*
	 * Public helper methods
	 */
	
	public boolean isJsonObject(Object o)
	{
		return o instanceof JsonObject;
	}
	
	public boolean isJsonArray(Object o)
	{
		return o instanceof JsonArray;
	}
}
