/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

public class WebResourceNotFoundException extends OxygenWebException {
  public WebResourceNotFoundException(String msg) {
    super(msg);
  }

  public WebResourceNotFoundException(String msg, Throwable thr) {
    super(msg, thr);
  }
}
