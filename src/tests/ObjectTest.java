package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import net.enigmablade.jsonic.*;

public class ObjectTest
{
	@Test
	public void testParse()
	{
		//basicTests(false);
	}
	
	@Test
	public void testDelayedParse()
	{
		basicTests(true);
	}
	
	private void basicTests(boolean delayed)
	{
		JsonObject obj;
		
		//Empty cases
		String[] emptyCases = {"{}", "{ }", "{  }", "{   }", "{\t}", "{\t\t}", "{\t\t\t}", "{\n}", "{\n\n}", "{\n\n\n}", "{\n\t\n}"};
		for(int n = 0; n < emptyCases.length; n++)
		{
			assertNotNull("Failed to parse empty case "+n+" ("+previousError+"): "+emptyCases[n], obj = testParseHelper(emptyCases[n], delayed));
			if(delayed)
				assertTrue(obj.isParsingDelayed());
			else
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
			obj = testParseHelper(stringCases[n], delayed);
			assertNotNull("Failed to parse single case "+n+" ("+previousError+"): "+stringCases[n], obj);
			if(delayed)
				assertTrue(obj.isParsingDelayed());
			else
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
			obj = testParseHelper(multiStringCases[n], delayed);
			assertNotNull("Failed to parse single case "+n+" ("+previousError+"): "+multiStringCases[n], obj);
			if(delayed)
				assertTrue(obj.isParsingDelayed());
			else
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
			assertNotNull("Failed to parse object case "+n+" ("+previousError+"): "+objectCases[n], obj = testParseHelper(objectCases[n], delayed));
			if(delayed)
				assertTrue(obj.isParsingDelayed());
			else
				assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 1, obj.size());
				
				JsonObject object = obj.getObject("key");
				assertNull("Value found", object);
				
				object = obj.getObject("body");
				assertNotNull("Value not found", object);
				
				if(delayed)
				{
					assertTrue(object.isParsingDelayed());
					assertFalse(obj.isParsingDelayed());
				}
				else
				{
					assertFalse(object.isParsingDelayed());
					assertFalse(obj.isParsingDelayed());
				}
				assertEquals("Invalid size,", 1, object.size());
				
				String value = object.getString("key");
				assertEquals("value", value);
			}
			catch(JsonException e)
			{
				fail("Failed to get value on test "+n+": "+e.getMessage());
			}
		}
		
		//Nested array cases
		String[] arrayCases = {"{\"body\":[{\"key\":\"value\"},{\"key2\":\"value2\"}]}"};
		for(int n = 0; n < arrayCases.length; n++)
		{
			assertNotNull("Failed to parse array case "+n+" ("+previousError+"): "+arrayCases[n], obj = testParseHelper(arrayCases[n], delayed));
			if(delayed)
				assertTrue(obj.isParsingDelayed());
			else
				assertFalse(obj.isParsingDelayed());
			
			try
			{
				assertEquals("Invalid size,", 1, obj.size());
				
				Object object = obj.get("key");
				assertNull("Value found", object);
				
				JsonArray array = obj.getArray("body");
				assertNotNull("Value not found", array);
				
				if(delayed)
				{
					assertTrue(array.isParsingDelayed());
					assertFalse(obj.isParsingDelayed());
				}
				else
				{
					assertFalse(array.isParsingDelayed());
					assertFalse(obj.isParsingDelayed());
				}
				assertEquals("Invalid size,", 2, array.size());
			}
			catch(JsonException e)
			{
				fail("Failed to get value on test "+n+": "+e.getMessage());
			}
		}
	}
	
	/*
	 * Helper things
	 */
	
	private String previousError;
	
	private JsonObject testParseHelper(String objStr, boolean delayed)
	{
		try
		{
			return new TestObject(objStr, delayed);
		}
		catch(JsonParseException e)
		{
			e.printStackTrace();
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
