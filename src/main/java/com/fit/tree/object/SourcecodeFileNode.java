package com.fit.tree.object;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import java.io.File;

public abstract class SourcecodeFileNode<N extends IASTTranslationUnit> extends Node
        implements IHasFileNode, ISourcecodeFileNode<N> {

    protected N AST;

    @Override
    public N getAST() {
        return this.AST;
    }

    @Override
    public void setAST(N aST) {
        this.AST = aST;
    }

    @Override
    public String toString() {
        return this.AST.getRawSignature();
    }

    @Override
    public File getFile() {
        return new File(getAbsolutePath());
    }
}
