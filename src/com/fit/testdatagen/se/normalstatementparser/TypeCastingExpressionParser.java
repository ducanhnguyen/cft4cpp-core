package com.fit.testdatagen.se.normalstatementparser;

import com.fit.testdatagen.se.memory.VariableNodeTable;
import org.eclipse.cdt.core.dom.ast.IASTNode;

/**
 * Ex: c = (char) x
 *
 * @author ducanhnguyen
 */
public class TypeCastingExpressionParser extends NormalBinaryAssignmentParser {
    @Override
    public void parse(IASTNode ast, VariableNodeTable table) throws Exception {

        super.parse(ast, table);
    }
}
