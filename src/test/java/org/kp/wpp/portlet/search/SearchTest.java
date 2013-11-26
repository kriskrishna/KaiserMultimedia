package org.kp.wpp.portlet.search;

import java.util.ArrayList;

import javax.portlet.ReadOnlyException;

import junit.framework.TestCase;

import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.portlet.search.beans.SearchResultsView;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.helpers.SearchHelper;
import org.kp.wpp.utils.search.service.objects.Document;
import org.kp.wpp.utils.search.service.objects.KaiserSearchResponse;
import org.kp.wpp.utils.search.service.parser.SearchResultsHandler;
import org.springframework.mock.web.portlet.MockPortletPreferences;

public class SearchTest extends TestCase{

	private static final Log LOGGER = LogFactory.getLog(SearchTest.class);
	
	public void testSearchHandlerAndHelperForProjectNames(){

		KaiserSearchResponse kSearchResp = new KaiserSearchResponse();
		MockPortletPreferences pPref = new MockPortletPreferences();
		kSearchResp.setLstDocuments(null);
		kSearchResp.setSearchProject(IPortletConstants.SEARCH_COLLECTION_CLASSES);
		SearchResultsView srv = new SearchHelper().processSearchResults(kSearchResp, pPref);
		assertEquals(IPortletConstants.SEARCH_FEATURE_CLASSES,srv.getSearchCriteria().getProject());

		kSearchResp.setSearchProject(IPortletConstants.SEARCH_COLLECTION_DOCTOR);
		srv = new SearchHelper().processSearchResults(kSearchResp, pPref);
		assertEquals(IPortletConstants.SEARCH_FEATURE_DOCTOR,srv.getSearchCriteria().getProject());

		kSearchResp.setSearchProject(IPortletConstants.SEARCH_COLLECTION_DRUGENCY);
		srv = new SearchHelper().processSearchResults(kSearchResp, pPref);
		assertEquals(IPortletConstants.SEARCH_FEATURE_DRUGENCY,srv.getSearchCriteria().getProject());

		kSearchResp.setSearchProject(IPortletConstants.SEARCH_COLLECTION_FACDIR );
		srv = new SearchHelper().processSearchResults(kSearchResp, pPref);
		assertEquals(IPortletConstants.SEARCH_FEATURE_FACDIR,srv.getSearchCriteria().getProject());

		kSearchResp.setSearchProject(IPortletConstants.SEARCH_COLLECTION_HENCY);
		srv = new SearchHelper().processSearchResults(kSearchResp, pPref);
		assertEquals(IPortletConstants.SEARCH_FEATURE_HENCY,srv.getSearchCriteria().getProject());

	}
	
	public void testSearchHandlerAndHelperForNull(){		
		KaiserSearchResponse kSearchResp = new KaiserSearchResponse();
		kSearchResp.setLstDocuments(null);
		MockPortletPreferences pPref = new MockPortletPreferences();
		try {
			pPref.setValues(IPortletConstants.PREF_SORTORDER_FACETS, new String[]{"region", "language", "category"});
		} catch (ReadOnlyException e) {
			LOGGER.error("Exception while creating MockPortletPreferences --> "+e.getMessage());
		}
		SearchResultsView srv = new SearchHelper().processSearchResults(kSearchResp, pPref);
		assertNull(srv.getContents());
		kSearchResp = new KaiserSearchResponse();
		kSearchResp.setLstDocuments(new ArrayList<Document>());
		kSearchResp.setContentsPerPage("10");
		kSearchResp.setCurPageStartIndex("0");
		srv = new SearchHelper().processSearchResults(kSearchResp, pPref);
		assertNull(srv.getNavigationLinks());
		assertNull(srv.getSelectableFacetGroups());
		assertNull(srv.getContents());
	}
	
