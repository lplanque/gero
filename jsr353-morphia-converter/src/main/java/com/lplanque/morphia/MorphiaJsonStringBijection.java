package com.lplanque.morphia;

import static com.lplanque.morphia.JSR353Converter.TYPE_KEY;
import static com.lplanque.morphia.JSR353Converter.VALUE_KEY;

import static javax.json.JsonValue.ValueType.STRING;

import javax.json.JsonString;

import com.lplanque.math.Bijection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * This class defines a bijection between the two following types: <br>
 * <ul>
 *    <li>{@link JsonString},
 *    <li>{@link DBObject}.
 * </ul>
 * 
 * @author <a href="https://github.com/lplanque" target="_blank">Laurent Planque</a>
 */
public final class MorphiaJsonStringBijection implements Bijection<Object, DBObject>{

	public static final MorphiaJsonStringBijection INSTANCE = new MorphiaJsonStringBijection();
	
	/**
	 * The unique instance of the current bijection...
	 */
	private MorphiaJsonStringBijection() {
		// Singleton
	}
	
	/**
	 * Creates an instance of {@link JsonString} from a given {@link String}.
	 * If <code>s</code> is <code>null</code> then it returns <code>null</code>.
	 * @param s The <I>string</I></code> to map as JsonString...
	 * @return A {@link JsonString} implementation according to the JSR-353. 
	 */
	public static JsonString asJsonString(final String s) {
		
		return s == null? null: new JsonString() {
			
			@Override public ValueType getValueType() { return STRING;                     }
			@Override public String getString()       { return s;                          }
			@Override public CharSequence getChars()  { return s;                          }
			// The toString() method is the Json representation of the string, so we add '"' at the end and the begining
			@Override public String toString()        { return String.format("\"%s\"", s); }
			@Override public int hashCode()           { return s.hashCode();               }
			
			@Override public boolean equals(Object o) {
				
				if(!(o instanceof JsonString)) {
					return false;
				}
				
				final JsonString j = (JsonString)o;
				final String other = j.getString();
				
				return s.equals(other);
			}
		};
	}
	
	/**
	 * Here, the parameter <code>x</code> must be of type {@link JsonString}.
	 * @see Bijection#apply(Object)
	 */
	@Override public DBObject apply(Object x) {
		final DBObject dbo = new BasicDBObject();	
		dbo.put(TYPE_KEY, STRING.toString());
		dbo.put(VALUE_KEY, ((JsonString)x).getString());
		return dbo;
	}

	/**
	 * @see Bijection#unply(Object)
	 */
	@Override public JsonString unply(DBObject y) {
		return asJsonString((String)y.get(VALUE_KEY));
	}
}
