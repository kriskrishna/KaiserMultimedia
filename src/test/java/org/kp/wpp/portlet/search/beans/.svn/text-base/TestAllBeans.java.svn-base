package org.kp.wpp.portlet.search.beans;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;

public class TestAllBeans extends TestCase{

	private static final Log LOGGER = LogFactory.getLog(TestAllBeans.class);
	
	public void testSearchResultsView(){
		// testdata for facets
		SearchResultsView srv = new SearchResultsView();
		assertNull(srv.getContents());
		assertNull(srv.getNavigationLinks());
		assertNull(srv.getSearchCriteria());
		assertNull(srv.getSelectableFacetGroups());

		SearchCriteria sc = new SearchCriteria();
		assertNull(sc.getKeyword());
		assertNull(sc.getResultsCount());

		Content c = new Content();
		assertNull(c.getLinkValue());
		assertNull(c.getSnippet());
		assertNull(c.getTitle());
		assertEquals(0,c.getIndex());

		Facet f = new Facet();
		assertNull(f.getDeSelectHref());
		assertNull(f.getSelectHref());
		assertEquals(0,f.getCount());
		assertNull(f.getTitle());
		
		FacetGroup fG = new FacetGroup();
		assertNull(fG.getFacetKey());
		assertNull(fG.getFacets());
		
		PageLink pl = new PageLink();
		assertNull(pl.getHref());
		assertNull(pl.getTitle());
		
		LOGGER.debug("successfully checked all - fresh objects");
		
		srv.setSelectableFacetGroups(createFacetGroupListforTest());
		srv.setNavigationLinks(createPageLinksForTest());
		srv.setContents(createSearchResultsForTest());
		srv.setSearchCriteria(createSearchCriteriaForTest());
		
		assertEquals(srv.getSearchCriteria().getKeyword(),"oakland");
		assertEquals(srv.getSearchCriteria().getResultsCount(),"94");
		
		LOGGER.debug("successfully checked SearchCriteria");

		assertEquals(1,srv.getContents().get(0).getIndex());
		assertEquals("https://www.kaiserpermanante.org",srv.getContents().get(0).getLinkValue());
		assertEquals( "Features and services from Kaiser Permanante",srv.getContents().get(0).getSnippet());
		assertEquals("kp.org",srv.getContents().get(0).getTitle());
		assertEquals(2,srv.getContents().get(1).getIndex());
		assertEquals("https://www.kaiserpermanante-intranet.org",srv.getContents().get(1).getLinkValue());
		assertEquals("Features and services from Kaiser Permanante for employees",srv.getContents().get(1).getSnippet());
		assertEquals("kpintranet.org",srv.getContents().get(1).getTitle());

		LOGGER.debug("successfully checked Contents");

		assertEquals(srv.getSelectableFacetGroups().get(0).getFacetKey(), "Region");
		assertEquals(srv.getSelectableFacetGroups().get(0).getFacets().get(0).getTitle(),"California - Northern");
		assertEquals(srv.getSelectableFacetGroups().get(0).getFacets().get(0).getCount(),1695);
		assertEquals(srv.getSelectableFacetGroups().get(0).getFacets().get(0).getDeSelectHref(),"http://searchserver?project&binning_state=deselect-nca");
		assertEquals(srv.getSelectableFacetGroups().get(0).getFacets().get(0).getSelectHref(),"http://searchserver?project&binning_state=select-nca");
		assertTrue(srv.getSelectableFacetGroups().get(0).getFacets().get(0).isSelected());
		assertEquals(srv.getSelectableFacetGroups().get(0).getFacets().get(1).getTitle(),"California - Southern");
		assertEquals(srv.getSelectableFacetGroups().get(0).getFacets().get(1).getCount(),1590);
		assertNull(srv.getSelectableFacetGroups().get(0).getFacets().get(1).getDeSelectHref());
		assertEquals(srv.getSelectableFacetGroups().get(0).getFacets().get(1).getSelectHref(),"http://searchserver?project&binning_state=select-sca");
		assertFalse(srv.getSelectableFacetGroups().get(0).getFacets().get(1).isSelected());

		assertEquals(srv.getSelectableFacetGroups().get(1).getFacetKey(), "Category");
		assertEquals(srv.getSelectableFacetGroups().get(1).getFacets().get(0).getTitle(),"Health articles");
		assertEquals(srv.getSelectableFacetGroups().get(1).getFacets().get(0).getCount(),1370);
		assertNull(srv.getSelectableFacetGroups().get(1).getFacets().get(0).getDeSelectHref());
		assertEquals(srv.getSelectableFacetGroups().get(1).getFacets().get(0).getSelectHref(),"http://searchserver?project&binning_state=select-hat");
		assertFalse(srv.getSelectableFacetGroups().get(1).getFacets().get(0).isSelected());

		LOGGER.debug("successfully checked FacetGroups");
	}	

	private List<FacetGroup> createFacetGroupListforTest(){
		List<FacetGroup> lstFGroups = new ArrayList<FacetGroup>();
		List<Facet> lstFac = null; 

		FacetGroup fg = new FacetGroup();
		Facet f = new Facet();
		fg.setFacetKey("Region");
		lstFac = new ArrayList<Facet>();
		f = new Facet();f.setTitle("California - Northern"); f.setCount(1695); f.setSelectHref("http://searchserver?project&binning_state=select-nca");
		f.setDeSelectHref("http://searchserver?project&binning_state=deselect-nca");f.setSelected(true);
		lstFac.add(f);
		f = new Facet();f.setTitle("California - Southern"); f.setCount(1590); f.setSelectHref("http://searchserver?project&binning_state=select-sca");
		lstFac.add(f);
		fg.setFacets(lstFac);
		lstFGroups.add(fg);

		fg = new FacetGroup();
		f = new Facet();
		fg.setFacetKey("Category");
		lstFac = new ArrayList<Facet>();
		f = new Facet();f.setTitle("Health articles"); f.setCount(1370); f.setSelectHref("http://searchserver?project&binning_state=select-hat");
		lstFac.add(f);
		fg.setFacets(lstFac);
		lstFGroups.add(fg);

		return lstFGroups;
	}

	private SearchCriteria createSearchCriteriaForTest(){
		SearchCriteria sc = new SearchCriteria();
		sc.setResultsCount("94");
		sc.setKeyword("oakland");
		return sc;
	}

	private List<PageLink> createPageLinksForTest(){
		// testdata for pagination links
		List<PageLink> lstN = new ArrayList<PageLink>();
		PageLink navPrev = new PageLink();
		navPrev.setHref("href_prev");navPrev.setTitle("Previous");
		lstN.add(navPrev);
		PageLink nav = new PageLink();
		lstN.add(nav);
		PageLink navNext = new PageLink();
		navNext.setHref("href_next");navNext.setTitle("Next");
		lstN.add(navNext);
		return lstN;
	}

	private List<Content> createSearchResultsForTest(){
		// testdata for search results
		List<Content> lstC = new ArrayList<Content>();
		Content c = new Content();
		c.setLinkValue("https://www.kaiserpermanante.org");
		c.setTitle("kp.org");
		c.setSnippet("Features and services from Kaiser Permanante");		
		c.setIndex(1);
		lstC.add(c);
		
		c = new Content();
		c.setLinkValue("https://www.kaiserpermanante-intranet.org");
		c.setTitle("kpintranet.org");
		c.setSnippet("Features and services from Kaiser Permanante for employees");		
		c.setIndex(2);
		lstC.add(c);
		
		return lstC;
	}	
}
