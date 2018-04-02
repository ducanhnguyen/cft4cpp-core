package com.fit.testdatagen.fast;

import javax.sound.sampled.LineUnavailableException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;

import test.testdatageneration.AbstractJUnitTest;

/**
 * Vnu_Basic_Test
 */
public class FastTestdataGenerationTest_struct extends AbstractJUnitTest {
	@Rule
	public Timeout globalTimeout = Timeout.seconds(120); 

	@Test
	public void Vnu_Basic_Test1() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "testDate0(Date)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test
	public void Vnu_Basic_Test2() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "testDate1(Date)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test
	public void Vnu_Basic_Test3() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "testDate2(Date)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test
	public void Vnu_Basic_Test4() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "testDate3(Date)",
						new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 50.0f), IFunctionConfig.SUBCONDITION,
						new FuntionTestReportGUI()));
	}

	@Test
	public void Vnu_Basic_Test5() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "testDate4(Date)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}
}
