/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.util.OxygenUtils;
import net.ugorji.oxygen.util.StringUtils;

/**
 * Base servlet, which allows us to do generic stuff within out servlet sub-class, while this
 * superclass handles exceptions, et al Leverages constants defined in WebConstants i.e.
 * HANDLE_ERROR_KEY, LOG_THROWABLE_KEY, TRACE_REQUEST_KEY, RETHROW_ERROR_KEY
 *
 * @author Ugorji
 */
public abstract class BaseServlet extends HttpServlet {
  protected RequestHints hints;

  protected Properties props = new Properties();

  public void init() throws ServletException {
    super.init();
    try {
      doInit();
    } catch (ServletException sexc) {
      // sexc.printStackTrace();
      throw sexc;
    } catch (Throwable exc) {
      // exc.printStackTrace();
      throw new ServletException(exc);
    }
  }

  protected void doInit() throws Exception {
    String s = getServletContext().getInitParameter(WebConstants.PROPERTIES_RESOURCE_FILENAME_KEY);
    if (StringUtils.isBlank(s)) {
      s = WebConstants.DEFAULT_PROPERTIES_RESOURCE_FILENAME;
    }
    // System.out.println("---- BASESERVLET: Initializing properties from: " + s);
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    InputStream is = cl.getResourceAsStream(s);
    if (is != null) {
      props.load(is);
      CloseUtils.close(is);
    }

    hints = new RequestHints().reset(props);

    // System.out.println(" handleError: " + handleError + " logThrowable: " + logThrowable +  "
    // rethrowError: " + rethrowError +  " traceRequest: " + traceRequest);
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doGetOrPostService(req, res);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doGetOrPostService(req, res);
  }

  protected void doGetOrPostService(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    try {
      // System.out.println("Thread.currentThread(): " + Thread.currentThread());
      // System.out.println("getURLString: " + getURLString(req));
      // System.out.println(getURLString(req));
      handleTraceRequest(req, res);
      handleRequest(req, res);
    } catch (Throwable exc) {
      req.setAttribute(WebConstants.THROWABLE_ATTRIBUTE_KEY, exc);
      handleThrowable(req, res, exc);
    } finally {
      // System.out.println("Response Code for: " +
    }
  }

  protected void handleThrowable(HttpServletRequest req, HttpServletResponse res, Throwable thr)
      throws ServletException, IOException {
    // Do not check if response is committed here. It shouldn't be.
    try {
      if (hints.handleError) {
        if (hints.logThrowable) {
          OxygenUtils.error("Exception serving: " + getURLString(req), thr);
        } else {
          OxygenUtils.error("Exception serving: " + getURLString(req) + ". " + thr);
        }
        // Note that if we say handleError=true and rethrowError=true, then a
        // WebResourceNotFoundException
        // will not cause 404 to be sent to the client. This is because, once an exception is
        // thrown,
        // then the servlet container automatically makes it a 500
        if (thr instanceof WebResourceNotFoundException) {
          // System.out.println("Caught WebResourceNotFoundException: " + getURLString(req));
          res.sendError(HttpServletResponse.SC_NOT_FOUND, thr.getMessage());
        } else if (hints.rethrowError) {
          // System.out.println("---- BASESERVLET: Rethrowing error: " + thr);
          throw thr;
        } else {
          res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, thr.getMessage());
        }
      } else {
        throw thr;
      }
    } catch (RuntimeException re) {
      throw re;
    } catch (ServletException se) {
      throw se;
    } catch (IOException ioe) {
      throw ioe;
    } catch (Throwable exc) {
      throw new ServletException(exc);
    }
  }

  protected void handleTraceRequest(HttpServletRequest req, HttpServletResponse res)
      throws Exception {
    if (hints.traceRequest) {
      OxygenUtils.info("Serving Request: " + getURLString(req));
    }
  }

  protected String getURLString(HttpServletRequest req) {
    String querystr = req.getQueryString();
    String url = req.getRequestURI() + (StringUtils.isBlank(querystr) ? "" : ("?" + querystr));
    return url;
  }

  protected abstract void handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception;
}
