/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import net.ugorji.oxygen.util.Closeable;

public class JSPTemplateHelper implements Closeable {
  protected String baseResourceLocation;
  protected String[] templates;
  protected Map cache = new HashMap();
  protected boolean useCache = true;

  protected JSPTemplateHelper() {}

  public JSPTemplateHelper(String baseLocation0, String[] templates0) {
    init(baseLocation0, templates0);
  }

  protected void init(String baseLocation0, String[] templates0) {
    baseResourceLocation = baseLocation0;
    if (!baseResourceLocation.endsWith("/")) {
      baseResourceLocation = baseResourceLocation + "/";
    }
    LinkedHashSet lhs = new LinkedHashSet();
    if (templates0 != null) {
      lhs.addAll(Arrays.asList(templates0));
    }
    templates = (String[]) lhs.toArray(new String[0]);
  }

  public void close() {
    cache.clear();
  }

  public String findResourcePath(String jsppage) throws Exception {
    if (useCache && cache.containsKey(jsppage)) {
      return (String) cache.get(jsppage);
    }
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    String s = null;
    for (int i = 0; i < templates.length; i++) {
      s = baseResourceLocation + templates[i] + "/" + jsppage;
      if (wctx.getResource(s) != null) {
        break;
      }
    }
    // System.out.println("findResourcePath: " + jsppage + ": " + s);
    if (useCache) {
      synchronized (cache) {
        cache.put(jsppage, s);
      }
    }
    return s;
  }
}
