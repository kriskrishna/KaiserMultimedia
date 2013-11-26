package org.kp.wpp.portlet.search.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.apache.commons.lang.StringUtils;
import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.beans.Facet;
import org.kp.wpp.portlet.search.beans.FacetGroup;
import org.kp.wpp.portlet.search.beans.SearchCriteria;
import org.kp.wpp.portlet.search.beans.SearchResultsView;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.helpers.SearchHelper;
import org.kp.wpp.portlet.search.helpers.SearchPortletHelper;
import org.kp.wpp.utils.search.service.VivisimoXMLService;
import org.kp.wpp.utils.search.service.objects.KaiserSearchRequest;
import org.kp.wpp.utils.search.service.objects.KaiserSearchResponse;

public class SearchUtil {

	private static final Log LOGGER = LogFactory.getLog(SearchUtil.class);

	public static String getSearchBaseURL(PortletRequest request) {
		String searchBaseURL = (String) request.getPortletSession()
				.getAttribute(IPortletConstants.SESSION_PARAM_SEARCH_URL);

		// Storing Search server base URL in Session to avoid repeated calls to
		// MBean
		if (StringUtils.isBlank(searchBaseURL)) {
			ConfigMBeanUtil mBeanReader = new ConfigMBeanUtil();
			searchBaseURL = mBeanReader
					.getProperty(IPortletConstants.REE_URL_PROPERTY);
			request.getPortletSession().setAttribute(
					IPortletConstants.SESSION_PARAM_SEARCH_URL, searchBaseURL);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Search server base URL from ConfigMBeanUtil = "
					+ searchBaseURL);
		}

