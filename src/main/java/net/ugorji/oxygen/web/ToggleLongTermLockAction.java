/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import net.ugorji.oxygen.util.SimpleLock;
import net.ugorji.oxygen.util.StringUtils;

/** This action can only be used in an AJAX context, since it does no rendering */
public class ToggleLongTermLockAction extends GenericWebAction {
  {
    setFlag(FLAG_ADMIN_ACTION);
  }

  public int processAction() throws Exception {
    SimpleLock sl = WebLocal.getWebContainerEngine().getLongTermLock();
    if (sl.isHeld()) {
      sl.release();
    } else {
      String msg = WebLocal.getWebInteractionContext().getParameter("m");
      msg =
          StringUtils.nonNullString(msg, WebLocal.getI18n().str("general.engine_locked_long_term"));
      sl.hold(msg);
    }
    return ACTION_PROCESSING_COMPLETED;
  }
}
