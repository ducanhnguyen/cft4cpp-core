package com.fit.tree.dependency;

import com.fit.tree.object.INode;

public class IncludeHeaderDependency extends Dependency {

    public IncludeHeaderDependency(INode startArrow, INode endArrow) {
        this.startArrow = startArrow;
        this.endArrow = endArrow;
    }

}
