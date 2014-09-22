package com.lplanque.gero.morphia;

import javax.json.JsonString;

import com.lplanque.gero.math.BijectionTest;
import com.lplanque.gero.morphia.MorphiaJsonStringBijection;
import com.mongodb.DBObject;

public final class MorphiaJsonStringBijectionTest extends BijectionTest<Object, DBObject> {
	
	public final static JsonString X;
	public final static DBObject   Y;
	
	static {
		X = MorphiaJsonStringBijection.asJsonString("a string");
		Y = MorphiaJsonStringBijection.INSTANCE.apply(X);
	}
	
	public MorphiaJsonStringBijectionTest() {
		super(X, Y, MorphiaJsonStringBijection.INSTANCE);
	}
}
