package org.kp.wpp.portlet.search.beans;

import java.io.Serializable;

public class Distance implements Serializable{

	private static final long serialVersionUID = 1L;
	private String distValue;
	private String distName;

	/**
	 * @return the distValue
	 */
	public String getdistValue() {
		return distValue;
	}

	/**
	 * @param distValue the distValue to set
	 */
	public void setdistValue(final String distValue) {
		this.distValue = distValue;
	}

	/**
	 * @return the distName
	 */
	public String getdistName() {
		return distName;
	}

	/**
	 * @param distName the distName to set
	 */
	public void setdistName(final String distName) {
		this.distName = distName;
	}
}