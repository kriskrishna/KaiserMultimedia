package org.kp.wpp.portlet.search;

import java.io.IOException;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;
import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.kp.security.exception.CsrfException;
import org.kp.security.util.SecurityUtil;
import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.beans.AuxiliaryContent;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.helpers.SearchPortletHelper;
import org.kp.wpp.portlet.search.utils.FormatUtil;
import org.kp.wpp.portlet.search.utils.ROPMap;
import org.kp.wpp.portlet.search.utils.SearchUtil;

public class KaiserSearchPortlet extends GenericPortlet
{
	private static final Log LOGGER = LogFactory.getLog(KaiserSearchPortlet.class);
	//private static final String BASE_URL = "https://kpvc1035.crdc.kp.org:8443/search/cgi-bin/query-meta?v%3Aproject=kp-consumernet&render.function=xml-feed-display&content-type=text/xml&query=";

	/**
	 * Helper method to serve up the mandatory view mode.
	 */
	protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException,
	IOException
	{
		response.setContentType("text/html");
		PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(
		"/WEB-INF/jsp/Default_Edit.jsp");
		dispatcher.include(request, response);
	}

	/**
	 * Helper method to serve up the edit_defaults custom mode.
	 */
	protected void doCustomEditDefaults(RenderRequest request, RenderResponse response)
	throws PortletException, IOException
	{
		response.setContentType("text/html");
		PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(
		"/WEB-INF/jsp/Default_Edit.jsp");
		dispatcher.include(request, response);
	}

	/**
	 * Override doDispatch method for handling custom portlet modes.
	 */
	protected void doDispatch(RenderRequest request, RenderResponse response)
	throws PortletException, IOException
	{
		if (!WindowState.MINIMIZED.equals(request.getWindowState()))
		{
			PortletMode mode = request.getPortletMode();
			if (IPortletConstants.CUSTOM_EDIT_DEFAULTS_MODE.equals(mode))
			{
				doCustomEditDefaults(request, response);
				return;
			}
		}
		super.doDispatch(request, response);
	}

