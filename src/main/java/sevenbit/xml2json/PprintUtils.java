package sevenbit.xml2json;

import org.w3c.dom.Document;

import javax.json.Json;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 12/22/15.
 */
public class PprintUtils {

	public static String prettyPrint(JsonStructure json) {
		return jsonFormat(json, JsonGenerator.PRETTY_PRINTING);
	}

	public static String jsonFormat(JsonStructure json, String... options) {
		StringWriter stringWriter = new StringWriter();
		Map<String, Boolean> config = buildConfig(options);
		JsonWriterFactory writerFactory = Json.createWriterFactory(config);
		JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);

		jsonWriter.write(json);
		jsonWriter.close();

		return stringWriter.toString();
	}

	private static Map<String, Boolean> buildConfig(String... options) {
		Map<String, Boolean> config = new HashMap<>();

		if (options != null) {
			for (String option : options) {
				config.put(option, true);
			}
		}

		return config;
	}

//	public static String pprintXML(org.w3c.dom.Node element) {
////		Transformer transformer = null;
////		try {
////			transformer = TransformerFactory.newInstance().newTransformer();
////			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
////			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
////
////		} catch (TransformerConfigurationException e) {
////			e.printStackTrace();
////			return null;
////		}
////		StreamResult result = new StreamResult(new StringWriter());
////		DOMSource source = new DOMSource(element);
////		try {
////			transformer.transform(source, result);
////		} catch (TransformerException e) {
////			e.printStackTrace();
////			return null;
////		}
////		return result.getWriter().toString();
//
//
//	}

	public static void prettyPrintXML(Source source, OutputStream stream) {
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.
					setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, new StreamResult(stream));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public static void prettyPrintXML(Document doc, OutputStream stream) {
		prettyPrintXML(new DOMSource(doc), stream);
	}


	public static String prettyPrintXML(org.w3c.dom.Node doc) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		prettyPrintXML(new DOMSource(doc), baos);
		return new String(baos.toByteArray(), Charset.defaultCharset()).replace("&amp;", "&").replace("&quot;", "");


	}

	public static void prettyPrintXML(String xmlString, OutputStream stream) {
		prettyPrintXML(new StreamSource(new StringReader(xmlString)), stream);
	}

}
