
<%@ page session="false" contentType="text/html" pageEncoding="UTF-8" import="java.util.*,javax.portlet.*"%>
<%@ page import="javax.portlet.*,java.util.*"%>
<%@ page import="org.kp.wpp.portlet.search.utils.ROPMap"%>
<%@ page import="org.kp.wpp.portlet.search.utils.RadiusMap"%>
<%@ page import="org.kp.wpp.portlet.search.constants.IPortletConstants" %>
<%@ page import="org.kp.wpp.portlet.search.beans.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://wpp.kp.org/wppcontent" prefix="content" %>
<portlet:defineObjects/>
<%List<Region> regions = ROPMap.getRegionList();
List<Distance> distances = RadiusMap.getRadiusList();
String preSelectedRop = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_REGION);
String preSelectedMG = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH);
String preSelectedView = (String) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_SELECTED_MGSEARCH_LOCATION);
Boolean pcpGate = (Boolean) portletSession.getAttribute(IPortletConstants.GATED_PCP);
Boolean gatedPlanProviderSearch = (Boolean) portletSession.getAttribute(IPortletConstants.GATED_PLAN_PROVIDER_TYPE_SEARCH);
Boolean GatedPlanSearch = (Boolean) portletSession.getAttribute(IPortletConstants.GATED_PLAN_TYPE_SEARCH);
Boolean GatedProviderSearch = (Boolean) portletSession.getAttribute(IPortletConstants.GATED_PROVIDER_TYPE_SEARCH);
String pcpGateNo =  (String)portletSession.getAttribute(IPortletConstants.GATED_PCP_NO_VALUE);
String pcpGateYes = (String)portletSession.getAttribute(IPortletConstants.GATED_PCP_YES_VALUE);
Boolean ppGate=(Boolean) portletSession.getAttribute(IPortletConstants.GATED_PP);
String providerGateAcion=(String)portletSession.getAttribute(IPortletConstants.GATED_PROVIDER_ACTION);
String planGateAcion=(String)portletSession.getAttribute(IPortletConstants.GATED_PLAN_ACTION);



String strGateNo="";
String strGateYes="";
if(pcpGateNo!=null && pcpGateNo.equalsIgnoreCase(IPortletConstants.GATED_PCP_NO_VALUE)){
strGateNo="checked=checked";
}
if(pcpGateYes!=null && pcpGateYes.equalsIgnoreCase(IPortletConstants.GATED_PCP_YES_VALUE)){
strGateYes="checked=checked";
}
Boolean midAtlantic = (Boolean) portletSession.getAttribute(IPortletConstants.SESSION_PARAM_MID_EXCEPTION);
Map<String, List<Facet>> mpFGroups =  (Map<String, List<Facet>>) portletSession.getAttribute("BROWSE_LIST");
List<Facet> planType =  (List<Facet>) portletSession.getAttribute("lPlanType");
List<Facet> lProviderUpdate =  (List<Facet>) portletSession.getAttribute("lProvider");




AuxiliaryContent ac = (AuxiliaryContent) portletSession.getAttribute("CONTENTBEAN");
String error = (String) request.getAttribute(IPortletConstants.REQ_RESULTERROR);
pageContext.setAttribute("ac", ac);
pageContext.setAttribute("preSelectedRop", preSelectedRop);
pageContext.setAttribute("providerGateAcion", providerGateAcion);
pageContext.setAttribute("planGateAcion", planGateAcion);

%>
<fmt:setBundle basename="org.kp.wpp.search.properties.KaiserMGSearchPortlet"/>
<script type="text/javascript"> <%-- link style sheets on demand --%>
<% {
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



<%@include file="header.jspf"%>
<div id="mgs_bgcontent" alt="picture of a doctor">
<div class="mgs_maincontent">
	<%@include file="mgSelect.jspf"%>
	<%
		if(null != midAtlantic && midAtlantic.booleanValue()){%><%@include file="mid_searchOptions.jspf"%>
		<div style="clear:both;"></div>
		<%@include file="footer.jspf"%>
		<%}
   		//else if((preSelectedMG!=null && preSelectedMG.equalsIgnoreCase(IPortletConstants.MGSELECTION_LOCATIONS))||(pcpGate!=null && !pcpGate.booleanValue() && gatedPlanProviderSearch!=null && !gatedPlanProviderSearch.booleanValue())  || ((pcpGateNo!=null && pcpGateNo.equalsIgnoreCase(IPortletConstants.GATED_PCP_NO_VALUE)) || (pcpGateYes!=null && pcpGateYes.equalsIgnoreCase(IPortletConstants.GATED_PCP_YES_VALUE)|| (ppGate!=null && ppGate.booleanValue())))){ 
    	else if(pcpGate!=null || (preSelectedView != null && preSelectedView.equalsIgnoreCase(IPortletConstants.MGSELECTION_LOCATIONS))){%>
		<%@include file="searchMultipleOptions.jspf"%>
		<div style="clear:both;"></div>
		<%@include file="footer.jspf"%>
		<%}
	%>
     </div>
    <!--- SEARCH CONTAINER END--->
     
     
</div>
</div>
<%@include file="/WEB-INF/jsp/TrackEvents.jspf"%>














