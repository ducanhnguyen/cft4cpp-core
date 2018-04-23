package com.fit.gui.testreport.object;

import java.util.ArrayList;
import java.util.List;

import com.fit.cfg.testpath.ITestpathInCFG;
import com.fit.googletest.ITestpathTestGeneration;
import com.fit.googletest.TestpathTestGeneration;
import com.fit.gui.testedfunctions.ExpectedOutputItem;
import com.fit.gui.testedfunctions.ManageSelectedFunctionsDisplayer;
import com.fit.testdata.object.TestpathString_Marker;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.Utils;
import com.fit.utils.VariableTypesUtils;

/**
 * Represent a test path
 *
 * @author DucAnh
 */
public class TestpathReport implements INameRule, ITestpathReport {

	protected IFunctionNode functionNode;

	protected String staticInput = "";
	protected String normalizedTestpath = "";
	protected IInputReport input = new InputReport();
	protected IOutputReport expectedOutput = new OutputReport();
	protected IOutputReport actualOutput = new OutputReport();
	protected String pass = "";
	protected boolean isStoped = false;
	private IIdentificationForTestPath identification = new IdentificationForTestPath();
	private TestpathString_Marker executionTestpath = new TestpathString_Marker();

	public TestpathReport() {
	}

	public TestpathReport(TestpathString_Marker executionTestpath, String normlizeTestpath, IInputReport input,
			IOutputReport expectedOutput, IOutputReport actualOutput, String pass, IFunctionNode functionNode) {
		normalizedTestpath = normlizeTestpath;
		this.executionTestpath = executionTestpath;
		this.input = input;
		this.expectedOutput = expectedOutput;
		this.pass = pass;
		this.actualOutput = actualOutput;
		this.functionNode = functionNode;
	}

	@Override
	public String getTestdata() {
		return staticInput;
	}

	@Override
	public void setTestdata(String staticInput) {
		this.staticInput = staticInput;
	}

	private String deleteBracket(String testpath) {
		testpath = testpath.replace(ITestpathInCFG.SEPARATE_BETWEEN_NODES + " } ", "");
		testpath = testpath.replace(ITestpathInCFG.SEPARATE_BETWEEN_NODES + " { ", "");
		testpath = testpath.replace("{ " + ITestpathInCFG.SEPARATE_BETWEEN_NODES + " ", "");
		testpath = testpath.replace(" " + ITestpathInCFG.SEPARATE_BETWEEN_NODES + " End", "");
		testpath = testpath.replace(" " + ITestpathInCFG.SEPARATE_BETWEEN_NODES + " ",
				ITestpathInCFG.SEPARATE_BETWEEN_NODES);
		return testpath;
	}

	@Override
	public IOutputReport getActualOutput() {
		return actualOutput;
	}

	@Override
	public void setActualOutput(IOutputReport actualOutput) {
		this.actualOutput = actualOutput;
	}

	@Override
	public IOutputReport getExpectedOutput() {
		return expectedOutput;
	}

	@Override
	public void setExpectedOutput(IOutputReport expectedOutput) {
		this.expectedOutput = expectedOutput;
	}

	@Override
	public IFunctionNode getFunctionNode() {
		return functionNode;
	}

	@Override
	public void setFunctionNode(IFunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	@Override
	public IIdentificationForTestPath getIdentification() {
		return identification;
	}

	@Override
	public void setIdentification(IIdentificationForTestPath identification) {
		this.identification = identification;
	}

	@Override
	public IInputReport getInput() {
		return input;
	}

	@Override
	public void setInput(IInputReport input) {
		this.input = input;
	}

	@Override
	public String getPass() {
		return pass;
	}

	@Override
	public void setPass(String pass) {
		this.pass = pass;
	}

	@Override
	public String getNormalizedTestpath() {
		return normalizedTestpath;
	}

	@Override
	public void setNormalizedTestpath(String testpath) {
		normalizedTestpath = deleteBracket(testpath);
		ManageSelectedFunctionsDisplayer.getInstance().refresh();
	}

	@Override
	public String getShortenTestpath() {
		return deleteBracket(normalizedTestpath);
	}

	@Override
	public boolean isCanBeExportToUnitTest() {
		return getExpectedOutput().getExpectedValues().getData().size() > 0;
	}

	@Override
	public boolean isStoped() {
		return isStoped;
	}

	@Override
	public void setStoped(boolean isStoped) {
		this.isStoped = isStoped;
	}

	@Override
	public void setRawVariables(List<String> variable) {
		ManageSelectedFunctionsDisplayer.getInstance().refresh();
	}

	@Override
	public void setRawVariables(String variable) {
		List<String> tmp = new ArrayList<>();
		tmp.add(variable);
		this.setRawVariables(tmp);
	}

	@Override
	public String toString() {
		String output = "";
		output += "Testpath: " + normalizedTestpath + "\nInput: " + input;
		return output;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TestpathReport) {
			ITestpathReport tp = (ITestpathReport) obj;
			if (tp.getNormalizedTestpath().equals(getNormalizedTestpath()))
				return true;
			else
				return false;
		} else
			return false;
	}

