package org.kp.wpp.portlet.search.constants;

import javax.portlet.PortletMode;

public final class IPortletConstants
{
	public static final PortletMode CUSTOM_EDIT_DEFAULTS_MODE = new PortletMode("edit_defaults");
	// Cache JNDI
	public static final String CACHEDMAP_SERVICE_JNDI = "portletservice/org.kp.wpp.portletservice.ICachedMapPortletService";
	public static final String CACHEDMAP_SERVICE_ERROR = "Could not find cached map service using default search parameters";

	public static final String IMPREGIONCOOKIE = "ImpSessionRoP";
	public static final String EXPREGIONCOOKIE = "ExpSessionRoP";
	public static final String ROPREGIONCOOKIE = "regionOfPreference";
	public static final String COOKIE_DOMAIN = ".kaiserpermanente.org";

	public static final String SEARCHSTRINGRENDERPARM = "searchString";

	// search portlet session keys
	public static final String CURRENT_QUERY = "curQuery";
	public static final String CURRENT_REFINEMENT = "curRefinement";
	public static final String CURRENT_SELECTEDPAGE = "curSelectedPage";
	public static final String CURRENT_PAGEREFERENCE = "curPageReference";

	// search portlet request keys
	public static final String REQ_RESULTLIST = "rsltList";
	public static final String REQ_RESULTREFINEMENTS = "rsltRefine";
	public static final String REQ_RESULTREFINEPATH = "rsltRefpath";
	public static final String REQ_RESULTSUMMARY = "rsltSummary";
	public static final String REQ_RESULTPAGING = "rsltPaging";
	public static final String REQ_RESULTSELPAGE = "rsltSelPage";
	public static final String REQ_RESULTUSERMESSAGE = "rsltUserMsg";
	public static final String REQ_RESULTPARTIALMSG = "rsltPartMsg";
	public static final String REQ_RESULTERROR = "rsltError";

	public static final String CACHEMAPSEARCHSTRING = "searchString";

	// TODO:Add more comments here
	public static final String ACTION_SUBMITQUERY = "submitQuery";
	public static final String ACTION_SUBMITZIPCD = "submitZipCd";
	public static final String ACTION_SUBMITEDITUPDATES = "submitFinished";
	public static final String ACTION_ALPHABROWSE = "submitAlphaBrowse";
	public static final String SEARCH_FROM_FEATURE = "fromFeature";
	public static final String REFINEMENTLABEL = "refLabel";
	public static final String REFINEMENTTITLE = "refTitle";
	public static final String FACETREFRESH = "facetRefresh";

	// search portlet string validation values
	public static final String VAL_BOOLEANOR = " OR";
	public static final String VAL_BOOLEANAND = " AND";
	public static final String VAL_BOOLEANNOT = " NOT";
	public static final String VAL_DEFAULT_SEARCHTEXT = "Search our site";
	public static final String VAL_DEFAULT_MGSEARCHTEXT = "Search";
	public static final String VAL_DEFAULT_AREA_SELECT = "Select an area";
	public static final String VAL_EDITERROR = "editError";
	public static final String VAL_EDITPAGESIZE = "Invalid result page size, must be a value between 5 and 100, value entered: ";
	public static final String VAL_EDITTOTSIZE = "Invalid result pagination size, must be a value between 50 and 500, value entered: ";
	public static final String VAL_EDITURL = "URL cannot be empty";
	public static final String VAL_EDITSOURCE = "Source cannot be empty";
	public static final int DEFAULTLENGTH = 50;
	public static final String INVALID_SEARCHTIMEOUT = "Timeout is measured in milliseconds, please enter a numeric value between 6000 & 12000";

