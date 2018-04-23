package testdatageneration;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;

public class Tsdvr1_Struct_Test extends AbstractJUnitTest {
    @Test
    public void test1() throws LineUnavailableException {

        Assert.assertEquals(true,
                generateTestdata(Paths.TSDV_R1, "SimpleTest(Student)", null, IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test2() throws LineUnavailableException {

        Assert.assertEquals(true,
                generateTestdata(Paths.TSDV_R1, "SimpleTestWithKeyword(struct Student)", null, IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }

    @Test
    public void test3() throws LineUnavailableException {

        Assert.assertEquals(true,
                generateTestdata(Paths.TSDV_R1, "NestedStructTest(Class,Class)", null, IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
    }
}
