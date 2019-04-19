/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import java.io.Writer;
import net.ugorji.oxygen.markup.MarkupMacroParameters;
import net.ugorji.oxygen.markup.MarkupRenderContext;
import net.ugorji.oxygen.markup.macros.GenericMarkupMacro;

/**
 * Includes the current username Usage: [[username]]
 *
 * @author ugorji
 */
public class UsernameMarkupMacro extends GenericMarkupMacro {

  public void doExecute(Writer writer, MarkupRenderContext rc, MarkupMacroParameters params)
      throws Exception {
    WebInteractionContext req = WebLocal.getWebInteractionContext();
    if (req != null) {
      writer.write(req.getUserName());
    }
  }
}
