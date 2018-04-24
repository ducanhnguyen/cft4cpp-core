package test.parser;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.INode;
import com.fit.tree.object.TypedefDeclaration;
import com.fit.utils.search.Search;
import com.fit.utils.search.TypedefNodeCondition;

/**
 * Check the correct of typedef detection
 *
 * @author DucAnh
 */
public class TypedefNodeNameTest {

    @Test
    public void test1() {
        File p = new File(Paths.TYPEDEF_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new TypedefNodeCondition(), File.separator + "hoaqua")
                .get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test2() {
        File p = new File(Paths.TYPEDEF_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new TypedefNodeCondition(), File.separator + "int_type")
                .get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test3() {
        File p = new File(Paths.TYPEDEF_NODE_NAME_TEST);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new TypedefNodeCondition(), File.separator + "float_type")
                .get(0);
        Assert.assertEquals(true, n != null);
    }

    @Test
    public void test4() {
        File p = new File(Paths.BTL);

        ProjectParser parser = new ProjectParser(p);
        INode n = Search.searchNodes(parser.getRootTree(), new TypedefNodeCondition(), "qlsv.h" + File.separator + "TS")
                .get(0);

        Assert.assertEquals("thisinh", ((TypedefDeclaration) n).getOldType());
        Assert.assertEquals(true, n != null);
    }
}
