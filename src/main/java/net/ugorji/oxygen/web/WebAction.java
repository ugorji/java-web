/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import net.ugorji.oxygen.util.BitFlagHandler;
import net.ugorji.oxygen.util.Closeable;
import net.ugorji.oxygen.util.OxygenUtils;

/**
 * We use a light-weight MVC model for this implementation, where we define simple actions and allow
 * them to be plugged in. This interface defines the single contract for this.
 *
 * @author ugorji
 */
public interface WebAction extends Closeable, BitFlagHandler {
  int ACTION_PROCESSING_COMPLETED = OxygenUtils.makeFlag(0);
  int RENDER_COMPLETED = OxygenUtils.makeFlag(1);
  int REQUEST_PROCESSING_COMPLETED = OxygenUtils.makeFlag(2);
  int REDIRECT_AFTER_POST = OxygenUtils.makeFlag(3); // 8
  int REDIRECT_EXTERNAL = OxygenUtils.makeFlag(4); // 8

  int FLAG_ADMIN_ACTION = OxygenUtils.makeFlag(1);
  int FLAG_WRITE_ACTION = OxygenUtils.makeFlag(2);

  int processAction() throws Exception;

  int render() throws Exception;

  void includeView() throws UnsupportedOperationException, Exception;

  String getContentType();
}