	// error codes and messages
	public static final String ERROR_OK = "";
	public static final String ERROR_EMPTYSEARCH = "empty search";
	public static final String ERROR_NORESULTS = "no results";
	public static final String ERROR_PARTIALRESULTS = "partial results";
	public static final String ERROR_FAILURE = "search failure";
	public static final String ERROR_CONFIGMSG = "Search failed, configuration error";
	public static final String ERROR_EMPTYSEARCHMSG = "Empty Search String";
	public static final String ERROR_INVALIDSEARCHMSG = "Invalid Search String: ";
	public static final String ERROR_CITY = "ERR001";
	public static final String ERROR_ZIPCD = "ERR002";
	public static final String ERROR_RGNCD = "ERR003";
	public static final String ERROR_PAGING = "ERR004";
	public static final String ERROR_SPECIALTY = "ERR005";
	public static final String ERROR_HOSPITAL = "ERR006";
	public static final String ERROR_PLANTYPE = "ERR007";
	public static final String ERROR_PROVIDER = "ERR008";
	public static final String ERROR_PLAN_TYPE = "ERR009";
	public static final String ERROR_DOC_RGNCD = "ERR010";
	public static final String ERROR_LOC_RGNCD = "ERR011";




	// search portlet edit forms values
	public static final String FIELD_BROWSE_WO_RGNGATE = "textBoxBrowseWithoutRegionGate";
	public static final String FIELD_SORTORDER_FACETS = "textBoxcustomFacetOrder";
	public static final String FIELD_DEFAULT_REGIONFACET_WITH_ROP = "textBoxDefaultToROP";
	public static final String FIELD_DEFAULT_LOCALE = "textBoxDefaultLocale";
	public static final String FIELD_HELPPAGEURL = "textBoxMGHelpPageURL";
	public static final String FIELD_GLOSSARYPAGEURL = "textBoxMGGlossaryPageURL";
	public static final String FIELD_SEARCHPROXIMITYSOURCE = "textBoxProximitySearchSource";
	public static final String FIELD_SEARCHALHPABETSOURCE = "textBoxAlphabetSearchSource";
	public static final String FIELD_SEARCHLANDINGPAGE = "textBoxsearchLandingPage";
	public static final String FIELD_SEARCHREGION = "textBoxsearchRegion";
	public static final String FIELD_SEARCHRESULTSPERPAGE = "textBoxsearchResultsPerPage";
	public static final String FIELD_SEARCHTIMEOUT = "textBoxsearchTimeout";
	public static final String FIELD_SEARCHTOTALRESULTS = "textBoxsearchTotalResults";
	public static final String FIELD_SEARCHCUSTOMDISPLAY = "textBoxsearchXMLDisplayClone";
	public static final String FIELD_SEARCHCUSTOM_BINONLY_DISPLAY = "textBoxsearchXMLDisplayBinsClone";
	public static final String FIELD_SEARCHPROJECT = "textBoxsearchXMLProject";
	public static final String FIELD_SEARCHSOURCES = "textBoxsearchXMLSources";
	public static final String FIELD_SEARCHSTYLES = "textBoxstyles";
	
	public static final String FIELD_NCA = "chkNCA";
	public static final String FIELD_SCA = "chkSCA";
	public static final String FIELD_DB = "chkDB";
	public static final String FIELD_NC = "chkNC";
	public static final String FIELD_CS = "chkCS";
	public static final String FIELD_GGA = "chkGGA";
	public static final String FIELD_HAW = "chkHAW";
	public static final String FIELD_MID = "chkMID";
	public static final String FIELD_OHI = "chkOHI";
	public static final String FIELD_KNW = "chkKNW";
	

	
	

