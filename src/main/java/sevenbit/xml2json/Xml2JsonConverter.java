package sevenbit.xml2json;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by andy on 12/22/15.
 */
public class Xml2JsonConverter {
	public static String xml2json(String content) {
		if (content.trim().isEmpty()) return "";
		DocumentBuilder builder = createDocumentBuilder();
		if (builder == null) return null;
		InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
		Document document;
		try {
			document = builder.parse(stream);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			return null;
		}

		document.getDocumentElement().normalize();
		Element element = document.getDocumentElement();
		Deque<DocumentNode> q = buildNodesQueue(element);
		return elementQueueToJson(q);
	}

	static String elementQueueToJson(Deque<DocumentNode> q) {
		if (q.isEmpty()) return "";

		int parentId = q.getFirst().parentId;

		Map<Integer, JsonObjectBuilder> childBuilders = new HashMap<>();
		while (!q.isEmpty()) {
			Map<String, List<DocumentNode>> siblings = pollSiblings(q);
			JsonObjectBuilder builder = Json.createObjectBuilder();
			siblings.entrySet().stream().sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey())).forEach(e -> processSiblings(e.getValue(), childBuilders, builder));
			DocumentNode cur = siblings.entrySet().iterator().next().getValue().iterator().next();
			childBuilders.put(cur.parentId, builder);
		}
		JsonObjectBuilder mainBuilder = childBuilders.get(parentId);
		return PprintUtils.prettyPrint(mainBuilder.build());
	}

	static void fillAttributes(Map<String, String> attributes, JsonObjectBuilder builder) {
		JsonObjectBuilder attrBuilder = Json.createObjectBuilder();
		attributes.entrySet().stream()
				.forEach(e -> attrBuilder.add(e.getKey(), e.getValue()));
		builder.add(Constants.ATTRS, attrBuilder);
	}

	//poll all siblings from queue and group them by name
	static Map<String, List<DocumentNode>> pollSiblings(Deque<DocumentNode> q) {
		DocumentNode cur = q.pollLast();
		List<DocumentNode> siblings = new ArrayList<>();
		siblings.add(cur);
		while (true) { //collect all siblings
			DocumentNode next = q.peekLast();
			if (next != null && next.parentId == cur.parentId) {
				q.pollLast();
				siblings.add(next);
			} else {
				break;
			}
		}
		return siblings.stream().collect(Collectors.groupingBy(s -> s.name));
	}

	static void processSiblings(List<DocumentNode> siblings, Map<Integer, JsonObjectBuilder> childBuilders, JsonObjectBuilder parentBuilder) {
		if (siblings.size() == 1) {
			DocumentNode s = siblings.get(0);
			JsonObjectBuilder childBuilder = childBuilders.get(s.id);
			//атрибуты
			if (childBuilder != null) {
				if (!s.attributes.isEmpty()) {
					fillAttributes(s.attributes, childBuilder);
				}
				parentBuilder.add(s.name, childBuilder);

			} else {
				if (!s.attributes.isEmpty()) {
					childBuilder = Json.createObjectBuilder();
					fillAttributes(s.attributes, childBuilder);
					if (s.text != null && !s.text.isEmpty())
						childBuilder.add(Constants.VALUE, s.text);
					parentBuilder.add(s.name, childBuilder);
				} else {
					parentBuilder.add(s.name, s.text == null ? "" : s.text);
				}
			}

		} else {
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (DocumentNode s : siblings) {
				JsonObjectBuilder childBuilder = childBuilders.get(s.id);
				if (childBuilder != null) {
					if (!s.attributes.isEmpty()) {
						fillAttributes(s.attributes, childBuilder);
					}
					arrayBuilder.add(childBuilder);
				} else {
					if (!s.attributes.isEmpty()) {
						childBuilder = Json.createObjectBuilder();
						fillAttributes(s.attributes, childBuilder);
						if (s.text != null && !s.text.isEmpty())
							childBuilder.add(Constants.VALUE, s.text);
						arrayBuilder.add(childBuilder);
					} else {
						arrayBuilder.add(s.text == null ? "" : s.text);
					}
				}
			}
			parentBuilder.add(siblings.get(0).name, arrayBuilder);
		}
	}

	static Deque<DocumentNode> buildNodesQueue(Node root) {
		//обходим дерево в ширину (по уровням) и формируем очередь с узлами дерева
		Deque<NodeWithParent> q = new ArrayDeque<>();
		Deque<DocumentNode> result = new ArrayDeque<>();
		q.add(new NodeWithParent(root, null));
		while (!q.isEmpty()) {
			NodeWithParent withDepth = q.removeFirst();
			Node n = withDepth.node;
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				DocumentNode docCurrent = new DocumentNode();
				docCurrent.name = n.getNodeName();
				docCurrent.parentId = System.identityHashCode(n.getParentNode());
				docCurrent.id = System.identityHashCode(n);
				if (n.hasAttributes()) {
					docCurrent.attributes = getAttributesMap(n);
				}
				result.add(docCurrent);

				for (int i = 0; i < n.getChildNodes().getLength(); ++i) {
					q.add(new NodeWithParent(n.getChildNodes().item(i), docCurrent));
				}

			} else if (n.getNodeType() == Node.TEXT_NODE) {
				String textValue = n.getNodeValue().trim();
				if (withDepth.parent != null) {
					if (!textValue.isEmpty()) {
						withDepth.parent.text = textValue;
					}
				}
			}
		}
		return result;
	}

	static Map<String, String> getAttributesMap(Node node) {
		HashMap<String, String> result = new HashMap<>(node.getAttributes().getLength());
		for (int i = 0; i < node.getAttributes().getLength(); ++i) {
			Attr attr = (Attr) node.getAttributes().item(i);
			result.put(attr.getNodeName(), attr.getNodeValue());
		}
		return result;
	}

	static class DocumentNode {
		Map<String, String> attributes = new HashMap<>();
		int parentId;
		int id;
		String text;
		String name;
	}

	static class NodeWithParent {
		final Node node;
		final DocumentNode parent;

		public NodeWithParent(Node node, DocumentNode parent) {
			this.node = node;
			this.parent = parent;
		}
	}

	static DocumentBuilder createDocumentBuilder() {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			return builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

}
