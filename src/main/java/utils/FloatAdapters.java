package utils;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class FloatAdapters implements JsonSerializer<Double>{



	@Override
	public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
		// TODO Auto-generated method stub
		 return new JsonPrimitive(String.format("%.2f", src));  
	}


	
}
