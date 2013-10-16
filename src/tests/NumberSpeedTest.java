package tests;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;
import net.enigmablade.jsonic.*;

public class NumberSpeedTest
{
	public static void main(String[] args)
	{
		final String json = getJson();
		final int numTests = 1000000;
		
		System.out.println("Num tests: "+numTests);
		
		final DecimalFormat format = new DecimalFormat("#0.00");
		
		//JSON Simple
		final FutureTask<Long> task_jsonSimple = new FutureTask<Long>(new Runnable(){public void run(){}}, null){
			@Override
			public void run()
			{
				System.out.println("Starting JSON Simple test...");
				
				long startTime = System.nanoTime();
				
				for(int n = 0; n < numTests; n++)
					test_JSONSimple(json);	/**/
				
				long endTime = System.nanoTime();
				
				long diff = endTime-startTime;
				long avg = diff/numTests;
				
				double diff_ms = diff/1000000.0;
				double avg_ms = avg/1000000.0;
				
				System.out.println("\nJSON Simple test complete");
				System.out.println("\tTotal: "+format.format(diff_ms)+" ms ("+diff+" ns)");
				System.out.println("\tAverage: "+format.format(avg_ms)+" ms ("+avg+" ns)");
				System.out.println();
				
				set(avg);
			}
		};
		
		//Quick JSON
		final FutureTask<Long> task_quickJson = new FutureTask<Long>(new Runnable(){public void run(){}}, null){
			@Override
			public void run()
			{
				System.out.println("Starting Quick JSON test...");
				
				long startTime = System.nanoTime();
				
				for(int n = 0; n < numTests; n++)
					test_quickJson(json);	/**/
				
				long endTime = System.nanoTime();
				
				long diff = endTime-startTime;
				long avg = diff/numTests;
				
				double diff_ms = diff/1000000.0;
				double avg_ms = avg/1000000.0;
				
				System.out.println("\nQuick JSON test complete");
				System.out.println("\tTotal: "+format.format(diff_ms)+" ms ("+diff+" ns)");
				System.out.println("\tAverage: "+format.format(avg_ms)+" ms ("+avg+" ns)");
				System.out.println();
				
				set(avg);
			}
		};
		
		//JSON Simple
		final FutureTask<Long> task_lolRTMPS = new FutureTask<Long>(new Runnable(){public void run(){}}, null){
			@Override
			public void run()
			{
				System.out.println("Starting LoL RTMPS test...");
				
				long startTime = System.nanoTime();
				
				for(int n = 0; n < numTests; n++)
					test_lolRTMPS(json);	/**/
				
				long endTime = System.nanoTime();
				
				long diff = endTime-startTime;
				long avg = diff/numTests;
				
				double diff_ms = diff/1000000.0;
				double avg_ms = avg/1000000.0;
				
				System.out.println("\nLoL RTMPS test complete");
				System.out.println("\tTotal: "+format.format(diff_ms)+" ms ("+diff+" ns)");
				System.out.println("\tAverage: "+format.format(avg_ms)+" ms ("+avg+" ns)");
				
				set(avg);
			}
		};
		
		//JSON Simple
		final FutureTask<Long> task_jsonSmart = new FutureTask<Long>(new Runnable(){public void run(){}}, null){
			@Override
			public void run()
			{
				System.out.println("Starting JSON Smart test...");
				
				long startTime = System.nanoTime();
				
				for(int n = 0; n < numTests; n++)
					test_jsonSmart(json);	/**/
				
				long endTime = System.nanoTime();
				
				long diff = endTime-startTime;
				long avg = diff/numTests;
				
				double diff_ms = diff/1000000.0;
				double avg_ms = avg/1000000.0;
				
				System.out.println("\nJSON Smart test complete");
				System.out.println("\tTotal: "+format.format(diff_ms)+" ms ("+diff+" ns)");
				System.out.println("\tAverage: "+format.format(avg_ms)+" ms ("+avg+" ns)");
				
				set(avg);
			}
		};
		
		//JSONic
		final FutureTask<Long> task_jsonic = new FutureTask<Long>(new Runnable(){public void run(){}}, null){
			@Override
			public void run()
			{
				System.out.println("Starting JSONic test...");
				
				long startTime = System.nanoTime();
				
				for(int n = 0; n < numTests; n++)
					test_JSONic(json);	/**/
				
				long endTime = System.nanoTime();
				
				long diff = endTime-startTime;
				long avg = diff/numTests;
				
				double diff_ms = diff/1000000.0;
				double avg_ms = avg/1000000.0;
				
				System.out.println("\nJSONic test complete");
				System.out.println("\tTotal: "+format.format(diff_ms)+" ms ("+diff+" ns)");
				System.out.println("\tAverage: "+format.format(avg_ms)+" ms ("+avg+" ns)");
				System.out.println();
				
				set(avg);
			}
		};
		
		//JSONic delayed
		final FutureTask<Long> task_jsonicDelayed = new FutureTask<Long>(new Runnable(){public void run(){}}, null){
			@Override
			public void run()
			{
				System.out.println("Starting JSONic delayed test...");
				
				long startTime = 0;
				
				startTime = System.nanoTime();
				
				for(int n = 0; n < numTests; n++)
					test_JSONicDelayed(json);
				
				long endTime = System.nanoTime();
				
				long diff = endTime-startTime;
				long avg = diff/numTests;
				
				double diff_ms = diff/1000000.0;
				double avg_ms = avg/1000000.0;
				
				System.out.println("\nJSONic delayed test complete");
				System.out.println("\tTotal: "+format.format(diff_ms)+" ms ("+diff+" ns)");
				System.out.println("\tAverage: "+format.format(avg_ms)+" ms ("+avg+" ns)");
				System.out.println();
				
				set(avg);
			}
		};
		
		//Randomize test start order (to be fair)
		List<FutureTask<?>> tasks = new ArrayList<>(4);
		Collections.addAll(tasks, new FutureTask<?>[]{task_jsonSimple, task_lolRTMPS, task_quickJson, task_jsonSmart, task_jsonic, task_jsonicDelayed});
		Collections.shuffle(tasks);
		
		//Start computations
		for(FutureTask<?> task : tasks)
			new Thread(task).start();
		
		//Get the results
		try
		{
			double resultJsonicDelayed = task_jsonicDelayed.get();
			double resultJsonic = task_jsonic.get();
			double resultQuickJson = task_quickJson.get();
			double resultJsonSimple = task_jsonSimple.get();
			double resultLoLRTMPS = task_lolRTMPS.get();
			double resultJsonSmart = task_jsonSmart.get();
			
			System.out.println("\n------------------------------");
			
			System.out.println("\nCompared to JSON Simple:");
			double percentFaster = ((resultJsonSimple-resultJsonic)/resultJsonSimple)*100;
			System.out.println("JSONic in normal mode is "+format.format(percentFaster)+"% faster");
			double percentFasterDelayed = ((resultJsonSimple-resultJsonicDelayed)/resultJsonSimple)*100;
			System.out.println("JSONic in delayed mode is "+format.format(percentFasterDelayed)+"% faster");
			
			System.out.println("\nCompared to Quick JSON:");
			double percentFaster3 = ((resultQuickJson-resultJsonic)/resultQuickJson)*100;
			System.out.println("JSONic in normal mode is "+format.format(percentFaster3)+"% faster");
			double percentFasterDelayed3 = ((resultQuickJson-resultJsonicDelayed)/resultQuickJson)*100;
			System.out.println("JSONic in delayed mode is "+format.format(percentFasterDelayed3)+"% faster");
			
			System.out.println("\nCompared to LoL RTMPS:");
			double percentFaster2 = ((resultLoLRTMPS-resultJsonic)/resultLoLRTMPS)*100;
			System.out.println("JSONic in normal mode is "+format.format(percentFaster2)+"% faster");
			double percentFasterDelayed2 = ((resultLoLRTMPS-resultJsonicDelayed)/resultLoLRTMPS)*100;
			System.out.println("JSONic in delayed mode is "+format.format(percentFasterDelayed2)+"% faster");
			
			System.out.println("\nCompared to JSON Smart:");
			double percentFaster4 = ((resultJsonSmart-resultJsonic)/resultJsonSmart)*100;
			System.out.println("JSONic in normal mode is "+format.format(percentFaster4)+"% faster");
			double percentFasterDelayed4 = ((resultJsonSmart-resultJsonicDelayed)/resultJsonSmart)*100;
			System.out.println("JSONic in delayed mode is "+format.format(percentFasterDelayed4)+"% faster");
		}
		catch(Exception e)
		{
			System.err.println("Failed to get results");
			e.printStackTrace();
		}
	}
	
