package com.fit.gui.main;

import java.util.List;

import javax.swing.Icon;

import com.fit.config.Paths;
import com.fit.gui.swing.TreeExplorer;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IHasFileNode;
import com.fit.tree.object.INode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

public class ProjectExplorer extends TreeExplorer<INode> {

    /**
     *
     */
    private static final long serialVersionUID = 4064355610153245754L;

    public ProjectExplorer(INode root) {
        super(root);
    }

    @Override
    public int compare(INode i1, INode i2) {

        // Các thư mục sẽ được hiển thị trước các tập tin
        if (i1 instanceof IHasFileNode && i2 instanceof IHasFileNode) {
            boolean d1 = ((IHasFileNode) i1).getFile().isDirectory(), d2 = ((IHasFileNode) i2)
                    .getFile().isDirectory();
            return d1 ^ d2 ? d1 ? -1 : 1 : 0;
        }

        return 0;
    }

    @Override
    protected boolean hasItemChild(INode item) {
        return !(item instanceof FunctionNode) && item.getChildren().size() > 0;
    }

    private boolean isNotSupportedFunction(INode item) {
        return false;
    }

    @Override
    protected Iterable<INode> iterItemChilds(INode parent) {
        return parent.getChildren();
    }

    @Override
    protected Icon renderIcon(INode item, boolean selected, boolean expanded,
                              boolean leaf, int row, boolean hasFocus) {
        return item.getIcon();
    }

    @Override
    protected String renderText(INode item, boolean selected, boolean expanded,
                                boolean leaf, int row, boolean hasFocus) {
        return item.getNewType();
    }

    @Override
    protected boolean shouldDisplayInTree(INode item) {
        boolean ignoreFunction = false, ignoreItem = false;
        if (item.getAbsolutePath().equals(Paths.CURRENT_PROJECT.MAKEFILE_PATH))
            return true;
        else if (item instanceof FunctionNode)
            ignoreFunction = isNotSupportedFunction(item);
        else {
            // ignoreItem = Search.searchNodes(item, new
            // FunctionNodeCondition()).size() == 0;
            List<INode> fns = Search.searchNodes(item,
                    new FunctionNodeCondition());

            for (int i = fns.size() - 1; i >= 0; i--)
                if (isNotSupportedFunction(fns.get(i)))
                    fns.remove(i);
            if (fns.size() > 0)
                ignoreItem = false;
            else
                ignoreItem = true;
        }

        return !(ignoreFunction || ignoreItem);
    }

}
