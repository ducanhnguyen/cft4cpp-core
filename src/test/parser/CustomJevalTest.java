package test.parser;

import javax.sound.sampled.LineUnavailableException;

import org.junit.Assert;
import org.junit.Test;

import com.fit.utils.Utils;

import test.testdatageneration.AbstractJUnitTest;

public class CustomJevalTest extends AbstractJUnitTest {
	@Test
	public void test1() throws LineUnavailableException {
		Assert.assertEquals("1", Utils.transformFloatNegativeE("1E-0"));
	}

	@Test
	public void test2() throws LineUnavailableException {
		Assert.assertEquals("0.1", Utils.transformFloatNegativeE("1E-1"));
	}

	@Test
	public void test3() throws LineUnavailableException {
		Assert.assertEquals("0.01", Utils.transformFloatNegativeE("1E-2"));
	}

	@Test
	public void test4() throws LineUnavailableException {
		Assert.assertEquals("12.3", Utils.transformFloatNegativeE("123E-1"));
	}

	@Test
	public void test5() throws LineUnavailableException {
		Assert.assertEquals("123", Utils.transformFloatNegativeE("123E-0"));
	}

	@Test
	public void test6() throws LineUnavailableException {
		Assert.assertEquals("12.3", Utils.transformFloatNegativeE("123E-1"));
	}

	@Test
	public void test7() throws LineUnavailableException {
		Assert.assertEquals("0.0123", Utils.transformFloatNegativeE("123E-4"));
	}

	@Test
	public void test1_() throws LineUnavailableException {
		Assert.assertEquals("1", Utils.transformFloatPositiveE("1E+0"));
	}

	@Test
	public void test2_() throws LineUnavailableException {
		Assert.assertEquals("10", Utils.transformFloatPositiveE("1E+1"));
	}

	@Test
	public void test3_() throws LineUnavailableException {
		Assert.assertEquals("100", Utils.transformFloatPositiveE("1E+2"));
	}

	@Test
	public void test4_() throws LineUnavailableException {
		Assert.assertEquals("1230", Utils.transformFloatPositiveE("123E+1"));
	}

	@Test
	public void test5_() throws LineUnavailableException {
		Assert.assertEquals("123", Utils.transformFloatPositiveE("123E+0"));
	}

	@Test
	public void test6_() throws LineUnavailableException {
		Assert.assertEquals("123000", Utils.transformFloatPositiveE("123E+3"));
	}
}
