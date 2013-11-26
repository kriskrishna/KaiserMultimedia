<%@ page session="false" contentType="text/html" pageEncoding="UTF-8" import="java.util.*,javax.portlet.*"%>
<%@ page import="javax.portlet.*,java.util.*"%>
<%@ page import="org.kp.wpp.portlet.search.utils.ROPMap"%>
<%@ page import="org.kp.wpp.portlet.search.constants.IPortletConstants" %>
<%@ page import="org.kp.wpp.portlet.search.beans.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.kp.security.util.SecurityUtil" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<portlet:defineObjects/>
<%
String preSelectedLang = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_LANGUAGE);
String error = (String) request.getAttribute(IPortletConstants.REQ_RESULTERROR);
Map<String, List<Facet>> browseFacets =  (Map<String, List<Facet>>) portletSession.getAttribute("BROWSE_LIST");
PortletRequest pReq = (PortletRequest) request.getAttribute("javax.portlet.request");
%>
<%if(null != preSelectedLang){%><fmt:setLocale value="<%=preSelectedLang%>" scope="page"/><%}%>
<%String fmtR = "org.kp.wpp.search.properties."+portletConfig.getPortletName();%>
<fmt:setBundle basename="<%=fmtR%>"/>
<script type="text/javascript"> <%-- link style sheets on demand --%>
<%{
StringBuffer sb = new StringBuffer();
String[] ss = portletPreferences.getValues(IPortletConstants.PREF_SEARCHSTYLES,new String[0]);
for (int idx = 0; idx < ss.length; idx++) { if (idx > 0) sb.append(","); sb.append(ss[idx]); }
%>
dojo.require("kaiser.wpp.style");
kaiser.wpp.style.link(
     '<%=renderResponse.encodeURL(renderRequest.getContextPath())%>'
    ,'<%=sb.toString()%>');
<% } %>
function <portlet:namespace/>addErrorClassInput(divId){
    dojo.byId(divId).className=dojo.byId(divId).className+" mgsErrorInput";
}
</script>
<%-- 
<div class="mgsMastheadContainer">
    <div class="mgsPhoto"><span class="mgs_accessibility">background image Kaiser default</span></div>
    <div class="mgsInfo">
      <h1 class="mgsInfoTitle">Generic Search - Landing Page</h1>
      (Placeholder for promotional text).
    </div>
    <div class="mgsNub"></div>
    <div style="clear:both;"></div>
