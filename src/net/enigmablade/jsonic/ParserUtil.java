package net.enigmablade.jsonic;

import java.util.regex.*;

import net.enigmablade.jsonic.ValueUtil.*;

public class ParserUtil
{
	//Format information
	
	protected static final char OBJECT_OPEN = '{';
	protected static final char OBJECT_CLOSE = '}';
	protected static final char ARRAY_OPEN = '[';
	protected static final char ARRAY_CLOSE = ']';
	
	protected static final char STRING_1 = '"', STRING_2 = '\'';;
	
	protected static boolean isStringChar(char c)
	{
		return c == STRING_1 || c == STRING_2;
	}
	
	protected static final char OBJECT_MAP = ':';
	protected static final char SPLIT = ',';
	
	//String navigation
	
	protected static int nextNonWhitespace(String s, int start)
	{
		for(; start < s.length() && Character.isWhitespace(s.charAt(start)); start++);
		return start;
	}
	
	//String extraction
	
	protected static String getStringBlock(String s, int startIndex)
	{
		char boundaryChar = s.charAt(startIndex);
		if(!isStringChar(boundaryChar))
			return null;
		
		startIndex++;
		
		StringBuilder block = new StringBuilder();
		boolean escaped = false;
		for(char c; startIndex < s.length() && ((c = s.charAt(startIndex)) != boundaryChar || escaped); startIndex++)
		{
			block.append(c);
			escaped = c == '\\' && (startIndex+1 < s.length() ? s.charAt(startIndex+1) == boundaryChar : false);
		}
		
		return block.toString();
	}
	
	protected static String getObjectBlock(String s, int startIndex)
	{
		char boundaryChar = s.charAt(startIndex);
		if(boundaryChar != OBJECT_OPEN)
			return null;
		
		StringBuilder block = new StringBuilder();
		int objCount = 0;
		for(char c; startIndex < s.length(); startIndex++)
		{
			c = s.charAt(startIndex);
			block.append(c);
			
			if(c == OBJECT_CLOSE)
				objCount--;
			else if(c == OBJECT_OPEN)
				objCount++;
			
			if(objCount <= 0)
				break;
		}
		
		return block.toString();
	}
	
	protected static String getArrayBlock(String s, int startIndex)
	{
		char boundaryChar = s.charAt(startIndex);
		if(boundaryChar != ARRAY_OPEN)
			return null;
		
		StringBuilder block = new StringBuilder();
		int objCount = 0;
		for(char c; startIndex < s.length(); startIndex++)
		{
			c = s.charAt(startIndex);
			block.append(c);
			
			if(c == ARRAY_CLOSE)
				objCount--;
			else if(c == ARRAY_OPEN)
				objCount++;
			
			if(objCount <= 0)
				break;
		}
		
		return block.toString();
	}
	
	protected static String getUnknownBlock(String s, int startIndex)
	{
		StringBuilder block = new StringBuilder();
		for(int i = startIndex; i < s.length() && s.charAt(i) != OBJECT_MAP && s.charAt(i) != SPLIT && s.charAt(i) != OBJECT_CLOSE && s.charAt(i) != ARRAY_CLOSE && !Character.isWhitespace(s.charAt(i)); i++)
		{
			block.append(s.charAt(i));
		}
		return block.toString();
	}
	
	//Number parsing
	
	private static final Pattern longPattern = Pattern.compile("-?\\d+");
	private static final Pattern doublePattern = Pattern.compile("-?\\d*(\\.\\d+)?");
	
	protected static boolean isLong(String str)
	{
		return longPattern.matcher(str).matches();
	}
	
	protected static boolean isDouble(String str)
	{
		return doublePattern.matcher(str).matches();
	}
	
	protected static Value parseUnknown(String str) throws JsonParseException
	{
		switch(str.charAt(0))
		{
			//True
			case 't':
				if("true".equals(str))
					return ValueUtil.createValue(true);
				break;
			
			//False
			case 'f':
				if("false".equals(str))
					return ValueUtil.createValue(false);
				break;
			
			//Null
			case 'n':
				if("null".equals(str))
					return ValueUtil.createNullValue();
				break;
			
			//A number
			default:
				/*boolean isNumeric = true, isFloating = false;
				for(int n = 0; n < str.length(); n++)
				{
					char c = str.charAt(n);
					if((c == '-' && n != 0) || c < '0' || c > '9' || (c == '.' && isFloating))
					{
						isNumeric = false;
						break;
					}
					if(c == '.')
						isFloating = true;
				}
				if(isNumeric)
				{
					if(isFloating)
					{
						double value = Double.parseDouble(str);
						return ValueUtil.createValue(value);
					}
					long value = Long.parseLong(str);
					return ValueUtil.createValue(value);
				}*/
				
				if(isLong(str))
				{
					long value = Long.parseLong(str);
					return ValueUtil.createValue(value);
				}
				if(isDouble(str))
				{
					double value = Double.parseDouble(str);
					return ValueUtil.createValue(value);
				}
		}
		
		throw new JsonParseException(JsonParseException.Type.UNKNOWN_VALUE_TYPE, str);
	}
}
