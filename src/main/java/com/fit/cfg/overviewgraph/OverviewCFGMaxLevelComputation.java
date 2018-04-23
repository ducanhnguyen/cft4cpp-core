package com.fit.cfg.overviewgraph;

import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.apache.log4j.Logger;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;

import java.io.File;

/**
 * Compute the maximum level of the overview CFG
 *
 * @author DucAnh
 */
public class OverviewCFGMaxLevelComputation implements IOverviewCFGMaxLevelComputation {
    final static Logger logger = Logger.getLogger(OverviewCFGMaxLevelComputation.class);
    private IFunctionNode functionNode;

    private int maxLevel = IOverviewCFGMaxLevelComputation.MAXIMUM_LEVEL;

    private int currentLevel = IOverviewCFGMaxLevelComputation.MAXIMUM_LEVEL;

    public OverviewCFGMaxLevelComputation(IFunctionNode fn) {
        functionNode = fn;
    }

    public static void main(String[] args) {
        ProjectParser parser = new ProjectParser(new File(Paths.BTL));

        INode function = Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "TS::xoa_dau_cach_thua(char[])").get(0);
        IOverviewCFGMaxLevelComputation cfgMaxLevelDetecter = new OverviewCFGMaxLevelComputation(
                (IFunctionNode) function);
        cfgMaxLevelDetecter.computeMaxLevel();
        logger.debug(cfgMaxLevelDetecter.getMaxLevel());
    }

    @Override
    public void computeMaxLevel() {
        if (functionNode != null) {
            IASTFunctionDefinition ast = functionNode.getAST();

            ASTVisitor visitor = new ASTVisitor() {
                @Override
                public int leave(IASTStatement statement) {
                    if (statement instanceof CPPASTWhileStatement || statement instanceof CPPASTForStatement
                            || statement instanceof CPPASTIfStatement || statement instanceof CPPASTWhileStatement
                            || statement instanceof CPPASTSwitchStatement || statement instanceof CPPASTDoStatement
                            || statement instanceof CPPASTTryBlockStatement) {
                        if (currentLevel > maxLevel)
                            maxLevel = currentLevel;
                        currentLevel--;
                    }
                    return ASTVisitor.PROCESS_SKIP;
                }

                @Override
                public int visit(IASTStatement statement) {
                    if (statement instanceof CPPASTWhileStatement || statement instanceof CPPASTForStatement
                            || statement instanceof CPPASTIfStatement || statement instanceof CPPASTWhileStatement
                            || statement instanceof CPPASTSwitchStatement || statement instanceof CPPASTDoStatement
                            || statement instanceof CPPASTTryBlockStatement)
                        currentLevel++;
                    return ASTVisitor.PROCESS_CONTINUE;
                }
            };

            visitor.shouldVisitStatements = true;

            ast.accept(visitor);
        }
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    @Override
    public IFunctionNode getFunctionNode() {
        return functionNode;
    }

    @Override
    public void setFunctionNode(IFunctionNode functionNode) {
        this.functionNode = functionNode;
    }
}
