package com.hexastax.jsonserde1.doc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonDocumentDeserializer1 extends JsonDeserializer<Document1> {

	@Override
	public Document1 deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode node = oc.readTree(jp);
		return doDeserialize(jp, ctxt, node);
	}

	private Document1 doDeserialize(JsonParser jp, DeserializationContext ctxt, JsonNode node) throws IOException {
		Document1 doc = new Document1();

		for (Iterator<Map.Entry<String, JsonNode>> iter = node.fields(); iter.hasNext();) {
			Map.Entry<String, JsonNode> entry = iter.next();

			String key = entry.getKey();
			JsonNode nodeValue = entry.getValue();

			if ("id".equals(key)) {
				doc.setId(nodeValue.textValue());
			} else if ("fields".equals(key)) {
				for (JsonNode fieldSpecNode : nodeValue) {
					String fName = null;
					String dataType = null;

					for (Iterator<String> fiter = fieldSpecNode.fieldNames(); fiter.hasNext();) {
						String fn = fiter.next();
						JsonNode fvNode = fieldSpecNode.get(fn);

						if ("name".equals(fn)) {
							fName = fvNode.textValue();
						} else if ("type".equals(fn)) {
							dataType = fvNode.textValue();
						} else if ("values".equals(fn)) {
							try {
								deserializeFieldValues(jp, ctxt, doc, fName, dataType, fvNode);
							} catch (DocumentException ex) {
								throw new IOException("Error while deserializing", ex);
							}
						}
					}
				}
			}
		}

		return doc;
	}

	private void deserializeFieldValues(JsonParser jp, DeserializationContext ctxt, Document1 doc, String fieldName,
			String dataType, JsonNode fieldValuesNode) throws IOException, DocumentException {
		DataType1 dt = DataType1.fromExternal(dataType);
		switch (dt) {
		case STRING:
			doc.setField(fieldName, getStringValues(fieldValuesNode));
			break;
		case BOOLEAN:
			doc.setField(fieldName, getBooleanValues(fieldValuesNode));
			break;
		case DATE:
			doc.setField(fieldName, getDateValues(fieldValuesNode));
			break;
		case INTEGER:
			doc.setField(fieldName, getIntegerValues(fieldValuesNode));
			break;
		case LONG:
			doc.setField(fieldName, getLongValues(fieldValuesNode));
			break;
		case FLOAT:
			doc.setField(fieldName, getFloatValues(fieldValuesNode));
			break;
		case DOUBLE:
			doc.setField(fieldName, getDoubleValues(fieldValuesNode));
			break;
		case DOM:
			doc.setField(fieldName, getDomValues(fieldValuesNode));
			break;
		case BINARY:
			doc.setField(fieldName, getByteArrayValues(fieldValuesNode));
			break;
		case DOCUMENT:
			doc.setField(fieldName, getDocumentValues(jp, ctxt, fieldValuesNode));
			break;
		case NULL:
			doc.setField(fieldName, getNullValues(fieldValuesNode));
			break;
		default:
			break;
		}
	}

	private List<String> getStringValues(JsonNode fieldValuesNode) {
		List<String> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(el.textValue());
		}
		return values;
	}
	
	private List<Boolean> getBooleanValues(JsonNode fieldValuesNode) {
		List<Boolean> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(el.booleanValue());
		}
		return values;
	}

	private List<Integer> getIntegerValues(JsonNode fieldValuesNode) {
		List<Integer> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(el.intValue());
		}
		return values;
	}

	private List<Long> getLongValues(JsonNode fieldValuesNode) {
		List<Long> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(el.longValue());
		}
		return values;
	}

	private List<Float> getFloatValues(JsonNode fieldValuesNode) {
		List<Float> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(el.floatValue());
		}
		return values;
	}

	private List<Double> getDoubleValues(JsonNode fieldValuesNode) {
		List<Double> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(el.doubleValue());
		}
		return values;
	}

	private List<Date> getDateValues(JsonNode fieldValuesNode) {
		List<Date> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(new Date(el.longValue()));
		}
		return values;
	}

	private List<org.dom4j.Document> getDomValues(JsonNode fieldValuesNode) throws DocumentException {
		List<org.dom4j.Document> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(DocumentHelper.parseText(el.textValue()));
		}
		return values;
	}
	
	private List<byte[]> getByteArrayValues(JsonNode fieldValuesNode) throws IOException {
		List<byte[]> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(el.binaryValue());
		}
		return values;
	}

	private List<Document1> getDocumentValues(JsonParser jp, DeserializationContext ctxt, JsonNode fieldValuesNode) throws IOException {
		List<Document1> values = new ArrayList<>();
		for (JsonNode el : fieldValuesNode) {
			values.add(doDeserialize(jp, ctxt, el));
		}
		return values;
	}

	private List<Object> getNullValues(JsonNode fieldValuesNode) {
		List<Object> values = new ArrayList<>();
		for (int i = 0; i < fieldValuesNode.size(); i++) {
			values.add(null);
		}
		return values;
	}

	/*
	 * TODO: Document.deserializeWithType? Need to figure out if this is needed and if so, is this the right implementation. 
	@Override
	public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
			throws IOException {
		return typeDeserializer.deserializeTypedFromObject(p, ctxt);
	}
	*/	
}

