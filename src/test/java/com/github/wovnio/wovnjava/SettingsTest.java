package com.github.wovnio.wovnjava;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import java.util.ArrayList;

import javax.servlet.FilterConfig;

public class SettingsTest extends TestCase {

    private static FilterConfig mockEmptyConfig() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("");
        EasyMock.expect(mock.getInitParameter("secretKey")).andReturn("");
        EasyMock.expect(mock.getInitParameter("urlPattern")).andReturn("");
        EasyMock.expect(mock.getInitParameter("urlPatternReg")).andReturn("");
        EasyMock.expect(mock.getInitParameter("query")).andReturn("");
        EasyMock.expect(mock.getInitParameter("apiUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("defaultLang")).andReturn("");
        EasyMock.expect(mock.getInitParameter("supportedLangs")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("useProxy")).andReturn("");
        EasyMock.expect(mock.getInitParameter("debugMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalUrlHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalQueryStringHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("strictHtmlCheck")).andReturn("");
        EasyMock.replay(mock);

        return mock;
    }

    private static FilterConfig mockValidConfig() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("secretKey")).andReturn("secret");
        EasyMock.expect(mock.getInitParameter("urlPattern")).andReturn("query");
        EasyMock.expect(mock.getInitParameter("urlPatternReg")).andReturn("aaa");
        EasyMock.expect(mock.getInitParameter("query")).andReturn("foo,bar");
        EasyMock.expect(mock.getInitParameter("apiUrl")).andReturn("https://example.com/v0/values");
        EasyMock.expect(mock.getInitParameter("defaultLang")).andReturn("ja");
        EasyMock.expect(mock.getInitParameter("supportedLangs")).andReturn("en,ja");
        EasyMock.expect(mock.getInitParameter("testMode")).andReturn("true");
        EasyMock.expect(mock.getInitParameter("testUrl")).andReturn("https://example.com");
        EasyMock.expect(mock.getInitParameter("useProxy")).andReturn("");
        EasyMock.expect(mock.getInitParameter("debugMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalUrlHeader")).andReturn("REDIRECT_URL");
        EasyMock.expect(mock.getInitParameter("originalQueryStringHeader")).andReturn("REDIRECT_QUERY_STRING");
        EasyMock.expect(mock.getInitParameter("strictHtmlCheck")).andReturn("");
        EasyMock.replay(mock);

        return mock;
    }

    private static FilterConfig mockQueryConfig() {
        FilterConfig mock = EasyMock.createMock(FilterConfig.class);
        EasyMock.expect(mock.getInitParameter("userToken")).andReturn("2Wle3");
        EasyMock.expect(mock.getInitParameter("secretKey")).andReturn("secret");
        EasyMock.expect(mock.getInitParameter("urlPattern")).andReturn("query");
        EasyMock.expect(mock.getInitParameter("urlPatternReg")).andReturn("");
        EasyMock.expect(mock.getInitParameter("query")).andReturn("");
        EasyMock.expect(mock.getInitParameter("apiUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("defaultLang")).andReturn("");
        EasyMock.expect(mock.getInitParameter("supportedLangs")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("testUrl")).andReturn("");
        EasyMock.expect(mock.getInitParameter("useProxy")).andReturn("");
        EasyMock.expect(mock.getInitParameter("debugMode")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalUrlHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("originalQueryStringHeader")).andReturn("");
        EasyMock.expect(mock.getInitParameter("strictHtmlCheck")).andReturn("");
        EasyMock.replay(mock);

        return mock;
    }

    // urlPattern is "path".
    public void testSettingsWithEmptyConfig() {
        FilterConfig mock = mockEmptyConfig();
        Settings s = new Settings(mock);

        assertNotNull(s);
        assertEquals("", s.userToken);
        assertEquals("", s.secretKey);
        assertEquals("path", s.urlPattern);
        assertEquals(Settings.UrlPatternRegPath, s.urlPatternReg);
        assertEquals(new ArrayList<String>(), s.query);
        assertEquals("https://api.wovn.io/v0/values", s.apiUrl);
        assertEquals("en", s.defaultLang);
        ArrayList<String> supportedLangs = new ArrayList<String>();
        supportedLangs.add("en");
        assertEquals(supportedLangs, s.supportedLangs);
        assertFalse(s.testMode);
        assertEquals("", s.testUrl);

        assertEquals("", s.originalUrlHeader);
        assertEquals("", s.originalQueryStringHeader);
    }
    // urlPattern is "subdomain".
    public void testSettingsWithValidConfig() {
        FilterConfig mock = mockValidConfig();
        Settings s = new Settings(mock);

        assertNotNull(s);
        assertEquals("2Wle3", s.userToken);
        assertEquals("secret", s.secretKey);
        assertEquals("query", s.urlPattern);
        assertEquals(Settings.UrlPatternRegQuery, s.urlPatternReg);
        ArrayList<String> query = new ArrayList<String>();
        query.add("foo");
        query.add("bar");
        assertEquals(query, s.query);
        assertEquals("https://example.com/v0/values", s.apiUrl);
        assertEquals("ja", s.defaultLang);
        ArrayList<String> supportedLangs = new ArrayList<String>();
        supportedLangs.add("en");
        supportedLangs.add("ja");
        assertEquals(supportedLangs, s.supportedLangs);
        assertTrue(s.testMode);
        assertEquals("https://example.com", s.testUrl);

        assertEquals("REDIRECT_URL", s.originalUrlHeader);
        assertEquals("REDIRECT_QUERY_STRING", s.originalQueryStringHeader);
    }
    public void testSettingsWithQueryConfig() {
        FilterConfig mock = mockQueryConfig();
        Settings s = new Settings(mock);

        assertNotNull(s);
        assertEquals("query", s.urlPattern);
        assertEquals(Settings.UrlPatternRegQuery, s.urlPatternReg);
    }

    public void testIsValidWithEmptyConfig() {
        FilterConfig mock = mockEmptyConfig();
        Settings s = new Settings(mock);
        assertFalse(s.isValid());
    }
    public void testIsValidWithValidConfig() {
        FilterConfig mock = mockValidConfig();
        Settings s = new Settings(mock);
        assertTrue(s.isValid());
    }

    public void testGetBoolParameter() {
        assertTrue(Settings.getBoolParameter("on"));
        assertTrue(Settings.getBoolParameter("true"));
        assertTrue(Settings.getBoolParameter("1"));

        assertFalse(Settings.getBoolParameter(null));
        assertFalse(Settings.getBoolParameter(""));
        assertFalse(Settings.getBoolParameter("0"));
    }

    public void testGetArrayParameterWithoutComma() {
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("foo");
        assertEquals(expected, Settings.getArrayParameter("foo"));
    }
    public void testGetArrayParameterWithComma() {
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("foo");
        expected.add("bar");
        expected.add("baz");
        assertEquals(expected, Settings.getArrayParameter("foo,bar,baz"));
    }
    public void testGetArrayParameterWithNull() {
        assertNull(Settings.getArrayParameter(null));
    }
    public void testGetArrayParameterWithEmptyString() {
        assertNull(Settings.getArrayParameter(""));
    }

    public void testGetIntParamterWithInvalidString() {
        assertEquals(0, Settings.getIntParameter(null));
        assertEquals(0, Settings.getIntParameter(""));
        assertEquals(0, Settings.getIntParameter("a"));
        assertEquals(0, Settings.getIntParameter("3.14"));
    }
    public void testGetIntParameter() {
        assertEquals(0, Settings.getIntParameter("0"));
        assertEquals(1, Settings.getIntParameter("1"));
        assertEquals(2, Settings.getIntParameter("2"));
        assertEquals(13, Settings.getIntParameter("13"));
    }
}