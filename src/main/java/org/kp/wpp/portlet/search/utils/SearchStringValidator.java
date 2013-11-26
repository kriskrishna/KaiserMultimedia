package org.kp.wpp.portlet.search.utils;

/**
 * @CLASS SearchStringValidator
 * @DESCRIPTION Class used to do all the validation on searchString
 * @date 09th March 2009
 * @Updated l525045: Updated class with static members, all the methods are utilities to validate the search string no need to create instances.
 * 
 */
import org.apache.commons.lang.StringUtils;
import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;

public class SearchStringValidator
{

	private final static Log LOGGER = LogFactory.getLog(SearchStringValidator.class);

    /**
     * check all the validations
     *
     * @param searchStr
     * @return
     */
    public static boolean isValidHCOSearchString(String searchStr)
    {
    	boolean isValid = false;
    	if (StringUtils.isNotBlank(searchStr)){
			if (searchStr.trim().equals("*")){
				isValid = true;
			} else {
				String pattern = ".*([a-zA-Z0-9]+).*";
				isValid = searchStr.matches(pattern);
			}
    	}
    	return isValid;
    }

    /**
     * check all the validations
     *
     * @param searchStr
     * @return
     */
    public static boolean isValidSearchString(String searchStr, String defaultKwd)
    {
    	boolean isValid = false;
    	if (StringUtils.isNotBlank(searchStr) && isValidLength(searchStr) && !isDefaultValue(searchStr,defaultKwd)){
			if (searchStr.trim().equals("*")){
				isValid = true;
			} else {
				String pattern = ".*([a-zA-Z0-9]+).*";
				isValid = searchStr.matches(pattern);
			}
    	}
    	return isValid;
    }
  
    /**
     * Method used to check the length of the String is greater then or equal to default length.
     *
     * Return true if validation pass
     *
     * @param searchstr
     * @return
     */
    private static boolean isValidLength(String searchstr)
    {
    	boolean isValid = false;
        if (searchstr.length() > 0 && searchstr.length() <= 50)
        {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Validate the for the default String ""
     */
    private static boolean isDefaultValue(String searchStr, String defaultKwd)
    {
		///CLOVER:OFF
    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Validate Default Value = "+defaultKwd);
		///CLOVER:ON
    	boolean isDflt = false;
        if (searchStr.equalsIgnoreCase(defaultKwd))
        {
        	isDflt = true;
        }
        return isDflt;
    }

    public static boolean isValidZipCode(String zipcd)
    {
    	boolean vldZip = false;
    	if (StringUtils.length(zipcd) == 5){
    		vldZip = true;
    		try{
    			Integer.parseInt(zipcd);
    		}catch(NumberFormatException nfe){
    			LOGGER.error("Invalid zip code:"+zipcd);
    			vldZip = false;
    		}
    	}
    	return vldZip;
    }
}