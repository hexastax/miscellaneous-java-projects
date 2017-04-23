package com.hexastax.jsonserde2.model;

/**
 * Represents a message in the Model.
 */
public class Message2 extends Resource2 {

	public Message2() {
	}
	
	public Message2(String id) {
		super(id);
	}

	public String getTitle() {
		return getFirstValueAsString("title");
	}

	public void setTitle(String title) {
		setField("title", title);
	}

	public String getBody() {
		return getFirstValueAsString("body");
	}

	public void setBody(String body) {
		setField("body", body);
	}
}
