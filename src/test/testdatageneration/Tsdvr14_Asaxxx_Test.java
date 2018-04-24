package test.testdatageneration;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;

public class Tsdvr14_Asaxxx_Test extends AbstractJUnitTest {
	@Test
	public void test1() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "alnorm(double,bool)", null,
				IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
	}

	@Test
	public void test2() throws LineUnavailableException {
		AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION = true;
		Assert.assertEquals(true,
				generateTestdata(Paths.TSDV_R1_4, "student_noncentral_cdf_values(int*,int*,double*,double*,double*)",
						null, IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
		AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION = false;
	}

	@Test
	public void test3() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "digamma(double,int*)", null,
				IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
	}

	@Test
	public void test4() throws LineUnavailableException {
		AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION = true;
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "psi_values(int*,double*,double*)", null,
				IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
		AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION = false;
	}
}
