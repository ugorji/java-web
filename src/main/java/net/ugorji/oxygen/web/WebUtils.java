/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.portlet.PortletContext;
import javax.servlet.ServletContext;
import net.ugorji.oxygen.util.OxygenUtils;

public class WebUtils {

  public static void appendQueryParameters(StringBuffer buf, Map m) {
    for (Iterator itr = m.entrySet().iterator(); itr.hasNext(); ) {
      Map.Entry ent = (Map.Entry) itr.next();
      String key = (String) ent.getKey();
      Object val = ent.getValue();
      if (val != null) {
        if (val instanceof String) {
          buf.append("&").append(key).append("=").append(val);
        } else if (val instanceof String[]) {
          String[] val2 = (String[]) val;
          for (int i = 0; i < val2.length; i++) {
            buf.append("&").append(key).append("=").append(val2[i]);
          }
        }
      }
    }
  }

  public static String encodeURL(String s) {
    if (WebLocal.getWebInteractionContext() != null) {
      s = WebLocal.getWebInteractionContext().encodeURL(s, false);
    }
    return s;
  }

  public static WebApplication getWebApplication(ServletContext ctx) {
    String key = WebConstants.WEB_APPLICATION_KEY;
    WebApplication app = (WebApplication) ctx.getAttribute(key);
    if (app == null) {
      try {
        String s = ctx.getInitParameter(key);
        app = (WebApplication) OxygenUtils.getClass(s).newInstance();
        app.init(ctx);
        ctx.setAttribute(key, app);
      } catch (RuntimeException rexc) {
        throw rexc;
      } catch (Exception exc) {
        throw new RuntimeException(exc);
      }
    }
    return app;
  }

  public static WebApplication getWebApplication(PortletContext ctx) {
    String key = WebConstants.WEB_APPLICATION_KEY;
    WebApplication app = (WebApplication) ctx.getAttribute(key);
    if (app == null) {
      try {
        String s = ctx.getInitParameter(key);
        app = (WebApplication) OxygenUtils.getClass(s).newInstance();
        app.init(ctx);
        ctx.setAttribute(key, app);
      } catch (RuntimeException rexc) {
        throw rexc;
      } catch (Exception exc) {
        throw new RuntimeException(exc);
      }
    }
    return app;
  }

  public static Properties getInitProperties(ServletContext ctx) {
    Properties p = new Properties();
    String s = null;
    s = ctx.getRealPath("/");
    s = (s == null) ? "." : s;
    s = s.replace('\\', '/');
    s = (s.endsWith("/")) ? (s.substring(0, s.length() - 1)) : s;
    p.setProperty(WebConstants.WEB_BASE_PATH, s);
    for (Enumeration e = ctx.getInitParameterNames(); e.hasMoreElements(); ) {
      s = (String) e.nextElement();
      p.setProperty(s, ctx.getInitParameter(s));
    }
    return p;
  }

  public static Properties getInitProperties(PortletContext ctx) {
    Properties p = new Properties();
    String s = null;
    s = ctx.getRealPath("/");
    s = (s == null) ? "." : s;
    s = (s.endsWith("/")) ? (s.substring(0, s.length() - 1)) : s;
    p.setProperty(WebConstants.WEB_BASE_PATH, s);
    for (Enumeration e = ctx.getInitParameterNames(); e.hasMoreElements(); ) {
      s = (String) e.nextElement();
      p.setProperty(s, ctx.getInitParameter(s));
    }
    return p;
  }

  public static ActionStatus retrieveLastActionStatus(ViewContext vctx) {
    ActionStatus as = vctx.getStatus();
    ActionStatus as2 = as.getLinked(WebConstants.ACTIONSTATUS_REFERRED_BY);
    return ((as2 == null) ? as : as2);
  }

  static WebUserSession retrieveWebUserSession(WebInteractionContext webctx, WebApplication app)
      throws Exception {
    WebUserSession wus =
        (WebUserSession) webctx.getSessionAttribute(WebConstants.WEB_USERSESSION_KEY);
    if (wus == null) {
      wus = app.newWebUserSession();
      webctx.setSessionAttribute(WebConstants.WEB_USERSESSION_KEY, wus);
    }
    return wus;
  }

  // public static OxygenRevision getDiff(String text1, String text2) throws Exception {
  //   return OxygenRevision.getDiff(text1, text2, WebLocal.getI18n());
  // }
}
