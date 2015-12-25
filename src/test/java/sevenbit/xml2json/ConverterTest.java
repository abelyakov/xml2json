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

	/*Test when xml has tag with attributes and text value
	 json shoud contain extra @-tag for text and attr-tag for attributes*/
	@Test
	public void xml2jsonWithValueTest() {
		String xmlWithValue = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
				"test\n" +
				"</soapenv:Envelope>";

		String json = Xml2JsonConverter.xml2json(xmlWithValue);
//		System.out.println(json);
		String back = Json2XmlConverter.json2xml(json);
//		System.out.println(back);
		String backBack = Xml2JsonConverter.xml2json(back);
//		System.out.println(backBack);
		Assert.assertEquals(json, backBack);
	}

	@Test
	public void xml2jsonHugeTest() {
		String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:api=\"http://api.service.soap.emias.lanit.ru/\">\n" +
				"   <soapenv:Header/>\n" +
				"   <soapenv:Body>\n" +
				"      <api:cancelAppointment>\n" +
				"         <!--Optional:-->\n" +
				"         <omsNumber>?</omsNumber>\n" +
				"         <!--Optional:-->\n" +
				"         <omsSeries>?</omsSeries>\n" +
				"         <!--Optional:-->\n" +
				"         <birthDate>?</birthDate>\n" +
				"         <!--Optional:-->\n" +
				"         <externalSystemId>?</externalSystemId>\n" +
				"         <!--Optional:-->\n" +
				"         <appointmentId>?</appointmentId>\n" +
				"         <!--Optional:-->\n" +
				"         <identificationData>\n" +
				"            <cardOwner>\n" +
				"               <cardType>?</cardType>\n" +
				"               <uecCode>?</uecCode>\n" +
				"               <uecInstance>?</uecInstance>\n" +
				"            </cardOwner>\n" +
				"            <identificationDevice>\n" +
				"               <uecDeviceInstance>?</uecDeviceInstance>\n" +
				"            </identificationDevice>\n" +
				"         </identificationData>\n" +
				"      </api:cancelAppointment>\n" +
				"   </soapenv:Body>\n" +
				"</soapenv:Envelope>";

		String json = Xml2JsonConverter.xml2json(xml);
		System.out.println(json);
//		System.out.println("*********************");
		String back = Json2XmlConverter.json2xml(json);
		System.out.println(back);
		String backBack = Xml2JsonConverter.xml2json(back);
//		System.out.println(backBack);
		Assert.assertEquals(json, backBack);
	}

	@Test
	public void testWithArray() {
		String xml = "       <numbers type=\"array\">\n" +
				"            <value>3</value>\n" +
				"            <value>2</value>\n" +
				"            <value>1</value>\n" +
				"        </numbers>";

		String json = Xml2JsonConverter.xml2json(xml);
		System.out.println(json);
	}

	@Test
	public void buildNodesDequeTest() {
		String content = "<numbers type=\"array\">\n" +
				"            <value>3</value>\n" +
				"            <value>2</value>\n" +
				"            <value>1</value>\n" +
				"        </numbers>";

		String content2 = "<one test=\"attr\">\n" +
				"\t<two>test2</two>\n" +
				"\t<two>test1</two>\n" +
				"\t<five>\n" +
				"\t\t<four>1</four>\n" +
				"\t</five>\n" +
				"</one>";

		String content3 = "";

		String str = Xml2JsonConverter.xml2json(content);
		System.out.println(str);

	}
}
