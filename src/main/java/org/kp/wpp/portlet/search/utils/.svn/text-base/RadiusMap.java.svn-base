package org.kp.wpp.portlet.search.utils;

import java.util.ArrayList;
import java.util.List;

import org.kp.wpp.portlet.search.beans.Distance;
import org.kp.wpp.portlet.search.constants.IPortletConstants;

/**
 * 
 * This class stores the list of distances and the corresponding radius selection value.
 * 
 **/

public class RadiusMap {

	private static List<Distance> distMGSelectList = new ArrayList<Distance>();

	static {

		Distance d = new Distance();
		d.setdistName(IPortletConstants.LOCATED_WITHIN_5_MILES);
		d.setdistValue(IPortletConstants.LOCATED_WITHIN_5_MILES_VAL);
		distMGSelectList.add(d);
		d = new Distance();
		d.setdistName(IPortletConstants.LOCATED_WITHIN_10_MILES);
		d.setdistValue(IPortletConstants.LOCATED_WITHIN_10_MILES_VAL);
		distMGSelectList.add(d);
		d = new Distance();
		d.setdistName(IPortletConstants.LOCATED_WITHIN_25_MILES);
		d.setdistValue(IPortletConstants.LOCATED_WITHIN_25_MILES_VAL);
		distMGSelectList.add(d);		
		d = new Distance();
		d.setdistName(IPortletConstants.LOCATED_WITHIN_50_MILES);
		d.setdistValue(IPortletConstants.LOCATED_WITHIN_50_MILES_VAL);
		distMGSelectList.add(d);
	}

	public static List<Distance> getRadiusList() {
		return distMGSelectList;
	}
}
