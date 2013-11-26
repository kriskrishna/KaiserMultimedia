<%@page session="false" contentType="text/html" pageEncoding="UTF-8" import="java.util.*,javax.portlet.*" %>
<%@ page language="java" import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>        
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<portlet:defineObjects/>

<fmt:setBundle basename="org.kp.wpp.search.properties.KaiserMGSearchPortlet"/>

<script type="text/javascript"> <%-- link style sheets on demand --%>;
<% { // to make var local only %>
<% 
StringBuffer sb = new StringBuffer();
String[] ss = portletPreferences.getValues(IPortletConstants.PREF_SEARCHSTYLES,new String[0]);
for (int idx = 0; idx < ss.length; idx++) { if (idx > 0) sb.append(","); sb.append(ss[idx]); }
%>
dojo.require("kaiser.wpp.style");
kaiser.wpp.style.link(
    '<%=renderResponse.encodeURL(renderRequest.getContextPath())%>'
   ,'<%=sb.toString()%>');
<% } %>
</script>

<div class="wppErrorPage">
     <h1><fmt:message key="search.error.sorry"/></h1>
     <div>
          <p><fmt:message key="search.error.defaulttext"/></p>
     </div>
</div>
<%@include file="TrackEvents.jspf"%>