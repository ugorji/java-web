/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class helps abstract out the user session. Instead of directly using the HttpSession, we use
 * this WebUserSession, and can persist it into a HttpSession if necessary.
 *
 * @author ugorjid
 */
public class WebUserSession implements Serializable {
  protected boolean captchaChecked = false;
  protected String captchaWord;

  protected Map atts = new HashMap();

  public Object get(String key, boolean removeAfterGet) {
    Object o = (removeAfterGet ? atts.remove(key) : atts.get(key));
    return o;
  }

  public void put(String key, Object value) {
    atts.put(key, value);
  }

  public boolean isCaptchaChecked() {
    return captchaChecked;
  }

  public String getCaptchaWord() {
    return captchaWord;
  }

  public void setCaptchaWord(String captchaWord) {
    this.captchaWord = captchaWord;
  }

  public void checkCaptchaChallenge() throws Exception {
    if (!captchaChecked) {
      WebInteractionContext wctx = WebLocal.getWebInteractionContext();
      String response = wctx.getParameter(WebConstants.CAPTCHA_PARAMETER_KEY);
      if (captchaWord != null && captchaWord.equalsIgnoreCase(response)) {
        captchaChecked = true;
      }
    }
    if (!captchaChecked) {
      throw new Exception("Wrong response entered for captcha challenge");
    }
  }
}
