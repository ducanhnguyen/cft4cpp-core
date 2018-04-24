package com.fit.googletest.xmlparser;

/**
 * Represent a test case that store in .xml of google test project execution
 *
 * @author ducanhnguyen
 */
public interface IGoogleTestTestcase {

    boolean getResult();

    String getTestCase();

    String getTestSuite();
}