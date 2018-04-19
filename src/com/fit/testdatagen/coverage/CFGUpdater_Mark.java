package com.fit.testdatagen.coverage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fit.cfg.BranchInCFG;
import com.fit.cfg.ICFG;
import com.fit.cfg.object.BeginFlagCfgNode;
import com.fit.cfg.object.ConditionCfgNode;
import com.fit.cfg.object.EndFlagCfgNode;
import com.fit.cfg.object.ICfgNode;
import com.fit.cfg.object.ScopeCfgNode;
import com.fit.cfg.testpath.FullTestpath;
import com.fit.cfg.testpath.ITestpathInCFG;
import com.fit.config.Paths;
import com.fit.instrument.FunctionInstrumentationForStatementvsBranch_Marker;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdata.object.Property_Marker;
import com.fit.testdata.object.StatementInTestpath_Mark;
import com.fit.testdata.object.TestpathString_Marker;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

/**
 * Update visited statements in CFG
 *
 * @author ducanhnguyen
 */
public class CFGUpdater_Mark implements ICFGUpdater {
	final static Logger logger = Logger.getLogger(CFGUpdater_Mark.class);

	private TestpathString_Marker testpath = new TestpathString_Marker();

	private List<ICfgNode> previousVisitedNodes = new ArrayList<>();

	private List<BranchInCFG> previousVisitedBranches = new ArrayList<>();

	private List<ICfgNode> visitedStatements = new ArrayList<>();

	private List<BranchInCFG> visitedBranches = new ArrayList<>();

	// A list of visited cfg node by a given test path
	private ITestpathInCFG updatedCFGNodes = new FullTestpath();

	boolean isUncompleteTestpath = false;

	// Control flow graph
	private ICFG cfg;

	public static void main(String[] args) throws Exception {
		logger.setLevel(Level.DEBUG);
		ProjectParser parser = new ProjectParser(new File(Paths.JOURNAL_TEST));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "quickSort(int[],int,int)").get(0);

		FunctionNormalizer fnNorm = function.normalizedAST();
		function.setAST(fnNorm.getNormalizedAST());
		ICFG cfg = function.generateCFG();

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={###line-of-blockin-function=1###openning-function=true",
				"line-in-function=2###offset=68###statement=low < high###control-block=if",
				"statement={###line-of-blockin-function=2",
				"line-in-function=5###offset=153###statement=int pi = partition(arr, low, high);",
				"line-in-function=9###offset=268###statement=int x = pi - 1;",
				"line-in-function=10###offset=287###statement=Algorithm::Sort::quickSort(arr, low, x);###is-recursive=true\"",

				"statement={###line-of-blockin-function=1###openning-function=true",
				"line-in-function=2###offset=68###statement=low < high###control-block=if",

				"statement=}###line-of-blockin-function=1",

				"line-in-function=12###offset=335###statement=int y = pi + 1;",
				"line-in-function=13###offset=354###statement=Algorithm::Sort::quickSort(arr, y, high);###is-recursive=true\"",

				"statement={###line-of-blockin-function=1###openning-function=true",
				"line-in-function=2###offset=68###statement=low < high###control-block=if",

				"statement=}###line-of-blockin-function=1", "statement=}###line-of-blockin-function=1",
				"statement=}###line-of-blockin-function=1"

		};
		testpath.setEncodedTestpath(nodes);
		logger.debug(testpath.getEncodedTestpath());

