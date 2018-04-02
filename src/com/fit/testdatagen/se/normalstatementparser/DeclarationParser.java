package com.fit.testdatagen.se.normalstatementparser;

import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTArrayModifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTPointer;

import com.fit.testdatagen.se.memory.CharacterSymbolicVariable;
import com.fit.testdatagen.se.memory.EnumSymbolicVariable;
import com.fit.testdatagen.se.memory.NumberSymbolicVariable;
import com.fit.testdatagen.se.memory.OneDimensionCharacterSymbolicVariable;
import com.fit.testdatagen.se.memory.OneDimensionNumberSymbolicVariable;
import com.fit.testdatagen.se.memory.OneLevelCharacterSymbolicVariable;
import com.fit.testdatagen.se.memory.OneLevelNumberSymbolicVariable;
import com.fit.testdatagen.se.memory.StructSymbolicVariable;
import com.fit.testdatagen.se.memory.SymbolicVariable;
import com.fit.testdatagen.se.memory.UnionSymbolicVariable;
import com.fit.testdatagen.se.memory.VariableNodeTable;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.object.ClassNode;
import com.fit.tree.object.EnumNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.IVariableNode;
import com.fit.tree.object.StructNode;
import com.fit.tree.object.UnionNode;
import com.fit.tree.object.VariableNode;
import com.fit.utils.ASTUtils;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.Utils;

/**
 * Ex1: "int a = 2;" <br/>
 * Ex2: "int a = y+z;" <br/>
 *
 * @author ducanhnguyen
 */
public class DeclarationParser extends StatementParser {
	private IFunctionNode function;
	/**
	 * The current scope of the statement. The value of global scope is equivalent
	 * to zero.
	 */
	private int scopeLevel = 0;

	@Override
	public void parse(IASTNode ast, VariableNodeTable table) throws Exception {
		ast = Utils.shortenAstNode(ast);
		if (ast instanceof IASTSimpleDeclaration && function != null)
			parseDeclaration((IASTSimpleDeclaration) ast, table, scopeLevel, function);
	}

	public void parseDeclaration(IASTSimpleDeclaration stm3, VariableNodeTable table, int scopeLevel,
			IFunctionNode function) throws Exception {

		for (IASTDeclarator declarator : stm3.getDeclarators()) {
			String type = getType(stm3, declarator);
			type = Utils.getRealType(type, function);

			String name = declarator.getName().getRawSignature();

			SymbolicVariable v = null;

			if (VariableTypes.isNumBasic(type)) {
				String defaultValue = "0";
				v = new NumberSymbolicVariable(name, type, scopeLevel, defaultValue);

			} else if (VariableTypes.isChBasic(type)) {
				String defaultValue = "0";
				v = new CharacterSymbolicVariable(name, type, scopeLevel, defaultValue);

			} else if (VariableTypes.isNumOneDimension(type))
				v = new OneDimensionNumberSymbolicVariable(name, type, scopeLevel);

			else if (VariableTypes.isChOneDimension(type))
				v = new OneDimensionCharacterSymbolicVariable(name, type, scopeLevel);

			else if (VariableTypes.isNumOneLevel(type))
				v = new OneLevelNumberSymbolicVariable(name, type, scopeLevel);

			else if (VariableTypes.isChOneLevel(type))
				v = new OneLevelCharacterSymbolicVariable(name, type, scopeLevel);

			else if (VariableTypes.isStructureSimple(type)) {
				IVariableNode var = new VariableNode();
				var.setParent(function);
				var.setRawType(type);
				INode correspondingNode = var.resolveCoreType();
				if (correspondingNode instanceof StructNode)
					v = new StructSymbolicVariable(name,
							table.getCurrentNameSpace() + SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS + type,
							scopeLevel);
				else if (correspondingNode instanceof ClassNode)
					v = new StructSymbolicVariable(name,
							table.getCurrentNameSpace() + SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS + type,
							scopeLevel);
				else if (correspondingNode instanceof EnumNode)
					v = new EnumSymbolicVariable(name,
							table.getCurrentNameSpace() + SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS + type,
							scopeLevel);
				else if (correspondingNode instanceof UnionNode)
					v = new UnionSymbolicVariable(name,
							table.getCurrentNameSpace() + SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS + type,
							scopeLevel);
			} else {
				// dont support this type
			}

			if (v != null) {
				table.add(v);

				IASTInitializer initialization = declarator.getInitializer();

				if (initialization != null) {
					String ini = v.getName() + initialization.getRawSignature();

					IASTNode ast = ASTUtils.convertToIAST(ini);
					ast = Utils.shortenAstNode(ast);

					new BinaryAssignmentParser().parse(ast, table);
				}
			}
		}
	}

	/**
	 * Get type of variable. If the type of variable is <b>auto</b>, we replace this
	 * type by corresponding type
	 *
	 * @param stm3
	 *            Represent the declaration
	 * @param declarator
	 *            Represent the current declarator
	 * @return
	 */
	private String getType(IASTSimpleDeclaration stm3, IASTDeclarator declarator) {
		String decl = stm3.getDeclSpecifier().getRawSignature();
		String type = "";

		if (VariableTypes.isAuto(decl)) {
			String initialization = declarator.getInitializer().getChildren()[0].getRawSignature();
			/*
			 * Predict the type of variable based on its initialization
			 */
			type = VariableTypes.getTypeOfAutoVariable(initialization);

		} else {
			type = decl;
			/*
			 * Check the variable is pointer or not
			 * 
			 * The first child is corresponding to the left side. For example, considering
			 * "int a = z*y", we parse the first child (its content: "int a")
			 */
			IASTNode firstChild = declarator.getChildren()[0];
			if (firstChild instanceof CPPASTPointer)
				type += "*";

			if (declarator.getChildren().length >= 2) {
				IASTNode secondChild = declarator.getChildren()[1];
				if (secondChild instanceof CPPASTArrayModifier)
					type += "[]";
			}
		}
		return type;
	}

	public int getScopeLevel() {
		return scopeLevel;
	}

	public void setScopeLevel(int scopeLevel) {
		this.scopeLevel = scopeLevel;
	}

	public IFunctionNode getFunction() {
		return function;
	}

	public void setFunction(IFunctionNode function) {
		this.function = function;
	}
}
