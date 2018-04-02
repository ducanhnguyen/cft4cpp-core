package com.fit.instrument;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by DucToan on 10/03/2017.
 */
public class FunctionInstrumentationForSubConditionTest {

    @Test
    public void test1() {
        ProjectParser parser = new ProjectParser(new File(Paths.SUB_CONDITION));

        INode function = Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testIfCondition1(int,int)").get(0);

        Assert.assertTrue(new FunctionInstrumentationForSubCondition((IFunctionNode) function, true)
                .generateInstrumentedFunction().contains("(mark(\"a > 2\") && (a > 2)) && (mark(\"b <4\") && (b <4))"));

    }

    @Test
    public void test3() {
        ProjectParser parser = new ProjectParser(new File(Paths.SUB_CONDITION));

        INode function = Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testIfCondition3(int,int,int)").get(0);

        Assert.assertTrue(new FunctionInstrumentationForSubCondition((IFunctionNode) function, true)
                .generateInstrumentedFunction().contains(
                        "(((mark(\"a > 2\") && (a > 2)) &&  (mark(\"b < 4\") && (b < 4))) || (mark(\"c > 3\") && (c > 3)))"));

    }

    @Test
    public void test4() {
        ProjectParser parser = new ProjectParser(new File(Paths.SUB_CONDITION));

        INode function = Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testIfCondition4(int,int,int)").get(0);

        Assert.assertTrue(new FunctionInstrumentationForSubCondition((IFunctionNode) function, true)
                .generateInstrumentedFunction().contains(
                        "(mark(\"a > 2\") && (a > 2)) || (mark(\"b < 4\") && (b < 4)) || (mark(\"c > 3\") && (c > 3))"));

    }

    @Test
    public void test5() {
        ProjectParser parser = new ProjectParser(new File(Paths.SUB_CONDITION));

        INode function = Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testIfCondition5(bool,bool)").get(0);

        Assert.assertTrue(new FunctionInstrumentationForSubCondition((IFunctionNode) function, true)
                .generateInstrumentedFunction().contains("(mark(\"x\") && (x)) && (mark(\"y\") && (y))"));

    }

    @Test
    public void test6() {
        ProjectParser parser = new ProjectParser(new File(Paths.SUB_CONDITION));

        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testLoop1(int,int)")
                .get(0);

        Assert.assertTrue(new FunctionInstrumentationForSubCondition((IFunctionNode) function, true)
                .generateInstrumentedFunction().contains("(mark(\"a > 2\") && (a > 2)) && (mark(\"b <4\") && (b <4))"));

    }

    @Test
    public void test7() {
        ProjectParser parser = new ProjectParser(new File(Paths.SUB_CONDITION));

        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testLoop2(int)").get(0);

        Assert.assertTrue(new FunctionInstrumentationForSubCondition((IFunctionNode) function, true)
                .generateInstrumentedFunction()
                .contains("(mark(\"a > 2\") && (a > 2)) && (mark(\"i < 100\") && (i < 100))"));

    }

    @Test
    public void test8() {
        ProjectParser parser = new ProjectParser(new File(Paths.SUB_CONDITION));

        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "testLoop3(int,int)")
                .get(0);

        Assert.assertTrue(new FunctionInstrumentationForSubCondition((IFunctionNode) function, true)
                .generateInstrumentedFunction()
                .contains("(mark(\"a > 2\") && (a > 2)) && (mark(\"b < 4\") && (b < 4))"));

    }

}
