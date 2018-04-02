package com.fit.tree.object;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTPointer;

import com.fit.cfg.CFGGenerationSubCondition;
import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.config.AbstractSetting;
import com.fit.config.FunctionConfig;
import com.fit.config.IFunctionConfig;
import com.fit.config.ISettingv2;
import com.fit.externalvariable.ExternalVariableDetecter;
import com.fit.externalvariable.ReducedExternalVariableDetecter;
import com.fit.gui.testreport.object.INameRule;
import com.fit.normalizer.ArgumentTypeNormalizer;
import com.fit.normalizer.ClassvsStructNormalizer;
import com.fit.normalizer.ConditionCovertNormalizer;
import com.fit.normalizer.EndStringNormalizer;
import com.fit.normalizer.EnumNormalizer;
import com.fit.normalizer.ExternNormalizer;
import com.fit.normalizer.FunctionNameNormalizer;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.normalizer.MacroNormalizer2;
import com.fit.normalizer.NullPtrNormalizer;
import com.fit.normalizer.SetterandGetterFunctionNormalizer;
import com.fit.normalizer.SwitchCaseNormalizer;
import com.fit.normalizer.TernaryConvertNormalizer;
import com.fit.normalizer.ThrowNormalizer;
import com.fit.testdatagen.module.DataTreeGeneration;
import com.fit.testdatagen.module.IDataTreeGeneration;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.dependency.Dependency;
import com.fit.tree.dependency.GetterDependency;
import com.fit.tree.dependency.SetterDependency;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.Utils;

import test.testdatageneration.AbstractJUnitTest;

public abstract class AbstractFunctionNode extends CustomASTNode<IASTFunctionDefinition> implements IFunctionNode {
	final static Logger logger = Logger.getLogger(AbstractFunctionNode.class);

	private FunctionConfig functionConfig = new FunctionConfig();

	private MacroNormalizer2 fnMacroNormalizer = null;

	private FunctionNormalizer fnMoreSimpleAST = null;

	private FunctionNormalizer fnNormalizeFunctionToFindStaticTestcase = null;

	private FunctionNormalizer fnNormalizeFunctionToExecute = null;

	private FunctionNormalizer fnNormalizeFunctionToDisplayCFG = null;

	/**
	 * Represent the real parent of function. Ex: if function in class is defined
	 * outside it, then its real parent is this class
	 */
	private INode realParent;

	public AbstractFunctionNode() {
		try {
			Icon ICON_FUNCTION = new ImageIcon(Node.class.getResource("/image/node/FunctionNode.png"));
			setIcon(ICON_FUNCTION);
		} catch (Exception e) {
		}
	}

	@Override
	public List<IVariableNode> getArguments() {
		List<IVariableNode> arguments = new ArrayList<>();

		for (IASTNode child : getAST().getDeclarator().getChildren())
			if (child instanceof IASTParameterDeclaration) {
				IASTParameterDeclaration astArgument = (IASTParameterDeclaration) child;

				VariableNode argumentNode = new InternalVariableNode();
				argumentNode.setAST(astArgument);
				argumentNode.setParent(this);
				argumentNode.setAbsolutePath(getAbsolutePath() + File.separator + argumentNode.getNewType());
				getChildren().add(argumentNode);

				if (!arguments.contains(argumentNode))
					arguments.add(argumentNode);
			}
		return arguments;
	}

	@Override
	public String getDeclaration() {
		String simpleName = getSimpleName();
		String declator = getAST().getDeclarator().getRawSignature();
		int index = declator.indexOf(simpleName);

		return declator.substring(index);
	}

	@Override
	public List<IVariableNode> getReducedExternalVariables() {
		return new ReducedExternalVariableDetecter(this).findExternalVariables();
	}

	@Override
	public List<IVariableNode> getExternalVariables() {
		return new ExternalVariableDetecter(this).findExternalVariables();
	}

