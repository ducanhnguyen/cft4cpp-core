package test.parser;

import com.fit.config.Paths;
import com.fit.normalizer.FunctionNameNormalizer;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FunctionNameNormalizerTest {
    @Test
    public void test01() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));
        IFunctionNode function = (IFunctionNode) Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(),
                "nsTest0" + File.separator + "Student::isAvailable(int)").get(0);

        FunctionNameNormalizer normalizer = new FunctionNameNormalizer();
        normalizer.setFunctionNode(function);
        normalizer.normalize();
        Assert.assertEquals(normalizer.getNormalizedSourcecode().contains("int nsTest0::Student::isAvailable(int"),
                true);
    }

    @Test
    public void test02() throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST));
        IFunctionNode function = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "nsTest4::func1(int)").get(0);

        FunctionNameNormalizer normalizer = new FunctionNameNormalizer();
        normalizer.setFunctionNode(function);
        normalizer.normalize();
        Assert.assertEquals(normalizer.getNormalizedSourcecode().contains("int nsTest4::func1(int"), true);
    }
}
