<%@ page session="false" contentType="text/html" pageEncoding="UTF-8" import="java.util.*,javax.portlet.*"%>
<%@ page import="javax.portlet.*,java.util.*"%>
<%@ page import="org.kp.wpp.portlet.search.constants.IPortletConstants"%>
<%@ page import="org.kp.wpp.portlet.search.utils.FormatUtil"%>
<%@ page import="org.kp.wpp.portlet.search.beans.*"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<portlet:defineObjects/>

<%@ taglib uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model" prefix="portlet-client-model"%>

<%
SearchCriteria srv = (SearchCriteria)request.getAttribute("SEARCH_GENERIC_VIEW_BEAN");
String label = FormatUtil.updateWithHTMLCharacters(renderRequest.getParameter(IPortletConstants.SEARCH_RESULTS_LABEL));
pageContext.setAttribute("srv", srv);
String error = (String) request.getAttribute(IPortletConstants.REQ_RESULTERROR);

String pLandingPage1 = portletPreferences.getValue(IPortletConstants.PREF_SEARCHLANDINGPAGE,"");
pageContext.setAttribute("pLandingPage", pLandingPage1);
System.out.println("The LANDING PAGE from preferences is:" + pLandingPage1);
%>

<%String fmtR = "org.kp.wpp.search.properties."+portletConfig.getPortletName();%>
<fmt:setBundle basename="<%=fmtR%>"/>
<script type="text/javascript"> <%-- link style sheets on demand --%>
<%
StringBuffer sb = new StringBuffer();
String[] ss = portletPreferences.getValues(IPortletConstants.PREF_SEARCHSTYLES,new String[0]);
for (int idx = 0; idx < ss.length; idx++) { if (idx > 0) sb.append(","); sb.append(ss[idx]); }
%>
dojo.require("kaiser.wpp.style");
kaiser.wpp.style.link(
     '<%=renderResponse.encodeURL(renderRequest.getContextPath())%>'
    ,'<%=sb.toString()%>');
var <portlet:namespace/>backToHref = "javascript:history.back();";
</script>
<%if(null != error){%>
    <div class="wppErrorPage">
        <h1><fmt:message key="search.error.sorry"/></h1>
		<%if(null != label){%>
	         <div><p>Your search for "<%=label%>" did not find any matches.</p>
		<%}else{%>
	         <div><p>Your search did not find any matches.</p>
		<%}%>
	    <p>For more information on a variety of health topics, please visit our 
	    <a href="/health/care/consumer/health-wellness/conditions-diseases/health-encyclopedia">health encyclopedia</a> or our 
	    <a href="/health/care/consumer/health-wellness/conditions-diseases/enciclopedia-de-la-salud">enciclopedia de la salud</a>.</p></div>
    </div>
<%}else{%>
<div class="searchResultContainer">
    <div class="searchResult">
    <div class="mgsMarginTopErrorHCO"></div>
    <%@include file="ResultsPage.jspf"%>
    <%@include file="Paging.jspf"%>
    <div class="keywordSearchContainer">
        <div class="wppHeaderFragment">
        <div id="DivSearchResultsHeader"><h1><fmt:message key="search.default.text.header"/></h1></div>
        <div class="wppIntroFragment"><img class="wppIntroIcon"
            src="<%=response.encodeURL(request.getContextPath()+"/styles/private/images/si_mmr_home.jpg")%>"
            alt=""/>
        <div class="wppIntroNoSubhead"></div>
        <div id="wppPageTool" class="wppPageTool">
            <div class="wppPageToolTitle"></div>
            <div class="wppPageToolContent"><a class="printer" href="javascript:kaiser.wpp.PAGE_TOOL.printerFriendly(this);">
            <fmt:message key="search.default.link.print"/><span class="wptheme-access">Link will open in a new window</span></a>
        </div>
        <div class="ieCornerWrapBS"><span class="ieCornerBLS2"></span><span class="ieCornerBRS2"></span></div>
        </div>
        </div>
        </div>
    </div>
    </div>
    <div class="searchFacet">
        <div class="newSearch">            
            <a lang='<fmt:message key="search.selected.lang.value"/>' 
            href='<portlet:actionURL>
            		    <portlet:param name="<%=IPortletConstants.VALUE_STARTNEWSEARCH%>" value="<%=IPortletConstants.VALUE_STARTNEWSEARCH%>"/>
            		    <portlet:param name="<%=IPortletConstants.SEARCH_FROM_FEATURE%>" value="NCE"/>
            		    
            	  </portlet:actionURL>'>
              <fmt:message key="search.default.link.startnewsearch"/>
            </a>
        </div>
        <%List<FacetGroup> lstSelectedFGroups =  (List<FacetGroup>)request.getAttribute("BREADCRUMBS_LIST");
        pageContext.setAttribute("lstSelectedFacets", lstSelectedFGroups);%>
        <div id="BreadcrumbsDiv" class="facetBreadcrumb">
        <h3><fmt:message key="search.nbpath.text.currentsearch"/></h3>
        <strong><h6>Health encyclopedia</h6></strong>
        <c:forEach var="curfG" items="${lstSelectedFacets}">
            <c:forEach var="curFacet" items="${curfG.facets}">
                <c:if test="${curFacet.selected}">
                    <strong><span class="searchpath">${curfG.facetKey}</span></strong>
                    <div>${curFacet.title}
                    <a class="facetClear" href='<portlet:actionURL>
                        <portlet:param name="<%=IPortletConstants.SEARCHREFRESHURL%>" value"${curFacet.deSelectHref}"/>
                        </portlet:actionURL>'><fmt:message key="search.nbpath.link.clear"/></a>
                    </div>
                </c:if>
            </c:forEach>
        </c:forEach>
        </div>
        <%@include file="NarrowByPage.jspf"%>
    </div>
</div>
<%}%>