	@Override
	public String getFullName() {
		String fullName = getSingleSimpleName() + "(";
		for (INode var : getArguments())
			fullName += ((VariableNode) var).getNewType() + ",";
		fullName += ")";
		fullName = fullName.replace(",)", ")");

		/*
		 * Add prefix of the current name
		 */
		String logicPath = getLogicPathFromTopLevel();
		if (logicPath.length() > 0)
			return logicPath + SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS + fullName;
		else
			return fullName;
	}

	@Override
	public FunctionConfig getFunctionConfig() {
		return functionConfig;
	}

	@Override
	public void setFunctionConfig(FunctionConfig functionConfig) {
		this.functionConfig = functionConfig;
	}

	/**
	 * Get the name of the function and the types of variables pass into it Ex:
	 * "test(int,int)"
	 */
	@Override
	public String getNewType() {
		String output = getAST().getDeclarator().getName().getRawSignature();
		output += "(";
		for (IVariableNode paramater : getArguments())
			output += paramater.getRawType() + ",";
		output += ")";
		output = output.replace(",)", ")").replaceAll("\\s*\\)", "\\)");
		return output;
	}

	@Override
	public IASTFileLocation getNodeLocation() {
		return getAST().getDeclarator().getFileLocation();
	}

	@Override
	public INode getRealParent() {
		return realParent;
	}

	@Override
	public void setRealParent(INode realParent) {
		this.realParent = realParent;
	}

	@Override
	public String getSimpleName() {
		CPPASTFunctionDeclarator declarator = (CPPASTFunctionDeclarator) getAST().getDeclarator();
		IASTNode[] children = declarator.getChildren();
		IASTNode selectedChild = children[0];
		if (selectedChild instanceof CPPASTPointer)
			selectedChild = children[1];
		return selectedChild.getRawSignature();
	}

	@Override
	public File getSourceFile() {
		INode sourceCodeFileNode = Utils.getSourcecodeFile(this);
		if (sourceCodeFileNode != null)
			return new File(sourceCodeFileNode.getAbsolutePath());
		else
			return null;
	}

	@Override
	public INode isGetter() {
		for (Dependency d : getDependencies())
			if (d instanceof GetterDependency)
				if (d.getStartArrow() instanceof VariableNode)
					return d.getStartArrow();
				else if (d.getEndArrow() instanceof VariableNode)
					return d.getEndArrow();
		return null;
	}

	@Override
	public boolean isNoType() {
		return getAST().getDeclSpecifier().getRawSignature().isEmpty();
	}

	@Override
	public INode isSetter() {
		for (Dependency d : getDependencies())
			if (d instanceof SetterDependency)
				if (d.getStartArrow() instanceof VariableNode)
					return d.getStartArrow();
				else if (d.getEndArrow() instanceof VariableNode)
					return d.getEndArrow();
		return null;
	}

	@Override
	public void setAST(IASTFunctionDefinition aST) {
		super.setAST(aST);
	}

	@Override
	public void setParent(INode parent) {
		super.setParent(parent);
		realParent = parent;
	}

	@Override
	public String toString() {
		return getAST().getDeclSpecifier().toString() + " " + getAST().getDeclarator().getRawSignature();
	}

	@Override
	public boolean isTemplate() {
		return AST instanceof ICPPASTTemplateDeclaration;
	}

	@Override
	public String getSingleSimpleName() {
		String singleSimpleName = getSimpleName();
		if (!singleSimpleName.contains(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS))
			return singleSimpleName;
		else
			return singleSimpleName
					.substring(singleSimpleName.lastIndexOf(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS) + 2);
	}

	@Override
	public IVariableNode getCorrespondingVariable() {
		IVariableNode output = null;
		INode structureParent = Utils.getStructureParent(this);

		if (structureParent != null && structureParent instanceof StructureNode) {
			StructureNode p = (StructureNode) structureParent;

			for (IVariableNode child : p.getAttributes())
				if (child.getSetterNode() != null && child.getSetterNode().equals(this)
						|| child.getGetterNode() != null && child.getGetterNode().equals(this)) {
					output = child;
					break;
				}
		}
		return output;
	}

