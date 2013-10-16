package tests;

import static org.junit.Assert.*;
import org.junit.*;

import net.enigmablade.jsonic.*;

public class ValueUtilTest extends ValueUtil
{
	@Test
	public void testValue()
	{
		//Object
		
		JsonObject obj = new JsonObject();
		Value objValue = new Value(ValueType.OBJECT, obj);
		assertEquals(ValueType.OBJECT, objValue.type);
		assertEquals(obj, objValue.value);
		assertEquals(obj.toString(), objValue.toString());
		
		//Array
		
		JsonArray array = new JsonArray();
		Value arrayValue = new Value(ValueType.ARRAY, array);
		assertEquals(ValueType.ARRAY, arrayValue.type);
		assertEquals(array, arrayValue.value);
		assertEquals(array.toString(), arrayValue.toString());
		
		//String
		
		String str = "ohayou";
		Value strValue = new Value(ValueType.STRING, str);
		assertEquals(ValueType.STRING, strValue.type);
		assertEquals(str, strValue.value);
		assertEquals("\""+str+"\"", strValue.toString());
		
		//Long/integer
		
		long l = 999999999999999999l;
		Value longValue = new Value(ValueType.LONG, l);
		assertEquals(ValueType.LONG, longValue.type);
		assertEquals(l, longValue.value);
		assertEquals(String.valueOf(l), longValue.toString());
		
		int i = 999999999;
		Value intValue = new Value(ValueType.LONG, i);
		assertEquals(ValueType.LONG, intValue.type);
		assertEquals(i, intValue.value);
		assertEquals(String.valueOf(i), intValue.toString());
		
		//Double/float
		
		double d = 999999999.9;
		Value doubleValue = new Value(ValueType.DOUBLE, d);
		assertEquals(ValueType.DOUBLE, doubleValue.type);
		assertEquals(d, doubleValue.value);
		assertEquals(String.valueOf(d), doubleValue.toString());
		
		float f = 999999999.9f;
		Value floatValue = new Value(ValueType.DOUBLE, f);
		assertEquals(ValueType.DOUBLE, floatValue.type);
		assertEquals(f, floatValue.value);
		assertEquals(String.valueOf(f), floatValue.toString());
		
		//Boolean
		
		boolean bool = true;
		Value booleanValue = new Value(ValueType.BOOLEAN, bool);
		assertEquals(ValueType.BOOLEAN, booleanValue.type);
		assertEquals(bool, booleanValue.value);
		assertEquals(String.valueOf(bool), booleanValue.toString());
		
		boolean bool2 = false;
		Value booleanValue2 = new Value(ValueType.BOOLEAN, bool2);
		assertEquals(ValueType.BOOLEAN, booleanValue2.type);
		assertEquals(bool2, booleanValue2.value);
		assertEquals(String.valueOf(bool2), booleanValue2.toString());
		
		//Null
		
		Value nullValue = new Value(ValueType.NULL, null);
		assertEquals(ValueType.NULL, nullValue.type);
		assertNull(nullValue.value);
		assertEquals("null", nullValue.toString());
	}
	
	@Test
	public void testGeneralCreationHelper()
	{
		//Object
		
		Object obj = new JsonObject();
		Value objValue = createValue(obj);
		assertEquals(ValueType.OBJECT, objValue.type);
		assertEquals(obj, objValue.value);
		
		//Array
		
		Object array = new JsonArray();
		Value arrayValue = new Value(ValueType.ARRAY, array);
		assertEquals(ValueType.ARRAY, arrayValue.type);
		assertEquals(array, arrayValue.value);
		
		//String
		
		Object str = "ohayou";
		Value strValue = new Value(ValueType.STRING, str);
		assertEquals(ValueType.STRING, strValue.type);
		assertEquals(str, strValue.value);
		
		//Long/integer
		
		Object l = 999999999999999999l;
		Value longValue = new Value(ValueType.LONG, l);
		assertEquals(ValueType.LONG, longValue.type);
		assertEquals(l, longValue.value);
		
		Object i = 999999999;
		Value intValue = new Value(ValueType.LONG, i);
		assertEquals(ValueType.LONG, intValue.type);
		assertEquals(i, intValue.value);
		
		//Double/float
		
		Object d = 999999999.9;
		Value doubleValue = new Value(ValueType.DOUBLE, d);
		assertEquals(ValueType.DOUBLE, doubleValue.type);
		assertEquals(d, doubleValue.value);
		
		Object f = 999999999.9f;
		Value floatValue = new Value(ValueType.DOUBLE, f);
		assertEquals(ValueType.DOUBLE, floatValue.type);
		assertEquals(f, floatValue.value);
		
		//Boolean
		
		Object bool = true;
		Value booleanValue = new Value(ValueType.BOOLEAN, bool);
		assertEquals(ValueType.BOOLEAN, booleanValue.type);
		assertEquals(bool, booleanValue.value);
		
		Object bool2 = false;
		Value booleanValue2 = new Value(ValueType.BOOLEAN, bool2);
		assertEquals(ValueType.BOOLEAN, booleanValue2.type);
		assertEquals(bool2, booleanValue2.value);
		
		//Null
		
		Object nullObj = null;
		Value nullValue = createValue(nullObj);
		assertEquals(ValueType.NULL, nullValue.type);
		assertNull(nullValue.value);
	}
	
	@Test
	public void testSpecificCreationHelpers()
	{
		//Object
		
		JsonObject obj = new JsonObject();
		Value objValue = createValue(obj);
		assertEquals(ValueType.OBJECT, objValue.type);
		assertEquals(obj, objValue.value);
		
		//Array
		
		JsonArray array = new JsonArray();
		Value arrayValue = new Value(ValueType.ARRAY, array);
		assertEquals(ValueType.ARRAY, arrayValue.type);
		assertEquals(array, arrayValue.value);
		
		//String
		
		String str = "ohayou";
		Value strValue = new Value(ValueType.STRING, str);
		assertEquals(ValueType.STRING, strValue.type);
		assertEquals(str, strValue.value);
		
		//Long/integer
		
		long l = 999999999999999999l;
		Value longValue = new Value(ValueType.LONG, l);
		assertEquals(ValueType.LONG, longValue.type);
		assertEquals(l, longValue.value);
		
		int i = 999999999;
		Value intValue = new Value(ValueType.LONG, i);
		assertEquals(ValueType.LONG, intValue.type);
		assertEquals(i, intValue.value);
		
		//Double/float
		
		double d = 999999999.9;
		Value doubleValue = new Value(ValueType.DOUBLE, d);
		assertEquals(ValueType.DOUBLE, doubleValue.type);
		assertEquals(d, doubleValue.value);
		
		float f = 999999999.9f;
		Value floatValue = new Value(ValueType.DOUBLE, f);
		assertEquals(ValueType.DOUBLE, floatValue.type);
		assertEquals(f, floatValue.value);
		
		//Boolean
		
		boolean bool = true;
		Value booleanValue = new Value(ValueType.BOOLEAN, bool);
		assertEquals(ValueType.BOOLEAN, booleanValue.type);
		assertEquals(bool, booleanValue.value);
		
		boolean bool2 = false;
		Value booleanValue2 = new Value(ValueType.BOOLEAN, bool2);
		assertEquals(ValueType.BOOLEAN, booleanValue2.type);
		assertEquals(bool2, booleanValue2.value);
		
		//Null
		
		Value nullValue = createNullValue();
		assertEquals(ValueType.NULL, nullValue.type);
		assertNull(nullValue.value);
	}
	
	@Test
	public void testIsTypeHelpers()
	{
		
	}
}
