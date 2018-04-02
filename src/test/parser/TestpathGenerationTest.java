package test.parser;

import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.testpath.PossibleTestpathGeneration;
import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class TestpathGenerationTest {

    ProjectParser parser;

    @Before
    public void ini() throws Exception {
        File p = new File(Paths.TEST_PATH_GENERATION_TEST);
        parser = new ProjectParser(p);
    }

    @Test
    public void test1() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "test1()").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG());
        tpGen.generateTestpaths();
        Assert.assertEquals(1, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test10() throws Exception {
        INode function = Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "control_block1(int,int)").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG(), 2);

        tpGen.generateTestpaths();
        Assert.assertEquals(7, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test11() throws Exception {
        INode function = Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "control_block1(int,int)").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG(), 0);

        tpGen.generateTestpaths();
        Assert.assertEquals(1, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test12() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "control_block2()")
                .get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG(), 1);

        tpGen.generateTestpaths();
        Assert.assertEquals(2, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test14() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "control_block4()")
                .get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG(), 1);

        tpGen.generateTestpaths();
        Assert.assertEquals(1, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test15() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "control_block4()")
                .get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG(), 2);

        tpGen.generateTestpaths();
        Assert.assertEquals(2, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test2() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "test2()").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG());
        tpGen.generateTestpaths();
        Assert.assertEquals(1, tpGen.getPossibleTestpaths().size());
    }

    // @Test
    // public void test8() throws Exception{
    // INode function = Search.searchNodes((Node) parser.getRootTree(), new
    // FunctionNodeCondition(), "try_catch2()").get(0);
    //
    // CFGGeneration cfgGen = new CFGGeneration((IFunctionNode) function);
    // TestpathGeneration tpGen = new TestpathGeneration(cfgGen.getCFG());
    // assertEquals(???, tpGen.generateAllPossibleTestpaths().size());
    // }
    @Test
    public void test3() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "test3(int,int)").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG());
        tpGen.generateTestpaths();
        Assert.assertEquals(2, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test4() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "switch_case2(char)")
                .get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG());
        tpGen.generateTestpaths();
        Assert.assertEquals(2, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test5() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "switch_case3(char)")
                .get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG());
        tpGen.generateTestpaths();
        Assert.assertEquals(4, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test6() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "switch_case1(char)")
                .get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG());
        tpGen.generateTestpaths();
        Assert.assertEquals(2, tpGen.getPossibleTestpaths().size());
    }

    // @Test
    // public void test13() throws Exception{
    // INode function = Search.searchNodes((Node) parser.getRootTree(), new
    // FunctionNodeCondition(), "control_block3()")
    // .get(0);
    //
    // CFGGeneration cfgGen = new CFGGeneration((IFunctionNode) function);
    // TestpathGeneration tpGen = new TestpathGeneration(cfgGen.getCFG(), 1);
    //
    // assertEquals(???, tpGen.generateAllPossibleTestpaths().size());
    // }
    @Test
    public void test7() throws Exception {
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "try_catch1(int,int)")
                .get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG());
        tpGen.generateTestpaths();
        Assert.assertEquals(2, tpGen.getPossibleTestpaths().size());
    }

    @Test
    public void test9() throws Exception {
        INode function = Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "control_block1(int,int)").get(0);

        CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage((IFunctionNode) function);
        PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG(), 1);

        tpGen.generateTestpaths();
        Assert.assertEquals(3, tpGen.getPossibleTestpaths().size());
    }

    // @Test
    // public void test16() throws Exception{
    // INode function = Search.searchNodes((Node) parser.getRootTree(), new
    // FunctionNodeCondition(), "control_block5()")
    // .get(0);
    //
    // CFGGeneration cfgGen = new CFGGeneration((IFunctionNode) function);
    // TestpathGeneration tpGen = new TestpathGeneration(cfgGen.getCFG(),2);
    //
    // assertEquals(???, tpGen.generateAllPossibleTestpaths().size());
    // }
}
