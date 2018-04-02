package com.fit.googletest;

import interfaces.IGeneration;

import java.io.IOException;
import java.util.List;

public interface ISourcecodeTestGeneration extends IGeneration {

    /**
     * Build source code test for all functions in project
     *
     * @throws Exception
     */
    void generateTestpathSourcecode();

    /**
     * Get name of all unit tests files
     *
     * @throws IOException
     */
    List<String> getNameUnitTestFiles() throws IOException;

    List<ISourceCodeGoogleTest> getSourcecodeTest();

    void setSourcecodeTest(List<ISourceCodeGoogleTest> sourcecodeTest);

}