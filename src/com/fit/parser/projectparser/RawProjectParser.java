package com.fit.parser.projectparser;

import com.fit.config.Paths;
import com.fit.normalizer.AbstractProjectParser;
import com.fit.tree.object.INode;
import com.fit.tree.object.IProcessNotify;
import com.fit.tree.object.IProjectNode;
import com.fit.tree.object.SourcecodeFileNode;
import com.fit.utils.search.Search;
import com.fit.utils.search.SourcecodeFileNodeCondition;
import com.fit.utils.tostring.DependencyTreeDisplayer;

import java.io.File;
import java.util.List;

public class RawProjectParser extends AbstractProjectParser implements IProjectParser {

    public RawProjectParser(File projectPath) {
        this.projectPath = projectPath;
        notify = null;

    }

    public RawProjectParser(File projectPath, boolean isRawParsing) {
        this.projectPath = projectPath;
        isRawParsing = true;
    }

    public RawProjectParser(File projectPath, IProcessNotify notify) {
        this.notify = notify;
        this.projectPath = projectPath;
    }

    public static void main(String[] args) throws Exception {
        RawProjectParser projectParser = new RawProjectParser(new File(Paths.TSDV_R1), null);
        IProjectNode projectRoot = projectParser.getRootTree();

        /**
         * Display tree of project
         */
        System.out.println(new DependencyTreeDisplayer(projectRoot).getTreeInString());
    }

    private IProjectNode parseProject(File projectPath) {
        IProjectNode root = new ProjectLoader().load(projectPath);

        try {
            expandTreeuptoMethodLevel(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return root;
    }

    /**
     * Parse all source code file in the given project to expand the structure
     * to method level
     */
    private void expandTreeuptoMethodLevel(INode root) throws Exception {
        List<INode> sourcecodeNodes = Search.searchNodes(root, new SourcecodeFileNodeCondition());

        for (INode sourceCodeNode : sourcecodeNodes)
            if (sourceCodeNode instanceof SourcecodeFileNode) {
                SourcecodeFileNode fNode = (SourcecodeFileNode) sourceCodeNode;

                if (notify != null)
                    notify.notify(fNode.getFile().getName());

                File dir = fNode.getFile();

                if (dir != null) {
                    SourcecodeFileParser cppParser = new SourcecodeFileParser();
                    cppParser.setSourcecodeNode((SourcecodeFileNode) sourceCodeNode);
                    INode sourcecodeTreeRoot = cppParser.generateTree();
                    fNode.setAST(cppParser.getTranslationUnit());

                    for (INode sourcecodeItem : sourcecodeTreeRoot.getChildren()) {
                        sourceCodeNode.getChildren().add(sourcecodeItem);
                        sourcecodeItem.setParent(sourceCodeNode);
                    }
                }
            }
    }

    @Override
    public IProjectNode getRootTree() {
        return parseProject(projectPath);
    }
}
