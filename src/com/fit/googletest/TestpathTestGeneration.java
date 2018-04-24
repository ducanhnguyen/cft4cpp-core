package com.fit.googletest;

import java.util.ArrayList;
import java.util.List;

import com.fit.gui.testedfunctions.ExceptedExceptionOutputItem;
import com.fit.gui.testreport.object.FunctionCallStatement;
import com.fit.gui.testreport.object.IGoogleTestNameRule;
import com.fit.gui.testreport.object.ITestpathReport;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.object.AttributeOfStructureVariableNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.IVariableNode;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.Utils;

/**
 * This class generate function test follow standard Google Test for test path
 *
 * @author ducanh
 */
public class TestpathTestGeneration implements ITestpathTestGeneration {

    /**
     * Represent report test case of test path
     */
    private String testpathSourcecode = "";

    private ITestpathReport testpathReport;

    private IFunctionNode functionNode;

    /**
     * Represent name of test case
     */
    private String nameTestcase;

    /**
     * Represent name of test suite
     */
    private String nameTestSuite;

    /**
     * @param testpath     include information : input ,expected output of test path
     * @param functionNode function need test
     * @param nameTestcase name of test case
     */
    public TestpathTestGeneration(ITestpathReport testpath, IFunctionNode functionNode, String nameTestcase,
                                  String nameTestSuite) {
        testpathReport = testpath;
        this.functionNode = functionNode;
        this.nameTestcase = nameTestcase;
        this.nameTestSuite = nameTestSuite;
    }

    @Override
    public void generateUnitTest() {
        String input = "", expected = "", functionCall = "", comparision = "";

		/*
         * Create name of function
		 */
        String nameFunction = "TEST(" + nameTestSuite + ", " + nameTestcase + ") {" + SpecialCharacter.LINE_BREAK;

		/*
         * Generate input
		 */
        for (String item : testpathReport.getInput().getVariablesForGTest())
            if (item.trim().endsWith(";"))
                input += SpecialCharacter.TAB + item.trim() + SpecialCharacter.LINE_BREAK;
            else
                input += SpecialCharacter.TAB + item.trim() + ";" + SpecialCharacter.LINE_BREAK;
        input += SpecialCharacter.LINE_BREAK;

		/*
		 * Generate expected output
		 */
        List<IVariableNode> expectedOutputSet = testpathReport.getFunctionNode().getExpectedNodeTypes();
        if (expectedOutputSet.size() > 0) {
            ExceptedExceptionOutputItem exceptionItem = testpathReport.getExpectedOutput().getExpectedValues()
                    .containExceptionasExpectedOutput();
            if (exceptionItem == null) {
				/*
				 * Generate EO
				 */
                expected = SpecialCharacter.TAB + testpathReport.getExpectedOutput().getExpectedValues()
                        .getInputforGoogleTest(getFunctionNode()) + SpecialCharacter.LINE_BREAK;

				/*
				 * Generate function call
				 */

                FunctionCallStatement functionCallStatement = testpathReport.generateFunctionCall();

                functionCall += SpecialCharacter.TAB + functionCallStatement.getPreCallFunction()
                        + SpecialCharacter.LINE_BREAK;

                // Nguy cơ có thể có lỗi ở đoạn này mà chưa biết phòng ra sao :v
                List<String> variableCompare = new ArrayList<>();
                for (IVariableNode item : functionNode.getReducedExternalVariables()) {
                    if (item instanceof AttributeOfStructureVariableNode) {
                        if (item.getSetterNode() != null)
                            functionCall += SpecialCharacter.TAB + functionCallStatement.getObjectCallFunction() + "."
                                    + item.getSetterNode().getSimpleName() + "(" + item.getNewType() + ")"
                                    + SpecialCharacter.END_OF_STATEMENT + SpecialCharacter.LINE_BREAK;
                        else
                            functionCall += SpecialCharacter.TAB + functionCallStatement.getObjectCallFunction() + "."
                                    + item.getName() + "=" + item.getNewType()
                                    + SpecialCharacter.END_OF_STATEMENT + SpecialCharacter.LINE_BREAK;
                        variableCompare.add(item.getNewType());
                    }
                }

                functionCall += SpecialCharacter.TAB + functionCallStatement.getCallFunction()
                        + SpecialCharacter.LINE_BREAK;

				/*
				 * Generate comparison
				 */
                IFunctionNode originalFunctionNode = getFunctionNode();
                testpathReport.setFunctionNode(cloneFunctionNode(getFunctionNode()));
                comparision += SpecialCharacter.TAB + testpathReport.createEXPEC_EQ() + SpecialCharacter.LINE_BREAK;
                comparision = normalizeComparision(comparision, variableCompare,
                        functionCallStatement.getObjectCallFunction());
                testpathReport.setFunctionNode(originalFunctionNode);
            } else {
                FunctionCallStatement functionCallStatement = testpathReport.generateFunctionCall();

                comparision += SpecialCharacter.TAB + functionCallStatement.getPreCallFunction();
                comparision += SpecialCharacter.TAB + "ASSERT_THROW("
                        + functionCallStatement.getCallFunction().substring(
                        functionCallStatement.getCallFunction().lastIndexOf("=") + 1,
                        functionCallStatement.getCallFunction().length() - 1)
                        + "," + exceptionItem.getValueVar() + ")" + SpecialCharacter.END_OF_STATEMENT
                        + SpecialCharacter.LINE_BREAK;
            }

        } else
            functionCall = SpecialCharacter.TAB + testpathReport.generateFunctionCall() + SpecialCharacter.LINE_BREAK;

		/*
		 * Join all into one
		 */
        String test = nameFunction + input + expected + functionCall + comparision + "}";
        testpathSourcecode = test + SpecialCharacter.LINE_BREAK + SpecialCharacter.LINE_BREAK;
    }

