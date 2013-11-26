package org.kp.wpp.portlet.search.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.portlet.PortletPreferences;

import org.apache.commons.lang.StringUtils;
import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.beans.Content;
import org.kp.wpp.portlet.search.beans.Facet;
import org.kp.wpp.portlet.search.beans.FacetGroup;
import org.kp.wpp.portlet.search.beans.PageLink;
import org.kp.wpp.portlet.search.beans.SearchCriteria;
import org.kp.wpp.portlet.search.beans.SearchResultsView;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.utils.FormatUtil;
import org.kp.wpp.portlet.search.utils.ROPMap;
import org.kp.wpp.utils.search.service.objects.Bin;
import org.kp.wpp.utils.search.service.objects.Binning;
import org.kp.wpp.utils.search.service.objects.BinningSet;
import org.kp.wpp.utils.search.service.objects.BinningStateToken;
import org.kp.wpp.utils.search.service.objects.Boost;
import org.kp.wpp.utils.search.service.objects.Document;
import org.kp.wpp.utils.search.service.objects.KaiserSearchResponse;
import org.kp.wpp.utils.search.service.objects.Link;
import org.kp.wpp.utils.search.service.objects.Navigation;

public class SearchHelper {
	
	private static final Log LOGGER = LogFactory.getLog(SearchHelper.class);
	public SearchResultsView processProviderTypeResults(KaiserSearchResponse ksResp){
		SearchResultsView searchResultsView = new SearchResultsView();
		List<Document> lstDocuments = ksResp.getLstDocuments();

		if (LOGGER.isDebugEnabled())LOGGER.debug("Search Results available creating beans for display");
		int iPageSize = FormatUtil.convertStringtoInt(ksResp.getContentsPerPage());
		int curStartIndex = FormatUtil.convertStringtoInt(ksResp.getCurPageStartIndex());
		List<FacetGroup> lFacetGroups = processBinnings(ksResp.getBinning());
		List<FacetGroup> selectedFacetGroups = null;
		List<FacetGroup> selectableFacetGroups = null;
		HashMap<String,FacetGroup> facetGroupMap = new HashMap<String,FacetGroup>();
		if(null!=lFacetGroups){
			selectedFacetGroups = new ArrayList<FacetGroup>();
			selectableFacetGroups = new ArrayList<FacetGroup>();
			for(FacetGroup fG: lFacetGroups){
				boolean facetIsAlreadySelected = false;
				for(Facet curFacet: fG.getFacets()){
					if(curFacet.isSelected()){
						facetIsAlreadySelected = true;
					}
				}
				if(facetIsAlreadySelected){
					if (LOGGER.isDebugEnabled())LOGGER.debug(fG.getFacetKey()+" is selected");
					selectedFacetGroups.add(fG);
				}else{
					if (LOGGER.isDebugEnabled())LOGGER.debug(fG.getFacetKey()+" is not selected");
					selectableFacetGroups.add(fG);
				}
				// map created: used for sorting Facet Groups per feature sort order 
				facetGroupMap.put(fG.getFacetKey(),fG);
			}
		}
		searchResultsView.setSelectableFacetGroups(selectableFacetGroups);
		searchResultsView.setSelectedFacetGroups(selectedFacetGroups);
		searchResultsView.setNavigationLinks(processNavigationLinks(ksResp.getNav(), iPageSize));
		//searchResultsView.setContents(processDocuments(ksResp.getLstDocuments(),ksResp.getSearchRegionFilter(),curStartIndex));
		return searchResultsView;		
	}
	public SearchResultsView processSearchResults(KaiserSearchResponse ksResp, PortletPreferences pPref){
		if (LOGGER.isDebugEnabled())LOGGER.debug("processSearchResults >> " + ksResp.getSearchKeyword());
		SearchResultsView searchResultsView = new SearchResultsView();
		SearchCriteria searchCriteria = new SearchCriteria();
		String searchProj = ksResp.getSearchProject();
		searchCriteria = processSpellCheck(ksResp,searchCriteria);
		List<Document> lstDocuments = ksResp.getLstDocuments();
		if(null!=lstDocuments){
			if (LOGGER.isDebugEnabled())LOGGER.debug("Search Results available creating beans for display");
			int iPageSize = FormatUtil.convertStringtoInt(ksResp.getContentsPerPage());
			int curStartIndex = FormatUtil.convertStringtoInt(ksResp.getCurPageStartIndex());
			List<FacetGroup> lFacetGroups = processBinnings(ksResp.getBinning());
			List<FacetGroup> selectedFacetGroups = null;
			List<FacetGroup> selectableFacetGroups = null;
			HashMap<String,FacetGroup> facetGroupMap = new HashMap<String,FacetGroup>();
			if(null!=lFacetGroups){
				selectedFacetGroups = new ArrayList<FacetGroup>();
				selectableFacetGroups = new ArrayList<FacetGroup>();
				for(FacetGroup fG: lFacetGroups){
					boolean facetIsAlreadySelected = false;
					for(Facet curFacet: fG.getFacets()){
						if(curFacet.isSelected()){
							facetIsAlreadySelected = true;
						}
					}
					if(facetIsAlreadySelected){
						if (LOGGER.isDebugEnabled())LOGGER.debug(fG.getFacetKey()+" is selected");
						selectedFacetGroups.add(fG);
					}else{
						if (LOGGER.isDebugEnabled())LOGGER.debug(fG.getFacetKey()+" is not selected");
						selectableFacetGroups.add(fG);
					}
					// map created: used for sorting Facet Groups per feature sort order 
					facetGroupMap.put(fG.getFacetKey(),fG);
				}
			}
			String prefSortOrder = getPreferenceNameForFacetOrder(searchProj);
			String aPref[] = pPref.getValues(prefSortOrder, null);
			// custom sort order available, to be sorted by preferred order
			if(null != aPref){
				List<String> preferredSortOrder = Arrays.asList(aPref);
				//sort facetgroups in the specified order
				List<FacetGroup> sortSelectableFacetGroups = sortFacetGroupList(selectableFacetGroups,preferredSortOrder);
				searchResultsView.setSelectableFacetGroups(sortSelectableFacetGroups);
				//sort facetgroups in the specified order
				List<FacetGroup> sortSelectedFacetGroups = sortFacetGroupList(selectedFacetGroups,preferredSortOrder);
				searchResultsView.setSelectedFacetGroups(sortSelectedFacetGroups);
			}else{
				// no custom sort order available, no sorting done
				searchResultsView.setSelectableFacetGroups(selectableFacetGroups);
				searchResultsView.setSelectedFacetGroups(selectedFacetGroups);
			}
			searchResultsView.setNavigationLinks(processNavigationLinks(ksResp.getNav(), iPageSize));
			searchResultsView.setContents(processDocuments(ksResp.getLstDocuments(),ksResp.getSearchRegionFilter(),curStartIndex));
			searchCriteria.setSummaryDisplayFlag(true);
		}
		searchCriteria.setKeyword(FormatUtil.updateWithHTMLCharacters(ksResp.getSearchKeyword()));
		searchCriteria.setResultsCount(String.valueOf(ksResp.getTotalRecords()));
		String projectDisplayName = getDisplayNameForProject(searchProj);
		searchCriteria.setProject(projectDisplayName);
		searchCriteria.setRegionFilter(ROPMap.getRegionNameForCookie(ksResp.getSearchRegionFilter()));		
		searchCriteria.setUserZipCode(ksResp.getSearchZipCd());
		searchResultsView.setSearchCriteria(searchCriteria);
		if (LOGGER.isDebugEnabled())LOGGER.debug("<< processSearchResults ");
		return searchResultsView;		
	}	
	
