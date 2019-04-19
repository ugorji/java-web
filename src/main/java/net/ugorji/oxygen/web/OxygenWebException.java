/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

public class OxygenWebException extends RuntimeException {

  public OxygenWebException() {
    super("");
  }

  public OxygenWebException(String msg) {
    super(msg);
  }

  public OxygenWebException(String msg, Throwable thr) {
    super(msg, thr);
  }
}
