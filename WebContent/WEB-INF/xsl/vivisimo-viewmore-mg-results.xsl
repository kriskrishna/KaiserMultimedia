<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:java="java"
  xmlns:url="java.net.URLEncoder">
    <xsl:output method="html" standalone="no"/>
    <xsl:template match="/">
        <xsl:variable name="kpProject">
            <xsl:for-each select="vce/param">
                <xsl:if test="@name='v:project'">
                    <xsl:value-of select="@value"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>
        <xsl:variable name="sKeyword">
            <xsl:for-each select="vce/param">
                <xsl:if test="@name='query'">
                    <xsl:value-of select="@value"/>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>
        <xsl:variable name="legacyROPCode">
            <xsl:for-each select="vce/param">
                <xsl:if test="@name='rop'">
                    <xsl:choose>
                        <xsl:when test="@value='NCA'">MRN</xsl:when>
                        <xsl:otherwise><xsl:value-of select="@value"/></xsl:otherwise>
                    </xsl:choose>
                </xsl:if>
            </xsl:for-each>
        </xsl:variable>
        <xsl:for-each select="vce/list">
            <xsl:variable name="resultStartIndex" select="number(@start)"/>
            <xsl:for-each select="document">
                <xsl:variable name="rIndex" select="$resultStartIndex+position()"/>
                <xsl:variable name="CITY" select="url:encode(content[@name='city'])"/>
                <xsl:variable name="STATE" select="url:encode(content[@name='state'])"/>
		        <xsl:variable name="STADDRESS">
                    <xsl:choose>
                        <xsl:when test="content[@name='street_address'] != ''">
                            <xsl:value-of select="content[@name='street_address']"/>
                        </xsl:when>
                        <xsl:otherwise>
	                       <xsl:value-of select="content[@name='office_address1']"/>
                        </xsl:otherwise>
                    </xsl:choose>
		        </xsl:variable>
                <div class="searchMGListing">
                	<!-- Commenting the doctor photo code for this iteration.
                    <xsl:variable name="noPhoto" select="'/static/consumer/consumernet/mydoctor/styles/private/images/photo_none.jpg'"/>
                    <xsl:if test="content[@name='photo_url'] != ''">
                    	<div id="searchMGImage">
                    	<xsl:choose>
                    		<xsl:when test="content[@name='photo_url'] != 'none'">
                    			<img>
                    			<xsl:attribute name="src"><xsl:value-of select="content[@name='photo_url']" disable-output-escaping="yes"/></xsl:attribute>
                    			<xsl:attribute name="height">199</xsl:attribute>
                    			<xsl:attribute name="alt"><xsl:value-of select="content[@name='title']" disable-output-escaping="yes"/></xsl:attribute>
                    			<xsl:attribute name="title"><xsl:value-of select="content[@name='title']" disable-output-escaping="yes"/></xsl:attribute>
                    			</img><br/>
                    		</xsl:when>
                    		<xsl:otherwise>
                    			<img>
                    			<xsl:attribute name="src"><xsl:copy-of select="$noPhoto" disable-output-escaping="yes"/></xsl:attribute>
                    			<xsl:attribute name="height">199</xsl:attribute>
                    			<xsl:attribute name="alt"><xsl:value-of select="content[@name='title']" disable-output-escaping="yes"/></xsl:attribute>
                    			<xsl:attribute name="title"><xsl:value-of select="content[@name='title']" disable-output-escaping="yes"/></xsl:attribute>
                    			</img><br/>
                    		</xsl:otherwise>
                    	</xsl:choose>
                    	</div>
                    </xsl:if>
                    -->
                    <div id="searchMGContent">
                    <div class="searchListingHead">
                    <!-- extra if condition checking for ? to conditionally add WebTrends parameter -->
                    <xsl:choose>
                    <xsl:when test="contains(content[@name='p_url'],'?')">
                        <a onclick="dojo.publish('kaiser/wiEvent',['clickSearchResult',['{$sKeyword}',{$rIndex}]]);" href="{content[@name='p_url']}&amp;kpSearch={$sKeyword}">
                            <xsl:value-of select="content[@name='title']" disable-output-escaping="yes"/></a><br/>
                    </xsl:when>
                    <xsl:otherwise>
                        <a onclick="dojo.publish('kaiser/wiEvent',['clickSearchResult',['{$sKeyword}',{$rIndex}]]);" href="{content[@name='p_url']}?kpSearch={$sKeyword}">
                            <xsl:value-of select="content[@name='title']" disable-output-escaping="yes"/></a><br/>
                    </xsl:otherwise>
                    </xsl:choose>
                    <!-- extra if condition checking for ? to conditionally add WebTrends parameter -->
                    <xsl:if test="content[@name='medspecialty_display'] != ''">Specialty: <xsl:value-of select="content[@name='medspecialty_display']" disable-output-escaping="yes"/><br/></xsl:if>
                    <xsl:if test="content[@name='plan_list'] != ''">Plans: <xsl:value-of select="content[@name='plan_list']" disable-output-escaping="yes"/><br/></xsl:if>
                    </div>
	                <div class="searchListingBody">
	                    <xsl:if test="content[@name='program_type'] != ''"><xsl:value-of select="content[@name='program_type']" disable-output-escaping="yes"/><br/></xsl:if>
                        <xsl:if test="content[@name='office_name'] != ''"><xsl:value-of select="content[@name='office_name']" disable-output-escaping="yes"/><br/></xsl:if>
						<xsl:variable name="isAffiliate" select="content[@name='affiliated_facility']"/>
	                    <xsl:if test="$isAffiliate='Yes'">Kaiser Permanente Affiliate<br/></xsl:if>
	                    <xsl:if test="content[@name='description'] != ''"><xsl:value-of select="content[@name='description']" disable-output-escaping="yes"/><br/></xsl:if>
                        <xsl:if test="content[@name='facility'] != ''">
	                    <xsl:value-of select="content[@name='facility']" disable-output-escaping="yes"/>, <xsl:value-of select="content[@name='city']" disable-output-escaping="yes"/><br/>
	                    </xsl:if>
	                    <xsl:if test="content[@name='fac_distance'] != ''">
		                    <xsl:choose>
		                    	<xsl:when test="content[@name='medspecialty_display'] != ''">
	                            	<xsl:choose>
				                    <xsl:when test="content[@name='fac_distance'] = '1'">
				                        <xsl:value-of select="content[@name='fac_distance']" disable-output-escaping="yes"/> mile<br/>
				                    </xsl:when>
				                    <xsl:otherwise>
				                        <xsl:value-of select="content[@name='fac_distance']" disable-output-escaping="yes"/> miles<br/>
				                    </xsl:otherwise>
				                    </xsl:choose>
	                            </xsl:when>
	                            <xsl:otherwise>
	                            	<xsl:choose>
				                    <xsl:when test="content[@name='fac_distance'] = '1'">
				                        <b><xsl:value-of select="content[@name='fac_distance']" disable-output-escaping="yes"/> mile</b><br/>
				                    </xsl:when>
				                    <xsl:otherwise>
				                        <b><xsl:value-of select="content[@name='fac_distance']" disable-output-escaping="yes"/> miles</b><br/>
				                    </xsl:otherwise>
				                    </xsl:choose>
	                            </xsl:otherwise>
		                    </xsl:choose>
	                    </xsl:if>
	                    <xsl:if test="$STADDRESS != '' and $CITY != '' and $STATE != ''">
	                    	<a href="/kpweb/facilitydir/map.do?link=tripplus&amp;static_destination=1&amp;destcity={$CITY}&amp;deststateProvince={$STATE}&amp;destaddress={$STADDRESS}&amp;rop={$legacyROPCode}">
	                    		Maps and directions
	                    	</a>
	                    </xsl:if>
	            	</div>
	                <div class="searchListingFoot">
	                	<xsl:if test="content[@name='pcp_status'] != ''">
	                        <xsl:if test="content[@name='pcp_status'] = 'yes'"><b>Accepting new patients</b><br/></xsl:if>
                        </xsl:if>
	                	<xsl:if test="content[@name='services_list'] != ''">
	                        Services: <xsl:value-of select="content[@name='services_list']" disable-output-escaping="yes"/><br/>
	                    </xsl:if>
	                    <xsl:if test="content[@name='phone_number'] != ''">
	                    <SPAN>
	                    	<xsl:attribute name="id">phone_n</xsl:attribute>
	                    	<xsl:value-of select="content[@name='phone_number']" disable-output-escaping="yes"/>
	                    </SPAN>
	                    <br/>
	                    </xsl:if>
	                	<xsl:if test="content[@name='snippet'] != ''"><xsl:value-of select="content[@name='snippet']" disable-output-escaping="yes"/><br/></xsl:if>
	                </div>
	                </div>
	                <div id="searchMGSpace"></div>
                </div>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>