package test.parser;

import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IProcessNotify;
import com.fit.tree.object.Node;
import com.fit.tree.object.TypedefDeclaration;
import com.fit.utils.search.ISearchCondition;
import com.fit.utils.search.Search;
import com.fit.utils.search.TypedefNodeCondition;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class MultipleTypeDefDeclareTest {
    final static Logger logger = Logger.getLogger(MultipleTypeDefDeclareTest.class);

    private static ISearchCondition SEARCH_TYPEDEF = new TypedefNodeCondition();

    private static String PATH = "data-test/samvu/Simple";

    private Node root;

    @Before
    public void setUp() throws Exception {
        File p = new File(MultipleTypeDefDeclareTest.PATH);
        root = (Node) new ProjectParser(p, IProcessNotify.DEFAULT).getRootTree();
    }

    @Test
    public void _1_testSimple() {
        TypedefDeclaration td = (TypedefDeclaration) Search
                .searchNodes(root, MultipleTypeDefDeclareTest.SEARCH_TYPEDEF, File.separator + "int_type").get(0);
        Assert.assertEquals("int", td.getOldType());
        Assert.assertEquals("int_type", td.getNewType());
    }

    @Test
    public void _2_testSecondTypedef() {
        TypedefDeclaration td = (TypedefDeclaration) Search
                .searchNodes(root, MultipleTypeDefDeclareTest.SEARCH_TYPEDEF, File.separator + "int_type_2").get(0);
        Assert.assertEquals("int", td.getOldType());
        Assert.assertEquals("int_type_2", td.getNewType());
    }

    @Test
    public void _3_testPointerTypdef() {
        TypedefDeclaration td = (TypedefDeclaration) Search
                .searchNodes(root, MultipleTypeDefDeclareTest.SEARCH_TYPEDEF, File.separator + "int_pointer").get(0);
        Assert.assertEquals("int*", td.getOldType());
        Assert.assertEquals("int_pointer", td.getNewType());
    }

}
