package com.fit.gui.swing;

import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.tree.WebTree;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Trình duyệt theo dạng cây
 */
@SuppressWarnings("unchecked")
public abstract class TreeExplorer<E> extends WebTree implements Comparator<E> {
    /**
     *
     */
    private static final long serialVersionUID = 7615665937122674844L;

    private DefaultTreeModel mTreeModel;

    private TreeNode mRoot;

    private PopupMenu mMenu;

    private MenuHandle<E> mMenuHandle;

    private boolean mSetTooltips;

    public TreeExplorer() {
        setModel(null);
        addTreeExpansionListener(new TreeExpansionListener() {

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
            }

            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                TreeNode node = (TreeNode) event.getPath().getLastPathComponent();

                if (node.isPlaceHolderNode())
                    TreeExplorer.this.loadItems(node.getItem(), node);
            }
        });

        setComponentPopupMenu(this.mMenu = new PopupMenu());
    }

    public TreeExplorer(E root) {
        this();
        this.setRoot(root);
    }

    /**
     * Kiểm tra đối tượng có khả năng có chứa các đối tượng con hay không
     */
    protected abstract boolean hasItemChild(E item);

    /**
     * Liệt kê các đối tượng con ứng với đối tượng cha
     */
    protected abstract Iterable<E> iterItemChilds(E parent);

    public void addItemClickListener(ItemClickListener<E> listenr) {
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int row = TreeExplorer.this.getRowForLocation(e.getX(), e.getY());
                if (row == -1)
                    return;

                TreeNode node = (TreeExplorer<E>.TreeNode) TreeExplorer.this.getPathForRow(row).getLastPathComponent();
                listenr.itemClicked(node.getItem(), e);
            }

        });
    }

    @Override
    public int compare(E i1, E i2) {
        return 0;
    }

    /**
     * Trả về mục đầu tiên đang được chọn, hoặc null nếu không có mục được chọn
     */
    public E getSelectedItem() {
        if (getSelectionCount() == 0)
            return null;
        TreeNode node = (TreeNode) getSelectionPath().getLastPathComponent();
        return node.getItem();
    }

    /**
     * Trả về danh sách mục đang được chọn, có thể là tập rỗng
     */
    public List<E> getSelectedItems() {
        int count = getSelectionCount();
        TreePath[] paths = getSelectionPaths();
        List<E> items = new ArrayList<>(count);

        for (TreePath path : paths) {
            TreeNode node = (TreeNode) path.getLastPathComponent();
            items.add(node.getItem());
        }
        return items;
    }

    private void loadItems(E parent, TreeNode node) {
        node.removeAllChildren();
        for (E item : this.iterItemChilds(parent))
            if (this.shouldDisplayInTree(item)) {
                TreeNode child = new TreeNode(item);
                node.add(child);
            }
        node.sortChild((n1, n2) -> this.compare(n1.getItem(), n2.getItem()));

        this.mTreeModel.reload(node);
    }

    protected Icon renderIcon(E item, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        return null;
    }

    protected String renderText(E item, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        return super.convertValueToText(item, selected, expanded, leaf, row, hasFocus);
    }

    /**
     * Đặt điều khiển menu chuột phải
     */
    public void setMenuHandle(MenuHandle<E> handle) {
        this.mMenuHandle = handle;
        this.mMenu.removeAll();
        handle.accept(this.mMenu);
    }

    public void setRoot(E root) {
        this.mRoot = new TreeNode(root);
        setModel(this.mTreeModel = new DefaultTreeModel(this.mRoot));
        this.loadItems(root, this.mRoot);
        expandRow(0);
        setCellRenderer(new TreeCellRender());
    }

    public void setTooltipsEnable(boolean enable) {
        this.mSetTooltips = enable;
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    /**
     * Kiểm tra xem có nên hiển thị đối tượng trên cây hay không
     */
    protected boolean shouldDisplayInTree(E item) {
        return true;
    }

    public interface ItemClickListener<T> {

        void itemClicked(T item, MouseEvent e);
    }

    public interface MenuHandle<T> extends Consumer<JPopupMenu> {

        public void acceptList(List<T> items);
    }

    class PopupMenu extends WebPopupMenu {

        /**
         *
         */
        private static final long serialVersionUID = 1560401062220465810L;

        @Override
        public void show(Component invoker, int x, int y) {
            TreePath select = TreeExplorer.this.getPathForLocation(x, y);

            if (select == null) {
                if (TreeExplorer.this.mMenuHandle != null)
                    TreeExplorer.this.mMenuHandle.acceptList(Collections.EMPTY_LIST);
                setSelectionPaths(null);
            } else {
                boolean findPath = false;
                TreePath[] selects = getSelectionPaths();

                if (getSelectionCount() > 0)
                    for (TreePath p : selects)
                        if (p.equals(select)) {
                            findPath = true;
                            break;
                        }
                if (!findPath) {
                    setSelectionPath(select);
                    selects = new TreePath[]{select};
                }

                if (TreeExplorer.this.mMenuHandle != null) {
                    List<E> items = new ArrayList<>(selects.length);

                    for (TreePath select2 : selects) {
                        TreeNode node = (TreeNode) select2.getLastPathComponent();
                        items.add(node.getItem());
                    }
                    TreeExplorer.this.mMenuHandle.acceptList(items);
                }
            }
            super.show(invoker, x, y);
        }

    }

    class TreeCellRender extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            TreeNode node = (TreeNode) value;
            E item = node.getItem();

            if (node.hasItem()) {
                setIcon(TreeExplorer.this.renderIcon(item, sel, expanded, leaf, row, hasFocus));
                setText(TreeExplorer.this.renderText(item, sel, expanded, leaf, row, hasFocus));

                if (TreeExplorer.this.mSetTooltips)
                    setToolTipText(getText());
            }
            return c;
        }
    }

    class TreeNode extends DefaultMutableTreeNode {

        /**
         *
         */
        private static final long serialVersionUID = -6771448358453424137L;
        private E item;

        private TreeNode() {
        }

        public TreeNode(E item) {
            this.item = item;
            setUserObject(item);

            if (TreeExplorer.this.hasItemChild(item))
                this.add(new TreeNode());
        }

        @Override
        public TreeNode getChildAt(int index) {
            return (TreeNode) super.getChildAt(index);
        }

        public E getItem() {
            return this.item;
        }

        public boolean hasItem() {
            return this.item != null;
        }

        public boolean isPlaceHolderNode() {
            return getChildCount() == 1 && !this.getChildAt(0).hasItem();
        }

        public void sortChild(Comparator<TreeNode> c) {
            if (children != null)
                children.sort(c);
        }

    }

}
