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

package edu.umn.msi.tropix.webgui.client.utils;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;

public class ListenerLists {

  public static <T> ListenerList<T> getInstance() {
    return new ListenerListImpl<T>();
  }

  private static class ListenerListImpl<T> extends ArrayList<Listener<T>> implements ListenerList<T> {
    private static final long serialVersionUID = 1L;

    public void onEvent(final T event) {
      for(final Listener<T> listener : this) {
        try {
          listener.onEvent(event);
        } catch(final Throwable e) {
          GWT.getUncaughtExceptionHandler().onUncaughtException(e);
        }
      }
    }
  }

}
