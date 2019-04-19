/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Within the net.ugorji.oxygen.web package, an ActionStatus can be chained, and help indicate the results of
 * an action invocation. With this, we can do a bunch of redirects, and bundle the results of all
 * the actions happening, and show the user
 *
 * @author ugorjid
 */
public class ActionStatus implements Serializable {
  public static final int ERROR = 1;
  public static final int NOTICE = 2;
  public static final int UNDEFINED = 0;

  private String description = "";
  private int type = UNDEFINED;
  private Map linked = new LinkedHashMap();

  public void addLinked(String key, ActionStatus as) {
    linked.put(key, as);
  }

  public Map getAllLinked() {
    return linked;
  }

  public ActionStatus getLinked(String key) {
    return (ActionStatus) linked.get(key);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
