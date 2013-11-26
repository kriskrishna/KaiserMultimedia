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
    String ncaChk=pref.getValue(IPortletConstants.PREF_NCA_PLAN_PROVIDER_TYPE, "");
    String scaChk=pref.getValue(IPortletConstants.PREF_SCA_PLAN_PROVIDER_TYPE, "");
    String dbChk=pref.getValue(IPortletConstants.PREF_DB_PLAN_PROVIDER_TYPE, "");
    String ncChk=pref.getValue(IPortletConstants.PREF_NC_PLAN_PROVIDER_TYPE, "");
    String csChk=pref.getValue(IPortletConstants.PREF_CS_PLAN_PROVIDER_TYPE, "");
    String ggaChk=pref.getValue(IPortletConstants.PREF_GGA_PLAN_PROVIDER_TYPE, "");
    String hawChk=pref.getValue(IPortletConstants.PREF_HAW_PLAN_PROVIDER_TYPE, "");
    String midChk=pref.getValue(IPortletConstants.PREF_MID_PLAN_PROVIDER_TYPE, "");
    String ohiChk=pref.getValue(IPortletConstants.PREF_OHI_PLAN_PROVIDER_TYPE, "");
    String knwChk=pref.getValue(IPortletConstants.PREF_KNW_PLAN_PROVIDER_TYPE, "");
   
    Boolean blNCAChk=null;
    Boolean blSCAChk=null;
    Boolean blDBChk=null;
    Boolean blNCChk=null;
    Boolean blCSChk=null;
    Boolean blGGAChk=null;
    Boolean blHAWChk=null;
    Boolean blMIDChk=null;
    Boolean blOHIChk=null;
    Boolean blKNWChk=null;
   
   if(ncaChk!=null){
    blNCAChk= Boolean.valueOf(ncaChk);
    }else{
    blNCAChk = Boolean.FALSE;
    }
    String strNCA="";
    if(blNCAChk.booleanValue()){
    
    strNCA="checked=checked";
    }
    
    if(scaChk!=null){
    blSCAChk= Boolean.valueOf(scaChk);
    }else{
    blSCAChk = Boolean.FALSE;
    }
    String strSCA="";
    if(blSCAChk.booleanValue()){
    
    strSCA="checked=checked";
    }
    
    if(dbChk!=null){
    blDBChk= Boolean.valueOf(dbChk);
    }else{
    blDBChk = Boolean.FALSE;
    }
    String strDB="";
    if(blDBChk.booleanValue()){
    
    strDB="checked=checked";
    }
    
    if(ncChk!=null){
    blNCChk= Boolean.valueOf(ncChk);
    }else{
    blNCChk = Boolean.FALSE;
    }
    String strNC="";
    if(blNCChk.booleanValue()){
    
    strNC="checked=checked";
    }
    
    if(csChk!=null){
    blCSChk= Boolean.valueOf(csChk);
    }else{
    blCSChk = Boolean.FALSE;
    }
    
    String strCS="";
    if(blCSChk.booleanValue()){
    
    strCS="checked=checked";
    }
    
    if(ggaChk!=null){
    blGGAChk= Boolean.valueOf(ggaChk);
    }else{
    blGGAChk = Boolean.FALSE;
    }
    
    String strGGA="";
    if(blGGAChk.booleanValue()){
    
    strGGA="checked=checked";
    
    }
    
    if(hawChk!=null){
    blHAWChk= Boolean.valueOf(hawChk);
    }else{
    blHAWChk =Boolean.FALSE;
    }
    String strHAW="";
    if(blHAWChk.booleanValue()){
    
    strHAW="checked=checked";
    }
    if(midChk!=null){
    blMIDChk= Boolean.valueOf(midChk);
    }else{
    blMIDChk =Boolean.FALSE;
    }
    
    String strMID="";
    if(blMIDChk.booleanValue()){
    
    strMID="checked=checked";
    
    }
    
    if(ohiChk!=null){
    blOHIChk= Boolean.valueOf(ohiChk);
    }else{
    blOHIChk =Boolean.FALSE;
    }
    String strOHI="";
    if(blOHIChk.booleanValue()){
    
    strOHI="checked=checked";
    }
    
    if(knwChk!=null){
    blKNWChk= Boolean.valueOf(knwChk);
    }else{
    blKNWChk =Boolean.FALSE;
    }
    
    
    String strKNW="";
    if(blKNWChk.booleanValue()){
    
    strKNW="checked=checked";
    }
    
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
            <td>Search Sources - Source name as in Velocity</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHSOURCES%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHSOURCES, "")%>"
                type="text"></td>
        </tr>
        <tr>
            <td>Search Custom XSL Transformation</td>
            <td><input name="<%=IPortletConstants.FIELD_SEARCHCUSTOMDISPLAY%>"
                value="<%=pref.getValue(IPortletConstants.PREF_SEARCHCUSTOMDISPLAY, "")%>"
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
        
        
        
        </tbody>
        </table>
        <table width="470" height="100" border="0">
    <tbody>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
       <tr>
            <td align="center" colspan="2"><b>Gated Search</b></td>
            
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><b>Plan & Provider Types Gate</b></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>California - Northern </td>
            <td>
            <%if(strNCA.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_NCA%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_NCA%>" value="true" type="checkbox">
            <%} %>
            </td>
        </tr>
        <tr>
            <td>California - Southern</td>
            <td>
            <%if(strSCA.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_SCA%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_SCA%>" value="true" type="checkbox">
            <%} %>
            </td>
        </tr>
        <tr>
            <td>Colorado - Denver / Boulder</td>
            <td>
            <%if(strDB.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_DB%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_DB%>" value="true" type="checkbox">
            <%} %>
    		</td>
        </tr>
        <tr>
            <td>Colorado - Northern Colorado</td>
            <td>
            <%if(strNC.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_NC%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_NC%>" value="true" type="checkbox">
            <%} %>
    		</td>
        </tr>
        <tr>
            <td>Colorado - Southern Colorado</td>
             <td>
            <%if(strCS.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_CS%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_CS%>" value="true" type="checkbox">
            <%} %>
    		</td>
        </tr>
        <tr>
            <td>Georgia</td>
             <td>
            <%if(strGGA.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_GGA%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_GGA%>" value="true" type="checkbox">
            <%} %>
    		</td>
        </tr>
        <tr>
            <td>Hawaii</td>
            <td>
            <%if(strHAW.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_HAW%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_HAW%>" value="true" type="checkbox">
            <%} %>
    		</td>
        </tr>
        <tr>
            <td>Maryland / Virginia / Washington D.C.</td>
            <td>
            <%if(strMID.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_MID%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_MID%>" value="true" type="checkbox">
            <%} %>
    		</td>
        </tr>
        <tr>
            <td>Ohio</td>
            <td>
            <%if(strOHI.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_OHI%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_OHI%>" value="true" type="checkbox">
            <%} %>
    		</td>
        </tr>
        <tr>
            <td>Oregon / Washington</td>
             <td>
            <%if(strKNW.length()>0){ %>
            <input name="<%=IPortletConstants.FIELD_KNW%>" value="true" checked="checked" type="checkbox">
            <%}else{ %>
            <input name="<%=IPortletConstants.FIELD_KNW%>" value="true" type="checkbox">
            <%} %>
    		</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
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