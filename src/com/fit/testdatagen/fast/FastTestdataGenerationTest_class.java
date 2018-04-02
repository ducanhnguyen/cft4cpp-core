package com.fit.testdatagen.fast;

import javax.sound.sampled.LineUnavailableException;

import org.junit.Assert;
import org.junit.Ignore;
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
public class FastTestdataGenerationTest_class extends AbstractJUnitTest {
	@Rule
	public Timeout globalTimeout = Timeout.seconds(120);

	@Test
	public void test9() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.COMBINED_STATIC_AND_DYNAMIC_GENERATION,
				"struct_test0(SinhVien1)", null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test
	public void test10() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.COMBINED_STATIC_AND_DYNAMIC_GENERATION,
				"struct_test1(SinhVien1)", null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test
	@Ignore
	public void test11() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.COMBINED_STATIC_AND_DYNAMIC_GENERATION,
						"class_test0(int,int*,int[],int[2],char,char*,char[],char[10],SinhVien*,SinhVien,SinhVien[])",
						null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // HALF-PASS: sv.getAge() ---> sv.age
	@Ignore
	public void test12() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.COMBINED_STATIC_AND_DYNAMIC_GENERATION,
				"class_test1(SinhVien)", null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test
	@Ignore
	public void test13() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.COMBINED_STATIC_AND_DYNAMIC_GENERATION,
				"class_test2(SinhVien)", null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test
	@Ignore
	public void test14() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.COMBINED_STATIC_AND_DYNAMIC_GENERATION,
				"class_test3(SinhVien)", null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // HALF-PASS: sv.getAge() ---> sv.age
	@Ignore
	public void test15() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.COMBINED_STATIC_AND_DYNAMIC_GENERATION,
				"class_test4(SinhVien)", null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}
}