	@Override
	public List<IVariableNode> getExpectedNodeTypes() {
		List<IVariableNode> expectedNodeTypes = getReducedExternalVariables();
		String returnType = getReturnType();

		if (isNoType()) {
			// nothing to do
		} else {
			// add a variable representing return value
			VariableNode returnVar = new ReturnVariableNode();
			returnVar.setName(INameRule.RETURN_VARIABLE_NAME_PREFIX);
			returnVar.setRawType(returnType);
			returnVar.setCoreType(returnType.replace(SpecialCharacter.POINTER, ""));
			returnVar.setReducedRawType(returnType);
			returnVar.setParent(this);

			expectedNodeTypes.add(returnVar);
		}
		/*
		 * Add throw variable
		 */
		VariableNode returnVar = new ThrowVariableNode();
		returnVar.setName(INameRule.THROW_VARIABLE_NAME_PREFIX);
		returnVar.setRawType(VariableTypes.THROW);
		returnVar.setCoreType(VariableTypes.THROW);
		returnVar.setReducedRawType(VariableTypes.THROW);
		returnVar.setParent(this);

		expectedNodeTypes.add(returnVar);

		/*
		 * Add all variables as passing variable
		 */
		for (IVariableNode argument : getPassingVariables())
			if (!expectedNodeTypes.contains(argument))
			expectedNodeTypes.add(argument);

		return expectedNodeTypes;
	}

	@Override
	public List<IVariableNode> getPassingVariables() {
		List<IVariableNode> passingVariables = new ArrayList<>();

		for (IVariableNode n : getArguments())
			passingVariables.add(n);

		for (IVariableNode n : getReducedExternalVariables())
			passingVariables.add(n);

		return passingVariables;
	}

	@Override
	public String getReturnType() {
		IASTFunctionDefinition funDef = getAST();

		IASTNode firstChildren = funDef.getChildren()[0];
		String returnType = firstChildren.getRawSignature();

		/*
		 * Name of function may contain * character. Ex: SinhVien* StrDel2(char s[],int
		 * k,int h){...} ==> * StrDel2(char s[],int k,int h)
		 */
		boolean isReturnReference = false;
		CPPASTFunctionDeclarator functionDeclarator = (CPPASTFunctionDeclarator) funDef.getChildren()[1];
		IASTNode firstChild = functionDeclarator.getChildren()[0];
		if (firstChild instanceof CPPASTPointer)
			isReturnReference = true;
		/*
		 *
		 */
		returnType += isReturnReference == true ? "*" : "";
		return returnType;
	}

	@Override
	public IASTFunctionDefinition getNormalizedASTtoDisplayinCFG() throws Exception {
		IASTFunctionDefinition normalizedAST = getAST();
		SwitchCaseNormalizer switchCaseNormalizer = new SwitchCaseNormalizer(this);
		switchCaseNormalizer.setFunctionNode(this);
		switchCaseNormalizer.normalize();
		String sc = switchCaseNormalizer.getNormalizedSourcecode();

		normalizedAST = Utils.getFunctionsinAST(sc.toCharArray()).get(0);

		return normalizedAST;
	}

	@Override
	public String getInstrumentedofNormalizedSource() {
		return null;
	}

	@Override
	public String getLogicPathFromTopLevel() {
		INode namespace = Utils.getTopLevelClassvsStructvsNamesapceNodeParent(this);

		if (namespace == null)
			return "";
		else {
			String path = "";
			INode parent = this;
			while (!parent.equals(namespace)) {
				if (parent instanceof IFunctionNode)
					parent = ((IFunctionNode) parent).getRealParent();
				else
					parent = parent.getParent();

				if (parent instanceof NamespaceNode || parent instanceof StructureNode)
					path = parent.getNewType() + "::" + path;
				else
					break;
			}
			if (path.length() >= 2)
				path = path.substring(0, path.length() - 2);
			return path;
		}
	}

