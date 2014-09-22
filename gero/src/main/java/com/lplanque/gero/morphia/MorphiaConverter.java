package com.lplanque.gero.morphia;

import java.util.HashMap;
import java.util.Map;

import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lplanque.gero.math.Bijection;
import com.mongodb.DBObject;

/**
 * A generic class for Mongo converters. The idea was to map to a Java type <code>T</code>
 * a {@link Bijection} that converts <code>T</code> to a a Mongo-compliant object (<I>e.g.</I> an instance
 * of {@link DBObject} in that context). When calling {@link #decode(Class, Object, MappedField)} (resp.
 * {@link #encode(Object, MappedField)}) method, we find the <I>nearest</I> registered type having
 * a bijection and call it to decode (resp. encode) the given object.
 *  
 * @author <a href="https://github.com/lplanque" target="_blank">Laurent Planque</a>
 */
public abstract class MorphiaConverter extends TypeConverter implements SimpleValueConverter {
	
	// Convention for keys into a DBObject...
	
	/**
	 * A convenient key that denotes a type into the DBObject.
	 */
	protected static final String TYPE_KEY  = "type" ;
	
	/**
	 * A convenient key that denotes a value into the DBObject.
	 */
	protected static final String VALUE_KEY = "value";
	
	/**
	 * Our bijections...
	 */
	protected Map<Class<?>, Bijection<Object, DBObject>> functors;
	
	/**
	 * An SLF4J logger.
	 */
	protected Logger logger;
	
	/**
	 * Default constructor. Creates the logger and a new {@link HashMap} to define the mapping between
	 * regsitered types and bijections.
	 */
	protected MorphiaConverter() {
		functors = new HashMap<>();
		logger = LoggerFactory.getLogger(MorphiaConverter.class);
	}

	/**
	 * Creates an association between type and bijection.
	 * @param key The type to register.
	 * @param value The bijection associated to the type.
	 */
	protected void register(Class<?> key, Bijection<Object, DBObject> value) {
		// Maps type to a bijection
		functors.put(key, value);
		// Update the supported types from the superclass
		setSupportTypes(functors.keySet().toArray(new Class<?>[0]));
	}
	
	//Convenient method to trace information at the begining of a method.
	private void info(final String method, final Object value) {
		logger.info("{}#{}({})", getClass(), method, value);
	}

	// Find the better class TODO optimize !
	private Class<?> nearest(Class<?> clazz) {
		Class<?> as = null;
		for(Class<?> c: getSupportTypes()) {
			if(c.equals(clazz)) return c;
			if(c.isAssignableFrom(clazz)) as = c;
		}
		return as;
	}
	
	/**
	 * Decode method...
	 * @see TypeConverter#decode(Class, Object, MappedField)
	 */
	@Override @SuppressWarnings("rawtypes")
	public Object decode(Class clazz, Object from, MappedField _) {
		info("decode", from);
		final DBObject dbo = (DBObject)from;
		final Bijection<Object, DBObject> bij = functors.get(clazz);
		return dbo != null && bij != null? bij.unply(dbo): null;
	}
	
	/**
	 * Encode method...
	 * @see TypeConverter#encode(Object, MappedField)
	 */
	@Override public DBObject encode(Object value, MappedField _) {
		info("encode", value);
		if(value != null) {
			final Class<?> eff = nearest(value.getClass());
			final Bijection<Object, DBObject> bij = eff != null? functors.get(eff) : null;
			return bij != null? bij.apply(value): null;
		} else {
			return null;
		}
	}
}
