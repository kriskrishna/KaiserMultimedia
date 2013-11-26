package org.kp.wpp.portlet.search;
import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class to create a parser and parse an XML file
 */
public class CreateTestParser {
	
	private static final Log LOGGER = LogFactory.getLog(CreateTestParser.class);

	private DefaultHandler handler;
	private SAXParser saxParser;
	
	public CreateTestParser(DefaultHandler handler) {
		this.handler = handler;
		create();
	}

	private void create() {
		try {
			// Obtain a new instance of a SAXParserFactory.
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// Specifies that the parser produced by this code will provide support for XML namespaces.
			factory.setNamespaceAware(true);
			// Specifies that the parser produced by this code will validate documents as they are parsed.
			factory.setValidating(true);
			// Creates a new instance of a SAXParser using the currently configured factory parameters.
			saxParser = factory.newSAXParser();
		} catch (Throwable t) {
	        LOGGER.debug("Exception while trying to parse:"+t.getMessage());
			t.printStackTrace();
		}
	}
	
	public void parse(File file){
		try{
			saxParser.parse(file,handler);
		} catch (Throwable t) {
			LOGGER.debug("Exception while trying to parse:"+t.getMessage());
			t.printStackTrace();
		}
	}

	/**
	 * Parse a URI
	 * @param uri - String
	 */
	public void parse(String uri){
		try{
			saxParser.parse(uri,handler);
		} catch (Throwable t) {
			LOGGER.debug("Exception while trying to parse:"+t.getMessage());
			t.printStackTrace();
		}
	}

	/**
	 * Parse a Stream
	 * @param stream - InputStream
	 */
	public void parse(InputStream stream){
		try{
			saxParser.parse(stream,handler);
		} catch (Throwable t) {
			LOGGER.debug("Exception while trying to parse:"+t.getMessage());
			t.printStackTrace();
		}
	}
}