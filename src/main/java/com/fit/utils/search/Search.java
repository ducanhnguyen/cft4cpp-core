package com.fit.utils.search;

import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Search implements ISearch {

    /**
     * Tìm các con có tên xác định của một node
     *
     * @param parent
     * @param name
     * @return
     */
    public synchronized static INode searchFirstNodeByName(INode parent,
                                                           String name) {
        for (INode child : parent.getChildren()) {
            String nameChild = "";
            if (child instanceof IFunctionNode)
                nameChild = ((FunctionNode) child).getSimpleName();
            else
                nameChild = child.getNewType();

            if (nameChild.equals(name))
                return child;
        }
        return null;
    }

    /**
     * @param root       Root sub tree
     * @param conditions Danh sách điều kiện tìm kiếm
     * @return Danh sách node thỏa mãn điều kiện tìm kiếm
     */
    public synchronized static List<INode> searchNodes(INode root,
                                                       List<SearchCondition> conditions) {
        List<INode> output = new ArrayList<>();

        for (INode child : root.getChildren()) {
            boolean isSatisfiable = false;

            for (ISearchCondition con : conditions)
                if (con.isSatisfiable(child)) {
                    isSatisfiable = true;
                    break;
                }

            if (isSatisfiable)
                output.add(child);
            output.addAll(Search.searchNodes(child, conditions));
        }
        return output;
    }

    /**
     * @param root      Root sub tree
     * @param condition Điều kiện tìm kiếm
     * @return Danh sách node thỏa mãn điều kiện tìm kiếm
     */
    public synchronized static List<INode> searchNodes(INode root,
                                                       ISearchCondition condition) {
        List<INode> output = new ArrayList<>();

        for (INode child : root.getChildren()) {
            if (condition.isSatisfiable(child))
                output.add(child);
            output.addAll(Search.searchNodes(child, condition));
        }
        return output;
    }

    /**
     * @param root      Root sub tree
     * @param condition Điều kiện tìm kiếm
     * @return Danh sách node thỏa mãn điều kiện tìm kiếm
     */
    public synchronized static List<INode> searchNodes(INode root,
                                                       ISearchCondition condition, String relativePath) {
        List<INode> output = Search.searchNodes(root, condition);
        relativePath = Utils.normalizePath(relativePath);
        if (!relativePath.startsWith(File.separator))
            relativePath = File.separator + relativePath;

        for (int i = output.size() - 1; i >= 0; i--) {
            INode n = output.get(i);
            if (!n.getAbsolutePath().endsWith(relativePath))
                output.remove(n);
        }
        return output;
    }

}
