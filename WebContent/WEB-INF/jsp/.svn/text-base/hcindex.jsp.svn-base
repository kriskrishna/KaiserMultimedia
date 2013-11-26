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
<%@ taglib uri="http://wpp.kp.org/wppcontent" prefix="content" %>
<portlet:defineObjects/>
<%
List<Region> regions = ROPMap.getRegionList();
Map<String, List<Facet>> browseFacets =  (Map<String, List<Facet>>) portletSession.getAttribute("BROWSE_LIST");
String error = (String) request.getAttribute(IPortletConstants.REQ_RESULTERROR);
String dispContentForNCHC = (String) request.getAttribute(IPortletConstants.DISPLAY_NOCO_FRAG_FOR_HC);
String preSelectedLang = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_LANGUAGE);
String preSelectedRop = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
String preSelectedCookie = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_ROPCOOKIE);
pageContext.setAttribute("preSelectedCookie", preSelectedCookie);
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
function <portlet:namespace/>openTarget(){
    var sUrl = dojo.byId("<portlet:namespace/>CityVal").value;
    var w = dojo.byId("<portlet:namespace/>CityVal").selectedIndex;
    var selected_city = dojo.byId("<portlet:namespace/>CityVal").options[w].text;
    if (sUrl != ""){window.open(sUrl,"_self");}
}
function <portlet:namespace/>addErrorClassInput(divId){
    dojo.byId(divId).className=dojo.byId(divId).className+" mgsErrorInput";
}
</script>
<div class="mgsMastheadContainer">
    <div class="mgsPhoto"><span class="mgs_accessibility">background image Kaiser default</span></div>
    <div class="mgsInfo">
      <h1 class="mgsInfoTitle">Get inspired</h1>
      Find programs to support your health goals.
    </div>
    <div class="mgsNub"></div>
     <div style="clear:both;"></div>
