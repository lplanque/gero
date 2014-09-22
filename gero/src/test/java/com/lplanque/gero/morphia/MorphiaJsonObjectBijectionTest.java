package com.lplanque.gero.morphia;

import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import com.lplanque.gero.math.BijectionTest;
import com.lplanque.gero.morphia.MorphiaJsonNumberBijection;
import com.lplanque.gero.morphia.MorphiaJsonObjectBijection;
import com.lplanque.gero.morphia.MorphiaJsonStringBijection;
import com.mongodb.DBObject;

public class MorphiaJsonObjectBijectionTest extends BijectionTest<Object, DBObject> {

	public static final JsonObject X;
	public static final DBObject   Y;
	
	static {
		
		// Very random creation... :p
		
		final JsonObject obj = Json.createObjectBuilder()
			.add("str", MorphiaJsonStringBijection.asJsonString("a string"))
			.add("num", MorphiaJsonNumberBijection.asJsonNumber(new BigDecimal("3.1416")))
			.build();
			
		final JsonArray array = Json.createArrayBuilder()
			.add(new BigDecimal("3.1416"))
			.add(MorphiaJsonStringBijection.asJsonString("a string"))
			.add(obj)
			.build();
			
		X = Json.createObjectBuilder()
			.add("str", MorphiaJsonStringBijection.asJsonString("a string"))
			.add("num", MorphiaJsonNumberBijection.asJsonNumber(new BigDecimal("3.1416")))
			.add("arr", array)
			.build();
		
		Y =  MorphiaJsonObjectBijection.INSTANCE.apply(X);
	}
	
	public MorphiaJsonObjectBijectionTest() {
		super(X, Y, MorphiaJsonObjectBijection.INSTANCE);
	}
}
