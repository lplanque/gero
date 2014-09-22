package com.lplanque.gero.model;

import javax.json.JsonValue;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Converters;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.google.common.base.Objects;
import com.lplanque.gero.morphia.JSR353Converter;

@Entity @Converters({ JSR353Converter.class })
public final class Bean {

	@Id private ObjectId id;
	private JsonValue value;
	
	public Bean() {
		// Empty constructor
	}
	
	public Bean(final JsonValue value) {
		this.value = value;
	}

	public JsonValue getValue() {
		return value;
	}

	public void setValue(final JsonValue value) {
		this.value = value;
	}

	public ObjectId getId() {
		return id;
	}
	
	@Override public boolean equals(Object o) {
		
		if(o == null || !getClass().equals(o.getClass())) {
			return false;
		}
        
        final Bean other = (Bean)o;
        
        return Objects.equal(this.id, other.id)
        	&& Objects.equal(this.value, other.value);
	}
	
	@Override public int hashCode() {
		return Objects.hashCode(id, value);
	}
	
	@Override public String toString() {
		return Objects.toStringHelper(this)
			.add("id", id)
			.add("value", value)
			.toString();
	}
}
