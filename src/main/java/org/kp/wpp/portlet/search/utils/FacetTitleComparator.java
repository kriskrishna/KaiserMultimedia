package org.kp.wpp.portlet.search.utils;

import java.util.Comparator;

import org.kp.wpp.portlet.search.beans.Facet;

public class FacetTitleComparator implements Comparator<Facet> {
	public int compare(final Facet facet1, final Facet facet2) {
		final String facetTitle1 = facet1.getTitle();
		final String facetTitle2 = facet2.getTitle();
  		if (null == facetTitle1 && null == facetTitle2) {return 0;}
      	if (null != facetTitle1 && null == facetTitle2) {return -1;}
        if (null == facetTitle1 && null != facetTitle2) {return 1;}
	    return facetTitle1.compareToIgnoreCase(facetTitle2);
	}
}
