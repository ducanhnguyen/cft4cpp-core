package com.fit.gui.testreport.object;

import com.fit.gui.testedfunctions.ExpectedOutputData;

import java.util.List;

/**
 * Represent output report
 *
 * @author ducanhnguyen
 */
public interface IOutputReport extends ITestedReport {
    List<InternalExternalCallReport> getCalls();

    ExpectedOutputData getExpectedValues();

    void setExpectedValues(ExpectedOutputData expectedOuput);
}