	protected SearchCriteria processSpellCheck(KaiserSearchResponse ksResp, SearchCriteria searchCriteria){
		Boost b = ksResp.getBoost();
		if(null != b){
			if (LOGGER.isDebugEnabled())LOGGER.debug("BOOST PRESENT - SPELLING CORRECTION APPLIED");
			searchCriteria.setSpellCorrectedHref(b.getSpellingCorrectionHref());
			searchCriteria.setSpellCorrectedKeyword(b.getSpellingCorrectionKeyword());
			searchCriteria.setSummaryDisplayFlag(true);
		}
		return searchCriteria;
	}
	
	protected List<PageLink> processNavigationLinks(Navigation nav, int iPageSize){
		if (LOGGER.isDebugEnabled())LOGGER.debug("processNavigationLinks >> ");
		List<PageLink> lPages = null;
		if(null != nav){
			lPages = new ArrayList<PageLink>();
			for(Link navLink:nav.getLstLinks()){
				if(StringUtils.isBlank(navLink.getType())){
					PageLink pageLink = new PageLink();
					int iPageStart = FormatUtil.convertStringtoInt(navLink.getStart());
					pageLink.setHref(nav.getBaseURL()+navLink.getValue());
					// TODO: get the page size of from PREFERENCEs
					String title = StringUtils.isEmpty(navLink.getType())?Integer.toString(iPageStart/iPageSize+1):navLink.getType();
					// TODO: revisit upper casing NEXT/PREVIOUS page links
					title = Character.toUpperCase(title.charAt(0)) + title.substring(1);
					pageLink.setTitle(title);
					lPages.add(pageLink);
				}
			}
		}
		return lPages;
	}
	
