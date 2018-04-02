package com.fit.testdatagen.loop;

import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.cfg.object.AbstractConditionLoopCfgNode;
import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.File;

/**
 * Created by ducanhnguyen on 7/05/2017.
 */
public class TestRunnerForPaper3 {
    final static Logger logger = Logger.getLogger(TestRunnerForPaper3.class);

    @Test
    public void testLoop1() throws Exception {
        ICFG cfg = initialize();
        // Generate test paths for loop
        AbstractConditionLoopCfgNode loopCondition = (AbstractConditionLoopCfgNode) cfg
                .findFirstCfgNodeByContent("s[c] != '\\0'");
        PossibleTestpathGenerationForLoop tpGen = new PossibleTestpathGenerationForLoop(cfg, loopCondition);
        tpGen.setIterationForUnboundedTestingLoop(50);
        tpGen.setMaximumIterationsForOtherLoops(1);
        tpGen.setAddTheEndTestingCondition(true);
        tpGen.generateTestpaths();
        logger.debug(tpGen.getPossibleTestpaths().size());
    }

    public ICFG initialize() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));

        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "find_frequency(char[],int[])").get(0);
        logger.debug(function.getAST().getRawSignature());

        // Generate cfg
        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        cfg.setFunctionNode(function);
        cfg.setIdforAllNodes();
        cfg.resetVisitedStateOfNodes();
        return cfg;
    }
}
