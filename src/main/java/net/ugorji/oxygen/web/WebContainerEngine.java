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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import net.ugorji.oxygen.manager.GroupManager;
import net.ugorji.oxygen.manager.UserPasswordManager;
import net.ugorji.oxygen.manager.UserPreferencesManager;
import net.ugorji.oxygen.util.Closeable;
import net.ugorji.oxygen.util.OxygenCacheManager;
import net.ugorji.oxygen.util.OxygenConstants;
import net.ugorji.oxygen.util.OxygenEngine;
import net.ugorji.oxygen.util.PluginManager;
import net.ugorji.oxygen.util.SimpleLock;
import net.ugorji.oxygen.util.StringUtils;

public abstract class WebContainerEngine implements Closeable, OxygenEngine {
  protected UserPasswordManager authMgr;
  protected GroupManager atzMgr;
  protected UserPreferencesManager prefsMgr;
  protected I18nManager i18n;
  protected ActionManager actionMgr;

  protected PluginManager preInitPluginMgr;
  protected PluginManager postInitPluginMgr;

  protected TemplateHandler templateHdlr;

  protected Timer timer;
  protected int numOpenSessions = 0;
  protected OxygenCacheManager cachemgr;

  // make these locks package-private, so that only WikiApplication can see them
  protected SimpleLock longTermLock;
  protected SimpleLock.Strict shortTermAcquiredLock;

  protected WebContainerEngine parent;

  protected Properties props;
  protected Map attributes = new HashMap();

  protected File configDir;
  protected File runtimeDir;
  protected File uploadDir;

  protected Locale defaultLocale;
  protected long startTime = System.currentTimeMillis();

  public long getStartTime() {
    return startTime;
  }

  public GroupManager getAuthorizationManager() {
    return ((atzMgr == null && parent != null) ? parent.getAuthorizationManager() : atzMgr);
  }

  public UserPasswordManager getAuthenticationManager() {
    return ((authMgr == null && parent != null) ? parent.getAuthenticationManager() : authMgr);
  }

  public UserPreferencesManager getUserPreferencesManager() {
    return ((prefsMgr == null && parent != null) ? parent.getUserPreferencesManager() : prefsMgr);
  }

  public I18nManager getI18nManager() {
    return ((i18n == null && parent != null) ? parent.getI18nManager() : i18n);
  }

  public ActionManager getActionManager() {
    return ((actionMgr == null && parent != null) ? parent.getActionManager() : actionMgr);
  }

  public TemplateHandler getTemplateHandler() {
    return ((templateHdlr == null && parent != null) ? parent.getTemplateHandler() : templateHdlr);
  }

  public OxygenCacheManager getCacheManager() {
    return ((cachemgr == null && parent != null) ? parent.getCacheManager() : cachemgr);
  }

  public SimpleLock getLongTermLock() {
    return ((longTermLock == null && parent != null) ? parent.getLongTermLock() : longTermLock);
  }

  public SimpleLock getShortTermAcquiredLock() {
    return ((shortTermAcquiredLock == null && parent != null)
        ? parent.getShortTermAcquiredLock()
        : shortTermAcquiredLock);
  }

  protected Timer getTimer() {
    return ((timer == null && parent != null) ? parent.getTimer() : timer);
  }

  public Locale getDefaultLocale() {
    return defaultLocale;
  }

  public Properties getProperties() {
    return props;
  }

  public String getProperty(String key, String defValue) {
    return props.getProperty(key, defValue);
  }

  public String getProperty(String key) {
    return getProperty(key, null);
  }

  public Object getAttribute(Object s) {
    return attributes.get(s);
  }

  public void setAttribute(Object s, Object o) {
    attributes.put(s, o);
  }

  public void clearAttributes() {
    attributes.clear();
  }

  public void addTask(TimerTask tt, long delay, long interval) {
    getTimer().schedule(tt, delay, interval);
  }

  public int getNumOpenSessions() {
    if (numOpenSessions < 0) numOpenSessions = 0;
    return numOpenSessions;
  }

  public int incrementNumOpenSessions() {
    numOpenSessions++;
    return getNumOpenSessions();
  }

  public int decrementNumOpenSessions() {
    numOpenSessions--;
    return getNumOpenSessions();
  }

  public File getConfigDirectory() {
    configDir = getDir(OxygenConstants.ENGINE_CONFIG_DIR_KEY, configDir);
    return configDir;
  }

  public File getRuntimeDirectory() {
    runtimeDir = getDir(OxygenConstants.ENGINE_RUNTIME_DIR_KEY, runtimeDir);
    return runtimeDir;
  }

  public File getUploadDirectory() {
    uploadDir = getDir(OxygenConstants.ENGINE_UPLOAD_DIR_KEY, uploadDir);
    // System.out.println("getUploadDirectory: " + uploadDir);
    return uploadDir;
  }

  protected void propReplaceReferences(Properties p) {
    //System.out.println("1 >>>>>>>>>>>>>>>>>");
    //p.list(System.out);
    //System.out.println("2 >>>>>>>>>>>>>>>>>");
    StringUtils.replacePropertyReferences(p);
    //p.list(System.out);
    //System.out.println("3 >>>>>>>>>>>>>>>>>");
  }

  private File getDir(String key, File f) {
    // the getDir methods only work for top level containers (with no parents)
    if (f == null && parent == null) {
      String dir = getProperty(key);
      // System.out.println("Key: " + key + ", dir: " + dir + ", parent: " + parent);
      // dir = StringUtils.replacePropertyReferencesInString(dir, props);
      f = new File(dir);
      f.mkdirs();
      // System.out.println("getDir: " + f.getAbsolutePath());
    }
    return f;
  }
}