	/**
	 * 
	 * @param lstDocuments
	 * @param rop
	 * @return
	 */
	protected List<Content> processDocuments(List<Document> lstDocuments, String rop, int iStartIndex){
		if (LOGGER.isDebugEnabled())LOGGER.debug("processDocuments >> ");
		List<Content> lContents = null; 
		if(null != lstDocuments && lstDocuments.size() > 0){
			// converts the list of Documents to list of Content beans for display
			lContents = new ArrayList<Content>();
			int index = iStartIndex;
			for(Document doc:lstDocuments){
				Content cnt = new Content();
				HashMap<String, String> contMap = doc.getContentMap();
				cnt.setLinkValue(contMap.get(IPortletConstants.CONTENT_P_URL));
				cnt.setSnippet(contMap.get(IPortletConstants.CONTENT_SNIPPET));
				cnt.setTitle(FormatUtil.handleEmptyTitle(contMap.get(IPortletConstants.CONTENT_TITLE)));
				cnt.setSvcsList(contMap.get(IPortletConstants.CONTENT_SERVICES_LIST));
				cnt.setPhoneNumber(contMap.get(IPortletConstants.CONTENT_PHONE_NUMBER));
				cnt.setKpAffiliated(FormatUtil.convertToBoolean(contMap.get(IPortletConstants.CONTENT_AFFILIATED_FACILITY)));
				cnt.setFacilityType(contMap.get(IPortletConstants.CONTENT_FACILITY_TYPE));
				cnt.setPlanHospital(determineFacilityType(contMap.get(IPortletConstants.CONTENT_FACILITY_TYPE)));
				cnt.setDirectionsLink(createMapsDirectionsLink(doc, rop));
				cnt.setDistance(contMap.get(IPortletConstants.CONTENT_DISTANCE));
				cnt.setDescription(contMap.get(IPortletConstants.CONTENT_DESCRIPTION));
				cnt.setIndex(++index);
				cnt.setProgramType(contMap.get(IPortletConstants.CONTENT_PROGRAM_TYPE));
				cnt.setFacility(contMap.get(IPortletConstants.CONTENT_FACILITY));
				cnt.setCity(contMap.get(IPortletConstants.CONTENT_CITY));
				cnt.setMedOffice(contMap.get(IPortletConstants.CONTENT_OFFICE_NAME));
				cnt.setOfficeAddress1(contMap.get(IPortletConstants.CONTENT_OFFICE_ADDRESS1));
				cnt.setOfficeAddress2(contMap.get(IPortletConstants.CONTENT_OFFICE_ADDRESS2));
				cnt.setMedSpecialty(contMap.get(IPortletConstants.CONTENT_MED_SPECIALTY));
				cnt.setPlanList(contMap.get(IPortletConstants.CONTENT_PLAN_LIST));
				String acceptingNewPatients = contMap.get(IPortletConstants.CONTENT_ACCEPTING_PATIENTS);
				if(StringUtils.isNotBlank(acceptingNewPatients)){
					cnt.setAcceptingNewPatients(acceptingNewPatients);
				}
				String docPhotoUrl = contMap.get(IPortletConstants.CONTENT_PHOTO_URL);
				if(StringUtils.isNotBlank(docPhotoUrl)){
					cnt.setPhotoUrl(docPhotoUrl);
				}
				lContents.add(cnt);
			}
		}
		return lContents;
	}
	
