package com.fit.gui.testreport.object;

import com.fit.gui.testedfunctions.ExpectedOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent output report
 *
 * @author DucAnh
 */
public class OutputReport implements IOutputReport {
    /**
     * Represent expected output
     */
    protected ExpectedOutputData expectedOuput = new ExpectedOutputData();

    /**
     * Represent calls in the current test path
     */
    protected List<InternalExternalCallReport> calls = new ArrayList<>();

    public OutputReport() {
    }

    public OutputReport(String returnValue, InternalExternalCallReport... calls) {
        for (InternalExternalCallReport call : calls)
            this.calls.add(call);
    }

    protected OutputReport(String returnValue, List<InternalExternalCallReport> calls) {
        this.calls = calls;
    }

    @Override
    public List<InternalExternalCallReport> getCalls() {
        return calls;
    }

    @Override
    public ExpectedOutputData getExpectedValues() {
        return expectedOuput;
    }

    @Override
    public void setExpectedValues(ExpectedOutputData expectedOuput) {
        this.expectedOuput = expectedOuput;
    }

}
