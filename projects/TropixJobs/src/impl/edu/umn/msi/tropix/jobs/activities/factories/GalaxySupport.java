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

package edu.umn.msi.tropix.jobs.activities.factories;

import java.util.List;

import edu.umn.msi.tropix.galaxy.tool.Tool;
import edu.umn.msi.tropix.galaxy.inputs.RootInput;
import edu.umn.msi.tropix.grid.credentials.Credential;
import edu.umn.msi.tropix.transfer.types.TransferResource;

interface GalaxySupport {

  class GalaxyInputFile {
    private final String fileName;
    private final TransferResource transferResource;
    
    GalaxyInputFile(final String fileName, final TransferResource transferResource) {
      this.fileName = fileName;
      this.transferResource = transferResource;
    }

    public String getFileName() {
      return fileName;
    }

    public TransferResource getTransferResource() {
      return transferResource;
    }
    
  }

  List<GalaxyInputFile> prepareInputFiles(Tool tool, RootInput rootInput, Credential credential);
  
  
}
