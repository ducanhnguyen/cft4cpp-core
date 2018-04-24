package test.testdatageneration;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;

public class Vnu_SwitchCase_Test extends AbstractJUnitTest {
    @Test
    public void test1() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "switchCase0(int,int)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test2() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "switchCase0(int,int)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test3() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "switchCase2(char)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test4() throws LineUnavailableException {
        Assert.assertEquals(true,
                generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "switchCase3(char)",
                        new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 87.5f), IFunctionConfig.BRANCH_COVERAGE,
                        new FuntionTestReportGUI()));
    }

    @Test
    public void test5() throws LineUnavailableException {
        Assert.assertEquals(true,
                generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "switchCase3(char)",
                        new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 84.61539f), IFunctionConfig.STATEMENT_COVERAGE,
                        new FuntionTestReportGUI()));
    }

    @Test
    public void test6() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "switchCase4(char,int)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }
}
