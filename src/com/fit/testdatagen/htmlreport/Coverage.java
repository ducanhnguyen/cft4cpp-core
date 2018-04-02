package com.fit.testdatagen.htmlreport;

import com.fit.gui.testreport.object.ITestpathReport;
import com.fit.tree.object.IFunctionNode;

import java.util.ArrayList;
import java.util.List;

public abstract class Coverage implements ICoverage {
    protected long time;// ms

    protected int numIteration;

    protected float coverage = -1;

    protected List<ITestpathReport> testpaths = new ArrayList<>();

    protected IFunctionNode functionNode = null;

    protected int numStatic = 0;

    public Coverage() {
    }

    public Coverage(float coverage, List<ITestpathReport> testpaths, IFunctionNode functionNode) {
        this.coverage = coverage;
        this.testpaths = testpaths;
        this.functionNode = functionNode;
    }

    public Coverage(float coverage, List<ITestpathReport> testpaths, IFunctionNode functionNode, long time,
                    int numStatic) {
        this.coverage = coverage;
        this.testpaths = testpaths;
        this.functionNode = functionNode;
        this.time = time;
        this.numStatic = numStatic;
    }

    @Override
    public abstract float computeCoverage() throws Exception;

    @Override
    public float getCoverage() {
        return coverage;
    }

    @Override
    public void setCoverage(float coverage) {
        this.coverage = coverage;
    }

    @Override
    public List<ITestpathReport> getTestpaths() {
        return testpaths;
    }

    @Override
    public void setTestpaths(List<ITestpathReport> testpaths) {
        this.testpaths = testpaths;
    }

    @Override
    public void addTestpath(ITestpathReport tp) {
        testpaths.add(tp);
    }

    @Override
    public void removeTestpath(ITestpathReport tpReport) {
        testpaths.remove(tpReport);
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int getNumIterations() {
        return numIteration;
    }

    @Override
    public void setNumIteration(int numIteration) {
        this.numIteration = numIteration;
    }

    @Override
    public int getNumStatic() {
        return numStatic;
    }

    @Override
    public void setNumofSolverCalls(int num) {
        numStatic = num;
    }

}
