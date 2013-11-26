package org.kp.wpp.portlet.search.utils;

import java.util.Comparator;

import org.kp.wpp.portlet.search.beans.Facet;

public class FacetCountComparator implements Comparator<Facet> {
	public int compare(final Facet facet1, final Facet facet2) {
		final Integer facetCount1 = Integer.valueOf(facet1.getCount());
		final Integer facetCount2 = Integer.valueOf(facet2.getCount());
	    return facetCount2.compareTo(facetCount1);
	}
}