	public void testSearchHandlerAndHelperForResults(){		
		// Create Search Result Handler
		SearchResultsHandler sHandler = new SearchResultsHandler();
		// Create the parser
		CreateTestParser parser = new CreateTestParser(sHandler);
		// Parse the XML file, handler generates the output
		
		try{
			parser.parse(SearchTest.class.getClassLoader().getResourceAsStream("sample_search_response.xml"));
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

		MockPortletPreferences pPref = new MockPortletPreferences();
		try {
			pPref.setValues(IPortletConstants.PREF_SORTORDER_FACETS, new String[]{"region", "language", "category"});
		} catch (ReadOnlyException e) {
			LOGGER.error("Exception while creating MockPortletPreferences --> "+e.getMessage());
		}
		SearchResultsView srv = new SearchHelper().processSearchResults(kSearchResp, pPref);
		
		assertNull(srv.getSearchCriteria().getProject());
		assertEquals("pregnancy",srv.getSearchCriteria().getKeyword());
		assertEquals("2031",srv.getSearchCriteria().getResultsCount());

		assertEquals("region",srv.getSelectedFacetGroups().get(0).getFacetKey());
		assertEquals(1767,srv.getSelectedFacetGroups().get(0).getFacets().get(0).getCount());
		assertNull(srv.getSelectedFacetGroups().get(0).getFacets().get(0).getDeSelectHref());
		assertEquals("http://kpvc1003.crdc.kp.org:8443/search/cgi-bin/query-meta?&v:project=kp-consumernet&query=pregnancy&binning-state=region_label%3D%3DCalifornia+-+Northern%0A%0A",srv.getSelectedFacetGroups().get(0).getFacets().get(0).getSelectHref());
		assertFalse(srv.getSelectedFacetGroups().get(0).getFacets().get(0).isSelected());
		assertEquals("California - Northern",srv.getSelectedFacetGroups().get(0).getFacets().get(0).getTitle());

		assertEquals(1443,srv.getSelectedFacetGroups().get(0).getFacets().get(3).getCount());
		assertEquals("http://kpvc1003.crdc.kp.org:8443/search/cgi-bin/query-meta?&v:project=kp-consumernet&query=pregnancy&binning-state=",srv.getSelectedFacetGroups().get(0).getFacets().get(3).getDeSelectHref());
		assertEquals("http://kpvc1003.crdc.kp.org:8443/search/cgi-bin/query-meta?&v:project=kp-consumernet&query=pregnancy&binning-state=region_label%3D%3DColorado+-+Denver+%2F+Boulder%0A%0A",srv.getSelectedFacetGroups().get(0).getFacets().get(3).getSelectHref());
		assertTrue(srv.getSelectedFacetGroups().get(0).getFacets().get(3).isSelected());
		assertEquals("Colorado - Denver &#47; Boulder",srv.getSelectedFacetGroups().get(0).getFacets().get(3).getTitle());
		
		assertEquals("language",srv.getSelectableFacetGroups().get(0).getFacetKey());
		assertEquals(1932,srv.getSelectableFacetGroups().get(0).getFacets().get(0).getCount());
		assertNull(srv.getSelectableFacetGroups().get(0).getFacets().get(0).getDeSelectHref());
		//assertEquals("http://kpvc1003.crdc.kp.org:8443/search/cgi-bin/query-meta?&v:project=kp-consumernet&query=pregnancy&binning-state=region_label%3D%3DColorado+-+Denver+%2F+Boulder%0A%0Akp_language%3D%3DEnglish%0A%0A",srv.getSelectableFacetGroups().get(0).getFacets().get(0).getSelectHref());
		assertFalse(srv.getSelectableFacetGroups().get(0).getFacets().get(0).isSelected());
		assertEquals("English",srv.getSelectableFacetGroups().get(0).getFacets().get(0).getTitle());

		assertEquals("category",srv.getSelectableFacetGroups().get(1).getFacetKey());
		assertEquals(1439,srv.getSelectableFacetGroups().get(1).getFacets().get(0).getCount());
		assertNull(srv.getSelectableFacetGroups().get(1).getFacets().get(0).getDeSelectHref());
		//assertEquals("http://kpvc1003.crdc.kp.org:8443/search/cgi-bin/query-meta?&v:project=kp-consumernet&query=pregnancy&binning-state=category%3D%3DHealth+articles%0A%0Aregion_label%3D%3DColorado+-+Denver+%2F+Boulder%0A%0A",srv.getSelectableFacetGroups().get(1).getFacets().get(0).getSelectHref());
		assertFalse(srv.getSelectableFacetGroups().get(1).getFacets().get(0).isSelected());
		assertEquals("Health articles",srv.getSelectableFacetGroups().get(1).getFacets().get(0).getTitle());

		// testing Content beans
		assertEquals("Snippet for the first result",srv.getContents().get(0).getSnippet().trim());
		assertEquals("<span class=\"vivbold qt0\">Pregnancy</span>",srv.getContents().get(0).getTitle());
		assertEquals("https://members.kaiserpermanente.org/kpweb/healthency.do?hwid=hw197814",srv.getContents().get(0).getLinkValue());
		assertEquals(1,srv.getContents().get(0).getIndex());
		
		assertEquals("<span class=\"vivbold qt0\">Pregnancy</span> and newborn care featured health topic: Prenatal screening tests (Northern California)",srv.getContents().get(9).getTitle());
    	assertEquals("Snippet for the tenth result",srv.getContents().get(9).getSnippet().trim());
		assertEquals("https://members.kaiserpermanente.org/kpweb/Link.do?html=/htmlapp/feature/200pregnancy/ncal_prenatal_screening.html",srv.getContents().get(9).getLinkValue());
		assertEquals(10,srv.getContents().get(9).getIndex());

    	assertEquals("Snippet for the second result",srv.getContents().get(1).getSnippet().trim()); 
		assertEquals("NO TITLE",srv.getContents().get(1).getTitle());     
		assertEquals("https://members.kaiserpermanente.org/kpweb/Link.do?html=/htmlapp/feature/200pregnancy/scal_topic_overview.html",srv.getContents().get(1).getLinkValue());
		assertEquals(2,srv.getContents().get(1).getIndex());
		
		// testing all navigation link, functionalities
		assertEquals("1",srv.getNavigationLinks().get(0).getTitle());
		assertEquals("6",srv.getNavigationLinks().get(5).getTitle());
		assertEquals(srv.getNavigationLinks().size(),10);

		assertEquals("http://kpvc1003.crdc.kp.org:8443/search/cgi-bin/query-meta?v%3afile=viv_uMRnCA&v:state=root|root-0-10|0",srv.getNavigationLinks().get(0).getHref());
		assertEquals("http://kpvc1003.crdc.kp.org:8443/search/cgi-bin/query-meta?v%3afile=viv_uMRnCA&v:state=root|root-50-10|0",srv.getNavigationLinks().get(5).getHref());
	}
}