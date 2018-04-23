package com.fit.testdatagen.module;

import com.fit.testdata.object.IAbstractDataNode;
import com.fit.testdata.object.RootDataNode;

public class TestdatagenUtils {

    public static IAbstractDataNode getRoot(IAbstractDataNode n) {
        if (n instanceof RootDataNode)
            return n;
        else
            return TestdatagenUtils.getRoot(n.getParent());
    }
}
