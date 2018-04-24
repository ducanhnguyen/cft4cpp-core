package com.fit.testdatagen;

import com.fit.tree.object.INode;

public class TestdataGenerationThread extends Thread {

    private INode functionNode;

    public TestdataGenerationThread(Runnable target) {
        super(target);
    }

    public INode getFunctionNode() {
        return functionNode;
    }

    public void setFunctionNode(INode functionNode) {
        this.functionNode = functionNode;
    }
}
