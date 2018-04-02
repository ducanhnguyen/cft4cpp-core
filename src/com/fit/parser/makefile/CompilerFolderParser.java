package com.fit.parser.makefile;

import com.fit.normalizer.AbstractParser;
import com.fit.parser.makefile.object.GPlusPlusExeCondition;
import com.fit.parser.makefile.object.GccExeCondition;
import com.fit.parser.makefile.object.MakeExeCondition;
import com.fit.parser.projectparser.ProjectLoader;
import com.fit.tree.object.INode;
import com.fit.tree.object.IProjectNode;
import com.fit.utils.search.Search;
import interfaces.IGeneration;

import java.io.File;
import java.util.List;

/**
 * Parse Dev-Cpp folder to get compilation information
 *
 * @author ducanh
 */
public class CompilerFolderParser extends AbstractParser implements IGeneration {

    private String gccPath = "";

    private String gPlusPlusPath = "";

    private String makePath = "";

    private File mingwFolder;

    public CompilerFolderParser(File mingwFolder) {
        this.mingwFolder = mingwFolder;
    }

    public void parse() {
        IProjectNode projectRootNode = new ProjectLoader().load(mingwFolder);

        gccPath = this.getGccPath(projectRootNode).replace("\\", "/");
        gPlusPlusPath = getGPlusPlusPath(projectRootNode).replace("\\", "/");
        makePath = this.getMakePath(projectRootNode).replace("\\", "/");
    }

    public String getGccPath() {
        return gccPath;
    }

    private String getGccPath(IProjectNode projectRootNode) {
        List<INode> gccNodes = Search.searchNodes(projectRootNode, new GccExeCondition());
        if (gccNodes != null && gccNodes.size() > 0)
            return gccNodes.get(0).getAbsolutePath();
        else
            return "";
    }

    public String getgPlusPlusPath() {
        return gPlusPlusPath;
    }

    private String getGPlusPlusPath(IProjectNode projectRootNode) {
        List<INode> gPlusPlusNodes = Search.searchNodes(projectRootNode, new GPlusPlusExeCondition());
        if (gPlusPlusNodes != null && gPlusPlusNodes.size() > 0)
            return gPlusPlusNodes.get(0).getAbsolutePath();
        else
            return "";
    }

    public String getMakePath() {
        return makePath;
    }

    private String getMakePath(IProjectNode projectRootNode) {
        List<INode> makeNodes = Search.searchNodes(projectRootNode, new MakeExeCondition());
        if (makeNodes != null && makeNodes.size() > 0)
            return makeNodes.get(0).getAbsolutePath();
        else
            return "";
    }

}
