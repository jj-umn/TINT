package edu.umn.msi.tropix.proteomics.itraqquantitation.impl;

interface ReportEntry {

  public int getScanNumber();

  public short getScanCharge();

  public String getProteinAccession();

  public double getProteinProbability();

  public String getPeptideSequence();

  public double getPeptideProbability();

}