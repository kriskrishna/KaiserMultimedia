package org.kp.wpp.portlet.search;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.beans.AuxiliaryContent;
import org.kp.wpp.portlet.search.beans.Facet;
import org.kp.wpp.portlet.search.beans.FacetGroup;
import org.kp.wpp.portlet.search.beans.SearchCriteria;
import org.kp.wpp.portlet.search.beans.SearchResultsView;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.helpers.SearchHelper;
import org.kp.wpp.portlet.search.helpers.SearchPortletHelper;
import org.kp.wpp.portlet.search.utils.ConfigMBeanUtil;
import org.kp.wpp.portlet.search.utils.FormatUtil;
import org.kp.wpp.portlet.search.utils.ROPMap;
import org.kp.wpp.portlet.search.utils.SearchStringValidator;
import org.kp.wpp.utils.search.service.VivisimoXMLService;
import org.kp.wpp.utils.search.service.objects.KaiserSearchRequest;
import org.kp.wpp.utils.search.service.objects.KaiserSearchResponse;

import com.ibm.ws.portletcontainer.portlet.PortletUtils;

public class KaiserMGSearchPortlet extends GenericPortlet
{
	
	private static final String PREF_FACDIR_PROJECT = "facdir-project";
	private static final String PREF_DOCTOR_PROJECT = "doctor-project";
	private static final String PREF_FACDIR_SOURCE = "facdir-source";
	private static final String PREF_DOCTOR_SOURCE = "doctor-source";
	private static final String PREF_FACDIR_PROXIMITY_SOURCE = "facdir-proximity-source";
	private static final String PREF_DOCTOR_PROXIMITY_SOURCE = "doctor-proximity-source";

	private static Log LOGGER = LogFactory.getLog(KaiserMGSearchPortlet.class);
	//private static final String BASE_URL = "https://kpvc1035.crdc.kp.org:8443/search/cgi-bin/query-meta?v%3Aproject=kp-consumernet&render.function=xml-feed-display&content-type=text/xml&query=";

	/**
	 * Helper method to serve up the mandatory view mode.
	 */
	protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException,
	IOException
	{
		response.setContentType("text/html");
		PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(
		"/WEB-INF/mgsearch/jsp/Default_Edit.jsp");
		dispatcher.include(request, response);
	}

