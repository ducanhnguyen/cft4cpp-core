package com.fit.tree.dependency;

import com.fit.tree.object.INode;

import java.util.ArrayList;
import java.util.List;

/**
 * A level represents a list of equivalent
 *
 * @author DucAnh
 */
public class Level extends ArrayList<INode> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Level() {

    }

    public Level(List<INode> node) {
        this.addAll(node);
    }

    @Override
    public String toString() {
        String output = "";
        for (INode n : this)
            output += n.getAbsolutePath() + ", ";
        return output;
    }
}
