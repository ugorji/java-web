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
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.util.OxygenUtils;
import net.ugorji.oxygen.util.StringUtils;

public abstract class BasePortlet extends GenericPortlet {
  protected RequestHints hints;
  protected Properties props = new Properties();

  public void init() throws PortletException {
    super.init();
    try {
      doInit();
    } catch (PortletException sexc) {
      // sexc.printStackTrace();
      throw sexc;
    } catch (Throwable exc) {
      // exc.printStackTrace();
      throw new PortletException(exc);
    }
  }

  public void processAction(ActionRequest req, ActionResponse res)
      throws PortletException, IOException {
    try {
      handleTraceRequest(req, res);
      handleProcessAction(req, res);
    } catch (Throwable exc) {
      req.setAttribute(WebConstants.THROWABLE_ATTRIBUTE_KEY, exc);
      handleThrowable(req, res, exc);
    }
  }

  public void doView(RenderRequest req, RenderResponse res) throws PortletException, IOException {
    try {
      handleDoView(req, res);
    } catch (Throwable exc) {
      handleThrowable(req, res, exc);
    }
  }

  protected void doInit() throws Exception {
    String s = getInitParameter(WebConstants.PROPERTIES_RESOURCE_FILENAME_KEY);
    if (StringUtils.isBlank(s)) {
      s = WebConstants.DEFAULT_PROPERTIES_RESOURCE_FILENAME;
    }
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    InputStream is = cl.getResourceAsStream(s);
    if (is != null) {
      props.load(is);
      CloseUtils.close(is);
    }
    hints = new RequestHints().reset(props);
  }

  protected void handleThrowable(PortletRequest req, PortletResponse resp, Throwable thr)
      throws PortletException, IOException {
    try {
      if (hints.handleError) {
        if (hints.logThrowable) {
          OxygenUtils.error("Exception serving portlet request ... ", thr);
        } else {
          OxygenUtils.error("Exception serving portlet request ... " + thr);
        }
      }
      throw thr;
    } catch (RuntimeException re) {
      throw re;
    } catch (PortletException se) {
      throw se;
    } catch (IOException ioe) {
      throw ioe;
    } catch (Throwable exc) {
      throw new PortletException(exc);
    }
  }

  protected void handleTraceRequest(ActionRequest req, ActionResponse res) {}

  protected abstract void handleProcessAction(ActionRequest request, ActionResponse response)
      throws Exception;

  protected abstract void handleDoView(RenderRequest request, RenderResponse response)
      throws Exception;
}
