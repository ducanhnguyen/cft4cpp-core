package com.fit.tree.object;

import com.fit.cfg.ICFG;
import com.fit.config.FunctionConfig;
import com.fit.config.IFunctionConfig;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.normalizer.MacroNormalizer2;
import com.fit.normalizer.SetterandGetterFunctionNormalizer;
import com.fit.testdatagen.module.IDataTreeGeneration;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;

import java.util.List;
import java.util.Map;

/**
 * Represent a function in the structure tree
 *
 * @author DucAnh
 */
public interface IFunctionNode extends ISourceNavigable, INode {
	/**
	 * Get arguments. <br/>
	 * Ex: "void test(int a,int b)"----------------->arguments = {"int a", "int b"}
	 *
	 * @return
	 */
	List<IVariableNode> getArguments();

	/**
	 * Get corresponding abstract syntax tree (AST)
	 *
	 * @return
	 */
	IASTFunctionDefinition getAST();

	/**
	 * Set AST of the current function
	 *
	 * @param ast
	 */
	void setAST(IASTFunctionDefinition ast);

	/**
	 * Get declaration (= the name of function + parameters). <br/>
	 * Ex1: "Node* StackLinkedList::pop1()"----------->"StackLinkedList::pop1()"
	 * <br/>
	 * Ex2: "int test(a)"---------------> "test(a)"
	 *
	 * @return
	 */
	String getDeclaration();

	/**
	 * Get reduced external variables
	 *
	 * @return
	 */
	List<IVariableNode> getReducedExternalVariables();

	/**
	 * Get external variables
	 *
	 * @return
	 */
	List<IVariableNode> getExternalVariables();

	/**
	 * Get name of function (in context of class/namespace using "::") and name of
	 * its variables. <br/>
	 * Ex: int* symbolic_execution5(int a, int b){...} ----------->
	 * ClassA::ClassB::symbolic_execution5(a,b);
	 *
	 * @return
	 */
	String getFullName();

	/**
	 * Get the function configuration of the current function
	 *
	 * @return
	 */
	IFunctionConfig getFunctionConfig();

	/**
	 * Set the function configuration
	 *
	 * @param functionConfig
	 */
	void setFunctionConfig(FunctionConfig functionConfig);

	/**
	 * Get the passing variables (= arguments + external variables)
	 *
	 * @return
	 */
	List<IVariableNode> getPassingVariables();

	/**
	 * Get the simple name of function. VD: function "int* symbolic_execution5(int
	 * a, int b){...}" ------------------> "symbolic_execution5"
	 *
	 * @return
	 */
	String getSimpleName();

	/**
	 * @return true if the current function return void
	 */
	boolean isNoType();

	/**
	 * Get the AST of the given normalized source code to display in CFG
	 *
	 * @return
	 */
	IASTFunctionDefinition getNormalizedASTtoDisplayinCFG() throws Exception;

	/**
	 * Get the corresponding variable node of the current function. <br/>
	 * If the call of the function is corresponding to a variable, then return this
	 * variable. <br/>
	 * Ex: the function "getAge()" in sv.getAge() ---------------------> return
	 * attribute "age"
	 * <p>
	 * 
	 * <pre>
	 * class SinhVien{
	 * 		public:
	 * 			int getAge();
	 * }
	 * </pre>
	 *
	 * @return
	 */
	IVariableNode getCorrespondingVariable();

	/**
	 * Get expected node types
	 *
	 * @return
	 */
	List<IVariableNode> getExpectedNodeTypes();

	/**
	 * Get the instrumented source code after performing normalization
	 *
	 * @return
	 */
	String getInstrumentedofNormalizedSource();

	/**
	 * Get path from namespace to class/struct, not including function
	 *
	 * @return
	 */
	String getLogicPathFromTopLevel();

	/**
	 * is this function in unname(annonymous) namespace ?
	 *
	 * @return
	 */
	Boolean isChildrenOfUnnameNamespace();

	/**
	 * Get the return type of the current function
	 *
	 * @return
	 */
	String getReturnType();

	/**
	 * Get the real parent of the current function
	 *
	 * @return
	 */
	INode getRealParent();

	/**
	 * Set the real parent of the current function
	 *
	 * @param realParent
	 */
	void setRealParent(INode realParent);

