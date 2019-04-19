/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ugorji.oxygen.util.OxyLocal;
import net.ugorji.oxygen.util.OxygenUtils;
import net.ugorji.oxygen.util.StringUtils;

public class OxygenWebServlet extends BaseServlet {
  // private static String REDIRECT_AFTER_POST_ERROR_MESSAGE =
  //  "No ViewContext was found to allow access to this redirect-after-post URL";

  private WebApplication app;
  private Random rand = new Random();

  protected void doInit() throws Exception {
    super.doInit();
    app = WebUtils.getWebApplication(getServletContext());
  }

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    WebInteractionContext webctx = null;
    ViewContext vctx = null;
    String s = null;
    // boolean doRedirectAfterPost = false;
    try {
      app.atInitOfRequest();
      webctx = app.newWebInteractionContext(request, response);
      WebLocal.setWebInteractionContext(webctx);

      // String redirectAfterPostQueryURI = request.getContextPath() +
      // app.getRedirectAfterPostSuffix();
      // boolean redirectAfterPost = request.getRequestURI().equals(redirectAfterPostQueryURI);

      // if(redirectAfterPost) {
      //  vctx = (ViewContext)webctx.getSessionAttribute(WebConstants.VIEW_CONTEXT_KEY);
      //  if(vctx == null) {
      //    throw new RuntimeException(REDIRECT_AFTER_POST_ERROR_MESSAGE);
      //  }
      // } else {
      //  vctx = webctx.toViewContext();
      // }

      vctx = webctx.toViewContext();

      // System.out.println("ViewContext: " + vctx);
      WebLocal.setViewContext(vctx);

      WebUserSession wus = WebUtils.retrieveWebUserSession(webctx, app);
      WebLocal.setWebUserSession(wus);

      tryToAddRedirectAfterPostLastActionStatus(webctx, wus, vctx);

      app.atStartOfRequest();

      WebLocal.setI18n(app.getI18n(vctx.getLocale()));

      TemplateHandler thdlr = app.getTemplateHandler();
      WebLocal.setTemplateHandler(thdlr);

      WebAction action = app.getAction(vctx.getAction());
      String enc = app.getEncoding();
      response.setContentType(action.getContentType()); // + "; charset=" + enc);
      request.setCharacterEncoding(enc);

      app.checkAccess();

      // if(redirectAfterPost) {
      //  action.render();
      // } else {
      //  //if(action == null) { System.out.println("Bad action: " + action); }
      //  int status = action.processAction();
      //  if(!(OxygenUtils.isFlagSet(status, WebAction.REQUEST_PROCESSING_COMPLETED))) {
      //    if(OxygenUtils.isFlagSet(status, WebAction.REDIRECT_AFTER_POST)) {
      //      doRedirectAfterPost = true;
      //      //response.sendRedirect(response.encodeRedirectURL(redirectAfterPostQueryURI));
      //      String url2 = webctx.toURLString(
      //    } else {
      //      status = action.render();
      //    }
      //  }
      // }

      int status = action.processAction();
      if (!(OxygenUtils.isFlagSet(status, WebAction.REQUEST_PROCESSING_COMPLETED))) {
        if (OxygenUtils.isFlagSet(status, WebAction.REDIRECT_AFTER_POST)) {
          String rkey =
              System.currentTimeMillis() + "." + String.valueOf(rand.nextInt(Integer.MAX_VALUE));
          wus.put(rkey, vctx.getStatus());
          webctx.setHeader(WebConstants.REDIRECTAFTERPOST_LASTACTIONSTATUS_KEY_HEADER, rkey);
          response.sendRedirect(app.getRedirectURL());
        } else if (OxygenUtils.isFlagSet(status, WebAction.REDIRECT_EXTERNAL)) {
          // do nothing ... we are done
        } else {
          status = action.render();
        }
      }
    } finally {
      app.atEndOfRequest();
      // if(webctx != null) {
      //  ViewContext vctx2 = (doRedirectAfterPost || app.isRetainReferenceToLastViewContext()) ?
      // vctx : null;
      //  webctx.setSessionAttribute(WebConstants.VIEW_CONTEXT_KEY, vctx2);
      // }
      OxyLocal.clear();
    }
  }

  public void destroy() {
    super.destroy();
    app.shutdown();
    app = null;
  }

  private static void tryToAddRedirectAfterPostLastActionStatus(
      WebInteractionContext webctx, WebUserSession wus, ViewContext vctx) {
    String s = webctx.getHeader(WebConstants.REDIRECTAFTERPOST_LASTACTIONSTATUS_KEY_HEADER);
    if (!StringUtils.isBlank(s)) {
      ActionStatus as = (ActionStatus) wus.get(s, true);
      if (as != null) {
        vctx.getStatus().addLinked(WebConstants.ACTIONSTATUS_REFERRED_BY, as);
      }
    }
  }
}
