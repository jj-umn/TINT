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

package edu.umn.msi.tropix.jobs.activities.impl;

import edu.umn.msi.tropix.jobs.activities.ActivityContext;
import edu.umn.msi.tropix.jobs.activities.descriptions.ActivityDescription;
import edu.umn.msi.tropix.jobs.activities.descriptions.JobDescription;

public interface WorkflowListener {
  void activityStarted(ActivityContext activityContext, ActivityDescription activityDescritpion);
  
  void activityComplete(ActivityContext activityContext, ActivityDescription activityDescription, boolean finishedProperly);
  
  void workflowComplete(ActivityContext activityContext, boolean finishedProperly);
  
  void jobComplete(ActivityContext activityContext, JobDescription jobDescription, boolean finishedProperly);
  
}
