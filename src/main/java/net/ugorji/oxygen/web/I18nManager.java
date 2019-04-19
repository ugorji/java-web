/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.ugorji.oxygen.util.Closeable;
import net.ugorji.oxygen.util.I18n;

public class I18nManager implements Closeable {

  private Map instances = new HashMap();
  private String path;
  private Locale defaultLocale;

  public I18nManager(String path0, Locale defaultLocale0) {
    path = path0;
    defaultLocale = defaultLocale0;
    if (defaultLocale == null) {
      defaultLocale = Locale.getDefault();
    }
  }

  public I18nManager(String path0) {
    this(path0, null);
  }

  public I18n getI18n() {
    return getI18n(null);
  }

  public synchronized I18n getI18n(Locale _locale) {
    if (_locale == null) {
      _locale = defaultLocale;
    }
    I18n inst = (I18n) instances.get(_locale);
    if (inst == null) {
      inst = new I18n(path, _locale);
      instances.put(_locale, inst);
    }
    return inst;
  }

  public void close() {
    instances.clear();
    instances = null;
  }
}
