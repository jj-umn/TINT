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

import java.io.File;

/**
 * 
 * Implementations of DisposableResource are responsible for executing the dispose 
 * method at some point after the object is no longer "strongly reachable".
 * 
 * http://java.sun.com/j2se/1.5.0/docs/api/java/lang/ref/package-summary.html#reachability
 * 
 * @author John Chilton (chilton at msi dot umn dot edu)
 * 
 */
public interface DisposableResource {
  /**
   * Returns a File representation of this resource. The file should be deleted when delete is called.
   * 
   * @return The file wrapped by this resource.
   */
  File getFile();

  /**
   * Disposes of the resource, including freeing memory, deleting files, etc. This method may be called 
   * several times.
   */
  void dispose();

}