	//Determines certain functionality in KaiserSearchPortlet - Common to all search features
	public static final String PREF_BROWSE_WO_RGNGATE = "BrowseWithoutRegionGate";
	public static final String PREF_SORTORDER_FACETS = "customFacetOrder";
	public static final String PREF_DEFAULT_REGIONFACET_WITH_ROP = "DefaultToROP";
	public static final String PREF_DEFAULT_LOCALE = "DefaultLocale";
	public static final String PREF_HELPPAGEURL = "help-url";
	public static final String PREF_GLOSSARYPAGEURL = "glossary-url";
	public static final String PREF_SEARCHPROXIMITYSOURCE = "ProximitySearchSource";
	public static final String PREF_SEARCHALHPABETSOURCE = "AlphabetSearchSource";
	public static final String PREF_SEARCHLANDINGPAGE = "searchLandingPage";
	public static final String PREF_SEARCHREGION = "searchRegion";
	public static final String PREF_SEARCHRESULTSPERPAGE = "searchResultsPerPage";
	public static final String PREF_SEARCHTIMEOUT = "searchTimeout";
	public static final String PREF_SEARCHTOTALRESULTS = "searchTotalResults";
	public static final String PREF_SEARCHCUSTOMDISPLAY = "searchXMLDisplayClone";
	public static final String PREF_SEARCHCUSTOM_BINONLY_DISPLAY = "searchXMLDisplayBinsClone";
	public static final String PREF_SEARCHPROJECT = "searchXMLProject";
	public static final String PREF_SEARCHSOURCES = "searchXMLSources";
	public static final String PREF_SEARCHINTROICON = "SearchIntroIcon";
	public static final String PREF_SEARCHSTYLES = "styles";
	//Used in HE Search
	public static final String PREF_SYMPTOM_CHECKER = "SymptomCheckerURL";
	//Used in DE Search
	public static final String PREF_DRUGENCY_NMCD = "NMCDURL";
	
	public static final String PREF_FACDIR_FACETORDER = "facdir-facet-order";
	public static final String PREF_DOCTOR_FACETORDER = "doctor-facet-order";

	public static final String PREF_NCA_PLAN_PROVIDER_TYPE = "ncaPlanProviderType";
	public static final String PREF_SCA_PLAN_PROVIDER_TYPE = "scaPlanProviderType";
	public static final String PREF_DB_PLAN_PROVIDER_TYPE = "dbPlanProviderType";
	public static final String PREF_NC_PLAN_PROVIDER_TYPE = "ncPlanProviderType";
	public static final String PREF_CS_PLAN_PROVIDER_TYPE = "csPlanProviderType";
	public static final String PREF_GGA_PLAN_PROVIDER_TYPE = "ggaPlanProviderType";
	public static final String PREF_HAW_PLAN_PROVIDER_TYPE = "hawPlanProviderType";
	public static final String PREF_MID_PLAN_PROVIDER_TYPE = "midPlanProviderType";
	public static final String PREF_OHI_PLAN_PROVIDER_TYPE = "ohiPlanProviderType";
	public static final String PREF_KNW_PLAN_PROVIDER_TYPE = "knwPlanProviderType";


	// search portlet Session keys
	public static final String SESSION_PARAM_COOKIE_IDENTIFIER = "LBSearchCookie";
	public static final String SESSION_PARAM_SEARCH_URL = "REESEARCHBASEURL";
	public static final String SESSION_PARAM_REGION_LIST = "REGIONLIST";
	public static final String SESSION_PARAM_SELECTED_REGION = "UserPrefREGION";
	public static final String SESSION_PARAM_SELECTED_REGION_DUP = "DupUserPrefREGION";
	public static final String SESSION_PARAM_SELECTED_LANGUAGE = "UserPrefLANGUAGE";
	public static final String SESSION_PARAM_SELECTED_MGSEARCH = "DocFacSelection";
	public static final String SESSION_PARAM_SELECTED_MGSEARCH_LOCATION = "LocSelection";
	public static final String SESSION_PARAM_ROPCOOKIE = "RegionOfPrefCookie";
	public static final String SESSION_PARAM_MID_EXCEPTION = "MidAtlanticRegionException";
	
	// Have to hardcode refinement key for the requirement in story "US13827".
	// ROP from cookie needs to be passed to Search as a refinement.
	public static final String REGION_REFINEMENT_KEY = "&binning-state=region_label==";

