package com.lplanque.gero.morphia;

import static com.lplanque.gero.morphia.JSR353Converter.TYPE_KEY;
import static javax.json.JsonValue.ValueType;
import static javax.json.JsonValue.ValueType.FALSE;
import static javax.json.JsonValue.ValueType.NULL;
import static javax.json.JsonValue.ValueType.TRUE;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.lplanque.gero.math.Bijection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * This class defines a bijection between the two following types: <br>
 * <ul>
 *    <li>{@link JsonValue},
 *    <li>{@link DBObject}.
 * </ul>
 * <br></br>
 * Because the JsonValue is a non-terminal type, we use other bijections in
 * this package to map values to a coherent {@link DBObject} representation.
 * 
 * @author <a href="https://github.com/lplanque" target="_blank">Laurent Planque</a>
 */
public final class MorphiaJsonValueBijection implements Bijection<Object, DBObject> {
	
	/**
	 * The unique instance of the current bijection...
	 */
	public static final MorphiaJsonValueBijection INSTANCE = new MorphiaJsonValueBijection();
	
	private MorphiaJsonValueBijection() {
		// Singleton...
	}
	
	// Convenient methode to create a DBO with a single key-value pair...
	private DBObject create(String key, String value) {
		final DBObject dbo = new BasicDBObject();
		dbo.put(key, value);
		return dbo;
	}
	
	/**
	 * Here, the parameter <code>x</code> must be of type {@link JsonValue}.
	 * @see Bijection#apply(Object)
	 */
	@Override public DBObject apply(Object x) {

		DBObject y = null;
		final JsonValue jv = (JsonValue)x;
		final ValueType type = jv.getValueType();
		
		// For shorter identifiers...
		final MorphiaJsonArrayBijection  arr = MorphiaJsonArrayBijection.INSTANCE;
		final MorphiaJsonNumberBijection num = MorphiaJsonNumberBijection.INSTANCE;
		final MorphiaJsonStringBijection str = MorphiaJsonStringBijection.INSTANCE;
		final MorphiaJsonObjectBijection obj = MorphiaJsonObjectBijection.INSTANCE;
		
		// Find the effective type of the current value...
		switch(type) {
		
			// Constant types
			case NULL  : y = create(TYPE_KEY, NULL.toString()) ; break;
			case TRUE  : y = create(TYPE_KEY, TRUE.toString()) ; break;
			case FALSE : y = create(TYPE_KEY, FALSE.toString()); break;
			
			// Use the appropriate bijection
			case STRING: y = str.apply((JsonString)jv); break;
			case NUMBER: y = num.apply((JsonNumber)jv); break;
			case ARRAY : y = arr.apply((JsonArray) jv); break;
			case OBJECT: y = obj.apply((JsonObject)jv); break;
			
			default:
		}
		
		return y;
	}

	/**
	 * @see Bijection#unply(Object)
	 */
	@Override public JsonValue unply(DBObject y) {
		
		JsonValue x = null;
		final ValueType type = ValueType.valueOf((String)y.get(TYPE_KEY));
		
		// For shorter identifiers...
		final MorphiaJsonArrayBijection  arr = MorphiaJsonArrayBijection.INSTANCE;
		final MorphiaJsonNumberBijection num = MorphiaJsonNumberBijection.INSTANCE;
		final MorphiaJsonStringBijection str = MorphiaJsonStringBijection.INSTANCE;
		final MorphiaJsonObjectBijection obj = MorphiaJsonObjectBijection.INSTANCE;
		
		// Find the effective type of the targeted value...
		switch(type) {
		
			// Constant types
			case NULL  : x = JsonValue.NULL;  break;
			case TRUE  : x = JsonValue.TRUE;  break;
			case FALSE : x = JsonValue.FALSE; break;
			
			// Use the appropriate bijection
			case STRING: x = str.unply(y); break;
			case NUMBER: x = num.unply(y); break;
			case ARRAY : x = arr.unply(y); break;
			case OBJECT: x = obj.unply(y); break;
			
			default:
		}
		
		return x;
	}
}
