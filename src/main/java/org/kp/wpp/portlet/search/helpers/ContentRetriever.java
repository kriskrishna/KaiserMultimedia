package org.kp.wpp.portlet.search.helpers;

import java.util.ArrayList;
import java.util.List;

import org.kp.wpp.common.core.logging.Log;
import org.kp.wpp.common.core.logging.LogFactory;
import org.kp.wpp.content.bean.ContentBean;
import org.kp.wpp.content.engine.IContentEngine;
import org.kp.wpp.content.enums.EChannel;
import org.kp.wpp.content.enums.ENodeType;
import org.kp.wpp.content.enums.ERegion;
import org.kp.wpp.content.enums.EnumHelper;
import org.kp.wpp.portlet.search.beans.AuxiliaryContent;
import org.kp.wpp.portlet.search.constants.IPortletConstants;
import org.kp.wpp.portlet.search.utils.ROPMap;

/**
 * National Announcement 
 * Asset type = embedded_fragment Content type =
 * announcement Taxonomy type = site_context Taxonomy value = WPP::LAWPDU3UA
 * Region = Nat
 * 
 * Regional Annoucements 
 * Asset type = embedded_fragment Content type =
 * announcement Taxonomy type = site_context Taxonomy value = WPP::LAWPFX7YF
 * Region = varies depending on ROP
 * 
 * Risk Mitigation 
 * Asset type = embedded_fragment Content type = risk_mitigation
 * Taxonomy type = site_context Taxonomy value = WPP::LAWPEXFTB Region = NAT
 * 
 * Partner Links 
 * Asset type = embedded_fragment Content type = related_links
 * Taxonomy type = site_context Taxonomy value = WPP::LAWRB3D53 Region = varies
 * depending on ROP?
 * 
 * Link Bucket 1 (Find out about) 
 * Asset type = embedded_fragment Content type =
 * related_links Taxonomy type = site_context Taxonomy value = WPP::LAWRCEA55
 * Region = varies depending on ROP
 * 
 * Link Bucket 2 (More to explore) 
 * Asset type = embedded_fragment Content type =
 * related_links Taxonomy type = site_context Taxonomy value = WPP::LAWRESFCA
 * Region = varies depending on ROP
 * 
 * Disclaimer 
 * Asset type = embedded_fragment Content type = disclaimer Taxonomy
 * type = site_context Taxonomy value = WPP::LAWRNXLWQ Region = varies depending
 * on ROP
 * 
 * Facility Search Intro 
 * Asset type = embedded_fragment Content type = intro Taxonomy
 * type = site_context Taxonomy value = WPP::MP2KXO4R1 Region = varies depending
 * on ROP
 * 
 * Doctor Search Intro 
 * Asset type = embedded_fragment Content type = intro Taxonomy
 * type = site_context Taxonomy value = WPP::MOKC8G37Q Region = varies depending
 * on ROP
 * 
 * @author l525045
 * 
 */
public class ContentRetriever {

	public static final Log LOGGER = LogFactory.getLog(ContentRetriever.class);
	
	private IContentEngine cEngine = null;

	public ContentRetriever(IContentEngine ice){
		cEngine = ice;
	}
	
	public AuxiliaryContent getMGRegionalContents(final String regionCd){
		final AuxiliaryContent aContent = new AuxiliaryContent();
		ContentBean cb = getRegionalContent("WPP::LAWPFX7YF", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT,  IPortletConstants.CONTENT_TYPE_ANNOUNCEMENT,regionCd);
		if(null != cb){
			if(null != cb.getAsset()){aContent.setRegAnnouncementTitle(cb.getAsset().getAsset_primary_title());}
			aContent.setRegAnnouncement(cb.getContent());
		}
		cb = getRegionalContent("WPP::LAWRB3D53", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_RELATED_LINKS, regionCd);
		if(null != cb){
			aContent.setPartnerLinks(cb.getContent());
		}
		cb = getRegionalContent("WPP::LFLUJGDBL", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_RELATED_LINKS, regionCd);
		if(null != cb){
			aContent.setMoreSvcs(cb.getContent());
		}
		cb = getRegionalContent("WPP::LAWRCEA55", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_RELATED_LINKS, regionCd);
		if(null != cb){
			if(null != cb.getAsset()){aContent.setLinkBucket1Title(cb.getAsset().getAsset_primary_title());}
			aContent.setLinkBucket1(cb.getContent());
		}
		cb = getRegionalContent("WPP::LAWRESFCA", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_RELATED_LINKS, regionCd);
		if(null != cb){
			if(null != cb.getAsset()){aContent.setLinkBucket2Title(cb.getAsset().getAsset_primary_title());}
			aContent.setLinkBucket2(cb.getContent());
		}
		cb = getRegionalContent("WPP::LAWRNXLWQ", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_DISCLAIMER, regionCd);
		if(null != cb){
			aContent.setDisclaimer(cb.getContent());
		}
		cb = getRegionalContent("WPP::MP2KXO4R1", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_INTRO, regionCd);
		if(null != cb){
			aContent.setFacilityIntro(cb.getContent());
		}
		cb = getRegionalContent("WPP::MOKC8G37Q", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_INTRO, regionCd);
		if(null != cb){
			aContent.setDoctorIntro(cb.getContent());
		}
		
		return aContent;
	}
	