	public static final String SEARCH_ALPHASORTBY = "alpha_sort_title";
	public static final String SEARCH_ZIPCODE = "user_zip";
	public static final String SEARCH_ALPHABET = "search_alphabet";
	public static final String SEARCHSTRING = "searchString";
	public static final String SEARCH_RESULTS_LABEL = "searchLabel";
	public static final String SEARCH_REGIONFILTER = "searchRegionFilter";
	public static final String SEARCH_DOC_FILTER = "searchDoctorFilter";
	public static final String SEARCH_LOC_FILTER = "searchLocationFilter";
	
	
	
	public static final String SEARCH_LANGUAGESELECTOR = "searchLanguageSelector";
	public static final String SEARCH_MGSELECTION = "searchMGDocFacSelection";
	public static final String SEARCHREFRESHURL = "SEARCH_RESULTS_URL";
	public static final String REGION_FACET_KEY = "region";
	public static final String SEARCH_VIEW = "currSearchView";
	public static final String SEARCH_VIEW_LANDINGPAGE = "currSearchViewLanding";
	public static final String SEARCH_VIEW_RESULTSPAGE = "currSearchViewResults";
	public static final String SEARCHREFRESH_USERACTION = "SEARCH_RESULTS_USER_ACTION";
	public static final String SEARCHREFRESH_USERACTION_VALUE = "SEARCH_RESULTS_USER_ACTION_VALUE";
	public static final String SEARCH_CONTENTREGION = "searchContentRegion";
	
	public static final String VALUE_SELECTCITY = "SelectCity";
	public static final String VALUE_SELECTZIP = "SelectZip";
	public static final String VALUE_SELECTSPECIALTY = "SelectSpecialty";
	public static final String VALUE_SELECTHOSPITAL= "SelectHospital";
	public static final String VALUE_SELECTPLANTYPE= "SelectPlanType";
	public static final String VALUE_SELECTPROVIDER= "SelectProvider";
	public static final String VALUE_STARTNEWSEARCH = "StartNewSearch";
	public static final String VALUE_SELECTGENDER = "SelectGender";
	public static final String VALUE_SELECTLANGUAGE = "SelectLanguage";
	public static final String VALUE_SELECTISLAND = "SelectIsland";
	public static final String VALUE_SELECTSERVICES = "SelectServices";
	public static final String VALUE_FACET_SELECTCITY = "FacetCity";
	public static final String VALUE_FACET_SELECTSPECIALTY = "FacetSpecialty";
	public static final String VALUE_FACET_SELECTHOSPITAL= "FacetHospital";
	public static final String VALUE_FACET_SELECTGENDER = "FacetGender";
	public static final String VALUE_FACET_SELECTLANGUAGE = "FacetLanguage";
	public static final String VALUE_FACET_SELECTISLAND = "FacetIsland";
	public static final String VALUE_FACET_SELECTSERVICES = "FacetServices";
	public static final String VALUE_FACET_SELECTPLANTYPE= "FacetPlanType";
	public static final String VALUE_FACET_SELECTPROVIDER= "FacetProvider";
	public static final String VALUE_SUB_SELECTCITY = "SubCity";
	public static final String VALUE_SUB_SELECTSPECIALTY = "SubSpecialty";
	public static final String VALUE_SUB_SELECTHOSPITAL= "SubHospital";
	public static final String VALUE_SUB_SELECTGENDER = "SubGender";
	public static final String VALUE_SUB_SELECTLANGUAGE = "SubLanguage";
	public static final String VALUE_SUB_SELECTISLAND = "SubIsland";
	public static final String VALUE_SUB_SELECTSERVICES = "SubServices";
	public static final String VALUE_SUB_SELECTPLANTYPE= "SubPlanType";
	public static final String VALUE_SUB_SELECTPROVIDER= "SubProvider";
	public static final String VALUE_SUB_STARTNEWSEARCH = "SubNewSearch";
	
	public static final String CONTENT_EMPTY_TITLE = "NO TITLE";

