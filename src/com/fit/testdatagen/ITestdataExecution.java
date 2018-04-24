package com.fit.testdatagen;

import com.fit.testdata.object.TestpathString_Marker;
import com.fit.testdatagen.module.IDataTreeGeneration;
import com.fit.testdatagen.structuregen.ChangedTokens;

import interfaces.IGeneration;

/**
 * Executing function under test data to get test path
 *
 * @author DucAnh
 */
public interface ITestdataExecution extends IGeneration {

	String UNDEFINED_SOLUTION = "";
	String UNDEFINED_TESTPATH = "";

	IDataTreeGeneration getDataGen();

	TestpathString_Marker getEncodedTestpath();

	void setEncodedTestpath(TestpathString_Marker testpath);

	String normalizeTestpathFromFile(String testpath);

	String getInitialization();

	void setInitialization(String initialization);

	ChangedTokens getChangedTokens();

	void setChangedTokens(ChangedTokens changedTokens);

}