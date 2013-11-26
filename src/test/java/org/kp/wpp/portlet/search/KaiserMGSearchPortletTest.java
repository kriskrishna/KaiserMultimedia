package org.kp.wpp.portlet.search;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Before;
import org.junit.Test;
import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.helpers.SearchPortletHelper;
import org.kp.wpp.portlet.search.utils.ConfigMBeanUtil;


public class KaiserMGSearchPortletTest {

	@Mocked RenderRequest renderRequest;
	@Mocked RenderResponse renderResponse;
	@Mocked SearchPortletHelper searchPortletHelper;
	@Mocked ConfigMBeanUtil configMBeanUtil;
	@Mocked PortletSession portletSession;
	@Mocked PortletPreferences pPref;
	@Mocked GenericPortlet genericPortlet;
	@Mocked PortletContext portletContext;
	@Mocked PortletRequestDispatcher requestDispatcher;
	@Mocked LogFactory logFactory;
	@Mocked Log log;
	
	private KaiserMGSearchPortlet mgSearchPortlet;
	
	@Before
	public void before() {
		mgSearchPortlet = new KaiserMGSearchPortlet();
		
		Deencapsulation.setField(mgSearchPortlet, "LOGGER", log);
	}
	
	private void commonNonStrictExpectations() {
		new NonStrictExpectations() {
			{
				renderRequest.getPortletSession();
				result = portletSession;
				
				portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SEARCH_URL);
				result = "asdf";
				
				renderRequest.getPreferences();
				result = pPref;
				
				genericPortlet.getPortletContext();
				result = portletContext;
				
				portletContext.getRequestDispatcher(anyString);
				result = requestDispatcher;
			}
		};
	}
	
	@Test
	public void showSearchFields() throws Exception {
		showSearchFields(IPortletConstants.REGIONCD_NORTHERN_CALIFORNIA);
		showSearchFields(IPortletConstants.REGIONCD_SOUTHERN_CALIFORNIA);
		showSearchFields(IPortletConstants.REGIONCD_COLORADO_DENVER_BOULDER);
		showSearchFields(IPortletConstants.REGIONCD_COLORADO_NORTHERN);
		showSearchFields(IPortletConstants.REGIONCD_COLORADO_SOUTHERN);
		showSearchFields(IPortletConstants.REGIONCD_GEORGIA);
		showSearchFields(IPortletConstants.REGIONCD_HAWAII);
		showSearchFields(IPortletConstants.REGIONCD_OHIO);
		showSearchFields(IPortletConstants.REGIONCD_OREGON_WASHINGTON);
	}
	
	private void showSearchFields(String region) throws Exception {
		showContentFragmentForDoctorSearch(region, null, Boolean.FALSE);
	}
	
	@Test
	public void showContentFragmentForMASDoctorSearch() throws Exception {
		showContentFragmentForDoctorSearch(IPortletConstants.REGIONCD_MARYLAND_VIRGINIA_DC, IPortletConstants.REGIONCD_MARYLAND_VIRGINIA_DC, Boolean.TRUE);
	}
	
	private void showContentFragmentForDoctorSearch(final String region, final String expectedContentRegion, final Boolean showContentFragmentInsteadOfSearchFields) throws Exception {
		
		commonNonStrictExpectations();
		
		new NonStrictExpectations() {
			{
				renderRequest.getParameter(IPortletConstants.SEARCH_DOC_FILTER);
				result = region;
				
				renderRequest.getParameter(IPortletConstants.SEARCH_MGSELECTION);
				result = IPortletConstants.MGSELECTION_DOCTORS;
			}
		};
		
		mgSearchPortlet.doView(renderRequest, renderResponse);
		
		new Verifications() {
			{
				portletSession.setAttribute(IPortletConstants.SESSION_PARAM_MID_EXCEPTION, showContentFragmentInsteadOfSearchFields);
				
				if (showContentFragmentInsteadOfSearchFields) {
					portletSession.setAttribute(IPortletConstants.SEARCH_CONTENTREGION, expectedContentRegion);
				}
			}
		};
	}
}