package test.parser;

import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.cfg.object.ConditionCfgNode;
import com.fit.cfg.object.ICfgNode;
import com.fit.cfg.object.SimpleCfgNode;
import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test cfg generation for branch/statement coverage
 *
 * @author ducanhnguyen
 */
public class CFGGenerationTest {
    @Test
    public void test01() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.CFG_GENERATION_SOURCECODE));
        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testCFGBranch0()").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("int a;", "a>0", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>0", "a -= 1;", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>0", "a = 2;", false, cfg));
    }

    @Test
    public void test02() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.CFG_GENERATION_SOURCECODE));
        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testCFGBranch1()").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("int a;", "a>0", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>0", "a -= 1;", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>0", "a = 2;", false, cfg));
    }

    @Test
    public void test03() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.CFG_GENERATION_SOURCECODE));
        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testCFGBranch2()").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("int a;", "a>0", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>0", "a -= 1;", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>0", "a = 2;", false, cfg));
    }

    @Test
    public void test04() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.CFG_GENERATION_SOURCECODE));
        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testCFGBranch4()").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("bool b;", "b&&a==1", true, cfg));
        Assert.assertEquals(true, isExistEdge("bool b;", "b&&a==1", false, cfg));
        Assert.assertEquals(true, isExistEdge("b&&a==1", "a -= 1;", true, cfg));
        Assert.assertEquals(true, isExistEdge("b&&a==1", "a = 2;", false, cfg));
    }

    @Test
    public void test05() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.CFG_GENERATION_SOURCECODE));
        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testCFGBranch5()").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("bool b;", "(((b)))&&(((a==1)))", true, cfg));
        Assert.assertEquals(true, isExistEdge("bool b;", "(((b)))&&(((a==1)))", false, cfg));
        Assert.assertEquals(true, isExistEdge("(((b)))&&(((a==1)))", "a -= 1;", true, cfg));
        Assert.assertEquals(true, isExistEdge("(((b)))&&(((a==1)))", "a = 2;", false, cfg));
    }

    @Test
    public void test06() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.CFG_GENERATION_SOURCECODE));
        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testCFGBranch6()").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("int a;", "{", true, cfg));
        Assert.assertEquals(true, isExistEdge("int i = 0;", "i<10", true, cfg));
        Assert.assertEquals(true, isExistEdge("int i = 0;", "i<10", false, cfg));
        Assert.assertEquals(true, isExistEdge("i<10", "a -= 1;", true, cfg));
        Assert.assertEquals(true, isExistEdge("i<10", "}", false, cfg));

        Assert.assertEquals(true, isExistEdge("a -= 1;", "i++", true, cfg));
        Assert.assertEquals(true, isExistEdge("a -= 1;", "i++", false, cfg));
    }

    @Test
    public void test07() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.CFG_GENERATION_SOURCECODE));
        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testCFGBranch3()").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("int a;", "a -= 1;", true, cfg));
        Assert.assertEquals(true, isExistEdge("int a;", "a -= 1;", false, cfg));

        Assert.assertEquals(true, isExistEdge("a -= 1;", "a>0", true, cfg));
        Assert.assertEquals(true, isExistEdge("a -= 1;", "a>0", false, cfg));

        Assert.assertEquals(true, isExistEdge("a>0", "a -= 1;", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>0", "a = 2;", false, cfg));
    }

    /**
     * Check whether exist edge from two statements
     *
     * @param stm1       Content of statement 1
     * @param stm2       Content of statement 2
     * @param kindofEdge true/false
     * @param cfg
     * @return
     */
    private boolean isExistEdge(String stm1, String stm2, boolean kindofEdge, ICFG cfg) {
        boolean isExist = false;
        for (ICfgNode stm : cfg.getAllNodes())
            if (stm.getContent().equals(stm1))
                if (stm instanceof ConditionCfgNode) {
                    if (kindofEdge && ((ConditionCfgNode) stm).getTrueNode().getContent().equals(stm2))
                        isExist = true;
                    else if (!kindofEdge && ((ConditionCfgNode) stm).getFalseNode().getContent().equals(stm2))
                        isExist = true;
                } else if (stm instanceof SimpleCfgNode) {
                    if (kindofEdge && ((SimpleCfgNode) stm).getTrueNode().getContent().equals(stm2))
                        isExist = true;
                    else if (!kindofEdge && ((SimpleCfgNode) stm).getFalseNode().getContent().equals(stm2))
                        isExist = true;
                }
        return isExist;
    }
}
