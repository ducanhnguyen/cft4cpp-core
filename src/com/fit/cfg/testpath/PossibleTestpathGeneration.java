package com.fit.cfg.testpath;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.fit.cfg.CFGGenerationSubCondition;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.cfg.object.AbstractConditionLoopCfgNode;
import com.fit.cfg.object.ConditionCfgNode;
import com.fit.cfg.object.EndFlagCfgNode;
import com.fit.cfg.object.ICfgNode;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.config.Settingv2;
import com.fit.constraints.checker.RelatedConstraintsChecker;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.se.ISymbolicExecution;
import com.fit.testdatagen.se.Parameter;
import com.fit.testdatagen.se.PathConstraint;
import com.fit.testdatagen.se.SymbolicExecution;
import com.fit.testdatagen.se.solver.ISmtLibGeneration;
import com.fit.testdatagen.se.solver.RunZ3OnCMD;
import com.fit.testdatagen.se.solver.SmtLibGeneration;
import com.fit.testdatagen.se.solver.Z3SolutionParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.IVariableNode;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import com.ibm.icu.util.Calendar;

/**
 * Generate all possible test paths
 *
 * @author DucAnh
 */
public class PossibleTestpathGeneration implements ITestpathGeneration {
	public static final String CONSTRAINTS_FILE = Settingv2.getValue(ISettingv2.SMT_LIB_FILE_PATH);
	public static final String Z3 = Settingv2.getValue(ISettingv2.SOLVER_Z3_PATH);
	final static Logger logger = Logger.getLogger(PossibleTestpathGeneration.class);
	/**
	 * Represent control flow graph
	 */
	private ICFG cfg;
	private int maxIterationsforEachLoop = ITestpathGeneration.DEFAULT_MAX_ITERATIONS_FOR_EACH_LOOP;
	private FullTestpaths possibleTestpaths = new FullTestpaths();
	private long solvingTime = 0;// ms
	private long totalRunningTime = 0;// ms
	private long numberOfSolvingCalls = 0;// ms
	private long numberOfSolvingCallsThatNoSolution = 0;// ms

	public PossibleTestpathGeneration(ICFG cfg) {
		this.cfg = cfg;
	}

	public PossibleTestpathGeneration(ICFG cfg, int maxloop) {
		maxIterationsforEachLoop = maxloop;
		this.cfg = cfg;

		this.cfg.resetVisitedStateOfNodes();
		this.cfg.setIdforAllNodes();
	}

	/**
	 * @param cfg
	 * @param maxloop
	 * @param isResetVisitedState
	 *            true if the visit stated is marked unvisited
	 */
	public PossibleTestpathGeneration(ICFG cfg, int maxloop, boolean isResetVisitedState) {
		maxIterationsforEachLoop = maxloop;
		this.cfg = cfg;

		if (isResetVisitedState) {
			this.cfg.resetVisitedStateOfNodes();
			this.cfg.setIdforAllNodes();
		}
	}

	public static void main(String[] args) throws Exception {
		ProjectParser parser = new ProjectParser(new File(Paths.JOURNAL_TEST));

		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "mergeSort(int[],int,int)").get(0);
		System.out.println(function.getAST().getRawSignature());

		CFGGenerationSubCondition cfgGen = new CFGGenerationSubCondition(function,
				ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
		ICFG cfg = cfgGen.generateCFG();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		cfg.resetVisitedStateOfNodes();

		int maxIterations = 1;
		PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfg, maxIterations);
		tpGen.generateTestpaths();

