package org.kp.wpp.portlet.search.utils;

import junit.framework.TestCase;

import org.kp.wpp.portlet.search.constants.IPortletConstants;

public class FormatUtilTest extends TestCase {

	public void testConvertStringtoInt(){
		assertEquals(1467, FormatUtil.convertStringtoInt("1467"));
		assertEquals(0, FormatUtil.convertStringtoInt("HELLO"));
		assertEquals(0, FormatUtil.convertStringtoInt(null));
		assertEquals(0, FormatUtil.convertStringtoInt(""));
	}

	public void testHandleEmptyTitle(){
		assertEquals(IPortletConstants.CONTENT_EMPTY_TITLE,FormatUtil.handleEmptyTitle(null));
		assertEquals(IPortletConstants.CONTENT_EMPTY_TITLE,FormatUtil.handleEmptyTitle(""));
		assertEquals("hello",FormatUtil.handleEmptyTitle(" hello "));
		assertEquals("Title",FormatUtil.handleEmptyTitle("Title"));
	}

	public void testTrim(){
		assertEquals("",FormatUtil.trim(null));
		assertEquals("hello",FormatUtil.trim("  hello  "));
		assertEquals("hello",FormatUtil.trim("hello"));
	}
	
	public void testUpdateWithHTMLCharacters(){
		assertNull(FormatUtil.updateWithHTMLCharacters(null));
		assertEquals("&#39;hello&#39;",FormatUtil.updateWithHTMLCharacters("'hello'"));
		assertEquals("&quot;Heart Health&quot;",FormatUtil.updateWithHTMLCharacters("\"Heart Health\""));
		assertEquals("&quot;heart health&#39;",FormatUtil.updateWithHTMLCharacters("\"heart health'"));
	}

	public void testURLEncoder(){
		assertEquals("Colorado+-+Denver+%2F+Boulder",FormatUtil.urlEncode("Colorado - Denver / Boulder"));
		assertEquals("California+-+Northern",FormatUtil.urlEncode("California - Northern"));
		assertEquals("California+-+Southern",FormatUtil.urlEncode("California - Southern"));
		assertEquals("",FormatUtil.urlEncode(""));
	}
	
	public void testconvertToBoolean(){
		assertTrue(FormatUtil.convertToBoolean("yes"));
		assertFalse(FormatUtil.convertToBoolean("no"));
		assertFalse(FormatUtil.convertToBoolean("True"));
		assertFalse(FormatUtil.convertToBoolean(null));
		assertFalse(FormatUtil.convertToBoolean(""));
	}
	
	public void testconvertTokenToLabel(){
		assertEquals("",FormatUtil.convertTokenToLabel(null));
		assertEquals("",FormatUtil.convertTokenToLabel(""));
		assertEquals("",FormatUtil.convertTokenToLabel("   "));
		assertEquals("",FormatUtil.convertTokenToLabel("Primary Care"));
		assertEquals("",FormatUtil.convertTokenToLabel("services"));
		assertEquals("",FormatUtil.convertTokenToLabel("services=="));
		assertEquals("Primary Care;",FormatUtil.convertTokenToLabel("services==Primary Care"));
		assertEquals("Primary Care;",FormatUtil.convertTokenToLabel("==Primary Care"));
	}
}