	@Override
	public Boolean isChildrenOfUnnameNamespace() {
		INode namespace = Utils.getTopLevelClassvsStructvsNamesapceNodeParent(this);

		if (namespace == null)
			return false;
		else {
			if (namespace.getNewType().equals(""))
				return true;
		}

		return false;
	}

	@Override
	public FunctionNormalizer getNormalizeFunctionToExecute() throws Exception {
		// logger.debug("Normalize function to execute");
		if (fnNormalizeFunctionToExecute == null) {
			FunctionNormalizer fnNormalizer = new FunctionNormalizer();
			fnNormalizer.setFunctionNode(this);

			if (AbstractSetting.getValue(ISettingv2.IN_TEST_MODE).equals("false")
					|| (AbstractSetting.getValue(ISettingv2.IN_TEST_MODE).equals("true")
							&& AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION)) {
				fnNormalizer.addNormalizer(new MacroNormalizer2());
			}

			fnNormalizer.addNormalizer(new FunctionNameNormalizer());
			fnNormalizer.addNormalizer(new ArgumentTypeNormalizer());
			fnNormalizer.addNormalizer(new TernaryConvertNormalizer());
			fnNormalizer.addNormalizer(new ConditionCovertNormalizer());
			fnNormalizer.addNormalizer(new ThrowNormalizer());
			fnNormalizer.addNormalizer(new SwitchCaseNormalizer());
			fnNormalizer.normalize();
			fnNormalizeFunctionToExecute = fnNormalizer;
		}
		return fnNormalizeFunctionToExecute;
	}

	@Override
	@Deprecated
	public FunctionNormalizer normalizeFunctionToFindStaticTestcase() throws Exception {
		logger.debug("Normalize function to find static test case");
		if (fnNormalizeFunctionToFindStaticTestcase == null) {
			FunctionNormalizer fnNormalizer = new FunctionNormalizer();
			fnNormalizer.setFunctionNode(this);

			if (AbstractSetting.getValue(ISettingv2.IN_TEST_MODE).equals("false")
					|| (AbstractSetting.getValue(ISettingv2.IN_TEST_MODE).equals("true")
							&& AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION)) {
				fnNormalizer.addNormalizer(new MacroNormalizer2());
			}

			fnNormalizer.addNormalizer(new ArgumentTypeNormalizer());
			fnNormalizer.addNormalizer(new TernaryConvertNormalizer());
			fnNormalizer.addNormalizer(new ConditionCovertNormalizer());
			fnNormalizer.addNormalizer(new ClassvsStructNormalizer());
			fnNormalizer.addNormalizer(new EnumNormalizer());
			fnNormalizer.addNormalizer(new ExternNormalizer());
			fnNormalizer.addNormalizer(new NullPtrNormalizer());
			fnNormalizer.addNormalizer(new ThrowNormalizer());
			fnNormalizer.addNormalizer(new SwitchCaseNormalizer());
			fnNormalizer.addNormalizer(new EndStringNormalizer());
			// fnNormalizer.addNormalizer(new ConstantNormalizer());

			fnNormalizer.normalize();
			fnNormalizeFunctionToFindStaticTestcase = fnNormalizer;
		}
		return fnNormalizeFunctionToFindStaticTestcase;
	}

	@Override
	public FunctionNormalizer normalizeFunctionToDisplayCFG() throws Exception {
		logger.debug("Normalization function to display in CFG");
		if (fnNormalizeFunctionToDisplayCFG == null) {
			FunctionNormalizer fnNormalizer = new FunctionNormalizer();
			fnNormalizer.setFunctionNode(this);
			fnNormalizer.addNormalizer(new TernaryConvertNormalizer());
			fnNormalizer.addNormalizer(new ConditionCovertNormalizer());
			fnNormalizer.addNormalizer(new SwitchCaseNormalizer());
			fnNormalizer.normalize();
			fnNormalizeFunctionToDisplayCFG = fnNormalizer;
		}
		return fnNormalizeFunctionToDisplayCFG;
	}

