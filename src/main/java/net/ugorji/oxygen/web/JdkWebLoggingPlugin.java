/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.io.File;
import net.ugorji.oxygen.util.JdkLoggingPlugin;
import net.ugorji.oxygen.util.OxygenConstants;

/**
 * Initializes the JDK logging mechanism, setting the log information to go into the
 * logs/webengine.log file of the engine runtime directory
 *
 * @author ugorji
 */
public class JdkWebLoggingPlugin extends JdkLoggingPlugin {
  public void init() throws Exception {
    // System.out.println("About initializing JdkWebLoggingPlugin: ");
    WebContainerEngine we = WebLocal.getWebContainerEngine();
    logdir = new File(we.getRuntimeDirectory(), "logs");
    logfilepattern = "webengine.%g.%u.log";
    append = "true".equals(we.getProperty(OxygenConstants.LOGGER_APPEND_KEY));
    levelString = we.getProperty(OxygenConstants.LOGGER_LEVEL_KEY, levelString);
    useParentHandlers = false;
    super.doInit();
  }
}
