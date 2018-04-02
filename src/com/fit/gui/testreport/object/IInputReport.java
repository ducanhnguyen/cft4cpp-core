package com.fit.gui.testreport.object;

import com.fit.testdatagen.module.IDataTreeGeneration;
import com.fit.testdatagen.structuregen.ChangedTokens;

import java.util.List;

/**
 * Represent input report
 *
 * @author ducanhnguyen
 */
public interface IInputReport extends ITestedReport {

    IDataTreeGeneration getDataTree();

    void setDataTree(IDataTreeGeneration dataTree) throws Exception;

    /**
     * Lấy danh sách các biến để hiển thị trên GUI
     *
     * @return
     * @throws Exception
     */
    List<String> getVariablesForDisplay();

    /**
     * Get source code that put on google test file
     *
     * @return
     * @throws Exception
     */
    List<String> getVariablesForGTest();

    ChangedTokens getChangedTokens();

    void setChangedTokens(ChangedTokens changedTokens);
}