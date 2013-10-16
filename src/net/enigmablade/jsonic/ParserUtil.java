package net.enigmablade.jsonic;

import net.enigmablade.jsonic.ValueUtil.*;

/**
 * Non-public utilities and constants used by the parser.
 * 
 * @author Enigma
 */
public class ParserUtil
{
	//Format information
	
	protected static final char OBJECT_OPEN = '{';
	protected static final char OBJECT_CLOSE = '}';
	protected static final char ARRAY_OPEN = '[';
	protected static final char ARRAY_CLOSE = ']';
	
	protected static final char OBJECT_MAP = ':';
	protected static final char SPLIT = ',';
	
	protected static final char STRING_1 = '"', STRING_2 = '\'';;
	protected static boolean isStringChar(char c)
	{
		return c == STRING_1 || c == STRING_2;
	}
	
	protected static final char FLOATING_POINT_SEPARATOR = '.';
	protected static final char NUMBER_SPACER_1 = ',', NUMBER_SPACER_2 = '_';
	
	
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
	
	protected static Value parseUnknown(String str) throws JsonParseException
	{
		NaN: switch(str.charAt(0))
		{
			//True
			case 't':
				if(str.length() == 4 && str.charAt(1) == 'r' && str.charAt(2) == 'u' && str.charAt(3) == 'e')
					return ValueUtil.createValue(true);
				break;
			
			//False
			case 'f':
				if(str.length() == 5 && str.charAt(1) == 'a' && str.charAt(2) == 'l' && str.charAt(3) == 's' && str.charAt(4) == 'e')
					return ValueUtil.createValue(false);
				break;
			
			//Null
			case 'n':
				if(str.length() == 4 && str.charAt(1) == 'u' && str.charAt(2) == 'l' && str.charAt(3) == 'l')
					return ValueUtil.createNullValue();
				break;
			
			//A number
			default:
				boolean hasDecimal = false;
				for(char c : str.toCharArray())
				{
					switch(c)
					{
						//Decimal
						case FLOATING_POINT_SEPARATOR:
							//Can't have two decimal points, NaN
							if(hasDecimal)
								break NaN;
							hasDecimal = true;
							break;
						
						//Spacers, ignore
						case NUMBER_SPACER_1:
						case NUMBER_SPACER_2:
							break;
						
						//Check if valid digit
						default:
							if(c < '0' && c > '9')
								break NaN;
					}
				}
				
				//Return a double if a decimal was found
				if(hasDecimal)
					return ValueUtil.createValue(Double.parseDouble(str));
				//Otherwise return a long
				return ValueUtil.createValue(Long.parseLong(str));
		}
		
		throw new JsonParseException(JsonParseException.Type.UNKNOWN_VALUE_TYPE, str);
	}
}
