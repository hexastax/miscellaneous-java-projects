package com.hexastax.jsonserde1.doc;

import java.util.Date;

/**
 * Holds the list of supported data types.
 */
public enum DataType1 {

	// TODO: DataType: can we avoid maintaining a hardcoded list of data types and all the special data type handling??
	// TODO: DataType: how do we handle a "random" object?
	// TODO: DataType: what about collections and maps?
	
	STRING("String"),
	BOOLEAN("Boolean"),
	INTEGER("Integer"),
	LONG("Long"),
	FLOAT("Float"),
	DOUBLE("Double"),
	DATE("Date"),
	
	// For Dom4j DOM's
	DOM("DOM"),
	
	// For byte arrays
	BINARY("Binary"),
	
	// Document as in com.hexastax.jsonserde1.doc.Document
	DOCUMENT("Document"),
	
	NULL("Null");
	
	private String typeName;
	
	private DataType1(String typeName) {
		this.typeName = typeName;
	}
	
	public String getValue() {
		return typeName;
	}
	
	public static DataType1 fromExternal(String strValue) {
		for (DataType1 dataType : values()) {
			if (dataType.getValue().equals(strValue)) {
				return dataType;
			}
		}
		throw new IllegalArgumentException("No matching data type for " + strValue);
	}
	
	public static DataType1 fromObjectValue(Object value) {
		DataType1 type = null;
		
		// ick ...
		
		if (value == null) {
			type = NULL;
		} else if (value instanceof String) {
			type = STRING;
		} else if (value instanceof Boolean) {
			type = DataType1.BOOLEAN;
		} else if (value instanceof Integer) {
			type = DataType1.INTEGER;
		} else if (value instanceof Long) {
			type = DataType1.LONG;
		} else if (value instanceof Float) {
			type = FLOAT;
		} else if (value instanceof Double) {
			type = DOUBLE;
		} else if (value instanceof Date) {
			type = DATE;
		} else if (value instanceof org.dom4j.Document) {
			type = DOM;
		} else if (value instanceof byte[]) {
			type = BINARY;
		} else if (value instanceof Document1) {
			type = DOCUMENT;
		} else {
			throw new IllegalArgumentException("No matching data type for " + value.getClass().getName());
		}
		 
		return type;
	}
}









