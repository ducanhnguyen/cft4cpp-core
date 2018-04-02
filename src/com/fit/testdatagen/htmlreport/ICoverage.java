package com.fit.testdatagen.htmlreport;

import com.fit.gui.testreport.object.ITestpathReport;

import java.util.List;

public interface ICoverage {
    float MINIMUM_COVERAGE = 0.0f;
    float MAXIMUM_COVERAGE = 1.0f;

    float computeCoverage() throws Exception;

    float getCoverage();

    void setCoverage(float coverage);

    List<ITestpathReport> getTestpaths();

    void setTestpaths(List<ITestpathReport> testpaths);

    void addTestpath(ITestpathReport tp);

    void removeTestpath(ITestpathReport tpReport);

    long getTime();

    void setTime(long time);

    int getNumIterations();

    void setNumIteration(int numIteration);

    int getNumStatic();

    void setNumofSolverCalls(int num);
}