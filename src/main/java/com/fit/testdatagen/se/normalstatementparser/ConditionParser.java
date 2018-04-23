package com.fit.testdatagen.se.normalstatementparser;

import com.fit.testdatagen.se.ExpressionRewriterUtils;
import com.fit.testdatagen.se.memory.VariableNodeTable;
import com.fit.utils.Utils;
import org.eclipse.cdt.core.dom.ast.IASTNode;

public class ConditionParser extends StatementParser {

    private String newConstraint = "";

    @Override
    public void parse(IASTNode ast, VariableNodeTable table) throws Exception {
        ast = Utils.shortenAstNode(ast);
        newConstraint = ExpressionRewriterUtils.rewrite(table, ast.getRawSignature());
    }

    public String getNewConstraint() {
        return newConstraint;
    }

    public void setNewConstraint(String newConstraint) {
        this.newConstraint = newConstraint;
    }
}
