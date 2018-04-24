package test.testdatageneration;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;

public class Tsdvr12_ViewSource_Test extends AbstractJUnitTest {
    @Test
    public void test1() throws LineUnavailableException {
        Assert.assertEquals(true,
                generateTestdata(Paths.TSDV_R1_2, "foo()", new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 50f),
                        IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test0() throws LineUnavailableException {
        Assert.assertEquals(true,
                generateTestdata(Paths.TSDV_R1_2, "foo()", new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 75f),
                        IFunctionConfig.STATEMENT_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test2() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_2, "bubbleSort(int[],int)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test3() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_2, "CFGIfElse(int)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test4() throws LineUnavailableException {
        Assert.assertEquals(true,
                generateTestdata(Paths.TSDV_R1_2, "CFGSwitch(int,int)",
                        new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 86.36364f), IFunctionConfig.BRANCH_COVERAGE,
                        new FuntionTestReportGUI()));
    }

    @Test
    public void test5() throws LineUnavailableException {
        Assert.assertEquals(true,
                generateTestdata(Paths.TSDV_R1_2, "CFGSwitch(int,int)", new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 92.0f),
                        IFunctionConfig.STATEMENT_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test6() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_2, "CFGException(int)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }
}
