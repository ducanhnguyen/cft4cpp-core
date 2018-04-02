package test.testdatageneration;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;

public class Tsdvr14_r85_Test extends AbstractJUnitTest {
    @Test
    public void test1() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "i4_log_10(int)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }

    @Test
    public void test2() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "i4_power(int,int)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }

    @Test
    public void test3() throws LineUnavailableException {
        Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "r8_uniform_01(int&)", null, IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI()));
    }
}
