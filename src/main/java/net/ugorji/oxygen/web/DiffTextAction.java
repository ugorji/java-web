package net.ugorji.oxygen.web;

import java.io.Writer;
import net.ugorji.oxygen.util.OxygenRevision;

public class DiffTextAction extends GenericWebAction {
  public int render() throws Exception {
    WebInteractionContext wctx = WebLocal.getWebInteractionContext();
    String text1 = wctx.getParameter("text1");
    String text2 = wctx.getParameter("text2");
    OxygenRevision rev = OxygenRevision.getDiff(text1, text2, WebLocal.getI18n());
    Writer w = wctx.getWriter();
    rev.writeHTML(w);
    w.flush();
    return RENDER_COMPLETED;
  }
}
