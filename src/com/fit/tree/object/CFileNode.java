package com.fit.tree.object;

import com.fit.tree.dependency.Dependency;
import com.fit.tree.dependency.IncludeHeaderDependency;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTTranslationUnit;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CFileNode extends SourcecodeFileNode<CASTTranslationUnit> {

    public CFileNode() {
        try {
            Icon ICON_C = new ImageIcon(Node.class.getResource("/image/node/Soucecode-C.png"));
            setIcon(ICON_C);
        } catch (Exception e) {
        }
    }

    public List<Dependency> getIncludeHeaderNodes() {
        List<Dependency> includedDependencies = new ArrayList<>();
        for (Dependency dependency : getDependencies())
            if (dependency instanceof IncludeHeaderDependency)
                includedDependencies.add(dependency);
        return includedDependencies;
    }

}
