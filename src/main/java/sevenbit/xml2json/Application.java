package sevenbit.xml2json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Convert xml content to json representation
 * Created by andy on 12/21/15.
 */
public class Application {

	public static void main(String[] args) {
		final Args arguments = new Args(args);
		if (arguments.files.isEmpty()) {
			printInfoMessage();
			return;
		}

		for (String fileName : arguments.files) {
			String content = fileToString(fileName);
			if (content == null) continue;

			String convertedContent = arguments.reverse
					? Json2XmlConverter.json2xml(content)
					: Xml2JsonConverter.xml2json(content);

			final String outputFileName = getOutputFileName(fileName, arguments.reverse);
			boolean success = stringToFile(convertedContent, outputFileName);
			if (success) {
				System.out.println("output: " + outputFileName);
			}
		}
	}

	static String getOutputFileName(String inputName, boolean reverse) {
		final String newPrefix = reverse ? ".xml" : ".json";
		final String oldPrefix = reverse ? ".json" : ".xml";

		if (inputName.length() > 4 && inputName.substring(inputName.length() - oldPrefix.length(), inputName.length()).equals(oldPrefix)) {
			return inputName.substring(0, inputName.length() - oldPrefix.length()) + newPrefix;
		}
		return inputName + newPrefix;
	}

	private static String fileToString(String fileName) {
		try {
			return String.join("\n", Files.readAllLines(Paths.get(fileName)));
		} catch (IOException e) {
			System.out.println("Exception while processing file " + fileName + ":");
			System.out.println(e);
			return null;
		}
	}

	private static boolean stringToFile(String str, String fileName) {
		try {
			Files.write(Paths.get(fileName), str.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			System.out.println("Exception while writing the result to " + fileName);
			System.out.println(e);
			return false;
		}
		return true;
	}

	private static void printInfoMessage() {
		System.out.println("No xml files given!");
		System.out.println("Usage: java -jar xml2json.jar input1.xml input2.xml");
		System.out.println("flag: -r  reverse transformation from json to xml");
	}


	static class Args {
		final static String REVERSE_KEY = "-r";
		final List<String> files;
		final boolean reverse;

		public Args(String[] args) {
			boolean reverse = false;
			files = new ArrayList<String>(args.length);
			for (String arg : args) {
				if (REVERSE_KEY.equals(arg)) {
					reverse = true;
				} else {
					files.add(arg);
				}
			}
			this.reverse = reverse;
		}
	}
}
