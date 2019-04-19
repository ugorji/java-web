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
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.util.Closeable;
import net.ugorji.oxygen.util.OxygenUtils;

public class ActionManager implements Closeable {
  private Map actions = new HashMap();

  public ActionManager(Properties p, String prefix) throws Exception {
    Properties pp = new Properties();
    OxygenUtils.extractProps(p, pp, prefix, true);
    for (Enumeration enum0 = pp.propertyNames(); enum0.hasMoreElements(); ) {
      String k = (String) enum0.nextElement();
      String v = pp.getProperty(k);
      // System.out.println("v: " + v);
      WebAction w = (WebAction) OxygenUtils.getClass(v).newInstance();
      actions.put(k, w);
    }
  }

  public WebAction[] getAllActions() {
    return (WebAction[]) actions.values().toArray(new WebAction[0]);
  }

  public WebAction getAction(String s) {
    return (WebAction) actions.get(s);
  }

  public String[] getActionKeys() {
    return (String[]) actions.keySet().toArray(new String[0]);
  }

  public void close() {
    for (Iterator itr = actions.values().iterator(); itr.hasNext(); ) {
      WebAction wa = (WebAction) itr.next();
      CloseUtils.close(wa);
    }
  }
}
