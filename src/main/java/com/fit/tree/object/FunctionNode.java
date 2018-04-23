package com.fit.tree.object;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

import java.io.File;

public class FunctionNode extends AbstractFunctionNode {

    public static void main(String[] args) {
        ProjectParser parser = new ProjectParser(new File(Paths.TSDV_R1), null);
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "SimpleTest(RGBA)")
                .get(0);

        System.out.println(((FunctionNode) function).getPassingVariables());

    }

}