	public static final String REE_URL_PROPERTY = "wpp.vivisimo.search.baseurl";
	public static final String REE_READ_TIMEOUT = "wpp.vivisimo.read.timeout";
	public static final String REE_CONNECT_TIMEOUT = "wpp.vivisimo.connect.timeout";
	public static final String REE_MGSEARCH_LOCATIONS_REDIRECT = "wpp.mgsearch.redirect.locations";

	public static final String AFFILIATED_FACILITY_TRUE = "YES";
	public static final String AFFILIATED_FACILITY_FALSE = "NO";
	public static final String AFFILIATED_PLAN_HOSPITAL = "Plan Hospital";
	public static final String MGSELECTION_DOCTORS = "DOCTORS";
	public static final String MGSELECTION_LOCATIONS = "LOCATIONS";

	public static final String LANGUAGE_ENGLISH= "ENGLISH";
	public static final String LANGUAGE_SPANISH= "SPANISH";

	// REGION NAMES used for backend querying, when rop involved
	public static final String REGION_COLORADO_DENVER_BOULDER = "Colorado - Denver / Boulder";
	public static final String REGION_COLORADO_NORTHERN = "Colorado - Northern Colorado";
	public static final String REGION_SOUTHERN_CALIFORNIA = "California - Southern";
	public static final String REGION_GEORGIA = "Georgia";
	public static final String REGION_HAWAII = "Hawaii";
	public static final String REGION_NORTHERN_CALIFORNIA = "California - Northern";
	public static final String REGION_OREGON_WASHINGTON = "Oregon / Washington";
	public static final String REGION_MARYLAND_VIRGINIA_DC = "Maryland / Virginia / Washington D.C.";
	public static final String REGION_COLORADO_SOUTHERN = "Colorado - Southern Colorado";
	public static final String REGION_OHIO = "Ohio";

	// div ids used for dynamically show/hide
	public static final String DOCTORS_SELECTION_REGION_DIV = "DoctorRegionSelect";
	public static final String LOCATIONS_SELECTION_REGION_DIV = "LocationRegionSelect";

	public static final String FACET_TITLE_CITY = "city";
	public static final String FACET_TITLE_SERVICES = "services";
	public static final String FACET_TITLE_DEPARTMENTS = "department type";
	public static final String FACET_TITLE_PROGRAM_TYPE = "program type";
	public static final String FACET_TITLE_HEALTH_TOPIC = "health topic";
	public static final String FACET_TITLE_PCP = "primary care physicians accepting new patients";
	//Q4 Search Engine Label Change
	public static final String FACET_TITLE_PCP_LABEL ="doctors accepting new patients";
	public static final String FACET_TITLE_PLAN_TYPE= "health plan";
	public static final String FACET_TITLE_PROVIDER_TYPE= "provider";
	//Q4 Search Engine Label Change
	public static final String FACET_TITLE_PROVIDER_TYPE_LABEL="provider type or medical group";
	public static final String FACET_TITLE_SPECIALTY = "medical specialty";
	public static final String FACET_TITLE_HOSPITAL = "hospital";
	//Q4 Search Engine Label Change
	public static final String FACET_TITLE_HOSPITAL_LABEL="hospital affiliation";
	public static final String FACET_TITLE_GENDER_LABEL="gender";
	public static final String FACET_TITLE_LANGUAGE_LABEL="spoken language";
	public static final String FACET_TITLE_ISLAND_LABEL="island";
	//2013 Q4 Search Engine Label Change
	public static final String FACET_TITLE_LOCATED_WITHIN_LABEL="located within";

