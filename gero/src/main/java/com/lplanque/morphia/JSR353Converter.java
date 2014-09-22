package com.lplanque.morphia;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.lplanque.math.Bijection;

import com.mongodb.DBObject;

/**
 * This Morphia converter has to be added to entities that contains fields
 * with a type from the JSR-353 (Java and JSON Processing) :<br/>
 * <ul>
 *    <li>{@link JsonArray},</li>
 *    <li>{@link JsonNumber},</li>
 *    <li>{@link JsonObject},</li>
 *    <li>{@link JsonString},</li>
 *    <li>{@link JsonValue}.</li>
 * </ul>
 * <br/>
 * Each JSR-353 type is associated to a {@link Bijection} to encode it to a
 * Mongo {@link DBObject} and decode it from.
 * @author <a href="https://github.com/lplanque" target="_blank">Laurent Planque</a>
 */
public final class JSR353Converter extends MorphiaConverter {

	/**
	 * The call of the constructor registers all types from the JSR-353
	 * and their bijection.
	 */
	public JSR353Converter() {
		register(JsonArray.class,  MorphiaJsonArrayBijection.INSTANCE);
		register(JsonNumber.class, MorphiaJsonNumberBijection.INSTANCE);
		register(JsonObject.class, MorphiaJsonObjectBijection.INSTANCE);
		register(JsonString.class, MorphiaJsonStringBijection.INSTANCE);
		register(JsonValue.class,  MorphiaJsonValueBijection.INSTANCE);
	}
}
