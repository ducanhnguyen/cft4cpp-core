package com.fit.testdatagen.htmlreport;

import com.fit.gui.testreport.object.ITestedFunctionReport;

public interface ITestReport {

    public static final int ERROR = -1;
    public static final int NORMAL = 0;
    public static final int GOOD = 1;

    int getNo_K();

    void setNo_K(int no_K);

    String getFunctionName();

    int getMaximumSizeArray();

    String getRangeOfRandomCharacter();

    String getRangeOfRandomNumber();

    int getMaximumIterationsForEachLoop();

    ICoverage getBranchCoverage();

    public void setBranchCoverage(ICoverage coverage);

    ICoverage getStatementCoverage();

    public void setStatementCoverage(ICoverage coverage);

    String getNote();

    void setNote(String note);

    int getSignal();

    void setSignal(int signal);

    String getSourceCode();

    ITestedFunctionReport getTestedFunctionReport();

    void setTestedFunctionReport(ITestedFunctionReport tp);
}
