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

package edu.umn.msi.tropix.client.credential.impl;

import org.easymock.EasyMock;
import org.testng.annotations.Test;

import edu.umn.msi.tropix.client.authentication.LocalUserManager;
import edu.umn.msi.tropix.client.credential.InvalidUsernameOrPasswordException;
import edu.umn.msi.tropix.grid.credentials.Credential;

public class LocalGlobusCredentialProviderImplTest {

  @Test(groups = "unit")
  public void getGlobusCredential() {
    final LocalUserManager passwordFunction = EasyMock.createMock(LocalUserManager.class);
    final LocalGlobusCredentialProviderImpl provider = new LocalGlobusCredentialProviderImpl();
    provider.setLocalUserManager(passwordFunction);
    
    EasyMock.expect(passwordFunction.isUsersPassword("John", "Chilton")).andReturn(true);
    EasyMock.expect(passwordFunction.isUsersPassword("John", "chiltonx")).andReturn(false);
    EasyMock.replay(passwordFunction);
    
    Credential credential = provider.getGlobusCredential("John", "Chilton", null);
    assert credential != null;
    InvalidUsernameOrPasswordException oe = null;
    try {
      provider.getGlobusCredential("John", "chiltonx", null);
    } catch(final InvalidUsernameOrPasswordException ie) {
      oe = ie;
    }
    assert oe != null;
  }
  
}
