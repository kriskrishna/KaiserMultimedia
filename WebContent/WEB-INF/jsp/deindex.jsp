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
String error = (String) request.getAttribute(IPortletConstants.REQ_RESULTERROR);
String helpLink = portletPreferences.getValue(IPortletConstants.PREF_HELPPAGEURL,"");
pageContext.setAttribute("helpLink", helpLink);
String fmtR = "org.kp.wpp.search.properties."+portletConfig.getPortletName();
PortletRequest pReq = (PortletRequest) request.getAttribute("javax.portlet.request");%>
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
<div class="mgs_maincontent">
    <div class="mgs_main1">
        <div class="mgs_help_lbl mgs_col_left"> </div>
        <div class="mgs_help_cont mgs_col_right">   
          <div class="wppHeaderFragment">
			    <h1><fmt:message key="search.index.label.feature"/></h1>
			    <div class="wppIntroFragment">
			        <img alt="" src="/static/health/images/spotlight_icons/health_wellness/si_drug_enc.jpg" class="wppIntroIcon">
			        <h1 class="subhead"><fmt:message key="search.index.label.feature2"/></h1>	    
			    </div><!-- class="wppIntroFragment" -->
	      </div>
        </div>

        <div style="clear:both;"></div>
    </div>

    <c:if test="${not empty helpLink}">
    <div class="DE_tipsinspanish"><a lang="es" href="<%=portletPreferences.getValue(IPortletConstants.PREF_HELPPAGEURL,"")%>">
    <fmt:message key="search.default.link.help"/></a></div></c:if>

    <div class="mgs_main2" id="mgsearchcriteriadiv">
        <div class="mgs_start_search_lbl  mgs_col_left_1" lang='<fmt:message key="search.selected.lang.value"/>'> <fmt:message key="search.index.text.start"/> <span class="mgs_accessibility">keyword or letter</span></div>
        <div style="clear:both;"></div>
        <div class="mgs_keyword_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'> <fmt:message key="search.index.text.keyword"/> </div>
        <div class="mgs_keyword_cont">
            <form class="mgs_landing_form" name="<portlet:namespace/>MGKWsearchForm" id="<portlet:namespace/>MGSearchKWForm" action='<portlet:actionURL/>' target="_self">
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
        <div class="mgs_area_lbl  mgs_col_left" lang='<fmt:message key="search.selected.lang.value"/>'>
            letter<span class="mgs_accessibility">Browse by alphabets</span>
        </div>
        <div class="mgs_area_cont mgs_col_right">
                <div class="de_letters_container">
                <%char letter;
                for(letter='A';letter<= 'Z';letter++){%>
                    <a class="de_letters_link" href='<portlet:actionURL>
                    <portlet:param name="<%=IPortletConstants.SEARCH_ALPHABET%>" value="<%=String.valueOf(letter)%>"/>
                    <portlet:param name="<%=IPortletConstants.ACTION_ALPHABROWSE%>" value="<%=IPortletConstants.VAL_SUBMITQUERY_ALPHABET_BROWSE%>"/>
                    </portlet:actionURL>'>
                    <%=letter%></a>
                <%}%>
                    <a class="de_letters_link" href='<portlet:actionURL>
                    <portlet:param name="<%=IPortletConstants.SEARCH_ALPHABET%>" value="<%=IPortletConstants.NUMERICAL_PARENT_LETTER%>"/>
                    <portlet:param name="<%=IPortletConstants.ACTION_ALPHABROWSE%>" value="<%=IPortletConstants.VAL_SUBMITQUERY_ALPHABET_BROWSE%>"/>
                    </portlet:actionURL>'>
                    <%=IPortletConstants.NUMERICAL_PARENT_LETTER%></a>
                </div>
        </div>
        <div style="clear:both;"></div>
        <div id="DivNMCDLink" class="mgs_area_cont mgs_col_right">
        <content:taxonomy 
		    taxonomyName="site_context" 
		    taxonomyValue="WPP::LTDIG6LD1" 
		    assetType="embedded_fragment"
		    contentType="instructional"
            missingMessage="No content found."/>
        </div>
        <div style="clear:both;"></div>
        <div id="DivDisclaimer" class="mgs_area_cont mgs_col_right">
        <content:taxonomy 
            taxonomyName="site_context" 
            taxonomyValue="WPP::LSS2I3FC6" 
            assetType="embedded_fragment"
            contentType="disclaimer"
            language = "en-us"
            missingMessage="No content found."/>
        <content:taxonomy 
            taxonomyName="site_context" 
            taxonomyValue="WPP::LSS2I3FC6" 
            assetType="embedded_fragment"
            contentType="disclaimer"
            language = "es-us"
            missingMessage="No content found."/>
        </div>
   </div>
   <br/>
</div>