package com.fit.gui.testreport.object;

import com.fit.testdatagen.module.DataTreeGeneration;
import com.fit.testdatagen.module.IDataTreeGeneration;
import com.fit.testdatagen.structuregen.ChangedToken;
import com.fit.testdatagen.structuregen.ChangedTokens;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent <b>Input (arguments, external variables)</b> column
 *
 * @author DucAnh
 */
public class InputReport implements IInputReport {
    protected ChangedTokens changedTokens = new ChangedTokens();

    protected IDataTreeGeneration dataTree = new DataTreeGeneration();

    public InputReport() {
    }

    @Override
    public IDataTreeGeneration getDataTree() {
        return dataTree;
    }

    @Override
    public void setDataTree(IDataTreeGeneration dataTree) throws Exception {
        this.dataTree = dataTree;
    }

    @Override
    public List<String> getVariablesForDisplay() {
        List<String> input = new ArrayList<>();
        input.add(restoreTestdata(dataTree.getInputforGoogleTest(), changedTokens));
        return input;
    }

    @Override
    public List<String> getVariablesForGTest() {
        List<String> input = new ArrayList<>();
        input.add(restoreTestdata(dataTree.getInputforGoogleTest(), changedTokens));
        return input;
    }

    @Override
    public String toString() {
        return dataTree.getInputforGoogleTest();
    }

    @Override
    public ChangedTokens getChangedTokens() {
        return changedTokens;
    }

    @Override
    public void setChangedTokens(ChangedTokens changedTokens) {
        this.changedTokens = changedTokens;
    }

    private String restoreTestdata(String testdata, ChangedTokens changedTokens) {
        String originalTestdata = testdata;
        for (ChangedToken item : changedTokens)
            originalTestdata = originalTestdata.replaceAll("\\b" + item.getNewName() + "\\b", item.getOldName());
        return originalTestdata;
    }
}
