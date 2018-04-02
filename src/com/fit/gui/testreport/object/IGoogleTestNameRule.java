package com.fit.gui.testreport.object;

public interface IGoogleTestNameRule {

    /**
     * Represent the return value of the function under testing
     */
    public static final String AO_ACTUAL_OUTPUT_VARIABLE = "returnVar";

    /**
     * The prefix of the expected variable when export to source code. Ex: EO_x
     */
    public static final String EO_EXPECTED_OUTPUT_PREFIX_IN_SOURCECODE = "EO_";
    public static final String EO_RETURN_VARIABLE_IN_SOURCECODE = "returnVar";

    /**
     * The prefix of the expected variable when display in GUI
     */
    public static final String EO_EXPECTED_OUTPUT_PREFIX_IN_GUI = "";
    public static final String EO_RETURN_VARIABLE_IN_GUI = IGoogleTestNameRule.EO_RETURN_VARIABLE_IN_SOURCECODE;

    /**
     * Represent the return value of the tested function
     */
    public static final String EO_RETURN_VARIABLE = "EO_returnVar";
}
