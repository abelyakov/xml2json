package sevenbit.xml2json;

import org.junit.Assert;
import org.junit.Test;

import static sevenbit.xml2json.Json2XmlConverter.json2xml;
import static sevenbit.xml2json.Xml2JsonConverter.xml2json;

/**
 * Created by andy on 12/24/15.
 */
public class Xml2JsonConverterTest {

	@Test
	public void realWorldTest() {
		String input = "{\"v1:UpdateAvailableResource\":" +
				"{\"id\":\"13878189867\"," +
				"\"departmentId\":\"13947473\"," +
				"\"name\":\"\\\"Тестовый ресурс15\\\"\",\"" +
				"allowSelfService\":true,\"" +
				"doctorOnDuty\":\"false\",\"" +
				"employees\":[{\"id\":\"11313225\",\"isMain\":true}],\"" +
				"receptionTypes\":[{\"id\":\"1863\",\"isAvailable\":false}]," +
				"\"serviceAreaTypes\":[\"451052\",\"209\"],\"complexResources\":[\"13355213\",\"13354188\"],\"ldps\":[{\"id\":\"135261\",\"isAvailable\":true}],\"version\":\"2\",\"attrs\":{\"xmlns:v1\":\"http://emias2.gov.ru/resources/v1/\"}}}";
		String xml = json2xml(input);
		System.out.println(xml);
		Assert.assertTrue(input.contains("allowSelfService"));
		Assert.assertTrue(xml.contains("allowSelfService"));
		String json =xml2json(xml);
		System.out.println(json);
//		Assert.assertTrue(json.contains("version"));
	}

	@Test
	public void emptyStringTest() {
		String input = "";
		xmlJsonXmlJsonTest(input, true);
	}

	@Test
	public void oneTagTest() {
		String input = "<one></one>";
		xmlJsonXmlJsonTest(input, true);
	}
	@Test
	public void oneTagWithAttrsTest() {
		String input = "<one name=\"1\"></one>";
		xmlJsonXmlJsonTest(input, true);
	}

	@Test
	public void oneTagWithNameAndAttrsTest() {
		String input = "<one name=\"1\">один</one>";
		xmlJsonXmlJsonTest(input, true);
	}

	@Test
	public void tagWithNameAndAttrsTest2() {
		String input = "<one name=\"1\">один</one>";
		String json = xml2json(input);
		Assert.assertTrue(json.contains("@"));
	}

	@Test
	public void simpleArrayTest() {
		String input = "<one test=\"attr\">\n" +
				"\t<two>test1</two>\n" +
				"\t<two>test2</two>\n" +
				"\t<five>\n" +
				"\t\t<four>1</four>\n" +
				"\t</five>\n" +
				"</one>";
		xmlJsonXmlJsonTest(input, true);
	}

	@Test
	public void getSpecialitiesInfoRequestTest() {
		String input = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:api=\"http://api.service.soap.emias.lanit.ru/\">\n" +
				"   <soapenv:Header/>\n" +
				"   <soapenv:Body>\n" +
				"      <api:getSpecialitiesInfo>\n" +
				"         <!--Optional:-->\n" +
				"         <omsNumber>?</omsNumber>\n" +
				"         <!--Optional:-->\n" +
				"         <omsSeries>?</omsSeries>\n" +
				"         <!--Optional:-->\n" +
				"         <birthDate>?</birthDate>\n" +
				"         <!--Optional:-->\n" +
				"         <lpuId>?</lpuId>\n" +
				"         <!--Optional:-->\n" +
				"         <externalSystemId>?</externalSystemId>\n" +
				"      </api:getSpecialitiesInfo>\n" +
				"   </soapenv:Body>\n" +
				"</soapenv:Envelope>";
		xmlJsonXmlJsonTest(input, true);
	}


