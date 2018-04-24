package com.fit.normalizer;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.*;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.search.AbstractFunctionNodeCondition;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

import java.io.File;
import java.util.List;

public class ParentReconstructor {

    private INode projectNodeRoot;

    public ParentReconstructor(INode projectNodeRoot) throws Exception {
        this.projectNodeRoot = projectNodeRoot;
        reconstruct();
    }

    public static void main(String[] args) throws Exception {
        ProjectParser projectParser = new ProjectParser(new File(Paths.SYMBOLIC_EXECUTION_TEST), null);
        projectParser.getRootTree();

        IFunctionNode sampleNode = (IFunctionNode) Search.searchNodes(projectParser.getRootTree(),
                new FunctionNodeCondition(), "nsTest0" + File.separator + "Student::isAvailable(int)").get(0);
        System.out.println(sampleNode.getRealParent().getAbsolutePath());
    }

    public INode getProjectNodeRoot() {
        return projectNodeRoot;
    }

    public void setProjectNodeRoot(Node projectNodeRoot) {
        this.projectNodeRoot = projectNodeRoot;
    }

    private void reconstruct() throws Exception {
        List<INode> functionNodes = Search.searchNodes(projectNodeRoot, new AbstractFunctionNodeCondition());
        for (INode functionNode : functionNodes)
            if (functionNode instanceof AbstractFunctionNode) {

                AbstractFunctionNode f = (AbstractFunctionNode) functionNode;
                /*
				 * Ex: void SinhVien::timSinhVien(int msv){...}
				 *
				 */
                if (f.getSimpleName().contains(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS)) {
                    String address = "";

                    String[] elements = f.getSimpleName().split(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS);
                    for (String element : elements)
                        if (f.getSimpleName().contains(element + SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS))
                            address += element + File.separator;

                    address = address.replaceAll(File.separator + File.separator + "$", "");
					/*
					 * Search
					 */
                    VariableNode vituralVar = new VariableNode();
                    vituralVar.setRawType(address);
                    vituralVar.setCoreType(address);
                    vituralVar.setReducedRawType(address);
                    vituralVar.setParent(functionNode);

                    INode realParentNode = vituralVar.resolveCoreType();
					/*
					 *
					 */
                    if (realParentNode instanceof StructureNode || realParentNode instanceof NamespaceNode)
                        ((AbstractFunctionNode) functionNode).setRealParent(realParentNode);
                }
            }
    }
}
