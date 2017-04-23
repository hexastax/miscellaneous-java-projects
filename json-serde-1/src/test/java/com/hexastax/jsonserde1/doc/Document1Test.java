package com.hexastax.jsonserde1.doc;

import java.util.Date;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Document1Test {

	// NOTE:
	// With the custom Document serializer/deserializer, we get a nice JSON structure like so:
	/*
{
  "com.hexastax.jsonserde1.doc.Document" : {
    "id" : "123",
    "fields" : [ {
      "name" : "aaa",
      "type" : "String",
      "values" : [ "bbb" ]
    }, {
      "name" : "ccc",
      "type" : "Integer",
      "values" : [ 12 ]
    }, {
      "name" : "dateField",
      "type" : "Date",
      "values" : [ 1492791505220 ]
    }, {
      "name" : "domField",
      "type" : "DOM",
      "values" : [ "<?xml version=\"1.0\" encoding=\"UTF-8\"?><person><firstname>John</firstname><lastname>Smith</lastname></person>" ]
    } ]
  }
}
	 */

	// NOTE: without the custom serializer/deserializer:
	//
	// Problem 1: DOM's --
	// Get JsonMappingException: Infinite recursion (StackOverflowError) ! on the DOM value
	//
	// Problem 2: Dates --
	/*
{
  "id" : "123",
  "fieldMap" : {
    "aaa" : [ "bbb" ],
    "ccc" : [ 12 ],
    "dateField" : [ 1492791811418 ]
  },
  "fieldNames" : [ "aaa", "ccc", "dateField" ]
}
	 */
	// Date is serialized to JSON as a long, epoch value. Deserializer won't deserialize it properly as a Date.
	// I'm sure this is solved in the Jackson framework... pluggable date serde that would have to be plugged in someplace...
	
	@Test
	public void testDocumentJsonSerde() throws JsonProcessingException, DocumentException {
		Document1 doc = new Document1("123");
		doc.setField("aaa", "bbb");
		doc.setField("ccc", 12);
		doc.setField("dateField", new Date());
		doc.setField("domField", DocumentHelper.parseText("<person><firstname>John</firstname><lastname>Smith</lastname></person>"));
		
		ObjectMapper om = new ObjectMapper();
		// om.enableDefaultTyping();
		String strJson = om.writerWithDefaultPrettyPrinter().writeValueAsString(doc);
		System.out.println(">> Serialized doc:");
		System.out.println(strJson);
		System.out.println();
	}
	
}