</div>
--%>
<div class="mgs_maincontent">
    <div class="mgs_main1">
        <div class="he_introText" class="printArea" lang='<fmt:message key="search.selected.lang.value"/>'>
            <div class="wppHeaderFragment">
                <h1 class="wppIntroNoIcon"><fmt:message key="search.index.label.feature"/></h1>
                <div class="wppIntroFragment">
                    <div class="wppIntroNoIcon"></div>
                    <h1 class="subhead"><fmt:message key="search.index.label.feature2"/></h1>
                 </div>
            </div>
            <div class="wppPageTool" id="hc_wppPageTool">
                <div class="wppPageToolTitle"/></div>
                <div id="hc_wppPageToolContent" class="wppPageToolContent">
                          <%if(StringUtils.equalsIgnoreCase(preSelectedLang,IPortletConstants.CGI_PARAM_SPANISH)){%>
                                  <a lang='en' class="hcEnglishLink" href='/health/care/consumer/health-wellness/conditions-diseases/health-encyclopedia'>
                                   In English</a>
                          <%}else{%>
                                  <a lang='es' class="hcEspanolLink" href='/health/care/consumer/health-wellness/conditions-diseases/enciclopedia-de-la-salud'>
                                  En espa&ntilde;ol</a>
                          <%}%> 
                    <span class="bl"></span><span class="br"/></span>
                </div>
           </div>   
        </div>
        <div style="clear:both;"></div>
    </div>
    <div style="clear:both;"></div>
    <a name="SearchCriteria"></a>
    <div class="mgs_main2" id="mgsearchcriteriadiv">
        <div class="mgs_start_search_lbl  mgs_col_left_1" lang='<fmt:message key="search.selected.lang.value"/>'> <fmt:message key="search.index.text.start"/> <span class="mgs_accessibility">keyword, symptoms, or topic</span></div>
        <div style="clear:both;"></div>
        <div class="mgs_keyword_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'> <fmt:message key="search.index.text.keyword"/> </div>
        <div class="mgs_keyword_cont">
            <form class="mgs_landing_form" name="<portlet:namespace/>MGKWsearchForm" id="<portlet:namespace/>MGSearchKWForm" action='<portlet:actionURL/>' target="_self" method="post">
                <label class="wptheme-access" for="<%=IPortletConstants.SEARCHSTRING%>">keyword search</label>
                <input size="50" type="text" maxlength="50" id="<%=IPortletConstants.SEARCHSTRING%>" name="<%=IPortletConstants.SEARCHSTRING%>" 
                   onfocus="if (this.value==this.defaultValue) this.value='';" value='<fmt:message key="<%=IPortletConstants.RESOURCE_ENTRY_DEFAULT_KEYWORD%>"/>'/>
                <input class="mgsInputGo" type="submit" id="<%=IPortletConstants.ACTION_SUBMITQUERY%>" title="submit keyword search" value="keywordsearch" name="<%=IPortletConstants.ACTION_SUBMITQUERY%>" alt="submit search by keyword"/>
                <input type="hidden" name="<%=IPortletConstants.SEARCH_VIEW%>" value="<%=IPortletConstants.SEARCH_VIEW_LANDINGPAGE%>">
                <input type="hidden" name=":<%=SecurityUtil.TOKEN_HOLDER%>" value="<%=SecurityUtil.getCSRFToken(pReq)%>" >
                <%-- IE hack where IE doesn't submit form upon striking enter --%>
                <div style="display:none"><input type="text" name="hiddenText"/></div>
                <%-- end IE hack--%>
            </form>
            <br/>
        <%if(IPortletConstants.ERROR_EMPTYSEARCH.equals(error)){%>
            <div id="MgSearchErrorDiv" class="wppInputErrorMessage mgsKeywordError">
              <ul><li><fmt:message key="search.searchbox.text.enterkeyword"/></li></ul>
            </div>
             <script><portlet:namespace/>addErrorClassInput("<portlet:namespace/>MGSearchKWForm");</script>
        <%}%>
        </div>
        <div style="clear:both;"></div>
        <div id="DivSymptomCheckerLabel" class="mgs_specialty_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'><fmt:message key="search.index.text.symptom"/></div>
        <div id="DivSymptomCheckerLinks" class="mgs_specialty_cont mgs_col_right" lang='<fmt:message key="search.selected.lang.value"/>'>
            <div class="mgs_last_link">
                <%if(StringUtils.equalsIgnoreCase(preSelectedLang,IPortletConstants.CGI_PARAM_SPANISH)){%>
                      <a href='<%=portletPreferences.getValue(IPortletConstants.SYMPTOM_CHECKER_URL_SPANISH,IPortletConstants.SYMPTOM_CHECKER_URL_SPANISH)%>'> <fmt:message key="search.index.link.symptom"/></a>
          		<%}else{%>
                      <a href='<%=portletPreferences.getValue(IPortletConstants.PREF_SYMPTOM_CHECKER,IPortletConstants.DEFAULT_SYMPTOM_CHECKER_URL)%>'> <fmt:message key="search.index.link.symptom"/></a>
                <%}%>
            </div>
        </div>        
        <div style="clear:both;"></div>
        <%if(null!=browseFacets){
        List<Facet> lPrograms = browseFacets.get(IPortletConstants.FACET_TITLE_HEALTH_TOPIC);
        pageContext.setAttribute("lstBrowsePrograms", lPrograms);%>
        <div id="DivBrowseTopicLabel" class="mgs_specialty_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'><fmt:message key="search.index.text.topic"/></div>
        <div id="DivBrowseTopicLinks" class="mgs_specialty_cont mgs_col_right" lang='<fmt:message key="search.selected.lang.value"/>'>
            <c:choose>
                <c:when test="${fn:length(lstBrowsePrograms) mod 2 == 0 }">
                    <c:set var="pBreakPosition" value="${fn:length(lstBrowsePrograms) / 2}" scope="page"/>
                </c:when>
                <c:otherwise>
                    <c:set var="pBreakPosition" value="${(fn:length(lstBrowsePrograms)+1) / 2}" scope="page"/>
                </c:otherwise>
            </c:choose>
            <table><tr><td class="mc_column">
            <c:forEach var="curFacet" items="${lstBrowsePrograms}" varStatus="curIndex">
                <c:if test="${(curIndex.index) == pBreakPosition}">
                    </td><td class="mc_column">
                </c:if>
                <div class="mgsSpecialtyLink">
                    <a href='<portlet:actionURL><portlet:param name="<%=IPortletConstants.SEARCHREFRESHURL%>" value="${curFacet.selectHref}"/><portlet:param name="<%=IPortletConstants.SEARCHREFRESH_USERACTION%>" value="Health topic|${curFacet.title}"/></portlet:actionURL>'>
                    ${curFacet.title}</a>
                </div>
            </c:forEach>
            </td></tr></table>
        </div>
        <div style="clear:both;"></div>        
        <%}%>
   </div>
   <br/>
</div>
<%@include file="TrackEvents.jspf"%>