		return searchBaseURL;
	}

	public static boolean doSearch(PortletRequest request, PortletConfig pConfig)
			throws PortletException, IOException {
		boolean dsplyRslts = false;
		String submitQuery = FormatUtil.updateWithHTMLCharacters(request
				.getParameter(IPortletConstants.ACTION_SUBMITQUERY), true);
		boolean submitKwd = StringUtils.isNotBlank(submitQuery);
		boolean submitZipCd = StringUtils.isNotBlank(request
				.getParameter(IPortletConstants.ACTION_SUBMITZIPCD));
		String searchRegionFilter = request
				.getParameter(IPortletConstants.SEARCH_REGIONFILTER);
		String submitAlphaBrowse = request
				.getParameter(IPortletConstants.ACTION_ALPHABROWSE);

		SearchPortletHelper sHelper = null;
		if (request instanceof ActionRequest) {
			sHelper = new SearchPortletHelper((ActionRequest) request);
		} else {
			sHelper = new SearchPortletHelper((RenderRequest) request);
		}
		// //PortletPreferences pPref = request.getPreferences();
		String refreshSearchURL = request
				.getParameter(IPortletConstants.SEARCHREFRESHURL);

		String currLocale = getCurrentLocale(request);
		LOGGER.debug("currLocale is:- " + currLocale);
		KaiserSearchRequest ksReq = getKaiserSearchRequest(request,
				getSearchBaseURL(request));

		// check if there is a refresh URL for current search, has to be done
		// before checking for empty search
		if (StringUtils.isNotBlank(refreshSearchURL)) {
			dsplyRslts = refreshSearch(request, sHelper, refreshSearchURL,
					ksReq);
		} else if (submitKwd) { // valid search string, proceed with search
			dsplyRslts = searchByKeyword(request, pConfig, submitQuery,
					sHelper, currLocale, ksReq);
		} else if (submitZipCd) {// zip code - proximity search
			dsplyRslts = searchByZip(request, sHelper, currLocale, ksReq);
		} else if (StringUtils.isNotBlank(searchRegionFilter)) {
			searchByRegion(request, currLocale, ksReq);
		} else if (StringUtils.isNotBlank(submitAlphaBrowse)) {
			dsplyRslts = searchByAlphabet(request, sHelper, currLocale, ksReq);
		} else {
			LOGGER.debug("doing searchByDefault now...");
			serachByDefault(request, sHelper, currLocale, ksReq);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("***** doSearch - dsplyRslts=" + dsplyRslts);
		}

		return dsplyRslts;
	}

	private static void serachByDefault(PortletRequest request,
			SearchPortletHelper sHelper, String currLocale,
			KaiserSearchRequest ksReq) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("preparing for default behaviour");
		}

		String browsePref = request.getPreferences().getValue(
				IPortletConstants.PREF_BROWSE_WO_RGNGATE, "");
		boolean browseFlag = (StringUtils.equalsIgnoreCase(browsePref, "Yes")) ? true
				: false;
		if (browseFlag) {// setting up landing page, browse for feature
			// without geo-triage
			if (LOGGER.isDebugEnabled()) {
				LOGGER
						.debug("setup initial view for feature without geo-triage but enable browse");
			}
			ksReq.setSearchSources(request.getPreferences().getValue(
					IPortletConstants.PREF_SEARCHSOURCES, ""));
			ksReq.setDisplayXMLFeedClone(request.getPreferences().getValue(
					IPortletConstants.PREF_SEARCHCUSTOM_BINONLY_DISPLAY,
					"xml-feed-display-wpp-mg-bins"));
			ksReq.setSourceProject(request.getPreferences().getValue(
					IPortletConstants.PREF_SEARCHPROJECT, ""));
			ksReq.setSearchLocale(currLocale);
			ksReq.setSearchKeyword("");
			VivisimoXMLService vService = new VivisimoXMLService();
			try {
				KaiserSearchResponse ksResp = vService.search(ksReq);
				SearchHelper helper = new SearchHelper();
				SearchResultsView searchResultView = helper
						.processBinOnlyResults(ksResp);
				List<FacetGroup> selectableFG = searchResultView
						.getSelectableFacetGroups();
				Map<String, List<Facet>> mpFg = helper
						.convertFacetListtoMap(selectableFG);
				request.getPortletSession().setAttribute("BROWSE_LIST", mpFg);
			} catch (Exception e) {
				request.setAttribute(IPortletConstants.REQ_RESULTERROR,
						IPortletConstants.ERROR_FAILURE);
				LOGGER.error("Exception occured: Redirecting to error page: "
						+ e.getMessage());
			}
		} else {
			if (LOGGER.isDebugEnabled())
				LOGGER
						.debug("setup initial view for feature respective of regionVal in session");
			String regionFilter = (String) request.getPortletSession()
					.getAttribute(
							IPortletConstants.SESSION_PARAM_SELECTED_REGION);
			String ropFromCookie = ROPMap.fetchRegionFilterFromCookie(sHelper
					.fetchROP());
			if (StringUtils.isBlank(regionFilter)) {
				request.getPortletSession().setAttribute(
						IPortletConstants.SESSION_PARAM_ROPCOOKIE,
						ropFromCookie);
			} else if (!regionFilter
					.equals(IPortletConstants.VAL_DEFAULT_AREA_SELECT)
					&& !regionFilter.equals(ropFromCookie)) {// checking if
				// there is an update ROP cookie..
				// If cookie was update outside of Search region dropdown,
				// clear all browsable links
				request.getPortletSession().setAttribute("BROWSE_LIST", null);
				request.getPortletSession().setAttribute(
						IPortletConstants.SESSION_PARAM_SELECTED_REGION, null);
				request.getPortletSession().setAttribute(
						IPortletConstants.SESSION_PARAM_ROPCOOKIE,
						ropFromCookie);
			}
		}
	}

	private static boolean searchByAlphabet(PortletRequest request,
			SearchPortletHelper sHelper, String currLocale,
			KaiserSearchRequest ksReq) {
		boolean dsplyRslts;
		String searchAlphabet = request
				.getParameter(IPortletConstants.SEARCH_ALPHABET);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Searching with alphabet - " + searchAlphabet);
		String regionFilter = (String) request.getPortletSession()
				.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
		String searchKeyword = IPortletConstants.PREFIX_QUERY_PARENT_LETTER
				+ searchAlphabet;
		searchKeyword = FormatUtil.urlEncode(searchKeyword);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("UTF-8 encoded keyWord - " + searchKeyword);
		ksReq.setSourceProject(request.getPreferences().getValue(
				IPortletConstants.PREF_SEARCHPROJECT, "kp-consumernet"));
		ksReq.setSearchSources(request.getPreferences().getValue(
				IPortletConstants.PREF_SEARCHALHPABETSOURCE, ""));
		ksReq.setSearchRegionFilter(regionFilter);
		ksReq.setSearchLocale(currLocale);
		ksReq.setSortResultsBy(IPortletConstants.SEARCH_ALPHASORTBY);
		ksReq.setSearchKeyword(searchKeyword);
		// doSearch for search Keyword
		sHelper.fetchSearchResults(ksReq);
		dsplyRslts = true;
		return dsplyRslts;
	}

	private static void searchByRegion(PortletRequest request,
			String currLocale, KaiserSearchRequest ksReq) {
		String rgnCode = request
				.getParameter(IPortletConstants.SEARCH_REGIONFILTER);
		String sourceProject = request.getPreferences().getValue(
				IPortletConstants.PREF_SEARCHPROJECT, "");
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Searching with regionFilter - " + rgnCode
					+ " sourceProject = " + sourceProject);
		if (StringUtils.equalsIgnoreCase(
				IPortletConstants.REGIONCD_COLORADO_NORTHERN, rgnCode)
				&& StringUtils.equalsIgnoreCase(
						IPortletConstants.SEARCH_COLLECTION_CLASSES,
						sourceProject)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("TEMP display content fragment for NoCo");
			}
			request.setAttribute(IPortletConstants.DISPLAY_NOCO_FRAG_FOR_HC,
					Boolean.TRUE.toString());
		} else {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Searching with regionFilter - " + rgnCode);
			// request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION,
			// rgnCode);
			// request.getPortletSession().setAttribute(IPortletConstants.SESSION_PARAM_ROPCOOKIE,
			// rgnCode);
			ksReq.setSearchSources(request.getPreferences().getValue(
					IPortletConstants.PREF_SEARCHSOURCES, ""));
			ksReq.setDisplayXMLFeedClone(request.getPreferences().getValue(
					IPortletConstants.PREF_SEARCHCUSTOM_BINONLY_DISPLAY,
					"xml-feed-display-wpp-mg-bins"));
			ksReq.setSourceProject(request.getPreferences().getValue(
					IPortletConstants.PREF_SEARCHPROJECT, ""));
			ksReq.setSearchRegionFilter(rgnCode);
			ksReq.setSearchLocale(currLocale);
			ksReq.setSearchKeyword("");
			VivisimoXMLService vService = new VivisimoXMLService();
			try {
				KaiserSearchResponse ksResp = vService.search(ksReq);
				SearchHelper helper = new SearchHelper();
				SearchResultsView searchResultView = helper
						.processBinOnlyResults(ksResp);
				List<FacetGroup> selectableFG = searchResultView
						.getSelectableFacetGroups();
				Map<String, List<Facet>> mpFg = helper
						.convertFacetListtoMap(selectableFG);
				request.getPortletSession().setAttribute("BROWSE_LIST", mpFg);
			} catch (Exception e) {
				request.setAttribute(IPortletConstants.REQ_RESULTERROR,
						IPortletConstants.ERROR_FAILURE);
				LOGGER.error("Exception occured: Redirecting to error page: "
						+ e.getMessage());
			}
		}
		request.getPortletSession().setAttribute(
				IPortletConstants.SESSION_PARAM_SELECTED_REGION, rgnCode);
		request.getPortletSession().setAttribute(
				IPortletConstants.SESSION_PARAM_ROPCOOKIE, rgnCode);
	}

	private static boolean searchByZip(PortletRequest request,
			SearchPortletHelper sHelper, String currLocale,
			KaiserSearchRequest ksReq) {

		boolean dsplyRslts = false;
		String userZipCd = FormatUtil.updateWithHTMLCharacters(request
				.getParameter(IPortletConstants.SEARCH_ZIPCODE), true);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Searching with zipCode - " + userZipCd);
		String regionFilter = (String) request.getPortletSession()
				.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
		// checking to see if ROP available in portletSession. Added for
		// TimeOut defect (DE3601)
		if (StringUtils.isNotBlank(regionFilter)) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Valid session: ROP available");
			if (SearchStringValidator.isValidZipCode(userZipCd)) {
				// check for valid zipcode
				// get Doctor/Location project name from pPref
				ksReq.setSourceProject(request.getPreferences().getValue(
						IPortletConstants.PREF_SEARCHPROJECT, ""));
				ksReq.setSearchSources(request.getPreferences().getValue(
						IPortletConstants.PREF_SEARCHPROXIMITYSOURCE, ""));
				ksReq.setSearchRegionFilter(regionFilter);
				ksReq.setSearchLocale(currLocale);
				ksReq.setSearchKeyword("");
				ksReq.setSearchZipCd(userZipCd);
				// doSearch for search Keyword
				sHelper.fetchSearchResults(ksReq);
				dsplyRslts = true;
			} else {
				// If condition added to determine where user is current
				// now.
				String currView = FormatUtil.updateWithHTMLCharacters(request
						.getParameter(IPortletConstants.SEARCH_VIEW));
				if (IPortletConstants.SEARCH_VIEW_RESULTSPAGE
						.equalsIgnoreCase(currView)) {
					SearchCriteria scForZipError = new SearchCriteria();
					scForZipError
							.setProject(IPortletConstants.MGSELECTION_LOCATIONS);
					scForZipError.setRegionFilter(ROPMap
							.getRegionNameForCookie(regionFilter));
					scForZipError.setUserZipCode(userZipCd);
					request.setAttribute("SEARCH_GENERIC_VIEW_BEAN",
							scForZipError);
					dsplyRslts = true;
				}

				if (LOGGER.isDebugEnabled()) {
					LOGGER
							.debug("Invalid ZipCd: setting error attribute ->  >> ");
				}

				request.setAttribute(IPortletConstants.REQ_RESULTERROR,
						IPortletConstants.ERROR_ZIPCD);
			}
		}
		return dsplyRslts;
	}

	private static boolean searchByKeyword(PortletRequest request,
			PortletConfig pConfig, String submitQuery,
			SearchPortletHelper sHelper, String currLocale,
			KaiserSearchRequest ksReq) {

		boolean dsplyRslts = false;
		String searchKeyword = FormatUtil.updateWithHTMLCharacters(request
				.getParameter(IPortletConstants.SEARCHSTRING), true);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Searching with searchKeyword - " + searchKeyword);
		String fmtR = "org.kp.wpp.search.properties."
				+ pConfig.getPortletName();
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Looking for default keyword in - " + fmtR);
		ResourceBundle rb = null;
		if (StringUtils.isNotBlank(currLocale)) {
			if (currLocale
					.equalsIgnoreCase(IPortletConstants.CGI_PARAM_ENGLISH)) {
				rb = ResourceBundle.getBundle(fmtR, new Locale("en"));
			} else if (currLocale
					.equalsIgnoreCase(IPortletConstants.CGI_PARAM_SPANISH)) {
				rb = ResourceBundle.getBundle(fmtR, new Locale("es"));
			}
		} else {
			rb = ResourceBundle.getBundle(fmtR);
		}
		String defaultKwd = "";
		if (null != rb) {
			defaultKwd = rb
					.getString(IPortletConstants.RESOURCE_ENTRY_DEFAULT_KEYWORD);
		}
		String regionFilter = (String) request.getPortletSession()
				.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
		// checking to see if ROP available in portletSession. Added for
		// TimeOut defect (DE3601)
		// if(StringUtils.isNotBlank(regionFilter)){
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Valid session: ROP available");
		boolean isValidSearchString = false;
		if (IPortletConstants.VAL_SUBMITQUERY_HCO_LINKS.equals(submitQuery)) {
			isValidSearchString = SearchStringValidator
					.isValidHCOSearchString(searchKeyword);
		} else {
			isValidSearchString = SearchStringValidator.isValidSearchString(
					searchKeyword, defaultKwd);
		}
		if (isValidSearchString) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("incoming keyword - " + searchKeyword);
			searchKeyword = FormatUtil.urlEncode(searchKeyword);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("UTF-8 encoded keyWord - " + searchKeyword);
			// ROP cookie for default region
			String defaultRegionToROP = request.getPreferences().getValue(
					IPortletConstants.PREF_DEFAULT_REGIONFACET_WITH_ROP, "");
			if ("YES".equalsIgnoreCase(defaultRegionToROP)) {
				searchKeyword = sHelper
						.updateForRegionOfPreference(searchKeyword);
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("encoded keyWord updated for ROP - "
							+ searchKeyword);
			}
			// project name for now is facdir (for Location search).. Should
			// be based on user selection when Doctors is added to MG
			// Search.
			ksReq.setSourceProject(request.getPreferences().getValue(
					IPortletConstants.PREF_SEARCHPROJECT, "kp-consumernet"));
			ksReq.setSearchSources(request.getPreferences().getValue(
					IPortletConstants.PREF_SEARCHSOURCES, ""));
			ksReq.setSearchRegionFilter(regionFilter);
			ksReq.setSearchLocale(currLocale);
			ksReq.setSearchKeyword(searchKeyword);
			// doSearch for search Keyword
			sHelper.fetchSearchResults(ksReq);
			dsplyRslts = true;
		} else {
			String currView = FormatUtil.updateWithHTMLCharacters(request
					.getParameter(IPortletConstants.SEARCH_VIEW));
			if (IPortletConstants.SEARCH_VIEW_RESULTSPAGE
					.equalsIgnoreCase(currView)) {
				dsplyRslts = true;
			}
			if (LOGGER.isDebugEnabled())
				LOGGER
						.debug("Invalid keyword: setting error attribute -> empty search >> ");
			// Display message: "Enter a keyword in the search box."
			request.setAttribute(IPortletConstants.REQ_RESULTERROR,
					IPortletConstants.ERROR_EMPTYSEARCH);
		}
		// }

		return dsplyRslts;
	}

	private static boolean refreshSearch(PortletRequest request,
			SearchPortletHelper sHelper, String refreshSearchURL,
			KaiserSearchRequest ksReq) {
		boolean dsplyRslts = false;
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Searching with refreshURL - " + refreshSearchURL);
		if (refreshSearchURL.equalsIgnoreCase("Select city")) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Empty CitySearch - " + refreshSearchURL);
			// apply error flow for City Blank Search
			request.setAttribute(IPortletConstants.REQ_RESULTERROR,
					IPortletConstants.ERROR_CITY);
		} else {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("incoming refreshSearchURL - " + refreshSearchURL);
			ksReq.setSearchRefreshUrl(refreshSearchURL);
			// doSearch for refresh Search URL
			sHelper.fetchSearchResults(ksReq);
			dsplyRslts = true;
		}
		return dsplyRslts;
	}

	private static String getCurrentLocale(PortletRequest request) {
		String currLocale = null;
		Boolean checkLangFromRequest = (Boolean) request
				.getAttribute(IPortletConstants.USE_LANGUAGE_FROM_REQUEST);
		LOGGER.debug("checkLangFromRequest= " + checkLangFromRequest);
		if (checkLangFromRequest != null) {
			Locale locale = request.getLocale();
			LOGGER.debug("locale= " + locale);
			if (locale != null){
				String language = locale.getLanguage();
				String country = locale.getCountry();
				LOGGER.debug("language= " + language);
				LOGGER.debug("country= " + country);
				if (StringUtils.isNotBlank(language)
						&& StringUtils.isNotBlank(country))
					currLocale = language.toLowerCase() + "-"
							+ country.toLowerCase();
			}
		} else {
			// Check if language is selected per User Preference(from action
			// phase),
			// if yes leave the user preference, else default to portlet
			// configured
			// locale
			String sessionLang = (String) request.getPortletSession()
					.getAttribute(
							IPortletConstants.SESSION_PARAM_SELECTED_LANGUAGE);
			LOGGER.debug("SessionLang= " + sessionLang);
			if (StringUtils.isNotBlank(sessionLang)) {
				currLocale = sessionLang;
			} else {
				String prefLocale = request.getPreferences().getValue(
						IPortletConstants.PREF_DEFAULT_LOCALE, null);
				LOGGER.debug("prefLocale= " + prefLocale);
				currLocale = prefLocale;
				request.getPortletSession().setAttribute(
						IPortletConstants.SESSION_PARAM_SELECTED_LANGUAGE,
						currLocale);
			}
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("creating language specific region view for "
					+ currLocale);
		return currLocale;
	}

	private static KaiserSearchRequest getKaiserSearchRequest(
			PortletRequest request, String searchBaseURL) {
		KaiserSearchRequest ksReq = new KaiserSearchRequest();
		PortletPreferences pPref = request.getPreferences();
		ksReq.setBaseUrl(searchBaseURL);
		ksReq.setDisplayXMLFeedClone(pPref.getValue(
				IPortletConstants.PREF_SEARCHCUSTOMDISPLAY,
				"xml-feed-display-wpp"));
		ksReq.setTotalResults(pPref.getValue(
				IPortletConstants.PREF_SEARCHTOTALRESULTS, ""));
		ksReq.setResultsPerPage(pPref.getValue(
				IPortletConstants.PREF_SEARCHRESULTSPERPAGE, ""));
		ksReq.setTimeOut(pPref.getValue(IPortletConstants.PREF_SEARCHTIMEOUT,
				""));
		ksReq.setSourceProject(pPref.getValue(
				IPortletConstants.PREF_SEARCHPROJECT, ""));
		ksReq.setSearchSources(pPref.getValue(
				IPortletConstants.PREF_SEARCHSOURCES, ""));
		// Cookie persistence maintained through code, to avoid issues with load
		// balanced environments.
		// Cookie set if present in Session
		ksReq
				.setSearchSessionIdentifier((String) request
						.getPortletSession()
						.getAttribute(
								IPortletConstants.SESSION_PARAM_COOKIE_IDENTIFIER));

		ConfigMBeanUtil mBeanReader = new ConfigMBeanUtil();
		// R4 Post release Code Fix - QC 997
		String connectTimeout = mBeanReader
				.getProperty(IPortletConstants.REE_CONNECT_TIMEOUT);
		String readTimeout = mBeanReader
				.getProperty(IPortletConstants.REE_READ_TIMEOUT);
		ksReq.setHttpConnecttimeOut(connectTimeout);
		ksReq.setHttpReadtimeOut(readTimeout);
		// R4 Post release Code Fix - QC 997

		return ksReq;
	}

}
