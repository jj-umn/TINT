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

import java.io.Reader;

import com.google.common.base.Supplier;

import edu.umn.msi.tropix.common.io.impl.LineProcessorSupplierImpl;

/**
 * Utility method for obtain a supplier of {@link LineProcessor} objects that
 * operate on {@link Reader}.
 * 
 * @author John Chilton
 *
 */
public class LineProcessors {
  private static final Supplier<LineProcessor<Reader>> DEFAULT_SUPPLIER = new LineProcessorSupplierImpl();
  
  // Extra level of indirection provided by having this return a Supplier might be too convoluted.
  public static Supplier<LineProcessor<Reader>> getDefaultSupplier() {
    return LineProcessors.DEFAULT_SUPPLIER;
  }
}
