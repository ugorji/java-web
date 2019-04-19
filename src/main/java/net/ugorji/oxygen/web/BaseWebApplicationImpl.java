/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import net.ugorji.oxygen.util.I18n;
import net.ugorji.oxygen.util.OxyLocal;

public abstract class BaseWebApplicationImpl implements WebApplication {
  protected WebContainerEngine oce0;
  // protected Throwable engineLoadingError;
  // protected Thread engineLoadThread;
  protected ServletContext sctx;
  protected PortletContext pctx;

  public void sessionCreated() {
    oce().incrementNumOpenSessions();
  }

  public void sessionDestroyed() {
    oce().decrementNumOpenSessions();
  }

  public WebInteractionContext newWebInteractionContext(
      PortletRequest request, PortletResponse response) throws Exception {
    throw new UnsupportedOperationException();
  }

  public TemplateHandler getTemplateHandler() {
    return oce().getTemplateHandler();
  }

  public WebAction getAction(String s) {
    WebAction wa = oce().getActionManager().getAction(s);
    if (wa == null) {
      throw new WebResourceNotFoundException(
          WebLocal.getI18n().str("general.action_not_supported", s));
    }
    return wa;
  }

  public void atInitOfRequest() throws Exception {
    WebLocal.setWebContainerEngine(oce());
  }

  public void atStartOfRequest() throws Exception {
    // ensure that the engine is not locked et al,
    // and grab a lock if an admin action
    WebAction action = getAction(WebLocal.getViewContext().getAction());
    OxyLocal.set(MyWebActionHolder.class, action);
    if (action.isFlagSet(WebAction.FLAG_ADMIN_ACTION)) {
      oce()
          .getShortTermAcquiredLock()
          .hold(WebLocal.getI18n().str("general.engine_locked_short_term"));
    } else if (action.isFlagSet(WebAction.FLAG_WRITE_ACTION)) {
      if (oce().getLongTermLock().isHeld()) {
        throw new OxygenWebException(oce().getLongTermLock().getHoldMessage());
      }
      if (oce().getShortTermAcquiredLock().isHeld()) {
        oce().getShortTermAcquiredLock().waitTillReleased();
      }
    }
  }

  public void atEndOfRequest() {
    WebAction action = getWebActionAtStartOfRequest();
    OxyLocal.set(MyWebActionHolder.class, null);
    // it's possible that the action in viewcontext is changed during the thread execution,
    // so we can't depend on this to work. Instead, use a value stored on the threadlocal
    // Engine eng = engine();
    // WebAction action = eng.getAction(WebLocal.getViewContext().getAction());
    if (action != null && action.isFlagSet(WebAction.FLAG_ADMIN_ACTION)) {
      oce().getShortTermAcquiredLock().release();
    }
    WebLocal.setWebContainerEngine(null);
  }

  public I18n getI18n(Locale locale) throws Exception {
    return oce().getI18nManager().getI18n(locale);
  }

  public void init(ServletContext ctx) throws Exception {
    sctx = ctx;
    Properties p = WebUtils.getInitProperties(ctx);
    updatePropertiesForInit(p);
    oce0 = createWebContainerEngine(p);
  }

  public void init(PortletContext ctx) throws Exception {
    pctx = ctx;
    Properties p = WebUtils.getInitProperties(ctx);
    updatePropertiesForInit(p);
    oce0 = createWebContainerEngine(p);
  }

  public void checkAccess() throws Exception {}

  public String getRedirectURL() {
    ViewContext vctx = WebLocal.getViewContext();
    return WebLocal.getWebInteractionContext().toURLString(vctx, null);
  }

  public Map getPortletRenderParameters() throws Exception {
    Map m = new HashMap(WebLocal.getWebInteractionContext().getParameterMap());
    return m;
  }

  public int render(WebAction wa) throws Exception {
    return wa.render();
  }

  public int processAction(WebAction wa) throws Exception {
    return wa.processAction();
  }

  protected WebAction getWebActionAtStartOfRequest() {
    return (WebAction) OxyLocal.get(MyWebActionHolder.class);
  }

  protected WebContainerEngine oce() {
    // if(oce0 == null) {
    //   throw new OxygenWebException("prior error loading engine: " + engineLoadingError);
    // }
    return oce0;
  }

  private class MyWebActionHolder {}

  protected abstract WebContainerEngine createWebContainerEngine(Properties p) throws Exception;

  protected abstract void updatePropertiesForInit(Properties p);
}
