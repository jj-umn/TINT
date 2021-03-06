/********************************************************************************
 * Copyright (c) 2009 Regents of the University of Minnesota
 *
 * This Software was written at the Minnesota Supercomputing Institute
 * http://msi.umn.edu
 *
 * All rights reserved. The following statement of license applies
 * only to this file, and and not to the other files distributed with it
 * or derived therefrom.  This file is made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Minnesota Supercomputing Institute - initial API and implementation
 *******************************************************************************/

package edu.umn.msi.tropix.proteomics.itraqquantitation.impl;

import java.io.Writer;
import java.util.Collection;

import edu.umn.msi.tropix.common.io.IOUtils;
import edu.umn.msi.tropix.common.io.IOUtilsFactory;

class ColumnUtils {
  private static final IOUtils IO_UTILS = IOUtilsFactory.getInstance();

  public static void writeColumns(final Writer writer, final Collection<? extends Column> columns, final CharSequence columnSeparator, final CharSequence rowSeparator) {
    if(columns.size() == 0) {
      throw new IllegalArgumentException("Must specify at least one column for writing");
    }
    boolean first = true;
    for(final Column column : columns) {
      if(!first) {
        IO_UTILS.append(writer, columnSeparator);
      }
      first = false;
      IO_UTILS.append(writer, column.getHeader());
    }
    final int numRows = columns.iterator().next().getLength();
    for(int i = 0; i < numRows; i++) {
      IO_UTILS.append(writer, rowSeparator);
      first = true;
      for(final Column column : columns) {
        if(!first) {
          IO_UTILS.append(writer, columnSeparator);
        }
        first = false;
        IO_UTILS.append(writer, column.getValue(i));
      }
    }
  }

}