	private static void test_JSONSimple(String json)
	{
		org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
		try
		{
			Object o = parser.parse(json);
			org.json.simple.JSONObject obj = (org.json.simple.JSONObject)o;
			org.json.simple.JSONObject data = (org.json.simple.JSONObject)obj.get("data");
			
			org.json.simple.JSONArray longs = (org.json.simple.JSONArray)data.get("longs");
			longs.size();
			
			org.json.simple.JSONArray doubles = (org.json.simple.JSONArray)data.get("doubles");
			doubles.size();
		}
		catch(org.json.simple.parser.ParseException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void test_lolRTMPS(String json)
	{
		try
		{
			Object o = com.gvaneyck.rtmp.encoding.JSON.parse(json);
			com.gvaneyck.rtmp.encoding.ObjectMap obj = (com.gvaneyck.rtmp.encoding.ObjectMap)o;
			com.gvaneyck.rtmp.encoding.ObjectMap data = obj.getMap("data");
			
			Object[] longs = data.getArray("longs");
			String.valueOf(longs.length);
			
			Object[] doubles = data.getArray("doubles");
			String.valueOf(doubles.length);
		}
		catch(com.gvaneyck.rtmp.encoding.JSONParsingException e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void test_quickJson(String json)
	{
		com.json.parsers.JsonParserFactory factory = com.json.parsers.JsonParserFactory.getInstance();
		com.json.parsers.JSONParser parser = factory.newJsonParser();
		Map obj = parser.parseJson(json);
		Map data = (Map)obj.get("data");
		
		List longs = (List)data.get("longs");
		longs.size();
		
		List doubles = (List)data.get("doubles");
		doubles.size();
	}
	
	private static void test_jsonSmart(String json)
	{
		@SuppressWarnings("deprecation")
		net.minidev.json.parser.JSONParser parser = new net.minidev.json.parser.JSONParser();
		try
		{
			Object o = parser.parse(json);
			net.minidev.json.JSONObject obj = (net.minidev.json.JSONObject)o;
			net.minidev.json.JSONObject data = (net.minidev.json.JSONObject)obj.get("data");
			
			net.minidev.json.JSONArray longs = (net.minidev.json.JSONArray)data.get("longs");
			longs.size();
			
			net.minidev.json.JSONArray doubles = (net.minidev.json.JSONArray)data.get("doubles");
			doubles.size();
		}
		catch(net.minidev.json.parser.ParseException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void test_JSONic(String json)
	{
		try
		{
			JsonObject obj = JsonParser.parseObject(json, false);
			JsonObject data = obj.getObject("data");
			
			JsonArray longs = data.getArray("longs");
			longs.size();
			
			JsonArray doubles = data.getArray("doubles");
			doubles.size();
		}
		catch(JsonException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void test_JSONicDelayed(String json)
	{
		try
		{
			JsonObject obj = JsonParser.parseObject(json, true);
			JsonObject data = obj.getObject("data");
			
			JsonArray longs = data.getArray("longs");
			longs.size();
			
			JsonArray doubles = data.getArray("doubles");
			doubles.size();
		}
		catch(JsonException e)
		{
			e.printStackTrace();
		}
	}
	
	//Helper methods
	
	private static String getJson()
	{
		try(Scanner scanner = new Scanner(new File("test_libs/numbers.json")))
		{
			StringBuilder s = new StringBuilder();
			while(scanner.hasNextLine())
				s.append(scanner.nextLine()).append('\n');
			return s.toString();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
