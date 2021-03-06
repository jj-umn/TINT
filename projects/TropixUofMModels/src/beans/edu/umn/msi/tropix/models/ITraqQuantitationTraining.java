package edu.umn.msi.tropix.models;

import java.io.Serializable;
import java.util.Collection;

/**
	* 	**/
public class ITraqQuantitationTraining extends TropixObject implements Serializable {
  /**
   * An attribute to allow serialization of the domain objects
   */
  private static final long serialVersionUID = 1234567890L;

  /**
   * An associated edu.umn.msi.tropix.models.ProteomicsRun object's collection
   **/

  private Collection<ProteomicsRun> runs;

  /**
   * Retreives the value of runs attribue
   * 
   * @return runs
   **/

  public Collection<ProteomicsRun> getRuns() {
    return runs;
  }

  /**
   * Sets the value of runs attribue
   **/

  public void setRuns(final Collection<ProteomicsRun> runs) {
    this.runs = runs;
  }

  /**
   * An associated edu.umn.msi.tropix.models.TropixFile object
   **/

  private TropixFile report;

  /**
   * Retreives the value of report attribue
   * 
   * @return report
   **/

  public TropixFile getReport() {
    return report;
  }

  /**
   * Sets the value of report attribue
   **/

  public void setReport(final TropixFile report) {
    this.report = report;
  }

  /**
   * An associated edu.umn.msi.tropix.models.TropixFile object
   **/

  private TropixFile trainingFile;

  /**
   * Retreives the value of trainingFile attribue
   * 
   * @return trainingFile
   **/

  public TropixFile getTrainingFile() {
    return trainingFile;
  }

  /**
   * Sets the value of trainingFile attribue
   **/

  public void setTrainingFile(final TropixFile trainingFile) {
    this.trainingFile = trainingFile;
  }

  /**
   * Compares <code>obj</code> to it self and returns true if they both are same
   * 
   * @param obj
   **/
  @Override
  public boolean equals(final Object obj) {
    if(obj instanceof ITraqQuantitationTraining) {
      final ITraqQuantitationTraining c = (ITraqQuantitationTraining) obj;
      if(getId() != null && getId().equals(c.getId())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns hash code for the primary key of the object
   **/
  @Override
  public int hashCode() {
    if(getId() != null) {
      return getId().hashCode();
    }
    return 0;
  }

}