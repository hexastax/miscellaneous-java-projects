package com.hexastax.jsonserde2.utils;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.hexastax.jsonserde2.doc.Document2;

public class MyUtils2 {

	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String toCompactString(org.dom4j.Document document) throws IOException {
		OutputFormat format = OutputFormat.createCompactFormat();
		return domToString(document, format);
	}

	private static String domToString(org.dom4j.Document document, OutputFormat format) throws IOException {
		format.setNewLineAfterDeclaration(false);
		StringWriter sw = new StringWriter();
		XMLWriter writer = new XMLWriter(sw, format);
		writer.write(document);
		writer.flush();
		writer.close();
		return sw.toString();
	}
	
	public static void printDocument (Document2 doc, PrintStream out) {
		out.println("\n>> DOCUMENT:");
		out.println(">> ID: " + doc.getId());
		
		for (String fieldName : doc.getFieldNames()) {
			Collection<Object> fieldValues = doc.getFieldValues(fieldName);
		
			for (Object fieldValue : fieldValues) {
				String strFieldValue = null;
				if (fieldValue instanceof Date) {
					strFieldValue = DF.format((Date) fieldValue);
				} else if (fieldValue instanceof org.dom4j.Document) {
					try {
						strFieldValue = toCompactString((org.dom4j.Document) fieldValue);
					} catch (IOException ex) {
						strFieldValue = fieldValue.toString();
					}
				} else if (fieldValue instanceof byte[]) {
					try {
						strFieldValue = new String((byte[]) fieldValue, "UTF-8");
					} catch (UnsupportedEncodingException ex) {
						// ...
					}
				} else if (fieldValue == null){
					strFieldValue = "null";
				} else {
					strFieldValue = fieldValue.toString();
				}
				
				out.println(">> Field: " + padRight(fieldName, 30) + 
						" - Value: " + padRight(strFieldValue, 40) + " - " + fieldValue);
			}
		}
	}
	
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}
}
