package org.kp.wpp.portlet.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.ReadOnlyException;

import junit.framework.TestCase;

import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.beans.Facet;
import org.kp.wpp.portlet.search.beans.FacetGroup;
import org.kp.wpp.portlet.search.beans.SearchResultsView;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.helpers.SearchHelper;
import org.kp.wpp.utils.search.service.objects.Bin;
import org.kp.wpp.utils.search.service.objects.Binning;
import org.kp.wpp.utils.search.service.objects.BinningSet;
import org.kp.wpp.utils.search.service.objects.BinningStateToken;
import org.kp.wpp.utils.search.service.objects.Document;
import org.kp.wpp.utils.search.service.objects.KaiserSearchResponse;
import org.kp.wpp.utils.search.service.parser.SearchResultsHandler;
import org.springframework.mock.web.portlet.MockPortletPreferences;

public class SearchMGTest extends TestCase{

	private static final Log LOGGER = LogFactory.getLog(SearchMGTest.class);

	public void testSearchMGHelperForNull(){		
		KaiserSearchResponse kSearchResp = new KaiserSearchResponse();
		kSearchResp.setLstDocuments(null);
		kSearchResp.setSearchProject("");
		
		MockPortletPreferences mockPref = new MockPortletPreferences();
		try {
			mockPref.setValues(IPortletConstants.PREF_FACDIR_FACETORDER, new String[]{"city", "services", "department type"});
			mockPref.setValues(IPortletConstants.PREF_DOCTOR_FACETORDER, new String[]{"city", "gender", "medical specialty"});
		} catch (ReadOnlyException e) {
			LOGGER.error("Exception while creating MockPortletPreferences --> "+e.getMessage());
		}
		SearchResultsView srv = new SearchHelper().processSearchResults(kSearchResp, mockPref);
		assertNull(srv.getContents());
		
		kSearchResp = new KaiserSearchResponse();
		kSearchResp.setLstDocuments(new ArrayList<Document>());
		kSearchResp.setSearchProject("");
		kSearchResp.setContentsPerPage("25");
		kSearchResp.setCurPageStartIndex("0");
		srv = new SearchHelper().processSearchResults(kSearchResp, mockPref);
		assertNull(srv.getNavigationLinks());
		assertNull(srv.getSelectableFacetGroups());
		assertNull(srv.getSelectedFacetGroups());
		assertNull(srv.getContents());
	}

	public void testSearchHandlerAndHelperForMGResults(){		
		// Create Search Result Handler
		SearchResultsHandler sHandler = new SearchResultsHandler();
		// Create the parser
		CreateTestParser parser = new CreateTestParser(sHandler);
		// Parse the XML file, handler generates the output
		
		try{
			parser.parse(SearchTest.class.getClassLoader().getResourceAsStream("sample_mgsearch_locations.xml"));
		}
		catch(Exception e){
			e.printStackTrace();
			LOGGER.error("Exception while parsing:"+e.getMessage());
		}
		
		KaiserSearchResponse kSearchResp = new KaiserSearchResponse();
		kSearchResp.setBinning(sHandler.getBinning());
		kSearchResp.setLstDocuments(sHandler.getLstDocuments());
		kSearchResp.setNav(sHandler.getNav());
		kSearchResp.setTotalRecords(sHandler.getTotalRecords());
		kSearchResp.setContentsPerPage(sHandler.getPageSize());
		kSearchResp.setCurPageStartIndex(sHandler.getStartIndex());
		kSearchResp.setSearchKeyword(sHandler.getSearchKeyword());
		kSearchResp.setSearchProject(sHandler.getParamProjectName());
		kSearchResp.setSearchRegionFilter(sHandler.getParamRegionFilter());
		kSearchResp.setBoost(sHandler.getBoost());

		MockPortletPreferences mockPref = new MockPortletPreferences();
		try {
			mockPref.setValues(IPortletConstants.PREF_FACDIR_FACETORDER, new String[]{"city", "services", "department type"});
			mockPref.setValues(IPortletConstants.PREF_DOCTOR_FACETORDER, new String[]{"city", "gender", "medical specialty"});
		} catch (ReadOnlyException e) {
			LOGGER.error("Exception while creating MockPortletPreferences --> "+e.getMessage());
		}
		SearchResultsView srv = new SearchHelper().processSearchResults(kSearchResp, mockPref);
		
		assertEquals("Locations",srv.getSearchCriteria().getProject());
		assertEquals("California - Northern",srv.getSearchCriteria().getRegionFilter());

		assertEquals("/kpweb/facilitydir/map.do?link=tripplus&static_destination=1&destcity=Oakland&deststateProvince=CA&destaddress=280+W.+MacArthur+Blvd.&rop=MRN",srv.getContents().get(12).getDirectionsLink());
		assertEquals("/kpweb/facilitydir/map.do?link=tripplus&static_destination=1&destcity=San+Jose&deststateProvince=CA&destaddress=401+Bicentennial+Way&rop=MRN",srv.getContents().get(1).getDirectionsLink());
		assertEquals("/kpweb/facilitydir/map.do?link=tripplus&static_destination=1&destcity=Bolinas&deststateProvince=CA&destaddress=7+Wharf+Road&rop=MRN",srv.getContents().get(20).getDirectionsLink());
				
		assertTrue(srv.getContents().get(20).isKpAffiliated());
		assertFalse(srv.getContents().get(21).isKpAffiliated());
    	assertFalse(srv.getContents().get(9).isKpAffiliated());

		assertEquals("415-899-7525",srv.getContents().get(20).getPhoneNumber());
    	assertEquals("916-784-4000",srv.getContents().get(9).getPhoneNumber());
 
		assertEquals("Home Health <span class=\"vivbold qt0\">Care</span>, Pediatrics (Fremont West), Infertility/Reproductive Endocrinology/InVitro, Adult travel injections, Advice Nurse, Allergy, Ambulatory Surgery Center, Cardiology, <span class=\"vivbold qt0\">Care</span> Management, Case Management, Dermatology, Emergency Department, Gastroenterology (GI), Head and Neck Surgery/Audiology/Speech Therapy, Infectious Disease, Internal Medicine, Laboratory and Pathology, Nephrology, Neurology, Obstetrics/Gynecology, Occupational Health, Oncology, Ophthalmology, Optical Center, Optometry, Orthopedics, Pediatric injections, Pediatrics (Fremont Main Campus), Pharmacy (Niles East), Pharmacy (Niles West), Pharmacy (Ohlone Pediatric Pharmacy), Podiatry, Psychiatry, Pulmonology, Radiology, Rehabilitation Services (Physical and Occupational, Rheumatology Services, Sports Medicine, Sub-Specialty Medicine, Surgery Clinic, Urgent <span class=\"vivbold qt0\">Care</span>, Urology,",srv.getContents().get(22).getSvcsList());
		assertNull(srv.getContents().get(21).getSvcsList());
	}

