package net.qoopo.qoopo.security.test.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FakeHttpServerRequest implements HttpServletRequest {

    private String servletPath;
    private String pathInfo = null;
    private String queryString = null;
    private String remoteAddr;
    private String method;

    public FakeHttpServerRequest(String servletPath) {
        this.servletPath = servletPath;
        this.method = "GET";
    }

    @Override
    public Object getAttribute(String name) {

        throw new UnsupportedOperationException("Unimplemented method 'getAttribute'");
    }

    @Override
    public Enumeration<String> getAttributeNames() {

        throw new UnsupportedOperationException("Unimplemented method 'getAttributeNames'");
    }

    @Override
    public String getCharacterEncoding() {

        throw new UnsupportedOperationException("Unimplemented method 'getCharacterEncoding'");
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

        throw new UnsupportedOperationException("Unimplemented method 'setCharacterEncoding'");
    }

    @Override
    public int getContentLength() {

        throw new UnsupportedOperationException("Unimplemented method 'getContentLength'");
    }

    @Override
    public long getContentLengthLong() {

        throw new UnsupportedOperationException("Unimplemented method 'getContentLengthLong'");
    }

    @Override
    public String getContentType() {

        throw new UnsupportedOperationException("Unimplemented method 'getContentType'");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        throw new UnsupportedOperationException("Unimplemented method 'getInputStream'");
    }

    @Override
    public String getParameter(String name) {

        throw new UnsupportedOperationException("Unimplemented method 'getParameter'");
    }

    @Override
    public Enumeration<String> getParameterNames() {

        throw new UnsupportedOperationException("Unimplemented method 'getParameterNames'");
    }

    @Override
    public String[] getParameterValues(String name) {

        throw new UnsupportedOperationException("Unimplemented method 'getParameterValues'");
    }

    @Override
    public Map<String, String[]> getParameterMap() {

        throw new UnsupportedOperationException("Unimplemented method 'getParameterMap'");
    }

    @Override
    public String getProtocol() {

        throw new UnsupportedOperationException("Unimplemented method 'getProtocol'");
    }

    @Override
    public String getScheme() {

        throw new UnsupportedOperationException("Unimplemented method 'getScheme'");
    }

    @Override
    public String getServerName() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerName'");
    }

    @Override
    public int getServerPort() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerPort'");
    }

    @Override
    public BufferedReader getReader() throws IOException {

        throw new UnsupportedOperationException("Unimplemented method 'getReader'");
    }

    @Override
    public String getRemoteAddr() {
        return remoteAddr;
    }

    @Override
    public String getRemoteHost() {

        throw new UnsupportedOperationException("Unimplemented method 'getRemoteHost'");
    }

    @Override
    public void setAttribute(String name, Object o) {

        throw new UnsupportedOperationException("Unimplemented method 'setAttribute'");
    }

    @Override
    public void removeAttribute(String name) {

        throw new UnsupportedOperationException("Unimplemented method 'removeAttribute'");
    }

    @Override
    public Locale getLocale() {

        throw new UnsupportedOperationException("Unimplemented method 'getLocale'");
    }

    @Override
    public Enumeration<Locale> getLocales() {

        throw new UnsupportedOperationException("Unimplemented method 'getLocales'");
    }

    @Override
    public boolean isSecure() {

        throw new UnsupportedOperationException("Unimplemented method 'isSecure'");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {

        throw new UnsupportedOperationException("Unimplemented method 'getRequestDispatcher'");
    }

    @Override
    public int getRemotePort() {

        throw new UnsupportedOperationException("Unimplemented method 'getRemotePort'");
    }

    @Override
    public String getLocalName() {

        throw new UnsupportedOperationException("Unimplemented method 'getLocalName'");
    }

    @Override
    public String getLocalAddr() {

        throw new UnsupportedOperationException("Unimplemented method 'getLocalAddr'");
    }

    @Override
    public int getLocalPort() {

        throw new UnsupportedOperationException("Unimplemented method 'getLocalPort'");
    }

    @Override
    public ServletContext getServletContext() {

        throw new UnsupportedOperationException("Unimplemented method 'getServletContext'");
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {

        throw new UnsupportedOperationException("Unimplemented method 'startAsync'");
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IllegalStateException {

        throw new UnsupportedOperationException("Unimplemented method 'startAsync'");
    }

    @Override
    public boolean isAsyncStarted() {

        throw new UnsupportedOperationException("Unimplemented method 'isAsyncStarted'");
    }

    @Override
    public boolean isAsyncSupported() {

        throw new UnsupportedOperationException("Unimplemented method 'isAsyncSupported'");
    }

    @Override
    public AsyncContext getAsyncContext() {

        throw new UnsupportedOperationException("Unimplemented method 'getAsyncContext'");
    }

    @Override
    public DispatcherType getDispatcherType() {

        throw new UnsupportedOperationException("Unimplemented method 'getDispatcherType'");
    }

    @Override
    public String getRequestId() {

        throw new UnsupportedOperationException("Unimplemented method 'getRequestId'");
    }

    @Override
    public String getProtocolRequestId() {

        throw new UnsupportedOperationException("Unimplemented method 'getProtocolRequestId'");
    }

    @Override
    public ServletConnection getServletConnection() {

        throw new UnsupportedOperationException("Unimplemented method 'getServletConnection'");
    }

    @Override
    public String getAuthType() {

        throw new UnsupportedOperationException("Unimplemented method 'getAuthType'");
    }

    @Override
    public Cookie[] getCookies() {

        throw new UnsupportedOperationException("Unimplemented method 'getCookies'");
    }

    @Override
    public long getDateHeader(String name) {

        throw new UnsupportedOperationException("Unimplemented method 'getDateHeader'");
    }

    @Override
    public String getHeader(String name) {

        throw new UnsupportedOperationException("Unimplemented method 'getHeader'");
    }

    @Override
    public Enumeration<String> getHeaders(String name) {

        throw new UnsupportedOperationException("Unimplemented method 'getHeaders'");
    }

    @Override
    public Enumeration<String> getHeaderNames() {

        throw new UnsupportedOperationException("Unimplemented method 'getHeaderNames'");
    }

    @Override
    public int getIntHeader(String name) {

        throw new UnsupportedOperationException("Unimplemented method 'getIntHeader'");
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    @Override
    public String getPathTranslated() {

        throw new UnsupportedOperationException("Unimplemented method 'getPathTranslated'");
    }

    @Override
    public String getContextPath() {

        throw new UnsupportedOperationException("Unimplemented method 'getContextPath'");
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public String getRemoteUser() {

        throw new UnsupportedOperationException("Unimplemented method 'getRemoteUser'");
    }

    @Override
    public boolean isUserInRole(String role) {

        throw new UnsupportedOperationException("Unimplemented method 'isUserInRole'");
    }

    @Override
    public Principal getUserPrincipal() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserPrincipal'");
    }

    @Override
    public String getRequestedSessionId() {

        throw new UnsupportedOperationException("Unimplemented method 'getRequestedSessionId'");
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer();
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public HttpSession getSession(boolean create) {

        throw new UnsupportedOperationException("Unimplemented method 'getSession'");
    }

    @Override
    public HttpSession getSession() {

        throw new UnsupportedOperationException("Unimplemented method 'getSession'");
    }

    @Override
    public String changeSessionId() {

        throw new UnsupportedOperationException("Unimplemented method 'changeSessionId'");
    }

    @Override
    public boolean isRequestedSessionIdValid() {

        throw new UnsupportedOperationException("Unimplemented method 'isRequestedSessionIdValid'");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {

        throw new UnsupportedOperationException("Unimplemented method 'isRequestedSessionIdFromCookie'");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {

        throw new UnsupportedOperationException("Unimplemented method 'isRequestedSessionIdFromURL'");
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {

        throw new UnsupportedOperationException("Unimplemented method 'authenticate'");
    }

    @Override
    public void login(String username, String password) throws ServletException {

        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public void logout() throws ServletException {

        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {

        throw new UnsupportedOperationException("Unimplemented method 'getParts'");
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {

        throw new UnsupportedOperationException("Unimplemented method 'getPart'");
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {

        throw new UnsupportedOperationException("Unimplemented method 'upgrade'");
    }

}
