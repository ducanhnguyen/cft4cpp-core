package com.fit.tree.object;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.utils.search.Search;
import com.fit.utils.search.StructNodeCondition;

import java.io.File;

/**
 * Represent a constructor
 *
 * @author DucAnh
 */
public class ConstructorNode extends AbstractFunctionNode {

    public static void main(String[] args) {
        ProjectParser parser = new ProjectParser(new File(Paths.SAMPLE01), null);
        StructureNode structureNode = (StructureNode) Search
                .searchNodes(parser.getRootTree(), new StructNodeCondition(), "Node").get(0);

        structureNode.getConstructors().get(0);
    }

}
