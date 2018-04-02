package com.fit.testdatagen.htmlreport;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fit.cfg.CFGGenerationSubCondition;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.gui.testreport.object.ITestpathReport;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.testdata.object.TestpathString_Marker;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.Utils;

/**
 * @author DucAnh
 */
public class SubConditionCoverage extends Coverage {
	final static Logger logger = Logger.getLogger(SubConditionCoverage.class);

	public SubConditionCoverage(IFunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	public SubConditionCoverage(float coverage, List<ITestpathReport> testpaths, IFunctionNode functionNode) {
		super(coverage, testpaths, functionNode);
	}

	public SubConditionCoverage(float coverage, List<ITestpathReport> testpaths, IFunctionNode functionNode, long time,
			int numStatic) {
		super(coverage, testpaths, functionNode, time, numStatic);
	}

	@Override
	public float computeCoverage() throws Exception {
		float coverageNow = 0;

		logger.debug("\n");
		logger.debug("Computing sub-condition coverage reaching by " + getTestpaths().size() + " test paths");

		FunctionNormalizer fnNorm = functionNode.normalizedAST();
		String normalizedCoverage = fnNorm.getNormalizedSourcecode();
		IFunctionNode clone = (IFunctionNode) functionNode.clone();
		clone.setAST(Utils.getFunctionsinAST(normalizedCoverage.toCharArray()).get(0));

		ICFG cfg = new CFGGenerationSubCondition(clone, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES).generateCFG();
		cfg.setIdforAllNodes();
		cfg.resetVisitedStateOfNodes();
		cfg.setFunctionNode(clone);
		
		List<ITestpathReport> tpReportClone = new ArrayList<>(getTestpaths());
		for (ITestpathReport testpathReport : tpReportClone) {
			TestpathString_Marker testpathMarker = testpathReport.getExecutionTestpath();
			cfg.updateVisitedNodes_Marker(testpathMarker);
		}
		coverageNow = cfg.computeBranchCoverage() * 100;

		logger.debug("Done. 1.Subcondition coverage = " + coverageNow);
		logger.debug("2.Unvisited sub-branches (size=" + cfg.getUnvisitedBranches().size() + "): "
				+ cfg.getUnvisitedBranches().toString());
		logger.debug("3.Unvisited sub-statements (size=" + cfg.getUnvisitedStatements().size() + "): "
				+ cfg.getUnvisitedStatements().toString() + "\n");
		return coverageNow;
	}
}
