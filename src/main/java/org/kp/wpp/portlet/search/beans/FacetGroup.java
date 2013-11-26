package org.kp.wpp.portlet.search.beans;

import java.io.Serializable;
import java.util.List;

public class FacetGroup implements Serializable{

	private List<Facet> facets;
	private String facetKey; 
	
	/**
	 * @return the facets
	 */
	public List<Facet> getFacets() {
		return facets;
	}
	/**
	 * @param facets the facets to set
	 */
	public void setFacets(final List<Facet> facets) {
		this.facets = facets;
	}
	/**
	 * @return the facetKey
	 */
	public String getFacetKey() {
		return facetKey;
	}
	/**
	 * @param facetKey the facetKey to set
	 */
	public void setFacetKey(final String facetKey) {
		this.facetKey = facetKey;
	}

	private static final long serialVersionUID = 1L;
}
