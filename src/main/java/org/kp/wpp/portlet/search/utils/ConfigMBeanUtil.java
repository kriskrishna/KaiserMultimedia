package org.kp.wpp.portlet.search.utils;

import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.common.property.props.ResourceProperties;

public class ConfigMBeanUtil {
    private static final Log LOG = LogFactory.getLog(ConfigMBeanUtil.class);
	/**
	 * @param propertyName
	 * @return
	 */
	public String getProperty(String propertyName) {
		String reeValue = ResourceProperties.getProperty(propertyName);
		if(LOG.isDebugEnabled())LOG.debug("REE Property:" + propertyName+", Value="+reeValue);
        return reeValue;
	}
}
