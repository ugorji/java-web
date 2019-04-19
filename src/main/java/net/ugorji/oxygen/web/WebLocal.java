/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.util.Properties;
import net.ugorji.oxygen.util.I18n;
import net.ugorji.oxygen.util.OxyLocal;

/**
 * This uses ThreadLocals to keep track of variables that are relevant within the context of a
 * request/response thread of execution.
 *
 * @author ugorjid
 */
public class WebLocal {
  public static void setI18n(I18n o) {
    OxyLocal.set(I18n.class, o);
  }

  public static void setWebInteractionContext(WebInteractionContext o) {
    OxyLocal.set(WebInteractionContext.class, o);
  }

  public static void setViewContext(ViewContext o) {
    OxyLocal.set(ViewContext.class, o);
  }

  public static void setTemplateHandler(TemplateHandler o) {
    OxyLocal.set(TemplateHandler.class, o);
  }

  public static void setProperties(Properties o) {
    OxyLocal.set(Properties.class, o);
  }

  public static void setWebUserSession(WebUserSession o) {
    OxyLocal.set(WebUserSession.class, o);
  }

  public static void setWebContainerEngine(WebContainerEngine o) {
    OxyLocal.set(WebContainerEngine.class, o);
  }

  public static WebInteractionContext getWebInteractionContext() {
    return (WebInteractionContext) OxyLocal.get(WebInteractionContext.class);
  }

  public static ViewContext getViewContext() {
    return (ViewContext) OxyLocal.get(ViewContext.class);
  }

  public static TemplateHandler getTemplateHandler() {
    return (TemplateHandler) OxyLocal.get(TemplateHandler.class);
  }

  public static I18n getI18n() {
    return (I18n) OxyLocal.get(I18n.class);
  }

  public static Properties getProperties() {
    return (Properties) OxyLocal.get(Properties.class);
  }

  public static WebUserSession getWebUserSession() {
    return (WebUserSession) OxyLocal.get(WebUserSession.class);
  }

  public static WebContainerEngine getWebContainerEngine() {
    return (WebContainerEngine) OxyLocal.get(WebContainerEngine.class);
  }
}
