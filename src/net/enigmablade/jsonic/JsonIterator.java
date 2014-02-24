package net.enigmablade.jsonic;

import java.util.*;

/**
 * An iterator for use with JSON arrays.
 * Contains methods supporting the different return values.
 * 
 * @author Enigma
 */
public class JsonIterator implements Iterator<Object>
{
	private JsonArray array;
	private int counter;
	
	protected JsonIterator(JsonArray array)
	{
		this.array = array;
		counter = 0;
	}
	
	@Override
	public boolean hasNext()
	{
		return counter < array.size();
	}
	
	@Override
	public Object next()
	{
		return array.get(counter++);
	}
	
	public JsonObject nextObject()
	{
		return array.getObject(counter++);
	}
	
	public JsonArray nextArray()
	{
		return array.getArray(counter++);
	}
	
	public String nextString()
	{
		return array.getString(counter++);
	}
	
	public Integer nextInt()
	{
		return array.getInt(counter++);
	}
	
	public Long nextLong()
	{
		return array.getLong(counter++);
	}
	
	public Double nextDouble()
	{
		return array.getDouble(counter++);
	}
	
	public Float nextFloat()
	{
		return array.getFloat(counter++);
	}
	
	public Boolean nextBoolean()
	{
		return array.getBoolean(counter++);
	}
	
	@Override
	public void remove()
	{
		array.remove((counter = counter-1));
	}
}
