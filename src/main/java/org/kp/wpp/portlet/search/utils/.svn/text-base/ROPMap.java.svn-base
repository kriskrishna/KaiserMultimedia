package org.kp.wpp.portlet.search.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kp.wpp.portlet.search.beans.Region;
import org.kp.wpp.portlet.search.constants.IPortletConstants;

/**
 * This class stores the list of regions and the corresponding ROP cookie value.
 * The map returns the region as a search refinement when the ROP cookie value
 * is passed in as key.
 * 
 * Note: Needs to be updated if there is a new Region added to the ROP cookie
 * list or in the indexing server.
 */

public class ROPMap {

	private static HashMap<String, String> ropCGIQueryMap = new HashMap<String, String>();

	private static HashMap<String, String> ropMap = new HashMap<String, String>();

	private static List<Region> ropMGSelectList = new ArrayList<Region>();

	static {
		ropMap.put("MRN", IPortletConstants.REGION_NORTHERN_CALIFORNIA);
		ropMap.put("NCA", IPortletConstants.REGION_NORTHERN_CALIFORNIA);
		ropMap.put("SCA", IPortletConstants.REGION_SOUTHERN_CALIFORNIA);
		ropMap.put("CS", IPortletConstants.REGION_COLORADO_SOUTHERN);
		ropMap.put("SCO", IPortletConstants.REGION_COLORADO_SOUTHERN);
		ropMap.put("CCO", IPortletConstants.REGION_COLORADO_DENVER_BOULDER);
		ropMap.put("DB", IPortletConstants.REGION_COLORADO_DENVER_BOULDER);
		ropMap.put("OR", IPortletConstants.REGION_OREGON_WASHINGTON);
		ropMap.put("WA", IPortletConstants.REGION_OREGON_WASHINGTON);
		ropMap.put("NW", IPortletConstants.REGION_OREGON_WASHINGTON);
		ropMap.put("HAW", IPortletConstants.REGION_HAWAII);
		ropMap.put("HI", IPortletConstants.REGION_HAWAII);
		ropMap.put("OH", IPortletConstants.REGION_OHIO);
		ropMap.put("EOH", IPortletConstants.REGION_OHIO);
		ropMap.put("OHI", IPortletConstants.REGION_OHIO);
		ropMap.put("GGA", IPortletConstants.REGION_GEORGIA);
		ropMap.put("SGA", IPortletConstants.REGION_GEORGIA);
		ropMap.put("GA", IPortletConstants.REGION_GEORGIA);
		ropMap.put("KNW", IPortletConstants.REGION_OREGON_WASHINGTON);
		ropMap.put("MID", IPortletConstants.REGION_MARYLAND_VIRGINIA_DC);
		ropMap.put("NC", IPortletConstants.REGION_COLORADO_NORTHERN);

		// ropQueryMap holds Region mapping for querying Region as a CGI param
		Region r = new Region();
		r.setRegionNm(IPortletConstants.REGION_NORTHERN_CALIFORNIA);
		r.setRegionCode(IPortletConstants.REGIONCD_NORTHERN_CALIFORNIA);
		ropMGSelectList.add(r);
		r = new Region();
		r.setRegionNm(IPortletConstants.REGION_SOUTHERN_CALIFORNIA);
		r.setRegionCode(IPortletConstants.REGIONCD_SOUTHERN_CALIFORNIA);
		ropMGSelectList.add(r);
		r = new Region();
		r.setRegionNm(IPortletConstants.REGION_COLORADO_DENVER_BOULDER);
		r.setRegionCode(IPortletConstants.REGIONCD_COLORADO_DENVER_BOULDER);
		ropMGSelectList.add(r);		
		r = new Region();
		r.setRegionNm(IPortletConstants.REGION_COLORADO_NORTHERN);
		r.setRegionCode(IPortletConstants.REGIONCD_COLORADO_NORTHERN);
		ropMGSelectList.add(r);		
		r = new Region();
		r.setRegionNm(IPortletConstants.REGION_COLORADO_SOUTHERN);
		r.setRegionCode(IPortletConstants.REGIONCD_COLORADO_SOUTHERN);
		ropMGSelectList.add(r);
		r = new Region();
		r.setRegionNm(IPortletConstants.REGION_GEORGIA);
		r.setRegionCode(IPortletConstants.REGIONCD_GEORGIA);
		ropMGSelectList.add(r);
		r = new Region();
		r.setRegionNm(IPortletConstants.REGION_HAWAII);
		r.setRegionCode(IPortletConstants.REGIONCD_HAWAII);
		ropMGSelectList.add(r);
		r = new Region();
		r.setRegionNm(IPortletConstants.REGION_MARYLAND_VIRGINIA_DC);
		r.setRegionCode(IPortletConstants.REGIONCD_MARYLAND_VIRGINIA_DC);
		ropMGSelectList.add(r);
		r = new Region();
		r.setRegionNm(IPortletConstants.REGION_OHIO);
		r.setRegionCode(IPortletConstants.REGIONCD_OHIO);
		ropMGSelectList.add(r);
		r = new Region();
		r.setRegionNm(IPortletConstants.REGION_OREGON_WASHINGTON);
		r.setRegionCode(IPortletConstants.REGIONCD_OREGON_WASHINGTON);
		ropMGSelectList.add(r);

		for (Region reg : ropMGSelectList) {
			ropCGIQueryMap.put(reg.getRegionNm(), reg.getRegionCode());
		}
	}