	public static final String DEFAULT_MAPS_DIRECTIONS_URL = "/kpweb/facilitydir/map.do?link=tripplus&static_destination=1&destcity=<CITYNAME>&deststateProvince=<STATE_ABBREVIATION>&destaddress=<ESCAPED_STREET_ADDRESS>&rop=<LEGACY_REGION_CODE>";
	public static final String DEFAULT_SYMPTOM_CHECKER_URL = "https://members.kaiserpermanente.org/kpweb/symptoms.do";
	public static final String SYMPTOM_CHECKER_URL_SPANISH = "/health/poc?uri=content:health-encyclopedia&hwid=kp100000&locale=es-us&ctype=kb";
	public static final String DEFAULT_DRUGENCY_NMCD_URL = "https://members.kaiserpermanente.org/kpweb/ShortCutToFullURL?segment=members&target=/redirects/naturalmedicines/int.htm";

	public static final String CONTENT_NODE_TYPE_FRAGMENT = "embedded_fragment";
	public static final String CONTENT_TYPE_ANNOUNCEMENT = "announcement";
	public static final String CONTENT_TYPE_DISCLAIMER = "disclaimer";
	public static final String CONTENT_TYPE_RELATED_LINKS = "related_links";
	public static final String CONTENT_TYPE_RISK_MITIGATION = "risk_mitigation";
	public static final String CONTENT_TYPE_INTRO = "intro";
	
	// attributes for a content node from the search server response
	public static final String CONTENT_P_URL = "P_URL";
	public static final String CONTENT_SNIPPET = "SNIPPET";
	public static final String CONTENT_TITLE = "TITLE";
	public static final String CONTENT_AFFILIATED_FACILITY = "AFFILIATED_FACILITY";
	public static final String CONTENT_SERVICES_LIST = "SERVICES_LIST";
	public static final String CONTENT_PHONE_NUMBER = "PHONE_NUMBER";
	public static final String CONTENT_STATE = "STATE";
	public static final String CONTENT_CITY = "CITY";
	public static final String CONTENT_ST_ADDRESS = "STREET_ADDRESS";
	public static final String CONTENT_DISTANCE = "FAC_DISTANCE";
	public static final String CONTENT_DESCRIPTION = "DESCRIPTION";
	public static final String CONTENT_PROGRAM_TYPE = "PROGRAM_TYPE";
	public static final String CONTENT_FACILITY = "FACILITY";
	public static final String CONTENT_FACILITY_TYPE = "FACILITY_TYPE";

	public static final String CONTENT_PLAN_LIST = "PLAN_LIST";
	public static final String CONTENT_OFFICE_NAME = "OFFICE_NAME";
	public static final String CONTENT_OFFICE_ADDRESS1 = "OFFICE_ADDRESS1";
	public static final String CONTENT_OFFICE_ADDRESS2 = "OFFICE_ADDRESS2";
	public static final String CONTENT_MED_SPECIALTY = "MEDSPECIALTY_DISPLAY";
	public static final String CONTENT_ACCEPTING_PATIENTS = "PCP_STATUS";
	public static final String CONTENT_PHOTO_URL = "PHOTO_URL";
	
	public static final String CGI_PARAM_ENGLISH = "en-us";
	public static final String CGI_PARAM_SPANISH = "es-us";
	
	public static final String RESOURCE_ENTRY_DEFAULT_KEYWORD = "search.index.default.keyword";
	
	public static final String VAL_SUBMITQUERY_HCO_LINKS = "HCOLINKFORENCYCLOPEDIA";
	public static final String VAL_SUBMITQUERY_ALPHABET_BROWSE = "alphabetSearch";

	public static final String PREFIX_QUERY_PARENT_LETTER = "parent_letter:";
	public static final String NUMERICAL_PARENT_LETTER = "0 to 9";
	
	// REGION CODES used for backend querying, when rop involved
	public static final String REGIONCD_COLORADO_DENVER_BOULDER = "DB";
	public static final String REGIONCD_SOUTHERN_CALIFORNIA = "SCA";
	public static final String REGIONCD_GEORGIA = "GGA";
	public static final String REGIONCD_HAWAII = "HAW";
	public static final String REGIONCD_NORTHERN_CALIFORNIA = "NCA";
	public static final String REGIONCD_OREGON_WASHINGTON = "KNW";
	public static final String REGIONCD_MARYLAND_VIRGINIA_DC = "MID";
	public static final String REGIONCD_COLORADO_SOUTHERN = "CS";
	public static final String REGIONCD_COLORADO_NORTHERN = "NC";
	public static final String REGIONCD_OHIO = "OHI";
	
