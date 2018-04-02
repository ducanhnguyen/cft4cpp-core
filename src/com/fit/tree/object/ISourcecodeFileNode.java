package com.fit.tree.object;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import java.io.File;

public interface ISourcecodeFileNode<N extends IASTTranslationUnit> extends INode {

    N getAST();

    void setAST(N aST);

    @Override
    String toString();

    File getFile();

}