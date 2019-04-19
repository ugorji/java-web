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

/**
 * This abstracts like the request, response and session allowing us to use this in either a servlet
 * or a portlet context
 *
 * @author ugorjid
 */
public interface WebInteractionContext {
  Object getSessionAttribute(String key);

  void setSessionAttribute(String key, Object value);

  void invalidateSession();

  PrintWriter getWriter() throws Exception;

  String getParameter(String string) throws Exception;

  Object getAttribute(String key);

  void setAttribute(String key, Object value);

  String getHeader(String string);

  String[] getParameterValues(String string) throws Exception;

  Enumeration getParameterNames() throws Exception;

  String getContextPath();

  Locale getLocale();

  void include(String basepath) throws Exception;

  void removeSessionAttribute(String key);

  void removeUserName() throws Exception;

  void setUserName(String username2) throws Exception;

  String getUserName() throws Exception;

  boolean isUserInRole(String group) throws Exception;

  OutputStream getOutputStream() throws Exception;

  void setContentType(String mimetype);

  void setHeader(String string, String string2);

  String getMimeType(String name);

  int getContentLength();

  InputStream getInputStream() throws IOException;
  // does this support full HTML, or just fragments (portlets only support fragments)
  boolean allowOnlyHTMLFragments();

  Map getParameterMap() throws Exception;

  String encodeURL(String s, boolean redirectURL);

  void sendRedirect(String s) throws Exception;

  String getRemoteAddress();

  ViewContext toViewContext() throws Exception;

  String toURLString(ViewContext v, Map extraparams);

  String getSessionId();

  Principal getUserPrincipal();

  boolean isCommitted();

  URL getResource(String s) throws Exception;

  boolean isMultipartContent() throws Exception;

  File[] getUploadedFiles() throws Exception;

  String getBaseURL();
}
