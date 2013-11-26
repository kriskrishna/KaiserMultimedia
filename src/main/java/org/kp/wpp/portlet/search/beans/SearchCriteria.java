package org.kp.wpp.portlet.search.beans;

import java.io.Serializable;

public class SearchCriteria implements Serializable{

	private static final long serialVersionUID = 1L;
	private String keyword;
	private String resultsCount;
	private String spellCorrectedKeyword;
	private String spellCorrectedHref;
	private boolean summaryDisplayFlag;
	private String project;
	private String regionFilter;
	private String userZipCode;
	private String searchAlphabet;

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return the resultsCount
	 */
	public String getResultsCount() {
		return resultsCount;
	}
	/**
	 * @param resultsCount the resultsCount to set
	 */
	public void setResultsCount(final String resultsCount) {
		this.resultsCount = resultsCount;
	}

	/**
	 * @return the spellCorrectedKeyword
	 */
	public String getSpellCorrectedKeyword() {
		return spellCorrectedKeyword;
	}
	/**
	 * @param spellCorrectedKeyword the spellCorrectedKeyword to set
	 */
	public void setSpellCorrectedKeyword(String spellCorrectedKeyword) {
		this.spellCorrectedKeyword = spellCorrectedKeyword;
	}
	/**
	 * @return the spellCorrectedHref
	 */
	public String getSpellCorrectedHref() {
		return spellCorrectedHref;
	}
	/**
	 * @param spellCorrectedHref the spellCorrectedHref to set
	 */
	public void setSpellCorrectedHref(String spellCorrectedHref) {
		this.spellCorrectedHref = spellCorrectedHref;
	}
	/**
	 * @return the summaryDisplayFlag
	 */
	public boolean isSummaryDisplayFlag() {
		return summaryDisplayFlag;
	}
	/**
	 * @param summaryDisplayFlag the summaryDisplayFlag to set
	 */
	public void setSummaryDisplayFlag(boolean summaryDisplayFlag) {
		this.summaryDisplayFlag = summaryDisplayFlag;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}
	/**
	 * @return the regionFilter
	 */
	public String getRegionFilter() {
		return regionFilter;
	}
	/**
	 * @param regionFilter the regionFilter to set
	 */
	public void setRegionFilter(String regionFilter) {
		this.regionFilter = regionFilter;
	}
	/**
	 * @return the userZipCode
	 */
	public String getUserZipCode() {
		return userZipCode;
	}
	/**
	 * @param userZipCode the userZipCode to set
	 */
	public void setUserZipCode(String userZipCode) {
		this.userZipCode = userZipCode;
	}
	/**
	 * @return the searchAlphabet
	 */
	public String getSearchAlphabet() {
		return searchAlphabet;
	}
	/**
	 * @param searchAlphabet the searchAlphabet to set
	 */
	public void setSearchAlphabet(String searchAlphabet) {
		this.searchAlphabet = searchAlphabet;
	}
}
