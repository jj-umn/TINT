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

package edu.umn.msi.tropix.common.spring;

import java.lang.annotation.Annotation;

import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import com.google.common.base.Predicate;

public abstract class AnnotatedBeanProcessor<T extends Annotation> extends TypedBeanProcessor implements ApplicationContextAware {
  private final Class<T> annotationClass;

  protected AnnotatedBeanProcessor(final Class<T> annotationClass) {
    super(new Predicate<Class<?>>() {
      public boolean apply(final Class<?> arg) {
        return AnnotationUtils.findAnnotation(arg, annotationClass) != null;
      }
    });
    this.annotationClass = annotationClass;
  }

  protected T getAnnotation(final Object object) {
    return AnnotationUtils.findAnnotation(object.getClass(), annotationClass);
  }

}
