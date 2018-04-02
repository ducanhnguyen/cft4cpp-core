package com.fit.gui.testreport.object;

import java.io.File;
import java.util.List;

import com.fit.googletest.UnitTestProject;
import com.fit.googletest.xmlparser.IGoogleTestTestcase;
import com.fit.tree.object.IFunctionNode;

/**
 * Represent test report of a project
 *
 * @author ducanhnguyen
 */
public interface IProjectReport extends ITestedReport {

    /**
     * Add a function into the current project report
     *
     * @param function
     */
    void addFunction(IFunctionNode function);

    /**
     * Check whether a function is existed in the current project report or not
     *
     * @param n
     * @return
     */
    boolean contain(IFunctionNode n);

    /**
     * Get test report of a function in the current project report
     *
     * @param n
     * @return
     */
    ITestedFunctionReport getFunction(IFunctionNode n);

    List<SourcecodeFileReport> getSourcecodeFiles();

    void removeAll();

    /**
     * Delete a function
     *
     * @param n
     * @return
     */
    boolean removeFunction(IFunctionNode n);

    /**
     * Export the current project report to excel file
     *
     * @param excelPath
     */
    void exportToExcel(String excelPath);

    /**
     * Update the state of pass/fail for each test case. The state is specified
     * by running google test project
     *
     * @param testCases
     */
    void updateTestcaseStates(List<IGoogleTestTestcase> testCases);

    /**
     * Generate unit test project
     *
     * @param unitTestSourcecodeProjectPath the path of unit test project (where you want to put into)
     * @return
     * @throws Exception
     */
    UnitTestProject generateUnitTest(File unitTestSourcecodeProjectPath) throws Exception;
}