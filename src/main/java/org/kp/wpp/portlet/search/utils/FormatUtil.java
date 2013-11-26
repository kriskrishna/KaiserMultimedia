package org.kp.wpp.portlet.search.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.constants.IPortletConstants;


public class FormatUtil {
	
	private static final Log LOGGER = LogFactory.getLog(FormatUtil.class);
	
	/**
	 * Util method to convert String to integer. Returns 0 in case, exception
	 * @param strInt - Count as a String variable
	 * @return integer 0 if invalid format, else int value of the String
	 */
	public static int convertStringtoInt(final String strInt){
		int intVal = 0;
		try{
			intVal = Integer.parseInt(strInt);
		}
		catch(NumberFormatException ne){
			LOGGER.debug("Exception while trying to convert "+strInt+" to int value");
		}
		return intVal;
	}

	/**
	 * Util method to handle Empty Title for Search Results. <br>
	 * In case of empty title, defaults value to "NO TITLE" <br>
	 * returns input value if valid title
	 * @param title The title value to be validated
	 * @return NO TITLE if empty, else title
	 */
	public static String handleEmptyTitle(final String title){
		String uTitle = trim(title);
		if(StringUtils.isBlank(uTitle)){
			uTitle = IPortletConstants.CONTENT_EMPTY_TITLE;
		}
		return uTitle;
	}
	
	/**
	 * Util method to trim strings, extends String.trim with a null check
	 * @param string string to be trimmed
	 * @return "" if empty value, else trimmed string
	 */
	public static String trim(final String string){
		String updated = "";
		if(StringUtils.isNotEmpty(string)){
			updated = string.trim();
		}
		return updated;
	}
	
	/**
	 * String util method to convert html special characters with unicode chars. <br> 
	 * To avoid/disable cross site scripting. Should be carried out on strings that are displayed back on browser.
	 * @param htmlChar - string to escape html characters
	 * @return String after converting html characters to unicode encodedHtmlCharacters
	 */
	public static String updateWithHTMLCharacters(final String htmlChar){
		return updateWithHTMLCharacters(htmlChar, false);
	}
	
	/**
	 * String util method to convert html special characters with unicode chars. <br> 
	 * To avoid/disable cross site scripting. Should be carried out on strings that are displayed back on browser.
	 * Don't convert the single/double quote if ignoreQuotes is true.
	 * @param htmlChar - string to escape html characters
	 * @return String after converting html characters to unicode encodedHtmlCharacters
	 */
	public static String updateWithHTMLCharacters(final String htmlChar, boolean ignoreQuotes){
		String encodedHtmlChar = null;
		if(StringUtils.isNotBlank(htmlChar)){
			encodedHtmlChar = htmlChar;
			if (!ignoreQuotes) {
				encodedHtmlChar = encodedHtmlChar.replaceAll("\"", "&quot;");
				encodedHtmlChar = encodedHtmlChar.replaceAll("'", "&#39;");
			}
			encodedHtmlChar = encodedHtmlChar.replaceAll("<", "&#60;");
			encodedHtmlChar = encodedHtmlChar.replaceAll(">", "&#62;");
			encodedHtmlChar = encodedHtmlChar.replaceAll("!", "&#33;");
			encodedHtmlChar = encodedHtmlChar.replaceAll("%", "&#37;");
			encodedHtmlChar = encodedHtmlChar.replaceAll("/", "&#47;");
			encodedHtmlChar = encodedHtmlChar.replaceAll("\\+", "&#43;"); 
			
		}
		return encodedHtmlChar;
	}
	
	/**
	 * Utility method to url encode(UTF-8) string values using URLEncoder.encode
	 * @param value - string from xml for affiliated facility flag   
	 * @return String UTF-8 encoded string
	 */
	public static String urlEncode(final String value){
		String encoded = value;
		try {
			encoded = URLEncoder.encode(value,"UTF-8");
		///CLOVER:OFF
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Exception while trying to encode Search Keyword for request:"+value+":"+e.getMessage());
		}
		///CLOVER:ON
		return encoded;
	}

	/**
	 * Utility method to convert Affiliated value in response xml to boolean 
	 * Test Case: <br> Input: Yes <br> Output: true
	 * @param value - string from xml for affiliated facility flag   
	 * @return boolean
	 */
	public static boolean convertToBoolean(final String value){
		boolean isValid = false;
		if(IPortletConstants.AFFILIATED_FACILITY_TRUE.equalsIgnoreCase(value)){
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * String parse method to split the bin value from the bin token. 
	 * Test Case: <br> Input: city==Colorado Springs <br> Output: Colorado Springs
	 * @param value - bin token used for filtering search results.   
	 * @return bin value parsed out from token
	 */
	public static String convertTokenToLabel(final String value){
		String label = "";
		if(StringUtils.isNotBlank(value)){
			final int indexOfLabel = value.indexOf("==");
			if(indexOfLabel >= 0){
				label = value.substring(indexOfLabel+2);
				if(StringUtils.isNotBlank(label)){
					label = label+";";
				}
			}
		}
		return label;
	}
}