	/** 
	 * 
	 * @param Facility Type coming in from the XML response: FACILITY_TYPE
	 * @return boolean Flag to represent if this facility is a Plan Hospital
	 */
	private boolean determineFacilityType(String facType){
		if (LOGGER.isDebugEnabled())LOGGER.debug("determineFacilityType >> "+facType);
		return (IPortletConstants.AFFILIATED_PLAN_HOSPITAL.equalsIgnoreCase(facType)) ? true : false;
	}
	
	/** 
	 * Format used to create the maps & directions link is <br/>
	 * https://members.kaiserpermanente.org/kpweb/facilitydir/map.do?link=tripplus&static_destination=1&destcity=<CITYNAME>&deststateProvince=<STATE_ABBREVIATION>&destaddress=<ESCAPED_STREET_ADDRESS>&rop=<LEGACY_REGION_CODE>
	 * The legacy region codes are from KP.org and are listed below. Use these with the "rop" parameter to bypass geotriage on the legacy kp.org site.  
	 * For example, the URL "https://members.kaiserpermanente.org/kpweb/facilitydir/map.do?link=tripplus&static_destination=1&destcity=Oakland&deststateProvince=CA&destaddress=2101%20Webster%20St&rop=MRN" uses the parameter rop=MRN  at the end of the URL.
		MRN = NorCal
		SCA = SoCal
		DB = Denver / Boulder
		CS = Southern Colorado
		GGA = Georgia
		HAW = Hawaii
		MID = Mid Atlantic
		OHI = Ohio
		KNW = Oregon/Washington
	 * @param doc - Document object that holds the state, city, address info
	 * @param rop - Region Selection for the current Search to be passed to Legacy
	 * @return link as a String to the legacy maps and direction feature for the current facility/doctor
	 */
	private String createMapsDirectionsLink(Document doc, String rop){
		String mapsLink = null;
		String stAdd = doc.getContentMap().get(IPortletConstants.CONTENT_ST_ADDRESS);
		if(StringUtils.isBlank(stAdd)){
			stAdd = doc.getContentMap().get(IPortletConstants.CONTENT_OFFICE_ADDRESS1);
		}
		String city = doc.getContentMap().get(IPortletConstants.CONTENT_CITY);
		String state = doc.getContentMap().get(IPortletConstants.CONTENT_STATE);
		if(StringUtils.isNotBlank(stAdd) && 
				StringUtils.isNotBlank(city) && 
					StringUtils.isNotBlank(state)){
			mapsLink = IPortletConstants.DEFAULT_MAPS_DIRECTIONS_URL;
			mapsLink = mapsLink.replace("<CITYNAME>", FormatUtil.urlEncode(FormatUtil.trim(city)));
			mapsLink = mapsLink.replace("<STATE_ABBREVIATION>", FormatUtil.urlEncode(FormatUtil.trim(state)));
			//stAdd=stAdd.replaceAll(",", "");
			//stAdd=stAdd.replaceAll("#", "");
			String regex = "[#,;\\/:*?\"<>|&']";
			stAdd=stAdd.replaceAll(regex, "");
			mapsLink = mapsLink.replace("<ESCAPED_STREET_ADDRESS>", FormatUtil.urlEncode(FormatUtil.trim(stAdd)));
			String legacyRop = ROPMap.updateRegionCodeForLegacyURLs(rop);
			mapsLink = mapsLink.replace("<LEGACY_REGION_CODE>", FormatUtil.urlEncode(FormatUtil.trim(legacyRop)));
			if (LOGGER.isDebugEnabled())LOGGER.debug("Created map link:"+mapsLink);
		}
		return mapsLink;
	}

