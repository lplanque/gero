package com.lplanque.gero.morphia;

import java.math.BigDecimal;

import javax.json.JsonNumber;

import com.lplanque.gero.math.BijectionTest;
import com.lplanque.gero.morphia.MorphiaJsonNumberBijection;
import com.mongodb.DBObject;

public final class MorphiaJsonNumberBijectionTest extends BijectionTest<Object, DBObject> {

	public static final JsonNumber X;
	public static final DBObject   Y;
	
	static {
		X = MorphiaJsonNumberBijection.asJsonNumber(new BigDecimal("3.1416"));
		Y = MorphiaJsonNumberBijection.INSTANCE.apply(X);
	}
	
	public MorphiaJsonNumberBijectionTest() {
		super(X, Y, MorphiaJsonNumberBijection.INSTANCE);
	}
}
