package tests;

import static org.junit.Assert.*;
import java.lang.reflect.*;
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
			array.get(0);
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
	
	/***************
	 * Value tests *
	 ***************/
	
	@Test
	public void testValues()
	{
		//Longs
		String[] objectsJson =		{ "[{}]",				"[{}, {}]",								"[{}, {}, {}]"	};
		JsonObject[][] objectsEx =	{  {new JsonObject()},	 {new JsonObject(), new JsonObject()},	 {new JsonObject(), new JsonObject(), new JsonObject()}	};
		for(int objectsJsoff = 0; objectsJsoff < objectsJson.length; objectsJsoff++)
		{
			String json = objectsJson[objectsJsoff];
			JsonObject[] ex = objectsEx[objectsJsoff];
			
			JsonArray parsed = new TestArray(json, false);
			assertEquals(ex.length, parsed.size());
			for(int e = 0; e < ex.length; e++)
			{
				//Valid
				assertEquals(ex[e], ((JsonObject)parsed.get(e)));
				assertEquals(ex[e], parsed.getObject(e));
				
				//Invalid
				invalidValueHelper(parsed, "getArray", e);
				invalidValueHelper(parsed, "getString", e);
				invalidValueHelper(parsed, "getLong", e);
				invalidValueHelper(parsed, "getInt", e);
				invalidValueHelper(parsed, "getDouble", e);
				invalidValueHelper(parsed, "getFloat", e);
				invalidValueHelper(parsed, "getBoolean", e);
			}
		}
		
		//Strings
		String[] stringsJson =	{ "[\"test1\"]",	"[\"test1\", \"test2\"]",	"[\"test1\", \"test2\", \"test3\"]"	};
		String[][] stringsEx =	{  {"test1"},		 {"test1", "test2"},		 {"test1", "test2", "test3"}		};
		for(int stringsJsoff = 0; stringsJsoff < stringsJson.length; stringsJsoff++)
		{
			String json = stringsJson[stringsJsoff];
			String[] ex = stringsEx[stringsJsoff];
			
			JsonArray parsed = new TestArray(json, false);
			assertEquals(ex.length, parsed.size());
			for(int e = 0; e < ex.length; e++)
			{
				//Valid
				assertEquals(ex[e], (String)parsed.get(e));
				assertEquals(ex[e], parsed.getString(e));
				
				//Invalid
				invalidValueHelper(parsed, "getObject", e);
				invalidValueHelper(parsed, "getArray", e);
				invalidValueHelper(parsed, "getLong", e);
				invalidValueHelper(parsed, "getInt", e);
				invalidValueHelper(parsed, "getDouble", e);
				invalidValueHelper(parsed, "getFloat", e);
				invalidValueHelper(parsed, "getBoolean", e);
			}
		}
		
		//Longs
		String[] longsJson =	{ "[1]",	"[1, 2]",	"[1, 2, 3]"	};
		long[][] longsEx =		{  {1},		 {1, 2},	 {1, 2, 3}	};
		for(int intsJsoff = 0; intsJsoff < longsJson.length; intsJsoff++)
		{
			String json = longsJson[intsJsoff];
			long[] ex = longsEx[intsJsoff];
			
			JsonArray parsed = new TestArray(json, false);
			assertEquals(ex.length, parsed.size());
			for(int e = 0; e < ex.length; e++)
			{
				//Valid
				assertEquals(ex[e], ((Long)parsed.get(e)).longValue());
				assertEquals(ex[e], parsed.getLong(e).longValue());
				assertEquals(ex[e], parsed.getInt(e).intValue());
				
				//Invalid
				invalidValueHelper(parsed, "getObject", e);
				invalidValueHelper(parsed, "getArray", e);
				invalidValueHelper(parsed, "getString", e);
				invalidValueHelper(parsed, "getDouble", e);
				invalidValueHelper(parsed, "getFloat", e);
				invalidValueHelper(parsed, "getBoolean", e);
			}
		}
		
		//Floats
		String[] floatsJson =	{ "[1.1]",	"[1.1, 2.2f]",	"[1.1, 2.2f, 3.3d]"	};
		float[][] floatsEx =	{  {1.1f},	 {1.1f, 2.2f},	 {1.1f, 2.2f, 3.3f}	};
		for(int floatsJsoff = 0; floatsJsoff < floatsJson.length; floatsJsoff++)
		{
			String json = floatsJson[floatsJsoff];
			float[] ex = floatsEx[floatsJsoff];
			
			JsonArray parsed = new TestArray(json, false);
			assertEquals(ex.length, parsed.size());
			for(int e = 0; e < ex.length; e++)
			{
				//Valid
				assertEquals(ex[e], ((Double)parsed.get(e)).doubleValue(), 0.01);
				assertEquals(ex[e], parsed.getDouble(e).doubleValue(), 0.01);
				assertEquals(ex[e], parsed.getFloat(e).floatValue(), 0.01f);
				
				//Invalid
				invalidValueHelper(parsed, "getObject", e);
				invalidValueHelper(parsed, "getArray", e);
				invalidValueHelper(parsed, "getString", e);
				invalidValueHelper(parsed, "getLong", e);
				invalidValueHelper(parsed, "getInt", e);
				invalidValueHelper(parsed, "getBoolean", e);
			}
		}
		
		//Booleans
		String[] boolJson =		{ "[true]",	"[true, false]",	"[true, false, true]"	};
		boolean[][] boolEx =	{  {true},	 {true, false},		 {true, false, true}	};
		for(int boolsJsoff = 0; boolsJsoff < boolJson.length; boolsJsoff++)
		{
			String json = boolJson[boolsJsoff];
			boolean[] ex = boolEx[boolsJsoff];
			
			JsonArray parsed = new TestArray(json, false);
			assertEquals(ex.length, parsed.size());
			for(int e = 0; e < ex.length; e++)
			{
				//Valid
				assertEquals(ex[e], ((Boolean)parsed.get(e)).booleanValue());
				assertEquals(ex[e], parsed.getBoolean(e).booleanValue());
				
				//Invalid
				invalidValueHelper(parsed, "getObject", e);
				invalidValueHelper(parsed, "getArray", e);
				invalidValueHelper(parsed, "getString", e);
				invalidValueHelper(parsed, "getLong", e);
				invalidValueHelper(parsed, "getInt", e);
				invalidValueHelper(parsed, "getDouble", e);
				invalidValueHelper(parsed, "getFloat", e);
			}
		}
		
		//Nulls
		String[] nullJson =	{ "[null]",	"[null, null]",	"[null, null, null]"	};
		Object[][] nullEx =	{  {null},	 {null, null},	 {null, null, null}		};	//Should I really be expecting anything else?
		for(int nullJsoff = 0; nullJsoff < nullJson.length; nullJsoff++)
		{
			String json = nullJson[nullJsoff];
			Object[] ex = nullEx[nullJsoff];
			
			JsonArray parsed = new TestArray(json, false);
			assertEquals(ex.length, parsed.size());
			for(int e = 0; e < ex.length; e++)
			{
				//Valid
				assertNull(parsed.get(e));
				assertNull(parsed.getObject(e));
				assertNull(parsed.getArray(e));
				assertNull(parsed.getString(e));
				assertNull(parsed.getLong(e));
				assertNull(parsed.getInt(e));
				assertNull(parsed.getDouble(e));
				assertNull(parsed.getFloat(e));
				assertNull(parsed.getBoolean(e));
			}
		}
	}
	
	// Helper things
	
	private class TestArray extends JsonArray
	{
		public TestArray(String objStr, boolean delayed) throws JsonParseException
		{
			super(objStr, 0, delayed);
		}
	}
	
	////Parsing
	
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
			e.printStackTrace();
			return null;
		}
	}
	
	////Values
	
	private void invalidValueHelper(Object obj, String methodName, int i)
	{
		try
		{
			Method m = obj.getClass().getMethod(methodName, int.class);
			m.invoke(obj, i);
			fail("Didn't cause an invalid value exception.");
		}
		catch(NoSuchMethodException | SecurityException e)
		{
			System.err.println("Error when getting the method. Uh oh!");
			e.printStackTrace();
			fail();
		}
		catch(IllegalAccessException | IllegalArgumentException e)
		{
			System.err.println("Error when invoking the method. Uh oh!");
			e.printStackTrace();
			fail();
		}
		catch(InvocationTargetException e)
		{
			assertEquals(JsonTypeException.class, e.getTargetException().getClass());
		}
	}
}
