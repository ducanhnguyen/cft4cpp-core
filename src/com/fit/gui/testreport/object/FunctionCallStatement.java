package com.fit.gui.testreport.object;

/**
 * All statement need to call function success
 *
 * @author DucToan
 */
public class FunctionCallStatement {
    private String preCallFunction;
    private String callFunction;

    public FunctionCallStatement(String preCallFunction, String callFunction) {
        this.preCallFunction = preCallFunction;
        this.callFunction = callFunction;
    }

    public String getObjectCallFunction() {
        String object = "";
        if (!preCallFunction.equals("")) {
            object = callFunction.substring(callFunction.lastIndexOf("=") + 1, callFunction.lastIndexOf("."));
        }

        return object;
    }

    @Override
    public String toString() {
        return preCallFunction + callFunction;
    }

    public String getPreCallFunction() {
        return preCallFunction;
    }

    public void setPreCallFunction(String preCallFunction) {
        this.preCallFunction = preCallFunction;
    }

    public String getCallFunction() {
        return callFunction;
    }

    public void setCallFunction(String callFunction) {
        this.callFunction = callFunction;
    }
}
