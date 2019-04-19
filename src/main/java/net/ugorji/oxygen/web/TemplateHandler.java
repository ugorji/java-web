/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import net.ugorji.oxygen.util.Closeable;

/**
 * This is the actual class that handles rendering of a view. This abstraction allows us use
 * FreeMarket, JSP or other rendering technologies within the framework.
 *
 * @author ugorjid
 */
public interface TemplateHandler extends Closeable {
  public void render() throws Exception;
}
