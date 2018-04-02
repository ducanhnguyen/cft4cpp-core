package com.fit.testdatagen.htmlreport;

import com.fit.gui.testreport.object.ITestedFunctionReport;

/**
 * Report test report of a function
 *
 * @author DucAnh
 */
public class FuntionTestReportGUI implements ITestReport {

    private int no_K = -1;

    private ICoverage branchCoverage;

    private ICoverage statementCoverage;

    private String note = "";

    private int signal = ITestReport.NORMAL;

    private ITestedFunctionReport testedFunctionReport;

    @Override
    public int getNo_K() {
        return no_K;
    }

    @Override
    public void setNo_K(int no_K) {
        this.no_K = no_K;
    }

    @Override
    public String getFunctionName() {
        return testedFunctionReport.getFunctionNode().getFullName();
    }

    @Override
    public int getMaximumSizeArray() {
        return testedFunctionReport.getFunctionNode().getFunctionConfig().getSizeOfArray();
    }

    @Override
    public String getRangeOfRandomCharacter() {
        return "[" + testedFunctionReport.getFunctionNode().getFunctionConfig().getCharacterBound().getLower() + ","
                + testedFunctionReport.getFunctionNode().getFunctionConfig().getCharacterBound().getUpper() + "]";
    }

    @Override
    public String getRangeOfRandomNumber() {
        return "[" + testedFunctionReport.getFunctionNode().getFunctionConfig().getIntegerBound().getLower() + ","
                + testedFunctionReport.getFunctionNode().getFunctionConfig().getIntegerBound().getUpper() + "]";
    }

    @Override
    public int getMaximumIterationsForEachLoop() {
        return testedFunctionReport.getFunctionNode().getFunctionConfig().getMaximumInterationsForEachLoop();
    }

    @Override
    public ICoverage getBranchCoverage() {
        return branchCoverage;
    }

    @Override
    public void setBranchCoverage(ICoverage coverage) {
        branchCoverage = coverage;
    }

    @Override
    public ICoverage getStatementCoverage() {
        return statementCoverage;
    }

    @Override
    public void setStatementCoverage(ICoverage coverage) {
        statementCoverage = coverage;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int getSignal() {
        return signal;
    }

    @Override
    public void setSignal(int signal) {
        this.signal = signal;
    }

    @Override
    public String getSourceCode() {
        return testedFunctionReport.getFunctionNode().getAST().getRawSignature();
    }

    @Override
    public ITestedFunctionReport getTestedFunctionReport() {
        return testedFunctionReport;
    }

    @Override
    public void setTestedFunctionReport(ITestedFunctionReport tp) {
        testedFunctionReport = tp;
    }

}
