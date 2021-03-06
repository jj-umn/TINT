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

package edu.umn.msi.tropix.webgui.services.object;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.umn.msi.tropix.models.Folder;
import edu.umn.msi.tropix.models.TropixObject;
import edu.umn.msi.tropix.models.VirtualFolder;
import edu.umn.msi.tropix.models.utils.TropixObjectContext;
import edu.umn.msi.tropix.models.utils.TropixObjectType;

public interface FolderServiceAsync {
  void getFolderContents(String folderId, TropixObjectType[] types, AsyncCallback<List<TropixObject>> callback);

  void createFolder(String parentFolderId, Folder folder, AsyncCallback<Void> callback);

  void createVirtualFolder(String parentFolderId, VirtualFolder folder, AsyncCallback<Void> callback);

  void getGroupFolders(AsyncCallback<List<Folder>> callback);

  void getAllGroupFolders(AsyncCallback<List<Folder>> callback);

  void createGroupFolder(Folder folder, String ownerId, AsyncCallback<Folder> callback);

  void addUserToGroupFolder(String objectId, String userId, AsyncCallback<Void> callback);

  void addGroupToGroupFolder(String objectId, String groupId, AsyncCallback<Void> callback);

  void getMySharedFolders(AsyncCallback<List<TropixObjectContext<VirtualFolder>>> callback);

  void getGroupSharedFolders(String groupId, AsyncCallback<List<TropixObjectContext<VirtualFolder>>> callback);

  void createGroupVirtualFolder(String groupId, VirtualFolder folder, AsyncCallback<Void> callback);
}
