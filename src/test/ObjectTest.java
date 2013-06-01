package test;

import static org.junit.Assert.*;

import org.junit.Test;

import net.enigmablade.jsonic.*;

public class ObjectTest
{
	@Test
	public void testParse()
	{
		JsonObject obj;
		
		//Empty cases
		String[] emptyCases = {"{}", "{ }", "{  }", "{   }", "{\t}", "{\t\t}", "{\t\t\t}", "{\n}", "{\n\n}", "{\n\n\n}", "{\n\t\n}"};
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
		String[] stringCases = {"{\"key\":\"value\"}", "{ \"key\":\"value\" }", "{\t\"key\":\"value\"\t}", "{\n\"key\":\"value\"\n}", "{\n\t\"key\":\"value\"\n}"};
		for(int n = 0; n < stringCases.length; n++)
		{
			obj = testParseHelper(stringCases[n]);
			assertNotNull("Failed to parse single case "+n+" ("+previousError+"): "+stringCases[n], obj);
			assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 1, obj.size());
				
				String value = obj.getString("key");
				assertEquals("value", value);
			}
			catch(JsonException e)
			{
				fail("Failed to get value");
			}
		}
		
		//Multiple string cases
		String[] multiStringCases = {"{\"key\":\"value\",\"key2\":\"value2\"}"};
		for(int n = 0; n < multiStringCases.length; n++)
		{
			obj = testParseHelper(multiStringCases[n]);
			assertNotNull("Failed to parse single case "+n+" ("+previousError+"): "+multiStringCases[n], obj);
			assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 2, obj.size());
				
				String value = obj.getString("key");
				assertEquals("value", value);
				
				String value2 = obj.getString("key2");
				assertEquals("value2", value2);
			}
			catch(JsonException e)
			{
				fail("Failed to get value");
			}
		}
		
		//Nested object cases
		String[] objectCases = {"{\"body\":{\"key\":\"value\"}}", "{\n\t\"body\":{\n\t\t\"key\":\"value\"\n\t}\n}"};
		for(int n = 0; n < objectCases.length; n++)
		{
			assertNotNull("Failed to parse object case "+n+" ("+previousError+"): "+objectCases[n], obj = testParseHelper(objectCases[n]));
			assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 1, obj.size());
				
				JsonObject object = obj.getObject("key");
				assertNull("Value found", object);
				
				object = obj.getObject("body");
				assertNotNull("Value not found", object);
				
				assertFalse(object.isParsingDelayed());
				assertEquals("Invalid size,", 1, object.size());
				
				String value = object.getString("key");
				assertEquals("value", value);
			}
			catch(JsonException e)
			{
				fail("Failed to get value: "+e.toString());
			}
		}
		
		//Nested array cases
		String[] arrayCases = {"{\"body\":[{\"key\":\"value\"},{\"key2\":\"value2\"}]}"};
		for(int n = 0; n < arrayCases.length; n++)
		{
			assertNotNull("Failed to parse array case "+n+" ("+previousError+"): "+arrayCases[n], obj = testParseHelper(arrayCases[n]));
			assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 1, obj.size());
				
				Object object = obj.get("key");
				assertNull("Value found", object);
				
				JsonArray array = obj.getArray("body");
				assertNotNull("Value not found", array);
				
				assertFalse(array.isParsingDelayed());
				assertEquals("Invalid size,", 2, array.size());
			}
			catch(JsonException e)
			{
				fail("Failed to get value: "+e.toString());
			}
		}
	}
	
	/*@Test
	public void testDelayedParse()
	{
		fail("Not yet implemented");
	}*/
	
	/*
	 * Helper things
	 */
	
	private String previousError;
	
	private JsonObject testParseHelper(String objStr)
	{
		try
		{
			return new TestObject(objStr, false);
		}
		catch(JsonParseException e)
		{
			previousError = e.getMessage();
			return null;
		}
	}
	
	private JsonObject testParseDelayedHelper(String objStr)
	{
		try
		{
			return new TestObject(objStr, true);
		}
		catch(JsonParseException e)
		{
			previousError = e.getMessage();
			return null;
		}
	}
	
	private class TestObject extends JsonObject
	{
		public TestObject(String objStr, boolean delayed) throws JsonParseException
		{
			super(objStr, 0, delayed);
		}
	}
}
