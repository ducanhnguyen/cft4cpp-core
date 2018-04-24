package test.testdatageneration;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;

public class TsdvSubConditionTest extends AbstractJUnitTest {
//	@Test
//	public void test1() throws LineUnavailableException {
//		boolean reachCoverageObjective = generateTestdata(Paths.TSDV_R1_4, "bmi(float,float)",
//				new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 75f), IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI());
//		Assert.assertEquals(true, reachCoverageObjective);
//	}

    @Test
    public void test3() throws LineUnavailableException {
        boolean reachCoverageObjective = generateTestdata(Paths.TSDV_R1_4, "calculateAge(Date,Date)", null,
                IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI());
        Assert.assertEquals(true, reachCoverageObjective);
    }
    //
    // @Test
    // public void test4() throws LineUnavailableException {
    // boolean reachCoverageObjective = generateTestdata(Paths.TSDV_R1_4,
    // "calculateZodiac(Date)", null,
    // IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI());
    // Assert.assertEquals(true, reachCoverageObjective);
    // }

}
