package org.kp.wpp.portlet.search;

import javax.portlet.ReadOnlyException;

import junit.framework.TestCase;

import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.beans.SearchResultsView;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.helpers.SearchHelper;
import org.kp.wpp.utils.search.service.objects.KaiserSearchResponse;
import org.kp.wpp.utils.search.service.parser.SearchResultsHandler;
import org.springframework.mock.web.portlet.MockPortletPreferences;

public class SearchSpellCheckTest extends TestCase{
	
	private static final Log LOGGER = LogFactory.getLog(SearchSpellCheckTest.class);
	public void testSpellCheckWithResults(){
		SearchResultsHandler sHandler = new SearchResultsHandler();
		CreateTestParser parser = new CreateTestParser(sHandler);
		try{
			parser.parse(SearchTest.class.getClassLoader().getResourceAsStream("sample-spellcheck-withresults.xml"));
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
		kSearchResp.setBoost(sHandler.getBoost());

		MockPortletPreferences mockPref = new MockPortletPreferences();
		try {
			mockPref.setValues(IPortletConstants.PREF_SORTORDER_FACETS, new String[]{"region", "language", "category"});
		} catch (ReadOnlyException e) {
			LOGGER.error("Exception while creating MockPortletPreferences --> "+e.getMessage());
		}
        SearchResultsView srv = new SearchHelper().processSearchResults(kSearchResp, mockPref);

		assertTrue(srv.getSearchCriteria().isSummaryDisplayFlag());

        assertEquals("pregnacy",srv.getSearchCriteria().getKeyword());
		assertEquals("2",srv.getSearchCriteria().getResultsCount());
		
		assertEquals("pregnancy",srv.getSearchCriteria().getSpellCorrectedKeyword());
		assertEquals("https://kpvc1035.crdc.kp.org:8443/search/cgi-bin/query-meta?query=pregnancy&v:project=kp-consumernet",srv.getSearchCriteria().getSpellCorrectedHref());
	}

	public void testSpellCheckWithNoResults(){
		SearchResultsHandler sHandler = new SearchResultsHandler();
		CreateTestParser parser = new CreateTestParser(sHandler);
		try{
			parser.parse(SearchTest.class.getClassLoader().getResourceAsStream("sample-spellcheck-noresults.xml"));
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
		kSearchResp.setBoost(sHandler.getBoost());

		MockPortletPreferences mockPref = new MockPortletPreferences();
		try {
			mockPref.setValues(IPortletConstants.PREF_SORTORDER_FACETS, new String[]{"region", "language", "category"});
		} catch (ReadOnlyException e) {
			LOGGER.error("Exception while creating MockPortletPreferences --> "+e.getMessage());
		}
        SearchResultsView srv = new SearchHelper().processSearchResults(kSearchResp, mockPref);

        assertEquals("okland",srv.getSearchCriteria().getKeyword());
		assertEquals("0",srv.getSearchCriteria().getResultsCount());
		
		assertEquals("oakland",srv.getSearchCriteria().getSpellCorrectedKeyword());
		assertEquals("https://kpvc1035.crdc.kp.org:8443/search/cgi-bin/query-meta?query=oakland&v:project=kp-consumernet",srv.getSearchCriteria().getSpellCorrectedHref());
		
		assertTrue(srv.getSearchCriteria().isSummaryDisplayFlag());

		assertNull(srv.getContents());
		assertNull(srv.getNavigationLinks());
		assertNull(srv.getSelectableFacetGroups());
	}
}