    private String normalizeComparision(String comparision, List<String> variableCompare, String objectToCallFunction) {
        String newComparision = comparision;
        for (String var : variableCompare) {
            newComparision = newComparision.replace("," + var,
                    "," + objectToCallFunction + ".get" + var.toUpperCase() + "()");
        }
        return newComparision;
    }

    private IFunctionNode cloneFunctionNode(IFunctionNode function) {
        IFunctionNode clone = (IFunctionNode) function.clone();
        String newFunctionContent = generateNewFunctionContent(function);
        clone.setAST(Utils.getFunctionsinAST(newFunctionContent.toCharArray()).get(0));
        return clone;
    }

    private String generateNewFunctionContent(IFunctionNode function) {
        String newFunctionContent = function.getReturnType() + " " + function.getSimpleName() + "(";

        List<String> variables = new ArrayList<>();
        for (IVariableNode var : function.getArguments())
            if (VariableTypes.isOneDimension(var.getRawType()) || VariableTypes.isTwoDimension(var.getRawType())) {

                int arrayPosition = var.getRawType().indexOf("[");

                String newType = var.getRawType().substring(0, arrayPosition);
                String newVarName = IGoogleTestNameRule.EO_EXPECTED_OUTPUT_PREFIX_IN_SOURCECODE + var.getNewType()
                        + var.getRawType().substring(arrayPosition);
                variables.add(newType + " " + newVarName);

            } else {
                String newVarName = IGoogleTestNameRule.EO_EXPECTED_OUTPUT_PREFIX_IN_SOURCECODE + var.getNewType();
                variables.add(var.getRawType() + " " + newVarName);
            }

        for (IVariableNode var : function.getExternalVariables()) {
            String newVarName = IGoogleTestNameRule.EO_EXPECTED_OUTPUT_PREFIX_IN_SOURCECODE + var.getNewType();
            variables.add(var.getRawType() + " " + newVarName);
        }

        if (VariableTypes.isVoid(function.getReturnType())) {
            // nothing to do
        } else
            variables.add(function.getReturnType() + " " + IGoogleTestNameRule.EO_RETURN_VARIABLE);

        for (String var : variables)
            newFunctionContent += var + ",";
        newFunctionContent = newFunctionContent.substring(0, newFunctionContent.length() - 1) + "){}";

        return newFunctionContent;
    }

    @Override
    public String getTestpathSourcecode() {
        return testpathSourcecode;
    }

    @Override
    public void setTestpathSourcecode(String testpathSourcecode) {
        this.testpathSourcecode = testpathSourcecode;
    }

    @Override
    public IFunctionNode getFunctionNode() {
        return functionNode;
    }

    @Override
    public void setFunctionNode(IFunctionNode functionNode) {
        this.functionNode = functionNode;
    }
}
