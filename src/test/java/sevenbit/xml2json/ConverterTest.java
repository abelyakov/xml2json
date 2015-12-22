package sevenbit.xml2json;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by andy on 12/22/15.
 */
public class ConverterTest {

	String xml =
			"<soapenv:Envelope " +
//					"soapenv:Header=\"123\" " +
					"xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:api=\"http://api.service.soap.emias.lanit.ru/\">\n" +
					"   <soapenv:Header/>\n" +
					"   <soapenv:Body>\n" +
					"      <api:getInsuredPersonInfoByPolicy>\n" +
					"         <!--Optional:-->\n" +
					"         <codeOMSPolicy>111</codeOMSPolicy>\n" +
					"         <!--Optional:-->\n" +
					"         <personBirthDate>222</personBirthDate>\n" +
					"      </api:getInsuredPersonInfoByPolicy>\n" +
					"   </soapenv:Body>\n" +
					"</soapenv:Envelope>";

	@Test
	public void xml2jsonTest() {
		String json = Xml2JsonConverter.xml2json(xml);
//		System.out.println(json);
//		System.out.println("*********************");
		String back = Json2XmlConverter.json2xml(json);
//		System.out.println(back);
		String backBack = Xml2JsonConverter.xml2json(back);
//		System.out.println(backBack);
		Assert.assertEquals(json, backBack);
	}

	@Test
	public void xml2jsonWithValueTest() {
		String xmlWithValue = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
				"test\n" +
				"</soapenv:Envelope>";

		String json = Xml2JsonConverter.xml2json(xmlWithValue);
		System.out.println(json);
		String back = Json2XmlConverter.json2xml(json);
		System.out.println(back);
//		String backBack = Xml2JsonConverter.xml2json(back);
//		System.out.println(backBack);
//		Assert.assertEquals(json, backBack);
	}
}
