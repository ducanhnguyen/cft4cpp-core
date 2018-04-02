package com.fit.testdatagen.loop;

import java.io.File;

import org.apache.log4j.Logger;

import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.cfg.object.AbstractConditionLoopCfgNode;
import com.fit.cfg.object.EndFlagCfgNode;
import com.fit.cfg.object.ICfgNode;
import com.fit.cfg.testpath.PartialTestpath;
import com.fit.cfg.testpath.PartialTestpaths;
import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.se.solver.IZ3SolutionParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

/**
 * Created by ducanhnguyen on 5/8/2017.
 */
@Deprecated
public class PossibleTestpathGenerationForLoop2 extends AbstractPossibleTestpathGenerationForLoop {
    final static Logger logger = Logger.getLogger(PossibleTestpathGenerationForLoop.class);

    public PossibleTestpathGenerationForLoop2(ICFG cfg, AbstractConditionLoopCfgNode loopCondition) {
        this.cfg = cfg;
        this.cfg.resetVisitedStateOfNodes();
        this.loopCondition = loopCondition;
    }

    public static void main(String[] args) throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "Merge1(int[3],int[3],int[6])").get(0);
        logger.debug(function.getAST().getRawSignature());

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedStateOfNodes();

        // Generate test path for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("i < 3");
        PossibleTestpathGenerationForLoop2 tpGen = new PossibleTestpathGenerationForLoop2(cfg, loopCondition);
        tpGen.setMaximumIterationsForOtherLoops(4);
        tpGen.setIterationForUnboundedTestingLoop(10);
        tpGen.setAddTheEndTestingCondition(true);
        tpGen.generateTestpaths();

        logger.debug("num test path = " + tpGen.getPossibleTestpaths().size());
    }

    @Override
    protected void traverseCFG(ICfgNode stm, PartialTestpath tp, PartialTestpaths testpaths,
                               boolean isJustOverTheTestingLoop) throws Exception {

        if (!isJustOverTheTestingLoop && !(stm instanceof EndFlagCfgNode)) {
            tp.add(stm);
            ICfgNode trueNode = stm.getTrueNode();
            ICfgNode falseNode = stm.getFalseNode();

            if (stm.isCondition()) {
                int currentIterations = tp.count(trueNode);

                if (stm instanceof AbstractConditionLoopCfgNode) {
                    // If the current condition is the testing loop condition
                    if (stm.equals(loopCondition)) {
                        // Find the maximum iteration for the testing loop
                        if (currentIterations == 0) {
                            maximumIterationForTestingLoop = getMaximumIterationsInTargetCondition(stm, tp);
                        }

                        if (currentIterations < maximumIterationForTestingLoop) {
                            if (hasSolution(tp, trueNode, true, cfg))
                                traverseCFG(trueNode, tp, testpaths, false);
                            else
                                logger.debug("no solution. stop this branch");

                        } else if (currentIterations == maximumIterationForTestingLoop) {
                            PartialTestpath newTp = (PartialTestpath) tp.clone();

                            if (!addTheEndTestingCondition)
                                newTp.remove(newTp.size() - 1);
                            else {
                                newTp.setFinalConditionType(false);
                            }

                            newTp.setDescription("Loop condition: " + loopCondition.toString() + ", its loop num = "
                                    + maximumIterationForTestingLoop + ", max other loops iteration = "
                                    + maximumIterationsForOtherLoops + ", INCREASE = " + delta_);
                            String solution = solveTestpath(cfg.getFunctionNode(), newTp);
                            if (!solution.equals(IZ3SolutionParser.NO_SOLUTION)) {
                                logger.debug(newTp.getDescription());
                                logger.debug(solution);
                                logger.debug("\n");
                            }
                            testpaths.add(newTp);
                        }

                    } else if (currentIterations <= maximumIterationsForOtherLoops) {
                        if (hasSolution(tp, falseNode, false, cfg))
                            traverseCFG(falseNode, tp, testpaths, false);
                        else
                            logger.debug("no solution. stop this branch");

                        if (hasSolution(tp, trueNode, true, cfg))
                            traverseCFG(trueNode, tp, testpaths, false);
                        else
                            logger.debug("no solution. stop this branch");
                    }

                } else {
                    if (hasSolution(tp, falseNode, false, cfg))
                        traverseCFG(falseNode, tp, testpaths, false);
                    else
                        logger.debug("no solution. stop this branch");

                    if (hasSolution(tp, trueNode, true, cfg))
                        traverseCFG(trueNode, tp, testpaths, false);
                    else
                        logger.debug("no solution. stop this branch");
                }

            } else
                traverseCFG(trueNode, tp, testpaths, false);
            tp.remove(tp.size() - 1);
        }
    }

    boolean hasSolution(PartialTestpath tp, ICfgNode newNode, boolean typeNewNode, ICFG cfg) throws Exception {
        PartialTestpath tmpTp = (PartialTestpath) tp.clone();
        tmpTp.add(newNode);
        tmpTp.setFinalConditionType(typeNewNode);

        if (solveTestpath(cfg.getFunctionNode(), tmpTp).equals(IZ3SolutionParser.NO_SOLUTION)) {
            return false;
        } else
            return true;
    }

}
