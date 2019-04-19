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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewContext implements Serializable {
  protected transient Map attributes = new HashMap();
  private transient Map models = new HashMap();
  private transient ActionStatus status = new ActionStatus();

  protected String action;
  protected Locale locale;

  public String getAction() {
    return action;
  }

  public void setAction(String s) {
    action = s;
  }

  public void setAttribute(Object s, Object o) {
    attributes.put(s, o);
  }

  public Object getAttribute(Object s) {
    return attributes.get(s);
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale0) {
    locale = locale0;
  }

  public void setAttributes(Map m) {
    attributes = m;
  }

  public Map getAttributes() {
    return attributes;
  }

  public void setModel(String s, Object o) {
    models.put(s, o);
  }

  public Object getModel(String s) {
    return models.get(s);
  }

  public Map getModels() {
    return models;
  }

  public ActionStatus getStatus() {
    return status;
  }
}
