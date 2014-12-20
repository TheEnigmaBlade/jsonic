package tests;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.util.function.*;
import net.enigmablade.jsonic.*;

public class ParserUtilTest extends ParserUtil
{
	private static final int NUM_VALUES = 1_000_000;
	private static final int NUM_ITERATIONS = 1000;
	
	private Map<String, Long> results;
	
	@Test
	public void testUnknownParse()
	{
		//Test validity
		assertEquals(new Long(0), parseUnknown("0").value);
		assertEquals(new Long(1), parseUnknown("1").value);
		assertEquals(new Long(12), parseUnknown("12").value);
		assertEquals(new Long(123), parseUnknown("123").value);
		assertEquals(new Long(1234), parseUnknown("1234").value);
		assertEquals(new Long(-1), parseUnknown("-1").value);
		assertEquals(new Long(-21), parseUnknown("-21").value);
		assertEquals(new Long(-321), parseUnknown("-321").value);
		assertEquals(new Long(-4321), parseUnknown("-4321").value);
		assertEquals(new Long((long)2e3), parseUnknown("2e3").value);
		assertEquals(new Long((long)2e12), parseUnknown("2e12").value);
		
		assertEquals(new Double(0.0), parseUnknown("0.0").value);
		assertEquals(new Double(1.1), parseUnknown("1.1").value);
		assertEquals(new Double(12.21), parseUnknown("12.21").value);
		assertEquals(new Double(123.321), parseUnknown("123.321").value);
		assertEquals(new Double(1234.4321), parseUnknown("1234.4321").value);
		assertEquals(new Double(-1.9), parseUnknown("-1.9").value);
		assertEquals(new Double(-21.89), parseUnknown("-21.89").value);
		assertEquals(new Double(-321.789), parseUnknown("-321.789").value);
		assertEquals(new Double(-4321.6789), parseUnknown("-4321.6789").value);
		assertEquals(new Double(0.2e3), parseUnknown("0.2e3").value);
		assertEquals(new Double(2.1e11), parseUnknown("2.1e11").value);
		assertEquals(new Double(2e-1), parseUnknown("2e-1").value);
		assertEquals(new Double(2e-11), parseUnknown("2e-11").value);
	}
	
	@Test
	public void testUnknownParseSpeed()
	{
		List<Supplier<Void>> methods = new ArrayList<>();
		methods.add(this::testTrueParse);
		methods.add(this::testFalseParse);
		methods.add(this::testNullParse);
		methods.add(this::testLongParse);
		methods.add(this::testDoubleParse);
		
		results = Collections.synchronizedMap(new HashMap<String, Long>(methods.size(), 1.1f));
		for(int i = 0; i < NUM_ITERATIONS; i++)
		{
			Collections.shuffle(methods);
			methods.parallelStream().forEach(m -> m.get());
		}
		
		System.out.println("Results\n-----------------");
		System.out.println("true  | "+getResult("true")+" ms");
		System.out.println("false | "+getResult("false")+" ms");
		System.out.println("null  | "+getResult("null")+" ms");
		System.out.println("long  | "+getResult("long")+" ms");
		System.out.println("float | "+getResult("double")+" ms");
	}
	
	public Void testTrueParse()
	{
		long startTime = System.currentTimeMillis();
		for(long n = 0; n < NUM_VALUES; n++)
			parseUnknown("true");
		addResult("true", System.currentTimeMillis() - startTime);
		return null;
	}
	
	public Void testFalseParse()
	{
		long startTime = System.currentTimeMillis();
		for(long n = 0; n < NUM_VALUES; n++)
			parseUnknown("false");
		addResult("false", System.currentTimeMillis() - startTime);
		return null;
	}
	
	public Void testNullParse()
	{
		long startTime = System.currentTimeMillis();
		for(long n = 0; n < NUM_VALUES; n++)
			parseUnknown("null");
		addResult("null", System.currentTimeMillis() - startTime);
		return null;
	}
	
	public Void testLongParse()
	{
		//Test speed
		Random rand = new Random();
		String[] vals = new String[NUM_VALUES];
		for(int n = 0; n < NUM_VALUES; n++)
			vals[n] = String.valueOf(rand.nextLong());
		
		long startTime = System.currentTimeMillis();
		for(int n = 0; n < NUM_VALUES; n++)
			parseUnknown(vals[n]);
		addResult("long", System.currentTimeMillis() - startTime);
		
		return null;
	}
	
	public Void testDoubleParse()
	{
		//Test speed
		Random rand = new Random();
		String[] vals = new String[NUM_VALUES];
		for(int n = 0; n < NUM_VALUES; n++)
			vals[n] = String.valueOf(rand.nextDouble());
		
		long startTime = System.currentTimeMillis();
		for(int n = 0; n < NUM_VALUES; n++)
			parseUnknown(vals[n]);
		addResult("double", System.currentTimeMillis() - startTime);
		
		return null;
	}
	
	//Util
	
	private void addResult(String key, long val)
	{
		synchronized(results)
		{
			if(!results.containsKey(key))
				results.put(key, val);
			else
				results.put(key, results.get(key) + val);
		}
	}
	
	private double getResult(String key)
	{
		return 1.0*results.get(key)/NUM_ITERATIONS;
	}
}
