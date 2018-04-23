package com.fit.tree.dependency;

import com.fit.tree.object.INode;

public class GetterDependency extends Dependency {

    public GetterDependency(INode startArrow, INode endArrow) {
        this.startArrow = startArrow;
        this.endArrow = endArrow;
    }

}
