package org.kp.wpp.portlet.search.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.ValidatorException;
import javax.portlet.WindowState;
import javax.servlet.http.Cookie;

import junit.framework.TestCase;

import org.junit.Assert;
import org.kp.wpp.content.engine.IContentEngine;
import org.kp.wpp.portlet.search.beans.Content;
import org.kp.wpp.portlet.search.beans.FacetGroup;
import org.kp.wpp.portlet.search.beans.PageLink;
import org.kp.wpp.portlet.search.beans.SearchCriteria;
import org.kp.wpp.portlet.search.beans.SearchResultsView;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.utils.search.service.VivisimoXMLService;
import org.kp.wpp.utils.search.service.objects.Document;
import org.kp.wpp.utils.search.service.objects.KaiserSearchRequest;
import org.kp.wpp.utils.search.service.objects.KaiserSearchResponse;

public class SearchPortletHelperTest extends TestCase {
	
	/**
	 * Test for not NULL rop value
	 */
	public void testFetchROP1() {
		FakeRenderRequest request = new FakeRenderRequest();
		String[] values = {"CCO"};
		request.loadPortletPreferences(IPortletConstants.PREF_SEARCHREGION, values);
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		String rop = helper.fetchROP();
		Assert.assertNotNull(rop);
	}
	
	/**
	 * Test for NULL rop value
	 */
/*	public void testFetchROP2() {
		FakeRenderRequest request = new FakeRenderRequest();
		String[] values = {null};
		request.loadPortletPreferences("DYMMY_PREFERNCE", values);
		request.loadCookies();
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		String rop = helper.fetchROP();
		Assert.assertNotNull(rop);
	}*/
	
	public void testUpdateForRegionOfPreference1() {
		FakeRenderRequest request = new FakeRenderRequest();
		String[] values = {"CCO"};
		request.loadPortletPreferences(IPortletConstants.PREF_SEARCHREGION, values);
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		String updatedKeyword = helper.updateForRegionOfPreference("medical");
		Assert.assertNotNull(updatedKeyword);
	}
	
	public void testPersistSearchPreferences() {
		FakeActionRequest request = new FakeActionRequest();
		this.loadActionParams(request);
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		helper.persistSearchPreferences();
		PortletPreferences pPref = request.getPreferences();
		String checkvalue = pPref.getValue(IPortletConstants.PREF_SEARCHPROJECT, "");
		assertEquals("fldsearchproject", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_SEARCHSOURCES, "");
		assertEquals("fldsearchsources", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_SEARCHCUSTOMDISPLAY, "");
		assertEquals("fldsearchcustomdisplay", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_SEARCHTOTALRESULTS, "");
		assertEquals("fldsearchtotalresults", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_SEARCHRESULTSPERPAGE, "");
		assertEquals("fldsearchresultsperpage", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_SEARCHTIMEOUT, "");
		assertEquals("fldsearchtimeout", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_SEARCHREGION, "");
		assertEquals("fldsearchregion", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_NCA_PLAN_PROVIDER_TYPE, "");
		assertEquals("NCA", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_SCA_PLAN_PROVIDER_TYPE, "");
		assertEquals("SCA", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_DB_PLAN_PROVIDER_TYPE, "");
		assertEquals("DB", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_NC_PLAN_PROVIDER_TYPE, "");
		assertEquals("NC", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_CS_PLAN_PROVIDER_TYPE, "");
		assertEquals("CS", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_GGA_PLAN_PROVIDER_TYPE, "");
		assertEquals("GGA", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_HAW_PLAN_PROVIDER_TYPE, "");
		assertEquals("HAW", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_MID_PLAN_PROVIDER_TYPE, "");
		assertEquals("MID", checkvalue);
		checkvalue = pPref.getValue(IPortletConstants.PREF_OHI_PLAN_PROVIDER_TYPE, "");
		assertEquals("OHI", checkvalue);
	}
	
	public void testFetchSearchResults1() {
		FakeRenderRequest request = new FakeRenderRequest();
		KaiserSearchRequest ksReq = new KaiserSearchRequest();
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		helper.fetchSearchResults(ksReq);
		Assert.assertNotNull(request.getAttribute("SEARCH_GENERIC_VIEW_BEAN"));
	}

	
	private boolean isSummaryDisplayFlag = false;
	private boolean parentLetter = false;
	private boolean exception = false;
	private boolean searchSessionIdentifier = false;
	private boolean setSearchResults = false;
	
