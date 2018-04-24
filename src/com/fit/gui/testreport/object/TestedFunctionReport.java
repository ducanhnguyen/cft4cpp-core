package com.fit.gui.testreport.object;

import com.fit.config.IFunctionConfig;
import com.fit.gui.testedfunctions.ManageSelectedFunctionsDisplayer;
import com.fit.testdatagen.htmlreport.ICoverage;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.SpecialCharacter;

import java.util.List;

/**
 * Represent function report
 *
 * @author DucAnh
 */
public class TestedFunctionReport implements ITestedFunctionReport {

	protected IFunctionNode functionNode;
	protected ICoverage coverage;
	protected String strategy;
	private int totalPossibleTestpath = -1;
	private String state = "-";
	private float currentCoverage = 0.0f;

	public TestedFunctionReport(IFunctionNode function) {
		functionNode = function;
	}

	public TestedFunctionReport(String name, String state) {
		this.state = state;
	}

	@Override
	public String generateUnitTest() {
		String functiontest = "";
		/**
		 * Build source google test for each test path
		 */
		String functionTest = "";
		int idTestpath = 1;
		for (ITestpathReport testpath : getTestpaths())
			if (testpath.isCanBeExportToUnitTest()) {
				String nameTestpath = "testpath" + idTestpath++;
				functiontest += testpath.generateUnitTest(nameTestpath);
			}
		functiontest += functionTest + SpecialCharacter.LINE_BREAK;
		return functiontest;

	}

	@Override
	public boolean checkLatestTestpath() {
		boolean isExist = false;

		if (coverage.getTestpaths().size() > 1) {
			int latestIndex = coverage.getTestpaths().size() - 1;
			ITestpathReport latestTp = coverage.getTestpaths().get(latestIndex);
			if (latestIndex >= 1)
				for (int i = latestIndex - 1; i >= 0; i--)
					if (coverage.getTestpaths().get(i).equals(latestTp)) {
						isExist = true;
						coverage.getTestpaths().remove(i);
						break;
					}
		}
		return isExist;
	}

	@Override
	public String getCoverageCritertion() {
		switch (getFunctionNode().getFunctionConfig().getTypeofCoverage()) {
		case IFunctionConfig.BRANCH_COVERAGE:
			return "branch coverage";

		case IFunctionConfig.STATEMENT_COVERAGE:
			return "statement coverage";

		case IFunctionConfig.SUBCONDITION:
			return "sub-condition coverage";

		default:
			return "unknown coverage";
		}
	}

	@Override
	public float computeCoverage() throws Exception {
		return coverage.computeCoverage();
	}

	@Override
	public void addTestpath(ITestpathReport tpReport) {
		coverage.addTestpath(tpReport);
	}

	@Override
	public void removeTestpath(ITestpathReport tpReport) {
		coverage.removeTestpath(tpReport);
	}

	@Override
	public IFunctionNode getFunctionNode() {
		return functionNode;
	}

	@Override
	public void setFunctionNode(IFunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	@Override
	public String getName() {
		return functionNode.getNewType();
	}

	@Override
	public int getNumTestcase() {
		return coverage.getTestpaths().size();
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public void setState(String state) {
		this.state = state;
		ManageSelectedFunctionsDisplayer.getInstance().refresh();
	}

	@Override
	public int getTotalPossibleTestpath() {
		return totalPossibleTestpath;
	}

	@Override
	public void setTotalPossibleTestpath(int totalPossibleTestpath) {
		this.totalPossibleTestpath = totalPossibleTestpath;
	}

	@Override
	public ICoverage getCoverage() {
		return coverage;
	}

	@Override
	public void setCoverage(ICoverage coverage) {
		this.coverage = coverage;
	}

	@Override
	public List<ITestpathReport> getTestpaths() {
		return getCoverage().getTestpaths();
	}

	@Override
	public String getStrategy() {
		return strategy;
	}

	@Override
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	@Override
	public long getTime() {
		return coverage.getTime();
	}

	@Override
	public void setTime(long time) {
		coverage.setTime(time);
	}

	@Override
	public boolean canBeExporttoUnitTest() {
		for (ITestpathReport tp : getTestpaths())
			if (tp.isCanBeExportToUnitTest())
				return true;
		return false;
	}

	@Override
	public float getCurrentCoverage() {
		return currentCoverage;
	}

	@Override
	public void setCurrentCoverage(float coverage) {
		this.currentCoverage = coverage;
	}
}
