package org.kp.wpp.portlet.search.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.tree.TreeModel;

import junit.framework.TestCase;

import org.kp.wpp.content.bean.ContentBean;
import org.kp.wpp.content.engine.IContentEngine;
import org.kp.wpp.content.enums.EAssetType;
import org.kp.wpp.content.enums.EChannel;
import org.kp.wpp.content.enums.EContentType;
import org.kp.wpp.content.enums.ENodeType;
import org.kp.wpp.content.enums.ERegion;
import org.kp.wpp.content.file.AFileReader;
import org.kp.wpp.content.hibernate.domain.Asset;
import org.kp.wpp.content.hibernate.domain.AssetMetadata;
import org.kp.wpp.content.hibernate.domain.ContentType;
import org.kp.wpp.portlet.search.beans.AuxiliaryContent;

public class ContentTest extends TestCase {
	
	public void testContentFragments(){
		ContentRetriever cRetriever = new ContentRetriever(new MockContentEngineWithTitle());
		AuxiliaryContent ac = cRetriever.getMGRegionalContents("GA");
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWRNXLWQ", ac.getDisclaimer());
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWPFX7YF", ac.getRegAnnouncement());
		assertEquals("TEST TITLE FOR REGION:GA AND NODE ID:WPP::LAWPFX7YF", ac.getRegAnnouncementTitle());
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWRCEA55", ac.getLinkBucket1());
		assertEquals("TEST TITLE FOR REGION:GA AND NODE ID:WPP::LAWRCEA55", ac.getLinkBucket1Title());
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWRESFCA", ac.getLinkBucket2());
		assertEquals("TEST TITLE FOR REGION:GA AND NODE ID:WPP::LAWRESFCA", ac.getLinkBucket2Title());
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWRB3D53", ac.getPartnerLinks());
		ac = cRetriever.getMGNationalContents();
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:NAT AND NODE ID:WPP::LAWPEXFTB", ac.getRiskMitigationTest()); // Replace the Node id LFJPULC4T - DE5337 Doctor Home Page: Risk mitigation text - wrong Teamsite node
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:NAT AND NODE ID:WPP::LAWPDU3UA", ac.getNatAnnouncement());
		assertEquals("TEST TITLE FOR REGION:NAT AND NODE ID:WPP::LAWPDU3UA", ac.getNatAnnouncementTitle());

		cRetriever = new ContentRetriever(new MockContentEngineWithoutTitle());
		ac = cRetriever.getMGRegionalContents("GA");
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWRNXLWQ", ac.getDisclaimer());
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWPFX7YF", ac.getRegAnnouncement());
		assertNull(ac.getRegAnnouncementTitle());
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWRCEA55", ac.getLinkBucket1());
		assertNull(ac.getLinkBucket1Title());
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWRESFCA", ac.getLinkBucket2());
		assertNull(ac.getLinkBucket2Title());
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:GA AND NODE ID:WPP::LAWRB3D53", ac.getPartnerLinks());
		ac = cRetriever.getMGNationalContents();
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:NAT AND NODE ID:WPP::LAWPEXFTB", ac.getRiskMitigationTest()); // Replace the Node id LFJPULC4T - DE5337 Doctor Home Page: Risk mitigation text - wrong Teamsite node
		assertEquals("TEST CONTENT FRAGMENT FOR REGION:NAT AND NODE ID:WPP::LAWPDU3UA", ac.getNatAnnouncement());
		assertNull(ac.getNatAnnouncementTitle());
	
	}
	

	 public class  MockContentEngineWithTitle implements IContentEngine{

