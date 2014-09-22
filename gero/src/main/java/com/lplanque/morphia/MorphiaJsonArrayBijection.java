package com.lplanque.morphia;

import static com.lplanque.morphia.JSR353Converter.TYPE_KEY;
import static com.lplanque.morphia.JSR353Converter.VALUE_KEY;

import static javax.json.JsonValue.ValueType.ARRAY;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;

import com.lplanque.math.Bijection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * This class defines a bijection between the two following types: <br>
 * <ul>
 *    <li>{@link JsonArray},
 *    <li>{@link DBObject}.
 * </ul>
 * 
 * @author <a href="https://github.com/lplanque" target="_blank">Laurent Planque</a>
 */
public final class MorphiaJsonArrayBijection implements Bijection<Object, DBObject> {
	
	/**
	 * The unique instance of the current bijection...
	 */
	public static final MorphiaJsonArrayBijection INSTANCE = new MorphiaJsonArrayBijection();
	
	private MorphiaJsonArrayBijection() {
		// Singleton
	}
	
	/**
	 * Here, the parameter <code>x</code> must be of type {@link JsonArray}.
	 * @see Bijection#apply(Object)
	 */
	@Override public DBObject apply(Object x) {
		
		final JsonArray ja = (JsonArray)x;
		final DBObject dbo = new BasicDBObject();
		final List<DBObject> elts = new ArrayList<>();
		final MorphiaJsonValueBijection val = MorphiaJsonValueBijection.INSTANCE;
		
		dbo.put(TYPE_KEY, ARRAY.toString());
		dbo.put(VALUE_KEY, elts);
		
		for(JsonValue elt: ja) { 
			/* Because a JsonArray contains JsonValues, we have to call a bijection
			 * between JsonValues and DBObjects...
			 */
			elts.add(val.apply(elt));
		}
		
		return dbo;
	}

	/**
	 * @see Bijection#unply(Object)
	 */
	@Override public JsonArray unply(DBObject y) {
		
		final JsonArrayBuilder builder = Json.createArrayBuilder();
		final List<?> elts = (List<?>)y.get(VALUE_KEY);
		final MorphiaJsonValueBijection val = MorphiaJsonValueBijection.INSTANCE;
		
		for(Object elt: elts) {
			/* Because a JsonArray contains JsonValues, we have to call a bijection
			 * between JsonValues and DBObjects...
			 */
			builder.add(val.unply((DBObject)elt));
		}
		
		return builder.build();
	}
}
