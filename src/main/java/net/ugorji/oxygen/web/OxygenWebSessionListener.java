/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class OxygenWebSessionListener implements HttpSessionListener {
  public void sessionCreated(HttpSessionEvent se) {
    WebApplication app = WebUtils.getWebApplication(se.getSession().getServletContext());
    app.sessionCreated();
  }

  public void sessionDestroyed(HttpSessionEvent se) {
    WebApplication app = WebUtils.getWebApplication(se.getSession().getServletContext());
    app.sessionDestroyed();
  }
}
