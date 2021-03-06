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

package edu.umn.msi.tropix.proteomics.service;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import edu.umn.msi.tropix.common.test.FreshConfigTest;

@ContextConfiguration
public class AllServicesSpringTest extends FreshConfigTest {
  
  @Inject
  private ITraqQuantitationJobQueueContext quantService;

  @Inject
  private OmssaJobQueueContext omssaService;
  
  @Inject
  private ProteomicsConvertJobQueueContext proteomicsConvertService;
  
  @Inject
  private RawExtractJobQueueContext rawExtractService;

  @Inject
  private SequestJobQueueContext sequestService;
  
  @Inject
  private XTandemJobQueueContext xTandemJobQueueContext;
  
  @Inject 
  private MyriMatchJobQueueContext myriMatchJobQueueContext;
  
  @Inject
  private IdPickerJobQueueContext idPickerJobQueueContext;
  
  @Inject
  private TagReconJobQueueContext tagReconJobQueueContext;
  
  //@Inject
  private InspectJobQueueContext inspectJobQueueContext;
  
  @Test(groups = "spring")
  public void testLoad() {
    // Tests the spring context loads without exceptions
  }
  
}