	@Override
	public TestpathString_Marker getExecutionTestpath() {
		return executionTestpath;
	}

	@Override
	public void setExecutionTestpath(TestpathString_Marker executionTestpath) {
		this.executionTestpath = executionTestpath;
	}

	@Override
	public FunctionCallStatement generateFunctionCall() {
		String functionCall = "";
		String preFunctionCall = "";

		String fullName = getFunctionNode().getFullName();

		if (VariableTypes.isNotReturn(getFunctionNode().getReturnType())) {
			/*
			 * If the function does not return value
			 */
			if (getFunctionNode().isStaticFunction())
				// Khai báo hàm thật sự chứa "static"
				functionCall = fullName + SpecialCharacter.END_OF_STATEMENT;

			else if (fullName.contains(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS)) {
				String scope = fullName.substring(0,
						fullName.lastIndexOf(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS));
				String name = fullName.replace(scope + SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS, "");

				String nameInstance = "_"
						+ scope.replace(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS, "_").toLowerCase();
				preFunctionCall += scope + " " + nameInstance + SpecialCharacter.END_OF_STATEMENT;

				functionCall += nameInstance + "." + name + SpecialCharacter.END_OF_STATEMENT;
			} else
				functionCall += fullName + SpecialCharacter.END_OF_STATEMENT;
		} else {
			/*
			 * If the function returns value
			 */
			String nameInstance = "";
			if (getFunctionNode().isStaticFunction())
				functionCall += getFunctionNode().getReturnType() + " " + IGoogleTestNameRule.AO_ACTUAL_OUTPUT_VARIABLE
						+ "=" + fullName + SpecialCharacter.END_OF_STATEMENT;

			else if (fullName.contains(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS)) {
				String scope = fullName.substring(0,
						fullName.lastIndexOf(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS));
				String call = fullName.replace(scope + SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS, "");
				nameInstance = "_" + scope.replace(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS, "_").toLowerCase();

				preFunctionCall += scope + " " + nameInstance + SpecialCharacter.END_OF_STATEMENT;

				functionCall += getFunctionNode().getReturnType() + " " + IGoogleTestNameRule.AO_ACTUAL_OUTPUT_VARIABLE
						+ "=" + nameInstance + "." + call + SpecialCharacter.END_OF_STATEMENT;

			} else
				/*
				 * If the function return basic variable, e.g., int, char or Enum
				 */
				functionCall += getFunctionNode().getReturnType() + " " + IGoogleTestNameRule.AO_ACTUAL_OUTPUT_VARIABLE
						+ "=" + fullName + SpecialCharacter.END_OF_STATEMENT;
		}

		return new FunctionCallStatement(preFunctionCall, functionCall);
	}

	/**
	 * Compare Expected Output and Actual Output
	 */
	@Override
	public String createEXPEC_EQ() {
		String comparision = "";

		comparision += SpecialCharacter.LINE_BREAK;
		for (ExpectedOutputItem item : getExpectedOutput().getExpectedValues().getData()) {
			String originalNameVar = item.getNameVar()
					.replaceFirst(Utils.toRegex(IGoogleTestNameRule.EO_EXPECTED_OUTPUT_PREFIX_IN_SOURCECODE), "");

			if (VariableTypes.isBasic(item.getTypeVar()) || VariableTypesUtils.isEnumNode(item.getTypeVar())
					|| VariableTypesUtils.isDefineNodeOfBasicType(item.getTypeVar())) {
				comparision += SpecialCharacter.TAB + "EXPECT_EQ(" + item.getNameVar() + "," + originalNameVar + ")"
						+ SpecialCharacter.END_OF_STATEMENT + SpecialCharacter.LINE_BREAK;

			} else if (VariableTypes.isOneLevel(item.getTypeVar()) || VariableTypes.isTwoLevel(item.getTypeVar())) {
				/*
				 * The expected output of pointer variable is NULL
				 */

				if (item.getValueVar().equals("NULL"))
					comparision += SpecialCharacter.TAB + "EXPECT_EQ(" + item.getNameVar() + ", " + originalNameVar
							+ ")" + SpecialCharacter.END_OF_STATEMENT + SpecialCharacter.LINE_BREAK;
			}
		}

		return comparision;
	}

	@Override
	public String generateUnitTest(String nameTestcase) {
		/*
		 * Create name test suite
		 */
		int orderOfFunctionInFile = getFunctionNode().getParent().getChildren().indexOf(getFunctionNode());
		String nameTestSuite = getFunctionNode().getSimpleName().replace("::", "_") + orderOfFunctionInFile;

		/*
		 * Save information test suite and test case
		 */
		setIdentification(new IdentificationForTestPath(nameTestSuite, nameTestcase));

		/*
		 * Build source test for test path in function
		 */
		ITestpathTestGeneration testpathGen = new TestpathTestGeneration(this, getFunctionNode(), nameTestcase,
				nameTestSuite);
		testpathGen.generateUnitTest();
		return testpathGen.getTestpathSourcecode();
	}

}
