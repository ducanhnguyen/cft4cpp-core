package test.testdatageneration;

import javax.sound.sampled.LineUnavailableException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;

public class Tsdvr14_Age_Test extends AbstractJUnitTest {
    @Test
    public void test1() throws LineUnavailableException {
        boolean reachCoverageObjective = generateTestdata(Paths.TSDV_R1_4, "bmi(float,float)",
                new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 87.5f), IFunctionConfig.BRANCH_COVERAGE,
                new FuntionTestReportGUI());
        Assert.assertEquals(true, reachCoverageObjective);
    }

    @Test
    public void test2() throws LineUnavailableException {
        boolean reachCoverageObjective = generateTestdata(Paths.TSDV_R1_4, "bmi(float,float)",
                new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 90.909096f), IFunctionConfig.STATEMENT_COVERAGE,
                new FuntionTestReportGUI());
        Assert.assertEquals(true, reachCoverageObjective);
    }

    @Test
    @Ignore
    public void test3() throws LineUnavailableException {
        boolean reachCoverageObjective = generateTestdata(Paths.TSDV_R1_4, "calculateAge(Date,Date)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI());
        Assert.assertEquals(true, reachCoverageObjective);
    }

    @Test
    @Ignore
    public void test4() throws LineUnavailableException {
        boolean reachCoverageObjective = generateTestdata(Paths.TSDV_R1_4, "calculateZodiac(Date)", null,
                IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI());
        Assert.assertEquals(true, reachCoverageObjective);
    }

}
