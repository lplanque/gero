package com.lplanque.morphia;

import static com.lplanque.morphia.JSR353Converter.TYPE_KEY;
import static com.lplanque.morphia.JSR353Converter.VALUE_KEY;

import static javax.json.JsonValue.ValueType.OBJECT;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import com.lplanque.math.Bijection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * This class defines a bijection between the two following types: <br>
 * <ul>
 *    <li>{@link JsonObject},
 *    <li>{@link DBObject}.
 * </ul>
 * 
 * @author <a href="mailto:planquel@lild01.pictime.fr">planquel</a>
 */
public class MorphiaJsonObjectBijection implements Bijection<Object, DBObject> {
	
	/**
	 * The unique instance of the current bijection...
	 */
	public static final MorphiaJsonObjectBijection INSTANCE = new MorphiaJsonObjectBijection();
	
	private MorphiaJsonObjectBijection() {
		// Singleton...
	}

	/**
	 * Here, the parameter <code>x</code> must be of type {@link JsonObject}.
	 * @see Bijection#apply(Object)
	 */
	@Override public DBObject apply(Object x) {
		
		final JsonObject jo = (JsonObject)x;
		final DBObject dbo = new BasicDBObject();
		final Map<String, DBObject> entries = new HashMap<>();
		final MorphiaJsonValueBijection val = MorphiaJsonValueBijection.INSTANCE;
		
		dbo.put(TYPE_KEY, OBJECT.toString());
		dbo.put(VALUE_KEY, entries);
		
		for(Map.Entry<String, JsonValue> entry: jo.entrySet()) {
			/* Because a JsonArray contains JsonValues, we have to call a bijection
			 * between JsonValues and DBObjects...
			 */
			entries.put(entry.getKey(), val.apply(entry.getValue()));
		}
		
		return dbo;
	}

	/**
	 * @see Bijection#unply(Object)
	 */
	@Override @SuppressWarnings("unchecked")
	public JsonObject unply(DBObject y) {

		final JsonObjectBuilder builder = Json.createObjectBuilder();
		final Map<String, DBObject> entries = (Map<String, DBObject>)y.get(VALUE_KEY);
		final MorphiaJsonValueBijection val = MorphiaJsonValueBijection.INSTANCE;
		
		for(Map.Entry<String, DBObject> entry: entries.entrySet()) {
			/* Because a JsonArray contains JsonValues, we have to call a bijection
			 * between JsonValues and DBObjects...
			 */
			builder.add(entry.getKey(), val.unply(entry.getValue()));
		}
		
		return builder.build();
	}
}
