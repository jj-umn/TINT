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

package edu.umn.msi.tropix.common.io;

import edu.umn.msi.tropix.common.io.impl.StagingDirectorySupplierImpl;

/**
 * Utility class for producing default instances of {@link StagingDirectoryFactory}.
 * This default factory will produce instances of {@link StagingDirectory} that operate
 * on real directories on the file system this class is running on.
 * 
 * @author John Chilton
 *
 */
public class StagingDirectories {
  private static final StagingDirectoryFactory DEFAULT_FACTORY = new StagingDirectorySupplierImpl();

  public static StagingDirectoryFactory getDefaultFactory() {
    return StagingDirectories.DEFAULT_FACTORY;
  }

}
