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

package edu.umn.msi.tropix.webgui.client.components.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.Command;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

import edu.umn.msi.tropix.models.locations.Location;
import edu.umn.msi.tropix.models.locations.Locations;
import edu.umn.msi.tropix.webgui.client.AsyncCallbackImpl;
import edu.umn.msi.tropix.webgui.client.components.DescribableLocationCommandComponentFactory;
import edu.umn.msi.tropix.webgui.client.components.tree.TreeItem;
import edu.umn.msi.tropix.webgui.client.components.tree.TropixObjectTreeItem;
import edu.umn.msi.tropix.webgui.client.mediators.LocationUpdateMediator;
import edu.umn.msi.tropix.webgui.client.mediators.LocationUpdateMediator.UpdateEvent;
import edu.umn.msi.tropix.webgui.services.object.ObjectService;

public class DeleteCommandComponentFactoryImpl implements DescribableLocationCommandComponentFactory<Command> {

  public boolean acceptsLocations(final Collection<TreeItem> treeItems) {
    System.out.println("In acceptsLocations");
    if(!(treeItems != null && treeItems.size() > 0
        && Locations.allTropixObjectTreeItemsWithSameRoot(treeItems)
        && Locations.allParentsAreFolder(treeItems))) {
      return false;
    }
    final TreeItem firstItem = treeItems.iterator().next();
    final Location rootItem = firstItem.getRoot();

    // If some items are root shared folders and not others don't allow delete...
    if(Locations.isMySharedFoldersItem(rootItem) || Locations.isMyGroupSharedFoldersItem(rootItem)) {
      for(final TreeItem treeItem : treeItems) {
        if(Locations.isMySharedFoldersItem(treeItem.getParent()) != Locations.isMySharedFoldersItem(firstItem.getParent())) {
          return false;
        }
        if(Locations.isMyGroupSharedFoldersItem(treeItem.getParent()) != Locations.isMyGroupSharedFoldersItem(firstItem.getParent())) {
          return false;
        }

      }
    }

    // Don't let you delete from searches, etc...
    boolean deleteableType = firstItem instanceof TropixObjectTreeItem
        && (rootItem instanceof TropixObjectTreeItem || Locations.isRootLocationAFolder(rootItem) || Locations.isMyRecentActivityItem(rootItem));
    if(!deleteableType) {
      return false;
    }
    return Locations.allTropixObjectLocationsAreModifiable(treeItems);
  }

  public Command get(final Collection<TreeItem> treeItems) {
    return new Command() {
      public void execute() {
        final TreeItem firstItem = treeItems.iterator().next();
        final boolean isVirtual = Locations.isRootASharedRootMetaLocation(firstItem);
        final boolean isVirtualRoot = Locations.isSharedFolderRoot(firstItem);
        final Collection<String> ids = new ArrayList<String>(treeItems.size());
        for(final TreeItem treeItem : treeItems) {
          ids.add(treeItem.getId());
        }

        if(isVirtualRoot) {
          SC.ask("Are you sure you wish to remove the selected shared folders from your view.", new BooleanCallback() {
            public void execute(final Boolean value) {
              if(!value) {
                return;
              }
              ObjectService.Util.getInstance().ownedItemsInVirtualHierarchiesCount(ids, new AsyncCallbackImpl<Long>() {
                private void remove(final boolean removeOwnedItems) {
                  for(final TreeItem treeItem : treeItems) {
                    ObjectService.Util.getInstance().removeSharedFolder(treeItem.getId(), removeOwnedItems, new AsyncCallbackImpl<Void>() {
                      @Override
                      public void onSuccess(final Void result) {
                        LocationUpdateMediator.getInstance().onEvent(new UpdateEvent(treeItem.getParent().getId(), null));
                      }
                    });
                  }
                }

                @Override
                public void onSuccess(final Long objectCount) {
                  if(objectCount == 0) {
                    remove(false);
                  } else {
                    SC.ask("The selected shared folder(s) contain objects owned by you, do you wish to remove these from the shared folder?",
                        new BooleanCallback() {
                          public void execute(final Boolean value) {
                            remove(value);
                          }
                        });
                  }
                }
              });
            }
          });
        } else if(isVirtual) {
          SC.ask("Are you sure you wish to remove the selected object(s) from their shared folders.", new BooleanCallback() {
            public void execute(final Boolean value) {
              for(final TreeItem treeItem : treeItems) {
                final AsyncCallbackImpl<Void> callback = new AsyncCallbackImpl<Void>() {
                  @Override
                  public void onSuccess(final Void result) {
                    LocationUpdateMediator.getInstance().onEvent(new UpdateEvent(treeItem.getParent().getId(), null));
                  }
                };
                if(value) {
                  ObjectService.Util.getInstance().removeFromSharedFolder(treeItem.getId(), treeItem.getParent().getId(), callback);
                }
              }
            }
          });
        } else {
          SC.ask("Are you sure you wish to delete the selected object(s).", new BooleanCallback() {
            public void execute(final Boolean value) {
              for(final String id : ids) {
                final AsyncCallbackImpl<Void> callback = new AsyncCallbackImpl<Void>() {
                  @Override
                  public void onSuccess(final Void result) {
                    LocationUpdateMediator.getInstance().onEvent(new LocationUpdateMediator.RemoveUpdateEvent(id, null));
                  }
                };
                if(value) {
                  ObjectService.Util.getInstance().delete(id, null, callback);
                }
              }
            }
          });
        }
      }
    };
  }

  public String getDescription() {
    return "Delete";
  }
}