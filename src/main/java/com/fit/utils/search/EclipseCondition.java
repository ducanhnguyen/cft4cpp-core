package com.fit.utils.search;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.tree.object.ExeNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.UnknowObjectNode;
import com.fit.utils.Utils;

import java.io.File;

public class EclipseCondition extends SearchCondition {

    @Override
    public boolean isSatisfiable(INode n) {
        if (Utils.isUnix()) {
            if (n instanceof UnknowObjectNode && (new File(n.getAbsolutePath()).canExecute())
                    && n.getAbsolutePath().endsWith(File.separator + AbstractSetting.getValue(ISettingv2.ECLIPSE_NAME)))
                return true;
        } else if (Utils.isWindows()) {
            if (n instanceof ExeNode && (new File(n.getAbsolutePath()).getName()
                    .equals(AbstractSetting.getValue(ISettingv2.ECLIPSE_NAME))))
                return true;
        }
        return false;
    }

}
