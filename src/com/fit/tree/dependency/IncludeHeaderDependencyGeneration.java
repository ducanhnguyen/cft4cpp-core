package com.fit.tree.dependency;

import java.io.File;
import java.util.List;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.INode;
import com.fit.tree.object.IncludeHeaderNode;
import com.fit.tree.object.Node;
import com.fit.utils.Utils;
import com.fit.utils.search.HeaderNodeCondition;
import com.fit.utils.search.IncludeHeaderNodeCondition;
import com.fit.utils.search.Search;
import com.fit.utils.tostring.ReducedDependencyTreeDisplayer;

public class IncludeHeaderDependencyGeneration {

    public IncludeHeaderDependencyGeneration(INode sourceCodeFileNode) {
        dependencyGeneration(sourceCodeFileNode);
    }

    public static void main(String[] args) {
        ProjectParser parser = new ProjectParser(new File(
                Paths.SEPARATE_FUNCTION_TEST), null);
        Node root = (Node) parser.getRootTree();

        new ReducedDependencyTreeDisplayer(root);

    }

    public void dependencyGeneration(INode owner) {
        List<INode> includeHeaderNodes = Search.searchNodes(owner,
                new IncludeHeaderNodeCondition());
        INode root = Utils.getRoot(owner);
        for (INode includeHeaderNode : includeHeaderNodes) {
            String includeFilePath = ((IncludeHeaderNode) includeHeaderNode)
                    .getNewType();

            includeFilePath = Utils.normalizePath(includeFilePath);
            List<INode> searchedNodes = Search
                    .searchNodes(root, new HeaderNodeCondition(),
                            File.separator + includeFilePath);
            if (searchedNodes.size() >= 1) {
                INode refferedNode = searchedNodes.get(0);

                IncludeHeaderDependency d = new IncludeHeaderDependency(owner,
                        refferedNode);
                if (!owner.getDependencies().contains(d)
                        && !refferedNode.getDependencies().contains(d)) {
                    owner.getDependencies().add(d);
                    refferedNode.getDependencies().add(d);
                }
            }
        }
    }

}
