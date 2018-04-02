package com.fit.parser.projectparser;

import com.fit.tree.object.IProcessNotify;
import com.fit.tree.object.IProjectNode;
import interfaces.IGeneration;

import java.io.File;

/**
 * Parse the given project down to method, parameter, attribute level
 *
 * @author ducanh
 */
public interface IProjectParser extends IGeneration {

    IProcessNotify getNotify();

    void setNotify(IProcessNotify notify);

    /**
     * Get the path of the given C/C++ project
     *
     * @return
     */
    File getProjectPath();

    void setProjectPath(File projectPath);

    /**
     * Get the root of the structure tree
     *
     * @return
     */
    IProjectNode getRootTree();

}
