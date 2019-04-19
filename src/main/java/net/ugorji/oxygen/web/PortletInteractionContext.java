/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import net.ugorji.oxygen.util.StringUtils;

public abstract class PortletInteractionContext implements WebInteractionContext {
  private static String COOKIE_USERNAME_NAME = "net.ugorji.oxygen.wiki.username";

  protected PortletRequest request;
  protected PortletResponse response;
  protected PortletContext pctx;

  protected boolean multipart = false;
  protected WebFileUpload webfileupload0;

  protected PortletInteractionContext(
      PortletContext pctx0, PortletRequest request0, PortletResponse response0) throws Exception {
    request = request0;
    response = response0;
    pctx = pctx0;
    if (request != null && request instanceof ActionRequest) {
      ActionRequest ar = (ActionRequest) request;
      String s = ar.getContentType();
      multipart = (s != null && s.toLowerCase().startsWith(WebConstants.MULTIPART_PREFIX));
    }
  }

  public boolean isMultipartContent() {
    return multipart;
  }

  public File[] getUploadedFiles() throws Exception {
    return (multipart ? webfileupload().getFiles() : null);
  }

  public String getSessionId() {
    return request.getPortletSession(true).getId();
  }

  public Object getSessionAttribute(String key) {
    return request.getPortletSession(true).getAttribute(key);
  }

  public void setSessionAttribute(String key, Object value) {
    request.getPortletSession(true).setAttribute(key, value);
  }

  public PrintWriter getWriter() throws Exception {
    return ((RenderResponse) response).getWriter();
  }

  public String getParameter(String string) throws Exception {
    return (multipart ? webfileupload().getParameter(string) : request.getParameter(string));
  }

  public Object getAttribute(String key) {
    return request.getAttribute(key);
  }

  public void setAttribute(String key, Object value) {
    request.setAttribute(key, value);
  }

  public String getHeader(String string) {
    return request.getProperty(string);
  }

  public String[] getParameterValues(String string) throws Exception {
    return (multipart
        ? webfileupload().getParameterValues(string)
        : request.getParameterValues(string));
  }

  public Enumeration getParameterNames() throws Exception {
    return (multipart ? webfileupload().getParameterNames() : request.getParameterNames());
  }

  public String getContextPath() {
    return request.getContextPath();
  }

  public Locale getLocale() {
    return request.getLocale();
  }

  public void sendRedirect(String s) throws Exception {
    ((ActionResponse) response).sendRedirect(s);
  }

  public void include(String jsp) throws Exception {
    pctx.getRequestDispatcher(jsp).include((RenderRequest) request, (RenderResponse) response);
  }

  public void removeSessionAttribute(String key) {
    request.getPortletSession(true).removeAttribute(key);
  }

  public void invalidateSession() {
    request.getPortletSession(true).invalidate();
  }

  public OutputStream getOutputStream() throws Exception {
    return ((RenderResponse) response).getPortletOutputStream();
  }

  public void setContentType(String mimetype) {
    ((RenderResponse) response).setContentType(mimetype);
  }

  public void setHeader(String string, String string2) {
    response.setProperty(string, string2);
  }

  public String getMimeType(String name) {
    return pctx.getMimeType(name);
  }

  public String encodeURL(String s, boolean redirectURL) {
    return response.encodeURL(s);
  }

  public int getContentLength() {
    return ((ActionRequest) request).getContentLength();
  }

  public InputStream getInputStream() throws IOException {
    return ((ActionRequest) request).getPortletInputStream();
  }

  public boolean allowOnlyHTMLFragments() {
    return true;
  }

  public String getUserName() throws Exception {
    String username = null;
    if (StringUtils.isBlank(username)) {
      username = request.getPreferences().getValue(COOKIE_USERNAME_NAME, null);
    }
    if (StringUtils.isBlank(username)) {
      username = request.getRemoteUser();
    }
    return username;
  }

  public void setUserName(String username) throws Exception {
    PortletPreferences pref = request.getPreferences();
    pref.setValue(COOKIE_USERNAME_NAME, username);
    pref.store();
  }

  public void removeUserName() throws Exception {
    PortletPreferences pref = request.getPreferences();
    pref.setValue(COOKIE_USERNAME_NAME, null);
    pref.store();
  }

  public boolean isUserInRole(String group) throws Exception {
    return request.isUserInRole(group);
  }

  public Map getParameterMap() throws Exception {
    return (multipart ? webfileupload().getParameterMap() : request.getParameterMap());
  }

  /** throws UnsupportedOperationException */
  public String getRemoteAddress() {
    throw new UnsupportedOperationException("Portlets do not support getting remote addresses");
    // return "0.0.0.0";
  }

  public Principal getUserPrincipal() {
    return request.getUserPrincipal();
  }

  public boolean isCommitted() {
    return ((response instanceof RenderResponse)
        ? ((RenderResponse) response).isCommitted()
        : false);
  }

  public URL getResource(String s) throws Exception {
    return pctx.getResource(s);
  }

  public String getBaseURL() {
    // return (request.getScheme() + "://" + request.getServerName() + ":" +
    // request.getServerPort());
    StringBuilder sb = new StringBuilder();
    sb.append(request.getScheme()).append("://").append(request.getServerName());
    if (!((request.getScheme() == "http" && request.getServerPort() == 80)
        || (request.getScheme() == "https" && request.getServerPort() == 443))) {
      sb.append(":").append(request.getServerPort());
    }
    sb.append(request.getContextPath());
    return sb.toString();
  }

  protected long getMaxUploadSize() {
    return Long.MAX_VALUE;
  }

  private WebFileUpload webfileupload() throws Exception {
    if (webfileupload0 == null) {
      webfileupload0 = new WebFileUpload(this, getMaxUploadSize(), null);
    }
    return webfileupload0;
  }
}
