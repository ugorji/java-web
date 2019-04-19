/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import net.ugorji.oxygen.util.CaptchaTextImageProducer;
import net.ugorji.oxygen.util.CloseUtils;

public class JCaptchaAction extends GenericWebAction {
  private CaptchaTextImageProducer imgProducer;

  public JCaptchaAction() {
    String fontnames = "Arial,Dialog";
    String charRanges = "A-Z";
    try {
      fontnames = WebLocal.getProperties().getProperty("net.ugorji.oxygen.captcha.fontnames");
      charRanges = WebLocal.getProperties().getProperty("net.ugorji.oxygen.captcha.charranges");
    } catch (Exception exc) {
    } // ignore. E.g. if not in a WebLocal environment
    imgProducer = new CaptchaTextImageProducer();
    imgProducer.init(fontnames, charRanges);
  }

  public int render() throws Exception {
    OutputStream os = null;
    try {
      WebInteractionContext wctx = WebLocal.getWebInteractionContext();
      WebUserSession wus = WebLocal.getWebUserSession();
      // ViewContext vctx = WebLocal.getViewContext();

      // flush it in the response
      wctx.setHeader("Cache-Control", "no-store");
      wctx.setHeader("Pragma", "no-cache");
      wctx.setHeader("Expires", "0");
      wctx.setContentType("image/jpeg");

      os = wctx.getOutputStream();
      String word = imgProducer.getNextRandomWord();
      wus.setCaptchaWord(word);

      BufferedImage image = imgProducer.getCaptchaImage(word);
      imgProducer.writeImageAsJPEG(image, os);

      return RENDER_COMPLETED;
    } finally {
      CloseUtils.close(os);
    }
  }
}

/*
public class JCaptchaAction extends GenericWebAction {
  private static ImageCaptchaService captcha = new DefaultManageableImageCaptchaService();
  private static String SESSION_KEY = JCaptchaAction.class.getName() + ".captcha.success";

  public int render() throws Exception {
    OutputStream os = null;
    try {
      WebInteractionContext wctx = WebLocal.getWebInteractionContext();
      ViewContext vctx = WebLocal.getViewContext();

      // the output stream to render the captcha image as jpeg into
      ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
      String captchaId = wctx.getSessionId();
      //System.out.println("captchaId: 1: " + captchaId);
      BufferedImage challenge = captcha.getImageChallengeForID(captchaId, vctx.getLocale());

      JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
      jpegEncoder.encode(challenge);
      byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

      // flush it in the response
      wctx.setHeader("Cache-Control", "no-store");
      wctx.setHeader("Pragma", "no-cache");
      wctx.setHeader("Expires", "0");
      wctx.setContentType("image/jpeg");

      os = wctx.getOutputStream();
      os.write(captchaChallengeAsJpeg);
      os.flush();
      return RENDER_COMPLETED;
    } finally {
      CloseUtils.close(os);
    }
  }

  public static void checkCaptchaChallenge() throws Exception {
    if(isCaptchaAlreadyChecked()) {
      return;
    }
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    String captchaId = wctx.getSessionId();
    //System.out.println("captchaId: 2: " + captchaId);
    Boolean captchaCorrect = captcha.validateResponseForID(captchaId, wctx.getParameter("j_captcha_response"));
    if(captchaCorrect.booleanValue()) {
      wctx.setSessionAttribute(SESSION_KEY, "true");
    } else {
      throw new Exception("Wrong response entered for captcha challenge");
    }
  }

  public static boolean isCaptchaAlreadyChecked() {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    return "true".equals(wctx.getSessionAttribute(SESSION_KEY));
  }
}
*/

/*
  static void checkCaptchaChallenge() throws Exception {
    if(isCaptchaAlreadyChecked()) {
      return;
    }
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    String response = wctx.getParameter("j_captcha_response");
    String word = (String)wctx.getSessionAttribute(SESSION_WORD_KEY);
    if(word != null && word.equalsIgnoreCase(response)) {
      wctx.setSessionAttribute(SESSION_CHECKED_KEY, "true");
    } else {
      throw new Exception("Wrong response entered for captcha challenge");
    }
  }

  static boolean isCaptchaAlreadyChecked() {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    return "true".equals(wctx.getSessionAttribute(SESSION_CHECKED_KEY));
  }

*/
