package sevenbit.xml2json;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by andy on 12/22/15.
 */
public class Xml2JsonConverter {
	public static String xml2json(String content) {
		DocumentBuilder builder = createDocumentBuidler();
		if(builder == null) return null;

		InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
		Document document;
		try {
			document = builder.parse(stream);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}

		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

		document.getDocumentElement().normalize();
		Element element = document.getDocumentElement();
		convertXmlNode(element, jsonBuilder);


		String result = PprintUtils.prettyPrint(jsonBuilder.build());
		return result;
	}


	private static void convertXmlNode(Node node, JsonObjectBuilder builder) {
		if (node.getNodeType() != Node.ELEMENT_NODE)
			return;
		JsonObjectBuilder elementBuilder = Json.createObjectBuilder();
		boolean elementBuilderNotEmpty = false;

		if (node.hasAttributes()) { //attributes
			elementBuilderNotEmpty = true;
			elementBuilder.add("attrs", getAttributesNodeBuilder(node));
		}

		String nodeTextValue = null;
		if (node.hasChildNodes()) { //children
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); ++i) {
				Node child = children.item(i);
				switch (child.getNodeType()) {
					case Node.ELEMENT_NODE:
						elementBuilderNotEmpty = true;
						convertXmlNode(child, elementBuilder);
						break;
					case Node.TEXT_NODE:
						String textValue = child.getNodeValue().trim();
						if (textValue.isEmpty()) continue;
						if (nodeTextValue != null)
							throw new RuntimeException("node already have text value: " + nodeTextValue + "new text value: " + textValue);
						nodeTextValue = textValue;
						break;
				}
			}
		}

		nodeTextValue = nodeTextValue == null ? "" : nodeTextValue;
		if (elementBuilderNotEmpty) {
			if (!nodeTextValue.isEmpty()) {
				elementBuilder.add("#value", nodeTextValue);
			}
			builder.add(node.getNodeName(), elementBuilder);
		} else {
			builder.add(node.getNodeName(), nodeTextValue);
		}
	}

	private static JsonObjectBuilder getAttributesNodeBuilder(Node element) {
		NamedNodeMap attributes = element.getAttributes();
		JsonObjectBuilder builder = Json.createObjectBuilder();
		for (int i = 0; i < attributes.getLength(); ++i) {
			Attr attr = (Attr) attributes.item(i);
			builder.add(attr.getNodeName(), attr.getNodeValue());
		}
		return builder;
	}

	static DocumentBuilder createDocumentBuidler() {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			return builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
}
