package com.fit.testdatagen.fast;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({ FastTestdataGenerationTest_complex.class, FastTestdataGenerationTest_class.class,
		FastTestdataGenerationTest_struct.class, FastTestdataGenerationTest_basic.class })
/**
 * Three kinds of test: <br/>
 * 1. PASS <br/>
 * 2. HALF-PASS: the functions in these kind of tests do not reach the maximize
 * coverage, or the test data generation time is too long<br/>
 * 3. Fail
 * 
 * @author ducanhnguyen
 *
 */
public class FastTestdataGenerationTest {
}