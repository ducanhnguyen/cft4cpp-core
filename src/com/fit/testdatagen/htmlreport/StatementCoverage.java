package com.fit.testdatagen.htmlreport;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.gui.testreport.object.ITestpathReport;
import com.fit.testdata.object.TestpathString_Marker;
import com.fit.tree.object.IFunctionNode;

/**
 * @author DucAnh
 */
public class StatementCoverage extends Coverage {
	final static Logger logger = Logger.getLogger(StatementCoverage.class);

	public StatementCoverage(float coverage, List<ITestpathReport> testpaths, IFunctionNode functionNode) {
		super(coverage, testpaths, functionNode);
	}

	public StatementCoverage(float coverage, List<ITestpathReport> testpaths, IFunctionNode functionNode, long time,
			int numStatic) {
		super(coverage, testpaths, functionNode, time, numStatic);
	}

	public StatementCoverage(IFunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	@Override
	public float computeCoverage() throws Exception {
		float coverageNow = 0;
		/**
		 * Create testpath in string
		 */
		logger.debug("\n");
		logger.debug("Computing statement coverage reaching by " + getTestpaths().size() + " test paths");

		// Initialize CFG
		ICFG cfg = new CFGGenerationforBranchvsStatementCoverage(functionNode, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES).generateCFG();
		cfg.setIdforAllNodes();
		cfg.resetVisitedStateOfNodes();
		cfg.setFunctionNode(functionNode);

		// Compute coverage
		List<ITestpathReport> tpReportClone = new ArrayList<>(getTestpaths());
		for (ITestpathReport testpathReport : tpReportClone) {
			TestpathString_Marker testpathMarker = testpathReport.getExecutionTestpath();
			cfg.updateVisitedNodes_Marker(testpathMarker);
		}
		coverageNow = cfg.computeStatementCoverage() * 100;

		logger.debug("Done. 1.Statement coverage = " + coverageNow);
		logger.debug("2.Unvisited branches (size=" + cfg.getUnvisitedBranches().size() + "): "
				+ cfg.getUnvisitedBranches().toString());
		logger.debug("3.Unvisited statements (size=" + cfg.getUnvisitedStatements().size() + "): "
				+ cfg.getUnvisitedStatements().toString() + "\n");

		return coverageNow;
	}
}