	@Test
	public void largeComplexTest() {
		String input = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
				"<soap:Body>\n" +
				"<ns2:getAvailableResourceScheduleInfoResponse xmlns:ns2=\"http://api.service.soap.emias.lanit.ru/\">\n" +
				"<return>\n" +
				"<availableResourceId>11319372</availableResourceId>\n" +
				"<lpuId>10000375</lpuId>\n" +
				"<doctorSpeciality>Врач-терапевт участковый</doctorSpeciality>\n" +
				"<doctorFio>Бакшеева Гульшат Шамилевна</doctorFio>\n" +
				"<availableResourceName>ГП16 Бакшеева Г.Ш. &lt;404&gt;</availableResourceName>\n" +
				"\n" +
				"\n" +
				"<schedules>\n" +
				"<date>2015-12-25T00:00:00+03:00</date>\n" +
				"<resourceSchedule>\n" +
				"<complexResourceId>12504465</complexResourceId>\n" +
				"<cabinetName>332</cabinetName>\n" +
				"<workTime/>\n" +
				"</resourceSchedule>\n" +
				"<resourceSchedule>\n" +
				"<complexResourceId>11130988</complexResourceId>\n" +
				"<cabinetName>404</cabinetName>\n" +
				"<workTime/>\n" +
				"\n" +
				"<timePeriods>\n" +
				"<startTime>2015-12-25T13:15:00+03:00</startTime>\n" +
				"<endTime>2015-12-25T13:30:00+03:00</endTime>\n" +
				"<allowedAppointment>true</allowedAppointment>\n" +
				"</timePeriods>\n" +
				"<timePeriods>\n" +
				"<startTime>2015-12-25T15:00:00+03:00</startTime>\n" +
				"<endTime>2015-12-25T15:15:00+03:00</endTime>\n" +
				"<allowedAppointment>true</allowedAppointment>\n" +
				"</timePeriods>\n" +
				"</resourceSchedule>\n" +
				"</schedules>\n" +
				"<schedules>\n" +
				"<date>2015-12-29T00:00:00+03:00</date>\n" +
				"<resourceSchedule>\n" +
				"<complexResourceId>12504465</complexResourceId>\n" +
				"<cabinetName>332</cabinetName>\n" +
				"<workTime/>\n" +
				"</resourceSchedule>\n" +
				"\n" +
				"<resourceSchedule>\n" +
				"<complexResourceId>11130988</complexResourceId>\n" +
				"<cabinetName>404</cabinetName>\n" +
				"<workTime/>\n" +
				"<timePeriods>\n" +
				"<startTime>2015-12-30T12:45:00+03:00</startTime>\n" +
				"<endTime>2015-12-30T13:00:00+03:00</endTime>\n" +
				"<allowedAppointment>true</allowedAppointment>\n" +
				"</timePeriods>\n" +
				"<timePeriods>\n" +
				"<startTime>2015-12-30T19:45:00+03:00</startTime>\n" +
				"<endTime>2015-12-30T20:00:00+03:00</endTime>\n" +
				"<allowedAppointment>true</allowedAppointment>\n" +
				"</timePeriods>\n" +
				"</resourceSchedule>\n" +
				"</schedules>\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"<schedules>\n" +
				"<date>2015-12-31T00:00:00+03:00</date>\n" +
				"<resourceSchedule>\n" +
				"<complexResourceId>12504465</complexResourceId>\n" +
				"<cabinetName>332</cabinetName>\n" +
				"<workTime/>\n" +
				"</resourceSchedule>\n" +
				"<resourceSchedule>\n" +
				"<complexResourceId>11130988</complexResourceId>\n" +
				"<cabinetName>404</cabinetName>\n" +
				"<workTime/>\n" +
				"<timePeriods>\n" +
				"<startTime>2015-12-31T08:00:00+03:00</startTime>\n" +
				"<endTime>2015-12-31T08:15:00+03:00</endTime>\n" +
				"<allowedAppointment>true</allowedAppointment>\n" +
				"</timePeriods>\n" +
				"<timePeriods>\n" +
				"<startTime>2015-12-31T15:00:00+03:00</startTime>\n" +
				"<endTime>2015-12-31T15:15:00+03:00</endTime>\n" +
				"<allowedAppointment>true</allowedAppointment>\n" +
				"</timePeriods>\n" +
				"</resourceSchedule>\n" +
				"</schedules>\n" +
				"\n" +
				"\n" +
				"</return>\n" +
				"</ns2:getAvailableResourceScheduleInfoResponse>\n" +
				"</soap:Body>\n" +
				"</soap:Envelope>";

		xmlJsonXmlJsonTest(input, true);

	}

	public static void print(String value, boolean verbose) {
		if (verbose) {
			System.out.println(value);
			System.out.println("--------------------");
		}
	}

	public static void xmlJsonXmlJsonTest(String input, boolean verbose) {
		print(input, verbose);
		String json = xml2json(input);
		print(json, verbose);
		String xml = json2xml(json);
//		print(xml, verbose);
		String json2 = xml2json(xml);
		print(json2, verbose);

		Assert.assertEquals(json, json2);
		String xml2 = json2xml(json2);
//		Assert.assertEquals(xml, xml2);
	}
}
