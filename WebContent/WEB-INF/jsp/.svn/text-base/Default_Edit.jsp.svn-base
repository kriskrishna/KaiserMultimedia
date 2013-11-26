<%@page session="false" contentType="text/html" pageEncoding="UTF-8"
import="java.util.*,javax.portlet.*"%>
<%@page import="org.kp.wpp.portlet.search.constants.IPortletConstants"%>
<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@taglib
    uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model"
    prefix="portlet-client-model"%>

<portlet:defineObjects/>
<%
    RenderRequest pReq = (RenderRequest) request.getAttribute("javax.portlet.request");
    PortletPreferences pref = pReq.getPreferences();
%>

<form name="inputForm" target="_self" method="POST"
    action='<portlet:actionURL/>'>
<table width="470" height="100" border="0">
    <tbody>
        <tr>
            <td>Search Project - Project name as in Velocity</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHPROJECT%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHPROJECT, "")%>"
                type="text"></td>
        </tr>
        <tr>
            <td>Search Source - Source name as in Velocity</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHSOURCES%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHSOURCES, "")%>"
                type="text"></td>
        </tr>
        <tr>
            <td>Search Source - Source name as in Velocity for Zip Code proximity searches</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHPROXIMITYSOURCE%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHPROXIMITYSOURCE, "")%>"
                type="text"></td>
        </tr>       
        <tr>
            <td>Search Source - Source name as in Velocity for Alphabet searches</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHALHPABETSOURCE%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHALHPABETSOURCE, "")%>"
                type="text"></td>
        </tr>       
        <tr>
            <td>Search Custom XSL Transformation</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHCUSTOMDISPLAY%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHCUSTOMDISPLAY, "")%>"
                type="text"></td>
        </tr>
        <tr>
            <td>Search Custom XSL Transformation for Landing page browse - brings only facets no results</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHCUSTOM_BINONLY_DISPLAY%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHCUSTOM_BINONLY_DISPLAY, "")%>"
                type="text"></td>
        </tr>       
        <tr>
            <td>Total number of results</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHTOTALRESULTS%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHTOTALRESULTS, "")%>"
                type="text"></td>
        </tr>
        <tr>
            <td>Number of Results per Page</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHRESULTSPERPAGE%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHRESULTSPERPAGE, "")%>"
                type="text"></td>
        </tr>
        <tr>
            <td>Timeout for the Search Service Calls(in ms):</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHTIMEOUT%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHTIMEOUT, "")%>"
                type="text"></td>
        </tr>
        <tr>
            <td>Search Region - If Search Results, by default, needed to be filtered by a region</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHREGION%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHREGION, "")%>"
                type="text"></td>
        </tr>
        <tr>
            <td>Search default locale for a search feature - If not set, locale CGI param will not be sent to search server</td>
            <td><input name="<%=IPortletConstants.FIELD_DEFAULT_LOCALE%>"
                value="<%=pref.getValue(IPortletConstants.PREF_DEFAULT_LOCALE, "")%>"
                type="text"></td>
        </tr>       
        <tr>
            <td>Search flag to default Region facet with current ROP value (Yes/No)</td>
            <td><input name="<%=IPortletConstants.FIELD_DEFAULT_REGIONFACET_WITH_ROP%>"
                value="<%=pref.getValue(IPortletConstants.PREF_DEFAULT_REGIONFACET_WITH_ROP, "")%>"
                type="text"></td>
        </tr>       
        <tr>
            <td>Search flag to indicate that Browse option through facets is available on Feature landing page</td>
            <td><input name="<%=IPortletConstants.FIELD_BROWSE_WO_RGNGATE%>"
                value="<%=pref.getValue(IPortletConstants.PREF_BROWSE_WO_RGNGATE, "")%>"
                type="text"></td>
        </tr>       
        <tr>
            <td>URL used for Glossary page in a search feature</td>
            <td><input name="<%=IPortletConstants.FIELD_GLOSSARYPAGEURL%>"
                value="<%=pref.getValue(IPortletConstants.PREF_GLOSSARYPAGEURL, "")%>"
                type="text"></td>
        </tr>       
        <tr>
            <td>URL used for Help page in a search feature</td>
            <td><input name="<%=IPortletConstants.FIELD_HELPPAGEURL%>"
                value="<%=pref.getValue(IPortletConstants.PREF_HELPPAGEURL, "")%>"
                type="text"></td>
        </tr>       
        <tr>
            <td>Feature landing page JSP filename and location relative to the Web Content root</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHLANDINGPAGE%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHLANDINGPAGE, "")%>"
                type="text"></td>
        </tr>       
        <tr>
            <td>CSS filename and location relative to the Web Content root</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHSTYLES%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHSTYLES, "")%>"
                type="text"></td>
        </tr>       
    </tbody>
    </table>
    <table border="0">
        <tbody>
            <tr>
                <td></td>
                <td></td>
                <td><input class="portlet-form-button" name="<%=IPortletConstants.ACTION_SUBMITEDITUPDATES%>"
                    type="submit" value="Save Preferences"></td>
                <td></td>
                <td><input class="portlet-form-button" name="submitCancel"
                    type="reset" value="Cancel"></td>
            </tr>
        </tbody>
    </table>
</form>
