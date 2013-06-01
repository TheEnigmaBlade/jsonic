package test;

import static org.junit.Assert.*;

import org.junit.Test;

import net.enigmablade.jsonic.*;

public class ValueTest
{
	@Test
	public void testParse() throws JsonException
	{
		JsonObject obj;
		JsonArray ary;
		
		//Longs
		
		obj = JsonParser.parseObject("{\"key\":123}");
		assertEquals(123, (long)obj.getLong("key"));
		obj = JsonParser.parseObject("{\"key\":123,\"key2\":456}");
		assertEquals(123, (long)obj.getLong("key"));
		assertEquals(456, (long)obj.getLong("key2"));
		obj = JsonParser.parseObject("{\"key\":123,\"key2\":456,\"key3\":789,\"key4\":0}");
		assertEquals(123, (long)obj.getLong("key"));
		assertEquals(456, (long)obj.getLong("key2"));
		assertEquals(789, (long)obj.getLong("key3"));
		assertEquals(0, (long)obj.getLong("key4"));
		
		ary = JsonParser.parseArray("[123]");
		assertEquals(123, (long)ary.getLong(0));
		ary = JsonParser.parseArray("[123, 456]");
		assertEquals(123, (long)ary.getLong(0));
		assertEquals(456, (long)ary.getLong(1));
		ary = JsonParser.parseArray("[123, 456, 789, 0]");
		assertEquals(123, (long)ary.getLong(0));
		assertEquals(456, (long)ary.getLong(1));
		assertEquals(789, (long)ary.getLong(2));
		assertEquals(0, (long)ary.getLong(3));
		
		obj = JsonParser.parseObject("{\"key\":-42}");
		assertEquals(-42, (long)obj.getLong("key"));
		ary = JsonParser.parseArray("[-57]");
		assertEquals(-57, (long)ary.getLong(0));
		
		//Doubles
		
		obj = JsonParser.parseObject("{\"key\":12.3}");
		assertEquals(12.3, (double)obj.getDouble("key"), 0.01);
		obj = JsonParser.parseObject("{\"key\":12.3,\"key2\":4.56}");
		assertEquals(12.3, (double)obj.getDouble("key"), 0.01);
		assertEquals(4.56, (double)obj.getDouble("key2"), 0.01);
		obj = JsonParser.parseObject("{\"key\":12.3,\"key2\":4.56,\"key3\":789.0,\"key4\":0.0}");
		assertEquals(12.3, (double)obj.getDouble("key"), 0.01);
		assertEquals(4.56, (double)obj.getDouble("key2"), 0.01);
		assertEquals(789, (double)obj.getDouble("key3"), 0.01);
		assertEquals(0.0, (double)obj.getDouble("key4"), 0.01);
		
		ary = JsonParser.parseArray("[12.3]");
		assertEquals(12.3, (double)ary.getDouble(0), 0.01);
		ary = JsonParser.parseArray("[12.3, 4.56]");
		assertEquals(12.3, (double)ary.getDouble(0), 0.01);
		assertEquals(4.56, (double)ary.getDouble(1), 0.01);
		ary = JsonParser.parseArray("[12.3, 4.56, 789.0, 0.0]");
		assertEquals(12.3, (double)ary.getDouble(0), 0.01);
		assertEquals(4.56, (double)ary.getDouble(1), 0.01);
		assertEquals(789, (double)ary.getDouble(2), 0.01);
		assertEquals(0.0, (double)ary.getDouble(3), 0.01);
		
		obj = JsonParser.parseObject("{\"key\":-4.2}");
		assertEquals(-4.2, (double)obj.getDouble("key"), 0.01);
		ary = JsonParser.parseArray("[-57.1]");
		assertEquals(-57.1, (double)ary.getDouble(0), 0.01);
		
		obj = JsonParser.parseObject("{\"key\":.1248}");
		assertEquals(0.1248, (double)obj.getDouble("key"), 0.0001);
		ary = JsonParser.parseArray("[.1248]");
		assertEquals(0.1248, (double)ary.getDouble(0), 0.0001);
		
		//Booleans
		
		obj = JsonParser.parseObject("{\"key\":true}");
		assertTrue(obj.getBoolean("key"));
		obj = JsonParser.parseObject("{\"key\":false}");
		assertFalse(obj.getBoolean("key"));
		obj = JsonParser.parseObject("{\"key\":true,\"key2\":false}");
		assertTrue(obj.getBoolean("key"));
		assertFalse(obj.getBoolean("key2"));
		obj = JsonParser.parseObject("{\"key\":false,\"key2\":true}");
		assertFalse(obj.getBoolean("key"));
		assertTrue(obj.getBoolean("key2"));
		
		ary = JsonParser.parseArray("[true]");
		assertTrue(ary.getBoolean(0));
		ary = JsonParser.parseArray("[false]");
		assertFalse(ary.getBoolean(0));
		ary = JsonParser.parseArray("[true, false]");
		assertTrue(ary.getBoolean(0));
		assertFalse(ary.getBoolean(1));
		ary = JsonParser.parseArray("[false, true]");
		assertFalse(ary.getBoolean(0));
		assertTrue(ary.getBoolean(1));
		
		//Nulls
		
		obj = JsonParser.parseObject("{\"key\":null}");
		assertNull(obj.get("key"));
		assertNull(obj.getObject("key"));
		assertNull(obj.getArray("key"));
		assertNull(obj.getLong("key"));
		assertNull(obj.getDouble("key"));
		assertNull(obj.getBoolean("key"));
		obj = JsonParser.parseObject("{\"key\":null,\"key2\":null}");
		assertNull(obj.get("key"));
		assertNull(obj.getObject("key"));
		assertNull(obj.getArray("key"));
		assertNull(obj.getLong("key"));
		assertNull(obj.getDouble("key"));
		assertNull(obj.getBoolean("key"));
		assertNull(obj.get("key2"));
		assertNull(obj.getObject("key2"));
		assertNull(obj.getArray("key2"));
		assertNull(obj.getLong("key2"));
		assertNull(obj.getDouble("key2"));
		assertNull(obj.getBoolean("key2"));
		
		ary = JsonParser.parseArray("[null]");
		assertNull(ary.get(0));
		assertNull(ary.getObject(0));
		assertNull(ary.getArray(0));
		assertNull(ary.getLong(0));
		assertNull(ary.getDouble(0));
		assertNull(ary.getBoolean(0));
		ary = JsonParser.parseArray("[null, null]");
		assertNull(ary.get(0));
		assertNull(ary.getObject(0));
		assertNull(ary.getArray(0));
		assertNull(ary.getLong(0));
		assertNull(ary.getDouble(0));
		assertNull(ary.getBoolean(0));
		assertNull(ary.get(1));
		assertNull(ary.getObject(1));
		assertNull(ary.getArray(1));
		assertNull(ary.getLong(1));
		assertNull(ary.getDouble(1));
		assertNull(ary.getBoolean(1));
	}
}
