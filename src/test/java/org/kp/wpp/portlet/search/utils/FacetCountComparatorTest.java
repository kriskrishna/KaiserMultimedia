package org.kp.wpp.portlet.search.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.kp.wpp.portlet.search.beans.Facet;

public class FacetCountComparatorTest extends TestCase {

	public void testCountComparator(){
		List<Facet> lFacets = new ArrayList<Facet>();
		
		Facet f = new Facet();f.setTitle("Cantonese"); f.setCount(1590);
		lFacets.add(f);

		f = new Facet();f.setTitle("Navi"); f.setCount(890);
		lFacets.add(f);

		f = new Facet();f.setTitle("English"); f.setCount(1222);
		lFacets.add(f);
		
		f = new Facet();f.setTitle("Spanish"); f.setCount(1695);
		lFacets.add(f);

		Collections.sort(lFacets, new FacetCountComparator());

		assertEquals("Spanish",lFacets.get(0).getTitle());
		assertEquals("Cantonese",lFacets.get(1).getTitle());
		assertEquals("English",lFacets.get(2).getTitle());
		assertEquals("Navi",lFacets.get(3).getTitle());
	}
}
