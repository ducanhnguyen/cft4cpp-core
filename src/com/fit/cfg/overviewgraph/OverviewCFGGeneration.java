package com.fit.cfg.overviewgraph;

import com.fit.cfg.CFG;
import com.fit.cfg.ICFG;
import com.fit.cfg.object.*;
import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.apache.log4j.Logger;
import org.eclipse.cdt.core.dom.ast.*;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCatchHandler;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTUnaryExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate the overview CFG
 *
 * @author DucAnh, HungPQ
 */
public class OverviewCFGGeneration implements IOverviewCFGGeneration {
    final static Logger logger = Logger.getLogger(OverviewCFGGeneration.class);

    private ICFG cfg;

    private ICfgNode BEGIN, END;

    private int levelCFG = IOverviewCFGGeneration.CFG_LEVEL_ONE;

    private int forAnalysisStrategy = IOverviewCFGGeneration.FOR_IS_ONE_NODE;

    private int posLevel = 1;

    private boolean isInTryBlock = false;

    private ICfgNode posCfgStatement;

    public OverviewCFGGeneration(IFunctionNode fn, int levelCFG) {
        this.levelCFG = levelCFG;
        cfg = parse(fn);
    }

    public OverviewCFGGeneration(IFunctionNode fn, int levelCFG, int forAnalysisStrategy) {
        this.levelCFG = levelCFG;
        this.forAnalysisStrategy = forAnalysisStrategy;
        cfg = parse(fn);
    }

    public static void main(String[] args) {
        ProjectParser parser = new ProjectParser(new File(Paths.BTL));
        INode function = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "TS::chuan_hoa(char*)")
                .get(0);
        IOverviewCFGGeneration cfgGen = new OverviewCFGGeneration((IFunctionNode) function,
                IOverviewCFGGeneration.CFG_LEVEL_TWO, IOverviewCFGGeneration.FOR_IS_ONE_NODE);
        logger.debug(cfgGen.getOverviewCFG().toString());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.fit.cfg.overviewgraph.IOverviewCFGGeneration2#getOverviewCFG()
     */
    @Override
    public ICFG getOverviewCFG() {
        return cfg;
    }

    private String ast(IASTNode node) {
        return node == null ? "" : node.getRawSignature();
    }

    private void linkStatement(ICfgNode root, List<ICfgNode> cfgNodes) {
        if (root == null || root.isVisited())
            return;
        root.setVisit(true);
        cfgNodes.add(root);

        if (root.isMultipleTarget()) {
            for (ICfgNode target : root.getListTarget())
                linkStatement(target, cfgNodes);
            return;
        }

        ICfgNode stmTrue = root.getTrueNode();
        while (stmTrue instanceof ForwardCfgNode)
            stmTrue = stmTrue.getTrueNode();
        root.setTrue(stmTrue);

        ICfgNode stmFalse = root.getFalseNode();
        while (stmFalse instanceof ForwardCfgNode)
            stmFalse = stmFalse.getTrueNode();
        root.setFalse(stmFalse);

        linkStatement(stmTrue, cfgNodes);
        linkStatement(stmFalse, cfgNodes);
    }

    private ICFG parse(IFunctionNode fn) {
        BEGIN = new BeginFlagCfgNode();
        END = new EndFlagCfgNode();
        ArrayList<ICfgNode> stmList = new ArrayList<>();

        posCfgStatement = BEGIN;

        /**
         * IASTFunctionDefinition đại diện định nghĩa hàm
         */
        IASTFunctionDefinition ast = fn.getAST();

        /**
         * Xây dựng bộ duyệt cây
         */
        ASTVisitor visitor = new ASTVisitor() {

            /**
             * IASTDeclaration đại diện cho khai báo
             */
            @Override
            public int visit(IASTDeclaration declaration) {
                return ASTVisitor.PROCESS_CONTINUE;
            }

            /**
             * IASTStatement đại diện cho câu lệnh/khối câu lệnh (như khối for,
             * if, v.v.)
             */
            @Override
            public int visit(IASTStatement stmAst) {
                OverviewCFGGeneration.this.visitStatement(stmAst);
                if (stmAst instanceof CPPASTReturnStatement) {
                    shouldVisitStatements = false;
                    shouldVisitDeclarations = false;
                }
                if (stmAst instanceof CPPASTWhileStatement || stmAst instanceof CPPASTForStatement
                        || stmAst instanceof CPPASTIfStatement || stmAst instanceof CPPASTSwitchStatement
                        || stmAst instanceof CPPASTDoStatement || stmAst instanceof CPPASTTryBlockStatement
                        || stmAst instanceof CPPASTReturnStatement)
                    return ASTVisitor.PROCESS_SKIP;
                return super.visit(stmAst);
            }
        };
        /**
         * Cài chế độ duyệt
         */
        visitor.shouldVisitStatements = true;// duyệt câu lệnh for, if, v.v.
        visitor.shouldVisitDeclarations = true; // duyệt khai báo

        /**
         * Bắt đầu duyệt
         */
        ast.accept(visitor);

        posCfgStatement.setBranch(END);
        linkStatement(BEGIN, stmList);
        return new CFG(stmList);
    }

    /**
     * visit while, for ,... statement like a simple statement (but has separate
     * node)
     *
     * @param stmAst
     */
    private void visitComplexStatementSimply(IASTStatement stmAst) {
        CfgNode stmTemp = new NormalCfgNode(stmAst);
        posCfgStatement.setBranch(stmTemp);
        posCfgStatement = stmTemp;

        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
        posCfgStatement.setBranch(stmSeparate);
        posCfgStatement = stmSeparate;
    }

