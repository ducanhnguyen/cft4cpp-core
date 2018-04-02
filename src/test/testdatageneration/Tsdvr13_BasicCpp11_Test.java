package test.testdatageneration;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;

public class Tsdvr13_BasicCpp11_Test extends AbstractJUnitTest {

    @Test
    public void test1() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_3, "AutoKwTest(int)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }

    @Test
    public void test2() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_3, "AutoKwTest2(int)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }

    @Test
    public void test3() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_3, "AutoKwTest3(int)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }

    @Test
    public void test4() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_3, "NullPtrTest(int*)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }

}
