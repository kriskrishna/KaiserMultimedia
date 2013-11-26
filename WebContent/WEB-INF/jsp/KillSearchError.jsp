<%@page session="false" contentType="text/html" pageEncoding="UTF-8" import="java.util.*,javax.portlet.*" %>
<%@ page language="java" import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>        
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setBundle basename="org.kp.wpp.search.properties.KaiserSearchPortlet"/>

<script type="text/javascript"> <%-- link style sheets on demand --%>;
dojo.require("kaiser.wpp.style");
dojo.addOnLoad(function(){dojo.publish("browserTitle",['Kaiser Permanente']);});
</script>

<div class="wppErrorPage">
     <h1><fmt:message key="search.error.sorry"/></h1>
     <div>
          <p><fmt:message key="search.error.outage"/></p>
     </div>
</div>