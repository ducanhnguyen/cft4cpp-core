package com.fit.testdatagen.htmlreport;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fit.cfg.ICFG;
import com.fit.gui.testreport.object.ITestpathReport;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.testdata.object.TestpathString_Marker;
import com.fit.tree.object.IFunctionNode;

/**
 * @author DucAnh
 */
public class BranchCoverage extends Coverage {
	final static Logger logger = Logger.getLogger(BranchCoverage.class);

	public BranchCoverage(IFunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	public BranchCoverage(float coverage, List<ITestpathReport> testpaths, IFunctionNode functionNode) {
		super(coverage, testpaths, functionNode);
	}

	public BranchCoverage(float coverage, List<ITestpathReport> testpaths, IFunctionNode functionNode, long time,
			int numStatic) {
		super(coverage, testpaths, functionNode, time, numStatic);
	}

	@Override
	public float computeCoverage() throws Exception {
		float coverageNow = 0;
		/**
		 * Create testpath in string
		 */
		logger.debug("\n");
		logger.debug("Computing branch coverage reaching by " + getTestpaths().size() + " test paths");

		// Initialize CFG
		IFunctionNode clone = (IFunctionNode) functionNode.clone();
		FunctionNormalizer fnNorm = clone.normalizedAST();
		clone.setAST(fnNorm.getNormalizedAST());
		ICFG cfg = clone.generateCFG();

		cfg.setIdforAllNodes();
		cfg.resetVisitedStateOfNodes();
		cfg.setFunctionNode(clone);

		// Compute coverage
		List<ITestpathReport> tpReportClone = new ArrayList<>(getTestpaths());
		for (ITestpathReport testpathReport : tpReportClone) {
			TestpathString_Marker testpathMarker = testpathReport.getExecutionTestpath();
			cfg.updateVisitedNodes_Marker(testpathMarker);
		}
		coverageNow = cfg.computeBranchCoverage() * 100;

		logger.debug("Done. 1.Branch coverage = " + coverageNow);
		logger.debug("2.Unvisited branches (size=" + cfg.getUnvisitedBranches().size() + "): "
				+ cfg.getUnvisitedBranches().toString());
		logger.debug("3.Unvisited statements (size=" + cfg.getUnvisitedStatements().size() + "): "
				+ cfg.getUnvisitedStatements().toString() + "\n");

		return coverageNow;
	}
}
