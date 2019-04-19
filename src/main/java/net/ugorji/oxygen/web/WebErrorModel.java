/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;
import net.ugorji.oxygen.util.FreemarkerTemplateHelper;
import net.ugorji.oxygen.util.I18n;
import net.ugorji.oxygen.util.StringUtils;

public class WebErrorModel {
  private static FreemarkerTemplateHelper.ErrorHandler FM_Error_Handler =
      new FreemarkerTemplateHelperErrorHandler();

  private String currentUser;
  private String contextPath;
  private boolean pageNotFound;
  private boolean unauthorizedAccess;
  private boolean internalServerError;
  private String errRequestURI;
  private Integer errStatusCodeINT;
  private int errStatusCode;
  private String errTopic;
  private String errMessage;
  private String excstacktrace;
  private String excMessage;
  private Exception exception;
  private boolean showMessageOnly;
  private boolean onInclude;

  public WebErrorModel(boolean showMessageOnly0, boolean errorOnInclude0) throws Exception {
    onInclude = errorOnInclude0;
    showMessageOnly = showMessageOnly0;

    I18n wi18n = WebLocal.getI18n();
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();

    currentUser = wctx.getUserName();
    contextPath = wctx.getContextPath();
    pageNotFound = false;
    unauthorizedAccess = false;
    internalServerError = false;
    errRequestURI = (String) wctx.getAttribute("javax.servlet.error.request_uri");
    errStatusCodeINT = (Integer) wctx.getAttribute("javax.servlet.error.status_code");
    errStatusCode = -1;
    if (errStatusCodeINT != null) {
      errStatusCode = errStatusCodeINT.intValue();
    }
    errTopic = wi18n.str("common.error.topic_other_prefix") + errStatusCodeINT;
    errMessage = (String) wctx.getAttribute("javax.servlet.error.message");
    if (errStatusCode == 404 || errStatusCode == 403 || errStatusCode == 500) {
      errTopic = wi18n.str("common.error.topic_" + errStatusCode);
    }
    if (StringUtils.isBlank(errMessage)) {
      if (errStatusCode == 404 || errStatusCode == 403 || errStatusCode == 500) {
        errMessage = wi18n.str("common.error.message_" + errStatusCode);
      } else {
        errMessage = "";
      }
    }
    excstacktrace = null;
    excMessage = null;
    exception = (Exception) wctx.getAttribute("javax.servlet.error.exception");
    if (exception != null) {
      excMessage = exception.getMessage();
      excstacktrace = StringUtils.toString(exception);
    }
    // System.out.println("errRequestURI: " + errRequestURI);
  }

  public String getErrMessage() {
    return errMessage;
  }

  public String getErrRequestURI() {
    return errRequestURI;
  }

  public int getErrStatusCode() {
    return errStatusCode;
  }

  public String getErrTopic() {
    return errTopic;
  }

  public String getExcMessage() {
    return excMessage;
  }

  public String getExcstacktrace() {
    return excstacktrace;
  }

  public boolean getShowMessageOnly() {
    return showMessageOnly;
  }

  public boolean getOnInclude() {
    return onInclude;
  }

  public static String i18n(String s) throws Exception {
    return WebLocal.getI18n().str(s);
  }

  public static String i18n(String s, String v1) throws Exception {
    return WebLocal.getI18n().str(s, v1);
  }

  public static String dateTimeAsString() throws Exception {
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.FULL);
    return df.format(new Date());
  }

  /**
   * This method expects that the FreemarkerTemplateHelper can find a error.html which uses a
   * "error" model to show error info If the writer passed is different from the writer
   */
  public static void includeErrorView(Exception thr, Writer w, FreemarkerTemplateHelper fth)
      throws Exception {
    // handle the exceptions ...
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    ViewContext tctx = WebLocal.getViewContext();
    if (wctx == null || tctx == null) {
      throw thr;
    }

    boolean showMessageOnly = false;
    WebContainerEngine we = WebLocal.getWebContainerEngine();
    if (we != null) {
      Properties props = we.getProperties();
      RequestHints rh = new RequestHints().reset(props).handleThrowable(thr);
      showMessageOnly = "true".equals(we.getProperty(WebConstants.SHOW_ERROR_MESSAGE_ONLY_KEY));
    }

    w.flush();

    // now, write the exception into the writer nicely
    try {
      wctx.setAttribute("javax.servlet.error.message", "[Render Error]: " + thr.getMessage());
      wctx.setAttribute("javax.servlet.error.exception", thr);
      tctx.setModel("error", new WebErrorModel(showMessageOnly, true));
      fth.write("error.html", tctx.getModels(), w);
    } finally {
      wctx.setAttribute("javax.servlet.error.message", null);
      wctx.setAttribute("javax.servlet.error.exception", null);
      tctx.setModel("error", null);
    }
    // We should re-throw the error, if the writer is not the same from the WebInteractionContext
    // the only time we want to swallow the error, is if we are rendering into the web interaction
    // context writer
    if (w != wctx.getWriter()) {
      throw thr;
    }
  }

  private static class FreemarkerTemplateHelperErrorHandler
      implements FreemarkerTemplateHelper.ErrorHandler {
    public void handleError(FreemarkerTemplateHelper fth, Exception thr, Writer w)
        throws Exception {
      includeErrorView(thr, w, fth);
    }
  }

  public static void setFreemarkerTemplateErrorHandler(boolean b) {
    if (b) FreemarkerTemplateHelper.setErrorHandler(FM_Error_Handler);
    else FreemarkerTemplateHelper.setErrorHandler(null);
  }
}
