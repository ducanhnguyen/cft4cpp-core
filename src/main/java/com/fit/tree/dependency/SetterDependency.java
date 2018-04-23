package com.fit.tree.dependency;

import com.fit.tree.object.INode;

public class SetterDependency extends Dependency {

    public SetterDependency(INode startArrow, INode endArrow) {
        this.startArrow = startArrow;
        this.endArrow = endArrow;
    }

}
