package com.fit.parser.makefile.object;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.tree.object.ExeNode;
import com.fit.tree.object.INode;
import com.fit.utils.search.SearchCondition;

/**
 * Đại diện tệp g++.exe
 *
 * @author DucAnh
 */
public class GPlusPlusExeCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof ExeNode && n.getNewType().equals(AbstractSetting.getValue(ISettingv2.GNU_GPlusPlus_NAME)))
            return true;
        return false;
    }
}
