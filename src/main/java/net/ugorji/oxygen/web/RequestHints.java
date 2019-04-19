/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.util.Properties;
import net.ugorji.oxygen.util.OxygenUtils;

public class RequestHints {
  public boolean handleError = true;
  public boolean logThrowable = true;
  public boolean rethrowError = false;
  public boolean traceRequest = false;

  public RequestHints reset(Properties props) {
    String s = null;
    s = props.getProperty(WebConstants.HANDLE_ERROR_KEY);
    handleError = ((s == null) ? handleError : "true".equals(s));
    s = props.getProperty(WebConstants.LOG_THROWABLE_KEY);
    logThrowable = ((s == null) ? logThrowable : "true".equals(s));
    s = props.getProperty(WebConstants.TRACE_REQUEST_KEY);
    traceRequest = ((s == null) ? traceRequest : "true".equals(s));
    s = props.getProperty(WebConstants.RETHROW_ERROR_KEY);
    rethrowError = ((s == null) ? rethrowError : "true".equals(s));

    return this;
  }

  public RequestHints handleThrowable(Throwable thr) {
    if (handleError && thr != null) {
      if (logThrowable) {
        OxygenUtils.error("[RequestHints]: ", thr);
      } else {
        OxygenUtils.error("[RequestHints]: " + thr);
      }
    }
    return this;
  }
}