		System.out.println("Num of test paths: " + tpGen.getPossibleTestpaths().size());
	}

	@Override
	public void generateTestpaths() {
		// Date startTime = Calendar.getInstance().getTime();
		FullTestpaths testpaths_ = new FullTestpaths();

		ICfgNode beginNode = cfg.getBeginNode();
		FullTestpath initialTestpath = new FullTestpath();
		initialTestpath.setFunctionNode(cfg.getFunctionNode());
		try {
			traverseCFG(beginNode, initialTestpath, testpaths_);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (ITestpathInCFG tp : testpaths_)
			tp.setFunctionNode(cfg.getFunctionNode());

		possibleTestpaths = testpaths_;

		// Calculate the running time
		// Date end = Calendar.getInstance().getTime();
		// totalRunningTime = end.getTime() - startTime.getTime();
		// logger.debug("Total running time: " + totalRunningTime + " ms");
		// logger.debug("Solving time: " + solvingTime + " ms");
		// logger.debug("Number of solving calls: " + numberOfSolvingCalls + " ms");
		// logger.debug(
		// "Number of solving calls that does not have solution: " +
		// numberOfSolvingCallsThatNoSolution + " ms");
	}

	private void traverseCFG(ICfgNode stm, FullTestpath tp, FullTestpaths testpaths) throws Exception {
		tp.add(stm);
		if (stm instanceof EndFlagCfgNode) {
			testpaths.add((FullTestpath) tp.clone());
			tp.remove(tp.size() - 1);
		} else {
			ICfgNode trueNode = stm.getTrueNode();
			ICfgNode falseNode = stm.getFalseNode();

			if (stm instanceof ConditionCfgNode)

				if (stm instanceof AbstractConditionLoopCfgNode) {

					int currentIterations = tp.count(trueNode);
					if (currentIterations < maxIterationsforEachLoop) {

						traverseCFG(falseNode, tp, testpaths);
						traverseCFG(trueNode, tp, testpaths);
					} else
						traverseCFG(falseNode, tp, testpaths);
				} else {
					traverseCFG(falseNode, tp, testpaths);
					traverseCFG(trueNode, tp, testpaths);
				}
			else
				traverseCFG(trueNode, tp, testpaths);

			tp.remove(tp.size() - 1);
		}
	}

	protected boolean haveSolution(FullTestpath tp, boolean finalConditionType) throws Exception {
		IPartialTestpath tp1 = createPartialTestpath(tp, finalConditionType);

		Date startTime = Calendar.getInstance().getTime();
		String solution = solveTestpath(cfg.getFunctionNode(), tp1);
		// Calculate the running time
		Date end = Calendar.getInstance().getTime();
		solvingTime += end.getTime() - startTime.getTime();

		if (!solution.equals(IStaticSolutionGeneration.NO_SOLUTION))
			return true;
		else {
			numberOfSolvingCallsThatNoSolution++;
			return false;
		}
	}

	protected IPartialTestpath createPartialTestpath(FullTestpath fullTp, boolean finalConditionType) {
		IPartialTestpath partialTp = new PartialTestpath();
		for (ICfgNode node : fullTp.getAllCfgNodes())
			partialTp.getAllCfgNodes().add(node);

		partialTp.setFinalConditionType(finalConditionType);
		return partialTp;
	}

	protected String solveTestpath(IFunctionNode function, ITestpathInCFG testpath) throws Exception {
		/*
		 * Get the passing variables of the given function
		 */
		Parameter paramaters = new Parameter();
		for (IVariableNode n : function.getArguments())
			paramaters.add(n);
		for (IVariableNode n : function.getReducedExternalVariables())
			paramaters.add(n);

		/*
		 * Get the corresponding path constraints of the test path
		 */
		ISymbolicExecution se = new SymbolicExecution(testpath, paramaters, function);

		// fast checking
		RelatedConstraintsChecker relatedConstraintsChecker = new RelatedConstraintsChecker(
				se.getConstraints().getNormalConstraints(), function);
		boolean isRelated = relatedConstraintsChecker.check();
		//
		if (isRelated) {
			if (se.getConstraints().getNormalConstraints().size()
					+ se.getConstraints().getNullorNotNullConstraints().size() > 0) {
				/*
				 * Solve the path constraints
				 */
				ISmtLibGeneration smtLibGen = new SmtLibGeneration(function.getPassingVariables(),
						se.getConstraints().getNormalConstraints());
				smtLibGen.generate();

				Utils.writeContentToFile(smtLibGen.getSmtLibContent(), CONSTRAINTS_FILE);

				RunZ3OnCMD z3 = new RunZ3OnCMD(Z3, CONSTRAINTS_FILE);
				z3.execute();
				logger.debug("solving done");
				numberOfSolvingCalls++;
				String staticSolution = new Z3SolutionParser().getSolution(z3.getSolution());

				if (staticSolution.equals(IStaticSolutionGeneration.NO_SOLUTION)) {
					return IStaticSolutionGeneration.NO_SOLUTION;
				} else {
					if (se.getConstraints().getNullorNotNullConstraints().size() > 0)
						for (PathConstraint nullConstraint : se.getConstraints().getNullorNotNullConstraints())
							staticSolution += nullConstraint + SpecialCharacter.END_OF_STATEMENT;

					if (se.getConstraints().getNullorNotNullConstraints().size() > 0)
						return staticSolution + "; " + se.getConstraints().getNullorNotNullConstraints();
					else
						return staticSolution + ";";
				}
			} else
				return IStaticSolutionGeneration.NO_SOLUTION;
		} else
			return IStaticSolutionGeneration.EVERY_SOLUTION;
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
	public int getMaxIterationsforEachLoop() {
		return maxIterationsforEachLoop;
	}

	@Override
	public void setMaxIterationsforEachLoop(int maxIterationsforEachLoop) {
		this.maxIterationsforEachLoop = maxIterationsforEachLoop;
	}

	@Override
	public FullTestpaths getPossibleTestpaths() {
		return possibleTestpaths;
	}
}
