package com.fit.instrument;

import com.fit.tree.object.IFunctionNode;
import interfaces.IGeneration;

/**
 * Instrument function
 *
 * @author DucAnh
 */
public interface IFunctionInstrumentationGeneration extends IGeneration {
    /**
     * Generate instrumented source code of a function
     *
     * @return
     */
    String generateInstrumentedFunction();

    /**
     * Get the function node
     *
     * @return
     */
    IFunctionNode getFunctionNode();

    /**
     * Set the function node
     *
     * @param functionNode
     */
    void setFunctionNode(IFunctionNode functionNode);

    /**
     * @return true when the function is normalized before performing
     * instrumentation
     */
    boolean isNormalizedMode();

    /**
     * Set the normalization mode.
     *
     * @param normalizedMode true if the function is normalized before performing
     *                       instrumentation
     */
    void setNormalizedMode(boolean normalizedMode);
}
