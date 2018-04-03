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

	// A list of visited cfg node by a given test path
	private ITestpathInCFG updatedCFGNodes = new FullTestpath();

	boolean isUncompleteTestpath = false;

	// Control flow graph
	private ICFG cfg;

	public static void main(String[] args) {
		ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "add_digits(int)").get(0);

		ICFG cfg = function.generateCFGToFindStaticSolution();

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={###line-of-blockin-function=1###openning-function=true",
				"line-in-function=2###offset=26###statement=static int sum = 0;",
				"line-in-function=4###offset=55###statement=n == 0###control-block=if",
				"line-in-function=7###offset=85###statement=sum = n%10 + add_digits(n/10);###is-recursive=true",
				"statement={###line-of-blockin-function=1###openning-function=true",
				"line-in-function=2###offset=26###statement=static int sum = 0;",
				"line-in-function=4###offset=55###statement=n == 0###control-block=if",
				"line-in-function=7###offset=85###statement=sum = n%10 + add_digits(n/10);###is-recursive=true",
				"statement={###line-of-blockin-function=1###openning-function=true",
				"line-in-function=2###offset=26###statement=static int sum = 0;",
				"line-in-function=4###offset=55###statement=n == 0###control-block=if",
				"statement={###line-of-blockin-function=4", "line-in-function=5###offset=68###statement=return 0;",
				"line-in-function=9###offset=120###statement=return sum;",
				"line-in-function=9###offset=120###statement=return sum;" };
		testpath.setEncodedTestpath(nodes);
		logger.debug(testpath.getEncodedTestpath());

		// Map
		cfg.setFunctionNode(function);
		cfg.resetVisitedStateOfNodes();
		cfg.setIdforAllNodes();
		logger.debug("\n" + cfg.toString());
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

		Stack<ICfgNode> recursivePoints = new Stack<>();
		// Perform mapping
		Set<ICfgNode> candidateCfgNodes = new HashSet<>();
		candidateCfgNodes.add(cfg.getBeginNode().getTrueNode());
		ICfgNode previousNode = cfg.getBeginNode().getTrueNode();

		for (int i = 0; i < detailTestpath.size(); i++) {
			logger.debug("\n");

			StatementInTestpath_Mark testpathItem = detailTestpath.get(i);
			StatementInTestpath_Mark nextTestpathItem = i == detailTestpath.size() - 1 ? null
					: detailTestpath.get(i + 1);
			logger.debug("current candidate CfgNodes: " + candidateCfgNodes);
			logger.debug("testpathItem=" + testpathItem);

			boolean mapOK = false;

			for (ICfgNode candidateCfgNode : candidateCfgNodes) {
				if (checkStrongConnectedMapping(testpathItem, candidateCfgNode, nextTestpathItem)) {
					// A
					candidateCfgNode.setVisit(true);
					updatedCFGNodes.getAllCfgNodes().add(candidateCfgNode);

					if (previousNode instanceof ConditionCfgNode) {
						if (previousNode.getTrueNode().equals(candidateCfgNode))
							((ConditionCfgNode) previousNode).setVisitedTrueBranch(true);
						else if (previousNode.getFalseNode().equals(candidateCfgNode))
							((ConditionCfgNode) previousNode).setVisitedFalseBranch(true);
					}
					// Create new candidate set
					candidateCfgNodes.removeAll(candidateCfgNodes);

					Property_Marker isRecursive = testpathItem
							.getPropertyByName(FunctionInstrumentationForStatementvsBranch_Marker.IS_RECURSIVE);
					if (isRecursive != null) {
						candidateCfgNodes.add(cfg.getBeginNode().getTrueNode());
						recursivePoints.add(candidateCfgNode);
					} else {
						candidateCfgNodes.add(candidateCfgNode.getTrueNode());
						candidateCfgNodes.add(candidateCfgNode.getFalseNode());
					}
					previousNode = candidateCfgNode;

					logger.debug("new candidate CfgNodes: " + candidateCfgNodes);
					logger.debug("Recursive points in stack: " + recursivePoints);
					mapOK = true;
					break;
					// END-A
				}
			}

			// May be due to recursive
			if (!mapOK && recursivePoints.size() > 0) {
				ICfgNode latestRecursivePoint = recursivePoints.pop();
				candidateCfgNodes.removeAll(candidateCfgNodes);
				candidateCfgNodes.add(latestRecursivePoint.getFalseNode());
				candidateCfgNodes.add(latestRecursivePoint.getTrueNode());

				for (ICfgNode candidateCfgNode : candidateCfgNodes)
					if (checkStrongConnectedMapping(testpathItem, candidateCfgNode, nextTestpathItem)) {
						// CLONE A
						candidateCfgNode.setVisit(true);
						updatedCFGNodes.getAllCfgNodes().add(candidateCfgNode);

						if (previousNode instanceof ConditionCfgNode) {
							if (previousNode.getTrueNode().equals(candidateCfgNode))
								((ConditionCfgNode) previousNode).setVisitedTrueBranch(true);
							else if (previousNode.getFalseNode().equals(candidateCfgNode))
								((ConditionCfgNode) previousNode).setVisitedFalseBranch(true);
						}
						// Create new candidate set
						candidateCfgNodes.removeAll(candidateCfgNodes);

						Property_Marker isRecursive = testpathItem
								.getPropertyByName(FunctionInstrumentationForStatementvsBranch_Marker.IS_RECURSIVE);
						if (isRecursive != null) {
							candidateCfgNodes.add(cfg.getBeginNode().getTrueNode());
							recursivePoints.add(candidateCfgNode);
						} else {
							candidateCfgNodes.add(candidateCfgNode.getTrueNode());
							candidateCfgNodes.add(candidateCfgNode.getFalseNode());
						}
						previousNode = candidateCfgNode;

						logger.debug("new candidate CfgNodes: " + candidateCfgNodes);
						logger.debug("Recursive points in stack: " + recursivePoints);
						mapOK = true;
						break;
						// END CLONE A
					}
			}
			if (!mapOK) {
				System.out.println("Mapping fails!" + testpathItem);
				break;
			}
		}
		// Detect the completetion of test path
		ICfgNode theLastNode = updatedCFGNodes.getAllCfgNodes().get(updatedCFGNodes.getAllCfgNodes().size() - 1);
		if (theLastNode instanceof EndFlagCfgNode || theLastNode.getTrueNode() instanceof EndFlagCfgNode
				|| theLastNode.getFalseNode() instanceof EndFlagCfgNode)
			isUncompleteTestpath = false;
		else
			isUncompleteTestpath = true;
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
	protected boolean checkStrongConnectedMapping(StatementInTestpath_Mark propertiesInANode, ICfgNode checkedNode,
			StatementInTestpath_Mark nextTestpathItem) {
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
			else if (stm.getValue().equals(ScopeCfgNode.SCOPE_CLOSE)) {
				mappingOK = true;
			}
			// CASE 4
			else if (stm.getValue().equals(ScopeCfgNode.SCOPE_OPEN)) {
				if (checkedNode.getParent() instanceof BeginFlagCfgNode) {
					mappingOK = true;

				} else if (nextTestpathItem != null) {
					Property_Marker nextStm = nextTestpathItem
							.getPropertyByName(FunctionInstrumentationForStatementvsBranch_Marker.STATEMENT);
					if (nextStm != null
							&& (checkStrongConnectedMapping(nextTestpathItem, checkedNode.getTrueNode(), null)
									|| checkStrongConnectedMapping(nextTestpathItem, checkedNode.getFalseNode(), null)))

						mappingOK = true;
				}
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
