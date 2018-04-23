package com.fit.tree.object;

import org.eclipse.cdt.core.dom.ast.IASTFileLocation;

import java.io.File;

public interface ISourceNavigable {

    IASTFileLocation getNodeLocation();

    File getSourceFile();
}
