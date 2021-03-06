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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Wraps a {@link DataOutputStream} to provide methods for writing {@code double}s
 * and {@code float}s without dealing with checked exceptions.
 * 
 * @author John Chilton
 *
 */
public class FloatingPointDataOutputStream {
  private DataOutputStream dos = null;

  public FloatingPointDataOutputStream(final OutputStream outputStream) {
    this.dos = new DataOutputStream(outputStream);
  }

  public void writeFloat(final float floatToWrite) {
    try {
      this.dos.writeFloat(floatToWrite);
    } catch(final IOException e) {
      throw new IORuntimeException(e);
    }
  }

  public void writeDouble(final double doubleToWrite) {
    try {
      this.dos.writeDouble(doubleToWrite);
    } catch(final IOException e) {
      throw new IORuntimeException(e);
    }
  }

}
