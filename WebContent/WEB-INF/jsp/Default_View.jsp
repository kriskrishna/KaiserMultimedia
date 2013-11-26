<%@ page contentType="text/html"%>
<%@ page import="javax.portlet.*,java.util.*"%>
<%@ page import="org.kp.wpp.portlet.search.constants.IPortletConstants"%>
<%@ page import="org.kp.wpp.portlet.search.beans.*"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page session="false" contentType="text/html" pageEncoding="UTF-8" import="java.util.*,javax.portlet.*"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model" prefix="portlet-client-model"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<portlet:defineObjects/>
<%
SearchCriteria srv = (SearchCriteria)request.getAttribute("SEARCH_GENERIC_VIEW_BEAN");
pageContext.setAttribute("srv", srv);
String pLandingPage = portletPreferences.getValue(IPortletConstants.PREF_SEARCHLANDINGPAGE,"");
pageContext.setAttribute("pLandingPage", pLandingPage);
String introIconImage = portletPreferences.getValue(IPortletConstants.PREF_SEARCHINTROICON,"");
pageContext.setAttribute("introIcon", introIconImage);
String helpLink = portletPreferences.getValue(IPortletConstants.PREF_HELPPAGEURL,"");
pageContext.setAttribute("helpLink", helpLink);
String glossaryLink = portletPreferences.getValue(IPortletConstants.PREF_GLOSSARYPAGEURL,"");
pageContext.setAttribute("glossaryLink", glossaryLink);
String searchXMLProject = portletPreferences.getValue(IPortletConstants.PREF_SEARCHPROJECT,"");
pageContext.setAttribute("searchXMLProject", searchXMLProject);
String preSelectedLang = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_LANGUAGE);
String error = (String) request.getAttribute(IPortletConstants.REQ_RESULTERROR);
Boolean pcpGate = (Boolean) portletSession.getAttribute(IPortletConstants.GATED_PCP);
String pcpGateNo =  (String)portletSession.getAttribute(IPortletConstants.GATED_PCP_NO_VALUE);
String pcpGateYes = (String)portletSession.getAttribute(IPortletConstants.GATED_PCP_YES_VALUE);
Boolean gatedPlanProviderSearch = (Boolean) portletSession.getAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH);
String providerGateAcion=(String)portletSession.getAttribute(IPortletConstants.GATED_PROVIDER_ACTION);
String planGateAcion=(String)portletSession.getAttribute(IPortletConstants.GATED_PLAN_ACTION);
String preSelectedMG = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH);
String preSelectedRop = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
pageContext.setAttribute("providerGateAcion", providerGateAcion);
pageContext.setAttribute("planGateAcion", planGateAcion);
Boolean thisIsMGSearch = (preSelectedMG!=null && (StringUtils.equalsIgnoreCase(IPortletConstants.MGSELECTION_LOCATIONS, preSelectedMG) || StringUtils.equalsIgnoreCase(IPortletConstants.MGSELECTION_DOCTORS, preSelectedMG)));
Boolean thisIsSiteSearch = (searchXMLProject!=null && (StringUtils.equalsIgnoreCase("kp-consumernet", searchXMLProject)));
%>
<%-- Error message if present, will be used in the jspfs.. --%>
<%if(null != preSelectedLang){%><fmt:setLocale value="<%=preSelectedLang%>" scope="page"/><%}%>
<%String fmtR = "org.kp.wpp.search.properties."+portletConfig.getPortletName();%>

<fmt:setBundle basename="<%=fmtR%>"/>

<script type="text/javascript"> <%-- link style sheets on demand --%>
<%
AuxiliaryContent ac = (AuxiliaryContent) portletSession.getAttribute("CONTENTBEAN");
pageContext.setAttribute("ac", ac);
StringBuffer sb = new StringBuffer();
String[] ss = portletPreferences.getValues(IPortletConstants.PREF_SEARCHSTYLES,new String[0]);
for (int idx = 0; idx < ss.length; idx++) { if (idx > 0) sb.append(","); sb.append(ss[idx]); }
%>
dojo.require("kaiser.wpp.style");
kaiser.wpp.style.link(
     '<%=renderResponse.encodeURL(renderRequest.getContextPath())%>'
    ,'<%=sb.toString()%>');
