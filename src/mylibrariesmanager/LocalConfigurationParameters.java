package mylibrariesmanager;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.*;
import javax.xml.validation.*;
import javax.xml.transform.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LocalConfigurationParameters {
	
	private static String addressDBMS;
	private static String portDBMS;
	
	public static boolean retrieveLocalConfiguration() {
		boolean bool;
		Document document;
		Schema schema;
		String configurationFileXML = "configuration.xml";
		String configurationFileXSD = "configuration.xsd";
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
			document = builder.parse(new File(configurationFileXML));
			schema = schemaFactory.newSchema(new File(configurationFileXSD));
			
			schema.newValidator().validate(new DOMSource(document));
		} catch (ParserConfigurationException e) {
	         e.printStackTrace();
	         document = null;
	    } catch (SAXException e) {
	         e.printStackTrace();
	         document = null;
	    } catch (IOException e) {
	         e.printStackTrace();
	         document = null;
	    }
		
		if (document == null) //there is at least an exception
			bool = false;
		else { //no exception caught
			setAddressDBMS(document.getElementsByTagName("addressDBMS").item(0).getTextContent());
			setPortDBMS(document.getElementsByTagName("portDBMS").item(0).getTextContent());
			bool = true;
		}
		
		return bool;
	}

	public static String getAddressDBMS() {
		return addressDBMS;
	}

	public static void setAddressDBMS(String addressDBMS) {
		LocalConfigurationParameters.addressDBMS = addressDBMS;
	}

	public static String getPortDBMS() {
		return portDBMS;
	}

	public static void setPortDBMS(String portDBMS) {
		LocalConfigurationParameters.portDBMS = portDBMS;
	}
}