	/**
	 * Helper method to serve up the mandatory view mode.
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException,
	IOException
	{

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Enter doView");
			debugDocSession(request, " - doView() - END");
			debugRequestParameter(request);
		}
		
		boolean submitZipCd = StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCH_ZIPCODE));
		boolean submitKeyWd = StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCHSTRING));
		boolean facetRefresh = StringUtils.isNotBlank(request.getParameter(IPortletConstants.FACETREFRESH));
		
		String refreshSearchURL = request.getParameter(IPortletConstants.SEARCHREFRESHURL);
		String providerTypeURL =(String)request.getPortletSession().getAttribute(IPortletConstants.GATED_PROVIDER_VALUE);
		String planGateAcion =(String)request.getPortletSession().getAttribute(IPortletConstants.GATED_PLAN_ACTION);
		String errorCode = request.getParameter("ErrorCode");
		
		String pcpYESUrl=(String)request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP_YES_URL);
		
		if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.VALUE_STARTNEWSEARCH)) ||
				StringUtils.isNotBlank(request.getParameter(IPortletConstants.VALUE_SUB_STARTNEWSEARCH))){
			clearDocSession(request);
		}	
		
		SearchPortletHelper sHelper = new SearchPortletHelper(request);
		String ropFromCookie = ROPMap.fetchRegionFilterFromCookie(sHelper.fetchROP());
		String preRgCode=(String)request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
		if(preRgCode!=null && preRgCode.equalsIgnoreCase(IPortletConstants.REGIONCD_COLORADO_NORTHERN)){
			preRgCode=IPortletConstants.REGIONCD_COLORADO_DENVER_BOULDER;
		}
		
		
		if(request.getPortletSession().getAttribute("actionView")==null){	
			request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION, ropFromCookie);
			
			// Find a Doctor or locate a facility redirect on searchtype URL parameter
			HttpServletRequest httpServletRequest = PortletUtils.getHttpServletRequest(request);
			if(httpServletRequest != null){
				String searchType = (String) httpServletRequest.getParameter("searchtype");
				if(LOGGER.isDebugEnabled())LOGGER.debug("doView URL parameter searchtype = "+searchType);
				
				if (StringUtils.isNotBlank(searchType)){
					if (StringUtils.equalsIgnoreCase(IPortletConstants.MGSELECTION_LOCATIONS, searchType)){
						request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH_LOCATION, IPortletConstants.MGSELECTION_LOCATIONS);
					} else if (StringUtils.equalsIgnoreCase(IPortletConstants.MGSELECTION_DOCTORS, searchType)){
						request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH, null);
						request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH_LOCATION, IPortletConstants.MGSELECTION_DOCTORS);
					}
				}
			}
		}
		// getting Search backend url from REE (wpp/search JNDI)
		ConfigMBeanUtil mBeanReader = new ConfigMBeanUtil();
		String searchBaseURL = (String)request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SEARCH_URL);
		//Storing Search server base url in Session to avoid repeated calls to MBean
		if(StringUtils.isBlank(searchBaseURL))
		{
			searchBaseURL = mBeanReader.getProperty(IPortletConstants.REE_URL_PROPERTY);
			request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SEARCH_URL, searchBaseURL);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Search server base URL from ConfigMBeanUtil = "+searchBaseURL);
		}
		if(StringUtils.isNotBlank(searchBaseURL)){
			boolean dsplyRslts = false;
			KaiserSearchRequest ksReq = new KaiserSearchRequest();
			ksReq.setBaseUrl(searchBaseURL);
			PortletPreferences pPref = request.getPreferences();
			ksReq.setDisplayXMLFeedClone(pPref.getValue(IPortletConstants.PREF_SEARCHCUSTOMDISPLAY, "xml-feed-display-wpp-mg"));
			ksReq.setTotalResults(pPref.getValue(IPortletConstants.PREF_SEARCHTOTALRESULTS, ""));
			ksReq.setResultsPerPage(pPref.getValue(IPortletConstants.PREF_SEARCHRESULTSPERPAGE, ""));
			ksReq.setTimeOut(pPref.getValue(IPortletConstants.PREF_SEARCHTIMEOUT, ""));

			// R4 Post release Code Fix - QC 997
			String connectTimeout = mBeanReader.getProperty(IPortletConstants.REE_CONNECT_TIMEOUT);
			String readTimeout = mBeanReader.getProperty(IPortletConstants.REE_READ_TIMEOUT);
			ksReq.setHttpConnecttimeOut(connectTimeout);
			ksReq.setHttpReadtimeOut(readTimeout);
			// R4 Post release Code Fix - QC 997

			// check if there is a refresh URL for current search, has to be done before checking for empty search
			if(StringUtils.isNotBlank(refreshSearchURL)){
				//check for error scenario: All empty input
				if(StringUtils.isNotBlank(errorCode)){
					if(LOGGER.isDebugEnabled())LOGGER.debug("Empty search parameters. Set error and return");
					// apply error flow for empty search fields
					request.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_EMPTYSEARCHMSG);					
				}
				else if (refreshSearchURL.equalsIgnoreCase(IPortletConstants.VALUE_SELECTPROVIDER)){
					if(LOGGER.isDebugEnabled())LOGGER.debug("Empty Provider Search - "+refreshSearchURL);
					// apply error flow for Provider Type Blank Search
					request.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_PROVIDER);
					Boolean gatedProviderSearch = (Boolean) request.getPortletSession().getAttribute(IPortletConstants.GATED_PROVIDER_TYPE_SEARCH);
					if(gatedProviderSearch!=null && gatedProviderSearch.booleanValue()){
					request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_ACTION,IPortletConstants.VALUE_SELECTPROVIDER);
					}
				}
				else if (facetRefresh){
					if(LOGGER.isDebugEnabled())LOGGER.debug("incoming facetRefresh for URL - "+refreshSearchURL);
					ksReq.setSearchRefreshUrl(refreshSearchURL);
					//doSearch for refresh Search URL
					sHelper.fetchSearchResults(ksReq);
					dsplyRslts = true;
				}
				else {
					//Build query URL with multiple parameters
					String queryURL = buildSearchURL(request);
					boolean isEmptyQuery = StringUtils.isBlank(queryURL);
					//check for zip to set proper project
					if(submitZipCd){
						String userZipCd = request.getParameter(IPortletConstants.SEARCH_ZIPCODE);
						String locWithin = request.getParameter(IPortletConstants.VALUE_LOCATED_WITHIN);
						if (StringUtils.isNotBlank(userZipCd)) {
							userZipCd = FormatUtil.updateWithHTMLCharacters(userZipCd);
						}
						if(LOGGER.isDebugEnabled())LOGGER.debug("incoming zipcode - "+userZipCd);
						String searchKeyword = request.getParameter(IPortletConstants.SEARCHSTRING);
						if (StringUtils.isNotBlank(searchKeyword)) {
							searchKeyword = FormatUtil.updateWithHTMLCharacters(searchKeyword);
						}
						if(LOGGER.isDebugEnabled())LOGGER.debug("searchKeyword = "+searchKeyword);
						String regionFilter = (String)request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION_DUP);
						if(LOGGER.isDebugEnabled())LOGGER.debug("KaiserMGSearchPortlet--regionFilter--"+regionFilter);
						// checking to see if ROP available in portletSession. Added for TimeOut defect (DE3601)
						if(StringUtils.isNotBlank(regionFilter)){
							if(LOGGER.isDebugEnabled())LOGGER.debug("Valid session: ROP available");
							if(SearchStringValidator.isValidZipCode(userZipCd)){ // check for valid zipcode
								// get Doctor/Location project name from pPref
								String mgSelection = (String) request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH_LOCATION);
								if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(mgSelection)){
									ksReq.setSearchSources(pPref.getValue(PREF_FACDIR_PROXIMITY_SOURCE, ""));
									ksReq.setSourceProject(pPref.getValue(PREF_FACDIR_PROJECT, ""));
								}else if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
									ksReq.setSearchSources(pPref.getValue(PREF_DOCTOR_PROXIMITY_SOURCE, ""));
									ksReq.setSourceProject(pPref.getValue(PREF_DOCTOR_PROJECT, ""));
								}
								ksReq.setSearchRegionFilter(regionFilter);
								
								if (!isEmptyQuery) {
									queryURL=queryURL.replaceFirst("sources=kp-doctor", "sources="+pPref.getValue(PREF_DOCTOR_PROXIMITY_SOURCE, ""));
									queryURL=queryURL.replaceFirst("sources=kp-mg-facdir-keyword", "sources="+pPref.getValue(PREF_FACDIR_PROXIMITY_SOURCE, ""));
								}
								else if (providerTypeURL!=null){
									queryURL=providerTypeURL.replaceFirst("sources=kp-doctor", "sources="+pPref.getValue(PREF_DOCTOR_PROXIMITY_SOURCE, ""));
								}
								else if (pcpYESUrl!=null){
									queryURL=pcpYESUrl.replaceFirst("sources=kp-doctor", "sources="+pPref.getValue(PREF_DOCTOR_PROXIMITY_SOURCE, ""));
								}
								
								if(submitKeyWd && SearchStringValidator.isValidSearchString(searchKeyword,IPortletConstants.VAL_DEFAULT_MGSEARCHTEXT)){
									if (StringUtils.isNotBlank(queryURL)){
										if(LOGGER.isDebugEnabled())LOGGER.debug("incoming keyword - "+searchKeyword);
										searchKeyword = FormatUtil.urlEncode(searchKeyword);
										ksReq.setSearchKeyword(searchKeyword);
										queryURL=queryURL.replaceFirst("&query=", "&query="+searchKeyword);
									}
									else {
										searchKeyword = FormatUtil.urlEncode(searchKeyword);
										ksReq.setSearchKeyword(searchKeyword);
									}
								}
								
								if(LOGGER.isDebugEnabled())LOGGER.debug("Final zip query string is " + queryURL);
								if (StringUtils.isNotBlank(queryURL)){
									if (StringUtils.isNotBlank(locWithin)) {
										if(LOGGER.isDebugEnabled())LOGGER.debug("Adding radius parameter to queryURL.");
										queryURL=queryURL + "%0A%0A"+locWithin;
									}
									queryURL=queryURL + "&user_zip="+userZipCd;
									ksReq.setSearchRefreshUrl(queryURL);
								} else if (StringUtils.isNotBlank(locWithin)) {
									if(LOGGER.isDebugEnabled())LOGGER.debug("Adding radius parameter to ZIPCODE search.");
									ksReq.setSearchLocWithin(locWithin);
								}
								ksReq.setSearchZipCd(userZipCd);
								sHelper.fetchSearchResults(ksReq);
								dsplyRslts = true;
							}else{
								// If condition added to determine where user is current now.
								String currView = request.getParameter(IPortletConstants.SEARCH_VIEW);
								if(IPortletConstants.SEARCH_VIEW_RESULTSPAGE.equalsIgnoreCase(currView)){
									SearchCriteria scForZipError = new SearchCriteria();
									scForZipError.setProject(IPortletConstants.MGSELECTION_LOCATIONS);
									scForZipError.setRegionFilter(ROPMap.getRegionNameForCookie(regionFilter));
									scForZipError.setUserZipCode(userZipCd);
									request.setAttribute("SEARCH_GENERIC_VIEW_BEAN", scForZipError);
									dsplyRslts = true;
								}
								if(LOGGER.isDebugEnabled())LOGGER.debug("Invalid ZipCd: setting error attribute ->  >> ");
								request.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_ZIPCD);
							}
						}
					}
					else {
						//set search projects to non-zip sources
						if(LOGGER.isDebugEnabled())LOGGER.debug("Enter non zip");
						String searchKeyword = request.getParameter(IPortletConstants.SEARCHSTRING);
						if(LOGGER.isDebugEnabled())LOGGER.debug("searchKeyword = "+searchKeyword);
						if (StringUtils.isNotBlank(searchKeyword))
						{
							searchKeyword = FormatUtil.updateWithHTMLCharacters(searchKeyword);
						}
						String regionFilter = (String)request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION_DUP);
						if(LOGGER.isDebugEnabled())LOGGER.debug("KaiserMGSearchPortlet--regionFilter--"+regionFilter);
						// checking to see if ROP available in portletSession. Added for TimeOut defect (DE3601)
						if(StringUtils.isNotBlank(regionFilter)){
							if(LOGGER.isDebugEnabled())LOGGER.debug("Valid session: ROP available");
							
							// project name for now is facdir (for Location search).. Should be based on user selection when Doctors is added to MG Search.
							String mgSelection = (String) request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH_LOCATION);
							if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(mgSelection)){
								ksReq.setSearchSources(pPref.getValue(PREF_FACDIR_SOURCE, ""));
								ksReq.setSourceProject(pPref.getValue(PREF_FACDIR_PROJECT, ""));
							}else if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
								ksReq.setSearchSources(pPref.getValue(PREF_DOCTOR_SOURCE, ""));
								ksReq.setSourceProject(pPref.getValue(PREF_DOCTOR_PROJECT, ""));
							}						
							ksReq.setSearchRegionFilter(regionFilter);
							
							if (isEmptyQuery && providerTypeURL!=null){
								queryURL=providerTypeURL;
							}
							else if (isEmptyQuery && pcpYESUrl!=null){
								queryURL=pcpYESUrl;
							}
							
							if(submitKeyWd && SearchStringValidator.isValidSearchString(searchKeyword,IPortletConstants.VAL_DEFAULT_MGSEARCHTEXT)){
								if (StringUtils.isNotBlank(queryURL)){
									if(LOGGER.isDebugEnabled())LOGGER.debug("incoming keyword - "+searchKeyword);
									searchKeyword = FormatUtil.urlEncode(searchKeyword);
									ksReq.setSearchKeyword(searchKeyword);
									queryURL=queryURL.replaceFirst("&query=", "&query="+searchKeyword);
								}
								else {
									searchKeyword = FormatUtil.urlEncode(searchKeyword);
									ksReq.setSearchKeyword(searchKeyword);
									if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection))
										ksReq.setSortResultsBy("provider_sort+last_name");
								}
							}
							
							//doSearch for search Keyword. sort results
							if (StringUtils.isNotBlank(queryURL)){
								if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
									queryURL=queryURL + "&sortby=provider_sort&sortby=last_name";
								}else if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(mgSelection)){
									queryURL=queryURL + "&sortby=affiliated_facility&sortby=title";
								}
								ksReq.setSearchRefreshUrl(queryURL);
							}
							sHelper.fetchSearchResults(ksReq);
							dsplyRslts = true;
						}
					}
				}
			} // end refreshSearchURL conditional
			else if((StringUtils.isNotBlank(request.getParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE))) && (ropFromCookie!=null && ropFromCookie.equalsIgnoreCase(preRgCode)))	{
				AuxiliaryContent ac = null;
				
					
					String rgnCode =(String)request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
					String mgSelection =(String)request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH);

					if(rgnCode.equalsIgnoreCase(IPortletConstants.REGIONCD_COLORADO_NORTHERN)) {
						ac = sHelper.fetchRegionalContentFragments(IPortletConstants.REGIONCD_COLORADO_DENVER_BOULDER);
					}
					else {
						ac = sHelper.fetchRegionalContentFragments(rgnCode);
					}
					

					request.getPortletSession().setAttribute("CONTENTBEAN", ac);
					if((IPortletConstants.REGIONCD_MARYLAND_VIRGINIA_DC.equalsIgnoreCase(rgnCode) ) && 
							IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
						if(LOGGER.isDebugEnabled())LOGGER.debug("region is MID ATLANTIC or Northern CO : exception applied");
						request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_MID_EXCEPTION, Boolean.TRUE);
						request.getPortletSession().setAttribute(IPortletConstants.SEARCH_CONTENTREGION, ROPMap.updateRegionCodeForContentAPIs(rgnCode));
					}else{

						String providerTypeValue=request.getParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE);
						String userAction =request.getParameter(IPortletConstants.SEARCHREFRESH_USERACTION);
						if(userAction!=null){
						StringTokenizer stProvider = new StringTokenizer(userAction,"|");
						while (stProvider.hasMoreElements()) {
								userAction=(String)stProvider.nextElement();
							
						}
						}

						ksReq.setSearchRefreshUrl(providerTypeValue);	
						request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_MID_EXCEPTION, Boolean.FALSE);
						if(LOGGER.isDebugEnabled())LOGGER.debug("region not MID ATLANTIC: no exception applied");
						ksReq.setSearchSources(pPref.getValue(IPortletConstants.PREF_SEARCHSOURCES, ""));
						ksReq.setDisplayXMLFeedClone(pPref.getValue(IPortletConstants.PREF_SEARCHCUSTOM_BINONLY_DISPLAY, "xml-feed-display-wpp-mg-bins"));
						if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(mgSelection)){
							ksReq.setSearchSources(pPref.getValue(PREF_FACDIR_SOURCE, ""));
							ksReq.setSourceProject(pPref.getValue(PREF_FACDIR_PROJECT, ""));
						}else if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
							ksReq.setSearchSources(pPref.getValue(PREF_DOCTOR_SOURCE, ""));
							ksReq.setSourceProject(pPref.getValue(PREF_DOCTOR_PROJECT, ""));
						}
						if(LOGGER.isDebugEnabled())LOGGER.debug("KaiserMGSearchPortlet--rgnCode--"+rgnCode);
						
						ksReq.setSearchRegionFilter(rgnCode);
						ksReq.setSearchKeyword("");
						VivisimoXMLService vService = new VivisimoXMLService();
						try{
							
							KaiserSearchResponse ksResp = vService.search(ksReq);
							
							SearchHelper helper = new SearchHelper();
							SearchResultsView searchResultView = helper.processBinOnlyResults(ksResp);
							List<FacetGroup> selectableFG = searchResultView.getSelectableFacetGroups();
							Map<String, List<Facet>> mpFg = helper.convertFacetListtoMap(selectableFG);
							request.getPortletSession().setAttribute(IPortletConstants.GATED_PP,Boolean.TRUE);
							request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_ACTION,userAction);
							request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_ACTION,planGateAcion);
							request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_VALUE,providerTypeValue);
							request.getPortletSession().setAttribute("BROWSE_LIST", mpFg);
						}catch(Exception e){
							request.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_FAILURE);
							LOGGER.error("Exception occured: Redirecting to error page: "+e.getMessage());
						}
					}
				
			}
			else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCH_MGSELECTION))){
				
				if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming MGSelection & RegionFilter");
				//String rgnCode = request.getParameter(IPortletConstants.SEARCH_REGIONFILTER);
				String rgnCode =null;
				String mgSelection="";
				
				
				if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(request.getParameter(IPortletConstants.SEARCH_MGSELECTION))){
					 mgSelection = request.getParameter(IPortletConstants.SEARCH_MGSELECTION);
						String locRgnSelected = request.getParameter(IPortletConstants.SEARCH_LOC_FILTER);
					 	
					if(locRgnSelected!=null && !locRgnSelected.equals(IPortletConstants.VAL_DEFAULT_AREA_SELECT)){
						rgnCode=locRgnSelected;
					}
					if(request.getPortletSession().getAttribute("actionView")!=null){	
						request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION, rgnCode);
						request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION_DUP, rgnCode);
 						
					}else if(ropFromCookie!=null){
 							rgnCode=ropFromCookie;
						}
					LOGGER.debug(IPortletConstants.MGSELECTION_LOCATIONS+"---rgnCode-------------->"+rgnCode);
					
				}
				if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(request.getParameter(IPortletConstants.SEARCH_MGSELECTION))){
					 mgSelection = request.getParameter(IPortletConstants.SEARCH_MGSELECTION);
					 String docRgnSelected = request.getParameter(IPortletConstants.SEARCH_DOC_FILTER);
					 if(!docRgnSelected.equalsIgnoreCase((String)request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION))){
						 clearDocSession(request);
							
					 } else if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){

							if(StringUtils.isNotBlank(request.getParameter(IPortletConstants.GATED_PCP_RADIO))){
								String radioPCP = request.getParameter(IPortletConstants.GATED_PCP_RADIO);
								if(radioPCP.equalsIgnoreCase(IPortletConstants.GATED_PCP_NO_VALUE)){
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_NO_VALUE, radioPCP);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_YES_VALUE, null);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_YES_URL, null);
								}
								else{
									ksReq.setSearchRefreshUrl(radioPCP);
									request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_YES_URL, radioPCP);
									request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_YES_VALUE, IPortletConstants.GATED_PCP_YES_VALUE);
									request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_NO_VALUE, null);

								}
							}else{
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_NO_VALUE, null);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_YES_VALUE, null);
							}
					}
					if(docRgnSelected!=null && !docRgnSelected.equals(IPortletConstants.VAL_DEFAULT_AREA_SELECT)){
						rgnCode=docRgnSelected;
					}
					if(request.getPortletSession().getAttribute("actionView")!=null){	
						request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION, rgnCode);
						request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION_DUP, rgnCode);
 						}else if(ropFromCookie!=null){
 							rgnCode=ropFromCookie;
						}
					if(StringUtils.isBlank(request.getParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE))){
						request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_ACTION,null);
						request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_ACTION,null);
					}
					
					LOGGER.debug(IPortletConstants.MGSELECTION_DOCTORS+"---rgnCode-------------->"+rgnCode);


				}
				
				request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH, mgSelection);
				request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH_LOCATION, mgSelection);

				AuxiliaryContent ac = null;
				LOGGER.debug("doView--->rgnCode ----> "+rgnCode);
				
				if(rgnCode.equalsIgnoreCase(IPortletConstants.REGIONCD_COLORADO_NORTHERN)) {
					ac = sHelper.fetchRegionalContentFragments(IPortletConstants.REGIONCD_COLORADO_DENVER_BOULDER);
				}
				else {
					ac = sHelper.fetchRegionalContentFragments(rgnCode);
				}
				
				request.getPortletSession().setAttribute("CONTENTBEAN", ac);
				if((IPortletConstants.REGIONCD_MARYLAND_VIRGINIA_DC.equalsIgnoreCase(rgnCode) ) && 
						IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
					if(LOGGER.isDebugEnabled())LOGGER.debug("region is MID ATLANTIC or Northern CO : exception applied");
					request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_MID_EXCEPTION, Boolean.TRUE);
					request.getPortletSession().setAttribute(IPortletConstants.SEARCH_CONTENTREGION, ROPMap.updateRegionCodeForContentAPIs(rgnCode));
				}else{
					request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_MID_EXCEPTION, Boolean.FALSE);
					if(LOGGER.isDebugEnabled())LOGGER.debug("region not MID ATLANTIC: no exception applied");
					ksReq.setSearchSources(pPref.getValue(IPortletConstants.PREF_SEARCHSOURCES, ""));
					ksReq.setDisplayXMLFeedClone(pPref.getValue(IPortletConstants.PREF_SEARCHCUSTOM_BINONLY_DISPLAY, "xml-feed-display-wpp-mg-bins"));
					

					if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(mgSelection)){
						

						ksReq.setSearchSources(pPref.getValue(PREF_FACDIR_SOURCE, ""));
						ksReq.setSourceProject(pPref.getValue(PREF_FACDIR_PROJECT, ""));
					}else if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
						ksReq.setSearchSources(pPref.getValue(PREF_DOCTOR_SOURCE, ""));
						ksReq.setSourceProject(pPref.getValue(PREF_DOCTOR_PROJECT, ""));
					}
					if(LOGGER.isDebugEnabled())LOGGER.debug("KaiserMGSearchPortlet--rgnCode--"+rgnCode);
					ksReq.setSearchRegionFilter(rgnCode);
					
					ksReq.setSearchKeyword("");

					
					//doSearch for search Keyword 
					VivisimoXMLService vService = new VivisimoXMLService();
					try{
						KaiserSearchResponse ksResp = vService.search(ksReq);
						
						SearchHelper helper = new SearchHelper();
						SearchResultsView searchResultView = helper.processBinOnlyResults(ksResp);
						List<FacetGroup> selectableFG = searchResultView.getSelectableFacetGroups();
						Map<String, List<Facet>> mpFg = helper.convertFacetListtoMap(selectableFG);
						
						// Get value from portal config
						Boolean pcp = null;
						LOGGER.debug("mgSelection---------------------->"+mgSelection);

						if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
						List<Facet> lPCP = mpFg.get(IPortletConstants.FACET_TITLE_PCP_LABEL);
						List<Facet> lPlanType = mpFg.get(IPortletConstants.FACET_TITLE_PLAN_TYPE);
						List<Facet> lProvider = mpFg.get(IPortletConstants.FACET_TITLE_PROVIDER_TYPE_LABEL);
						request.getPortletSession().setAttribute("lPlanType", lPlanType);
						request.getPortletSession().setAttribute("lProvider", lProvider);

						
						LOGGER.debug(IPortletConstants.MGSELECTION_DOCTORS+"---lPCP-------------PCP--->"+lPCP);


						if(lPCP!=null){
							
	
						for(Facet fct:lPCP){
							
													
							LOGGER.debug(IPortletConstants.ACCEPTING_PATIENTS+"---fct.getTitle()-------------PCP--->"+fct.getTitle());

							if(IPortletConstants.ACCEPTING_PATIENTS.equalsIgnoreCase(fct.getTitle())){
								pcp = Boolean.TRUE;
								break;
							}
						}}
												
						LOGGER.debug(IPortletConstants.ACCEPTING_PATIENTS+"----------------PCP--->"+pcp);
						
						LOGGER.debug("Enter processAction");
						
						Boolean isPlanProviderType=getRegionGate(request,rgnCode);
						
						LOGGER.debug("isPlanProviderType------------------->"+isPlanProviderType);

						
						if(isPlanProviderType!=null && isPlanProviderType.booleanValue()){
							
							

							if((null!=lPlanType && lPlanType.size() > 0) && (null!=lProvider && lProvider.size() > 0)) {
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH, Boolean.TRUE);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_TYPE_SEARCH, Boolean.TRUE);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_TYPE_SEARCH, Boolean.TRUE);
							} else if(null!=lPlanType && lPlanType.size() > 0){
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH, Boolean.TRUE);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_TYPE_SEARCH, Boolean.TRUE);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_TYPE_SEARCH, Boolean.FALSE);
							}else if(null!=lProvider && lProvider.size() > 0){
								
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH, Boolean.TRUE);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_TYPE_SEARCH, Boolean.TRUE);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_TYPE_SEARCH, Boolean.FALSE);

							}else{
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH, Boolean.FALSE);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_TYPE_SEARCH, Boolean.FALSE);
								request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_TYPE_SEARCH, Boolean.FALSE);							}	
							
						
						}else{
							request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH, Boolean.FALSE);
						}
						
						
						LOGGER.debug("isPlanProviderType---------------------->"+isPlanProviderType+"---pcp--->"+pcp);
						
						// Making PCP search option available to all regions regardless of ProviderPlan Gate US35431
						/*if((isPlanProviderType!=null && isPlanProviderType.booleanValue()) || pcp==null){
							pcp= Boolean.FALSE;
						}*/
						request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP, pcp);
						}else{
							clearDocSession(request);
						}
						
						request.getPortletSession().setAttribute("BROWSE_LIST", mpFg);
					}catch(Exception e){
						request.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_FAILURE);
						LOGGER.error("Exception occured: Redirecting to error page: "+e.getMessage());
					}
				}
			}else{
				if(LOGGER.isDebugEnabled())LOGGER.debug("no request parameters, have to setup initial view");
				String regionFilter = (String) request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
				String mgSelection=(String) request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH);
				
				ropFromCookie = ROPMap.fetchRegionFilterFromCookie(sHelper.fetchROP());
				String rgnSelected=null;
				clearDocSession(request);
				if(request.getPortletSession().getAttribute("actionView")!=null){
				if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(mgSelection)){
					String locRgnSelected = request.getParameter(IPortletConstants.SEARCH_LOC_FILTER);
					 	
					if(locRgnSelected!=null && !locRgnSelected.equals(IPortletConstants.VAL_DEFAULT_AREA_SELECT)){
						rgnSelected=locRgnSelected;
					}
				}
				if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
					 String docRgnSelected = request.getParameter(IPortletConstants.SEARCH_DOC_FILTER);

					 if(docRgnSelected!=null && !docRgnSelected.equals(IPortletConstants.VAL_DEFAULT_AREA_SELECT)){
						 rgnSelected=docRgnSelected;
						}
				}
				if(rgnSelected==null){
					if (StringUtils.isBlank(request.getParameter(IPortletConstants.VALUE_STARTNEWSEARCH))){					
					if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)){
						request.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_DOC_RGNCD);
					}else if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(mgSelection)){
	 					request.setAttribute(IPortletConstants.REQ_RESULTERROR, IPortletConstants.ERROR_LOC_RGNCD);

					}
					}
				}
				request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION, null);
				}else {
					request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION, ropFromCookie);
				}

			

				
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("regionFilter = '" + regionFilter + "'");
					LOGGER.debug("ropFromCookie = '" + ropFromCookie + "'");
				}
				
				if(StringUtils.isBlank(regionFilter)){
					request.getPortletSession().setAttribute("CONTENTBEAN", sHelper.fetchNationalContentFragments());
				}else if(!regionFilter.equals(IPortletConstants.VAL_DEFAULT_AREA_SELECT) &&
						 !regionFilter.equals(ropFromCookie)){//in case of rop cookie updated outside of current Search feature
					if(LOGGER.isDebugEnabled())LOGGER.debug("ROP cookie updated outside of current Search feature");
					request.getPortletSession().setAttribute("BROWSE_LIST", null);
					request.getPortletSession().setAttribute("CONTENTBEAN", null);
					request.getPortletSession().setAttribute("CONTENTBEAN", sHelper.fetchIntroContentFragments());
				}else if((mgSelection == null) || ((request.getPortletSession().getAttribute("actionView") == null) 
													&& (IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(mgSelection)))){
					// Region is present, but MG selection is null. We are on the entry page with preselected ROP from cookie
					request.getPortletSession().setAttribute("BROWSE_LIST", null);
					request.getPortletSession().setAttribute("CONTENTBEAN", null);
					request.getPortletSession().setAttribute("CONTENTBEAN", sHelper.fetchNationalContentFragments());
				}
			}
			String searchLandingPage = pPref.getValue(IPortletConstants.PREF_SEARCHLANDINGPAGE, "");
			if(dsplyRslts){
				response.setContentType(request.getResponseContentType());
				PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(
				"/WEB-INF/jsp/Default_View.jsp");
				dispatcher.include(request, response);
			}else{
				response.setContentType(request.getResponseContentType());
				
				PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(searchLandingPage);
				dispatcher.include(request, response);
			}
		}
		else{
			response.setContentType(request.getResponseContentType());
			PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(
			"/WEB-INF/jsp/error.jsp");
			dispatcher.include(request, response);
		}
		request.getPortletSession().setAttribute("actionView", null);
		if(LOGGER.isDebugEnabled()) {
			debugDocSession(request, " - doView() - END");
		}
	}

	/**
	 * Helper method to serve up the edit_defaults custom mode.
	 */
	protected void doCustomEditDefaults(RenderRequest request, RenderResponse response)
	throws PortletException, IOException
	{
		response.setContentType("text/html");
		PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(
		"/WEB-INF/mgsearch/jsp/Default_Edit.jsp");
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
	 * Called by the portlet container to allow the portlet to process an action request. This
	 * method is called if the client request was originated by a URL created (by the portlet) with
	 * the RenderResponse.createActionURL() method.
	 */
	public void processAction(ActionRequest request, ActionResponse response)
	throws PortletException, IOException
	{
		if(LOGGER.isDebugEnabled())LOGGER.debug("Enter processAction");
		request.getPortletSession().setAttribute("actionView", "actionView");
		response.setRenderParameter("isSearchFromMG", "yes");
		SearchPortletHelper sHelper = new SearchPortletHelper(request);
		// search request
		// Updated to accommodate multiple search parameters. Moving specific logic around zipcode/keyword search
		// process inputs to determine requested search criteria as multiple parameters may be present
		
		if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCHREFRESHURL))){
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming new search URL: passing it to doView:"+request.getParameter(IPortletConstants.SEARCHREFRESHURL));
			response.setPortletMode(PortletMode.VIEW);
			response.setRenderParameter(IPortletConstants.SEARCHREFRESHURL, request.getParameter(IPortletConstants.SEARCHREFRESHURL));
			//sHelper.updateEventForWI(request.getParameter(IPortletConstants.SEARCHREFRESH_USERACTION), response);
			if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.FACETREFRESH))){
				response.setRenderParameter(request.getParameter(IPortletConstants.FACETREFRESH), request.getParameter(IPortletConstants.FACETREFRESH));
			}
			else{
				determineSearchParameters(request, response);
			}
		}
		else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.VALUE_STARTNEWSEARCH))){
			if(LOGGER.isDebugEnabled())LOGGER.debug("Start a new search, resetting queries...");
			response.setPortletMode(PortletMode.VIEW);
			response.setRenderParameter(IPortletConstants.SEARCHREFRESHURL, "");
			response.setRenderParameter(IPortletConstants.SEARCHSTRING, "");
			response.setRenderParameter(IPortletConstants.VALUE_STARTNEWSEARCH,request.getParameter(IPortletConstants.VALUE_STARTNEWSEARCH));
		}
		else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.ACTION_SUBMITZIPCD))){
			String submitZipCd = FormatUtil.updateWithHTMLCharacters(request.getParameter(IPortletConstants.ACTION_SUBMITZIPCD));
			String zipCd = request.getParameter(IPortletConstants.SEARCH_ZIPCODE);
			if (StringUtils.isNotBlank(zipCd)) {
				zipCd = FormatUtil.updateWithHTMLCharacters(zipCd);
			}
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming ACTION_SUBMITZIPCD: "+submitZipCd);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Start a proximity search, getting user entered zipcode..."+zipCd);
			response.setPortletMode(PortletMode.VIEW);
			response.setRenderParameter(IPortletConstants.ACTION_SUBMITZIPCD, submitZipCd);
			response.setRenderParameter(IPortletConstants.SEARCH_ZIPCODE, zipCd);
			response.setRenderParameter(IPortletConstants.SEARCHREFRESHURL, IPortletConstants.ACTION_SUBMITZIPCD);
			response.setRenderParameter(IPortletConstants.SEARCH_VIEW, request.getParameter(IPortletConstants.SEARCH_VIEW));
			sHelper.updateEventForWI("Zipcode|"+zipCd, response);
		}
		else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.ACTION_SUBMITQUERY))){
			String submitQuery = FormatUtil.updateWithHTMLCharacters(request.getParameter(IPortletConstants.ACTION_SUBMITQUERY));
			String keyword = request.getParameter(IPortletConstants.SEARCHSTRING);
			if (StringUtils.isNotBlank(keyword)) {
				keyword = FormatUtil.updateWithHTMLCharacters(keyword);
			}
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming ACTION_SUBMITQUERY: "+submitQuery);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming search keyword: passing it to doView:"+keyword);
			response.setPortletMode(PortletMode.VIEW);
			response.setRenderParameter(IPortletConstants.ACTION_SUBMITQUERY, submitQuery);
			response.setRenderParameter(IPortletConstants.SEARCHSTRING, keyword);
			response.setRenderParameter(IPortletConstants.SEARCH_VIEW, request.getParameter(IPortletConstants.SEARCH_VIEW));
			sHelper.updateEventForWI("Keyword|"+keyword, response);
		}
		else if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCH_MGSELECTION)))
				{
			String mgSelection = request.getParameter(IPortletConstants.SEARCH_MGSELECTION);
			
			//String rgnSelected = request.getParameter(IPortletConstants.SEARCH_REGIONFILTER);
			String rgnSelected=null;
			
			if(IPortletConstants.MGSELECTION_LOCATIONS.equalsIgnoreCase(request.getParameter(IPortletConstants.SEARCH_MGSELECTION))){
				String locRgnSelected = request.getParameter(IPortletConstants.SEARCH_LOC_FILTER);
				 	
				if(locRgnSelected!=null && !locRgnSelected.equals(IPortletConstants.VAL_DEFAULT_AREA_SELECT)){
					rgnSelected=locRgnSelected;
				}
			}
			if(IPortletConstants.MGSELECTION_DOCTORS.equalsIgnoreCase(request.getParameter(IPortletConstants.SEARCH_MGSELECTION))){
				 String docRgnSelected = request.getParameter(IPortletConstants.SEARCH_DOC_FILTER);

				 if(docRgnSelected!=null && !docRgnSelected.equals(IPortletConstants.VAL_DEFAULT_AREA_SELECT)){
					 rgnSelected=docRgnSelected;
				}
			}
			response.setRenderParameter(IPortletConstants.SEARCH_DOC_FILTER, request.getParameter(IPortletConstants.SEARCH_DOC_FILTER));
			response.setRenderParameter(IPortletConstants.SEARCH_LOC_FILTER, request.getParameter(IPortletConstants.SEARCH_LOC_FILTER));
			
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
				if(LOGGER.isDebugEnabled())LOGGER.debug("Perform search in WPP: Looking for "+mgSelection+" in "+rgnSelected);
				response.setPortletMode(PortletMode.VIEW);
				response.setRenderParameter(IPortletConstants.SEARCH_MGSELECTION, mgSelection);
				response.setRenderParameter(IPortletConstants.SEARCH_REGIONFILTER, rgnSelected);
				sHelper.updateEventForWI("SearchROP|"+rgnSelected, response);
			}else{ // not a valid region code coming in.. head to an error flow
				if(LOGGER.isDebugEnabled())LOGGER.debug("incoming regionCode invalid: "+rgnSelected);
				request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH, mgSelection);
				request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH_LOCATION, mgSelection);

				request.getPortletSession().setAttribute("BROWSE_LIST", null);
				request.getPortletSession().setAttribute("CONTENTBEAN", null);
				
			}
			if(StringUtils.isNotBlank(request.getParameter(IPortletConstants.GATED_PCP_RADIO))){
				response.setRenderParameter(IPortletConstants.GATED_PCP_RADIO,request.getParameter(IPortletConstants.GATED_PCP_RADIO));
			}
			if(StringUtils.isNotBlank(request.getParameter(IPortletConstants.VALUE_SUB_STARTNEWSEARCH))){
				response.setRenderParameter(IPortletConstants.VALUE_SUB_STARTNEWSEARCH,request.getParameter(IPortletConstants.VALUE_SUB_STARTNEWSEARCH));
			}
		}else if(StringUtils.isNotBlank(request.getParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE))){
			response.setRenderParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE,request.getParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE));
			if(request.getParameter(IPortletConstants.SEARCHREFRESH_USERACTION)!=null){
			response.setRenderParameter(IPortletConstants.SEARCHREFRESH_USERACTION,request.getParameter(IPortletConstants.SEARCHREFRESH_USERACTION));
			}else{
				response.setRenderParameter(IPortletConstants.SEARCHREFRESHURL,request.getParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE));
			}

		}
		else if(StringUtils.isNotBlank(request.getParameter(IPortletConstants.ACTION_SUBMITEDITUPDATES))){
			sHelper.persistMGSearchPreferences();
		}
	}	

	public void serveResource(ResourceRequest rReq, ResourceResponse rResp){
		LOGGER.debug("serveResource------->Start");
		SearchPortletHelper sHelper = new SearchPortletHelper();
		sHelper.helpServeResource(rReq, rResp);
		LOGGER.debug("serveResource------->End");
	}
	private Boolean getRegionGate(RenderRequest request,String rgnSelected){
		
		PortletPreferences pPref = request.getPreferences();
		Boolean blGateValue=null;
		String preValue=null;
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_NORTHERN_CALIFORNIA)){
			preValue=pPref.getValue(IPortletConstants.PREF_NCA_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_SOUTHERN_CALIFORNIA)){
			preValue=pPref.getValue(IPortletConstants.PREF_SCA_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_COLORADO_DENVER_BOULDER)){
			preValue=pPref.getValue(IPortletConstants.PREF_DB_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_COLORADO_NORTHERN)){
			preValue=pPref.getValue(IPortletConstants.PREF_NC_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_COLORADO_SOUTHERN)){
			preValue=pPref.getValue(IPortletConstants.PREF_CS_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_GEORGIA)){
			preValue=pPref.getValue(IPortletConstants.PREF_GGA_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_HAWAII)){
			preValue=pPref.getValue(IPortletConstants.PREF_HAW_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_MARYLAND_VIRGINIA_DC)){
			preValue=pPref.getValue(IPortletConstants.PREF_MID_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_OHIO)){
			preValue=pPref.getValue(IPortletConstants.PREF_OHI_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		if(rgnSelected.equalsIgnoreCase(IPortletConstants.REGIONCD_OREGON_WASHINGTON)){
			preValue=pPref.getValue(IPortletConstants.PREF_KNW_PLAN_PROVIDER_TYPE, "");
			
			if(preValue!=null){
				blGateValue= Boolean.valueOf(preValue);
			 }	
		}
		
		/* Switch Case for String Java 7
		switch(rgnSelected){
		case IPortletConstants.REGIONCD_NORTHERN_CALIFORNIA:
			String ncaChk=pPref.getValue(IPortletConstants.PREF_NCA_PLAN_PROVIDER_TYPE, "");
		}
		*/
		
		/*Map rgnValue = new HashMap<String, RegionStub>();
		RegionStub rgDB= new RegionStub();
		rgDB.setRegionCode("DB");
		rgDB.setPlanProviderType(true);
		RegionStub rgNCA= new RegionStub();
		rgNCA.setRegionCode("NCA");
		rgNCA.setPlanProviderType(false);
		RegionStub rgSCA= new RegionStub();
		rgSCA.setRegionCode("SCA");
		rgSCA.setPlanProviderType(true);
		rgnValue.put(rgSCA.getRegionCode(), rgSCA);
		rgnValue.put(rgDB.getRegionCode(), rgDB);
		rgnValue.put(rgNCA.getRegionCode(), rgNCA);*/
		return blGateValue;
	}
	
	private void determineSearchParameters(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		LOGGER.debug("Search Parameters-------->Start");
		boolean parameterError = true;
		SearchPortletHelper sHelper = new SearchPortletHelper(request);
		
		//City or Zip. Default to Zip search
		if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCH_ZIPCODE))){
			String zipCd = request.getParameter(IPortletConstants.SEARCH_ZIPCODE);
			String locWithin = request.getParameter(IPortletConstants.VALUE_LOCATED_WITHIN);
			zipCd = FormatUtil.updateWithHTMLCharacters(zipCd);
			
			response.setRenderParameter(IPortletConstants.SEARCH_ZIPCODE, zipCd);
			if (StringUtils.isNotBlank(locWithin)) 
				response.setRenderParameter(IPortletConstants.VALUE_LOCATED_WITHIN, locWithin);
			response.setRenderParameter(IPortletConstants.SEARCH_VIEW, request.getParameter(IPortletConstants.SEARCH_VIEW));
			sHelper.updateEventForWI("Zipcode|"+zipCd, response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Start a proximity search, getting user entered zipcode..."+zipCd);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Zipcode search distance within parameter..."+locWithin);
		}
		else if (StringUtils.isNotBlank(request.getParameter("CitySelect")) && !(request.getParameter("CitySelect")).equalsIgnoreCase(IPortletConstants.VALUE_SELECTCITY)){
			String [] values = request.getParameter("CitySelect").split("\\|");
			response.setRenderParameter(IPortletConstants.VALUE_SELECTCITY, values[0]);
			int start = values[0].lastIndexOf("=");
			response.setRenderParameter(IPortletConstants.VALUE_SUB_SELECTCITY, values[0].substring(start + 1));
			response.setRenderParameter(IPortletConstants.VALUE_FACET_SELECTCITY, values[1].replaceAll(" ", "+"));
			sHelper.updateEventForWI("City|"+values[1], response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("City value from form is " + request.getParameter("CitySelect"));
		}
		//Keyword
		if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCHSTRING)) && !(request.getParameter(IPortletConstants.SEARCHSTRING)).equalsIgnoreCase(IPortletConstants.VAL_DEFAULT_MGSEARCHTEXT)){
			String keyword = request.getParameter(IPortletConstants.SEARCHSTRING);
			keyword = FormatUtil.updateWithHTMLCharacters(keyword);
			
			response.setRenderParameter(IPortletConstants.SEARCHSTRING, keyword);
			response.setRenderParameter(IPortletConstants.SEARCH_VIEW, request.getParameter(IPortletConstants.SEARCH_VIEW));
			sHelper.updateEventForWI("Keyword|"+keyword, response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Incoming search keyword: passing it to doView:"+keyword);
			if(LOGGER.isDebugEnabled())LOGGER.debug("Keyword value from form is " + request.getParameter(IPortletConstants.SEARCHSTRING));
		}
		//Services
		if (StringUtils.isNotBlank(request.getParameter("ServicesSelect")) && !(request.getParameter("ServicesSelect")).equalsIgnoreCase(IPortletConstants.VALUE_SELECTSERVICES)){
			String [] values = request.getParameter("ServicesSelect").split("\\|");
			response.setRenderParameter(IPortletConstants.VALUE_SELECTSERVICES, values[0]);
			int start = values[0].lastIndexOf("=");
			response.setRenderParameter(IPortletConstants.VALUE_SUB_SELECTSPECIALTY, values[0].substring(start + 1));
			response.setRenderParameter(IPortletConstants.VALUE_FACET_SELECTSERVICES, values[1].replaceAll(" ", "+"));
			sHelper.updateEventForWI("Services|"+values[1], response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Services value from form is " + request.getParameter("ServicesSelect"));
		}
		//Specialty
		if (StringUtils.isNotBlank(request.getParameter("SpecialtySelect")) && !(request.getParameter("SpecialtySelect")).equalsIgnoreCase(IPortletConstants.VALUE_SELECTSPECIALTY)){
			String [] values = request.getParameter("SpecialtySelect").split("\\|");
			response.setRenderParameter(IPortletConstants.VALUE_SELECTSPECIALTY, values[0]);
			int start = values[0].lastIndexOf("=");
			response.setRenderParameter(IPortletConstants.VALUE_SUB_SELECTSPECIALTY, values[0].substring(start + 1));
			response.setRenderParameter(IPortletConstants.VALUE_FACET_SELECTSPECIALTY, values[1].replaceAll(" ", "+"));
			sHelper.updateEventForWI("Specialty|"+values[1], response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Specialty value from form is " + request.getParameter("SpecialtySelect"));
		}
		//Gender
		if (StringUtils.isNotBlank(request.getParameter("GenderSelect")) && !(request.getParameter("GenderSelect")).equalsIgnoreCase(IPortletConstants.VALUE_SELECTGENDER)){
			String [] values = request.getParameter("GenderSelect").split("\\|");
			response.setRenderParameter(IPortletConstants.VALUE_SELECTGENDER, values[0]);
			int start = values[0].lastIndexOf("=");
			response.setRenderParameter(IPortletConstants.VALUE_SUB_SELECTGENDER, values[0].substring(start + 1));
			response.setRenderParameter(IPortletConstants.VALUE_FACET_SELECTGENDER, values[1]);
			sHelper.updateEventForWI("Gender|"+values[1], response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Gender value from form is " + request.getParameter("GenderSelect"));
		}
		//Hospital Affiliation
		if (StringUtils.isNotBlank(request.getParameter("HospitalSelect")) && !(request.getParameter("HospitalSelect")).equalsIgnoreCase(IPortletConstants.VALUE_SELECTHOSPITAL)){
			String [] values = request.getParameter("HospitalSelect").split("\\|");
			response.setRenderParameter(IPortletConstants.VALUE_SELECTHOSPITAL, values[0]);
			int start = values[0].lastIndexOf("=");
			response.setRenderParameter(IPortletConstants.VALUE_SUB_SELECTHOSPITAL, values[0].substring(start + 1));
			response.setRenderParameter(IPortletConstants.VALUE_FACET_SELECTHOSPITAL, values[1].replaceAll(" ", "+"));
			sHelper.updateEventForWI("Hospital|"+values[1], response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Hospital Affiliation value from form is " + request.getParameter("HospitalSelect"));
		}
		//Language
		if (StringUtils.isNotBlank(request.getParameter("LanguageSelect")) && !(request.getParameter("LanguageSelect")).equalsIgnoreCase(IPortletConstants.VALUE_SELECTLANGUAGE)){
			String [] values = request.getParameter("LanguageSelect").split("\\|");
			response.setRenderParameter(IPortletConstants.VALUE_SELECTLANGUAGE, values[0]);
			int start = values[0].lastIndexOf("=");
			response.setRenderParameter(IPortletConstants.VALUE_SUB_SELECTLANGUAGE, values[0].substring(start + 1));
			response.setRenderParameter(IPortletConstants.VALUE_FACET_SELECTLANGUAGE, values[1].replaceAll(" ", "+"));
			sHelper.updateEventForWI("Language|"+values[1], response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Language value from form is " + request.getParameter("LanguageSelect"));
		}
		//Island
		if (StringUtils.isNotBlank(request.getParameter("IslandSelect")) && !(request.getParameter("IslandSelect")).equalsIgnoreCase(IPortletConstants.VALUE_SELECTISLAND)){
			String [] values = request.getParameter("IslandSelect").split("\\|");
			response.setRenderParameter(IPortletConstants.VALUE_SELECTISLAND, values[0]);
			int start = values[0].lastIndexOf("=");
			response.setRenderParameter(IPortletConstants.VALUE_SUB_SELECTISLAND, values[0].substring(start + 1));
			response.setRenderParameter(IPortletConstants.VALUE_FACET_SELECTISLAND, values[1].replaceAll(" ", "+"));
			sHelper.updateEventForWI("Island|"+values[1], response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Island value from form is " + request.getParameter("IslandSelect"));
		}
		//Provider Type
		if (StringUtils.isNotBlank(request.getParameter("ProviderSelect")) && !(request.getParameter("ProviderSelect")).equalsIgnoreCase(IPortletConstants.VALUE_SELECTPROVIDER)){
			String [] values = request.getParameter("ProviderSelect").split("\\|");
			response.setRenderParameter(IPortletConstants.VALUE_SELECTPROVIDER, values[0]);
			int start = values[0].lastIndexOf("=");
			response.setRenderParameter(IPortletConstants.VALUE_SUB_SELECTPROVIDER, values[0].substring(start + 1));
			response.setRenderParameter(IPortletConstants.VALUE_FACET_SELECTPROVIDER, values[1].replaceAll(" ", "+"));
			sHelper.updateEventForWI("Provider|"+values[1], response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Provider type value from form is " + request.getParameter("ProviderSelect"));
		}
		//Plan Type
		if (StringUtils.isNotBlank(request.getParameter("PlanSelect")) && !(request.getParameter("PlanSelect")).equalsIgnoreCase(IPortletConstants.VALUE_SELECTPLANTYPE)){
			String [] values = request.getParameter("PlanSelect").split("\\|");
			response.setRenderParameter(IPortletConstants.VALUE_SELECTPLANTYPE, values[0]);
			int start = values[0].lastIndexOf("=");
			response.setRenderParameter(IPortletConstants.VALUE_SUB_SELECTPLANTYPE, values[0].substring(start + 1));
			response.setRenderParameter(IPortletConstants.VALUE_FACET_SELECTPLANTYPE, values[1].replaceAll(" ", "+"));
			sHelper.updateEventForWI("Plan|"+values[1], response);
			parameterError = false;
			if(LOGGER.isDebugEnabled())LOGGER.debug("Plan type value from form is " + request.getParameter("PlanSelect"));
		}

		if (parameterError && StringUtils.isBlank((String)request.getPortletSession().getAttribute(IPortletConstants.GATED_PROVIDER_VALUE)) && StringUtils.isBlank((String)request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP_YES_URL)))
			response.setRenderParameter("ErrorCode", "Empty search parameters");
				
		LOGGER.debug("Search Parameters-------->End");
	}
	
	private String buildSearchURL(RenderRequest request){
		LOGGER.debug("Build the search URL-------->Start");
		
		StringBuilder sb = new StringBuilder();
		boolean firstParam = true;
		String startParam = "%3D%3D";
		String endParam = "%0A%0A";
		boolean submitZipCd = StringUtils.isNotBlank(request.getParameter(IPortletConstants.SEARCH_ZIPCODE));
		Iterator <String> it = request.getParameterMap().keySet().iterator();
		while (it.hasNext())
		{
			String key = it.next();
			//Zip and Keyword excluded because they do not form the proper search URL. Will be added later in processing.
			if (firstParam) {
				if (!submitZipCd && key.equals(IPortletConstants.VALUE_SELECTCITY)) {
					sb.append(request.getParameter(key) + endParam);
					firstParam = false;
				}
				else if (key.equals(IPortletConstants.VALUE_SELECTSERVICES) ||
					key.equals(IPortletConstants.VALUE_SELECTSPECIALTY) ||
					key.equals(IPortletConstants.VALUE_SELECTGENDER) ||
					key.equals(IPortletConstants.VALUE_SELECTHOSPITAL) ||
					key.equals(IPortletConstants.VALUE_SELECTLANGUAGE) ||
					key.equals(IPortletConstants.VALUE_SELECTISLAND) ||
					key.equals(IPortletConstants.VALUE_SELECTPLANTYPE) ||
					key.equals(IPortletConstants.VALUE_SELECTPROVIDER)){
					sb.append(request.getParameter(key) + endParam);
					firstParam = false;
				}
			}
			else
			{
				if (!submitZipCd && key.equals(IPortletConstants.VALUE_SELECTCITY)) {
					sb.append("city_label" + startParam + request.getParameter(IPortletConstants.VALUE_FACET_SELECTCITY) + endParam);
				}
				
				if (key.equals(IPortletConstants.VALUE_SELECTSERVICES)){
					sb.append("services" + startParam + request.getParameter(IPortletConstants.VALUE_FACET_SELECTSERVICES) + endParam);
				}
				if (key.equals(IPortletConstants.VALUE_SELECTSPECIALTY)){
					sb.append("medical_specialty_label" + startParam + request.getParameter(IPortletConstants.VALUE_FACET_SELECTSPECIALTY) + endParam);
				}
				if (key.equals(IPortletConstants.VALUE_SELECTGENDER)){
					sb.append("gender_label" + startParam + request.getParameter(IPortletConstants.VALUE_FACET_SELECTGENDER) + endParam);
				}
				if (key.equals(IPortletConstants.VALUE_SELECTHOSPITAL)){
					sb.append("hospital_label" + startParam + request.getParameter(IPortletConstants.VALUE_FACET_SELECTHOSPITAL) + endParam);
				}
				if (key.equals(IPortletConstants.VALUE_SELECTLANGUAGE)){
					sb.append("dr_language_label" + startParam + request.getParameter(IPortletConstants.VALUE_FACET_SELECTLANGUAGE) + endParam);
				}
				if (key.equals(IPortletConstants.VALUE_SELECTISLAND)){
					sb.append("island_label" + startParam + request.getParameter(IPortletConstants.VALUE_FACET_SELECTISLAND) + endParam);
				}
				if (key.equals(IPortletConstants.VALUE_SELECTPLANTYPE)){
					sb.append("health_plan_label" + startParam + request.getParameter(IPortletConstants.VALUE_FACET_SELECTPLANTYPE) + endParam);
				}
				if (key.equals(IPortletConstants.VALUE_SELECTPROVIDER)){
					sb.append("provider_label" + startParam + request.getParameter(IPortletConstants.VALUE_FACET_SELECTPROVIDER) + endParam);
				}
			}
			
		}
		LOGGER.debug("Build the Search URL-------->End");
		return sb.toString();

	}
	
	private void clearDocSession(RenderRequest request){
		LOGGER.debug("Clear the session-------->Start");
		if (StringUtils.isNotBlank(request.getParameter(IPortletConstants.VALUE_STARTNEWSEARCH))){
			//request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION, null); // commented as per judy's email region should be populated 
			request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH, null);
		}
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP, null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH, null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_TYPE_SEARCH, null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_TYPE_SEARCH, null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_NO_VALUE, null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_YES_VALUE, null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PCP_YES_URL, null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PP,null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_ACTION,null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PROVIDER_VALUE,null);
		request.getPortletSession().setAttribute(IPortletConstants.GATED_PLAN_ACTION, null);
		request.getPortletSession().setAttribute("lPlanType", null);
		request.getPortletSession().setAttribute("lProvider", null);
	
		LOGGER.debug("Clear the session-------->End");
		
	}
	private void debugDocSession(RenderRequest request, String where){
		LOGGER.debug("debugDocSession the session--------> " + where);
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH---->"+request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PLAN_TYPE_SEARCH---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PLAN_TYPE_SEARCH));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PROVIDER_TYPE_SEARCH---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PROVIDER_TYPE_SEARCH));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP_NO_VALUE---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP_NO_VALUE));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP_YES_VALUE---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP_YES_VALUE));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP_YES_URL---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PCP_YES_URL));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PP---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PP));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PROVIDER_ACTION---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PROVIDER_ACTION));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PROVIDER_VALUE---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PROVIDER_VALUE));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.GATED_PLAN_ACTION---->"+request.getPortletSession().getAttribute(IPortletConstants.GATED_PLAN_ACTION));
		LOGGER.debug("request.getPortletSession().getAttribute(lPlanType---->"+request.getPortletSession().getAttribute("lPlanType"));
		LOGGER.debug("request.getPortletSession().getAttribute(lProvider---->"+request.getPortletSession().getAttribute("lProvider"));
		LOGGER.debug("request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH_LOCATION---->"+request.getPortletSession().getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH_LOCATION));
	}
	private void debugRequestParameter(RenderRequest request){
		LOGGER.debug("debugRequestParameter the request-------->Start");
		LOGGER.debug("request.getParameter(isSearchFromMG)---->"+request.getParameter("isSearchFromMG"));
		LOGGER.debug("request.getParameter(IPortletConstants.SEARCHREFRESHURL)---->"+request.getParameter(IPortletConstants.SEARCHREFRESHURL));
		LOGGER.debug("request.getParameter(IPortletConstants.SEARCHSTRING)---->"+request.getParameter(IPortletConstants.SEARCHSTRING));
		LOGGER.debug("request.getParameter(IPortletConstants.VALUE_STARTNEWSEARCH)---->"+request.getParameter(IPortletConstants.VALUE_STARTNEWSEARCH));
		LOGGER.debug("request.getParameter(IPortletConstants.ACTION_SUBMITZIPCD)---->"+request.getParameter(IPortletConstants.ACTION_SUBMITZIPCD));
		LOGGER.debug("request.getParameter(IPortletConstants.SEARCH_ZIPCODE)---->"+request.getParameter(IPortletConstants.SEARCH_ZIPCODE));
		LOGGER.debug("request.getParameter(IPortletConstants.SEARCH_VIEW)---->"+request.getParameter(IPortletConstants.SEARCH_VIEW));
		LOGGER.debug("request.getParameter(IPortletConstants.ACTION_SUBMITQUERY)---->"+request.getParameter(IPortletConstants.ACTION_SUBMITQUERY));
		LOGGER.debug("request.getParameter(IPortletConstants.SEARCH_DOC_FILTER)---->"+request.getParameter(IPortletConstants.SEARCH_DOC_FILTER));
		LOGGER.debug("request.getParameter(IPortletConstants.SEARCH_LOC_FILTER)---->"+request.getParameter(IPortletConstants.SEARCH_LOC_FILTER));
		LOGGER.debug("request.getParameter(IPortletConstants.SEARCH_MGSELECTION)---->"+request.getParameter(IPortletConstants.SEARCH_MGSELECTION));
		LOGGER.debug("request.getParameter(IPortletConstants.SEARCH_REGIONFILTER)---->"+request.getParameter(IPortletConstants.SEARCH_REGIONFILTER));
		LOGGER.debug("request.getParameter(IPortletConstants.GATED_PCP_RADIO)---->"+request.getParameter(IPortletConstants.GATED_PCP_RADIO));
		LOGGER.debug("request.getParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE)---->"+request.getParameter(IPortletConstants.GATED_PROVIDER_TYPE_VALUE));
		LOGGER.debug("request.getParameter(IPortletConstants.SEARCHREFRESH_USERACTION)---->"+request.getParameter(IPortletConstants.SEARCHREFRESH_USERACTION));
		LOGGER.debug("debugRequestParameter the request-------->end");

		
	}
}
