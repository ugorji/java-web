/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.util.Locale;
import java.util.Map;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ugorji.oxygen.util.I18n;

public interface WebApplication {
  void init(PortletContext ctx) throws Exception;

  void init(ServletContext ctx) throws Exception;

  void sessionCreated();

  void sessionDestroyed();

  WebInteractionContext newWebInteractionContext(
      HttpServletRequest request, HttpServletResponse response) throws Exception;

  WebInteractionContext newWebInteractionContext(PortletRequest request, PortletResponse response)
      throws Exception;

  WebUserSession newWebUserSession() throws Exception;
  // String getRedirectAfterPostSuffix();
  Map getPortletRenderParameters() throws Exception;

  String getRedirectURL();

  TemplateHandler getTemplateHandler();

  String getEncoding();

  WebAction getAction(String s);

  void atInitOfRequest() throws Exception;

  void atStartOfRequest() throws Exception;

  void atEndOfRequest();

  I18n getI18n(Locale locale) throws Exception;

  void checkAccess() throws Exception;

  void shutdown();
  // boolean isRetainReferenceToLastViewContext();
}
