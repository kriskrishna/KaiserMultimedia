package org.kp.wpp.portlet.search.helpers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.content.engine.EngineFactory;
import org.kp.wpp.content.engine.IContentEngine;
import org.kp.wpp.portlet.search.beans.AuxiliaryContent;
import org.kp.wpp.portlet.search.beans.Facet;
import org.kp.wpp.portlet.search.beans.FacetGroup;
import org.kp.wpp.portlet.search.beans.SearchCriteria;
import org.kp.wpp.portlet.search.beans.SearchResultsView;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.utils.ConfigMBeanUtil;
import org.kp.wpp.portlet.search.utils.FormatUtil;
import org.kp.wpp.portlet.search.utils.ROPMap;
import org.kp.wpp.utils.search.service.VivisimoXMLService;
import org.kp.wpp.utils.search.service.objects.KaiserSearchRequest;
import org.kp.wpp.utils.search.service.objects.KaiserSearchResponse;
import org.kp.wpp.utils.search.service.objects.Link;
import org.kp.wpp.utils.search.service.objects.Navigation;

public class SearchPortletHelper {

	private static final Log LOGGER = LogFactory.getLog(SearchPortletHelper.class);
	private ContentRetriever cRetriever = null;
	private RenderRequest rReq = null;
	private ActionRequest aReq = null;

	/**
	 * To store the request and instantiate contentengine
	 * @param request
	 */
	public SearchPortletHelper(RenderRequest request){
		cRetriever = new ContentRetriever(getEngine(request));
		rReq = request;
	}
	
	/**
	 * To store the request and instantiate contentengine
	 * @param request
	 */
	public SearchPortletHelper(ActionRequest request){
		cRetriever = new ContentRetriever(getEngine(request));
		aReq = request;
	}
		
	public SearchPortletHelper() {
	}

	public String fetchROP(){
		String rop = rReq.getPreferences().getValue(IPortletConstants.PREF_SEARCHREGION, "");
		/* if the region is not available as a preference parameter check the cookie and set it */
		if(StringUtils.isBlank(rop))
		{
			Cookie[] cookies = rReq.getCookies();
			if (cookies != null)
			{
				for (Cookie cookie : cookies)
				{
					if (cookie.getName().equals(IPortletConstants.IMPREGIONCOOKIE))
					{
						if(LOGGER.isDebugEnabled())LOGGER.debug("Found region cookie: " + cookie.getValue());
						rop = cookie.getValue();
						break;
					}
				}
			}
		}
		return rop;
	}

	public String updateForRegionOfPreference(String sKeyword){
		String rop = fetchROP();
		String updatedKeyword = null;
		/* if there is a region, get the region name from ROPMap else return the keyword */
		if(StringUtils.isNotBlank(rop))
		{
			updatedKeyword = ROPMap.updateKeywordForROP(sKeyword, rop);
		}
		else{
			updatedKeyword = sKeyword;
		}
		return updatedKeyword;
	}
	
