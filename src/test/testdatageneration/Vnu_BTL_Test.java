package test.testdatageneration;

import javax.sound.sampled.LineUnavailableException;

import org.junit.Assert;
import org.junit.Test;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.testdatagen.htmlreport.FuntionTestReportGUI;

public class Vnu_BTL_Test extends AbstractJUnitTest {
	@Test
	public void test1() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.BTL, "TS::xoa_dau_cach_thua(char[])", null,
				IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
	}

	@Test
	public void test2() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.BTL, "TS::trao_doi(int&,int&)", null,
				IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
	}

	@Test
	public void test3() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.BTL, "TS::trao_doi(char&,char&)", null,
				IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
	}

	@Test
	public void test4() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.BTL, "TS::trao_doi(float&,float&)", null,
				IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
	}

	@Test
	public void test5() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.BTL, "TS::trao_doi(char[],char[])", null,
				IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
	}

	@Test // Error
	public void test6() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.BTL, "TS::trao_doi(TS*,TS*)", null,
				IFunctionConfig.BRANCH_COVERAGE, new FuntionTestReportGUI()));
	}

}
