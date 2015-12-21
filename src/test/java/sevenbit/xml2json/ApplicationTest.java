package sevenbit.xml2json;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by andy on 12/21/15.
 */
public class ApplicationTest {

	@Test
	public void test1() {
		String name = Application.getOutputFileName("test", false);
		Assert.assertEquals("test.json", name);
	}

	@Test
	public void test2() {
		String name = Application.getOutputFileName("test.xml", false);
		Assert.assertEquals("test.json", name);
	}

	@Test
	public void test3() {
		String name = Application.getOutputFileName("test.json", true);
		Assert.assertEquals("test.xml", name);
	}
}