	@Override
	public FunctionNormalizer normalizedAST() throws Exception {
		if (fnMoreSimpleAST == null) {
			FunctionNormalizer fnNormalizer = new FunctionNormalizer();
			fnNormalizer.setFunctionNode(this);

			// only for test
			if (AbstractSetting.getValue(ISettingv2.IN_TEST_MODE).equals("false")
					|| (AbstractSetting.getValue(ISettingv2.IN_TEST_MODE).equals("true")
							&& AbstractJUnitTest.ENABLE_MACRO_NORMALIZATION)) {
				fnNormalizer.addNormalizer(new MacroNormalizer2());
			}
			// end test
			fnNormalizer.addNormalizer(new TernaryConvertNormalizer());
			fnNormalizer.addNormalizer(new ConditionCovertNormalizer());
			fnNormalizer.addNormalizer(new SwitchCaseNormalizer());
			
			fnNormalizer.normalize();
			fnMoreSimpleAST = fnNormalizer;
			return fnMoreSimpleAST;

		} else
			return fnMoreSimpleAST;
	}

	@Override
	public SetterandGetterFunctionNormalizer performSettervsGetterTransformer() {
		SetterandGetterFunctionNormalizer performer = new SetterandGetterFunctionNormalizer();
		performer.setFunctionNode(this);
		performer.normalize();
		return performer;
	}

	@Override
	public IDataTreeGeneration generateDataTree(Map<String, Object> solution) {
		IDataTreeGeneration dataTreeGen = new DataTreeGeneration();
		dataTreeGen.setFunctionNode(this);
		dataTreeGen.setStaticSolution(solution);
		return dataTreeGen;
	}

