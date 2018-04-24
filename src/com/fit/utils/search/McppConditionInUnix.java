package com.fit.utils.search;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.tree.object.INode;
import com.fit.tree.object.UnknowObjectNode;

import java.io.File;

public class McppConditionInUnix extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (n instanceof UnknowObjectNode && (new File(n.getAbsolutePath()).canExecute())
                && n.getAbsolutePath().endsWith(File.separator + AbstractSetting.getValue(ISettingv2.MCPP_NAME)))
            return true;
        return false;
    }

}
