package com.hexastax.jsonserde2.doc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Represents a basic "property bag" type of object: a mapping of keys to lists
 * of values.
 * <p>
 * These mappings are (at least at the moment) not strongly typed; an assumption
 * is made that the user would not stuff objects of varying types into the same
 * value list for a given key.
 */

// TODO: Document: presumably, either JsonSerialize/JsonDeserialize or @JsonTypeInfo (how?)
@JsonSerialize(using = JsonDocumentSerializer2.class)
@JsonDeserialize(using = JsonDocumentDeserializer2.class)
//
// NOTE: we cannot have any JsonSubType annotations here!
// Document is generic and needs to stay unaware of its subclasses
//
public class Document2 {

	private String id;
	private Map<String, List<Object>> fieldMap = new LinkedHashMap<>();

	public Map<String, List<Object>> getFieldMap() {
		return fieldMap;
	}

	public Document2() {
	}

	public Document2(String id) {
		this.id = id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getFirstValueAsString(String fieldName) {
		Object firstVal = getFirstValue(fieldName);
		return (firstVal == null) ? null : firstVal.toString();
	}

	public Object getFirstValue(String fieldName) {
		return fieldMap.containsKey(fieldName) ? fieldMap.get(fieldName).get(0) : null;
	}

	public void setField(String fieldName, Object fieldValue) {
		if (fieldValue != null) {
			List<Object> values = new ArrayList<>();
			if (fieldValue instanceof Collection<?>) {
				Collection<?> col = (Collection<?>) fieldValue;
				values.addAll(col);
			} else {
				values.add(fieldValue);
			}
			fieldMap.put(fieldName, values);
		}
	}

	public Collection<String> getFieldNames() {
		return fieldMap.keySet();
	}

	public List<Object> getFieldValues(String fieldName) {
		List<Object> values = fieldMap.get(fieldName);
		return (values == null) ? new ArrayList<>() : values;
	}

	public String toString() {
		return "Document [id=" + id + ", fieldMap: " + fieldMap + "]";
	}
}
