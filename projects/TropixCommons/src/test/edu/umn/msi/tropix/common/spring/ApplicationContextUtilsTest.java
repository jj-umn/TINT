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

import java.util.List;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

@ContextConfiguration(locations = "classpath:edu/umn/msi/tropix/common/spring/annotationContext.xml")
public class ApplicationContextUtilsTest extends AbstractTestNGSpringContextTests {

  @Test(groups = "unit")
  public void constructor() {
    new ApplicationContextUtils();
  }

  @Test(groups = "unit")
  public void annotation() {
    final Iterable<Object> beansIterable = ApplicationContextUtils.filterBeans(applicationContext, new Predicate<Class<?>>() {
      public boolean apply(final Class<?> classArg) {
        return AnnotationUtils.findAnnotation(classArg, TestAnnotation.class) != null;
      }
    });
    final List<Object> beans = Lists.newArrayList(beansIterable);
    assert beans.size() == 2 : beans.size();
  }
}
