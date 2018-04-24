package com.fit.googletest;

import com.fit.tree.object.IFunctionNode;
import interfaces.IGeneration;

/**
 * This class is used to generate function test following the standard format of
 * Google Test framework
 *
 * @author ducanh
 */
public interface ITestpathTestGeneration extends IGeneration {
    /**
     * Generate test path source code
     */
    void generateUnitTest();

    String getTestpathSourcecode();

    void setTestpathSourcecode(String testpathSourcecode);

    IFunctionNode getFunctionNode();

    void setFunctionNode(IFunctionNode functionNode);

}