</div>
<div class="mgs_maincontent">
    <div class="mgs_main1">
        <div class="mgs_help_lbl mgs_col_left"> </div>
        <div class="mgs_help_cont mgs_col_right">
          <div class="wppHeaderFragment">
                    <div id="DivFeatureHeader" class="printArea" lang='<fmt:message key="search.selected.lang.value"/>'>
                        <h1 class="wppIntroNoIcon"><fmt:message key="search.index.label.feature"/></h1>
                    </div>
                    <div class="wppIntroFragment">
                        <div class="wppIntroNoIcon"/>
                            <div class="printArea">
                                <div class="wppIntroNoSubhead"/>    </div>
                                <div id="DivFeatureSubHeader" class="printArea" lang='<fmt:message key="search.selected.lang.value"/>'>
                                    <h1 class="subhead"><fmt:message key="search.index.label.feature2"/></h1>
                                </div>
                            </div>
                        </div>
                    </div>
            </div>  
            <div class="wppPageTool" id="hc_wppPageTool">
                    <div class="wppPageToolTitle"/></div>
                    <div id="hc_wppPageToolContent" class="wppPageToolContent">
                        <%if(StringUtils.equalsIgnoreCase(preSelectedLang,IPortletConstants.CGI_PARAM_SPANISH)){%>
                                <a lang='en' class="hcEspanolLink" href='<portlet:actionURL><portlet:param name="<%=IPortletConstants.SEARCH_LANGUAGESELECTOR%>" value="<%=IPortletConstants.LANGUAGE_ENGLISH%>"/></portlet:actionURL>'>
                                 In English</a>
                        <%}else{%>
                                <a lang='es' class="hcEspanolLink" href='<portlet:actionURL><portlet:param name="<%=IPortletConstants.SEARCH_LANGUAGESELECTOR%>" value="<%=IPortletConstants.LANGUAGE_SPANISH%>"/></portlet:actionURL>'>
                                En espa&ntilde;ol</a>
                        <%}%> 
                            <span class="bl"></span><span class="br"/></span>
                     </div>
           </div>                         
        </div>
        <div style="clear:both;"></div>
        <form name="<portlet:namespace/>SearchForm" target="_self" id="<portlet:namespace/>SearchSelectionForm" action='<portlet:actionURL/>'>
            <div class="mgs_doc_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'><fmt:message key="search.index.text.region"/><div class="mgs_pipe"></div></div>
            <div class="mgs_help_cont mgs_col_right">
                <div id="RegionSelectionDiv" class="mgs_drop_down_doc">
                       <select class="mgs_area_padding" alt="Select an area" style="width:250px" id="<portlet:namespace/>RegionDropDwn" name="<%=IPortletConstants.SEARCH_REGIONFILTER%>">
                         <option value='<%=IPortletConstants.VAL_DEFAULT_AREA_SELECT%>'><fmt:message key="search.index.regionselect.default"/></option>
                         <c:forEach var="region" items="<%=regions%>">
                             <c:choose><c:when test="${region.regionCode eq preSelectedCookie}">
                                <option value='<c:out value="${region.regionCode}"/>' selected><c:out value="${region.regionNm}"/></option>
                             </c:when><c:otherwise>
                                <option value='<c:out value="${region.regionCode}"/>'><c:out value="${region.regionNm}"/></option>
                             </c:otherwise></c:choose>
                         </c:forEach>
                       </select>
                   <input type="hidden" name=":<%=SecurityUtil.TOKEN_HOLDER%>" value="<%=SecurityUtil.getCSRFToken(pReq)%>" >    
                   <a id="ConfirmRegionGo" href="javascript:document.<portlet:namespace/>SearchForm.submit()" class="mgs_select_go" title="submit region selection"></a>
                </div>
                <% if (error != null && error.equals(IPortletConstants.ERROR_RGNCD)){%>
                <div id="SearchErrorDiv" class="wppInputErrorMessage mgsRegionError"><ul><li><fmt:message key="search.index.error.region"/></li></ul></div>
                <div style="clear:both;"></div> 
               <%}%>
            </div>        
            <div style="clear:both;"></div>
            <div class="mgs_spacer"> </div>
            <div class="mgs_help_cont mgs_col_right">
            </div>
            <div style="clear:both;"></div>
        </form>
    </div>
    <a name="SearchCriteria"></a>
    <%
    	if(StringUtils.isBlank(dispContentForNCHC)) {
     %>
    <div class="mgs_main2" id="DivSearchCriteria" style="display:none">
        <div class="mgs_start_search_lbl  mgs_col_left_1" lang='<fmt:message key="search.selected.lang.value"/>'> <fmt:message key="search.index.text.start"/> 
        <span lang='<fmt:message key="search.selected.lang.value"/>' class="mgs_accessibility"><fmt:message key="search.index.screenreader.start"/></span>
        </div>
        <div style="clear:both;"></div>
        <div class="mgs_keyword_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'> <fmt:message key="search.index.text.keyword"/> 
        </div>
        <div class="mgs_keyword_cont">
            <form class="mgs_landing_form" name="<portlet:namespace/>MGKWsearchForm" id="<portlet:namespace/>SearchKWForm" action='<portlet:actionURL/>' target="_self">
                <label class="wptheme-access" for="<%=IPortletConstants.SEARCHSTRING%>">keyword search</label>
                <input lang='<fmt:message key="search.selected.lang.value"/>' size="50" type="text" maxlength="50" id="<%=IPortletConstants.SEARCHSTRING%>" name="<%=IPortletConstants.SEARCHSTRING%>" 
                   onfocus="if (this.value==this.defaultValue) this.value='';" value='<fmt:message key="<%=IPortletConstants.RESOURCE_ENTRY_DEFAULT_KEYWORD%>"/>'/>
                <input class="mgsInputGo" type="submit" id="<%=IPortletConstants.ACTION_SUBMITQUERY%>" value="keywordsearch" name="<%=IPortletConstants.ACTION_SUBMITQUERY%>" alt='<fmt:message key="search.index.screenreader.kwd.submit"/>' title="submit search by keyword"/>
                <input type="hidden" name="<%=IPortletConstants.SEARCH_VIEW%>" value="<%=IPortletConstants.SEARCH_VIEW_LANDINGPAGE%>">
                <input type="hidden" name=":<%=SecurityUtil.TOKEN_HOLDER%>" value="<%=SecurityUtil.getCSRFToken(pReq)%>" >
                <%-- IE hack where IE doesn't submit form upon striking enter --%>
                <div style="display:none"><input type="text" name="hiddenText"/></div>
                <%-- end IE hack--%>
            </form>
            <br/>
        <%if(IPortletConstants.ERROR_EMPTYSEARCH.equals(error)){%>
            <div id="SearchErrorDiv" class="wppInputErrorMessage mgsKeywordError">
              <ul><li><fmt:message key="search.searchbox.text.enterkeyword"/></li></ul>
            </div>
             <script><portlet:namespace/>addErrorClassInput("<portlet:namespace/>SearchKWForm");</script>
        <%}%>
        </div>
        <div style="clear:both;"></div>
        <div class="mgs_area_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'>
            <fmt:message key="search.index.text.location"/><span class="mgs_accessibility">Search by area/location</span>
        </div>
        <div class="mgs_area_cont mgs_col_right">
            <%if(null!=browseFacets){
            List<Facet> lCities = browseFacets.get(IPortletConstants.FACET_TITLE_CITY);%>
                <div class="city_bg_select" id="citybgID">
                <select id="<portlet:namespace/>CityVal" name="CitySelect" class="mgs_area_padding" title="Select a city" lang='<fmt:message key="search.selected.lang.value"/>'>
                <option value="<portlet:actionURL><portlet:param name="<%=IPortletConstants.SEARCHREFRESHURL%>" value="Select city"/></portlet:actionURL>"><fmt:message key="search.index.cityselect.default"/></option>
                <c:forEach var="curFacet" items="<%=lCities%>">
                <option value="<portlet:actionURL><portlet:param name="<%=IPortletConstants.SEARCHREFRESHURL%>" value="${curFacet.selectHref}"/><portlet:param name="<%=IPortletConstants.SEARCHREFRESH_USERACTION%>" value="City|${curFacet.title}"/></portlet:actionURL>">${curFacet.title}</option>
                </c:forEach>
                </select>
                <a id="ConfirmCityGo" href="#SearchCriteria" onClick="javascript:<portlet:namespace/>openTarget()" class="mgs_select_go" title="submit to search by city"></a>
            </div>
            <%}%>
        </div>
        <% if (error != null){%>
            <div class="mgs_help_cont mgs_col_right">
               <%if (error.equals(IPortletConstants.ERROR_CITY)){%>
                   <div id="MgSearchErrorDiv" class="wppInputErrorMessage mgsZipError">
                    <ul><li><fmt:message key="search.index.error.city"/></li></ul>
                   </div>
                   <script><portlet:namespace/>addErrorClassInput("citybgID");</script>
                <%}%>
            </div><div style="clear:both;"></div>        
         <%}%>
        </div>
        <br/>
        <div style="clear:both;"></div>
        <%if(null!=browseFacets){
        List<Facet> lPrograms = browseFacets.get(IPortletConstants.FACET_TITLE_PROGRAM_TYPE);
        pageContext.setAttribute("lstBrowsePrograms", lPrograms);%>
        <div id="DivBrowseTypeLabel" class="mgs_specialty_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'><fmt:message key="search.index.text.type"/></div>
        <div id="DivBrowseTypeLinks" class="mgs_specialty_cont mgs_col_right" lang='<fmt:message key="search.selected.lang.value"/>'>
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
                    <a href='<portlet:actionURL><portlet:param name="<%=IPortletConstants.SEARCHREFRESHURL%>" value="${curFacet.selectHref}"/><portlet:param name="<%=IPortletConstants.SEARCHREFRESH_USERACTION%>" value="Program type|${curFacet.title}"/></portlet:actionURL>'>
                    ${curFacet.title}</a>
                </div>
            </c:forEach>
            </td></tr></table>
        </div>
        <div style="clear:both;"></div>
        <%List<Facet> lHTopics = browseFacets.get(IPortletConstants.FACET_TITLE_HEALTH_TOPIC);
        pageContext.setAttribute("lstBrowseHealth", lHTopics);%>
        <div id="DivBrowseTopicLabel" class="mgs_specialty_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'><fmt:message key="search.index.text.topic"/></div>
        <div id="DivBrowseTopicLinks" class="mgs_specialty_cont mgs_col_right" lang='<fmt:message key="search.selected.lang.value"/>'>
            <c:choose>
                <c:when test="${fn:length(lstBrowseHealth) mod 2 == 0 }">
                    <c:set var="hBreakPosition" value="${fn:length(lstBrowseHealth) / 2}" scope="page"/>
                </c:when>
                <c:otherwise>
                    <c:set var="hBreakPosition" value="${(fn:length(lstBrowseHealth)+1) / 2}" scope="page"/>
                </c:otherwise>
            </c:choose>
            <table><tr><td class="mc_column"> 
            <c:forEach var="curFacet" items="${lstBrowseHealth}" varStatus="curIndex">
                <c:if test="${(curIndex.index) == hBreakPosition}">
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
    <%
    	} else if(dispContentForNCHC.equalsIgnoreCase("true")) {
     %>        
<%@include file="NC_searchOptions.jspf"%>
     <%
    	}
     %>         
</div>
<script type="text/javascript"> <%-- link style sheets on demand --%>


<% if(StringUtils.isNotBlank(preSelectedRop) && !IPortletConstants.VAL_DEFAULT_AREA_SELECT.equalsIgnoreCase(preSelectedRop)
        && null != browseFacets){%>
dojo.byId("DivSearchCriteria").style.display='block';
<%}%>
</script>
<% if (error != null && error.equals(IPortletConstants.ERROR_RGNCD)){%>
    <script><portlet:namespace/>addErrorClassInput("RegionSelectionDiv");</script>
<%}%>
<%@include file="TrackEvents.jspf"%>