package com.hexastax.jsonserde1.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.hexastax.jsonserde1.doc.Document1;

/**
 * Represents an abstract resource that is the base of a model consisting of a
 * set of items/entities of various types.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.WRAPPER_OBJECT)
@JsonSubTypes({ @Type(value = Message1.class) })
public abstract class Resource1 extends Document1 {

	public Resource1(String id) {
		super(id);
	}

}
