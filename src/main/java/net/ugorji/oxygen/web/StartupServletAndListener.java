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
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import net.ugorji.oxygen.util.CloseUtils;

public class StartupServletAndListener implements ServletContextListener, Servlet {

  protected ServletConfig servletConfig;

  public void contextInitialized(ServletContextEvent evt) {
    doContextInitialized(evt.getServletContext());
  }

  public void contextDestroyed(ServletContextEvent evt) {
    // System.out.println("Destroying webapp context");
    doContextDestroyed(evt.getServletContext());
  }

  public void init(ServletConfig sc) throws ServletException {
    servletConfig = sc;
    doContextInitialized(servletConfig.getServletContext());
  }

  public void destroy() {
    doContextDestroyed(servletConfig.getServletContext());
  }

  public ServletConfig getServletConfig() {
    return servletConfig;
  }

  public String getServletInfo() {
    return getClass().getName();
  }

  public void service(ServletRequest arg0, ServletResponse arg1)
      throws ServletException, IOException {
    throw new ServletException("This servlet should not service any requests");
  }

  protected void doContextInitialized(ServletContext sc) {}

  protected void doContextDestroyed(ServletContext sc) {}

  public static Properties getServletContextProperties(
      ServletContext sctx, String resource, String webRootKey) throws Exception {
    Properties props = new Properties();
    InputStream is = sctx.getResourceAsStream(resource);
    if (is != null) {
      props.load(is);
      CloseUtils.close(is);
    }

    for (Enumeration enum0 = sctx.getInitParameterNames(); enum0.hasMoreElements(); ) {
      String _key = (String) enum0.nextElement();
      String _val = sctx.getInitParameter(_key);
      props.setProperty(_key, _val);
    }

    if (webRootKey != null) {
      String webroot = sctx.getRealPath("/");
      if (webroot != null) {
        String _val = webroot.trim().replace('\\', '/');
        props.setProperty(webRootKey, _val);
      }
    }

    return props;
  }
}