	public AuxiliaryContent getMGNationalContents(){
		AuxiliaryContent aContent = new AuxiliaryContent();
		ContentBean cb = getNationalContent("WPP::LAWPDU3UA", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_ANNOUNCEMENT);
		if(null != cb){
			if(null != cb.getAsset()){aContent.setNatAnnouncementTitle(cb.getAsset().getAsset_primary_title());}
			aContent.setNatAnnouncement(cb.getContent());
		}
		cb = getNationalContent("WPP::LAWPEXFTB", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_RISK_MITIGATION); // Replace the Node id LFJPULC4T - DE5337 Doctor Home Page: Risk mitigation text - wrong Teamsite node
		
		if(null != cb){
			aContent.setRiskMitigationTest(cb.getContent());
		}
		cb = getNationalContent("WPP::MP2KXO4R1", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_INTRO);
		if(null != cb){
			aContent.setFacilityIntro(cb.getContent());
		}
		cb = getNationalContent("WPP::MOKC8G37Q", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_INTRO);
		if(null != cb){
			aContent.setDoctorIntro(cb.getContent());
		}
		return aContent;
	}
	
	public AuxiliaryContent getMGIntroContents(){
		AuxiliaryContent aContent = new AuxiliaryContent();
		ContentBean cb = getNationalContent("WPP::MP2KXO4R1", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_INTRO);
		if(null != cb){
			aContent.setFacilityIntro(cb.getContent());
		}
		cb = getNationalContent("WPP::MOKC8G37Q", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_INTRO);
		if(null != cb){
			aContent.setDoctorIntro(cb.getContent());
		}
		return aContent;
	}
	
	public AuxiliaryContent getSiteContents(){
		AuxiliaryContent aContent = new AuxiliaryContent();
		ContentBean cb = getNationalContent("WPP::MR6CHF6O5", IPortletConstants.CONTENT_NODE_TYPE_FRAGMENT, IPortletConstants.CONTENT_TYPE_RELATED_LINKS);
		if(null != cb){
			aContent.setSiteSearchLinks(cb.getContent());
		}
		return aContent;
	}

	public ContentBean getRegionalContent(final String nodeId, final String nodeType, final String contentType, final String rgnCode){
		final String rgnCd = ROPMap.updateRegionCodeForContentAPIs(rgnCode);
		final ERegion eregion = new EnumHelper<ERegion>().safe(ERegion.class,rgnCd);
		List<ContentBean> lstCBean=new ArrayList<ContentBean>();
		lstCBean=cEngine.getTaxonomyFragments(null, nodeId, ENodeType.site_context, nodeType, contentType, eregion, EChannel.web, null);
		if(null != lstCBean && lstCBean.size() > 0 && null != lstCBean.get(0)){
			return lstCBean.get(0);
		}else{
			return getNationalContent(nodeId, nodeType, contentType);
		}
	}

	public ContentBean getNationalContent(final String nodeId, final String nodeType, final String contentType){
		final ERegion eregion = new EnumHelper<ERegion>().safe(ERegion.class,"NAT");
		List<ContentBean> lstCBean=new ArrayList<ContentBean>();
		lstCBean=cEngine.getTaxonomyFragments(null, nodeId, ENodeType.site_context, nodeType, contentType, eregion, EChannel.web, null);
		if(null != lstCBean && lstCBean.size() > 0 && null != lstCBean.get(0)){
			return lstCBean.get(0);
		}else{
			return null;
		}
	}
	
}