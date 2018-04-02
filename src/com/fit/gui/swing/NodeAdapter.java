package com.fit.gui.swing;

import java.util.ArrayList;

public class NodeAdapter<E> extends ArrayList<Node<E>> {

    private static final long serialVersionUID = 1L;

    protected Node<E> getNodeByElement(E element) {
        return this.getNodeByElement(element, this);
    }

    protected Node<E> getNodeByElement(E element, Iterable<Node<E>> iter) {
        for (Node<E> n : iter)
            if (n != null && n.getElement() == element)
                return n;
        return null;
    }
}
