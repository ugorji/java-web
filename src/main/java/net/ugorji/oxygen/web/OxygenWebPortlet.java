/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import net.ugorji.oxygen.util.I18n;
import net.ugorji.oxygen.util.OxyLocal;

/*
 * Always work, like U can't guarantee that state from the processAction is carried over
 * to the doView. This is because, there may be diff net.ugorji.oxygen-web apps portlets here,
 * and we can't assume that the ViewContext from last processAction is appropriate
 * for current doView.
 * E.g. processAction for wiki is done, then doView for forum is done, then doView for wiki.
 */
public class OxygenWebPortlet extends BasePortlet {
  private WebApplication app;

  protected void doInit() throws Exception {
    super.doInit();
    app = WebUtils.getWebApplication(getPortletContext());
  }

  protected void handleProcessAction(ActionRequest request, ActionResponse response)
      throws Exception {
    WebInteractionContext webctx = null;
    ViewContext vctx = null;
    try {
      app.atInitOfRequest();
      webctx = app.newWebInteractionContext(request, response);
      WebLocal.setWebInteractionContext(webctx);
      WebUserSession wus = WebUtils.retrieveWebUserSession(webctx, app);
      WebLocal.setWebUserSession(wus);
      app.atStartOfRequest();

      String enc = app.getEncoding();
      request.setCharacterEncoding(enc);

      vctx = webctx.toViewContext();
      WebLocal.setViewContext(vctx);

      I18n i18n = app.getI18n(vctx.getLocale());
      WebLocal.setI18n(i18n);

      app.checkAccess();

      WebAction action = app.getAction(vctx.getAction());
      action.processAction();

      response.setRenderParameters(app.getPortletRenderParameters());
    } finally {
      app.atEndOfRequest();
      // if(webctx != null) {
      //  webctx.setSessionAttribute(WebConstants.VIEW_CONTEXT_KEY, vctx);
      //  response.setRenderParameters(getRenderParameterMap(request));
      //  response.setRenderParameters(app.getPortletRenderParameters());
      // }
      // Don't clear OxyLocal here ... assume doView is in the same thread
      OxyLocal.clear();
    }
  }

  protected void handleDoView(RenderRequest request, RenderResponse response) throws Exception {
    WebInteractionContext webctx = null;
    ViewContext vctx = null;
    try {
      webctx = app.newWebInteractionContext(request, response);
      WebLocal.setWebInteractionContext(webctx);
      WebUserSession wus = WebUtils.retrieveWebUserSession(webctx, app);
      WebLocal.setWebUserSession(wus);
      app.atStartOfRequest();

      String enc = app.getEncoding();
      response.setContentType("text/html; charset=" + enc);

      // vctx = (ViewContext)webctx.getSessionAttribute(WebConstants.VIEW_CONTEXT_KEY);
      vctx = webctx.toViewContext();
      WebLocal.setViewContext(vctx);

      I18n i18n = app.getI18n(vctx.getLocale());
      WebLocal.setI18n(i18n);

      app.checkAccess();

      WebAction action = app.getAction(vctx.getAction());
      action.render();
    } finally {
      app.atEndOfRequest();
      OxyLocal.clear();
    }
  }

  public void destroy() {
    super.destroy();
    app.shutdown();
    app = null;
  }

  // private Map getRenderParameterMap(ActionRequest request) {
  //  Map m = new HashMap(request.getParameterMap());
  //  for(Iterator itr = m.entrySet().iterator(); itr.hasNext(); ) {
  //    Map.Entry me = (Map.Entry)itr.next();
  //    if(((String)me.getKey()).startsWith(WebConstants.NON_RENDER_PARAMETER_PREFIX)) {
  //      itr.remove();
  //    }
  //  }
  //  return m;
  // }

}
