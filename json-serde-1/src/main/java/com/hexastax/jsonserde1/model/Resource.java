package com.hexastax.jsonserde1.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.hexastax.jsonserde1.doc.Document;

/**
 * Represents an abstract resource that is the base of a model consisting of a
 * set of items/entities of various types.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.WRAPPER_OBJECT)
@JsonSubTypes({ @Type(value = Message.class) })
public abstract class Resource extends Document {

	public Resource(String id) {
		super(id);
	}

}