	@Override
	public ICFG generateCFGToFindStaticSolution() {
		logger.debug("Generate CFG to find static solution");
		ICFG cfg = null;
		try {
			if (getFunctionConfig().getTypeofCoverage() == IFunctionConfig.BRANCH_COVERAGE
					|| getFunctionConfig().getTypeofCoverage() == IFunctionConfig.STATEMENT_COVERAGE) {
				cfg = new CFGGenerationforBranchvsStatementCoverage(this,
						ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES).generateCFG();
				cfg.setFunctionNode(this);

			} else if (getFunctionConfig().getTypeofCoverage() == IFunctionConfig.SUBCONDITION) {
				cfg = new CFGGenerationSubCondition(this, ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES).generateCFG();
				cfg.setFunctionNode(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cfg;
	}

	@Override
	public ICFG generateCFGofExecutionSourcecode() {
		logger.debug("Generate CFG of execution source code");
		ICFG cfg = null;
		try {
			if (getFunctionConfig().getTypeofCoverage() == IFunctionConfig.BRANCH_COVERAGE
					|| getFunctionConfig().getTypeofCoverage() == IFunctionConfig.STATEMENT_COVERAGE) {
				cfg = new CFGGenerationforBranchvsStatementCoverage(
						this.getFnNormalizedASTtoInstrument().getFunctionNode(),
						ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES).generateCFG();
				cfg.setFunctionNode(this);

			} else if (getFunctionConfig().getTypeofCoverage() == IFunctionConfig.SUBCONDITION) {
				cfg = new CFGGenerationSubCondition(this.getFnNormalizedASTtoInstrument().getFunctionNode(),
						ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES).generateCFG();
				cfg.setFunctionNode(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cfg;
	}

	@Override
	public MacroNormalizer2 getFnMacroNormalizer() {
		return fnMacroNormalizer;
	}

	@Override
	public void setFnMacroNormalizer(MacroNormalizer2 fnMacroNormalizer) {
		this.fnMacroNormalizer = fnMacroNormalizer;
	}

	@Override
	public String generateCompleteFunction() {
		String completeFunction = "";

		// STEP 1: normalize the arguments of the current function
		ArgumentTypeNormalizer varialeTypeNormalizer = new ArgumentTypeNormalizer();
		varialeTypeNormalizer.setFunctionNode(this);
		varialeTypeNormalizer.normalize();
		completeFunction = varialeTypeNormalizer.getNormalizedSourcecode();

		// STEP 2: normalize the name of function
		String prefixPathofFunction = getLogicPathFromTopLevel();
		if (prefixPathofFunction.length() > 0)
			if (prefixPathofFunction.startsWith(SpecialCharacter.STRUCTURE_OR_NAMESPACE_ACCESS))
				completeFunction = completeFunction.replace(getSimpleName(),
						getLogicPathFromTopLevel() + getSingleSimpleName());
			else
				completeFunction = completeFunction.replace(getSimpleName(),
						getLogicPathFromTopLevel() + SpecialCharacter.FILE_SCOPE_ACCESS + getSingleSimpleName());

		return completeFunction;
	}

	@Override
	public INode clone() {
		IFunctionNode clone = new FunctionNode();
		clone.setAbsolutePath(getAbsolutePath());
		clone.setChildren(getChildren());
		clone.setDependencies(getDependencies());
		clone.setId(getId());
		clone.setName(getNewType());
		clone.setParent(getParent());
		clone.setFunctionConfig(getFunctionConfig());
		clone.setRealParent(getRealParent());
		clone.setAST(getAST());

		clone.setFnMacroNormalizer(getFnMacroNormalizer());
		clone.setFnNormalizedASTtoInstrument(getFnNormalizedASTtoInstrument());
		clone.setFnNormalizeFunctionToDisplayCFG(getFnNormalizeFunctionToDisplayCFG());
		clone.setFnNormalizeFunctionToExecute(getFnNormalizeFunctionToExecute());
		clone.setFnNormalizeFunctionToFindStaticTestcase(getFnNormalizeFunctionToFindStaticTestcase());
		return clone;
	}

	@Override
	public FunctionNormalizer getFnNormalizedASTtoInstrument() {
		return fnMoreSimpleAST;
	}

	@Override
	public void setFnNormalizedASTtoInstrument(FunctionNormalizer fnNormalizedASTtoInstrument) {
		this.fnMoreSimpleAST = fnNormalizedASTtoInstrument;
	}

	@Override
	public FunctionNormalizer getFnNormalizeFunctionToFindStaticTestcase() {
		return fnNormalizeFunctionToFindStaticTestcase;
	}

	@Override
	public void setFnNormalizeFunctionToFindStaticTestcase(FunctionNormalizer fnNormalizeFunctionToFindStaticTestcase) {
		this.fnNormalizeFunctionToFindStaticTestcase = fnNormalizeFunctionToFindStaticTestcase;
	}

	@Override
	public FunctionNormalizer getFnNormalizeFunctionToExecute() {
		return fnNormalizeFunctionToExecute;
	}

	@Override
	public void setFnNormalizeFunctionToExecute(FunctionNormalizer fnNormalizeFunctionToExecute) {
		this.fnNormalizeFunctionToExecute = fnNormalizeFunctionToExecute;
	}

	@Override
	public FunctionNormalizer getFnNormalizeFunctionToDisplayCFG() {
		return fnNormalizeFunctionToDisplayCFG;
	}

	@Override
	public void setFnNormalizeFunctionToDisplayCFG(FunctionNormalizer fnNormalizeFunctionToDisplayCFG) {
		this.fnNormalizeFunctionToDisplayCFG = fnNormalizeFunctionToDisplayCFG;
	}

	@Override
	public boolean isStaticFunction() {
		if (getRealParent() == null) {
			return getReturnType().contains("static");
		} else {
			for (INode node : getRealParent().getChildren()) {
				if (node instanceof DefinitionFunctionNode) {
					if (((DefinitionFunctionNode) node).getReturnType().contains("static")
							&& ((DefinitionFunctionNode) node).getSimpleName().equals(getSingleSimpleName())) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
