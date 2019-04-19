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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.ugorji.oxygen.util.OxygenUtils;

public class WebPluginManager {
  private Map actions = new HashMap();

  public WebPluginManager(Properties p, String prefix) throws Exception {
    Properties pp = new Properties();
    OxygenUtils.extractProps(p, pp, prefix, true);
    for (Enumeration enum0 = pp.propertyNames(); enum0.hasMoreElements(); ) {
      String k = (String) enum0.nextElement();
      String v = pp.getProperty(k);
      Runnable w = (Runnable) OxygenUtils.getClass(v).newInstance();
      actions.put(k, w);
    }
  }

  public Runnable[] getAllRunnables() {
    return (Runnable[]) actions.values().toArray(new Runnable[0]);
  }

  public Runnable getRunnable(String s) {
    return (Runnable) actions.get(s);
  }
}
