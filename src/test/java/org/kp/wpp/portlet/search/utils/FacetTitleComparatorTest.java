package org.kp.wpp.portlet.search.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.kp.wpp.portlet.search.beans.Facet;

public class FacetTitleComparatorTest extends TestCase {

	public void testTitleComparator(){
		List<Facet> lFacets = new ArrayList<Facet>();
		
		Facet f = new Facet();f.setTitle("Georgia"); f.setCount(1000);
		lFacets.add(f);
		
		f = new Facet();f.setTitle(null); f.setCount(20);
		lFacets.add(f);
		
		f = new Facet();f.setTitle(null); f.setCount(2);
		lFacets.add(f);

		f = new Facet();f.setTitle(""); f.setCount(10);
		lFacets.add(f);
		
		f = new Facet();f.setTitle("California - Northern"); f.setCount(1590);
		lFacets.add(f);

		f = new Facet();f.setTitle("Hawaii"); f.setCount(1222);
		lFacets.add(f);
		
		f = new Facet();f.setTitle("Ohio"); f.setCount(1695);
		lFacets.add(f);

		f = new Facet();f.setTitle("California - Southern"); f.setCount(889);
		lFacets.add(f);

		Collections.sort(lFacets, new FacetTitleComparator());

		assertEquals(10,lFacets.get(0).getCount());
		assertEquals(1590,lFacets.get(1).getCount());
		assertEquals(889,lFacets.get(2).getCount());
		assertEquals(1000,lFacets.get(3).getCount());
		assertEquals(1222,lFacets.get(4).getCount());
		assertEquals(1695,lFacets.get(5).getCount());
		assertEquals(20,lFacets.get(6).getCount());
		assertEquals(2,lFacets.get(7).getCount());
	}

}