	protected List<FacetGroup> processBinnings(Binning binning){
		if (LOGGER.isDebugEnabled())LOGGER.debug("processBinnings >> ");
		List<FacetGroup> lFacetGrp = null;
		if(null != binning){
			lFacetGrp = new ArrayList<FacetGroup>();
			for(BinningSet bs:binning.getLstBinningSet()){
				FacetGroup facetGroup = new FacetGroup();
				facetGroup.setFacetKey(bs.getLabel());
				List<Facet> lFacets = new ArrayList<Facet>();
				
				// To handle breadcrumb display issue when a radius selection produces results only for lower radius value
				boolean locWithinKey = "Located Within".equalsIgnoreCase(bs.getLabel());
				int locWithLblCnt = 0;
				int locWithLblIdx = 0;
				if (locWithinKey) {
					for(Bin b:bs.getLstBin()){
						if (StringUtils.isNotBlank(b.getLabel())) locWithLblCnt = locWithLblCnt+1;
					}
					if (LOGGER.isDebugEnabled())LOGGER.debug("Located Within labels in binning set " +locWithLblCnt);
				}
				
				for(Bin b:bs.getLstBin()){
					String encodedBToken = FormatUtil.urlEncode(b.getToken());
					//if (LOGGER.isDebugEnabled())LOGGER.debug("Creating url for "+bs.getLabel()+" and "+b.getToken());
					Facet facet = new Facet();
					facet.setCount(FormatUtil.convertStringtoInt(b.getNdocs()));
					facet.setTitle(FormatUtil.updateWithHTMLCharacters(b.getLabel()));
					/* Following code is required to allow only one facet selection at a time for a facetGroup.
					 * This functionality is a custom requirement, and is an aberration from how the default search interface works.*/
					Set<String> bstSelectFacets = new HashSet<String>();
					Set<String> bstDeSelectFacets = new HashSet<String>();
					boolean hasBinningStateTokens = false;
					String otherBins = "";
					for(BinningStateToken bst:binning.getLstBinningStateToken()){
						String encodedBSTToken = FormatUtil.urlEncode(bst.getToken());
						hasBinningStateTokens = true;
						// Check if the bin and binningStateToken belong to the same facetGroup
						if(bst.getId().equalsIgnoreCase(bs.getId())){
							locWithLblIdx = locWithLblIdx+1;
							// adding current bin token for selectHref
							//if (LOGGER.isDebugEnabled())LOGGER.debug("adding current bin token for selectHref for "+b.getLabel());
							bstSelectFacets.add(encodedBToken+binning.getBinningStateSeparator());
							// bin is already selected, part of binningStateToken list
							if(b.getToken().equalsIgnoreCase(bst.getToken())){
								//if (LOGGER.isDebugEnabled())LOGGER.debug("setting selectedFlag, true for "+b.getLabel());
								facet.setSelected(true);
							} else if (locWithinKey) {
								if (locWithLblCnt == locWithLblIdx) {
									if (LOGGER.isDebugEnabled()) LOGGER.debug("Locate Within setting selectedFlag in hardway for " +bst.getToken());
									if (bst.getToken().equalsIgnoreCase(IPortletConstants.LOCATED_WITHIN_10_MILES_LBL)) {
										facet.setTitle("10 miles");
									} else if (bst.getToken().equalsIgnoreCase(IPortletConstants.LOCATED_WITHIN_25_MILES_LBL)) {
										facet.setTitle("25 miles");
									} else if (bst.getToken().equalsIgnoreCase(IPortletConstants.LOCATED_WITHIN_50_MILES_LBL)) {
										facet.setTitle("50 miles");
									}
									facet.setSelected(true);
								}
							}
						}
						// bin and binningStateToken do not belong to the same facetGroup
						else{
							// adding current binStateToken values for selectHref & deSelectHref
							//if (LOGGER.isDebugEnabled())LOGGER.debug("adding current binStateToken values for selectHref & deSelectHref for "+b.getLabel());
							bstSelectFacets.add(encodedBSTToken+binning.getBinningStateSeparator());
							bstSelectFacets.add(encodedBToken+binning.getBinningStateSeparator());
							bstDeSelectFacets.add(encodedBSTToken+binning.getBinningStateSeparator());
							/*  -- WebTrends WI reporting stuff --- */
							otherBins = otherBins + FormatUtil.convertTokenToLabel(bst.getToken());
							/*  -- WebTrends WI reporting stuff --- */
						}
					}
					if(hasBinningStateTokens){
						//if (LOGGER.isDebugEnabled())LOGGER.debug("XML Has binning tokens, setting hrefs based on stringBuffers for "+b.getLabel());
						StringBuffer sbFacetURL = new StringBuffer();
						for(String s:bstSelectFacets){
							sbFacetURL.append(s);
						}
						String href = binning.getBinningURL()+sbFacetURL.toString();
						sbFacetURL = new StringBuffer();
						for(String s:bstDeSelectFacets){
							sbFacetURL.append(s);
						}
						String deSelectHref = binning.getBinningURL()+sbFacetURL.toString();
						/*  -- WebTrends WI reporting stuff --- */
						//if (LOGGER.isDebugEnabled())LOGGER.debug("OtherSelectedBins for "+b.getLabel()+" is "+otherBins);
						facet.setOtherSelectedBins(otherBins);
						/*  -- WebTrends WI reporting stuff --- */
						facet.setSelectHref(href);
						if(facet.isSelected()){
							facet.setDeSelectHref(deSelectHref);
						}
					}
					else{
						//if (LOGGER.isDebugEnabled())LOGGER.debug("XML does not have binning tokens");
						facet.setSelectHref(binning.getBinningURL()+encodedBToken);
					}
					// code ends for finding the selected facets 
					if (LOGGER.isDebugEnabled())LOGGER.debug("SELECT "+bs.getLabel()+" and "+b.getToken()+" -->>  "+facet.getSelectHref());
					if (LOGGER.isDebugEnabled())LOGGER.debug("DESELECT "+bs.getLabel()+" and "+b.getToken()+" -->> "+facet.getDeSelectHref());
					lFacets.add(facet);
				}
				facetGroup.setFacets(lFacets);
				lFacetGrp.add(facetGroup);
				locWithinKey = false;
			}
		}
		return lFacetGrp;
	}
	