	public void testFetchSearchResults2() {
		searchSessionIdentifier = true;
		FakeRenderRequest request = new FakeRenderRequest();
		KaiserSearchRequest ksReq = new KaiserSearchRequest();
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		helper.fetchSearchResults(ksReq);
		Assert.assertNotNull(request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_COOKIE_IDENTIFIER));
		Assert.assertNotNull(request.getAttribute(IPortletConstants.REQ_RESULTERROR));
	}
	
	public void testFetchSearchResults3() {
		isSummaryDisplayFlag = true;
		FakeRenderRequest request = new FakeRenderRequest();
		KaiserSearchRequest ksReq = new KaiserSearchRequest();
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		helper.fetchSearchResults(ksReq);
		Assert.assertNotNull(request.getAttribute("SEARCH_GENERIC_VIEW_BEAN"));
	}
	
	public void testFetchSearchResults4() {
		parentLetter = true;
		FakeRenderRequest request = new FakeRenderRequest();
		KaiserSearchRequest ksReq = new KaiserSearchRequest();
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		helper.fetchSearchResults(ksReq);
		Object obj = request.getAttribute("SEARCH_GENERIC_VIEW_BEAN");
		Assert.assertNotNull(request.getAttribute("SEARCH_GENERIC_VIEW_BEAN"));
		Assert.assertNotNull(request.getAttribute(IPortletConstants.REQ_RESULTERROR));
		SearchCriteria criteria = (SearchCriteria) request.getAttribute("SEARCH_GENERIC_VIEW_BEAN");
		Assert.assertNull(criteria.getKeyword());
	}
	
	public void testFetchSearchResults5() {
		exception = true;
		FakeRenderRequest request = new FakeRenderRequest();
		KaiserSearchRequest ksReq = new KaiserSearchRequest();
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		helper.fetchSearchResults(ksReq);
		Assert.assertNotNull(request.getAttribute(IPortletConstants.REQ_RESULTERROR));
	}
	
	public void testFetchSearchResults6() {
		setSearchResults = true;
		FakeRenderRequest request = new FakeRenderRequest();
		request.setParameter(IPortletConstants.SEARCH_RESULTS_LABEL, "somevalue");
		request.setParameter(IPortletConstants.ACTION_SUBMITQUERY, IPortletConstants.VAL_SUBMITQUERY_HCO_LINKS);
	
		KaiserSearchRequest ksReq = new KaiserSearchRequest();
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		helper.fetchSearchResults(ksReq);
		Assert.assertNotNull(request.getAttribute("SEARCH_GENERIC_VIEW_BEAN"));
		Assert.assertNotNull(request.getAttribute("FACETS_LIST"));
		Assert.assertNotNull(request.getAttribute("PAGINATION_LIST"));
		Assert.assertNotNull(request.getAttribute("BREADCRUMBS_LIST"));
		Assert.assertNotNull(request.getAttribute("RESULTS_LIST"));
	}
	
	public void testFetchSearchResults7() {
		setSearchResults = true;
		FakeRenderRequest request = new FakeRenderRequest();
		request.setParameter(IPortletConstants.ACTION_SUBMITQUERY, IPortletConstants.VAL_SUBMITQUERY_HCO_LINKS);
	
		KaiserSearchRequest ksReq = new KaiserSearchRequest();
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		helper.fetchSearchResults(ksReq);
		Assert.assertNotNull(request.getAttribute("SEARCH_GENERIC_VIEW_BEAN"));
		Assert.assertNotNull(request.getAttribute("FACETS_LIST"));
		Assert.assertNotNull(request.getAttribute("PAGINATION_LIST"));
		Assert.assertNotNull(request.getAttribute("BREADCRUMBS_LIST"));
		Assert.assertNotNull(request.getAttribute("RESULTS_LIST"));
	}

	public void testFetchSearchResults8() {
		setSearchResults = true;
		FakeRenderRequest request = new FakeRenderRequest();
		request.setParameter(IPortletConstants.ACTION_SUBMITQUERY, "somevalue");
	
		KaiserSearchRequest ksReq = new KaiserSearchRequest();
		FakeSearchPortletHelper helper = new FakeSearchPortletHelper(request);
		helper.fetchSearchResults(ksReq);
		Assert.assertNotNull(request.getAttribute("SEARCH_GENERIC_VIEW_BEAN"));
		Assert.assertNotNull(request.getAttribute("FACETS_LIST"));
		Assert.assertNotNull(request.getAttribute("PAGINATION_LIST"));
		Assert.assertNotNull(request.getAttribute("BREADCRUMBS_LIST"));
		Assert.assertNotNull(request.getAttribute("RESULTS_LIST"));
	}
	
	private void loadActionParams(FakeActionRequest request) {
		String[] value = new String[]{"fldsearchproject"};
		request.loadParams(IPortletConstants.FIELD_SEARCHPROJECT, value);
		value = new String[]{"fldsearchsources"};
		request.loadParams(IPortletConstants.FIELD_SEARCHSOURCES, value);
		value = new String[]{"fldsearchcustomdisplay"};
		request.loadParams(IPortletConstants.FIELD_SEARCHCUSTOMDISPLAY, value);
		value = new String[]{"fldsearchtotalresults"};
		request.loadParams(IPortletConstants.FIELD_SEARCHTOTALRESULTS, value);
		value = new String[]{"fldsearchresultsperpage"};
		request.loadParams(IPortletConstants.FIELD_SEARCHRESULTSPERPAGE, value);
		value = new String[]{"fldsearchtimeout"};
		request.loadParams(IPortletConstants.FIELD_SEARCHTIMEOUT, value);
		value = new String[]{"fldsearchregion"};
		request.loadParams(IPortletConstants.FIELD_SEARCHREGION, value);
		value = new String[]{"NCA"};
		request.loadParams(IPortletConstants.FIELD_NCA, value);
		value = new String[]{"SCA"};
		request.loadParams(IPortletConstants.FIELD_SCA, value);
		value = new String[]{"DB"};
		request.loadParams(IPortletConstants.FIELD_DB, value);
		value = new String[]{"NC"};
		request.loadParams(IPortletConstants.FIELD_NC, value);
		value = new String[]{"CS"};
		request.loadParams(IPortletConstants.FIELD_CS, value);
		value = new String[]{"GGA"};
		request.loadParams(IPortletConstants.FIELD_GGA, value);
		value = new String[]{"HAW"};
		request.loadParams(IPortletConstants.FIELD_HAW, value);
		value = new String[]{"MID"};
		request.loadParams(IPortletConstants.FIELD_MID, value);
		value = new String[]{"OHI"};
		request.loadParams(IPortletConstants.FIELD_OHI, value);
		value = new String[]{"KNW"};
		request.loadParams(IPortletConstants.FIELD_KNW, value);
	}
	
	class FakeSearchPortletHelper extends SearchPortletHelper {

		public FakeSearchPortletHelper(FakeRenderRequest request) {
			super(request);
		}
		
		public FakeSearchPortletHelper(FakeActionRequest request) {
			super(request);
		}

		protected IContentEngine getEngine(PortletRequest pReq) {
			return null;
		}
		
		protected VivisimoXMLService getVivisimoXMLService() {
			return new FakeVivisimoXMLService();
		}
		
		protected SearchHelper getSearchHelper() {
			return new FakeSearchHelper();
		}

	}
	
	class FakeVivisimoXMLService extends VivisimoXMLService {
		public KaiserSearchResponse search(KaiserSearchRequest ksReq) throws Exception{
			if(exception) {
				throw new Exception("Test for exception during search....");
			}
			KaiserSearchResponse response = new KaiserSearchResponse();
			if(searchSessionIdentifier) {
				response.setSearchSessionIdentifier("sessonidentifier");
			}
			response.setLstDocuments(new ArrayList<Document>()); //???
			response.setContentsPerPage("1"); //??
			response.setCurPageStartIndex("1"); //???
			response.setNav(null);
			return response;
		}
	}
	
	class FakeSearchHelper extends SearchHelper {
		public SearchResultsView processSearchResults(KaiserSearchResponse ksResp, PortletPreferences pPref){
			SearchResultsView mgSearchView = new SearchResultsView();
			SearchCriteria criteria = new SearchCriteria();
			if(isSummaryDisplayFlag) {
				criteria.setSummaryDisplayFlag(true);
			}
			if(parentLetter) {
				criteria.setKeyword(IPortletConstants.PREFIX_QUERY_PARENT_LETTER + "Dr Geranimo");
			}
			else {
				criteria.setKeyword("Dr Geranimo");
			}
			mgSearchView.setSearchCriteria(criteria);
			if(setSearchResults) {
				mgSearchView.setContents(new ArrayList<Content>());
				mgSearchView.setNavigationLinks(new ArrayList<PageLink>());
				mgSearchView.setSelectableFacetGroups(new ArrayList<FacetGroup>());
				mgSearchView.setSelectedFacetGroups(new ArrayList<FacetGroup>());
			}
			return mgSearchView;
		}
	}
	
	class FakeRenderRequest implements RenderRequest {
		private PortletSession session = new FakePortletSession();
		private Map<String, String> parameters = new HashMap<String, String>();
		private Map<String, Object> attributes = new HashMap<String, Object>();
		private FakePortletPreferences preferences = new FakePortletPreferences();
		private Cookie[] kookies = new Cookie[3];
		public void loadPortletPreferences(String key, String[] value) {
			preferences.load(key, value);
		}
		public void loadCookies() {
			//Cookie 1
			Cookie cookie = new Cookie("dummy1", "dummy1");
			kookies[0] = cookie;
			cookie = new Cookie(IPortletConstants.IMPREGIONCOOKIE, "CCO");
			kookies[1] = cookie;
			cookie = new Cookie("dummy2", "dummy2");
			kookies[2] = cookie;
		}
		public void loadCookie(String name, String value) {
			int nCounter = 0;
			for (Cookie cookie : kookies) {
				if(cookie == null) {
					cookie = new Cookie(name, value);
					kookies[nCounter] = cookie;
					break;
				}
			}
				
		}

		public String getETag() {
			// TODO Auto-generated method stub
			return null;
		}

		public Object getAttribute(String arg0) {
			if(attributes.containsKey(arg0)) {
				return attributes.get(arg0);
			}
			return null;
		}

		public Enumeration<String> getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getAuthType() {
			// TODO Auto-generated method stub
			return null;
		}

		public Locale getLocale() {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<Locale> getLocales() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getParameter(String arg0) {
			if(parameters.containsKey(arg0)) {
				return parameters.get(arg0);
			}
			return null;
		}

		public Map<String, String[]> getParameterMap() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setParameter(String param, String value) {
			parameters.put(param, value);
		}
		
		public Enumeration<String> getParameterNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public String[] getParameterValues(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public PortalContext getPortalContext() {
			// TODO Auto-generated method stub
			return null;
		}

		public PortletMode getPortletMode() {
			// TODO Auto-generated method stub
			return null;
		}

		public PortletSession getPortletSession() {
			return this.session;
		}

		public PortletSession getPortletSession(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public PortletPreferences getPreferences() {
			return preferences;
		}

		public Map<String, String[]> getPrivateParameterMap() {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getProperties(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getProperty(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getPropertyNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public Map<String, String[]> getPublicParameterMap() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getRemoteUser() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getRequestedSessionId() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getResponseContentType() {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getResponseContentTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getScheme() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getServerName() {
			// TODO Auto-generated method stub
			return null;
		}

		public int getServerPort() {
			// TODO Auto-generated method stub
			return 0;
		}

		public Principal getUserPrincipal() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getWindowID() {
			// TODO Auto-generated method stub
			return null;
		}

		public WindowState getWindowState() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isPortletModeAllowed(PortletMode arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isRequestedSessionIdValid() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isSecure() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isUserInRole(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isWindowStateAllowed(WindowState arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public void removeAttribute(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void setAttribute(String arg0, Object arg1) {
			this.attributes.put(arg0, arg1);
			
		}
		public String getContextPath() {
			// TODO Auto-generated method stub
			return null;
		}
		public Cookie[] getCookies() {
			// TODO Auto-generated method stub
			return kookies;
		}
		
	}
	
	class FakePortletPreferences implements PortletPreferences {
		private final Map<String, String[]> params = new HashMap<String, String[]>();
		
		public void load(String key, String[] value) {
			params.put(key, value);
		}

		public Map<String, String[]> getMap() {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getValue(String arg0, String arg1) {
			if(params.containsKey(arg0)) {
				return params.get(arg0)[0];
			}
			return null;
		}

		public String[] getValues(String arg0, String[] arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isReadOnly(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public void reset(String arg0) throws ReadOnlyException {
			// TODO Auto-generated method stub
			
		}

		public void setValue(String arg0, String arg1) throws ReadOnlyException {
			String[] value = {arg1};
			params.put(arg0, value);
		}

		public void setValues(String arg0, String[] arg1)
				throws ReadOnlyException {
			// TODO Auto-generated method stub
			
		}

		public void store() throws IOException, ValidatorException {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class FakeActionRequest implements ActionRequest {
		
		private FakePortletPreferences preferences = new FakePortletPreferences();
		
		private Map<String, String[]> params = null;
		
		public void loadParams(String key, String[] value) {
			if(params == null) {
				params = new HashMap<String, String[]>();
			}
			params.put(key, value);
		}

		public String getCharacterEncoding() {
			// TODO Auto-generated method stub
			return null;
		}

		public int getContentLength() {
			// TODO Auto-generated method stub
			return 0;
		}

		public String getContentType() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getMethod() {
			// TODO Auto-generated method stub
			return null;
		}

		public InputStream getPortletInputStream() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public BufferedReader getReader() throws UnsupportedEncodingException,
				IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public void setCharacterEncoding(String arg0)
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			
		}

		public Object getAttribute(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getAuthType() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getContextPath() {
			// TODO Auto-generated method stub
			return null;
		}

		public Cookie[] getCookies() {
			// TODO Auto-generated method stub
			return null;
		}

		public Locale getLocale() {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<Locale> getLocales() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getParameter(String name) {
			if(params != null) {
				if (params.get(name) != null && params.get(name).length >0) {
					return ((String[]) params.get(name))[0];
				}
			}
			return "somevalue";
		}

		public Map<String, String[]> getParameterMap() {
			// TODO Auto-generated method stub
			return params;
		}

		public Enumeration<String> getParameterNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public String[] getParameterValues(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public PortalContext getPortalContext() {
			// TODO Auto-generated method stub
			return null;
		}

		public PortletMode getPortletMode() {
			// TODO Auto-generated method stub
			return null;
		}

		public PortletSession getPortletSession() {
			// TODO Auto-generated method stub
			return null;
		}

		public PortletSession getPortletSession(boolean arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public PortletPreferences getPreferences() {
			return preferences;
		}

		public Map<String, String[]> getPrivateParameterMap() {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getProperties(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getProperty(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getPropertyNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public Map<String, String[]> getPublicParameterMap() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getRemoteUser() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getRequestedSessionId() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getResponseContentType() {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getResponseContentTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getScheme() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getServerName() {
			// TODO Auto-generated method stub
			return null;
		}

		public int getServerPort() {
			// TODO Auto-generated method stub
			return 0;
		}

		public Principal getUserPrincipal() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getWindowID() {
			// TODO Auto-generated method stub
			return null;
		}

		public WindowState getWindowState() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isPortletModeAllowed(PortletMode arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isRequestedSessionIdValid() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isSecure() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isUserInRole(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isWindowStateAllowed(WindowState arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public void removeAttribute(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void setAttribute(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class FakePortletSession implements PortletSession {
		
		private Map<String, Object> attributes = new HashMap<String, Object>();

		public Object getAttribute(String arg0) {
			if(attributes.containsKey(arg0)) {
				return attributes.get(arg0);
			}
			return null;
		}

		public Object getAttribute(String arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public Map<String, Object> getAttributeMap() {
			// TODO Auto-generated method stub
			return null;
		}

		public Map<String, Object> getAttributeMap(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public Enumeration<String> getAttributeNames(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getCreationTime() {
			// TODO Auto-generated method stub
			return 0;
		}

		public String getId() {
			// TODO Auto-generated method stub
			return null;
		}

		public long getLastAccessedTime() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getMaxInactiveInterval() {
			// TODO Auto-generated method stub
			return 0;
		}

		public PortletContext getPortletContext() {
			// TODO Auto-generated method stub
			return null;
		}

		public void invalidate() {
			// TODO Auto-generated method stub
			
		}

		public boolean isNew() {
			// TODO Auto-generated method stub
			return false;
		}

		public void removeAttribute(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void removeAttribute(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void setAttribute(String arg0, Object arg1) {
			attributes.put(arg0, arg1);
			
		}

		public void setAttribute(String arg0, Object arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		public void setMaxInactiveInterval(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
