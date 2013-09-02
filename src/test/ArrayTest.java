package test;

import static org.junit.Assert.*;
import org.junit.*;

import net.enigmablade.jsonic.*;

public class ArrayTest
{
	@Test
	public void testParse()
	{
		JsonArray obj;
		
		//Empty cases
		String[] emptyCases = {"[]", "[ ]", "[  ]", "[   ]", "[\t]", "[\t\t]", "[\t\t\t]", "[\n]", "[\n\n]", "[\n\n\n]", "[\n\t\n]"};
		for(int n = 0; n < emptyCases.length; n++)
		{
			assertNotNull("Failed to parse empty case "+n+" ("+previousError+"): "+emptyCases[n], obj = testParseHelper(emptyCases[n]));
			assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 0, obj.size());
			}
			catch(JsonException e)
			{
				fail("Failed to get value");
			}
		}
		
		//String cases
		String[] stringCases = {"[\"value\"]", "[ \"value\" ]", "[\t\"value\"\t]", "[\n\"value\"\n]", "[\n\t\"value\"\n]"};
		for(int n = 0; n < stringCases.length; n++)
		{
			obj = testParseHelper(stringCases[n]);
			assertNotNull("Failed to parse single case "+n+" ("+previousError+"): "+stringCases[n], obj);
			assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 1, obj.size());
				
				String value = obj.getString(0);
				assertEquals("value", value);
			}
			catch(JsonException e)
			{
				fail("Failed to get value");
			}
		}
		
		//Multiple strings
		String[] multiStringCases = {"[\"value\",\"value2\"]", "[ \"value\", \"value2\" ]", "[\t\"value\",\t\"value2\"\t]", "[\n\"value\",\n\"value2\"\n]", "[\n\t\"value\",\n\t\"value2\"\n]"};
		for(int n = 0; n < multiStringCases.length; n++)
		{
			obj = testParseHelper(multiStringCases[n]);
			assertNotNull("Failed to parse single case "+n+" ("+previousError+"): "+multiStringCases[n], obj);
			assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 2, obj.size());
				
				String value = obj.getString(0);
				assertEquals("value", value);
				
				String value2 = obj.getString(1);
				assertEquals("value2", value2);
			}
			catch(JsonException e)
			{
				fail("Failed to get value");
			}
		}
		
		//Nested objects
		String[] objectCases = {"[{\"key11\":\"value11\",\"key12\":\"value12\"},{\"key21\":\"value21\",\"key22\":\"value22\"}]"};
		for(int n = 0; n < objectCases.length; n++)
		{
			assertNotNull("Failed to parse array case "+n+" ("+previousError+"): "+objectCases[n], obj = testParseHelper(objectCases[n]));
			assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 2, obj.size());
				
				JsonObject obj1 = obj.getObject(0);
				assertNotNull("Value not found", obj1);
				assertFalse(obj1.isParsingDelayed());
				assertEquals("Invalid size,", 2, obj1.size());
				
				assertEquals("value11", obj1.get("key11"));
				assertEquals("value12", obj1.get("key12"));
				
				JsonObject obj2 = obj.getObject(1);
				assertNotNull("Value not found", obj2);
				assertFalse(obj2.isParsingDelayed());
				assertEquals("Invalid size,", 2, obj2.size());
				
				assertEquals("value21", obj2.get("key21"));
				assertEquals("value22", obj2.get("key22"));
			}
			catch(JsonException e)
			{
				fail("Failed to get value: "+e.toString());
			}
		}
	}
	
	/*
	 * Helper things
	 */
	
	private String previousError;
	
	private JsonArray testParseHelper(String objStr)
	{
		try
		{
			return new TestArray(objStr, false);
		}
		catch(JsonParseException e)
		{
			previousError = e.getMessage();
			return null;
		}
	}
	
	private class TestArray extends JsonArray
	{
		public TestArray(String objStr, boolean delayed) throws JsonParseException
		{
			super(objStr, 0, delayed);
		}
	}
}
