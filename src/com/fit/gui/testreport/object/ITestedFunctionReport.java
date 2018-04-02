package com.fit.gui.testreport.object;

import com.fit.testdatagen.htmlreport.ICoverage;
import com.fit.tree.object.IFunctionNode;

import java.util.List;

/**
 * Represent tested function report
 *
 * @author ducanhnguyen
 */
public interface ITestedFunctionReport extends ITestedReport {
	/**
	 * Generate unit test for function
	 *
	 * @return
	 */
	String generateUnitTest();

	/**
	 * Check whether the latest test path exists in the list of test path or not
	 *
	 * @return true if it is added before. The current test path will be removed
	 *         from the list of test path
	 */
	boolean checkLatestTestpath();

	String getCoverageCritertion();

	float computeCoverage() throws Exception;

	void addTestpath(ITestpathReport tpReport);

	void removeTestpath(ITestpathReport tpReport);

	IFunctionNode getFunctionNode();

	void setFunctionNode(IFunctionNode functionNode);

	String getName();

	int getNumTestcase();

	String getState();

	void setState(String state);

	int getTotalPossibleTestpath();

	void setTotalPossibleTestpath(int totalPossibleTestpath);

	ICoverage getCoverage();

	void setCoverage(ICoverage coverage);

	List<ITestpathReport> getTestpaths();

	String getStrategy();

	public void setStrategy(String strategy);

	long getTime();

	void setTime(long time);

	boolean canBeExporttoUnitTest();

	float getCurrentCoverage();

	void setCurrentCoverage(float coverage);
}