package test.testdatageneration;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;

public class Tsdvr1_Namespace_Test extends AbstractJUnitTest {
    @Test
    public void test1() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1, "Level2SimpleTest(X)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }

    @Test
    public void test2() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1, "Level2MultipleNsTest(::X,::ns1::X,X)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test3() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1, "Level1SimpleTest(X)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }

    @Test
    public void test4() throws LineUnavailableException {

        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1, "Level1MultipleNsTest(::X,X,ns2::X)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test5() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1, "Level0MultipleNsTest(X,ns1::X,ns1::ns2::X)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test6() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1, "FunctionInAnnonymousNsTest(int)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test7() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1, "Level0UsingNsTest(int)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }

    @Test
    public void test8() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1, "Level0UsingAutoNsTest(Y)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }
}
