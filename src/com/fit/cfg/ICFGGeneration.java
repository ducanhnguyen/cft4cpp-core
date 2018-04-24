package com.fit.cfg;

import com.fit.tree.object.IFunctionNode;
import interfaces.IGeneration;

/**
 * This interface is used to generate CFG
 *
 * @author ducanh
 */
public interface ICFGGeneration extends IGeneration {
    final static int IF_FLAG = 0;

    final static int DO_FLAG = 1;

    final static int WHILE_FLAG = 2;

    final static int FOR_FLAG = 3;

    final static int SEPARATE_FOR_INTO_SEVERAL_NODES = 1;

    final static int DONOT_SEPARATE_FOR = 0;

    /**
     * Generate the control flow graph corresponding to the given function
     *
     * @return
     */
    ICFG generateCFG() throws Exception;

    /**
     * Get type of For block (for (...;...;...)) analysis. In CFg generation,
     * For block may be a node, or a set of nodes
     *
     * @return
     */
    int getForModel();

    void setForModel(int forModel);

    IFunctionNode getFunctionNode();

    void setFunctionNode(IFunctionNode functionNode);

}
