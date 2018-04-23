package com.fit.gui.testedfunctions;

import com.fit.gui.testreport.object.IGoogleTestNameRule;
import com.fit.testdatagen.module.DataTreeGeneration;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.IVariableNode;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of expected output data item
 *
 * @author ducanhnguyen
 */
public class ExpectedOutputData {
    private List<ExpectedOutputItem> data = new ArrayList<>();

    public ExpectedOutputData() {

    }

    public List<ExpectedOutputItem> getData() {
        return data;
    }

    public void setData(List<ExpectedOutputItem> data) {
        this.data = data;
    }

    public void add(ExpectedOutputItem item) {
        data.add(item);
    }

    public ExceptedExceptionOutputItem containExceptionasExpectedOutput() {
        for (ExpectedOutputItem item : data)
            if (item instanceof ExceptedExceptionOutputItem)
                return (ExceptedExceptionOutputItem) item;
        return null;
    }

    /**
     * Get input to put into the unit test
     *
     * @param function
     * @return
     */
    public String getInputforGoogleTest(IFunctionNode function) {
        String inputForGoogleTest = "";

		/*
         * Generate complete declaration based on expected value
		 */
        DataTreeGeneration dataTree = new DataTreeGeneration();

        // create a variable node represent the return variable
        IFunctionNode clone = (IFunctionNode) function.clone();
        String newFunctionContent = generateNewFunctionContent(function);
        clone.setAST(Utils.getFunctionsinAST(newFunctionContent.toCharArray()).get(0));
		/*
		 * 
		 */
        dataTree.setFunctionNode(clone);

        Map<String, Object> staticSolution = new HashMap<>();
        for (ExpectedOutputItem item : data)
            staticSolution.put(item.getNameVar(), item.getValueVar());

        dataTree.setStaticSolution(staticSolution);
        try {
            dataTree.generateTree();
            inputForGoogleTest = dataTree.getInputforGoogleTest();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // restore function in the test path
        }

        return inputForGoogleTest;
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

    /**
     * Get input to display in UI
     *
     * @return
     */
    public String getInputforUI() {
        String inputForUI = "";
        for (ExpectedOutputItem item : data)
        /**
         * Show all
         */
//            if (VariableTypes.isBasic(item.getTypeVar()) || item.getValueVar().equals("NULL")
//                    || VariableTypes.isThrow(item.getTypeVar()))
                inputForUI += item.getNameVar() + "=" + item.getValueVar() + SpecialCharacter.END_OF_STATEMENT
                        + SpecialCharacter.LINE_BREAK;
        return inputForUI;
    }
}