	public void persistSearchPreferences(){
		LOGGER.debug("persistSearchPreferences---------------->Start--->");
		try{
		Enumeration<String> enum1 = aReq.getParameterNames();
		boolean searchLandingPageParameterFoundInRqst=false;
		if(enum1!=null){
			while (enum1.hasMoreElements()){
					String name = (String)enum1.nextElement();
					LOGGER.debug("@@@ Request Parameter Name: " + name + ", parameter value:  " + aReq.getParameter(name));
					if(name.equals(IPortletConstants.FIELD_SEARCHLANDINGPAGE)){
						LOGGER.debug("@@@ SearchLandingPage parameter found in request!");
						searchLandingPageParameterFoundInRqst=true;
					}
			}	
		}else{
			LOGGER.debug("No request parameters found!");
		}
			
			PortletPreferences pPref = aReq.getPreferences();
			
			pPref.setValue(IPortletConstants.PREF_SEARCHPROJECT, aReq.getParameter(IPortletConstants.FIELD_SEARCHPROJECT));
			pPref.setValue(IPortletConstants.PREF_SEARCHSOURCES, aReq.getParameter(IPortletConstants.FIELD_SEARCHSOURCES));
			pPref.setValue(IPortletConstants.PREF_SEARCHCUSTOMDISPLAY, aReq.getParameter(IPortletConstants.FIELD_SEARCHCUSTOMDISPLAY));
			pPref.setValue(IPortletConstants.PREF_SEARCHTOTALRESULTS, aReq.getParameter(IPortletConstants.FIELD_SEARCHTOTALRESULTS));
			pPref.setValue(IPortletConstants.PREF_SEARCHRESULTSPERPAGE, aReq.getParameter(IPortletConstants.FIELD_SEARCHRESULTSPERPAGE));
			pPref.setValue(IPortletConstants.PREF_SEARCHTIMEOUT, aReq.getParameter(IPortletConstants.FIELD_SEARCHTIMEOUT));
			pPref.setValue(IPortletConstants.PREF_SEARCHREGION, aReq.getParameter(IPortletConstants.FIELD_SEARCHREGION));
			LOGGER.debug("persistSearchPreferences---------------->aReq.getParameter(IPortletConstants.FIELD_NCA)--->"+aReq.getParameter(IPortletConstants.FIELD_NCA));

			pPref.setValue(IPortletConstants.PREF_NCA_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_NCA));
			pPref.setValue(IPortletConstants.PREF_SCA_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_SCA));
			pPref.setValue(IPortletConstants.PREF_DB_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_DB));
			pPref.setValue(IPortletConstants.PREF_NC_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_NC));
			pPref.setValue(IPortletConstants.PREF_CS_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_CS));
			pPref.setValue(IPortletConstants.PREF_GGA_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_GGA));
			pPref.setValue(IPortletConstants.PREF_HAW_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_HAW));
			pPref.setValue(IPortletConstants.PREF_MID_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_MID));
			pPref.setValue(IPortletConstants.PREF_OHI_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_OHI));
			pPref.setValue(IPortletConstants.PREF_KNW_PLAN_PROVIDER_TYPE, aReq.getParameter(IPortletConstants.FIELD_KNW));
			
			if(searchLandingPageParameterFoundInRqst){
				LOGGER.debug("Search landing page parameter found in request..so will go ahead and set it in the preferences.");
				pPref.setValue(IPortletConstants.PREF_SEARCHLANDINGPAGE, aReq.getParameter(IPortletConstants.FIELD_SEARCHLANDINGPAGE));
			}else{
				LOGGER.debug("Search landing page parameter NOT found in request..so will NOT set it in the preferences.");
			}
			
			//pPref.setValue(IPortletConstants.PREF_SEARCHPROXIMITYSOURCE, aReq.getParameter(IPortletConstants.FIELD_SEARCHPROXIMITYSOURCE));
			//pPref.setValue(IPortletConstants.PREF_SEARCHCUSTOM_BINONLY_DISPLAY, aReq.getParameter(IPortletConstants.FIELD_SEARCHCUSTOM_BINONLY_DISPLAY));
			//pPref.setValue(IPortletConstants.PREF_SEARCHALHPABETSOURCE, aReq.getParameter(IPortletConstants.FIELD_SEARCHALHPABETSOURCE));
			//pPref.setValue(IPortletConstants.PREF_DEFAULT_LOCALE, aReq.getParameter(IPortletConstants.FIELD_DEFAULT_LOCALE));
			//pPref.setValue(IPortletConstants.PREF_DEFAULT_REGIONFACET_WITH_ROP, aReq.getParameter(IPortletConstants.FIELD_DEFAULT_REGIONFACET_WITH_ROP));
			//pPref.setValue(IPortletConstants.PREF_BROWSE_WO_RGNGATE, aReq.getParameter(IPortletConstants.FIELD_BROWSE_WO_RGNGATE));
			//pPref.setValue(IPortletConstants.PREF_GLOSSARYPAGEURL, aReq.getParameter(IPortletConstants.FIELD_GLOSSARYPAGEURL));
			//pPref.setValue(IPortletConstants.PREF_HELPPAGEURL, aReq.getParameter(IPortletConstants.FIELD_HELPPAGEURL));
			//pPref.setValue(IPortletConstants.PREF_SEARCHLANDINGPAGE, aReq.getParameter(IPortletConstants.FIELD_SEARCHLANDINGPAGE));
			//pPref.setValue(IPortletConstants.PREF_SEARCHSTYLES, aReq.getParameter(IPortletConstants.FIELD_SEARCHSTYLES));
			//pPref.setValue(IPortletConstants.PREF_SORTORDER_FACETS, aReq.getParameter(IPortletConstants.FIELD_SORTORDER_FACETS));
			//pPref.setValue(IPortletConstants.PREF_SYMPTOM_CHECKER, aReq.getParameter(IPortletConstants.FIELD_SYMPTOM_CHECKER));
			pPref.store();
		}
		catch(Exception e){
			aReq.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_FAILURE);
			LOGGER.error("Exception occured: Redirecting to error page: "+e.getMessage());
		}
	}	

	public void persistMGSearchPreferences(){
		// TBD: Add code to persist MG specific preferences
		persistSearchPreferences();
	}	
	
	private SearchResultsView fetchProviderType(KaiserSearchRequest ksReq,ResourceRequest rReq){
		SearchResultsView mgSearchView=null;
		try{
		VivisimoXMLService vService = getVivisimoXMLService();
		KaiserSearchResponse ksResp = vService.search(ksReq);
		mgSearchView = new SearchHelper().processProviderTypeResults(ksResp);
		
		}
		catch(Exception e){
			rReq.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_FAILURE);
			LOGGER.error("Exception occured: Redirecting to error page: "+e.getMessage());
		}
		return mgSearchView;
	}
	
	public void fetchSearchResults(KaiserSearchRequest ksReq){
		try{
			VivisimoXMLService vService = getVivisimoXMLService();
			KaiserSearchResponse ksResp = vService.search(ksReq);
			Navigation nav1 = ksResp.getNav();
			if(nav1!=null){
				for (Link l : nav1.getLstLinks()) {
					LOGGER.debug("Link Value: " + l.getValue() + ";type: " + l.getType() + ";Start: " + l.getStart() + ";End: " + l.getEnd());
				}
			}
			// Set-Cookie value set in Session if present in response. Should be done only once.
			if(StringUtils.isNotBlank(ksResp.getSearchSessionIdentifier())){
				rReq.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_COOKIE_IDENTIFIER, ksResp.getSearchSessionIdentifier());
			}

			SearchResultsView mgSearchView = getSearchHelper().processSearchResults(ksResp, rReq.getPreferences());
			// fix to hide alphabet fielded search term from user:
			String keyword = mgSearchView.getSearchCriteria().getKeyword();
			if(StringUtils.isNotBlank(keyword) && 
				keyword.contains(IPortletConstants.PREFIX_QUERY_PARENT_LETTER)){
				mgSearchView.getSearchCriteria().setKeyword(null);
				mgSearchView.getSearchCriteria().setSearchAlphabet(keyword.replace(IPortletConstants.PREFIX_QUERY_PARENT_LETTER, ""));
			}
			// alphabet browse - fix ends
			if(null != mgSearchView.getContents()){	
				String label = rReq.getParameter(IPortletConstants.SEARCH_RESULTS_LABEL);
				String submitQuery = rReq.getParameter(IPortletConstants.ACTION_SUBMITQUERY);
				if(IPortletConstants.VAL_SUBMITQUERY_HCO_LINKS.equals(submitQuery)){
					if (StringUtils.isNotBlank(label) ){
						mgSearchView.getSearchCriteria().setKeyword(FormatUtil.updateWithHTMLCharacters(label));
					}else{
						mgSearchView.getSearchCriteria().setKeyword(null);
					}
				}
				if(LOGGER.isDebugEnabled())LOGGER.debug("beans available for display: setting request attributes >> ");
				rReq.setAttribute("FACETS_LIST", mgSearchView.getSelectableFacetGroups());
				rReq.setAttribute("PAGINATION_LIST", mgSearchView.getNavigationLinks());
				rReq.setAttribute("RESULTS_LIST", mgSearchView.getContents());
				rReq.setAttribute("BREADCRUMBS_LIST", mgSearchView.getSelectedFacetGroups());
				rReq.setAttribute("SEARCH_GENERIC_VIEW_BEAN", mgSearchView.getSearchCriteria());
			}
			else{		
				if(LOGGER.isDebugEnabled())LOGGER.debug("no search: setting error attribute -> empty no results >> ");
				SearchCriteria searchCriteriaForError = mgSearchView.getSearchCriteria();
				rReq.setAttribute("SEARCH_GENERIC_VIEW_BEAN", searchCriteriaForError);
				if(!searchCriteriaForError.isSummaryDisplayFlag()){	
					rReq.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_NORESULTS);
				}
			}
		}
		catch(Exception e){	
			rReq.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_FAILURE);
			LOGGER.error("Exception occured: Redirecting to error page: "+e.getMessage());
		}
	}	

	public void helpServeResource(ResourceRequest rReq, ResourceResponse rResp){
		String viewMoreURL = rReq.getParameter(IPortletConstants.SEARCHREFRESHURL);
		String planTypeURL = rReq.getParameter(IPortletConstants.GATED_PLAN_SEARCH_URL);
		String planTypeAction = rReq.getParameter(IPortletConstants.SEARCHREFRESH_USERACTION);
		String sessionCookie = (String)rReq.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_COOKIE_IDENTIFIER);
		String GatedPlanSearchURL = rReq.getParameter(IPortletConstants.GATED_PLAN_SEARCH_URL);
		String GatedProvideTypeValue = rReq.getParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE);
		String mgSelection = (String)rReq.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH);
		
		LOGGER.debug("helpServeResource--->sessionCookie------->B4--->"+sessionCookie);
		LOGGER.debug("helpServeResource--->viewMoreURL------->B4--->"+viewMoreURL);
		LOGGER.debug("helpServeResource--->planTypeURL------->B4--->"+planTypeURL);
		LOGGER.debug("helpServeResource--->planTypeAction------->B4--->"+planTypeAction);
		LOGGER.debug("helpServeResource--->GatedPlanSearchURL------->B4--->"+GatedPlanSearchURL);
		LOGGER.debug("helpServeResource--->GatedProvideTypeValue------->B4--->"+GatedProvideTypeValue);
		LOGGER.debug("helpServeResource--->mgSelection------->B4--->"+mgSelection);
		
		if ((planTypeURL != null) && (planTypeURL.indexOf("provider_label") != -1))
		{
			int providerStart = planTypeURL.indexOf("provider_label");
			planTypeURL = planTypeURL.substring(0, providerStart);
			LOGGER.debug("helpServeResource--->transformed planTypeURL to remove provider details------->"+planTypeURL);
		}
				
		if(GatedPlanSearchURL!=null || GatedProvideTypeValue!=null){
			viewMoreURL=null;
		}else{
			planTypeURL=null;
		}
		LOGGER.debug("helpServeResource--->viewMoreURL------->AF--->"+viewMoreURL);
		LOGGER.debug("helpServeResource--->planTypeURL------->AF--->"+planTypeURL);


		VivisimoXMLService vService = null;
		KaiserSearchRequest ksReq =null;
		ConfigMBeanUtil mBeanReader = null;
		
		rResp.setContentType("text/html");
		rResp.setCharacterEncoding("UTF-8");
		StringBuffer response= new StringBuffer();

			KaiserSearchResponse ksResp=null;
			ksReq = new KaiserSearchRequest();
			mBeanReader = new ConfigMBeanUtil();
			PortletPreferences pPref = rReq.getPreferences();
			ksReq.setDisplayXMLFeedClone(pPref.getValue(IPortletConstants.PREF_SEARCHCUSTOMDISPLAY, "xml-feed-display-wpp-mg"));
			ksReq.setTotalResults(pPref.getValue(IPortletConstants.PREF_SEARCHTOTALRESULTS, ""));
			ksReq.setResultsPerPage(pPref.getValue(IPortletConstants.PREF_SEARCHRESULTSPERPAGE, ""));
			ksReq.setTimeOut(pPref.getValue(IPortletConstants.PREF_SEARCHTIMEOUT, ""));
			String connectTimeout = mBeanReader.getProperty(IPortletConstants.REE_CONNECT_TIMEOUT);
			String readTimeout = mBeanReader.getProperty(IPortletConstants.REE_READ_TIMEOUT);
			// Cookie persistence maintained through code, to avoid issues with load balanced environments.
			ksReq.setSearchSessionIdentifier(sessionCookie);
			ksReq.setHttpConnecttimeOut(connectTimeout);
			ksReq.setHttpReadtimeOut(readTimeout);
		
			
		
		if(StringUtils.isNotBlank(planTypeURL)){
			
			String searchBaseURL = (String)rReq.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SEARCH_URL);
			if(StringUtils.isBlank(searchBaseURL))
			{
				searchBaseURL = mBeanReader.getProperty(IPortletConstants.REE_URL_PROPERTY);
				rReq.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SEARCH_URL, searchBaseURL);
				if(LOGGER.isDebugEnabled())LOGGER.debug("Search server base URL from ConfigMBeanUtil = "+searchBaseURL);
			}
			
				if(LOGGER.isDebugEnabled())LOGGER.debug("got providerTypeURL = "+ planTypeURL);
			try{
				if(planTypeAction!=null){
				StringTokenizer stPlan = new StringTokenizer(planTypeAction,"|");
				while (stPlan.hasMoreElements()) {
					planTypeAction=(String)stPlan.nextElement();
					
				}
				}
				LOGGER.debug("helpServeResource--->planTypeURL--->searchBaseURL------->Inside--->"+searchBaseURL);
				LOGGER.debug("helpServeResource--->planTypeURL--->planTypeURL------->Inside--->"+planTypeURL);

				rReq.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_ACTION, planTypeAction);
				ksReq.setBaseUrl(searchBaseURL);
				ksReq.setSearchRefreshUrl(planTypeURL);

				SearchResultsView mgSearchView= fetchProviderType(ksReq,rReq);
				//String response = ksResp.getProviderTypeResults();
				List<FacetGroup> selectableFG = mgSearchView.getSelectableFacetGroups();
				SearchHelper helper = new SearchHelper();
				Map<String, List<Facet>> mpFg = helper.convertFacetListtoMap(selectableFG);
				List<Facet> lProvider = mpFg.get(IPortletConstants.FACET_TITLE_PROVIDER_TYPE_LABEL);
				rReq.getPortletSession().setAttribute("lProvider", lProvider);

			    if(null!=lProvider && lProvider.size() > 0){

			    	PortletURL portletURLDynamic =rResp.createActionURL();
			    	PortletURL portletURL =rResp.createActionURL();
			    	portletURL.setParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE, IPortletConstants.VALUE_SELECTPROVIDER);
			    	String selectDropdown=rResp.getNamespace()+"ProviderVal";
			    	response.append("<div id=\"mgGatedSearchProviderTypeContainer\" style=\"display:block;\"></div>");
			    	response.append("<div style=\"clear:both;\"></div>");
			    	response.append("<div class=\" mgsEnhancementsGatedLabel\">Provider Type or Medical Group</div>");
			    	response.append("<div class=\"mgs_col_rightGatedSearch\">");
			    	response.append("<div id=\"ProviderbgID\"><label class=\"wptheme-access\" for=\"ProviderSelect\">Select a provider type or medical group, selecting an option will cause a page refresh.</label>");
			    	response.append("<select onchange='javascript:"+rResp.getNamespace()+"openTarget(\"Provider\")' id="+selectDropdown+" name=\"ProviderSelect\" class=\"mgs_area_padding\" title=\"Select a provider type or medical group, selecting an option will cause a page refresh.\">");
			    	response.append("<option value="+portletURL.toString()+">Select a Provider Type</option>");
			    	
			    	for (Facet fc : lProvider)
					{
			    		portletURLDynamic.setParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE, fc.getSelectHref());
			    		portletURLDynamic.setParameter(IPortletConstants.SEARCHREFRESH_USERACTION, "Provider|"+fc.getTitle());
			    		//response=response+"<option value=\"<portlet:actionURL><portlet:param name="+IPortletConstants.SEARCHREFRESHURL+"value="+fc.getSelectHref()+"/><portlet:param name="+IPortletConstants.SEARCHREFRESH_USERACTION+ "value=\"Provider|"+fc.getTitle()+"/></portlet:actionURL>"+fc.getTitle()+"</option>";
			    		response.append("<option value="+portletURLDynamic.toString()+">"+fc.getTitle()+"</option>");
			    		response.append(fc.getTitle()+"</option>");

			    		//response=response+"fc.getTitle()</option>";
						
					}
			    	response.append("</select>");
			   // 	response.append("<input type=\"button\" id=\"ConfirmProviderGo\" value=\"Go\" onClick='javascript:"+rResp.getNamespace()+"openTarget(\"Provider\")' class=\"mgsInputGoProvider\" title=\"submit to search by provider\">");
			    	response.append("</div>");
			    	response.append("</div>");

			    }
				//response= "This is Provider Display dropdown value"; 
				LOGGER.debug("response------->"+response.toString());

				


				if(StringUtils.isBlank(response.toString())){
					rResp.getWriter().print(IPortletConstants.ERROR_PLAN_TYPE);
				}else{
					rResp.getWriter().println(response.toString());
				}
			}
			catch(Exception e){
				LOGGER.error("Exception occured: "+e.getMessage());
				try {
					rResp.getWriter().print(IPortletConstants.ERROR_PLAN_TYPE);
				} catch (IOException ioEx) {
					LOGGER.error("Exception:IOException occured: "+ioEx.getMessage());
				}
			}
		
			
		}		
	
		if(StringUtils.isNotBlank(viewMoreURL)){
			LOGGER.debug("helpServeResource--->viewMoreURL--->viewMoreURL------->Inside--->"+viewMoreURL);
			
			if(LOGGER.isDebugEnabled())LOGGER.debug("got viewMoreURL = "+ viewMoreURL);
			try{
				vService = new VivisimoXMLService();
				ksReq.setSearchRefreshUrl(viewMoreURL);
				// Set doctor or facility source project. This will be used in service code to determine which XSL file to use.
				if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(mgSelection)){
					ksReq.setSourceProject(pPref.getValue("facdir-project", ""));
				} else if (IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
					ksReq.setSourceProject(pPref.getValue("doctor-project", ""));
				}
				ksResp = vService.getViewMoreResults(ksReq);
				response.append(ksResp.getSearchViewMoreResults());
				if(StringUtils.isBlank(response.toString())){
					rResp.getWriter().println(IPortletConstants.ERROR_PAGING);
				}else{
					rResp.getWriter().println(response);
				}
			}
			catch(Exception e){
				LOGGER.error("Exception occured: "+e.getMessage());
				try {
					rResp.getWriter().println(IPortletConstants.ERROR_PAGING);
				} catch (IOException ioEx) {
					LOGGER.error("Exception:IOException occured: "+ioEx.getMessage());
				}
			}
		}
		
		
	}
	
	public AuxiliaryContent fetchNationalContentFragments() {
		AuxiliaryContent ac = cRetriever.getMGNationalContents();
		return ac;
	}
	
	public AuxiliaryContent fetchRegionalContentFragments(String rgnCode) {
		AuxiliaryContent ac = cRetriever.getMGRegionalContents(rgnCode);
		return ac;
	}
	
	public AuxiliaryContent fetchIntroContentFragments() {
		AuxiliaryContent ac = cRetriever.getMGIntroContents();
		return ac;
	}
	
	public AuxiliaryContent fetchSiteContentFragments() {
		AuxiliaryContent ac = cRetriever.getSiteContents();
		return ac;
	}

	/**
	 * @return the engine
	 */
	protected IContentEngine getEngine(PortletRequest pReq) {
		return EngineFactory.getContentEngine(pReq);
	}
	
	protected VivisimoXMLService getVivisimoXMLService() {
		return new VivisimoXMLService();
	}
	
	protected SearchHelper getSearchHelper() {
		return new SearchHelper();
	}

	public void updateEventForWI(String eventValue, ActionResponse response){
		if(StringUtils.isNotBlank(eventValue)){
			StringTokenizer st = new StringTokenizer(eventValue, "|");
			int count = st.countTokens();
			if(count == 2){
				String event = st.nextToken();
				String value = st.nextToken();
				response.setRenderParameter(IPortletConstants.SEARCHREFRESH_USERACTION, event);
				response.setRenderParameter(IPortletConstants.SEARCHREFRESH_USERACTION_VALUE, value);
			}
		}
	}	
}
