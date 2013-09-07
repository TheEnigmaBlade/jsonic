package test;

import static org.junit.Assert.*;
import org.junit.*;

import net.enigmablade.jsonic.*;

public class ArrayTest
{
	/*****************
	 * Parsing tests *
	 *****************/
	
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
	
	/******************
	 * Creation tests *
	 ******************/
	
	@Test
	public void testCreate()
	{
		try
		{
			//Constructors
			JsonArray array = new JsonArray();
			assertEquals(0, array.size());
			
			JsonArray array2 = new JsonArray(57);
			assertEquals(0, array2.size());
			
			//add(Object) + get(int)
			Object addObj = new Object();
			array.add(addObj);
			assertEquals(1, array.size());
			assertEquals(addObj, array.get(0));
			
			Object addObj2 = new Object();
			array.add(addObj2);
			assertEquals(2, array.size());
			assertEquals(addObj2, array.get(1));
			
			//add(JsonObject) + getObject(int)
			JsonObject addJObj = new JsonObject();
			array.add(addJObj);
			assertEquals(3, array.size());
			assertEquals(addJObj, array.getObject(2));
			
			//add(JsonArray) + getArray(int)
			JsonArray addJArray = new JsonArray();
			array.add(addJArray);
			assertEquals(4, array.size());
			assertEquals(addJArray, array.getArray(3));
			
			//add(String) + getString(int)
			String addStr = "Kawaii desu!";
			array.add(addStr);
			assertEquals(5, array.size());
			assertEquals(addStr, array.getString(4));
			
			//add(int) + getLong(int)
			int addInt = 1_2_4_8;
			array.add(addInt);
			assertEquals(6, array.size());
			assertEquals(addInt, array.getLong(5).longValue());
			
			//add(long) + getLong(int)
			long addLong = 1_2_4_8_16_32_64;
			array.add(addLong);
			assertEquals(7, array.size());
			assertEquals(addLong, array.getLong(6).longValue());
			
			//add(float) + getDouble(int)
			float addFloat = 1_2_4_8.0f;
			array.add(addFloat);
			assertEquals(8, array.size());
			assertEquals(addFloat, array.getDouble(7).doubleValue(), 0.01);
			
			//add(double) + getDouble(int)
			double addDouble = 1_2_4_8.16_32_64;
			array.add(addDouble);
			assertEquals(9, array.size());
			assertEquals(addDouble, array.getDouble(8).doubleValue(), 0.0000001);
			
			//add(boolean) + getBoolean(int)
			boolean addBoolean = true;
			array.add(addBoolean);
			assertEquals(10, array.size());
			assertTrue(array.getBoolean(9));
			
			//Clone constructor
			JsonArray array3 = new JsonArray(array);
			assertEquals(10, array3.size());
			assertEquals(addObj, array3.get(0));
			assertEquals(addObj2, array3.get(1));
			assertEquals(addJObj, array3.getObject(2));
			assertEquals(addJArray, array3.getArray(3));
			assertEquals(addStr, array3.getString(4));
			assertEquals(addInt, array3.getLong(5).longValue());
			assertEquals(addLong, array3.getLong(6).longValue());
			assertEquals(addFloat, array3.getDouble(7).doubleValue(), 0.01);
			assertEquals(addDouble, array3.getDouble(8).doubleValue(), 0.0000001);
			assertTrue(array3.getBoolean(9));
		}
		catch(JsonException e)
		{
			fail("This shouldn't happen!");
		}
	}
	
	@Test
	public void testCreateFailures()
	{
		//Delayed invalid JSON
		JsonArray array = null;
		try
		{
			array = JsonParser.parseArray("[{]", true);
		}
		catch(JsonParseException e)
		{
			fail("This shouldn't happen!");
		}
		
		//get(int)
		try
		{
			Object o = array.get(0);
			fail("Didn't throw an exception on delayed invalid JSON");
		}
		catch(JsonParseException e){}
		catch(Exception e)
		{
			e.printStackTrace();
			fail("Wrong exception!");
		}
		
		//getObject(int)
		try
		{
			array.getObject(0);
			fail("Didn't throw an exception on delayed invalid JSON");
		}
		catch(JsonParseException e){}
		catch(Exception e)
		{
			fail("Wrong exception!");
		}
		
		//getArray(int)
		try
		{
			array.getArray(0);
			fail("Didn't throw an exception on delayed invalid JSON");
		}
		catch(JsonParseException e){}
		catch(Exception e)
		{
			fail("Wrong exception!");
		}
		
		//getString(int)
		try
		{
			array.getString(0);
			fail("Didn't throw an exception on delayed invalid JSON");
		}
		catch(JsonParseException e){}
		catch(Exception e)
		{
			fail("Wrong exception!");
		}
		
		//getLong(int)
		try
		{
			array.getLong(0);
			fail("Didn't throw an exception on delayed invalid JSON");
		}
		catch(JsonParseException e){}
		catch(Exception e)
		{
			fail("Wrong exception!");
		}
		
		//getDouble(int)
		try
		{
			array.getDouble(0);
			fail("Didn't throw an exception on delayed invalid JSON");
		}
		catch(JsonParseException e){}
		catch(Exception e)
		{
			fail("Wrong exception!");
		}
		
		//getBoolean(int)
		try
		{
			array.getBoolean(0);
			fail("Didn't throw an exception on delayed invalid JSON");
		}
		catch(JsonParseException e){}
		catch(Exception e)
		{
			fail("Wrong exception!");
		}
	}
}
