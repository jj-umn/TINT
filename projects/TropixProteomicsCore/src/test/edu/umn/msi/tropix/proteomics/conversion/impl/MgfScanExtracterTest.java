package edu.umn.msi.tropix.proteomics.conversion.impl;

import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.commons.io.LineIterator;
import org.testng.annotations.Test;

import com.google.common.collect.Iterables;

import edu.umn.msi.tropix.proteomics.conversion.Scan;

public class MgfScanExtracterTest {

  @Test(groups = "unit")
  public void testReadMsmTitle() {
    final Iterator<String> msmLines = getMsmLines();
    final MgfScanExtracter extracter = new MgfScanExtracter(msmLines, null);
    final Scan scan = Iterables.getOnlyElement(extracter.extractScans());
    assert scan.getPrecursorCharge() == 2 : scan.getPrecursorCharge();
    assert Math.abs(scan.getPrecursorIntensity() - 242947.1f) < 0.001 : scan.getPrecursorIntensity();
    assert scan.getParentFileName().equals("D20090930_PM_K562_SCX-IMAC_fxn03.raw") : scan.getParentFileName();
    assert Math.abs(scan.getPrecursorMz() - 408.31064f) < 0.0001;
    assert scan.getNumber() == 5 : scan.getNumber();
  }

  @Test(groups = "unit")
  public void testMaxQuantMsm() {
    final Iterator<String> msmLines = getMaxQuantLines();
    final MgfScanExtracter extracter = new MgfScanExtracter(msmLines, null);
    final Scan scan = Iterables.getOnlyElement(extracter.extractScans());
    assert Math.abs(scan.getPrecursorMz() - 462.267981002903f) < 0.0001;
    assert scan.getPrecursorCharge() == 2 : scan.getPrecursorCharge();
    assert scan.getParentFileName().equals("tgriffin_bandh003_020409_t2A1_A2.raw") : scan.getParentFileName();
    assert scan.getNumber() == 2 : scan.getNumber();
  }

  @Test(groups = "unit")
  public void testAnotherMgf() {
    final Iterator<String> mgfLines = getResourceLines("another_mgf_section.txt");
    final MgfScanExtracter extracter = new MgfScanExtracter(mgfLines, null);
    final Scan scan = extracter.extractScans().iterator().next();
    assert Math.abs(scan.getPrecursorMz() - 453.737461702821f) < 0.0001;
    assert scan.getParentFileName().equals("tgriffin_dejon039_8742_Apr19_H_BOC.raw") : scan.getParentFileName();
    assert scan.getNumber() == 14 : scan.getNumber();
  }

  private Iterator<String> getMaxQuantLines() {
    return getResourceLines("max_quant_section.txt");
  }

  private Iterator<String> getMsmLines() {
    final String resourceName = "msm_scan_section.txt";
    return getResourceLines(resourceName);
  }

  private Iterator<String> getResourceLines(final String resourceName) {
    @SuppressWarnings("unchecked")
    final Iterator<String> lineIterator =
        new LineIterator(new InputStreamReader(this.getClass().getResourceAsStream(resourceName)));
    return lineIterator;
  }

}
