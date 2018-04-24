package test.parser;

import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.FunctionNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class ExternalVariableDetecterTest {

    ProjectParser parser;

    @Before
    public void ini() {
        File p = new File(Utils.normalizePath("..\\ava\\data-test\\ducanh\\ExternalVariableDetecterTest"));
        parser = new ProjectParser(p);
    }

    @Test
    public void test1() {
        FunctionNode function = (FunctionNode) Search.searchNodes(
                parser.getRootTree(), new FunctionNodeCondition(),
                "C::test(A*,int)").get(0);
        Assert.assertEquals(function.getExternalVariables().size(), 10);
    }

    /**
     * static variable int class, struct
     */
    @Test
    public void test2() {
        FunctionNode function = (FunctionNode) Search.searchNodes(
                parser.getRootTree(), new FunctionNodeCondition(),
                "C\\testStatic()").get(0);
        Assert.assertEquals(function.getExternalVariables().size(), 3);
    }

    /**
     * 2 namespaces declared in 2 files
     */
    @Test
    public void test3() {
        FunctionNode function = (FunctionNode) Search.searchNodes(
                parser.getRootTree(), new FunctionNodeCondition(),
                "B\\C1::test()").get(0);
        Assert.assertEquals(function.getExternalVariables().size(), 2);
    }

    /**
     * static in namespace
     */
    @Test
    public void test4() {
        FunctionNode function = (FunctionNode) Search.searchNodes(
                parser.getRootTree(), new FunctionNodeCondition(),
                "A\\C2\\test()").get(0);
        Assert.assertEquals(function.getExternalVariables().size(), 1);
    }

    /**
     * namespace in namespace
     */
    @Test
    public void test5() {
        FunctionNode function = (FunctionNode) Search.searchNodes(
                parser.getRootTree(), new FunctionNodeCondition(), "test5()")
                .get(0);
        Assert.assertEquals(function.getExternalVariables().size(), 4);
    }
}
