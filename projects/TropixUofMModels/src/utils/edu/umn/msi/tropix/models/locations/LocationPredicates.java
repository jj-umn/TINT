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

package edu.umn.msi.tropix.models.locations;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;

import edu.umn.msi.tropix.models.Database;
import edu.umn.msi.tropix.models.FileType;
import edu.umn.msi.tropix.models.Folder;
import edu.umn.msi.tropix.models.IdentificationParameters;
import edu.umn.msi.tropix.models.TropixFile;
import edu.umn.msi.tropix.models.TropixObject;
import edu.umn.msi.tropix.models.VirtualFolder;
import edu.umn.msi.tropix.models.utils.TropixObjectTreeItemPredicate;
import edu.umn.msi.tropix.models.utils.TropixObjectType;
import edu.umn.msi.tropix.models.utils.TropixObjectTypeEnum;

public class LocationPredicates {
  private static final List<TropixObjectType> DESTINATION_TYPES = Arrays.<TropixObjectType>asList(TropixObjectTypeEnum.FOLDER, TropixObjectTypeEnum.VIRTUAL_FOLDER, TropixObjectTypeEnum.REQUEST);
  static final Predicate<Location> NOT_FOLDER_PREDICATE = new NotFolderPredicate();
  static final Predicate<Location> TROPIX_OBJECT_TREE_ITEM_PREDICATE = new TropixObjectTreeItemPredicate();
  static final Predicate<Location> FOLDER_PREDICATE = new FolderPredicate();

  private static class NotFolderPredicate extends TropixObjectTreeItemPredicate {
    @Override
    public boolean apply(final TropixObject object) {
      return !(object instanceof Folder || object instanceof VirtualFolder);
    }
  }

  private static class FolderPredicate extends TropixObjectTreeItemPredicate {
    @Override
    public boolean apply(final TropixObject object) {
      return (object instanceof Folder || object instanceof VirtualFolder);
    }
  }
  
  public static Predicate<Location> getTropixFileTreeItemPredicate(final String extension, final boolean otherObjects) {
    return new TropixObjectTreeItemPredicate(otherObjects) {
      @Override
      public boolean apply(final TropixObject tropixObject) {
        if(tropixObject instanceof TropixFile) {
          final TropixFile tropixFile = (TropixFile) tropixObject;
          final FileType fileType = tropixFile.getFileType();
          return fileType != null && fileType.getExtension().equals(extension);
        } else {
          return otherObjects;
        }
      }
    };
  }

  public static Predicate<Location> getIdentificationParametersOfTypePredicate(final String type, final boolean notTropixObjectDefault) {
    return new TropixObjectTreeItemPredicate() {
      @Override
      public boolean apply(final TropixObject tropixObject) {
        return tropixObject instanceof IdentificationParameters && type.equals(((IdentificationParameters) tropixObject).getType());
      }
    };
  }
  
  public static Predicate<Location> getDestinationsPredicate(final boolean notTropixObjectDefault) {
    return getTropixObjectTreeItemTypePredicate(DESTINATION_TYPES, notTropixObjectDefault);
  }

  
  public static Predicate<Location> getTropixObjectTreeItemTypePredicate(final Iterable<TropixObjectType> types, final boolean notTropixObjectDefault) {
    return LocationPredicates.getTropixObjectTreeItemPredicate(new Predicate<TropixObject>() {
      public boolean apply(final TropixObject object) {
        boolean isInstance = false;
        for(final TropixObjectType type : types) {
          if(type.isInstance(object)) {
            isInstance = true;
            break;
          }
        }
        return isInstance;
      }
    }, false);
  }

  public static Predicate<Location> getTropixObjectTreeItemTypePredicate(final TropixObjectType[] types, final boolean notTropixObjectDefault) {
    return LocationPredicates.getTropixObjectTreeItemTypePredicate(Arrays.asList(types), notTropixObjectDefault);
  }

  public static Predicate<Location> getTropixObjectTreeItemTypePredicate(final TropixObjectType type, final boolean notTropixObjectDefault) {
    return LocationPredicates.getTropixObjectTreeItemTypePredicate(Arrays.asList(type), false);
  }

  public static Predicate<Location> getTropixObjectTreeItemPredicate(final Predicate<TropixObject> predicate, final boolean notTropixObjectDefault) {
    return new Predicate<Location>() {
      public boolean apply(final Location treeItem) {
        if(!TROPIX_OBJECT_TREE_ITEM_PREDICATE.apply(treeItem)) {
          return false;
        } else {
          return predicate.apply(((TropixObjectLocation) treeItem).getObject());
        }
      }
    };
  }

  public static Predicate<Location> showDatabaseTypePredicate(final String format) {
    return new Predicate<Location>() {

      public boolean apply(final Location treeItem) {
        boolean show = true;
        if(TROPIX_OBJECT_TREE_ITEM_PREDICATE.apply(treeItem)) {
          final TropixObjectLocation toTreeItem = (TropixObjectLocation) treeItem;
          final TropixObject tropixObject = toTreeItem.getObject();
          
          if(tropixObject instanceof Database) {
            final Database database = (Database) tropixObject;
            show = database.getType().equals(format);
          } else if(!(tropixObject instanceof Folder || tropixObject instanceof VirtualFolder)) {
            show = false;
          }
        }
        return show;
      }

    };
  }

  public static Predicate<Location> selectDatabaseTypePredicate(final String format) {
    return new Predicate<Location>() {

      public boolean apply(final Location treeItem) {
        if(TROPIX_OBJECT_TREE_ITEM_PREDICATE.apply(treeItem)) {
          final TropixObjectLocation toTreeItem = (TropixObjectLocation) treeItem;
          final TropixObject tropixObject = toTreeItem.getObject();
          if(tropixObject instanceof Database) {
            final Database database = (Database) tropixObject;
            return database.getType().equals(format);
          }
        }
        return false;
      }

    };
  }

  public static Predicate<Location> getIsTropixObjectTreeItemPredicate() {
    return LocationPredicates.TROPIX_OBJECT_TREE_ITEM_PREDICATE;
  }

  public static Predicate<Location> getFolderPredicate() {
    return LocationPredicates.FOLDER_PREDICATE;
  }

  public static Predicate<Location> getTropixObjectNotFolderPredicate() {
    return LocationPredicates.NOT_FOLDER_PREDICATE;
  }

}