	/** 
	 * 
	 * @param ksResp
	 * @return SearchResultsView
	 */
	public SearchResultsView processBinOnlyResults(KaiserSearchResponse ksResp){
		if (LOGGER.isDebugEnabled())LOGGER.debug("<< processBinOnlyResults ");
		SearchResultsView searchResultsView = new SearchResultsView();
		List<FacetGroup> lFacetGroups = processBinnings(ksResp.getBinning());
		searchResultsView.setSelectableFacetGroups(lFacetGroups);
		if (LOGGER.isDebugEnabled())LOGGER.debug("<< processBinOnlyResults ");
		return searchResultsView;		
	}	
	
	/** 
	 * 
	 * @param lFGroups
	 * @return
	 */
	public Map<String, List<Facet>> convertFacetListtoMap(List<FacetGroup> lFGroups){
		if (LOGGER.isDebugEnabled())LOGGER.debug("convertFacetListtoMap >> ");
		Map<String, List<Facet>> mpFGroups = null; 
		if(null != lFGroups){
			mpFGroups = new HashMap<String, List<Facet>>();
			for(FacetGroup fg:lFGroups){
				if (LOGGER.isDebugEnabled())LOGGER.debug("convertFacetListtoMap - "+fg.getFacetKey());
				mpFGroups.put(fg.getFacetKey().toLowerCase(), fg.getFacets());
			}
		}
		return mpFGroups;
	}

