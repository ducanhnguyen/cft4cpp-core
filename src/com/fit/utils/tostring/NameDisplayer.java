package com.fit.utils.tostring;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectLoader;
import com.fit.testdatagen.se.expression.ExpressionNode;
import com.fit.tree.object.*;
import com.fit.utils.search.CppFileNodeCondition;
import com.fit.utils.search.Search;

import java.io.File;

public class NameDisplayer extends ToString {

    public NameDisplayer(INode root) {
        super(root);
    }

    public static void main(String[] args) {
        // Project tree generation
        IProjectNode projectRootNode = new ProjectLoader().load(new File(Paths.TSDV_R1));

        // display tree of project
        ToString treeDisplayer = new NameDisplayer(projectRootNode);
        System.out.println(treeDisplayer.getTreeInString());
    }

    private void displayTree(INode n, int level) {
        if (n == null || n instanceof FolderNode && Search.searchNodes(n, new CppFileNodeCondition()).size() == 0)
            return;
        else {
            if (n instanceof ExpressionNode)
                treeInString += genTab(level) + "[" + n.getClass().getSimpleName() + "] " + n.getNewType() + "	\tvalue="
                        + ((ExpressionNode) n).getValue() + "\n";
            else if (n instanceof VariableNode)
                treeInString += genTab(level) + "[" + n.getClass().getSimpleName() + "] " + n.getNewType()
                        + "\t;reduced type=" + ((IVariableNode) n).getReducedRawType() + "\t; raw type="
                        + ((IVariableNode) n).getRawType() + "\n";
            else
                treeInString += genTab(level) + "[" + n.getClass().getSimpleName() + "] " + n.getNewType() + "\n";
            if (n instanceof AbstractFunctionNode)
                treeInString += genTab(level + 1) + "real parent: "
                        + ((AbstractFunctionNode) n).getRealParent().getAbsolutePath() + "\n";
            treeInString += genTab(level + 1) + "id: " + n.getId() + "\n";
            treeInString += genTab(level + 1) + n.getAbsolutePath() + "\n";
        }
        for (Object child : n.getChildren()) {
            displayTree((Node) child, ++level);
            level--;
        }
    }

    @Override
    public String toString(INode n) {
        displayTree(n, 0);
        return treeInString;
    }
}
