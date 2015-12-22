package sevenbit.xml2json;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.xml.parsers.DocumentBuilder;
import java.io.StringReader;
import java.util.Map;

/**
 * Created by andy on 12/22/15.
 */
public class Json2XmlConverter {

	public static String json2xml(String content) {
		DocumentBuilder builder = Xml2JsonConverter.createDocumentBuidler();
		if (builder == null) return null;
		Document doc = builder.newDocument();


		JsonObject jsonObject = null;
		try (JsonReader jsonReader = Json.createReader(new StringReader(content))) {
			jsonObject = jsonReader.readObject();
			if (jsonObject.entrySet().size() > 1)
				throw new RuntimeException("Only one root node allowed");

			Map.Entry<String, JsonValue> root = jsonObject.entrySet().iterator().next();
			convertJsonNode(doc, null, root);
			return PprintUtils.prettyPrintXML(doc);

		}
	}

	private static void convertJsonNode(Document doc, Element parent, Map.Entry<String, JsonValue> entry) {
		Element xmlElement = doc.createElement(entry.getKey());
		if (parent != null) {
			parent.appendChild(xmlElement);
		} else {
			doc.appendChild(xmlElement);
		}

		if (entry.getValue().getValueType() == JsonValue.ValueType.OBJECT) {
			JsonObject children = (JsonObject) entry.getValue();
			if (children.containsKey(Constants.ATTRS)) {
				JsonObject attrNode = (JsonObject) children.get(Constants.ATTRS);
				for (Map.Entry<String, JsonValue> attr : attrNode.entrySet()) {
					xmlElement.setAttribute(attr.getKey(), attr.getValue().toString());
				}
			}
			for (Map.Entry<String, JsonValue> child : children.entrySet()) {
				if (child.getKey().equals(Constants.ATTRS)) continue;   //attributes
				if (child.getKey().equals(Constants.VALUE)) {       //attributes with value
					xmlElement.setTextContent(child.getValue().toString().replace("\"", ""));
				} else {
					JsonValue.ValueType childValueType = child.getValue().getValueType();

					if (childValueType == JsonValue.ValueType.OBJECT) {
						convertJsonNode(doc, xmlElement, child);
					} else if (childValueType == JsonValue.ValueType.STRING) {
						if (!child.getValue().toString().isEmpty()) { //text child element
							Element textChildElement = doc.createElement(child.getKey());
							xmlElement.appendChild(textChildElement);
							textChildElement.setTextContent(child.getValue().toString().replace("\"", ""));
						}
					}
				}
			}

		}

	}
}
