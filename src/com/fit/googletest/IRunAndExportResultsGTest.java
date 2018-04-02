package com.fit.googletest;

import java.util.List;

import com.fit.googletest.xmlparser.IGoogleTestTestcase;
import com.fit.gui.testreport.object.IProjectReport;

/**
 * Create makefile => create file .exe => parser .exe to .xml
 * <p>
 * => Complete TestCases => fill "pass/fail"
 *
 * @author DucToan
 */
public interface IRunAndExportResultsGTest {
    public final int FOR_FILE_CPP = 0;

    public final int FOR_AUTO = 1;

    /**
     * Run and export results
     *
     * @throws Exception
     */
    void exportResults() throws Exception;

    /**
     * Set environment before running
     *
     * @param projectReport
     * @param unitTestProject
     * @throws Exception
     */
    void setEnvironment(IProjectReport projectReport, UnitTestProject unitTestProject) throws Exception;

    List<IGoogleTestTestcase> getTestcases();

}