	/**
	 * Helper method to serve up the mandatory view mode.
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException,
	IOException
	{
		
		if(LOGGER.isDebugEnabled())LOGGER.debug("Enter doView");
	
		
		//Verify CSRF parameter coming from processAction. 
		if(StringUtils.isNotBlank(request.getParameter("csrfError")) && request.getParameter("csrfError").equalsIgnoreCase("true")){
			LOGGER.error("In doView. csrfError is true");
			response.setContentType(request.getResponseContentType());
			PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(
				"/WEB-INF/jsp/error.jsp");
			dispatcher.include(request, response);
		}
		
		String searchBaseURL = SearchUtil.getSearchBaseURL(request);
		
		LOGGER.debug("The SearchBaseURL: " + searchBaseURL);
		
		
		
		PortletPreferences pref1 = request.getPreferences();
		
		Enumeration <String> enum2 = request.getPreferences().getNames();
		while (enum2.hasMoreElements()){
			String name = enum2.nextElement();
			LOGGER.debug("@@@ In doView: Preference Name: " + name);
			LOGGER.debug("IndoView: preference value: " + pref1.getValue(name, null));
		}
		
		
		
		
		
		if(StringUtils.isNotBlank(searchBaseURL)){
			String submitQuery = FormatUtil.updateWithHTMLCharacters(request.getParameter(IPortletConstants.ACTION_SUBMITQUERY), true);
			LOGGER.debug("The Submit Query is: " + submitQuery);
			
			String searchFromFeature = request.getParameter(IPortletConstants.SEARCH_FROM_FEATURE);
			LOGGER.debug("searchFromFeature: " + searchFromFeature);
			
			if (StringUtils.isNotBlank(submitQuery)
					&& submitQuery.trim().equals(
							IPortletConstants.VAL_SUBMITQUERY_HCO_LINKS)
					&& StringUtils.isNotBlank(searchFromFeature)
					&& searchFromFeature.equals("NCE")) {
				LOGGER
						.debug("SEARCH is for NCE...so english and spanish content should be shown, so removing the language from session.");
				request.getPortletSession().removeAttribute(
						IPortletConstants.SESSION_PARAM_SELECTED_LANGUAGE);
			} else if (StringUtils.isNotBlank(submitQuery)
					&& submitQuery.trim().equals(
							IPortletConstants.VAL_SUBMITQUERY_HCO_LINKS)) {
				/*
				 * for all the health-encyclopedia search not originated from
				 * NCE disable the language facet and use the language present
				 * in the locale
				 */
				LOGGER
						.debug("SEARCH is not for NCE...so using the language from request object.");
				request.setAttribute(
						IPortletConstants.USE_LANGUAGE_FROM_REQUEST,
						Boolean.TRUE);
			}
			
			boolean dsplyRslts = SearchUtil.doSearch(request, this.getPortletConfig());
					
			// If searchXMLProject pref value is "kp-consumernet", then this is site search. Fetch the required content to be
			// displayed on the search results page.
			if("kp-consumernet".equalsIgnoreCase(pref1.getValue(IPortletConstants.PREF_SEARCHPROJECT, null))){
				LOGGER.debug("Site search, fetching the content to be displayed on results page.");
				SearchPortletHelper sHelper = new SearchPortletHelper(request);
				AuxiliaryContent ac = null;
				ac = sHelper.fetchSiteContentFragments();
				if(ac != null){
					LOGGER.debug("Site content is available. ");
				}
				request.getPortletSession().setAttribute("CONTENTBEAN", ac);
			}
			
			showLandingPage(request, response, submitQuery, dsplyRslts);
		} else {
			// Search Base URL not configured in WAS-REE, cannot perform search. No value back from MBean
			response.setContentType(request.getResponseContentType());
			PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(
			"/WEB-INF/jsp/error.jsp");
			dispatcher.include(request, response);
		}
	}

	/**
	 * Called by the portlet container to allow the portlet to process an action request. This
	 * method is called if the client request was originated by a URL created (by the portlet) with
	 * the RenderResponse.createActionURL() method.
	 */
	public void processAction(ActionRequest request, ActionResponse response)
	throws PortletException, IOException
	{
		if(LOGGER.isDebugEnabled())LOGGER.debug("Enter processAction");
		SearchPortletHelper sHelper = new SearchPortletHelper(request);
		// search request
		
		// Verify CSRF Token from incoming JSP request
		if(StringUtils.isNotBlank(request.getParameter(":"+SecurityUtil.TOKEN_HOLDER))){
			try {
				if(LOGGER.isDebugEnabled())LOGGER.debug("Verify CSRF");
				SecurityUtil.verifyCSRFToken(request);
				if(LOGGER.isDebugEnabled())LOGGER.debug("CSRF token Verified");
				
			} catch (CsrfException e) {
				// TODO Auto-generated catch block
				LOGGER.error("Class: KiaserSearchPortlet Function:ProcessAction CSRF ERROR ",e);
				response.setPortletMode(PortletMode.VIEW);
				response.setRenderParameter("csrfError", "true");
				return;
			}
		}
		if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCHREFRESHURL))){
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming new search URL: passing it to doView:"+request.getParameter(IPortletConstants.SEARCHREFRESHURL));
			response.setPortletMode(PortletMode.VIEW);
			response.setRenderParameter(IPortletConstants.SEARCHREFRESHURL, request.getParameter(IPortletConstants.SEARCHREFRESHURL));
			sHelper.updateEventForWI(request.getParameter(IPortletConstants.SEARCHREFRESH_USERACTION), response);
		}else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.VALUE_STARTNEWSEARCH))){
			if(LOGGER.isDebugEnabled())LOGGER.debug("Start a new search, resetting queries...");
			response.setPortletMode(PortletMode.VIEW);
			
			response.setRenderParameter(IPortletConstants.SEARCHREFRESHURL, "");
			response.setRenderParameter(IPortletConstants.SEARCHSTRING, "");
			response.setRenderParameter(IPortletConstants.ACTION_SUBMITQUERY, "");
			String searchFromFeature = request.getParameter(IPortletConstants.SEARCH_FROM_FEATURE);
			
			PortletPreferences pref1 = request.getPreferences();
			
			Enumeration <String> enum2 = request.getPreferences().getNames();
			while (enum2.hasMoreElements()){
				String name = enum2.nextElement();
				LOGGER.debug("@@@ In processAction: Preference Name: " + name);
				LOGGER.debug("In processAction: preference value: " + pref1.getValue(name, null));
			}
			
			if(StringUtils.isNotBlank(searchFromFeature)&& searchFromFeature.equals("NCE")){
				LOGGER.debug("Manually setting the default language in SESSION to en-us as searchFromFeature is NCE and StartNewSearch Link clicked..");
				request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_LANGUAGE, "en-us");
				LOGGER.debug("REMOVING THE SearchFromFeature attribute from renderParameter");
				response.setRenderParameter(IPortletConstants.SEARCH_FROM_FEATURE, "");		
				response.setRenderParameter("startNewSearchFromNceResultsPage", "true");
				LOGGER.debug("Setting searchFromFeature in session to NCE");
				request.getPortletSession().setAttribute(IPortletConstants.SEARCH_FROM_FEATURE, "NCE");
			}
			
		}else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.ACTION_SUBMITQUERY))){
			String submitQuery = FormatUtil.updateWithHTMLCharacters(request.getParameter(IPortletConstants.ACTION_SUBMITQUERY), true);
			String keyword = FormatUtil.updateWithHTMLCharacters(request.getParameter(IPortletConstants.SEARCHSTRING), true);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming ACTION_SUBMITQUERY: "+submitQuery);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming search keyword: passing it to doView:"+keyword);
			response.setPortletMode(PortletMode.VIEW);
			response.setRenderParameter(IPortletConstants.ACTION_SUBMITQUERY, submitQuery);
			response.setRenderParameter(IPortletConstants.SEARCHSTRING, keyword);
			response.setRenderParameter(IPortletConstants.SEARCH_VIEW, request.getParameter(IPortletConstants.SEARCH_VIEW));
			sHelper.updateEventForWI("Keyword|"+keyword, response);
		}else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.ACTION_SUBMITZIPCD))){
			String submitZipCd = request.getParameter(IPortletConstants.ACTION_SUBMITZIPCD);
			String zipCd = request.getParameter(IPortletConstants.SEARCH_ZIPCODE);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming ACTION_SUBMITZIPCD: "+submitZipCd);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Start a proximity search, getting user entered zipcode..."+zipCd);
			response.setPortletMode(PortletMode.VIEW);
			response.setRenderParameter(IPortletConstants.ACTION_SUBMITZIPCD, submitZipCd);
			response.setRenderParameter(IPortletConstants.SEARCH_ZIPCODE, zipCd);
			response.setRenderParameter(IPortletConstants.SEARCH_VIEW, request.getParameter(IPortletConstants.SEARCH_VIEW));
			sHelper.updateEventForWI("Zipcode|"+zipCd, response);
		}else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCH_REGIONFILTER))){
			String rgnSelected = request.getParameter(IPortletConstants.SEARCH_REGIONFILTER);
			// Validation added in for Region/Project selection in Action phase before redirecting  to legacy. 
			// Validation for all other flows will be handled in doView.
			if(StringUtils.isNotEmpty(ROPMap.fetchRegionFilterFromCookie(rgnSelected))){ // checking if region code is valid
				String legacyRgn = ROPMap.updateRegionCodeForLegacyURLs(rgnSelected);
				// Writing the ROP cookies to be recognized by kp.org features
				Cookie imp = new Cookie(IPortletConstants.IMPREGIONCOOKIE,legacyRgn);imp.setSecure(true);
				imp.setPath("/");imp.setDomain(IPortletConstants.COOKIE_DOMAIN);imp.setMaxAge(-1);
				Cookie exp = new Cookie(IPortletConstants.EXPREGIONCOOKIE,legacyRgn);exp.setSecure(true);
				exp.setPath("/");exp.setDomain(IPortletConstants.COOKIE_DOMAIN);exp.setMaxAge(-1);
				Cookie rop = new Cookie(IPortletConstants.ROPREGIONCOOKIE,legacyRgn);rop.setSecure(true);
				rop.setPath("/");rop.setDomain(IPortletConstants.COOKIE_DOMAIN);rop.setMaxAge(-1);
				response.addProperty(imp);
				response.addProperty(exp);
				response.addProperty(rop);
				// ----- Writing the ROP cookies to be recognized by kp.org features
				response.setPortletMode(PortletMode.VIEW);
				response.setRenderParameter(IPortletConstants.SEARCH_REGIONFILTER, rgnSelected);
				sHelper.updateEventForWI("SearchROP|"+rgnSelected, response);
			}else{ // not a valid region code coming in.. head to an error flow
				if(LOGGER.isDebugEnabled())LOGGER.debug("incoming regionCode invalid: "+rgnSelected);
				request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION, rgnSelected);
				request.getPortletSession().setAttribute("BROWSE_LIST", null);
				//request.getPortletSession().setAttribute("CONTENTBEAN", null);
				request.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_RGNCD);
			}
		}else if(StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCH_LANGUAGESELECTOR))){
			// setting the language selected in Session if its not English. If english refresh region selection view with no language
			String localeSelected = request.getParameter(IPortletConstants.SEARCH_LANGUAGESELECTOR);
			if(LOGGER.isDebugEnabled())LOGGER.debug("incoming language: "+localeSelected);
			if(IPortletConstants.LANGUAGE_ENGLISH.equalsIgnoreCase(localeSelected)){
				localeSelected = IPortletConstants.CGI_PARAM_ENGLISH;
				if(LOGGER.isDebugEnabled())LOGGER.debug("setting English language in session: "+localeSelected);
				request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_LANGUAGE,localeSelected);
			}else if(IPortletConstants.LANGUAGE_SPANISH.equalsIgnoreCase(localeSelected)){
				localeSelected = IPortletConstants.CGI_PARAM_SPANISH;
				if(LOGGER.isDebugEnabled())LOGGER.debug("setting Spanish language in session: "+localeSelected);
				request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_LANGUAGE,localeSelected);
			}
			// render parameter set if Region already selected so landing page can be refreshed for the region and the new language
			// if region is not already selected, will refresh the landing page with a fresh view
			String rgnSelected = (String)request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
			if(StringUtils.isNotEmpty(ROPMap.fetchRegionFilterFromCookie(rgnSelected))){ // checking if region code is valid
				response.setRenderParameter(IPortletConstants.SEARCH_REGIONFILTER, rgnSelected);
			}
			sHelper.updateEventForWI("SearchLocale|"+localeSelected, response);
		}else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.ACTION_ALPHABROWSE))){
			String submitAlpha = request.getParameter(IPortletConstants.ACTION_ALPHABROWSE);
			String alphabet = request.getParameter(IPortletConstants.SEARCH_ALPHABET);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming ACTION_ALPHABROWSE: "+submitAlpha+ " for alphabet:"+alphabet);
			response.setPortletMode(PortletMode.VIEW);
			response.setRenderParameter(IPortletConstants.ACTION_ALPHABROWSE, submitAlpha);
			response.setRenderParameter(IPortletConstants.SEARCH_ALPHABET, alphabet);
			sHelper.updateEventForWI("Alphabet|"+alphabet, response);
		}else if(StringUtils.isNotBlank(request.getParameter(IPortletConstants.ACTION_SUBMITEDITUPDATES))){
			sHelper.persistSearchPreferences();
		}
	}	
	
	
	
	public void serveResource(ResourceRequest rReq, ResourceResponse rResp){
		SearchPortletHelper sHelper = new SearchPortletHelper();
		sHelper.helpServeResource(rReq, rResp);
	}
	
	protected void showLandingPage(RenderRequest request,
			RenderResponse response, String submitQuery, boolean dsplyRslts)
			throws PortletException, IOException {
			
			Enumeration<String> enum1 = request.getParameterNames();
			while (enum1.hasMoreElements()){
				LOGGER.debug("@@@ Parameter Name: " + enum1.nextElement());
			}
			
			PortletPreferences pref1 = request.getPreferences();
		
			Enumeration <String> enum2 = request.getPreferences().getNames();
			while (enum2.hasMoreElements()){
				String name = enum2.nextElement();
				LOGGER.debug("@@@ Preference Name: " + name);
				LOGGER.debug("preference value: " + pref1.getValue(name, null));
			}
			
		
		LOGGER.debug("showLandingPage: submitQuery:" + submitQuery + ", dsplyRslts:" + dsplyRslts + " ,Preferences: " + request.getPreferences());
		String searchLandingPage = request.getPreferences().getValue(IPortletConstants.PREF_SEARCHLANDINGPAGE, "");
		LOGGER.debug("searchLandingPage from request Preferences: " + searchLandingPage);
		
		String searchFromFeature = request.getParameter(IPortletConstants.SEARCH_FROM_FEATURE);
		LOGGER.debug("searchFromFeature parameter value is: " + searchFromFeature);
		
	
		String startNewSearchFromNceResultsPage = request.getParameter("startNewSearchFromNceResultsPage");
		
		if (startNewSearchFromNceResultsPage!=null && startNewSearchFromNceResultsPage.trim().equals("true")){
			LOGGER.debug("Here to render the heindex.jsp and not default_view.jsp");
		}

		
		String forwardTo = determineLandingPage(submitQuery, dsplyRslts,
				searchLandingPage, searchFromFeature,startNewSearchFromNceResultsPage);
		
		if(StringUtils.isNotEmpty(forwardTo)){
			LOGGER.debug("finally ...forwarding to:" + forwardTo);
			dispatchRequest(request, response, forwardTo);	
		}else{
			LOGGER.debug("finally ...forwarding to:Default_View.jsp");
			dispatchRequest(request, response, "/WEB-INF/jsp/Default_View.jsp");	
			
		}
	}

	protected String determineLandingPage(String submitQuery, boolean dsplyRslts,
			String searchLandingPage, String searchFromFeature, String startNewSearchFromNceSearchResultsPage) {
	
		String forwardTo="";
		
		LOGGER.debug("submitQuery: "+ submitQuery + ", dsplyRslts: " + dsplyRslts + ", searchLandingPage: "+ searchLandingPage + ", searchFromFeature: "+searchFromFeature);
		
		if(StringUtils.isNotBlank(searchLandingPage) && !dsplyRslts){
			forwardTo=searchLandingPage;
			LOGGER.debug("searchLandingPage is not empty and dsplyRslts is false. Will forward to:" + searchLandingPage);
			
		}else if(StringUtils.isBlank(searchLandingPage) 
				&& !dsplyRslts 
				&& ((searchFromFeature !=null && searchFromFeature.trim().equals("NCE")) 
						|| (startNewSearchFromNceSearchResultsPage!=null 
						    && startNewSearchFromNceSearchResultsPage.trim().equals("true")) ))
		{
			forwardTo ="/WEB-INF/jsp/heindex.jsp";
			LOGGER.debug("searchLandingPage is empty, dsplyRslts is false and searchFromFeature is NCE. Will forward to:" + forwardTo);
		}
		else{
			if(IPortletConstants.VAL_SUBMITQUERY_HCO_LINKS.equals(submitQuery)){
				LOGGER.debug("dsplyRslts is true. submitQuery:" + IPortletConstants.VAL_SUBMITQUERY_HCO_LINKS);
				if(StringUtils.isNotBlank(searchFromFeature)&& searchFromFeature.equals("NCE") ){
					LOGGER.debug("will render NCECustomview.jsp");
					forwardTo="/WEB-INF/jsp/NCECustom_View.jsp";							
				}	else{
					LOGGER.debug("will render HCOCustomview.jsp");
					forwardTo="/WEB-INF/jsp/HCOCustom_View.jsp";
				}
			}else{
				LOGGER.debug("will render Default_View.jsp");
				forwardTo="/WEB-INF/jsp/Default_View.jsp";
			}	
		}
		
		return forwardTo;
	}

	private void dispatchRequest(RenderRequest request,
			RenderResponse response, String forwardTo) throws PortletException,
			IOException {
		
		LOGGER.debug("In dispatchRequest. forwardTo: "  + forwardTo);
		response.setContentType(request.getResponseContentType());
		PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(forwardTo);
		dispatcher.include(request, response);
	}


}