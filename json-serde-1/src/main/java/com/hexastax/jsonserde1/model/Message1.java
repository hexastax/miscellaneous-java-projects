package com.hexastax.jsonserde1.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
 * Represents a message in the Model.
 */
// TODO: Message: presumably, either JsonSerialize/JsonDeserialize on Document or @JsonTypeInfo (how?)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.WRAPPER_OBJECT)
public class Message1 extends Resource1 {

	public Message1(String id) {
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
