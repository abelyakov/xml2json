package sevenbit.xml2json;

import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Convert xml content to json representation
 * Created by andy on 12/21/15.
 */
public class Application {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("No xml files given! Usage: xmlj2json input1.xml input2.xml");
		}

		List<String> filesWritten = new ArrayList<String>(args.length);
		for (String fileName : args) {
			String content;
			try {
				content = String.join("\n", Files.readAllLines(Paths.get(fileName)));
			} catch (IOException e) {
				System.out.println("Exception while processing file " + fileName + ":");
				System.out.println(e);
				continue;
			}

			JSONObject jsonContent = XML.toJSONObject(content);

			final String outputFileName = fileName + ".json";
			try {
				Files.write(Paths.get(outputFileName), jsonContent.toString().getBytes(), StandardOpenOption.CREATE);
				filesWritten.add(outputFileName);
			} catch (IOException e) {
				System.out.println("Exception while writing the result to " + outputFileName);
				System.out.println(e);
			}

			System.out.println("output: " + Arrays.toString(filesWritten.toArray()));
		}
	}
}
