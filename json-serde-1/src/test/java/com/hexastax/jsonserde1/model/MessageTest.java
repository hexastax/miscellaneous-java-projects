package com.hexastax.jsonserde1.model;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexastax.jsonserde1.doc.Document;
import com.hexastax.jsonserde1.utils.MyUtils;

public class MessageTest {

	@Test
	public void testMessageJsonSerde() throws IOException {
		ObjectMapper om = new ObjectMapper();
		// TODO: General: when using ObjectMapper at serialization or deserialization, anything to set on it??
		om.enableDefaultTyping();
		
		Message m = new Message("message-123");
		m.setTitle("Test message title");
		m.setBody("Test message body");
		
		String strJson = om.writeValueAsString(m);
		System.out.println(">> Serialized message:");
		System.out.println(strJson);
		System.out.println();
		System.out.println("===============================================");
		System.out.println();
		
		// TODO: Message deser: readValue(Document.class) - want to read as Message, not Document (high priority)
		Document deserializedDoc = om.readValue(strJson, Document.class);
		System.out.println(">> Deserializing as Document, class: " + deserializedDoc.getClass().getName());
		MyUtils.printDocument(deserializedDoc, System.out);
		
		// TODO: Message deser: readValue(Document.class) - want to read as Message, not Document (low priority)
		Object asRes = om.readValue(strJson, Resource.class);
		System.out.println(">> Deserializing as Resource, class: " + asRes.getClass().getName());
		System.out.println(asRes);
		
		// TODO: Message deser: readValue(Message.class) - want to read as Message, not Document (low priority)
		Object asMessage = om.readValue(strJson, Message.class);
		System.out.println(">> Deserializing as Message, class: " + asMessage.getClass().getName());
		System.out.println(asRes);
	}
	
}