	public void testSearchHandlerAndHelperForMGFacetNavigation(){		
		// Create Search Result Handler
		SearchResultsHandler sHandler = new SearchResultsHandler();
		// Create the parser
		CreateTestParser parser = new CreateTestParser(sHandler);
		// Parse the XML file, handler generates the output
		
		try{
			parser.parse(SearchTest.class.getClassLoader().getResourceAsStream("mgsearch-locations-facets.xml"));
		}
		catch(Exception e){
			e.printStackTrace();
			LOGGER.error("Exception while parsing:"+e.getMessage());
		}
		
		KaiserSearchResponse kSearchResp = new KaiserSearchResponse();
		kSearchResp.setBinning(sHandler.getBinning());
		kSearchResp.setLstDocuments(sHandler.getLstDocuments());
		kSearchResp.setNav(sHandler.getNav());
		kSearchResp.setTotalRecords(sHandler.getTotalRecords());
		kSearchResp.setContentsPerPage(sHandler.getPageSize());
		kSearchResp.setCurPageStartIndex(sHandler.getStartIndex());
		kSearchResp.setSearchKeyword(sHandler.getSearchKeyword());
		kSearchResp.setSearchProject(sHandler.getParamProjectName());
		kSearchResp.setSearchRegionFilter(sHandler.getParamRegionFilter());
		kSearchResp.setBoost(sHandler.getBoost());

		MockPortletPreferences mockPref = new MockPortletPreferences();
		try {
			mockPref.setValues(IPortletConstants.PREF_FACDIR_FACETORDER, new String[]{"city", "services", "department type"});
			mockPref.setValues(IPortletConstants.PREF_DOCTOR_FACETORDER, new String[]{"city", "gender", "medical specialty"});
		} catch (ReadOnlyException e) {
			LOGGER.error("Exception while creating MockPortletPreferences --> "+e.getMessage());
		}
		SearchResultsView srv = new SearchHelper().processSearchResults(kSearchResp, mockPref);

		assertEquals("Locations",srv.getSearchCriteria().getProject());
		List<FacetGroup> selectable = srv.getSelectableFacetGroups();
		List<FacetGroup> selected = srv.getSelectedFacetGroups();

		assertEquals(1, selectable.size());
		assertEquals(1, selected.size());

		assertEquals("city",selectable.get(0).getFacetKey());
		assertEquals("department type",selected.get(0).getFacetKey());

		for(int i=0;i<selected.size(); i++){
			List<Facet> fac = selected.get(i).getFacets();
			for(int j=0;j < fac.size(); j++){
				if(fac.get(j).getTitle().equalsIgnoreCase("Internal Medicine")){
					assertTrue(fac.get(j).isSelected());
				}
				else{
					assertFalse(fac.get(j).isSelected());
				}
			}
		}
		
		// TESTING THE CONVERT FACETS TO MAP METHOD
		convertFacetListtoMap(selectable);

	}
	
