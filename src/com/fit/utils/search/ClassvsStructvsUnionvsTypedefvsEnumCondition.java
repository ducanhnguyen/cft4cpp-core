package com.fit.utils.search;

import com.fit.tree.object.ClassNode;
import com.fit.tree.object.EnumNode;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.SingleTypedefDeclaration;
import com.fit.tree.object.StructNode;
import com.fit.tree.object.UnionNode;
import com.fit.tree.object.VariableNode;

public class ClassvsStructvsUnionvsTypedefvsEnumCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof ClassNode || n instanceof StructNode || n instanceof SingleTypedefDeclaration
                || n instanceof UnionNode || n instanceof EnumNode)
            return true;
        else if (n instanceof VariableNode && !(n.getParent() instanceof FunctionNode))
            return true;
        return false;
    }
}
