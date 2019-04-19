/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

public interface WebConstants {
  String WEB_BASE_PATH = "net.ugorji.oxygen.web.basepath";
  String THROWABLE_ATTRIBUTE_KEY = "net.ugorji.oxygen.web.throwable";
  String WEB_USERSESSION_KEY = "net.ugorji.oxygen.web.usersession";
  String WEB_APPLICATION_KEY = "net.ugorji.oxygen.web.application";
  // String VIEW_CONTEXT_KEY = "net.ugorji.oxygen.web.viewcontext";
  String HANDLE_ERROR_KEY = "net.ugorji.oxygen.web.handle_error";
  String LOG_THROWABLE_KEY = "net.ugorji.oxygen.web.log_throwable";
  String TRACE_REQUEST_KEY = "net.ugorji.oxygen.web.trace_request";
  String RETHROW_ERROR_KEY = "net.ugorji.oxygen.web.rethrow_error";

  String BASE_URL_KEY = "net.ugorji.oxygen.web.base.url";

  String PROPERTIES_RESOURCE_FILENAME_KEY = "net.ugorji.oxygen.web.properties";
  String DEFAULT_PROPERTIES_RESOURCE_FILENAME = "net.ugorji.oxygen.web.properties";
  String MULTIPART_PREFIX = "multipart/";
  String REDIRECTAFTERPOST_LASTACTIONSTATUS_KEY_HEADER = "oxy-lastactionstatus-key";
  String ACTIONSTATUS_REFERRED_BY = "referred-by";
  String NON_RENDER_PARAMETER_PREFIX = "action.";
  String CAPTCHA_PARAMETER_KEY = "j_captcha_response";
  String SHOW_ERROR_MESSAGE_ONLY_KEY = "net.ugorji.oxygen.web.error.show_message_only";

  String UPLOAD_BASE_URL_KEY = "net.ugorji.oxygen.web.upload.base.url";
}
