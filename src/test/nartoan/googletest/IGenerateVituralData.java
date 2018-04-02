package test.nartoan.googletest;

import com.fit.tree.object.IVariableNode;

import java.util.List;

public interface IGenerateVituralData {

    VirtualDataInRows generateVirtualData(List<IVariableNode> expectedNodeTypes);
}