		// Map
		cfg.setFunctionNode(function);
		cfg.resetVisitedStateOfNodes();
		cfg.setIdforAllNodes();
		// logger.debug("\n" + cfg.toString());
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		updater.updateVisitedNodes();
		logger.debug("visited statements: " + cfg.getVisitedStatements());
		logger.debug("Visited branches: " + cfg.getVisitedBranches());
		logger.debug("Visited nodes: " + updater.getUpdatedCFGNodes());
		logger.debug("Complete test path: " + !updater.isUncompleteTestpath);
	}

	public CFGUpdater_Mark(TestpathString_Marker testpath, ICFG cfg) {
		logger.setLevel(Level.OFF);
		this.testpath = testpath;
		this.cfg = cfg;
	}

	public void storeTheCurrentVisitedStateOfCfg() {
		// Store the currently visited nodes of CFG
		for (ICfgNode node : getCfg().getAllNodes())
			if (node.isVisited())
				previousVisitedNodes.add(node);

		// Store the currently visited branches of CFG
		for (ICfgNode node : getCfg().getAllNodes())
			if (node instanceof ConditionCfgNode) {
				if (((ConditionCfgNode) node).isVisitedFalseBranch())
					previousVisitedBranches.add(new BranchInCFG(node, node.getFalseNode()));

				if (((ConditionCfgNode) node).isVisitedTrueBranch())
					previousVisitedBranches.add(new BranchInCFG(node, node.getTrueNode()));
			}
	}

	@Override
	public void updateVisitedNodes() {
		ArrayList<StatementInTestpath_Mark> detailTestpath = removeRedundantStatements();
		logger.debug("Compared testpath:" + detailTestpath);

		Set<ICfgNode> candidateCfgNodes = new HashSet<>();
		candidateCfgNodes.add(cfg.getBeginNode().getTrueNode());
		ICfgNode previousNode = cfg.getBeginNode();

		traverseGraph(0, detailTestpath, candidateCfgNodes, previousNode, new ArrayList<>(), new ArrayList<>(),
				new Stack<>());

		logger.debug("Visited statements: " + visitedStatements.toString());
		logger.debug("Visited branches: " + visitedBranches.toString());
		for (ICfgNode node : visitedStatements) {
			updatedCFGNodes.getAllCfgNodes().add(node);
			node.setVisit(true);
		}
		for (BranchInCFG branch : visitedBranches) {
			if (branch.getStart().getTrueNode().equals(branch.getEnd()))
				((ConditionCfgNode) branch.getStart()).setVisitedTrueBranch(true);
			else if (branch.getStart().getFalseNode().equals(branch.getEnd()))
				((ConditionCfgNode) branch.getStart()).setVisitedFalseBranch(true);
		}
	}

	private void traverseGraph(int index, ArrayList<StatementInTestpath_Mark> detailTestpath,
			Set<ICfgNode> candidateCfgNodes, ICfgNode previousNode, List<ICfgNode> visitedStatements,
			List<BranchInCFG> visitedBranches, Stack<ICfgNode> recursivePoints) {
		logger.debug("\n\ntraverseGraph");
		logger.debug("Visited statements: " + visitedStatements);
		logger.debug("Visited branches: " + visitedBranches);
		logger.debug("Recursive points: " + recursivePoints);
		logger.debug("index: " + index + " / " + detailTestpath.size());

		if (index == detailTestpath.size()) {
			logger.debug("Reach the end");
			this.visitedBranches = visitedBranches;
			this.visitedStatements = visitedStatements;
		} else {
			StatementInTestpath_Mark testpathItem = detailTestpath.get(index);
			logger.debug("Parse. " + testpathItem);

			// Find candidate nodes
			List<ICfgNode> matchCfgNodes = new ArrayList<>();
			for (ICfgNode item : candidateCfgNodes)
				if (isStronglyConnectedMapping(testpathItem, item))
					matchCfgNodes.add(item);

			if (matchCfgNodes.size() == 0 && recursivePoints.size() > 0) {
				ICfgNode recursivePoint = recursivePoints.pop();
				matchCfgNodes.add(recursivePoint.getFalseNode());
				matchCfgNodes.add(recursivePoint.getTrueNode());
			}
			if (matchCfgNodes.size() == 0)
				logger.debug("Mapping fails");
			else
				for (ICfgNode matchCfgNode : matchCfgNodes) {
					Set<ICfgNode> newCandidateCfgNodes = new HashSet<>();

					Property_Marker isRecursive = testpathItem
							.getPropertyByName(FunctionInstrumentationForStatementvsBranch_Marker.IS_RECURSIVE);
					if (isRecursive != null) {
						recursivePoints.add(matchCfgNode);
						newCandidateCfgNodes.add(cfg.getBeginNode().getTrueNode());
					} else {
						newCandidateCfgNodes.add(matchCfgNode.getTrueNode());
						newCandidateCfgNodes.add(matchCfgNode.getFalseNode());
					}

					ICfgNode newPreviousNode = matchCfgNode;

					List<ICfgNode> newVisitedStatements = new ArrayList<>();
					newVisitedStatements.addAll(visitedStatements);
					visitedStatements.add(matchCfgNode);

					List<BranchInCFG> newVisitedBranches = new ArrayList<>();
					newVisitedBranches.addAll(visitedBranches);
					if (previousNode instanceof ConditionCfgNode)
						newVisitedBranches.add(new BranchInCFG(previousNode, matchCfgNode));

					Stack<ICfgNode> newRecursivePoints = (Stack<ICfgNode>) recursivePoints.clone();
					index++;
					traverseGraph(index, detailTestpath, newCandidateCfgNodes, newPreviousNode, visitedStatements,
							newVisitedBranches, newRecursivePoints);
					index--;
				}
		}
	}

	private ArrayList<StatementInTestpath_Mark> removeRedundantStatements() {
		// Remove all additional nodes in the test path
		// It means that, we only keep related nodes (which occur in source code)
		// However, we only keep additional nodes for For-Loop
		ArrayList<StatementInTestpath_Mark> detailTestpath = testpath.getStandardTestpathByAllProperties();
		for (int i = detailTestpath.size() - 1; i >= 0; i--) {
			StatementInTestpath_Mark currentNode = detailTestpath.get(i);
			if (currentNode
					.getPropertyByName(FunctionInstrumentationForStatementvsBranch_Marker.SOURROUNDING_MARKER) != null
					&& currentNode
							.getPropertyByName(FunctionInstrumentationForStatementvsBranch_Marker.SOURROUNDING_MARKER)
							.getValue().equals("for")) {
				// ignore
			} else

			if (currentNode.getPropertyByName(
					FunctionInstrumentationForStatementvsBranch_Marker.ADDITIONAL_BODY_CONTROL_MARKER) != null
					&& currentNode
							.getPropertyByName(
									FunctionInstrumentationForStatementvsBranch_Marker.ADDITIONAL_BODY_CONTROL_MARKER)
							.getValue().equals("true")) {
				detailTestpath.remove(i);
			}
		}
		return detailTestpath;
	}

	/**
	 * Checking mapping
	 * 
	 * @param propertiesInANode
	 * @param checkedNode
	 * @return
	 */
	protected boolean isStronglyConnectedMapping(StatementInTestpath_Mark propertiesInANode, ICfgNode checkedNode) {
		boolean mappingOK = false;

		Property_Marker stm = propertiesInANode
				.getPropertyByName(FunctionInstrumentationForStatementvsBranch_Marker.STATEMENT);
		Property_Marker line_number_in_function = propertiesInANode
				.getPropertyByName(FunctionInstrumentationForStatementvsBranch_Marker.LINE_NUMBER_IN_FUNCTION);
		Property_Marker surroundingMarker = propertiesInANode
				.getPropertyByName(FunctionInstrumentationForStatementvsBranch_Marker.SOURROUNDING_MARKER);

		int lineOfFunctionInFile = cfg.getFunctionNode().getAST().getFileLocation().getStartingLineNumber();

		if (stm != null && checkedNode.getContent().equals(stm.getValue())) {
			// CASE 1. surrounding node of for control block
			if (surroundingMarker != null && surroundingMarker.getValue().equals("for")) {
				mappingOK = true;
			}
			// CASE 2. normal statement, e.g., x + 1 == 2
			else if (line_number_in_function != null) {
				int lineInFunction = Utils.toInt(line_number_in_function.getValue());

				if (checkedNode != null && checkedNode.getAstLocation() != null
						&& checkedNode.getAstLocation().getStartingLineNumber() - lineOfFunctionInFile
								+ 1 == lineInFunction) {
					mappingOK = true;
				}
			}
			// CASE 3
			else if (stm.getValue().equals(ScopeCfgNode.SCOPE_CLOSE)
					|| stm.getValue().equals(ScopeCfgNode.SCOPE_OPEN)) {
				mappingOK = true;
			}
		}
		return mappingOK;
	}

	@Override
	public String[] getTestpath() {
		String[] output = new String[testpath.getStandardTestpathByAllProperties().size()];
		for (int i = 0; i < output.length - 1; i++)
			output[i] = testpath.getStandardTestpathByAllProperties().get(i).toString();
		return output;
	}

	@Override
	public ICFG getCfg() {
		return cfg;
	}

	@Override
	public void setCfg(ICFG cfg) {
		this.cfg = cfg;
	}

	@Override
	@Deprecated
	public void setTestpath(String[] testpath) {

	}

	@Override
	public ITestpathInCFG getUpdatedCFGNodes() {
		return updatedCFGNodes;
	}

	@Override
	public void setUpdatedCFGNodes(ITestpathInCFG updatedCFGNodes) {
		this.updatedCFGNodes = updatedCFGNodes;
	}

	@Override
	public void unrollChangesOfTheLatestPath() {
		// Reset the visited state of statements
		this.getCfg().resetVisitedStateOfNodes();
		for (ICfgNode node : getCfg().getAllNodes())
			for (ICfgNode visitedNode : previousVisitedNodes) {
				if (visitedNode.equals(node))
					node.setVisit(true);
			}
		// Reset the visited state of branches
		for (ICfgNode node : getCfg().getAllNodes())
			if (node instanceof ConditionCfgNode) {
				((ConditionCfgNode) node).setVisitedFalseBranch(false);
				((ConditionCfgNode) node).setVisitedTrueBranch(false);
			}
		for (ICfgNode node : getCfg().getAllNodes())
			if (node instanceof ConditionCfgNode) {
				for (BranchInCFG branch : previousVisitedBranches)
					if (branch.getStart().equals(node)) {
						if (branch.getEnd().equals(node.getFalseNode()))
							((ConditionCfgNode) node).setVisitedFalseBranch(true);
						else if (branch.getEnd().equals(node.getTrueNode()))
							((ConditionCfgNode) node).setVisitedTrueBranch(true);
					}
			}
	}

	public boolean isUncompleteTestpath() {
		return isUncompleteTestpath;
	}

	public List<BranchInCFG> getPreviousVisitedBranches() {
		return previousVisitedBranches;
	}

	public List<ICfgNode> getPreviousVisitedNodes() {
		return previousVisitedNodes;
	}
}
