package com.lplanque.morphia;

import static com.lplanque.morphia.JSR353Converter.TYPE_KEY;
import static com.lplanque.morphia.JSR353Converter.VALUE_KEY;

import static javax.json.JsonValue.ValueType.NUMBER;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.json.JsonNumber;

import com.lplanque.math.Bijection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * This class defines a bijection between the two following types: <br>
 * <ul>
 *    <li>{@link JsonNumber},
 *    <li>{@link DBObject}.
 * </ul>
 * 
 * @author <a href="https://github.com/lplanque" target="_blank">Laurent Planque</a>
 */
public final class MorphiaJsonNumberBijection implements Bijection<Object, DBObject> {
	
	/**
	 * The unique instance of the current bijection...
	 */
	public static final MorphiaJsonNumberBijection INSTANCE = new MorphiaJsonNumberBijection();
	
	private MorphiaJsonNumberBijection() {
		// Singleton
	}
	
	/**
	 * Creates an instance of {@link JsonNumber} from a given {@link BigDecimal}.
	 * If <code>x</code> is <code>null</code> then it returns <code>null</code>.
	 * @param x The <I>big decimal</code> to map as JsonNumber...
	 * @return A {@link JsonNumber} implementation according to the JSR-353. 
	 */
	public static JsonNumber asJsonNumber(final BigDecimal x) {
		return x == null? null : new JsonNumber() {
			
			@Override public ValueType getValueType()          { return NUMBER; }
			@Override public long longValueExact()             { return x.longValueExact(); }
			@Override public long longValue()                  { return x.longValue(); }
			@Override public boolean isIntegral()              { return x.scale() == 0; } 
			@Override public int intValueExact()               { return x.intValueExact(); }
			@Override public int intValue()                    { return x.intValue(); }
			@Override public double doubleValue()              { return x.doubleValue(); }
			@Override public BigInteger bigIntegerValueExact() { return x.toBigIntegerExact(); }
			@Override public BigInteger bigIntegerValue()      { return x.toBigInteger(); }
			@Override public BigDecimal bigDecimalValue()      { return x; }
			@Override public String toString()                 { return x.toString(); }
			@Override public int hashCode()                    { return x.hashCode(); }
			
			@Override public boolean equals(Object o) {
				
				if(!(o instanceof JsonNumber)) {
					return false;
				}
				
				final JsonNumber j = (JsonNumber)o;
				final BigDecimal y = j.bigDecimalValue();
				
				return x.equals(y);
			}
		};
	}
	
	/**
	 * Here, the parameter <code>x</code> must be of type {@link JsonNumber}.
	 * @see Bijection#apply(Object)
	 */
	@Override public DBObject apply(Object x) {
		JsonNumber jn = (JsonNumber)x;
		final DBObject dbo = new BasicDBObject();
		dbo.put(TYPE_KEY, NUMBER.toString());
		dbo.put(VALUE_KEY, jn.bigDecimalValue().toString()); //TODO To check !
		return dbo;
	}

	/**
	 * @see Bijection#unply(Object)
	 */
	@Override public JsonNumber unply(DBObject y) {
		return asJsonNumber(new BigDecimal((String)y.get(VALUE_KEY)));
	}
}
