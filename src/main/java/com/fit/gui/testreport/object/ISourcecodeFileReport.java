package com.fit.gui.testreport.object;

import com.fit.tree.object.ISourcecodeFileNode;

import java.io.File;
import java.util.List;

/**
 * Represent a source code file report
 *
 * @author DucAnh
 */
public interface ISourcecodeFileReport extends ITestedReport {
    /**
     * Add a test function report into the source code file report
     *
     * @param functionReport
     */
    void addTestFunctionReport(ITestedFunctionReport functionReport);

    /**
     * Get all tested function reports in the current source code file report
     *
     * @return
     */
    List<ITestedFunctionReport> getTestedFunctionReports();

    /**
     * Set test function reports
     *
     * @param testedFunctionReports
     */
    void setTestedFunctionReports(List<ITestedFunctionReport> testedFunctionReports);

    /**
     * Get name of the current source code file report
     *
     * @return
     */
    String getName();

    /**
     * Set name of the tested function reports
     *
     * @param name
     */
    void setName(String name);

    /**
     * Get source code file node
     *
     * @return
     */
    ISourcecodeFileNode getSourcecodeFileNode();

    /**
     * Set source code file node
     *
     * @param sourcecodeFileNode
     */
    void setSourcecodeFileNode(ISourcecodeFileNode sourcecodeFileNode);

    /**
     * Generate unit test for a source code file
     *
     * @param originalTestedProject
     * @param cloneTestedProject
     * @return
     */
    String generateUnitTest(File originalTestedProject, File cloneTestedProject);

    boolean canBeExportToUnitTest();
}