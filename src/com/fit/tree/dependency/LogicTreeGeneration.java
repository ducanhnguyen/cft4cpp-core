package com.fit.tree.dependency;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.*;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

import java.io.File;

/**
 * Generate logic tree for a function
 *
 * @author ducanhnguyen
 */
public class LogicTreeGeneration {
    private IFunctionNode functionNode;
    private INode rootTree = new ProjectNode();

    public LogicTreeGeneration() {
    }

    public static void main(String[] args) throws Exception {
        ProjectParser projectParser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST), null);
        IProjectNode projectRoot = projectParser.getRootTree();

        LogicTreeGeneration logicTree = new LogicTreeGeneration();
        IFunctionNode function = (IFunctionNode) Search.searchNodes(((ProjectParser) projectRoot).getRootTree(),
                new FunctionNodeCondition(), "nsTest4" + File.separator + "func4(int)").get(0);
        logicTree.setFunctionNode(function);
        logicTree.generate();
    }

    public void generate() {
        VariableSearchingSpace space = new VariableSearchingSpace(functionNode);
        /*
		 * Generate initial tree
		 */
        Level fileLevel = space.getSpaces().get(VariableSearchingSpace.FILE_SCOPE_INDEX);
        INode fileScope = new ScopeNode();
        fileScope.getChildren().addAll(fileLevel);
        rootTree.getChildren().add(fileScope);

        Level headerLevel = space.getSpaces().get(VariableSearchingSpace.INCLUDED_INDEX);
        for (INode n : headerLevel) {
            INode fileScope2 = new ScopeNode();
            rootTree.getChildren().add(fileScope2);
            fileScope2.getChildren().addAll(fileLevel);
            rootTree.getChildren().add(fileScope2);
        }

		/*
		 * Merge namespace
		 */

    }

    public IFunctionNode getFunctionNode() {
        return functionNode;
    }

    public void setFunctionNode(IFunctionNode functionNode) {
        this.functionNode = functionNode;
    }

    /**
     * Represent file node
     *
     * @author ducanhnguyen
     */
    class ScopeNode extends Node {

    }
}