function allowOnlyNumbers(evt)
{
   var charCode = (evt.which) ? evt.which : event.keyCode
   if (charCode > 31 && (charCode < 48 || charCode > 57))
      return false;

   return true;
}
</script>
<div class="searchResultContainer">
	<%if(thisIsMGSearch){%>
    	<div class="searchResult" id="searchResultMG">
    <%} else {%>
    	<div class="searchResult">
    <%}%>
    <%@ include file="Summary.jspf"%>
    <%if(thisIsMGSearch){%>
    	<%@include file="ResultsMGPage.jspf"%>
    <%} else {%>
    	<%@include file="ResultsPage.jspf"%>
    <%}%>
    <%@include file="Paging.jspf"%>
    <div class="keywordSearchContainer">
        <div class="wppHeaderFragment">
        <%if(thisIsMGSearch){%>
        	<%if (StringUtils.equalsIgnoreCase(IPortletConstants.MGSELECTION_DOCTORS, preSelectedMG)){ %>
        	<div id="DivSearchResultsHeader"><h1 lang='<fmt:message key="search.selected.lang.value"/>'>${srv.regionFilter} <fmt:message key="search.default.text.header.doctor"/></h1></div>
        	<%} else {%>
        	<div id="DivSearchResultsHeader"><h1 lang='<fmt:message key="search.selected.lang.value"/>'>${srv.regionFilter} <fmt:message key="search.default.text.header.facility"/></h1></div>
        	<%} %>
    	<%} else {%>
    		<div id="DivSearchResultsHeader"><h1 lang='<fmt:message key="search.selected.lang.value"/>'><fmt:message key="search.default.text.header"/></h1></div>
    	<%}%>
        <div class="wppIntroFragment">
		<c:choose>
		<c:when test="${not empty introIcon}">
            <img class="wppIntroIcon" src="${introIcon}"/>
		</c:when>
		<c:otherwise>
            <img class="wppIntroIcon" src="/static/health/images/spotlight_icons/common/si_dr_search.jpg"/>
		</c:otherwise>
		</c:choose>
		<%if(!thisIsMGSearch){%>
        <div class="wppIntroNoSubhead"></div>
        <div id="wppPageTool" class="wppPageTool">
            <div class="wppPageToolTitle"></div>
            <div class="wppPageToolContent"><a lang='<fmt:message key="search.selected.lang.value"/>' class="printer" href="javascript:kaiser.wpp.PAGE_TOOL.printerFriendly(this);">
            <fmt:message key="search.default.link.print"/><span class="wptheme-access">Link will open in a new window</span></a>
        </div>
        <div class="ieCornerWrapBS"><span class="ieCornerBLS2"></span><span class="ieCornerBRS2"></span></div>
        </div>
        <div class="keywordSearchLink">
           <c:if test="${not empty helpLink}">
           <a href="<%=portletPreferences.getValue(IPortletConstants.PREF_HELPPAGEURL,"")%>">
           <fmt:message key="search.default.link.help"/></a></c:if>
           <c:if test="${not empty glossaryLink}"> | 
           <a href="<%=portletPreferences.getValue(IPortletConstants.PREF_GLOSSARYPAGEURL,"")%>">
           <fmt:message key="search.default.link.glossary"/></a>
           </c:if>
        </div>
        <%}%>
        </div>
        </div>
        
        <%if (!(preSelectedMG!=null && (StringUtils.equalsIgnoreCase(IPortletConstants.MGSELECTION_LOCATIONS, preSelectedMG) || StringUtils.equalsIgnoreCase(IPortletConstants.MGSELECTION_DOCTORS, preSelectedMG)))) {%>
        	<%@ include file="SearchBox.jspf"%>
        	<%if(thisIsSiteSearch){%>
        		${ac.siteSearchLinks}
        	<%}%>
        <% } else{ %>
        	<style>div.mgsMarginTopError{margin-top:120px!important}</style>
        <%}%>
    </div>
    <c:if test="${(not empty ac.linkBucket1Title || not empty ac.linkBucket2Title) || not empty ac.disclaimer}">
    <%if(thisIsMGSearch){%>
	    <div class="searchMGLinkDisclaimerContainer">
    <%}%>
    </c:if>
    <c:if test="${not empty ac.linkBucket1Title || not empty ac.linkBucket2Title}">
    
    <%if(thisIsMGSearch){%>
    	<div class="searchMGQuickLinkContainer">
	       <div class="searchMGQuickLink">
	           <h2>${ac.linkBucket1Title}</h2>
	           ${ac.linkBucket1}
	       </div>
	       <div class="searchMGQuickLink">
	           <h2>${ac.linkBucket2Title}</h2>
	           ${ac.linkBucket2}
	       </div>
	    </div>
    <% } else{ %>
        <div class="searchQuickLinkContainer">
	       <div class="searchQuickLink">
	           <h2>${ac.linkBucket1Title}</h2>
	           <div class="wppHeaderStroke"><div class="wppHeaderStrokeIcon"></div></div>
	           ${ac.linkBucket1}
	       </div>
	       <div class="searchQuickLink">
	           <h2>${ac.linkBucket2Title}</h2>
	           <div class="wppHeaderStroke"><div class="wppHeaderStrokeIcon"></div></div>
	           ${ac.linkBucket2}
	       </div>
	    </div>
	    <br/>
    <%}%>
    
    </c:if>
    <c:if test="${not empty ac.disclaimer}">
    <%if(thisIsMGSearch){%>
    	<div class="searchMGResultDisclaimer printArea">${ac.disclaimer}</div>
    <% } else{ %>
    	<div class="searchResultDisclaimer printArea">${ac.disclaimer}</div>
    <%}%>
    </c:if>   
    <c:if test="${(not empty ac.linkBucket1Title || not empty ac.linkBucket2Title) || not empty ac.disclaimer}">
    <%if(thisIsMGSearch){%>
	    </div>
    <%}%>
    </c:if>
    </div>
    <%if(thisIsMGSearch){%>
    	<div class="searchFacet" id="searchFacetMG">
    <%} else {%>
    	<div class="searchFacet">
    <%}%>
        <c:if test="${not empty pLandingPage}">
	        <%if(thisIsMGSearch){%>
		    	<div class="newSearch" id="newSearchMG">
		    <%} else {%>
		    	<div class="newSearch">
		    <%}%>
        	<%if(request.getParameter("isSearchFromMG")!=null){ %>
            <a lang='<fmt:message key="search.selected.lang.value"/>' 
            href='<portlet:actionURL>
            		<portlet:param name="<%=IPortletConstants.SEARCH_MGSELECTION%>" value="<%=preSelectedMG %>"/>
            		<portlet:param name="<%=IPortletConstants.SEARCH_DOC_FILTER%>" value="<%=preSelectedRop %>"/>
            		<portlet:param name="<%=IPortletConstants.SEARCH_LOC_FILTER%>" value="<%=preSelectedRop %>"/>
            		<portlet:param name="<%=IPortletConstants.VALUE_SUB_STARTNEWSEARCH%>" value="<%=IPortletConstants.VALUE_SUB_STARTNEWSEARCH%>"/>
            	</portlet:actionURL>'>
             <fmt:message key="search.default.link.startnewsearch"/>
            </a>
            <%}else{ %>
              <a lang='<fmt:message key="search.selected.lang.value"/>' 
            href='<portlet:actionURL><portlet:param name="<%=IPortletConstants.VALUE_STARTNEWSEARCH%>" value="<%=IPortletConstants.VALUE_STARTNEWSEARCH%>"/></portlet:actionURL>'>
              <fmt:message key="search.default.link.startnewsearch"/>
            </a>
            <%} %>
        </div>
        </c:if>
        <%@include file="NarrowByPath.jspf"%>
        <%@include file="NarrowByPage.jspf"%>
    </div>
</div>
<%@include file="TrackEvents.jspf"%>










