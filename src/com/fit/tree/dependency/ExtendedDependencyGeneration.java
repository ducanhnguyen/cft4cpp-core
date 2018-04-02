package com.fit.tree.dependency;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.INode;
import com.fit.tree.object.AvailableTypeNode;
import com.fit.tree.object.StructureNode;
import com.fit.tree.object.VariableNode;
import com.fit.utils.search.ClassvsStructvsNamespaceCondition;
import com.fit.utils.search.Search;
import com.fit.utils.tostring.ReducedDependencyTreeDisplayer;
import com.fit.utils.tostring.ToString;

import java.io.File;
import java.util.List;

public class ExtendedDependencyGeneration {

    public ExtendedDependencyGeneration(INode root) {
        dependencyGeneration(root);
    }

    public static void main(String[] args) {
        ProjectParser parser = new ProjectParser(new File(Paths.TSDV_R1), null);
        new ExtendedDependencyGeneration(parser.getRootTree());

        ToString treeDisplayer = new ReducedDependencyTreeDisplayer(parser.getRootTree());
        System.out.println(treeDisplayer.getTreeInString());
    }

    public void dependencyGeneration(INode root) {
        List<INode> nodes = Search.searchNodes(root, new ClassvsStructvsNamespaceCondition());
        for (INode n : nodes)
            if (n instanceof StructureNode) {
                List<String> extendClassNames = ((StructureNode) n).getExtendedNames();
                for (String extendClassName : extendClassNames) {
                    /*
					 * Create temporary variable
					 */
                    VariableNode v = new VariableNode();
                    v.setCoreType(extendClassName);
                    v.setRawType(extendClassName);
                    v.setParent(n);
					/*
					 * Find type of temporary variable
					 */
                    TypeDependencyGeneration typeGen;
                    try {
                        typeGen = new TypeDependencyGeneration(v);
                        INode correspondingNode = typeGen.getCorrespondingNode();
                        if (correspondingNode != null && !(correspondingNode instanceof AvailableTypeNode)) {
                            INode refferedNode = correspondingNode;

                            ExtendDependency d = new ExtendDependency(n, refferedNode);
                            n.getDependencies().add(d);
                            refferedNode.getDependencies().add(d);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    }
}
