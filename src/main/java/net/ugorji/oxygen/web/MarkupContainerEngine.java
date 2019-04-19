/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.web;

import net.ugorji.oxygen.markup.MarkupParserFactory;

public abstract class MarkupContainerEngine extends WebContainerEngine {

  protected MarkupParserFactory markupParserFactory;

  public MarkupParserFactory getMarkupParserFactory() {
    return markupParserFactory;
  }
}
