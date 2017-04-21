package com.hexastax.jsonserde1.doc;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.hexastax.jsonserde1.utils.MyUtils;

public class JsonDocumentSerializer extends JsonSerializer<Document> {

	@Override
	public void serialize(Document doc, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		// jgen.writeStartObject();

		setStringValue(jgen, "id", doc.getId());
		jgen.writeArrayFieldStart("fields");

		for (String fieldName : doc.getFieldNames()) {
			List<Object> fieldValues = doc.getFieldValues(fieldName);
			DataType dataType = getDataType(fieldValues);

			jgen.writeStartObject();

			setStringValue(jgen, "name", fieldName);
			setStringValue(jgen, "type", dataType.getValue());
			jgen.writeArrayFieldStart("values");
			for (Object fieldValue : fieldValues) {
				serializeValue(jgen, provider, fieldValue);
			}
			jgen.writeEndArray();

			jgen.writeEndObject();
		}

		jgen.writeEndArray();

		// jgen.writeEndObject();
	}

	private static void setStringValue(JsonGenerator jgen, String fieldName, String fieldValue) throws IOException {
		if (fieldValue != null) {
			jgen.writeStringField(fieldName, fieldValue);
		}
	}

	// The cumbersome dance to ascertain the data type for a list of values ...
	private static DataType getDataType(List<Object> values) {
		Object val = null;
		for (Object value : values) {
			if (value != null) {
				val = value;
				break;
			}
		}
		return DataType.fromObjectValue(val);
	}

	private void serializeValue(JsonGenerator jgen, SerializerProvider provider, Object value) throws IOException {
		if (value instanceof org.dom4j.Document) {
			jgen.writeString(MyUtils.toCompactString((org.dom4j.Document) value));
		} else {
			jgen.writeObject(value);
		}
	}

	/*
	 * TODO: JsonDocumentSerializer: need to figure out if this is needed and if so, is this the right implementation of the method?
	 */
	@Override
	public void serializeWithType(Document value, JsonGenerator gen, SerializerProvider serializers,
			TypeSerializer typeSer) throws IOException {
		typeSer.writeTypePrefixForObject(value, gen);
		serialize(value, gen, serializers);
		typeSer.writeTypeSuffixForObject(value, gen);
	}

}