	/**
	 * Get the corresponding variable node <br/>
	 * Example, <br/>
	 * <p>
	 * 
	 * <pre>
	 * class Student{
	 * 		private:
	 * 			int age;
	 * 		public:
	 * 			int getAge(){return age;}
	 * }
	 * </pre>
	 * <p>
	 * "getAge()"---------> "int age;"
	 *
	 * @return the variable corresponding to the current function
	 */
	INode isGetter();

	/**
	 * Get the corresponding variable node <br/>
	 * Example, <br/>
	 * <p>
	 * 
	 * <pre>
	 * class Student{
	 * 		private:
	 * 			int age;
	 * 		public:
	 * 			void setAge(int age_){age = age_;}
	 * }
	 * </pre>
	 * <p>
	 * "setAge()"---------> "int age;"
	 *
	 * @return the variable corresponding to the current function
	 */
	INode isSetter();

	/**
	 * @return true if the current function is template
	 */
	boolean isTemplate();

	/**
	 * Normalize a function before executing it. Therefore, the process for
	 * generating test driver is easier.
	 *
	 * @throws Exception
	 */
	FunctionNormalizer getNormalizeFunctionToExecute() throws Exception;

	/**
	 * Normalize a function before find static solution. Because the complexity of
	 * the function, it is hard to parse the function without any modifications on
	 * source code.
	 * <p>
	 * Our objective is to rewrite function so that the process for discovering
	 * static solution effectively!
	 *
	 * @throws Exception
	 */
	@Deprecated
	FunctionNormalizer normalizeFunctionToFindStaticTestcase() throws Exception;

	/**
	 * Normalize function to display control flow graph
	 *
	 * @return
	 * @throws Exception
	 */
	FunctionNormalizer normalizeFunctionToDisplayCFG() throws Exception;

	/**
	 * Normalize function before instrumenting source code
	 *
	 * @return
	 * @throws Exception
	 */
	FunctionNormalizer normalizedAST() throws Exception;

	/**
	 * Convert statement corresponding to the setters/getters to its corresponding
	 * variable.
	 * <p>
	 * Ex: "sv.getAge()>0"------> "sv.age>0"
	 *
	 * @return
	 */
	SetterandGetterFunctionNormalizer performSettervsGetterTransformer();

	/**
	 * Generate tree of test data
	 *
	 * @param solution
	 * @return
	 */
	IDataTreeGeneration generateDataTree(Map<String, Object> solution);

	/**
	 * Generate CFG
	 *
	 * @return
	 */
	ICFG generateCFGToFindStaticSolution();

	/**
	 * Get macro normalizer
	 *
	 * @return
	 */
	MacroNormalizer2 getFnMacroNormalizer();

	void setFnMacroNormalizer(MacroNormalizer2 fnMacroNormalizer);

	/**
	 * Generate complete function satisfies: <br/>
	 * 1. The name of function is full-path <br/>
	 * 2. The type of argument is full-path <br/>
	 * Ex1: "int test(Student a)"----------------> "int
	 * ClassA::test(Namespace1::Namespace2:ClassB::Student a) <br/>
	 * <p>
	 * Ex2: "int test2(Apple a)"----------------> "int ClassA::test2(::Apple a)
	 * <br/>
	 *
	 * @return
	 */
	String generateCompleteFunction();

	/**
	 * Return name of function not including namespace, class, struct, etc.. <br/>
	 * Ex: "int nsTest0::Student::isAvailable(int
	 * id)"------------------"isAvailable"
	 *
	 * @return
	 */
	String getSingleSimpleName();

	@Override
	INode clone();

	FunctionNormalizer getFnNormalizedASTtoInstrument();

	void setFnNormalizedASTtoInstrument(FunctionNormalizer fnNormalizedASTtoInstrument);

	FunctionNormalizer getFnNormalizeFunctionToFindStaticTestcase();

	void setFnNormalizeFunctionToFindStaticTestcase(FunctionNormalizer fnNormalizeFunctionToFindStaticTestcase);

	FunctionNormalizer getFnNormalizeFunctionToExecute();

	void setFnNormalizeFunctionToExecute(FunctionNormalizer fnNormalizeFunctionToExecute);

	FunctionNormalizer getFnNormalizeFunctionToDisplayCFG();

	void setFnNormalizeFunctionToDisplayCFG(FunctionNormalizer fnNormalizeFunctionToDisplayCFG);

	boolean isStaticFunction();

	ICFG generateCFGofExecutionSourcecode();
}