	// ********************************************** //
	/* This test method does not use a sample XML for creating the ksResp, just a test object is created */
	public void testSearchHandlerAndHelperForResults(){		
		KaiserSearchResponse kSearchResp = new KaiserSearchResponse();
		kSearchResp.setLstDocuments(null);
		kSearchResp.setNav(null);
		kSearchResp.setTotalRecords(null);
		kSearchResp.setContentsPerPage(null);
		kSearchResp.setCurPageStartIndex(null);
		kSearchResp.setSearchKeyword("");
		kSearchResp.setSearchProject("kp-mg-facdirproject");
		kSearchResp.setSearchRegionFilter("NCA");
		kSearchResp.setBoost(null);
		Binning binning = new Binning();
		binning.setBinningStateSeparator(null);
		binning.setBinningURL("binning-base-url");
		binning.setLstBinningStateToken(new ArrayList<BinningStateToken>());
		List<BinningSet> lstBinningSet = new ArrayList<BinningSet>();
		for(int i=0; i<5; i++){
			BinningSet bs = new BinningSet();
			bs.setId("binning_set_id_"+i);
			bs.setLabel("binning_set_label_"+i);
			List<Bin> lstBin = new ArrayList<Bin>();
			Bin bin = null;
			for(int j=0; j<8; j++){
				bin = new Bin();
				bin.setLabel("bin_label_"+i+"_"+j);
				bin.setNdocs(String.valueOf(j));
				bin.setToken("bin_token_"+i+"_"+j);
				lstBin.add(bin);
			}
			bs.setLstBin(lstBin);
			lstBinningSet.add(bs);
		}
		binning.setLstBinningSet(lstBinningSet);
		kSearchResp.setBinning(binning);
		SearchResultsView srv = new SearchHelper().processBinOnlyResults(kSearchResp);
		assertEquals("binning_set_label_0",srv.getSelectableFacetGroups().get(0).getFacetKey());
		assertEquals("binning_set_label_2",srv.getSelectableFacetGroups().get(2).getFacetKey());
		assertEquals(2,srv.getSelectableFacetGroups().get(0).getFacets().get(2).getCount());
		assertEquals(4,srv.getSelectableFacetGroups().get(1).getFacets().get(4).getCount());
		assertEquals("bin_label_1_4",srv.getSelectableFacetGroups().get(1).getFacets().get(4).getTitle());
		assertNull(srv.getSelectableFacetGroups().get(0).getFacets().get(0).getDeSelectHref());
	}
	
	private void convertFacetListtoMap(List<FacetGroup> lFGroups){
		SearchHelper helper = new SearchHelper();
		Map<String, List<Facet>> mpFg = helper.convertFacetListtoMap(lFGroups);
		List<Facet> fac = mpFg.get("city");
		assertEquals(56, fac.size());
		fac = mpFg.get("department type");
		assertNull(fac);
	}

	public void testConvertFacetListtoMapForNull(){
		SearchHelper helper = new SearchHelper();
		Map<String, List<Facet>> mpFg = helper.convertFacetListtoMap(null);
		assertNull(mpFg);
		mpFg = helper.convertFacetListtoMap(new ArrayList<FacetGroup>());
		assertNull(mpFg.get("city"));
	}
	public void testFetchProviderTypeForNull(){
		SearchHelper helper = new SearchHelper();
		KaiserSearchResponse kSearchResp = new KaiserSearchResponse();
		kSearchResp.setLstDocuments(null);
		kSearchResp.setSearchProject("");
		SearchResultsView srv =helper.processProviderTypeResults(kSearchResp);
		assertNull(srv.getContents());
		
		kSearchResp = new KaiserSearchResponse();
		kSearchResp.setLstDocuments(new ArrayList<Document>());
		kSearchResp.setSearchProject("");
		kSearchResp.setContentsPerPage("25");
		kSearchResp.setCurPageStartIndex("0");
		srv = helper.processProviderTypeResults(kSearchResp);
		assertNull(srv.getNavigationLinks());
		assertNull(srv.getSelectableFacetGroups());
		assertNull(srv.getSelectedFacetGroups());
		assertNull(srv.getContents());

	}
}