	public static String getRegionNameForCookie(String ropCookie) {
		String region = ropMap.get(ropCookie);
		if (StringUtils.isBlank(region)) {
			return null;
		} else {
			return region;
		}
	}

	public static String updateKeywordForROP(String sKeyword, String ropCookie) {
		String rop = getRegionNameForCookie(ropCookie);
		String ropKwd = sKeyword;
		if (StringUtils.isNotBlank(rop)) {
			ropKwd = sKeyword + IPortletConstants.REGION_REFINEMENT_KEY
					+ FormatUtil.urlEncode(rop);
		}
		return ropKwd;
	}

	public static List<Region> getRegionList() {
		return ropMGSelectList;
	}

	public static String fetchRegionFilterFromCookie(String ropCookie) {
		String rop = getRegionNameForCookie(ropCookie);
		if (StringUtils.isNotBlank(rop)) {
			return ropCGIQueryMap.get(rop);
		}
		return null;
	}

	/**
	 * Takes in the ropCookie as input, returns the List of Regions that will
	 * populate the region filter list. The regionCode will be sent in as the
	 * region filter CGI param for Search.
	 * 
	 * @return List ropList: List of regions
	 */
	public static List<Region> getRegionQueryList(String ropCookie) {
		String ropRegionName = getRegionNameForCookie(ropCookie);
		List<Region> ropList = new ArrayList<Region>();
		Set<String> set = ropCGIQueryMap.keySet();
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			String regionNm = iter.next();
			Region r = new Region();
			r.setRegionNm(regionNm);
			r.setRegionCode(ropCGIQueryMap.get(r.getRegionNm()));
			if (StringUtils.equalsIgnoreCase(ropRegionName, regionNm)) {
				r.setSelected(true);
			}
			ropList.add(r);
		}
		return ropList;
	}

	public static String updateRegionCodeForLegacyURLs(String regionCd) {
		String legacyRgn = regionCd;
		if (StringUtils.equalsIgnoreCase(regionCd, "NCA")) {
			legacyRgn = "MRN";
		} else if (StringUtils.equalsIgnoreCase(regionCd, IPortletConstants.REGIONCD_COLORADO_NORTHERN)) {
			legacyRgn = IPortletConstants.REGIONCD_COLORADO_DENVER_BOULDER;
		}
		return legacyRgn;
	}

	public static String updateRegionCodeForContentAPIs(String regionCd) {
		if (StringUtils.equalsIgnoreCase(regionCd, "OHI")) {
			return "OH";
		}
		if (StringUtils.equalsIgnoreCase(regionCd, "GGA")) {
			return "GA";
		}
		if (StringUtils.equalsIgnoreCase(regionCd, "HAW")) {
			return "HI";
		}
		if (StringUtils.equalsIgnoreCase(regionCd, "KNW")) {
			return "NW";
		}
		if (StringUtils.equalsIgnoreCase(regionCd, "CS")) {
			return "SCO";
		}
		if (StringUtils.equalsIgnoreCase(regionCd, "DB") || StringUtils.equalsIgnoreCase(regionCd, IPortletConstants.REGIONCD_COLORADO_NORTHERN)) {
			return "CCO";
		} else {
			return regionCd;
		}
	}
}
