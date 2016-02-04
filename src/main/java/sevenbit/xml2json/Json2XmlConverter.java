package sevenbit.xml2json;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.json.*;
import javax.xml.parsers.DocumentBuilder;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * Created by andy on 12/22/15.
 */
public class Json2XmlConverter {

	public static String json2xml(String content) {
		if (content.trim().isEmpty()) return "";
		DocumentBuilder builder = Xml2JsonConverter.createDocumentBuilder();
		if (builder == null) return null;
		Document doc = builder.newDocument();
		JsonObject jsonObject = null;
		try (JsonReader jsonReader = Json.createReader(new StringReader(content))) {
			jsonObject = jsonReader.readObject();
			if (jsonObject.entrySet().size() > 1)
				throw new RuntimeException("Only one root node allowed");
			Map.Entry<String, JsonValue> root = jsonObject.entrySet().iterator().next();
			convertJsonDocument(doc, root);
			return PprintUtils.prettyPrintXML(doc);
		}
	}

	private static void convertJsonDocument(Document doc, Map.Entry<String, JsonValue> rootEntry) {
		Deque<JsonNodeWithParent> stack = new ArrayDeque<>();
		stack.push(new JsonNodeWithParent(rootEntry, null));

		while (!stack.isEmpty()) {
			JsonNodeWithParent pair = stack.pop();
			JsonValue.ValueType type = pair.node.getValueType();
			if (type == JsonValue.ValueType.OBJECT) {
				Element e = doc.createElement(pair.name);
				JsonObject children = (JsonObject) pair.node;
				//attributes
				if (children.containsKey(Constants.ATTRS)) {
					JsonObject attrNode = (JsonObject) children.get(Constants.ATTRS);
					attrNode.entrySet().forEach(attr -> e.setAttribute(attr.getKey(), attr.getValue().toString()));
				}
				children.entrySet().stream()
						.sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
						.filter(c -> !c.getKey().equals(Constants.ATTRS))
						.forEach(c -> {
							if (c.getKey().equals(Constants.VALUE)) {
								e.setTextContent(c.getValue().toString().replace("\"", ""));
							} else {
								stack.push(new JsonNodeWithParent(c, e));
							}
						});
				appendChild(e, pair.parent, doc);
			} else if (type == JsonValue.ValueType.ARRAY) {
				JsonArray array = (JsonArray) pair.node;
				array.stream().forEach(c -> stack.push(new JsonNodeWithParent(pair.name, c, pair.parent)));
			} else if (type == JsonValue.ValueType.STRING
					|| type == JsonValue.ValueType.TRUE
					|| type == JsonValue.ValueType.FALSE
					|| type == JsonValue.ValueType.NUMBER){
				if (pair.node.toString().isEmpty())
					continue;
				Element e = doc.createElement(pair.name);
				e.setTextContent(pair.node.toString().replace("\"", ""));
				appendChild(e, pair.parent, doc);
			}
		}
	}

	private static void appendChild(Element e, Element parent, Document doc) {
		if (parent != null) {
			parent.appendChild(e);
		} else {
			doc.appendChild(e);
		}
	}

	static class JsonNodeWithParent {
		final String name;
		final JsonValue node;
		final Element parent;

		public JsonNodeWithParent(Map.Entry<String, JsonValue> entry, Element parentXml) {
			this.name = entry.getKey();
			this.node = entry.getValue();
			this.parent = parentXml;
		}

		public JsonNodeWithParent(String name, JsonValue node, Element parent) {
			this.name = name;
			this.node = node;
			this.parent = parent;
		}
	}
}