    private void visitDoStatement(IASTStatement statement) {

        posLevel++;

        CPPASTDoStatement stm = (CPPASTDoStatement) statement;

        SeparatePointCfgNode stmSeparateBefore = new SeparatePointCfgNode();
        posCfgStatement.setBranch(stmSeparateBefore);
        posCfgStatement = stmSeparateBefore;

        if (levelCFG == 1)
            visitComplexStatementSimply(stm);
        else {
            IASTExpression astCon = stm.getCondition();
            IASTStatement astBody = stm.getBody();
            ICfgNode stmCon = new NormalCfgNode(astCon);

            CfgNode befDo = new ForwardCfgNode();
            posCfgStatement.setBranch(befDo);
            ICfgNode endDo = new ForwardCfgNode();
            ICfgNode stmBody = new ForwardCfgNode();

            if (astBody instanceof IASTCompoundStatement)
                for (int i = 0; i < astBody.getChildren().length; i++) {
                    IASTNode child = astBody.getChildren()[i];

                    if (i == 0 && isEndStatement(child)) {
                        stmBody = new NormalCfgNode(child);
                        befDo.setBranch(stmBody);
                        stmBody.setBranch(END);
                        break;
                    }

                    if (i == 0 && !(child instanceof CPPASTDeclarationStatement
                            || child instanceof CPPASTExpressionStatement)) {

                        if (posLevel < levelCFG) {

                            befDo.setBranch(stmBody);
                            posCfgStatement = stmBody;
                            visitStatement((IASTStatement) child);
                            stmBody = posCfgStatement;
                        } else {
                            stmBody = new NormalCfgNode(child);
                            befDo.setBranch(stmBody);
                        }

                        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                        stmBody.setBranch(stmSeparate);
                        stmBody = stmSeparate;
                    }
                    if (i == 0 && (child instanceof CPPASTDeclarationStatement
                            || child instanceof CPPASTExpressionStatement)) {
                        stmBody = new NormalCfgNode(child);
                        befDo.setBranch(stmBody);

                    }
                    if (i != 0) {
                        if (isEndStatement(child)) {
                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparateTemp);
                            stmBody = stmSeparateTemp;

                            CfgNode statementTemp = new NormalCfgNode(child);
                            stmBody.setBranch(statementTemp);
                            statementTemp.setBranch(END);
                            break;
                        }

                        if (child instanceof CPPASTDeclarationStatement || child instanceof CPPASTExpressionStatement) {
                            CfgNode stmTemp = new NormalCfgNode(child);
                            stmBody.setBranch(stmTemp);
                            stmBody = stmTemp;
                        } else {

                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparateTemp);
                            stmBody = stmSeparateTemp;

                            if (posLevel < levelCFG) {

                                posCfgStatement = stmBody;
                                visitStatement((IASTStatement) child);
                                stmBody = posCfgStatement;
                            } else {
                                CfgNode stmTemp = new NormalCfgNode(child);
                                stmBody.setBranch(stmTemp);
                                stmBody = stmTemp;
                            }

                            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparate);
                            stmBody = stmSeparate;
                        }
                    }
                    if (i == astBody.getChildren().length - 1) {
                        stmBody.setBranch(stmCon);
                        stmCon.setTrue(befDo);
                    }
                }
            else if (isEndStatement(astBody)) {
                stmBody = new NormalCfgNode(astBody);
                befDo.setBranch(stmBody);
                stmBody.setBranch(END);

            } else if (posLevel < levelCFG) {
                befDo.setBranch(stmBody);
                posCfgStatement = stmBody;
                visitStatement(astBody);
                stmBody = posCfgStatement;
                stmBody.setBranch(stmCon);
                stmCon.setTrue(stmBody);

            } else {
                stmBody = new NormalCfgNode(astBody);
                befDo.setBranch(stmBody);
                stmBody.setBranch(stmCon);
                stmCon.setTrue(stmBody);
            }
            stmCon.setFalse(endDo);
            posCfgStatement = endDo;

            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;
        }
        posLevel--;

    }

    private void visitForStatement(IASTStatement stmAst) {
        posLevel++;
        CPPASTForStatement stm = (CPPASTForStatement) stmAst;
        IASTStatement astInit = stm.getInitializerStatement();
        IASTExpression astCond = stm.getConditionExpression();
        IASTExpression astIter = stm.getIterationExpression();
        IASTStatement astBody = stm.getBody();

        SeparatePointCfgNode stmSeparateBefore = new SeparatePointCfgNode();
        posCfgStatement.setBranch(stmSeparateBefore);
        posCfgStatement = stmSeparateBefore;

        if (levelCFG == 1)
            visitComplexStatementSimply(stm);
        else if (forAnalysisStrategy == IOverviewCFGGeneration.FOR_IS_ONE_NODE) {
            String sFor = String.format("%s %s; %s", ast(astInit), ast(astCond), ast(astIter));
            ICfgNode eFor = new MarkFlagCfgNode(sFor);
            posCfgStatement.setBranch(eFor);

            ICfgNode endFor = new ForwardCfgNode();
            ICfgNode stmBody = new ForwardCfgNode();

            if (astBody instanceof IASTCompoundStatement) {
                if (astBody.getChildren().length == 0)
                    eFor.setTrue(eFor);
                for (int i = 0; i < astBody.getChildren().length; i++) {
                    IASTNode stmChild = astBody.getChildren()[i];

                    if (i == 0 && isEndStatement(stmChild)) {
                        stmBody = new NormalCfgNode(stmChild);
                        eFor.setTrue(stmBody);
                        stmBody.setBranch(END);
                        break;
                    }

                    if (i == 0 && !(stmChild instanceof CPPASTDeclarationStatement
                            || stmChild instanceof CPPASTExpressionStatement)) {

                        if (posLevel < levelCFG) {

                            eFor.setTrue(stmBody);
                            posCfgStatement = stmBody;
                            visitStatement((IASTStatement) stmChild);
                            stmBody = posCfgStatement;
                        } else {
                            stmBody = new NormalCfgNode(stmChild);
                            eFor.setTrue(stmBody);
                        }

                        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                        stmBody.setBranch(stmSeparate);
                        stmBody = stmSeparate;
                    }
                    if (i == 0 && (stmChild instanceof CPPASTDeclarationStatement
                            || stmChild instanceof CPPASTExpressionStatement)) {
                        stmBody = new NormalCfgNode(stmChild);
                        eFor.setTrue(stmBody);
                    }
                    if (i != 0) {
                        if (isEndStatement(stmChild)) {
                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparateTemp);
                            stmBody = stmSeparateTemp;

                            ICfgNode statementTemp = new NormalCfgNode(stmChild);
                            stmBody.setBranch(statementTemp);
                            statementTemp.setBranch(END);
                            break;
                        }

                        if (stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement) {
                            ICfgNode stmTemp = new NormalCfgNode(stmChild);
                            stmBody.setBranch(stmTemp);
                            stmBody = stmTemp;
                        } else {
                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparateTemp);
                            stmBody = stmSeparateTemp;

                            if (posLevel < levelCFG) {

                                posCfgStatement = stmBody;
                                visitStatement((IASTStatement) stmChild);
                                stmBody = posCfgStatement;
                            } else {
                                ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                stmBody.setBranch(stmTemp);
                                stmBody = stmTemp;
                            }

                            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparate);
                            stmBody = stmSeparate;
                        }
                    }
                    if (i == astBody.getChildren().length - 1)
                        stmBody.setBranch(eFor);
                }
            } else if (isEndStatement(astBody)) {
                stmBody = new NormalCfgNode(astBody);
                eFor.setTrue(stmBody);
                stmBody.setBranch(END);

            } else if (posLevel < levelCFG) {
                eFor.setTrue(stmBody);
                posCfgStatement = stmBody;
                visitStatement(astBody);
                stmBody = posCfgStatement;
                eFor.setTrue(stmBody);
                stmBody.setBranch(eFor);

            } else {
                stmBody = new NormalCfgNode(astBody);
                eFor.setTrue(stmBody);
                stmBody.setBranch(eFor);
            }

            eFor.setFalse(endFor);
            posCfgStatement = endFor;

            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;

        } else {
            ICfgNode stmInit = ScopeCfgNode.newCloseScope();
            if (astInit != null)
                stmInit = new NormalCfgNode(astInit);
            ICfgNode stmCond = ScopeCfgNode.newCloseScope();

            if (astCond != null)
                stmCond = new NormalCfgNode(astCond);
            ICfgNode stmIter = ScopeCfgNode.newCloseScope();

            if (astIter != null)
                stmIter = new NormalCfgNode(astIter);

            posCfgStatement.setBranch(stmInit);
            stmInit.setBranch(stmCond);

            ICfgNode scopeOut = ScopeCfgNode.newCloseScope();
            ICfgNode stmBody = new ForwardCfgNode();

            if (astBody instanceof IASTCompoundStatement) {
                if (astBody.getChildren().length == 0)
                    stmCond.setTrue(stmIter);
                for (int i = 0; i < astBody.getChildren().length; i++) {
                    IASTNode stmChild = astBody.getChildren()[i];

                    if (i == 0 && isEndStatement(stmChild)) {
                        stmBody = new NormalCfgNode(stmChild);
                        stmCond.setTrue(stmBody);
                        stmBody.setBranch(END);
                        break;
                    }

                    if (i == 0 && !(stmChild instanceof CPPASTDeclarationStatement
                            || stmChild instanceof CPPASTExpressionStatement)) {

                        if (posLevel < levelCFG) {

                            stmCond.setTrue(stmBody);
                            posCfgStatement = stmBody;
                            visitStatement((IASTStatement) stmChild);
                            stmBody = posCfgStatement;
                        } else {
                            stmBody = new NormalCfgNode(stmChild);
                            stmCond.setTrue(stmBody);
                        }

                        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                        stmBody.setBranch(stmSeparate);
                        stmBody = stmSeparate;
                    }
                    if (i == 0 && (stmChild instanceof CPPASTDeclarationStatement
                            || stmChild instanceof CPPASTExpressionStatement)) {
                        stmBody = new NormalCfgNode(stmChild);
                        stmCond.setTrue(stmBody);
                    }
                    if (i != 0) {
                        if (isEndStatement(stmChild)) {
                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparateTemp);
                            stmBody = stmSeparateTemp;

                            ICfgNode statementTemp = new NormalCfgNode(stmChild);
                            stmBody.setBranch(statementTemp);
                            statementTemp.setBranch(END);
                            break;
                        }

                        if (stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement) {
                            ICfgNode stmTemp = new NormalCfgNode(stmChild);
                            stmBody.setBranch(stmTemp);
                            stmBody = stmTemp;
                        } else {
                            // stmSimple = null;
                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparateTemp);
                            stmBody = stmSeparateTemp;

                            if (posLevel < levelCFG) {
                                posCfgStatement = stmBody;
                                visitStatement((IASTStatement) stmChild);
                                stmBody = posCfgStatement;
                            } else {
                                ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                stmBody.setBranch(stmTemp);
                                stmBody = stmTemp;
                            }
                            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparate);
                            stmBody = stmSeparate;
                        }
                    }
                    if (i == astBody.getChildren().length - 1)
                        stmBody.setBranch(stmIter);
                }
            } else if (isEndStatement(astBody)) {
                stmBody = new NormalCfgNode(astBody);
                stmCond.setTrue(stmBody);
                stmBody.setBranch(END);
            } else if (posLevel < levelCFG) {
                stmCond.setTrue(stmBody);
                posCfgStatement = stmBody;
                visitStatement(astBody);
                stmBody = posCfgStatement;

                SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                stmBody.setBranch(stmSeparate);
                stmBody = stmSeparate;

                stmBody.setBranch(stmIter);

            } else {
                stmBody = new NormalCfgNode(astBody);
                stmCond.setTrue(stmBody);

                SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                stmBody.setBranch(stmSeparate);
                stmBody = stmSeparate;

                stmBody.setBranch(stmIter);
            }
            stmIter.setBranch(stmCond);
            stmCond.setFalse(scopeOut);
            posCfgStatement = scopeOut;

            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;
        }
        posLevel--;

    }

    private void visitIfStatement(IASTStatement stmAst) {

        posLevel++;

        CPPASTIfStatement stm = (CPPASTIfStatement) stmAst;
        // System.out.println(stm.getRawSignature() +
        // "\n----qwewqe------\n");
        IASTExpression astCon = stm.getConditionExpression();
        IASTStatement astThen = stm.getThenClause();
        IASTStatement astElse = stm.getElseClause();

        SeparatePointCfgNode stmSeparateBefor = new SeparatePointCfgNode();
        posCfgStatement.setBranch(stmSeparateBefor);
        posCfgStatement = stmSeparateBefor;

        if (levelCFG == 1)
            visitComplexStatementSimply(stm);
        else {
            // if (pos_level == 1) {
            ICfgNode befIf = new ForwardCfgNode();
            ICfgNode stmCon = new ForwardCfgNode();
            posCfgStatement.setBranch(befIf);

            if (astCon != null) {
                // CfgStatement stmTemp = new
                // CfgStatement(astCond.getRawSignature());
                stmCon = new NormalCfgNode(astCon);
                befIf.setBranch(stmCon);

            }

            ICfgNode endIf = new ForwardCfgNode();

            ICfgNode statementTrue = new ForwardCfgNode();
            ICfgNode statementFalse = new ForwardCfgNode();

            if (astThen != null)
                if (astThen instanceof CPPASTCompoundStatement) {
                    if (astThen.getChildren().length == 0)
                        stmCon.setTrue(endIf);
                    for (int i = 0; i < astThen.getChildren().length; i++) {
                        IASTNode stmChild = astThen.getChildren()[i];

                        if (i == 0 && isEndStatement(stmChild)) {
                            statementTrue = new NormalCfgNode(stmChild);
                            stmCon.setTrue(statementTrue);
                            statementTrue.setBranch(END);
                            break;
                        }

                        if (i == 0 && !(stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement)) {

                            if (posLevel < levelCFG) {

                                stmCon.setTrue(statementTrue);
                                posCfgStatement = statementTrue;
                                visitStatement((IASTStatement) stmChild);
                                statementTrue = posCfgStatement;
                            } else {
                                statementTrue = new NormalCfgNode(stmChild);
                                stmCon.setTrue(statementTrue);
                            }

                            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                            statementTrue.setBranch(stmSeparate);
                            statementTrue = stmSeparate;
                        }
                        if (i == 0 && (stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement)) {
                            statementTrue = new NormalCfgNode(stmChild);
                            stmCon.setTrue(statementTrue);
                        }
                        if (i != 0) {
                            if (isEndStatement(stmChild)) {
                                SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                statementTrue.setBranch(stmSeparateTemp);
                                statementTrue = stmSeparateTemp;

                                ICfgNode statementTemp = new NormalCfgNode(stmChild);
                                statementTrue.setBranch(statementTemp);
                                statementTemp.setBranch(END);
                                break;
                            }
                            if (stmChild instanceof CPPASTDeclarationStatement
                                    || stmChild instanceof CPPASTExpressionStatement) {
                                ICfgNode statementTemp = new NormalCfgNode(stmChild);
                                statementTrue.setBranch(statementTemp);
                                statementTrue = statementTemp;
                            } else {
                                SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                statementTrue.setBranch(stmSeparateTemp);
                                statementTrue = stmSeparateTemp;

                                if (posLevel < levelCFG) {

                                    posCfgStatement = statementTrue;
                                    visitStatement((IASTStatement) stmChild);
                                    statementTrue = posCfgStatement;
                                } else {
                                    ICfgNode statementTemp = new NormalCfgNode(stmChild);
                                    statementTrue.setBranch(statementTemp);
                                    statementTrue = statementTemp;
                                }

                                SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                                statementTrue.setBranch(stmSeparate);
                                statementTrue = stmSeparate;
                            }
                        }
                        if (i == astThen.getChildren().length - 1)
                            statementTrue.setBranch(endIf);
                    }
                } else if (isEndStatement(astThen)) {
                    statementTrue = new NormalCfgNode(astThen);
                    stmCon.setTrue(statementTrue);
                    statementTrue.setBranch(END);
                } else if (posLevel < levelCFG) {

                    stmCon.setTrue(statementTrue);
                    posCfgStatement = statementTrue;
                    visitStatement(astThen);
                    statementTrue = posCfgStatement;
                    statementTrue.setBranch(endIf);
                } else {
                    statementTrue = new NormalCfgNode(astThen);
                    stmCon.setTrue(statementTrue);
                    statementTrue.setBranch(endIf);
                }

            // stmSimple = null;
            if (astElse != null)
                if (astElse instanceof CPPASTCompoundStatement) {
                    if (astElse.getChildren().length == 0)
                        stmCon.setFalse(endIf);
                    for (int i = 0; i < astElse.getChildren().length; i++) {
                        IASTNode stmChild = astElse.getChildren()[i];

                        if (i == 0 && isEndStatement(stmChild)) {
                            statementFalse = new NormalCfgNode(stmChild);
                            stmCon.setFalse(statementFalse);
                            statementFalse.setBranch(END);
                            break;
                        }

                        if (i == 0 && !(stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement)) {

                            if (posLevel < levelCFG) {

                                stmCon.setFalse(statementFalse);
                                posCfgStatement = statementFalse;
                                visitStatement((IASTStatement) stmChild);
                                statementFalse = posCfgStatement;
                            } else {
                                statementFalse = new NormalCfgNode(stmChild);
                                stmCon.setFalse(statementFalse);
                            }

                            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                            statementFalse.setBranch(stmSeparate);
                            statementFalse = stmSeparate;
                        }
                        if (i == 0 && (stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement)) {
                            statementFalse = new NormalCfgNode(stmChild);
                            stmCon.setFalse(statementFalse);
                        }
                        if (i != 0) {
                            if (isEndStatement(stmChild)) {
                                SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                statementFalse.setBranch(stmSeparateTemp);
                                statementFalse = stmSeparateTemp;

                                ICfgNode statementTemp = new NormalCfgNode(stmChild);
                                statementFalse.setBranch(statementTemp);
                                statementTemp.setBranch(END);
                                break;
                            }

                            if (stmChild instanceof CPPASTDeclarationStatement
                                    || stmChild instanceof CPPASTExpressionStatement) {
                                ICfgNode statementTemp = new NormalCfgNode(stmChild);
                                statementFalse.setBranch(statementTemp);
                                statementFalse = statementTemp;
                            } else {
                                // stmSimple = null;
                                SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                statementFalse.setBranch(stmSeparateTemp);
                                statementFalse = stmSeparateTemp;

                                if (posLevel < levelCFG) {

                                    posCfgStatement = statementFalse;
                                    visitStatement((IASTStatement) stmChild);
                                    statementFalse = posCfgStatement;
                                } else {
                                    ICfgNode statementTemp = new NormalCfgNode(stmChild);
                                    statementFalse.setBranch(statementTemp);
                                    statementFalse = statementTemp;
                                }

                                SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                                statementFalse.setBranch(stmSeparate);
                                statementFalse = stmSeparate;
                            }
                        }
                        if (i == astElse.getChildren().length - 1)
                            statementFalse.setBranch(endIf);
                    }
                } else if (isEndStatement(astElse)) {
                    statementFalse = new NormalCfgNode(astElse);
                    stmCon.setFalse(statementFalse);
                    statementFalse.setBranch(END);
                } else if (posLevel < levelCFG) {

                    stmCon.setFalse(statementFalse);
                    posCfgStatement = statementFalse;
                    visitStatement(astElse);
                    statementFalse = posCfgStatement;
                    statementFalse.setBranch(endIf);
                } else {
                    statementFalse = new NormalCfgNode(astElse);
                    stmCon.setFalse(statementFalse);
                    statementFalse.setBranch(endIf);
                }
            if (astThen == null)
                stmCon.setTrue(endIf);
            if (astElse == null)
                stmCon.setFalse(endIf);
            posCfgStatement = endIf;

            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;
        }
        posLevel--;

    }

    /**
     * Check is CPPASTExpressionStatement is throw statement or not
     *
     * @param stmAst
     * @return
     */
    private boolean isThrowStatement(IASTStatement stmAst) {
        if (stmAst instanceof CPPASTExpressionStatement) {
            IASTExpression ex = ((IASTExpressionStatement) stmAst).getExpression();
            if (ex instanceof CPPASTUnaryExpression
                    && ((CPPASTUnaryExpression) ex).getOperator() == ICPPASTUnaryExpression.op_throw)
                return true;
        }
        return false;
    }

    /**
     * Visit simple statement like CPPASTExpressionStatement and
     * CPPASTDeclarationStatement
     *
     * @param stmAst
     */
    private void visitSimpleStatement(IASTStatement stmAst) {

        ICfgNode stmTemp = new NormalCfgNode(stmAst);
        posCfgStatement.setBranch(stmTemp);
        posCfgStatement = stmTemp;
    }

    /**
     * Visit general statement
     *
     * @param stmAst
     */
    private void visitStatement(IASTStatement stmAst) {
        if (stmAst instanceof CPPASTExpressionStatement || stmAst instanceof CPPASTDeclarationStatement) {
            posLevel++;
            visitSimpleStatement(stmAst);
            posLevel--;

        } else if (stmAst instanceof CPPASTForStatement)
            visitForStatement(stmAst);
        else if (stmAst instanceof CPPASTIfStatement)
            visitIfStatement(stmAst);
        else if (stmAst instanceof CPPASTWhileStatement)
            visitWhileStatement(stmAst);
        else if (stmAst instanceof CPPASTSwitchStatement)
            visitSwitchStatement(stmAst);
        else if (stmAst instanceof CPPASTDoStatement)
            visitDoStatement(stmAst);
        else if (stmAst instanceof CPPASTTryBlockStatement)
            visitTryBlockStatement(stmAst);
        else if (stmAst instanceof CPPASTReturnStatement) {
            CPPASTReturnStatement stm = (CPPASTReturnStatement) stmAst;

            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;

            visitSimpleStatement(stm);
        }
        /*
		 * Cau lenh dac biet nhu break
		 */
        else if (!(stmAst instanceof CPPASTCompoundStatement)) {
            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;

            visitComplexStatementSimply(stmAst);
        }
    }

    private void visitSwitchStatement(IASTStatement stmAst) {
        posLevel++;

        CPPASTSwitchStatement stm = (CPPASTSwitchStatement) stmAst;

        SeparatePointCfgNode stmSeparateBefore = new SeparatePointCfgNode();
        posCfgStatement.setBranch(stmSeparateBefore);
        posCfgStatement = stmSeparateBefore;

        if (levelCFG == 1)
            visitComplexStatementSimply(stm);
        else {
            IASTExpression astCon = stm.getControllerExpression();
            IASTStatement astNode = stm.getBody();
            ICfgNode stmDefault = null;
            ICfgNode stmEndDefault = null;
            ICfgNode stmEndSwitch = new ForwardCfgNode();
            ICfgNode stmBefSwitch = new ForwardCfgNode();
            boolean hasBreak = false;
            ArrayList<IASTNode> astBodyDefaults = new ArrayList<>();

            posCfgStatement.setBranch(stmBefSwitch);
            posCfgStatement = stmBefSwitch;

			/*
			 * Bien kiem tra xem co nen ve node switch hay khong
			 */
            int shouldDrawSwitch = 0;

			/*
			 * Tim node default bdau voi stmDefault va ket thuc tai node
			 * stmEndDefault
			 */
            for (int i = 0; i < astNode.getChildren().length; i++) {
                IASTNode astChild = astNode.getChildren()[i];

                if (astChild instanceof CPPASTDefaultStatement) {

					/*
					 * Kiem tra xem truoc default co break ko Neu khong thi
					 * khong ve duoc
					 */
                    if (i != 0) {
                        if (astNode.getChildren()[i - 1] instanceof CPPASTCompoundStatement)
                            for (int pos = 0; pos < astNode.getChildren()[i - 1].getChildren().length; pos++)
                                if (astNode.getChildren()[i - 1].getChildren()[pos] instanceof CPPASTBreakStatement)
                                    shouldDrawSwitch = 1;
                        if (astNode.getChildren()[i - 1] instanceof CPPASTBreakStatement)
                            shouldDrawSwitch = 1;
                    }
                    if (i == 0)
                        shouldDrawSwitch = 1;

					/*
					 * ktra node switch co ve duoc khong
					 */
                    if (shouldDrawSwitch == 0) {
                        visitComplexStatementSimply(stm);
                        return;
                    }

                    stmDefault = new ForwardCfgNode(); // stm bat dau default
                    stmEndDefault = new ForwardCfgNode();

                    ICfgNode stmBodyDefault = new ForwardCfgNode();

                    int fi = i;
                    while (!(astNode.getChildren()[fi + 1] instanceof CPPASTCaseStatement)) {
                        astBodyDefaults.add(astNode.getChildren()[fi + 1]);
                        fi++;
                        if (fi + 1 >= astNode.getChildren().length)
                            break;
                    }

                    for (int pos = 0; pos < astBodyDefaults.size(); pos++)
                        if (astBodyDefaults.get(pos) instanceof CPPASTBreakStatement) {
                            astBodyDefaults.remove(pos);
                            hasBreak = true;
                        }
                    for (int sec = 0; sec < astBodyDefaults.size(); sec++) {
                        IASTNode astChildTemp = astBodyDefaults.get(sec);
                        if (astChildTemp instanceof IASTCompoundStatement)
                            for (int pos = 0; pos < astChildTemp.getChildren().length; pos++) {
                                IASTNode stmChildDefaultSwitch = astChildTemp.getChildren()[pos];
                                if (sec == 0) {
                                    if (pos == 0 && isEndStatement(stmChildDefaultSwitch)) {
                                        stmBodyDefault = new NormalCfgNode(stmChildDefaultSwitch);
                                        stmDefault.setBranch(stmBodyDefault);
                                        stmBodyDefault.setBranch(END);
                                        hasBreak = true;
                                        stmBodyDefault = new ForwardCfgNode();
                                        break;
                                    }

                                    if (pos == 0 && stmChildDefaultSwitch instanceof CPPASTBreakStatement) {
                                        stmDefault.setBranch(stmEndDefault);
                                        stmEndDefault.setBranch(stmEndSwitch);
                                        hasBreak = true;
                                        break;
                                    }
                                    if (pos == 0 && !(stmChildDefaultSwitch instanceof CPPASTDeclarationStatement
                                            || stmChildDefaultSwitch instanceof CPPASTExpressionStatement)) {

                                        if (posLevel < levelCFG) {

                                            stmDefault.setBranch(stmBodyDefault);
                                            posCfgStatement = stmBodyDefault;
                                            visitStatement((IASTStatement) stmChildDefaultSwitch);
                                            stmBodyDefault = posCfgStatement;
                                        } else {
                                            stmBodyDefault = new NormalCfgNode(stmChildDefaultSwitch);
                                            stmDefault.setBranch(stmBodyDefault);
                                        }

                                        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                                        stmBodyDefault.setBranch(stmSeparate);
                                        stmBodyDefault = stmSeparate;
                                    }
                                    if (pos == 0 && (stmChildDefaultSwitch instanceof CPPASTDeclarationStatement
                                            || stmChildDefaultSwitch instanceof CPPASTExpressionStatement)) {
                                        stmBodyDefault = new NormalCfgNode(stmChildDefaultSwitch);
                                        stmDefault.setBranch(stmBodyDefault);
                                    }
                                    if (pos != 0) {
                                        if (isEndStatement(stmChildDefaultSwitch)) {
                                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                            stmBodyDefault.setBranch(stmSeparateTemp);
                                            stmBodyDefault = stmSeparateTemp;

                                            ICfgNode statementTemp = new NormalCfgNode(stmChildDefaultSwitch);
                                            stmBodyDefault.setBranch(statementTemp);
                                            statementTemp.setBranch(END);
                                            hasBreak = true;
                                            stmBodyDefault = new ForwardCfgNode();
                                            break;
                                        }

                                        if (stmChildDefaultSwitch instanceof CPPASTBreakStatement) {
                                            stmBodyDefault.setBranch(stmEndDefault);
                                            stmEndDefault.setBranch(stmEndSwitch);
                                            hasBreak = true;
                                            break;
                                        }
                                        if (stmChildDefaultSwitch instanceof CPPASTDeclarationStatement
                                                || stmChildDefaultSwitch instanceof CPPASTExpressionStatement) {
                                            ICfgNode stmTemp = new NormalCfgNode(stmChildDefaultSwitch);
                                            stmBodyDefault.setBranch(stmTemp);
                                            stmBodyDefault = stmTemp;
                                        } else {
                                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                            stmBodyDefault.setBranch(stmSeparateTemp);
                                            stmBodyDefault = stmSeparateTemp;

                                            if (posLevel < levelCFG) {

                                                posCfgStatement = stmBodyDefault;
                                                visitStatement((IASTStatement) stmChildDefaultSwitch);
                                                stmBodyDefault = posCfgStatement;
                                            } else {
                                                ICfgNode stmTemp = new NormalCfgNode(stmChildDefaultSwitch);
                                                stmBodyDefault.setBranch(stmTemp);
                                                stmBodyDefault = stmTemp;

                                            }

                                            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                                            stmBodyDefault.setBranch(stmSeparate);
                                            stmBodyDefault = stmSeparate;
                                        }
                                    }
                                } else {
                                    if (isEndStatement(stmChildDefaultSwitch)) {
                                        SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                        stmBodyDefault.setBranch(stmSeparateTemp);
                                        stmBodyDefault = stmSeparateTemp;

                                        ICfgNode statementTemp = new NormalCfgNode(stmChildDefaultSwitch);
                                        stmBodyDefault.setBranch(statementTemp);
                                        statementTemp.setBranch(END);
                                        hasBreak = true;
                                        stmBodyDefault = new ForwardCfgNode();
                                        break;
                                    }

                                    if (stmChildDefaultSwitch instanceof CPPASTBreakStatement) {
                                        stmBodyDefault.setBranch(stmEndDefault);
                                        stmEndDefault.setBranch(stmEndSwitch);
                                        hasBreak = true;
                                        break;
                                    }
                                    if (stmChildDefaultSwitch instanceof CPPASTDeclarationStatement
                                            || stmChildDefaultSwitch instanceof CPPASTExpressionStatement) {
                                        ICfgNode stmTemp = new NormalCfgNode(stmChildDefaultSwitch);
                                        stmBodyDefault.setBranch(stmTemp);
                                        stmBodyDefault = stmTemp;
                                    } else {
                                        SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                        stmBodyDefault.setBranch(stmSeparateTemp);
                                        stmBodyDefault = stmSeparateTemp;

                                        if (posLevel < levelCFG) {

                                            posCfgStatement = stmBodyDefault;
                                            visitStatement((IASTStatement) stmChildDefaultSwitch);
                                            stmBodyDefault = posCfgStatement;
                                        } else {
                                            ICfgNode stmTemp = new NormalCfgNode(stmChildDefaultSwitch);
                                            stmBodyDefault.setBranch(stmTemp);
                                            stmBodyDefault = stmTemp;
                                        }

                                        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                                        stmBodyDefault.setBranch(stmSeparate);
                                        stmBodyDefault = stmSeparate;
                                    }
                                }
                                if (pos == astChildTemp.getChildren().length - 1 && sec == astBodyDefaults.size() - 1)
                                    stmBodyDefault.setBranch(stmEndDefault);
                            }
                        else {
                            if (sec == 0) {
                                if (isEndStatement(astChildTemp)) {
                                    stmBodyDefault = new NormalCfgNode(astChildTemp);
                                    stmDefault.setBranch(stmBodyDefault);
                                    stmBodyDefault.setBranch(END);
                                    stmBodyDefault = new ForwardCfgNode();
                                    break;
                                } else if (posLevel < levelCFG) {
                                    stmDefault.setBranch(stmBodyDefault);
                                    posCfgStatement = stmBodyDefault;
                                    visitStatement((IASTStatement) astChildTemp);
                                    stmBodyDefault = posCfgStatement;
                                } else {
                                    stmBodyDefault = new NormalCfgNode(astChildTemp);
                                    stmDefault.setBranch(stmBodyDefault);
                                }
                            } else if (isEndStatement(astChildTemp)) {
                                ICfgNode stmTemp = new NormalCfgNode(astChildTemp);
                                stmBodyDefault.setBranch(stmTemp);
                                stmTemp.setBranch(END);
                                stmBodyDefault = new ForwardCfgNode();
                                break;
                            } else if (posLevel < levelCFG) {
                                posCfgStatement = stmBodyDefault;
                                visitStatement((IASTStatement) astChildTemp);
                                stmBodyDefault = posCfgStatement;
                            } else {
                                ICfgNode stmTemp = new NormalCfgNode(astChildTemp);
                                stmBodyDefault.setBranch(stmTemp);
                                stmBodyDefault = stmTemp;
                            }
                            if (sec == astBodyDefaults.size() - 1)
                                stmBodyDefault.setBranch(stmEndDefault);
                        }
                    }
                    if (hasBreak)
                        stmEndDefault.setBranch(stmEndSwitch);
                    // cau lenh default o cuoi cung k co break
                    if (i + 1 > astNode.getChildren().length)
                        stmEndDefault.setBranch(stmEndSwitch);
                    break;
                }
                if (i == astNode.getChildren().length - astBodyDefaults.size() - 1)
                    shouldDrawSwitch = 1;
            }

            ICfgNode stmCase = null;
            ICfgNode stmCaseBody = new ForwardCfgNode();
            int afterDefault = 0;

            ArrayList<IASTNode> astNodes = new ArrayList<>();
            // Danh dau neu default o dau va bat cau lenh
            // case phia sau
            int isCaseAfterFirstDefault = 0;

            for (int i = 0; i < astNode.getChildren().length; i++) {
                if (i == 0 && astNode.getChildren()[i] instanceof CPPASTDefaultStatement) {
                    isCaseAfterFirstDefault = 1;
                    if (stmEndDefault.getTrueNode() == null) {
                        afterDefault = 1;
                        i += astBodyDefaults.size() + 1;
                        if (i > astNode.getChildren().length) {
                            stmEndDefault.setBranch(stmEndSwitch);
                            break;
                        }
                    }
                    // cau lenh default co break
                    if (stmEndDefault.getTrueNode() != null) {
                        i += astBodyDefaults.size() + 1;
                        if (i >= astNode.getChildren().length)
                            break;

                    }
                }

                if (i == 0 && astNode.getChildren()[i] instanceof CPPASTCaseStatement) {
                    IASTNode astChild = astNode.getChildren()[i];
                    int isBreak = 0;
                    while (!(astChild instanceof CPPASTBreakStatement)) {
                        astNodes.add(astChild);
                        if (astChild instanceof CPPASTCompoundStatement)
                            for (int pos = 0; pos < astChild.getChildren().length; pos++)
                                if (astChild.getChildren()[pos] instanceof CPPASTBreakStatement) {
                                    isBreak++;
                                    i++;
                                }
                        if (isBreak != 0)
                            break;
                        i++;
                        astChild = astNode.getChildren()[i];
                    }

                    for (int pos = 0; pos < astNodes.size(); pos++) {
                        IASTNode astNodePos = astNodes.get(pos);
                        if (pos == 0) {
                            stmCase = new CfgNode(astCon.getRawSignature() + "=="
                                    + astNodes.get(pos).getChildren()[0].getRawSignature());
                            stmBefSwitch.setBranch(stmCase);

                            int temp = 1;
                            while (astNodes.get(pos + temp) instanceof CPPASTCaseStatement) {
                                stmCase.setContent(stmCase.getContent() + "||" + astCon.getRawSignature() + "=="
                                        + astNodes.get(pos + temp).getChildren()[0].getRawSignature());
                                temp++;
                            }
                            if (temp > 1) {
                                pos += temp;
                                astNodePos = astNodes.get(pos);
                            }
                        }

                        if (pos != 0 && astNodes.get(pos) instanceof CPPASTCaseStatement) {
                            ICfgNode stmTemp = new CfgNode(astCon.getRawSignature() + "=="
                                    + astNodes.get(pos).getChildren()[0].getRawSignature());
                            stmCase.setFalse(stmTemp);
                            stmCase = stmTemp;

                            int temp = 1;
                            while (astNodes.get(pos + temp) instanceof CPPASTCaseStatement) {
                                stmCase.setContent(stmCase.getContent() + "||" + astCon.getRawSignature() + "=="
                                        + astNodes.get(pos + temp).getChildren()[0].getRawSignature());
                                temp++;
                            }
                            if (temp > 1) {
                                pos += temp;
                                astNodePos = astNodes.get(pos);
                            }
                        }
                        if (!(astNodes.get(pos) instanceof CPPASTCaseStatement)) {
                            if (stmCaseBody instanceof ForwardCfgNode)
                                stmCase.setTrue(stmCaseBody);

                            if (astNodePos instanceof IASTCompoundStatement)
                                for (int first = 0; first < astNodePos.getChildren().length; first++) {
                                    IASTNode stmChild = astNodePos.getChildren()[first];

                                    if (isEndStatement(stmChild)) {
                                        SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                        stmCaseBody.setBranch(stmSeparateTemp);
                                        stmCaseBody = stmSeparateTemp;

                                        ICfgNode statementTemp = new NormalCfgNode(stmChild);
                                        stmCaseBody.setBranch(statementTemp);
                                        statementTemp.setBranch(END);

                                        if (first == 0 && astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                            stmCase.setTrue(stmCaseBody);

                                        stmCaseBody = ScopeCfgNode.newCloseScope();
                                        break;
                                    }

                                    if (!(stmChild instanceof CPPASTDeclarationStatement
                                            || stmChild instanceof CPPASTExpressionStatement)) {

                                        SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                        stmCaseBody.setBranch(stmSeparateTemp);
                                        stmCaseBody = stmSeparateTemp;

                                        if (first == 0 && astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                            stmCase.setTrue(stmCaseBody);

                                        if (posLevel < levelCFG) {

                                            posCfgStatement = stmCaseBody;
                                            visitStatement((IASTStatement) stmChild);
                                            stmCaseBody = posCfgStatement;
                                        } else {
                                            ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                            stmCaseBody.setBranch(stmTemp);
                                            stmCaseBody = stmTemp;
                                        }

                                        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                                        stmCaseBody.setBranch(stmSeparate);
                                        stmCaseBody = stmSeparate;
                                    }
                                    if (stmChild instanceof CPPASTDeclarationStatement
                                            || stmChild instanceof CPPASTExpressionStatement) {
                                        ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                        stmCaseBody.setBranch(stmTemp);
                                        stmCaseBody = stmTemp;

                                        if (first == 0 && astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                            stmCase.setTrue(stmCaseBody);
                                    }

                                }
                            else if (isEndStatement(astNodePos)) {
                                ICfgNode statementTemp = new NormalCfgNode(astNodePos);
                                stmCaseBody.setBranch(statementTemp);
                                statementTemp.setBranch(END);

                                if (astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                    stmCase.setTrue(statementTemp);

                                stmCaseBody = ScopeCfgNode.newCloseScope();
                            } else {
                                if (posLevel < levelCFG) {
                                    posCfgStatement = stmCaseBody;
                                    visitStatement((IASTStatement) astNodePos);
                                    stmCaseBody = posCfgStatement;
                                } else {
                                    ICfgNode stmTemp = new CfgNode(astNodes.get(pos).getRawSignature());
                                    stmCaseBody.setBranch(stmTemp);
                                    stmCaseBody = stmTemp;
                                }
                                if (astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                    stmCase.setTrue(stmCaseBody);
                            }

                        }
                        if (pos == astNodes.size() - 1) {
                            stmCaseBody.setBranch(stmEndSwitch);
                            astNodes.clear();
                            stmCaseBody = new ForwardCfgNode();
                        }
                    }
                }

				/*
				 * TH trc default co break va default co the co break hoac ko
				 */
                if (i != 0 && astNode.getChildren()[i] instanceof CPPASTDefaultStatement) {
                    if (!hasBreak) {
                        afterDefault = 1;
                        i += astBodyDefaults.size() + 1;
                        if (i >= astNode.getChildren().length) {
                            stmEndDefault.setBranch(stmEndSwitch);
                            break;
                        }
                    }
                    // cau lenh default co break
                    if (hasBreak) {
                        i += astBodyDefaults.size() + 1;
                        if (i >= astNode.getChildren().length)
                            break;

                    }
                }
                if (i != 0 && !(astNode.getChildren()[i] instanceof CPPASTDefaultStatement)) {
                    IASTNode astChild = astNode.getChildren()[i];
                    int isBreak = 0;
                    while (!(astChild instanceof CPPASTBreakStatement)) {
                        astNodes.add(astChild);
                        if (astChild instanceof CPPASTCompoundStatement)
                            for (int pos = 0; pos < astChild.getChildren().length; pos++)
                                if (astChild.getChildren()[pos] instanceof CPPASTBreakStatement)
                                    isBreak++; // i++;
                        if (isBreak != 0)
                            break;
                        i++;
                        if (i >= astNode.getChildren().length)
                            break;
                        astChild = astNode.getChildren()[i];
                    }

                    for (int pos = 0; pos < astNodes.size(); pos++) {
                        IASTNode astNodePos = astNodes.get(pos);
                        if (astNodes.get(pos) instanceof CPPASTCaseStatement) {
                            if (astNode.getChildren()[0] instanceof CPPASTDefaultStatement
                                    && isCaseAfterFirstDefault == 1) {
                                stmCase = new CfgNode(astCon.getRawSignature() + "=="
                                        + astNodes.get(pos).getChildren()[0].getRawSignature());
                                stmBefSwitch.setBranch(stmCase);
                                isCaseAfterFirstDefault++;
                            } else {
                                ICfgNode stmTemp = new CfgNode(astCon.getRawSignature() + "=="
                                        + astNodes.get(pos).getChildren()[0].getRawSignature());
                                stmCase.setFalse(stmTemp);
                                stmCase = stmTemp;
                            }
                            int temp = 1;
                            while (astNodes.get(pos + temp) instanceof CPPASTCaseStatement) {
                                stmCase.setContent(stmCase.getContent() + "||" + astCon.getRawSignature() + "=="
                                        + astNodes.get(pos + temp).getChildren()[0].getRawSignature());
                                temp++;
                            }
                            if (temp > 1) {
                                pos += temp;
                                astNodePos = astNodes.get(pos);
                            }

                        }
                        if (!(astNodes.get(pos) instanceof CPPASTCaseStatement)) {
                            if (stmCaseBody instanceof ForwardCfgNode) {
                                stmCase.setTrue(stmCaseBody);

                                if (afterDefault == 1) {
                                    stmEndDefault.setBranch(stmCaseBody);
                                    afterDefault = 0;
                                }
                            }

                            if (astNodePos instanceof IASTCompoundStatement)
                                for (int first = 0; first < astNodePos.getChildren().length; first++) {
                                    IASTNode stmChild = astNodePos.getChildren()[first];

                                    if (isEndStatement(stmChild)) {
                                        SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                        stmCaseBody.setBranch(stmSeparateTemp);
                                        stmCaseBody = stmSeparateTemp;

                                        ICfgNode statementTemp = new NormalCfgNode(stmChild);
                                        stmCaseBody.setBranch(statementTemp);
                                        statementTemp.setBranch(END);

                                        if (first == 0 && astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                            stmCase.setTrue(stmCaseBody);

                                        stmCaseBody = ScopeCfgNode.newCloseScope();
                                        break;
                                    }

                                    if (!(stmChild instanceof CPPASTDeclarationStatement
                                            || stmChild instanceof CPPASTExpressionStatement)) {

                                        SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                        stmCaseBody.setBranch(stmSeparateTemp);
                                        stmCaseBody = stmSeparateTemp;

                                        if (first == 0 && astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                            stmCase.setTrue(stmCaseBody);

                                        if (posLevel < levelCFG) {

                                            posCfgStatement = stmCaseBody;
                                            visitStatement((IASTStatement) stmChild);
                                            stmCaseBody = posCfgStatement;
                                        } else {
                                            ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                            stmCaseBody.setBranch(stmTemp);
                                            stmCaseBody = stmTemp;
                                        }

                                        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                                        stmCaseBody.setBranch(stmSeparate);
                                        stmCaseBody = stmSeparate;
                                    }
                                    if (stmChild instanceof CPPASTDeclarationStatement
                                            || stmChild instanceof CPPASTExpressionStatement) {

                                        ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                        stmCaseBody.setBranch(stmTemp);
                                        stmCaseBody = stmTemp;

                                        if (first == 0 && astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                            stmCase.setTrue(stmCaseBody);
                                    }

                                }
                            else if (isEndStatement(astNodePos)) {
                                ICfgNode statementTemp = new NormalCfgNode(astNodePos);
                                stmCaseBody.setBranch(statementTemp);
                                statementTemp.setBranch(END);

                                if (astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                    stmCase.setTrue(statementTemp);

                                stmCaseBody = ScopeCfgNode.newCloseScope();
                            } else {
                                if (posLevel < levelCFG) {
                                    posCfgStatement = stmCaseBody;
                                    visitStatement((IASTStatement) astNodePos);
                                    stmCaseBody = posCfgStatement;
                                } else {
                                    ICfgNode stmTemp = new CfgNode(astNodes.get(pos).getRawSignature());
                                    stmCaseBody.setBranch(stmTemp);
                                    stmCaseBody = stmTemp;
                                }
                                if (astNodes.get(pos - 1) instanceof CPPASTCaseStatement)
                                    stmCase.setTrue(stmCaseBody);
                            }

                        }
                        if (pos == astNodes.size() - 1) {
                            stmCaseBody.setBranch(stmEndSwitch);
                            astNodes.clear();
                            stmCaseBody = new ForwardCfgNode();
                        }
                    }

                }
            }

            stmCase.setFalse(stmDefault);
            posCfgStatement = stmEndSwitch;

            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;

            posLevel--;

        }

    }

    /**
     * check if statement is end statement or not (return or throw statement)
     *
     * @param stmAst
     */
    private boolean isEndStatement(IASTNode stmAst) {
        if (stmAst instanceof CPPASTReturnStatement)
            return true;
        /**
         * if throw statement not in try block
         */
        if (!isInTryBlock && isThrowStatement((IASTStatement) stmAst))
            return true;
        return false;
    }

    private void visitTryBlockStatement(IASTStatement stmAst) {
        isInTryBlock = true;
        posLevel++;

        CPPASTTryBlockStatement stm = (CPPASTTryBlockStatement) stmAst;

        SeparatePointCfgNode stmSeparateBefore = new SeparatePointCfgNode();
        posCfgStatement.setBranch(stmSeparateBefore);
        posCfgStatement = stmSeparateBefore;
        MarkFlagCfgNode eTry = new MarkFlagCfgNode("start try");

        MarkFlagCfgNode eCatch = new MarkFlagCfgNode("catch"), endTry = new MarkFlagCfgNode("end catch");
        posCfgStatement.setBranch(eTry);
        ArrayList<ICfgNode> listTarget = new ArrayList<>();

        if (levelCFG == 1) {
            ICfgNode stmTry = new NormalCfgNode(stm.getTryBody());
            eTry.setBranch(stmTry);
            stmTry.setBranch(eCatch);

            for (ICPPASTCatchHandler catcher : stm.getCatchHandlers()) {
                String content = catcher.isCatchAll() ? "..." : catcher.getDeclaration().getRawSignature();
                MarkFlagCfgNode eType = new MarkFlagCfgNode("[" + content + "]");
                listTarget.add(eType);
                eType.setInBlock(true);
                eType.setInSameLine(false);
                ICfgNode temp = new NormalCfgNode(catcher.getCatchBody());
                eType.setBranch(temp);
                temp.setBranch(endTry);
            }
            eCatch.setListTarget(listTarget);
            posCfgStatement = endTry;

            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;

        } else {
            IASTStatement astTry = stm.getTryBody();
            ICfgNode stmTry = new ForwardCfgNode();

            if (astTry instanceof IASTCompoundStatement)
                for (int i = 0; i < astTry.getChildren().length; i++) {
                    IASTNode stmChild = astTry.getChildren()[i];
                    if (i == 0 && !(stmChild instanceof CPPASTDeclarationStatement
                            || stmChild instanceof CPPASTExpressionStatement)) {
                        if (posLevel < levelCFG) {

                            eTry.setBranch(stmTry);
                            posCfgStatement = stmTry;
                            visitStatement((IASTStatement) stmChild);
                            stmTry = posCfgStatement;
                        } else {
                            stmTry = new NormalCfgNode(stmChild);
                            eTry.setBranch(stmTry);
                        }

                        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                        stmTry.setBranch(stmSeparate);
                        stmTry = stmSeparate;
                    }
                    if (i == 0 && (stmChild instanceof CPPASTDeclarationStatement
                            || stmChild instanceof CPPASTExpressionStatement)) {
                        stmTry = new NormalCfgNode(stmChild);
                        eTry.setBranch(stmTry);
                    }
                    if (i != 0)
                        if (stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement) {
                            ICfgNode stmTemp = new NormalCfgNode(stmChild);
                            stmTry.setBranch(stmTemp);
                            stmTry = stmTemp;
                        } else {
                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                            stmTry.setBranch(stmSeparateTemp);
                            stmTry = stmSeparateTemp;

                            if (posLevel < levelCFG) {

                                posCfgStatement = stmTry;
                                visitStatement((IASTStatement) stmChild);
                                stmTry = posCfgStatement;
                            } else {
                                ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                stmTry.setBranch(stmTemp);
                                stmTry = stmTemp;
                            }

                            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                            stmTry.setBranch(stmSeparate);
                            stmTry = stmSeparate;
                        }
                    if (i == astTry.getChildren().length - 1)
                        stmTry.setBranch(eCatch);
                }
            else if (posLevel < levelCFG) {
                eTry.setBranch(stmTry);
                posCfgStatement = stmTry;
                visitStatement(astTry);
                stmTry = posCfgStatement;
                stmTry.setBranch(eCatch);
            } else {
                stmTry = new NormalCfgNode(astTry);
                eTry.setBranch(stmTry);
                stmTry.setBranch(eCatch);
            }
            for (ICPPASTCatchHandler catcher : stm.getCatchHandlers()) {
                String content = catcher.isCatchAll() ? "..." : catcher.getDeclaration().getRawSignature();
                MarkFlagCfgNode eType = new MarkFlagCfgNode("[" + content + "]");
                listTarget.add(eType);
                eType.setInBlock(true);
                eType.setInSameLine(false);

                IASTStatement astCatch = catcher.getCatchBody();
                ICfgNode stmCatch = new ForwardCfgNode();

                if (astCatch instanceof IASTCompoundStatement) {
                    if (astCatch.getChildren().length == 0)
                        eType.setBranch(endTry);
                    for (int i = 0; i < astCatch.getChildren().length; i++) {
                        IASTNode stmChild = astCatch.getChildren()[i];

                        if (i == 0 && isEndStatement(stmChild)) {
                            stmCatch = new NormalCfgNode(stmChild);
                            eType.setBranch(stmCatch);
                            stmCatch.setBranch(END);
                            break;
                        }

                        if (i == 0 && !(stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement)) {

                            if (posLevel < levelCFG) {

                                eType.setBranch(stmCatch);
                                posCfgStatement = stmCatch;
                                visitStatement((IASTStatement) stmChild);
                                stmCatch = posCfgStatement;
                            } else {
                                stmCatch = new NormalCfgNode(stmChild);
                                eType.setBranch(stmCatch);
                            }

                            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                            stmCatch.setBranch(stmSeparate);
                            stmCatch = stmSeparate;
                        }
                        if (i == 0 && (stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement)) {
                            stmCatch = new NormalCfgNode(stmChild);
                            eType.setBranch(stmCatch);
                        }
                        if (i != 0) {
                            if (isEndStatement(stmChild)) {
                                SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                stmCatch.setBranch(stmSeparateTemp);
                                stmCatch = stmSeparateTemp;

                                ICfgNode statementTemp = new NormalCfgNode(stmChild);
                                stmCatch.setBranch(statementTemp);
                                statementTemp.setBranch(END);
                                break;
                            }

                            if (stmChild instanceof CPPASTDeclarationStatement
                                    || stmChild instanceof CPPASTExpressionStatement) {
                                ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                stmCatch.setBranch(stmTemp);
                                stmCatch = stmTemp;
                            } else {
                                SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                                stmCatch.setBranch(stmSeparateTemp);
                                stmCatch = stmSeparateTemp;

                                if (posLevel < levelCFG) {

                                    posCfgStatement = stmCatch;
                                    visitStatement((IASTStatement) stmChild);
                                    stmCatch = posCfgStatement;
                                } else {
                                    ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                    stmCatch.setBranch(stmTemp);
                                    stmCatch = stmTemp;
                                }

                                SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                                stmCatch.setBranch(stmSeparate);
                                stmCatch = stmSeparate;
                            }
                        }
                        if (i == astCatch.getChildren().length - 1)
                            stmCatch.setBranch(endTry);
                    }
                } else if (isEndStatement(astCatch)) {
                    stmCatch = new NormalCfgNode(astCatch);
                    eType.setBranch(stmCatch);
                    stmCatch.setBranch(END);
                } else if (posLevel < levelCFG) {
                    eType.setBranch(stmCatch);
                    posCfgStatement = stmCatch;
                    visitStatement(astCatch);
                    stmCatch = posCfgStatement;
                    stmCatch.setBranch(endTry);

                } else {
                    ICfgNode stmCatchBody = new NormalCfgNode(astCatch);
                    eType.setBranch(stmCatchBody);
                    stmCatchBody.setBranch(endTry);
                }

            }
            eCatch.setListTarget(listTarget);
            posCfgStatement = endTry;

            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;
        }
        posLevel--;
        isInTryBlock = false;
    }

    private void visitWhileStatement(IASTStatement stmAst) {

        posLevel++;

        CPPASTWhileStatement stm = (CPPASTWhileStatement) stmAst;

        SeparatePointCfgNode stmSeparateBefor = new SeparatePointCfgNode();
        posCfgStatement.setBranch(stmSeparateBefor);
        posCfgStatement = stmSeparateBefor;

        if (levelCFG == 1)
            visitComplexStatementSimply(stm);
        else {

            IASTExpression astCon = stm.getCondition();
            IASTStatement astBody = stm.getBody();

            ICfgNode stmCon = new NormalCfgNode(astCon);
            posCfgStatement.setBranch(stmCon);

            ICfgNode endWhile = new ForwardCfgNode();
            ICfgNode stmBody = new ForwardCfgNode();

            if (astBody instanceof IASTCompoundStatement)
                for (int i = 0; i < astBody.getChildren().length; i++) {
                    IASTNode stmChild = astBody.getChildren()[i];

                    if (i == 0 && isEndStatement(stmChild)) {
                        stmBody = new NormalCfgNode(stmChild);
                        stmCon.setTrue(stmBody);
                        stmBody.setBranch(END);
                        break;
                    }

                    if (i == 0 && !(stmChild instanceof CPPASTDeclarationStatement
                            || stmChild instanceof CPPASTExpressionStatement)) {

                        if (posLevel < levelCFG) {

                            stmCon.setTrue(stmBody);
                            posCfgStatement = stmBody;
                            visitStatement((IASTStatement) stmChild);
                            stmBody = posCfgStatement;
                        } else {
                            stmBody = new NormalCfgNode(stmChild);
                            stmCon.setTrue(stmBody);
                        }

                        SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                        stmBody.setBranch(stmSeparate);
                        stmBody = stmSeparate;
                    }
                    if (i == 0 && (stmChild instanceof CPPASTDeclarationStatement
                            || stmChild instanceof CPPASTExpressionStatement)) {
                        stmBody = new NormalCfgNode(stmChild);
                        stmCon.setTrue(stmBody);

                    }
                    if (i != 0) {
                        if (isEndStatement(stmChild)) {
                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparateTemp);
                            stmBody = stmSeparateTemp;

                            ICfgNode statementTemp = new NormalCfgNode(stmChild);
                            stmBody.setBranch(statementTemp);
                            statementTemp.setBranch(END);
                            break;
                        }
                        if (stmChild instanceof CPPASTDeclarationStatement
                                || stmChild instanceof CPPASTExpressionStatement) {
                            ICfgNode stmTemp = new NormalCfgNode(stmChild);
                            stmBody.setBranch(stmTemp);
                            stmBody = stmTemp;
                        } else {
                            SeparatePointCfgNode stmSeparateTemp = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparateTemp);
                            stmBody = stmSeparateTemp;

                            if (posLevel < levelCFG) {

                                posCfgStatement = stmBody;
                                visitStatement((IASTStatement) stmChild);
                                stmBody = posCfgStatement;
                            } else {
                                ICfgNode stmTemp = new NormalCfgNode(stmChild);
                                stmBody.setBranch(stmTemp);
                                stmBody = stmTemp;
                            }

                            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
                            stmBody.setBranch(stmSeparate);
                            stmBody = stmSeparate;
                        }
                    }
                    if (i == astBody.getChildren().length - 1)
                        stmBody.setBranch(stmCon);
                }
            else if (isEndStatement(astBody)) {
                stmBody = new NormalCfgNode(astBody);
                stmCon.setTrue(stmBody);
                stmBody.setBranch(END);

            } else if (posLevel < levelCFG) {
                stmCon.setTrue(stmBody);
                posCfgStatement = stmBody;
                visitStatement(astBody);
                stmBody = posCfgStatement;
                stmBody.setBranch(stmCon);

            } else {
                stmBody = new NormalCfgNode(astBody);
                stmCon.setTrue(stmBody);
                stmBody.setBranch(stmCon);
            }
            stmCon.setFalse(endWhile);
            posCfgStatement = endWhile;

            SeparatePointCfgNode stmSeparate = new SeparatePointCfgNode();
            posCfgStatement.setBranch(stmSeparate);
            posCfgStatement = stmSeparate;
        }
        posLevel--;
    }

}
