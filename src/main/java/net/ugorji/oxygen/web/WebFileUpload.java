/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import net.ugorji.oxygen.util.OxygenUtils;
import net.ugorji.oxygen.util.StringUtils;

/**
 * THis class helps us abstract out our use of Jakarta File Upload. make this class package private,
 * so only folks in here can see it
 */
class WebFileUpload {
  private List items;
  private File[] files2;
  private Hashtable params2;
  private static Random rand = new Random();

  public WebFileUpload(WebInteractionContext req, long maxUploadSize, String queryString)
      throws Exception {
    WFUHandler wfuh = new WFUHandler();
    wfuh.setSizeMax(maxUploadSize);
    items = wfuh.parseRequest(new WFURequestContext(req));
    Map params = new HashMap();
    File tmpdir = getTmpDir();
    List files = new ArrayList();
    for (Iterator itr = items.iterator(); itr.hasNext(); ) {
      FileItem item = (FileItem) itr.next();
      String s = null;
      if (item.isFormField()) {
        if ((s = item.getFieldName()) != null) {
          List l = (List) params.get(s);
          if (l == null) {
            l = new ArrayList();
            params.put(s, l);
          }
          l.add(item.getString());
        }
      } else {
        s = item.getName();
        if (!StringUtils.isBlank(s)) {
          // do this, since sometimes, the browser puts path information, and linux/unix consider \
          // to be part of the name
          s = s.replace('\\', File.separatorChar);
          File f = new File(s);
          f = new File(tmpdir, f.getName());
          if (!(files.contains(f))) {
            item.write(f);
            files.add(f);
          }
        }
      }
    }

    if (!StringUtils.isBlank(queryString)) {
      Hashtable qstable = HttpUtils.parseQueryString(queryString);
      for (Iterator itr = qstable.entrySet().iterator(); itr.hasNext(); ) {
        Map.Entry me = (Map.Entry) itr.next();
        String s = (String) me.getKey();
        String[] qsParams = (String[]) me.getValue();
        List l = (List) params.get(s);
        if (l == null) {
          l = new ArrayList();
          params.put(s, l);
        }
        l.addAll(0, Arrays.asList(qsParams));
        // System.out.println(" >>>> qsParams: " + l);
      }
    }

    params2 = new Hashtable();
    for (Iterator itr = params.entrySet().iterator(); itr.hasNext(); ) {
      Map.Entry me = (Map.Entry) itr.next();
      String s = (String) me.getKey();
      List l = (List) me.getValue();
      // System.out.println(" >>>> l: " + l);
      String[] sa = (String[]) l.toArray(new String[0]);
      params2.put(s, sa);
    }

    files2 = (File[]) files.toArray(new File[0]);
  }

  public File[] getFiles() {
    return files2;
  }

  public String[] getParameterValues(String s) {
    return (String[]) params2.get(s);
  }

  public String getParameter(String s) {
    String[] sa = getParameterValues(s);
    if (sa == null) {
      return null;
    } else {
      return sa[0];
    }
  }

  public Map getParameterMap() {
    return params2;
  }

  public Enumeration getParameterNames() {
    return params2.keys();
  }

  private static File getTmpDir() throws Exception {
    File tmpdir = new File(System.getProperty("java.io.tmpdir").replace('\\', '/'));
    tmpdir = new File(tmpdir, System.currentTimeMillis() + "-" + rand.nextInt(100000));
    OxygenUtils.mkdirs(tmpdir);
    return tmpdir;
  }

  private static class WFUHandler extends FileUpload {
    public WFUHandler() {
      this(new DiskFileItemFactory());
    }

    public WFUHandler(FileItemFactory fileItemFactory) {
      super(fileItemFactory);
    }
  }

  private static class WFURequestContext implements RequestContext {
    private WebInteractionContext req;

    private WFURequestContext(WebInteractionContext req0) {
      req = req0;
    }

    public String getCharacterEncoding() {
      return null;
    }

    public String getContentType() {
      return req.getHeader("content-type");
    }

    public int getContentLength() {
      return req.getContentLength();
    }

    public InputStream getInputStream() throws IOException {
      return req.getInputStream();
    }
  }
}

    /* We have to override this, 'cos there's a bug in commons-fileupload-1.1,
     * which causes all attachments to have lowercase filenames
     */
    /*
    protected String getFileName(Map headers) {
      String fileName = null;
      String cd = getHeader(headers, CONTENT_DISPOSITION);
      if (cd != null) {
        String cd22 = cd.toLowerCase();
        if (cd22.startsWith(FORM_DATA) || cd22.startsWith(ATTACHMENT)) {
          ParameterParser parser = new ParameterParser();
          parser.setLowerCaseNames(true);
          Map params = parser.parse(cd, ';');
          if (params.containsKey("filename")) {
            fileName = (String) params.get("filename");
            if (fileName != null) {
              fileName = fileName.trim();
            } else {
              fileName = "";
            }
          }
        }
      }
      return fileName;
    }
    */
