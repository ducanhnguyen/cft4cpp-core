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

public class FastTestdataGenerationTest_complex extends AbstractJUnitTest {
	@Rule
	public Timeout globalTimeout = Timeout.seconds(120);

	/**
	 * Tsdvr14_Age_Test
	 *
	 * @throws LineUnavailableException
	 */
	@Test // PASS
	public void Tsdvr14_Age_Test_1() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.TSDV_R1_4, "bmi(float,float)", new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 58.33f),
						IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	/**
	 * Tsdvr14_Cities_Test
	 *
	 * @throws LineUnavailableException
	 */
	@Test // PASS
	public void Tsdvr14_Age_Test_2() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.TSDV_R1_4, "calculateZodiac(Date)",
						new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 90.83f), IFunctionConfig.SUBCONDITION,
						new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Cities_Test_2() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "ch_cap(char)", null, IFunctionConfig.SUBCONDITION,
				new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Cities_Test_3() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "ch_eqi(char,char)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));

	}

	@Test // PASS
	public void Tsdvr14_Cities_Test_4() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "ch_to_digit(char)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Cities_Test_5() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "degrees_to_radians(double)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Cities_Test_6() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "dms_to_radians(int[])", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Cities_Test_7() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "i4_sign(int)", null, IFunctionConfig.SUBCONDITION,
				new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Cities_Test_8() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "lat_char(int)", null, IFunctionConfig.SUBCONDITION,
				new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Cities_Test_9() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "r8_abs(double)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	/**
	 * Tsdvr14_Complex_Test
	 *
	 * @throws LineUnavailableException
	 */
	@Test // PASS
	public void Tsdvr14_Complex_Test_test1() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.TSDV_R1_4, "ComplexTest2(int,int)", new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 91f),
						IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test2() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "toUpperCase(char*,int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test3() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "toUpperCase2(char*)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test4() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "MultipleScopeTest(int,int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test5() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "gcd(int,int)", null, IFunctionConfig.SUBCONDITION,
				new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test6() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "RecursiveSearch(int,int[],int,int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test7() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "BinarySearch(int,int[],int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test8() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.TSDV_R1_4, "getBigThree(int,int,int)",
						new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 81.25f), IFunctionConfig.SUBCONDITION,
						new FuntionTestReportGUI()));
	}

	@Test // HALF-PASS (xu ly lam tron bien kieu "int a=1/2" la okee)
	public void Tsdvr14_Complex_Test_test10() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.TSDV_R1_4, "r8_uniform_01(int*)", new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 50f),
						IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // HALF-PASS: handle casting
	public void Tsdvr14_Complex_Test_test11() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.TSDV_R1_4, "r4_uni(int*,int*)", new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 83.33f),
						IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test12() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "revers(int[],int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test13() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "r4poly_value(int,float[],float)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test14() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "r8vec_dot_product(int,double[],double[])", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Complex_Test_test15() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "trigamma(double,int*)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	/**
	 * Tsdvr14_Asaxxx_Test
	 */
	@Test // PASS
	public void Tsdvr14_Asaxxx_Test1() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "alnorm(double,bool)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Asaxxx_Test2() throws LineUnavailableException {
		AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION = true;
		Assert.assertEquals(true,
				generateTestdata(Paths.TSDV_R1_4, "student_noncentral_cdf_values(int*,int*,double*,double*,double*)",
						null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
		AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION = false;
	}

	@Test // PASS
	public void Tsdvr14_Asaxxx_Test3() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "digamma(double,int*)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Tsdvr14_Asaxxx_Test4() throws LineUnavailableException {
		AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION = true;
		Assert.assertEquals(true, generateTestdata(Paths.TSDV_R1_4, "psi_values(int*,double*,double*)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
		AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION = false;
	}

	/**
	 * Vnu_Pratical_Test
	 */
	@Test // PASS
	public void Vnu_Pratical_Test1() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "Tritype(int,int,int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test2() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "Merge1(int[3],int[3],int[6])", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // HALF-PASS: Detect test path wrong because of the special source code
	@Ignore
	public void Vnu_Pratical_Test3() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "Merge2(int[],int[],int[],int,int)",
				null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test4() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "uninit_var(int[3],int[3])", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test5() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "Bsearch(int[10],int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test6() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "ArrayCmp(int,unsigned char*,unsigned char*)", null,
						IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test7() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "f(int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test8() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "spec_f(int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test9() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "bsort(int*,int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test10() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "Fibonacci(int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test11() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "find_maximum(int[],int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test12() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "add_digits(int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test13() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "reverse(long)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test14() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "check_prime(int)",
						new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 83.33f), IFunctionConfig.SUBCONDITION,
						new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test15() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "check_armstrong(long long)",
				new EO(2, 80.0f), IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test16() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "power(int,int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test17() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "print_floyd(int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test18() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "find_minimum(int[],int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test19() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "linear_search1(long[],long,long)",
				null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test20() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "linear_search2(long*,long,long)",
				null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test21() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "bubble_sort(long[],long)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test22() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "concatenate(char[],char[])", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test23() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "concatenate_string(char*,char*)",
				null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test24() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "reverse2(char*,int,int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test25() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "copy_string(char*,char*)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test26() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "string_length(char*)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test27() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "compare_string(char*,char*)",
						new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 80.0f), IFunctionConfig.SUBCONDITION,
						new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test28() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "check_vowel(char)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test29() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "check_subsequence(char[],char[])",
				null, IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test30() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "find_frequency(char[],int[])", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test31() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "bsort(int*,int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test32() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "reverse_array(int*,int)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test33() throws LineUnavailableException {
		Assert.assertEquals(true, generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "string_length1(char*)", null,
				IFunctionConfig.SUBCONDITION, new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test34() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "merge(int[],int,int[],int,int[])",
						new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 75.0f), IFunctionConfig.SUBCONDITION,
						new FuntionTestReportGUI()));
	}

	@Test // PASS
	public void Vnu_Pratical_Test37() throws LineUnavailableException {
		Assert.assertEquals(true,
				generateTestdata(Paths.SYMBOLIC_EXECUTION_TEST, "check_anagram(char[],char[])",
						new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, 87.5f), IFunctionConfig.SUBCONDITION,
						new FuntionTestReportGUI()));
	}
}
