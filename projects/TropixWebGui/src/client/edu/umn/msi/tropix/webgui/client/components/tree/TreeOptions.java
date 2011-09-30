/*******************************************************************************
 * Copyright 2009 Regents of the University of Minnesota. All rights
 * reserved.
 * Copyright 2009 Mayo Foundation for Medical Education and Research.
 * All rights reserved.
 *
 * This program is made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED INCLUDING, WITHOUT LIMITATION, ANY WARRANTIES OR CONDITIONS
 * OF TITLE, NON-INFRINGEMENT, MERCHANTABILITY OR FITNESS FOR A
 * PARTICULAR PURPOSE.  See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * Contributors:
 * Minnesota Supercomputing Institute - initial API and implementation
 ******************************************************************************/

package edu.umn.msi.tropix.webgui.client.components.tree;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.SelectionStyle;

import edu.umn.msi.tropix.webgui.client.utils.Iterables;
import edu.umn.msi.tropix.webgui.client.utils.Lists;

public class TreeOptions {
  public static enum SelectionType {
    SINGLE(SelectionStyle.SINGLE), MULTIPlE(SelectionStyle.MULTIPLE);

    private final SelectionStyle style;

    private SelectionType(final SelectionStyle style) {
      this.style = style;
    }

    public SelectionStyle getSmartSelectionStyle() {
      return this.style;
    }

  }

  private SelectionType selectionType = SelectionType.SINGLE;
  private GetTreeState getTreeState;
  private Predicate<TreeItem> showPredicate;
  private Predicate<TreeItem> selectionPredicate;
  private Iterable<String> expandIds = null;
  private Iterable<TreeItem> selectedItems = null;
  private boolean acceptsDrops = true;

  public boolean isAcceptsDrops() {
    return this.acceptsDrops;
  }

  public void setAcceptsDrops(final boolean acceptsDrops) {
    this.acceptsDrops = acceptsDrops;
  }

  public Iterable<TreeItem> getSelectedItems() {
    return this.selectedItems;
  }

  public void setSelectedItems(final Iterable<TreeItem> selectedItems) {
    this.selectedItems = selectedItems;
  }

  public Iterable<String> getExpandIds() {
    return this.expandIds;
  }

  public void setExpandIds(final Iterable<String> expandIds) {
    this.expandIds = expandIds;
  }

  public void getInitialTreeState(final AsyncCallback<List<TreeState>> initialStateCallback) {
    this.getTreeState.getTreeState(initialStateCallback);
  }

  public void setInitialItems(final Iterable<TreeItem> initialItems) {
    final ArrayList<TreeState> rootStates = new ArrayList<TreeState>();
    for(final TreeItem item : initialItems) {
      final TreeState treeState = new TreeState();
      treeState.setTreeItem(item);
      rootStates.add(treeState);
    }
    this.setInitialState(rootStates);
  }

  public void setInitialState(final List<TreeState> treeState) {
    this.setInitialState(new GetTreeState() {
      public void getTreeState(final AsyncCallback<List<TreeState>> treeStateCallback) {
        treeStateCallback.onSuccess(treeState);
      }
    });
  }

  public void setInitialState(final GetTreeState getTreeState) {
    this.getTreeState = getTreeState;
  }

  public SelectionType getSelectionType() {
    return this.selectionType;
  }

  public void setSelectionType(final SelectionType selectionType) {
    this.selectionType = selectionType;
  }

  public Predicate<TreeItem> getSelectionPredicate() {
    return this.selectionPredicate;
  }

  public void setSelectionPredicate(final Predicate<TreeItem> selectionPredicate) {
    this.selectionPredicate = selectionPredicate;
  }

  public Predicate<TreeItem> getShowPredicate() {
    return this.showPredicate;
  }

  public void setShowPredicate(final Predicate<TreeItem> showPredicate) {
    this.showPredicate = showPredicate;
  }

  public void expandAndSelectValidItems(final Iterable<TreeItem> treeItems) {
    final Iterable<TreeItem> selectedItems = Iterables.filter(treeItems, getSelectionPredicate());
    expandAndSelectItems(selectedItems);
  }

  public void expandAndSelectValidItem(final Iterable<TreeItem> treeItems) {
    final TreeItem item = Iterables.find(treeItems, getSelectionPredicate());
    if(item != null) {
      expandAndSelectValidItems(Lists.newArrayList(item));
    }
  }

  private void expandAndSelectItems(final Iterable<TreeItem> selectedItems) {
    setSelectedItems(selectedItems);
    setExpandIds(TreeItems.getAncestorIds(selectedItems));
  }

}
