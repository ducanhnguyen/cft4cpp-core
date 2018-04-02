package test.parser;

import com.fit.cfg.CFGGenerationSubCondition;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.cfg.object.ConditionCfgNode;
import com.fit.cfg.object.ICfgNode;
import com.fit.cfg.object.SimpleCfgNode;
import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import com.fit.utils.tostring.DependencyTreeDisplayer;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test cfg generation for sub-condition
 *
 * @author ducanhnguyen
 */
public class CFGGenerationSubConditionTest {
    @Test
    public void test01() throws Exception {
        ProjectParser parser = new ProjectParser(new File(
                Utils.normalizePath(Paths.CFG_GENERATION_SOURCECODE)));
        System.out.println(new DependencyTreeDisplayer(parser.getRootTree())
                .getTreeInString());
        IFunctionNode function = (IFunctionNode) Search.searchNodes(
                parser.getRootTree(), new FunctionNodeCondition(),
                "test1(int,int,int,int)").get(0);

        CFGGenerationSubCondition cfgGen = new CFGGenerationSubCondition(
                function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();
        Assert.assertEquals(true, isExistEdge("int x = 0;", "a>b", true, cfg));
        Assert.assertEquals(true, isExistEdge("int x = 0;", "a>b", false, cfg));
        Assert.assertEquals(true, isExistEdge("a>b", "c>d", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>b", "x = 1;", false, cfg));
        Assert.assertEquals(true, isExistEdge("c>d", "x = 2;", true, cfg));
        Assert.assertEquals(true, isExistEdge("c>d", "x = 1;", false, cfg));
    }

    @Test
    public void test02() throws Exception {
        ProjectParser parser = new ProjectParser(new File(
                Utils.normalizePath(Paths.CFG_GENERATION_SOURCECODE)));
        IFunctionNode function = (IFunctionNode) Search.searchNodes(
                parser.getRootTree(), new FunctionNodeCondition(),
                "test2(int,int,int,int)").get(0);

        CFGGenerationSubCondition cfgGen = new CFGGenerationSubCondition(
                function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("int x = 0;", "a>b", true, cfg));
        Assert.assertEquals(true, isExistEdge("int x = 0;", "a>b", false, cfg));
        Assert.assertEquals(true, isExistEdge("a>b", "c>d", false, cfg));
        Assert.assertEquals(true, isExistEdge("a>b", "x = 2;", true, cfg));
        Assert.assertEquals(true, isExistEdge("c>d", "x = 2;", true, cfg));
        Assert.assertEquals(true, isExistEdge("c>d", "x = 1;", false, cfg));
    }

    @Test
    public void test03() throws Exception {
        ProjectParser parser = new ProjectParser(new File(
                Utils.normalizePath(Paths.CFG_GENERATION_SOURCECODE)));
        IFunctionNode function = (IFunctionNode) Search.searchNodes(
                parser.getRootTree(), new FunctionNodeCondition(),
                "test3(int,int,int,int)").get(0);

        CFGGenerationSubCondition cfgGen = new CFGGenerationSubCondition(
                function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("int x = 0;", "a>b", true, cfg));
        Assert.assertEquals(true, isExistEdge("int x = 0;", "a>b", false, cfg));

        Assert.assertEquals(true, isExistEdge("a>b", "c>d", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>b", "x = 1;", false, cfg));

        Assert.assertEquals(true, isExistEdge("c>d", "1>0", true, cfg));
        Assert.assertEquals(true, isExistEdge("c>d", "x = 1;", false, cfg));

        Assert.assertEquals(true, isExistEdge("1>0", "x = 2;", true, cfg));
        Assert.assertEquals(true, isExistEdge("1>0", "x = 1;", false, cfg));
    }

    @Test
    public void test04() throws Exception {
        ProjectParser parser = new ProjectParser(new File(
                Utils.normalizePath(Paths.CFG_GENERATION_SOURCECODE)));
        IFunctionNode function = (IFunctionNode) Search.searchNodes(
                parser.getRootTree(), new FunctionNodeCondition(),
                "test4(int,int,int,int)").get(0);

        CFGGenerationSubCondition cfgGen = new CFGGenerationSubCondition(
                function, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
        ICFG cfg = cfgGen.generateCFG();

        Assert.assertEquals(true, isExistEdge("int x = 0;", "a>b", true, cfg));
        Assert.assertEquals(true, isExistEdge("int x = 0;", "a>b", false, cfg));

        Assert.assertEquals(true, isExistEdge("a>b", "x = 2;", true, cfg));
        Assert.assertEquals(true, isExistEdge("a>b", "c>d", false, cfg));

        Assert.assertEquals(true, isExistEdge("c>d", "x = 2;", true, cfg));
        Assert.assertEquals(true, isExistEdge("c>d", "1>0", false, cfg));

        Assert.assertEquals(true, isExistEdge("1>0", "x = 2;", true, cfg));
        Assert.assertEquals(true, isExistEdge("1>0", "x = 1;", false, cfg));
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
    private boolean isExistEdge(String stm1, String stm2, boolean kindofEdge,
                                ICFG cfg) {
        boolean isExist = false;
        for (ICfgNode stm : cfg.getAllNodes())
            if (stm.getContent().equals(stm1))
                if (stm instanceof ConditionCfgNode) {
                    if (kindofEdge
                            && ((ConditionCfgNode) stm).getTrueNode()
                            .getContent().equals(stm2))
                        isExist = true;
                    else if (!kindofEdge
                            && ((ConditionCfgNode) stm).getFalseNode()
                            .getContent().equals(stm2))
                        isExist = true;
                } else if (stm instanceof SimpleCfgNode) {
                    if (kindofEdge
                            && ((SimpleCfgNode) stm).getTrueNode().getContent()
                            .equals(stm2))
                        isExist = true;
                    else if (!kindofEdge
                            && ((SimpleCfgNode) stm).getFalseNode()
                            .getContent().equals(stm2))
                        isExist = true;
                }
        return isExist;
    }
}
