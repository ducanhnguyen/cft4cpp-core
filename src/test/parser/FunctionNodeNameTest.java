package test.parser;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Lớp này kiểm tra tên các node trong chương trình đúng chuẩn chưa * @author
 * DucAnh
 */
public class FunctionNodeNameTest {

    @Test
    public void test1() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "getInforOfApple(IndividualStore::hoaqua)").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test10() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "testMethod(A1::customStruct)").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test11() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "displayAddress(IndividualStore::House::Accommodation::information)").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test12() {
        File p = new File(Paths.BTL);

        ProjectParser parser = new ProjectParser(p);
        FunctionNode sampleNode = (FunctionNode) Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "TS::inFile(ofstream&,TS*)").get(0);
        Assert.assertEquals(true, sampleNode != null);
    }

    @Test
    public void test2() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "listAllApples(std::vector<IndividualStore::hoaqua>)").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test3() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "getInforOfApples(IndividualStore::hoaqua*)").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test4() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "getInforOfApples(IndividualStore::hoaqua**)").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test5() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "getInforOfApples(IndividualStore::hoaqua&)").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test6() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "getInforOfApples2(IndividualStore::hoaqua[])").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test7() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "getInforOfApples2(IndividualStore::hoaqua[][5])").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test8() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                File.separator + "getInforOfChainStore(ns1::C1)").get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test9() {
        File p = new File(Paths.FUNCTION_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), File.separator + "getSQRT(float_type)")
                .get(0);
        Assert.assertEquals(true, n != null);
    }
}