	/** 
	 * 
	 * @param lFGroups
	 * @return
	 */
	private ArrayList<FacetGroup> sortFacetGroupList(List<FacetGroup> lFGroups, List<String> sortOrder){
		ArrayList<FacetGroup> sortedList = null; 
		HashMap<String, FacetGroup> myMp = new HashMap<String, FacetGroup>();
		if(null != lFGroups){
			sortedList = new ArrayList<FacetGroup>();
			for(FacetGroup fg:lFGroups){
				myMp.put(fg.getFacetKey().toLowerCase(), fg);
			}
			for(int i=0;i<sortOrder.size();i++){
				String fKey = sortOrder.get(i).toLowerCase();
				if(null != myMp.get(fKey)){
					sortedList.add(myMp.get(fKey));
					myMp.remove(fKey);
				}
			}
			for(Entry<String, FacetGroup> e:myMp.entrySet()){
				if (LOGGER.isDebugEnabled())LOGGER.debug("Custom sorting through facet list --> "+e.getValue().getFacetKey());
				sortedList.add(e.getValue());
			}
		}
		return sortedList;
	}
	
	/** 
	 * 
	 * @param Searchproject name coming in from the XML response: projectName
	 * @return preference parameter name for this project to get customFacetSortOrder
	 */
	private String getPreferenceNameForFacetOrder(String projectName){
		String sortOrderPref = IPortletConstants.PREF_SORTORDER_FACETS;
		if(StringUtils.isNotBlank(projectName)){
			if(projectName.equalsIgnoreCase(IPortletConstants.SEARCH_COLLECTION_FACDIR)){
				sortOrderPref = IPortletConstants.PREF_FACDIR_FACETORDER;
			}else if(projectName.equalsIgnoreCase(IPortletConstants.SEARCH_COLLECTION_DOCTOR)){
				sortOrderPref = IPortletConstants.PREF_DOCTOR_FACETORDER;
			}
		}
		return sortOrderPref;
	}
	
	/** 
	 * 
	 * @param Searchproject name coming in from the XML response: projectName
	 * @return Display name for the incoming project
	 */
	private String getDisplayNameForProject(String searchProj){
		String displayName=null;
		if(IPortletConstants.SEARCH_COLLECTION_FACDIR.equalsIgnoreCase(searchProj)){
			displayName = IPortletConstants.SEARCH_FEATURE_FACDIR;
		}else if(IPortletConstants.SEARCH_COLLECTION_DOCTOR.equalsIgnoreCase(searchProj)){
			displayName = IPortletConstants.SEARCH_FEATURE_DOCTOR;
		}else if(IPortletConstants.SEARCH_COLLECTION_DRUGENCY.equalsIgnoreCase(searchProj)){
			displayName = IPortletConstants.SEARCH_FEATURE_DRUGENCY;
		}else if(IPortletConstants.SEARCH_COLLECTION_CLASSES.equalsIgnoreCase(searchProj)){
			displayName = IPortletConstants.SEARCH_FEATURE_CLASSES;
		}else if(IPortletConstants.SEARCH_COLLECTION_HENCY.equalsIgnoreCase(searchProj)){
			displayName = IPortletConstants.SEARCH_FEATURE_HENCY;
		}
		return displayName;
	}
}