package com.fit.gui.testreport.object;

public class IdentificationForTestPath implements IIdentificationForTestPath {

    private String testSuite;
    private String testCase;

    public IdentificationForTestPath() {

    }

    public IdentificationForTestPath(String _testSuite, String _testCase) {
        testSuite = _testSuite;
        testCase = _testCase;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.fit.gui.testreport.object.IIdentificationForTestPath#getTestCase()
     */
    @Override
    public String getTestCase() {
        return testCase;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.fit.gui.testreport.object.IIdentificationForTestPath#setTestCase(java
     * .lang.String)
     */
    @Override
    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.fit.gui.testreport.object.IIdentificationForTestPath#getTestSuite()
     */
    @Override
    public String getTestSuite() {
        return testSuite;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.fit.gui.testreport.object.IIdentificationForTestPath#setTestSuite(
     * java.lang.String)
     */
    @Override
    public void setTestSuite(String testSuite) {
        this.testSuite = testSuite;
    }
}
