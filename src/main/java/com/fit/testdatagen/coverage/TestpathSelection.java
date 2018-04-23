package com.fit.testdatagen.coverage;

import com.fit.gui.testreport.object.ITestpathReport;
import com.fit.gui.testreport.object.TestedFunctionReport;

import java.util.ArrayList;
import java.util.List;

public class TestpathSelection {

    private TestedFunctionReport fnReport;

    public TestpathSelection() {
    }

    public void removeUnnecessaryTestpaths(TestedFunctionReport fnReport) throws Exception {
        increase(fnReport);
        decrease(fnReport);
    }

    private void decrease(TestedFunctionReport fnReport) throws Exception {
        List<ITestpathReport> tmp = new ArrayList<>();

        List<ITestpathReport> tps = fnReport.getTestpaths();

        for (int i = tps.size() - 1; i >= 0; i--) {
            ITestpathReport tp = tps.get(i);

            float oldCoverage = fnReport.computeCoverage();
            fnReport.getTestpaths().remove(i);
            fnReport.computeCoverage();
            float newCoverage = fnReport.computeCoverage();

            if (oldCoverage == newCoverage)
                continue;
            else if (oldCoverage > newCoverage)
                tmp.add(tp);
        }
        /*
		 *
		 */
        fnReport.getTestpaths().addAll(tmp);
        fnReport.computeCoverage();
    }

    private void increase(TestedFunctionReport fnReport) throws Exception {
        List<ITestpathReport> tmp = new ArrayList<>();

        List<ITestpathReport> tps = fnReport.getTestpaths();

        for (int i = 0; i < tps.size(); i++) {
            ITestpathReport tp = tps.get(i);

            float oldCoverage = fnReport.computeCoverage();
            fnReport.getTestpaths().remove(i);
            fnReport.computeCoverage();
            float newCoverage = fnReport.computeCoverage();

            if (oldCoverage == newCoverage)
                continue;
            else if (oldCoverage > newCoverage)
                tmp.add(tp);
        }
		/*
		 *
		 */
        fnReport.getTestpaths().addAll(tmp);
        fnReport.computeCoverage();
    }

    public TestedFunctionReport getFnReport() {
        return fnReport;
    }

    public void setFnReport(TestedFunctionReport fnReport) {
        this.fnReport = fnReport;
    }
}