	public static final String SEARCH_COLLECTION_FACDIR = "kp-mg-facdir-project";
	public static final String SEARCH_COLLECTION_DOCTOR = "kp-doctor-project";
	public static final String SEARCH_COLLECTION_HENCY = "kp-healthency-project";
	public static final String SEARCH_COLLECTION_DRUGENCY = "kp-drugency-project";
	public static final String SEARCH_COLLECTION_CLASSES = "kp-classes-project";
	
	public static final String SEARCH_FEATURE_FACDIR = "Locations";
	public static final String SEARCH_FEATURE_DOCTOR = "Doctors";
	public static final String SEARCH_FEATURE_HENCY = "Health encyclopedia";
	public static final String SEARCH_FEATURE_DRUGENCY = "Drug encyclopedia";
	public static final String SEARCH_FEATURE_CLASSES = "Health classes";
	
	public static final String DISPLAY_NOCO_FRAG_FOR_HC = "displayContentFragforHCforNC";
	public static final String DISPLAY_RESULTS = "displyResults";
	
	public static final String GATED_PLAN_PROVIDER_TYPE_SEARCH = "GatedPlanProviderSearch";
	public static final String GATED_PROVIDER_TYPE_SEARCH = "GatedProviderSearch";
	public static final String GATED_PLAN_TYPE_SEARCH = "GatedPlanSearch";
	public static final String GATED_PLAN_SEARCH_URL = "GatedPlanSearchURL";
	public static final String GATED_PCP = "pcpGate";
	public static final String ACCEPTING_PATIENTS = "Yes";
	public static final String GATED_PCP_NO_VALUE = "NO";
	public static final String GATED_PCP_YES_VALUE = "YES";
	public static final String GATED_PCP_RADIO ="radioPCP";
	public static final String GATED_PROVIDER_TYPE_VALUE = "GatedProvideTypeValue";
	public static final String  GATED_PP ="ppGate";
	public static final String GATED_PCP_YES_URL = "pcpYESUrl";
	public static final String GATED_PROVIDER_VALUE = "providerGateValue";
	public static final String GATED_PROVIDER_ACTION = "providerGateAcion";
	public static final String GATED_PLAN_ACTION = "planGateAcion";

	public static final String USE_LANGUAGE_FROM_REQUEST = "useLanguageFromRequest";
	
	//2013 Q4 Radius selection capability parameters
	public static final String VALUE_LOCATED_WITHIN = "LocatedWithin";
	public static final String LOCATED_WITHIN_5_MILES = "5 Miles";
	public static final String LOCATED_WITHIN_5_MILES_VAL = "distance%3d0%3a6";
	public static final String LOCATED_WITHIN_5_MILES_LBL = "distance=0:6";
	public static final String LOCATED_WITHIN_10_MILES = "10 Miles";
	public static final String LOCATED_WITHIN_10_MILES_VAL = "distance%3d0%3a11";
	public static final String LOCATED_WITHIN_10_MILES_LBL = "distance=0:11";
	public static final String LOCATED_WITHIN_25_MILES = "25 Miles";
	public static final String LOCATED_WITHIN_25_MILES_VAL = "distance%3d0%3a26";
	public static final String LOCATED_WITHIN_25_MILES_LBL = "distance=0:26";
	public static final String LOCATED_WITHIN_50_MILES = "50 Miles";
	public static final String LOCATED_WITHIN_50_MILES_VAL = "distance%3d0%3a51";
	public static final String LOCATED_WITHIN_50_MILES_LBL = "distance=0:51";

}
