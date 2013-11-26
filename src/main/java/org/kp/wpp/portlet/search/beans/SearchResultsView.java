/**
 * 
 */
package org.kp.wpp.portlet.search.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author l525045
 *
 */
public class SearchResultsView implements Serializable{

	private static final long serialVersionUID = 1L;
	private SearchCriteria searchCriteria;
	private List<Content> contents;
	private List<PageLink> navigationLinks;
	private List<FacetGroup> selectableFacetGroups;
	private List<FacetGroup> selectedFacetGroups;

	/**
	 * @return the contents
	 */
	public List<Content> getContents() {
		return contents;
	}
	/**
	 * @param contents the contents to set
	 */
	public void setContents(final List<Content> contents) {
		this.contents = contents;
	}
	/**
	 * @return the navigationLinks
	 */
	public List<PageLink> getNavigationLinks() {
		return navigationLinks;
	}
	/**
	 * @param navigationLinks the navigationLinks to set
	 */
	public void setNavigationLinks(final List<PageLink> navigationLinks) {
		this.navigationLinks = navigationLinks;
	}
	/**
	 * @return the selectableFacetGroups
	 */
	public List<FacetGroup> getSelectableFacetGroups() {
		return selectableFacetGroups;
	}
	/**
	 * @param selectableFacetGroups the selectableFacetGroups to set
	 */
	public void setSelectableFacetGroups(final List<FacetGroup> facetGroups) {
		this.selectableFacetGroups = facetGroups;
	}
	/**
	 * @return the searchCriteria
	 */
	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}
	/**
	 * @param searchCriteria the searchCriteria to set
	 */
	public void setSearchCriteria(final SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	/**
	 * @return the selectedFacetGroups
	 */
	public List<FacetGroup> getSelectedFacetGroups() {
		return selectedFacetGroups;
	}

	/**
	 * @param selectedFacetGroups the selectedFacetGroups to set
	 */
	public void setSelectedFacetGroups(final List<FacetGroup> selectedFacetGroups) {
		this.selectedFacetGroups = selectedFacetGroups;
	}
}
