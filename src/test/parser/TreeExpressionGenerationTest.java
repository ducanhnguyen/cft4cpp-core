package test.parser;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.se.expression.TreeExpressionGeneration;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.Node;
import com.fit.utils.Utils;
import com.fit.utils.search.ExpressionCondition;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.OperatorCondition;
import com.fit.utils.search.Search;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class TreeExpressionGenerationTest {
    IFunctionNode function = null;

    @Before
    public void ini() {
        ProjectParser parser = new ProjectParser(new File(Paths.TREE_EXPRESSION_GENERATION_TEST));
        function = (FunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "test(int,int,int)").get(0);
        System.out.println(function.getAST().getRawSignature());
    }

    @Test
    public void test1() throws Exception {
        TreeExpressionGeneration treeGen = new TreeExpressionGeneration(
                Utils.findFirstConditionByName("1", function.getAST()));
        Node root = treeGen.getRoot();
        Assert.assertEquals(0, Search.searchNodes(root, new OperatorCondition()).size());
        Assert.assertEquals(1, Search.searchNodes(root, new ExpressionCondition()).size());
    }

    @Test
    public void test2() throws Exception {
        TreeExpressionGeneration treeGen = new TreeExpressionGeneration(
                Utils.findFirstConditionByName("(1)", function.getAST()));
        Node root = treeGen.getRoot();
        Assert.assertEquals(0, Search.searchNodes(root, new OperatorCondition()).size());
        Assert.assertEquals(1, Search.searchNodes(root, new ExpressionCondition()).size());
    }

    @Test
    public void test3() throws Exception {
        TreeExpressionGeneration treeGen = new TreeExpressionGeneration(
                Utils.findFirstConditionByName("1>0", function.getAST()));
        Node root = treeGen.getRoot();
        Assert.assertEquals(0, Search.searchNodes(root, new OperatorCondition()).size());
        Assert.assertEquals(1, Search.searchNodes(root, new ExpressionCondition()).size());
    }

    @Test
    public void test4() throws Exception {
        TreeExpressionGeneration treeGen = new TreeExpressionGeneration(
                Utils.findFirstConditionByName("a>0 && b > 0", function.getAST()));
        Node root = treeGen.getRoot();
        Assert.assertEquals(1, Search.searchNodes(root, new OperatorCondition()).size());
        Assert.assertEquals(2, Search.searchNodes(root, new ExpressionCondition()).size());
    }

    @Test
    public void test5() throws Exception {
        TreeExpressionGeneration treeGen = new TreeExpressionGeneration(
                Utils.findFirstConditionByName("a>0 && b > 0 && c>0", function.getAST()));
        Node root = treeGen.getRoot();
        Assert.assertEquals(2, Search.searchNodes(root, new OperatorCondition()).size());
        Assert.assertEquals(3, Search.searchNodes(root, new ExpressionCondition()).size());
    }

    @Test
    public void test6() throws Exception {
        TreeExpressionGeneration treeGen = new TreeExpressionGeneration(
                Utils.findFirstConditionByName("a>0 && b > 0 && ((c>0))", function.getAST()));
        Node root = treeGen.getRoot();
        Assert.assertEquals(2, Search.searchNodes(root, new OperatorCondition()).size());
        Assert.assertEquals(3, Search.searchNodes(root, new ExpressionCondition()).size());
    }

    @Test
    public void test7() throws Exception {
        TreeExpressionGeneration treeGen = new TreeExpressionGeneration(
                Utils.findFirstConditionByName("a>0 || (b > 0 && b<9)", function.getAST()));
        Node root = treeGen.getRoot();
        Assert.assertEquals(2, Search.searchNodes(root, new OperatorCondition()).size());
        Assert.assertEquals(3, Search.searchNodes(root, new ExpressionCondition()).size());
    }

    @Test
    public void test8() throws Exception {
        TreeExpressionGeneration treeGen = new TreeExpressionGeneration(
                Utils.findFirstConditionByName("a>0 || (b > 0 && (b<9 || b>1))", function.getAST()));
        Node root = treeGen.getRoot();
        Assert.assertEquals(3, Search.searchNodes(root, new OperatorCondition()).size());
        Assert.assertEquals(4, Search.searchNodes(root, new ExpressionCondition()).size());
    }
}
