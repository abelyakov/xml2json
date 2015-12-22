package sevenbit.xml2json;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by andy on 12/21/15.
 */
public class ApplicationTest {

	@Test
	public void nameWithoutPrefixTest() {
		String name = Application.getOutputFileName("test", false);
		Assert.assertEquals("test.json", name);
	}

	@Test
	public void nameWithXmlPrefixTest() {
		String name = Application.getOutputFileName("test.xml", false);
		Assert.assertEquals("test.json", name);
	}

	@Test
	public void nameWithJsonPrefixTest() {
		String name = Application.getOutputFileName("test.json", true);
		Assert.assertEquals("test.xml", name);
	}

	@Test
	public void argsTest() {
		Application.Args args = new Application.Args(new String[]{"-r", "one"});
		Assert.assertEquals(true, args.reverse);

		args = new Application.Args(new String[]{"-rr", "one"});
		Assert.assertEquals(false, args.reverse);

	}
}
