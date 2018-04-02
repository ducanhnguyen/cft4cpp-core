package com.fit.googletest.xmlparser;

/*
 * Đại diện cho một test case có 3 thuộc tính testSuite: 1 nhóm các
 * testcase cùng test một chức năng testCase: tên testcase result: pass hay
 * fail
 */
public class Testcase implements IGoogleTestTestcase {

    private String testSuite;
    private String testCase;
    private boolean result;

    /**
     * @param testSuite
     * @param testCase
     * @param result
     */
    public Testcase(String testSuite, String testCase, boolean result) {
        this.testSuite = testSuite;
        this.testCase = testCase;
        this.result = result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.fit.xmlparser.ITestcase#getResult()
     */
    @Override
    public boolean getResult() {
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.fit.xmlparser.ITestcase#getTestCase()
     */
    @Override
    public String getTestCase() {
        return testCase;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.fit.xmlparser.ITestcase#getTestSuite()
     */
    @Override
    public String getTestSuite() {
        return testSuite;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.fit.xmlparser.ITestcase#toString()
     */
    @Override
    public String toString() {
        if (result)
            return testSuite + "\t\t" + testCase + "\t\t" + "PASS";
        else
            return testSuite + "\t\t" + testCase + "\t\t" + "FAIL";
    }
}
