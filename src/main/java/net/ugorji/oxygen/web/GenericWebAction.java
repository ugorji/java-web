/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import net.ugorji.oxygen.util.BitFlagHandlerImpl;

public class GenericWebAction extends BitFlagHandlerImpl implements WebAction {

  public void includeView() throws UnsupportedOperationException, Exception {
    throw new UnsupportedOperationException();
  }

  public int render() throws Exception {
    return RENDER_COMPLETED;
  }

  public int processAction() throws Exception {
    return ACTION_PROCESSING_COMPLETED;
  }

  public String getContentType() {
    return "text/html";
  }

  public void close() {}
}
