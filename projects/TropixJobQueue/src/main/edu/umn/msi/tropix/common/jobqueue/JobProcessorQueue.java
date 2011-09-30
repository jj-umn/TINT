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

package edu.umn.msi.tropix.common.jobqueue;

import edu.umn.msi.tropix.common.jobqueue.status.Status;
import edu.umn.msi.tropix.common.jobqueue.ticket.Ticket;

/**
 * 
 * @author John Chilton
 * 
 * @param <T>
 *          type of object which describes job to execute.
 */
public interface JobProcessorQueue<T extends JobDescription> {

  <S extends T> Ticket submitJob(JobProcessor<S> jobProcessor, String jobType);

  Status getStatus(Ticket ticket);

  void cancel(Ticket ticket);

  void setJobProcessorPostProcessedListener(JobProcessorPostProcessedListener<T> jobProcessorCompletionListener);

  void fail(Ticket ticket);
  
  void complete(Ticket ticket);
  
  void transferring(Ticket ticket);

}
