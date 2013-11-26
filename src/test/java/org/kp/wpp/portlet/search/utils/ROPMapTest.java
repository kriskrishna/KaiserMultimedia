package org.kp.wpp.portlet.search.utils;

import java.util.List;

import junit.framework.TestCase;

import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.beans.Region;
import org.kp.wpp.portlet.search.constants.IPortletConstants;

public class ROPMapTest extends TestCase {
	
	public final static Log LOGGER = LogFactory.getLog(ROPMapTest.class);

	public void testgetRegionNameForTest()
	{
		try
		{
			String regionRefinement = ROPMap.updateKeywordForROP("asthma","CCO");
			assertEquals("asthma"+"&binning-state=region_label==Colorado+-+Denver+%2F+Boulder", regionRefinement);
			regionRefinement = ROPMap.updateKeywordForROP("asthma","SCA");
			assertEquals("asthma"+"&binning-state=region_label==California+-+Southern", regionRefinement);
			regionRefinement = ROPMap.updateKeywordForROP("asthma","SCO");
			assertEquals("asthma"+"&binning-state=region_label==Colorado+-+Southern+Colorado", regionRefinement);
			regionRefinement = ROPMap.updateKeywordForROP("asthma","SGA");
			assertEquals("asthma"+"&binning-state=region_label==Georgia", regionRefinement);
			regionRefinement = ROPMap.updateKeywordForROP("asthma","WA");
			assertEquals("asthma"+"&binning-state=region_label==Oregon+%2F+Washington", regionRefinement);
			regionRefinement = ROPMap.updateKeywordForROP("asthma","");
			assertEquals("asthma", regionRefinement);
			regionRefinement = ROPMap.updateKeywordForROP("asthma",null);
			assertEquals("asthma", regionRefinement);
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	public void testGetRegionList() {
		List<Region> lstRegion = ROPMap.getRegionList();
		assertEquals(10, lstRegion.size());

		for (Region region : lstRegion) {
			if (IPortletConstants.REGIONCD_NORTHERN_CALIFORNIA.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_NORTHERN_CALIFORNIA, region.getRegionNm());
			} else if (IPortletConstants.REGIONCD_SOUTHERN_CALIFORNIA.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_SOUTHERN_CALIFORNIA, region.getRegionNm());
			} else if (IPortletConstants.REGIONCD_COLORADO_NORTHERN.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_COLORADO_NORTHERN, region.getRegionNm());
			} else if (IPortletConstants.REGIONCD_COLORADO_DENVER_BOULDER.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_COLORADO_DENVER_BOULDER, region.getRegionNm());
			} else if (IPortletConstants.REGIONCD_COLORADO_SOUTHERN.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_COLORADO_SOUTHERN, region.getRegionNm());
			} else if (IPortletConstants.REGIONCD_GEORGIA.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_GEORGIA, region.getRegionNm());
			} else if (IPortletConstants.REGIONCD_HAWAII.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_HAWAII, region.getRegionNm());
			} else if (IPortletConstants.REGIONCD_MARYLAND_VIRGINIA_DC.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_MARYLAND_VIRGINIA_DC, region.getRegionNm());
			} else if (IPortletConstants.REGIONCD_OHIO.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_OHIO, region.getRegionNm());
			} else if (IPortletConstants.REGIONCD_OREGON_WASHINGTON.equals(region.getRegionCode())) {
				assertEquals(IPortletConstants.REGION_OREGON_WASHINGTON, region.getRegionNm());
			}
		}
	}

	public void testFetchRegionFilterFromCookie()
	{
		String rop = ROPMap.fetchRegionFilterFromCookie("NCA");
		rop = ROPMap.fetchRegionFilterFromCookie("HAW");
		rop = ROPMap.fetchRegionFilterFromCookie("HOHOHO");
	}
	
	public void testGetRegionQueryList()
	{
		List<Region> lstRegion = ROPMap.getRegionQueryList("NCA");
		for(int i =0; i<lstRegion.size(); i++){
			Region r = lstRegion.get(i);
			if(r.getRegionNm().equals(IPortletConstants.REGION_NORTHERN_CALIFORNIA)){
				assertTrue(r.isSelected());
			}
			else
				assertFalse(r.isSelected());
		}

		lstRegion = ROPMap.getRegionQueryList("HAW");
		for(int i =0; i<lstRegion.size(); i++){
			Region r = lstRegion.get(i);
			if(r.getRegionNm().equals(IPortletConstants.REGION_HAWAII)){
				assertTrue(r.isSelected());
			}
			else
				assertFalse(r.isSelected());
		}

		lstRegion = ROPMap.getRegionQueryList("HOHOHO");
		for(int i =0; i<lstRegion.size(); i++){
			Region r = lstRegion.get(i);
			assertFalse(r.isSelected());
		}
	}
	
	public void testUpdateRegionCdForLegacyURLs()
	{
		assertEquals("SCA", ROPMap.updateRegionCodeForLegacyURLs("SCA"));
		assertEquals("MRN", ROPMap.updateRegionCodeForLegacyURLs("NCA"));
		assertEquals(IPortletConstants.REGIONCD_COLORADO_DENVER_BOULDER, ROPMap.updateRegionCodeForLegacyURLs(IPortletConstants.REGIONCD_COLORADO_NORTHERN));
	}
	
	public void testUpdateRegionCdForContentAPIs()
	{
		assertEquals("OH", ROPMap.updateRegionCodeForContentAPIs("OHI"));
		assertEquals("GA", ROPMap.updateRegionCodeForContentAPIs("GGA"));
		assertEquals("HI", ROPMap.updateRegionCodeForContentAPIs("HAW"));
		assertEquals("NW", ROPMap.updateRegionCodeForContentAPIs("KNW"));
		assertEquals("SCO", ROPMap.updateRegionCodeForContentAPIs("CS"));
		assertEquals("CCO", ROPMap.updateRegionCodeForContentAPIs("DB"));
		assertEquals("CCO", ROPMap.updateRegionCodeForContentAPIs("NC"));
		assertEquals("NCA", ROPMap.updateRegionCodeForContentAPIs("NCA"));
	}
}