		public List<ContentBean> getArticle(String arg0, Locale arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentType> getAssetForContentTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		public List<AssetMetadata> getAssetMetadata(String arg0,
				ENodeType arg1, String arg2, String arg3, String arg4,
				ERegion arg5) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		public TreeModel getCenter(File arg0, Locale arg1) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentBean> getContentByMetaData(String arg0, String arg1,
				String arg2, String arg3, ERegion arg4, String arg5,
				EChannel arg6, Locale arg7, ENodeType arg8) {
			// TODO Auto-generated method stub
			return null;
		}

		public InputStream getContentCenterIS(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentType> getContentTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentBean> getErrorMessageFragments(String arg0,
				ERegion arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentBean> getMessages(String arg0, Locale arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public ContentBean getRegionalMessage(File arg0, String arg1,
				String arg2, Locale arg3) {
			// TODO Auto-generated method stub
			return null;
		}

		public TreeModel getRegionalTopic(String arg0, String arg1,
				Locale arg2, ERegion arg3) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentBean> getRegions(String arg0, String arg1,
				Locale arg2) {
			// TODO Auto-generated method stub
			return null;
		}

		public TreeModel getTopic(String arg0, File arg1, Locale arg2)
				throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		public ContentBean getAsset(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public ContentBean getContentFile(File arg0, Locale arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentBean> getProviderFragments(String arg0, String arg1,
				EAssetType arg2, EContentType arg3, ERegion arg4,
				EChannel arg5, Locale arg6) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentBean> getProviderFragments(String arg0, String arg1,
				String arg2, String arg3, ERegion arg4, EChannel arg5,
				Locale arg6) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentBean> getTaxonomyFragments(String arg0, String arg1,
				ENodeType arg2, EAssetType arg3, EContentType arg4,
				ERegion arg5, EChannel arg6, Locale arg7) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentBean> getTaxonomyFragments(String arg0, String arg1,
				ENodeType arg2, String arg3, String arg4, ERegion arg5,
				EChannel arg6, Locale arg7) {
			List<ContentBean> messages=new ArrayList<ContentBean>();
			ContentBean bean = new ContentBean();
			bean.setContent("TEST CONTENT FRAGMENT FOR REGION:"+arg5.toString()+" AND NODE ID:"+arg1);
			Asset a = new Asset();
			a.setAsset_primary_title("TEST TITLE FOR REGION:"+arg5.toString()+" AND NODE ID:"+arg1);
			bean.setAsset(a);
			messages.add(bean);
			return messages;
		}

		public boolean isPathValid(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isProviderExists(String arg0, String arg1,
				EAssetType arg2, EContentType arg3, ERegion arg4,
				EChannel arg5, Locale arg6) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isProviderExists(String arg0, String arg1, String arg2,
				String arg3, ERegion arg4, EChannel arg5, Locale arg6) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isTaxonomyExists(String arg0, String arg1,
				ENodeType arg2, EAssetType arg3, EContentType arg4,
				ERegion arg5, EChannel arg6, Locale arg7) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isTaxonomyExists(String arg0, String arg1,
				ENodeType arg2, String arg3, String arg4, ERegion arg5,
				EChannel arg6, Locale arg7) {
			// TODO Auto-generated method stub
			return false;
		}

		public List<ContentBean> readAsset(List<Asset> arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<Map<String, Object>> getAssetMaps(String arg0, String arg1,
				ENodeType arg2, EAssetType arg3, EContentType arg4,
				ERegion arg5, EChannel arg6, Locale arg7) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<Map<String, Object>> getAssetMaps(String arg0, String arg1,
				ENodeType arg2, String arg3, String arg4, ERegion arg5,
				EChannel arg6, Locale arg7) {
			// TODO Auto-generated method stub
			return null;
		}

		public AFileReader getReader() {
			// TODO Auto-generated method stub
			return null;
		}

		public List<Map<String, Object>> providerMaps(String arg0, String arg1,
				EAssetType arg2, EContentType arg3, ERegion arg4,
				EChannel arg5, Locale arg6) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<Map<String, Object>> providerMaps(String arg0, String arg1,
				String arg2, String arg3, ERegion arg4, EChannel arg5,
				Locale arg6) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<ContentBean> getTaxonomyFragments(String arg0, String arg1,
				ENodeType arg2, String arg3, String arg4, ERegion arg5,
				EChannel arg6, Locale arg7, boolean arg8) {
			// TODO Auto-generated method stub
			return null;
		}
	 }
	
	public class MockContentEngineWithoutTitle extends  MockContentEngineWithTitle{
		public List<ContentBean> getTaxonomyFragments(String arg0, String arg1,
				ENodeType arg2, String arg3, String arg4, ERegion arg5,
				EChannel arg6, Locale arg7) {
			List<ContentBean> messages=new ArrayList<ContentBean>();
			ContentBean bean = new ContentBean();
			bean.setContent("TEST CONTENT FRAGMENT FOR REGION:"+arg5.toString()+" AND NODE ID:"+arg1);
			Asset a = null;
			bean.setAsset(a);
			messages.add(bean);
			return messages;
		}
	}
}
