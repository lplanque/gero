package com.lplanque.gero.morphia;

import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonArray;

import com.lplanque.gero.math.BijectionTest;
import com.lplanque.gero.morphia.MorphiaJsonArrayBijection;
import com.lplanque.gero.morphia.MorphiaJsonStringBijection;
import com.mongodb.DBObject;

public class MorphiaJsonArrayBijectionTest extends BijectionTest<Object, DBObject> {

	public static final JsonArray X;
	public static final DBObject  Y;
	
	static {
		
		// Very random creation... :p
		
		final JsonArray sub = Json.createArrayBuilder()
			.add("1").add(2).add(3.0)
			.build();
		
		X = Json.createArrayBuilder()
			.add(new BigDecimal("3.1416"))
			.add(MorphiaJsonStringBijection.asJsonString("a string"))
			.add(Json.createObjectBuilder()
				.add("key", "value")
				.add("sub", sub)
				.build()
			).build();
		
		Y = MorphiaJsonArrayBijection.INSTANCE.apply(X);
	}
	
	public MorphiaJsonArrayBijectionTest() {
		super(X, Y, MorphiaJsonArrayBijection.INSTANCE);
	}
}
