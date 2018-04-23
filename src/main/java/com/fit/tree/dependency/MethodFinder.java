package com.fit.tree.dependency;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.*;
import com.fit.utils.search.AbstractFunctionNodeCondition;
import com.fit.utils.search.DefinitionFunctionNodeCondition;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

import java.io.File;
import java.util.List;

/**
 * Find method
 *
 * @author DucAnh
 */
public class MethodFinder {

    /**
     * Node in the structure that contains the searched function
     */
    private IFunctionNode context;

    public MethodFinder(IFunctionNode context) {
        this.context = context;
    }

    public static void main(String[] args) throws Exception {
        ProjectParser parser = new ProjectParser(new File(Paths.SEPARATE_FUNCTION_TEST), null);
        FunctionNode context = (FunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), File.separator + "test5()").get(0);

        MethodFinder finder = new MethodFinder(context);
        finder.find("sub", 1);
    }

    public INode find(String simpleFunctionName, int paramater) throws Exception {
        List<Level> spaces = new VariableSearchingSpace(context).getSpaces();
        for (Level l : spaces)
            for (INode n : l) {

                List<INode> completeFunctions = Search.searchNodes(n, new AbstractFunctionNodeCondition());
                for (INode function : completeFunctions)
                    if (((AbstractFunctionNode) function).getSimpleName().equals(simpleFunctionName))
                        if (((AbstractFunctionNode) function).getArguments().size() == paramater)
                            return function;

                List<INode> onlyDefinedFunction = Search.searchNodes(n, new DefinitionFunctionNodeCondition());
                for (INode function : onlyDefinedFunction)
                    if (((DefinitionFunctionNode) function).getSimpleName().equals(simpleFunctionName))
                        if (((DefinitionFunctionNode) function).getArguments().size() == paramater)
                            return function;
            }
        return null;
    }
}
