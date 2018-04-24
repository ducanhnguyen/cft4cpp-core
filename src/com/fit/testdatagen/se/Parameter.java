package com.fit.testdatagen.se;

import com.fit.tree.object.INode;
import com.fit.tree.object.Node;
import com.fit.tree.object.VariableNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the paramaters of a function including the arguments + external
 * variables
 *
 * @author ducanhnguyen
 */
public class Parameter extends ArrayList<INode> {

    /**
     *
     */
    private static final long serialVersionUID = -2583457982870539611L;

    public Parameter() {
    }

    public Parameter(List<VariableNode> paramaters) {
        for (Node var : paramaters)
            this.add(var);
    }
}
