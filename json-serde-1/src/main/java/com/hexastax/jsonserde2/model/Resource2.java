package com.hexastax.jsonserde2.model;

import com.hexastax.jsonserde2.doc.Document2;

/**
 * Represents an abstract resource that is the base of a model consisting of a
 * set of items/entities of various types.
 */
public abstract class Resource2 extends Document2 {

	public Resource2() {
	}
	
	public Resource2(String id) {
		super(id);
	}

}
