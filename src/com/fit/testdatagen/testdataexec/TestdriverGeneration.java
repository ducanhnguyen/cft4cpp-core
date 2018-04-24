package com.fit.testdatagen.testdataexec;

import com.fit.config.IFunctionConfig;
import com.fit.config.Paths;
import com.fit.instrument.FunctionInstrumentationForSubCondition;
import com.fit.instrument.FunctionInstrumentationForStatementvsBranch_Marker;
import com.fit.instrument.IFunctionInstrumentationGeneration;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate test driver for a function
 *
 * @author Vu + D.Anh
 */
public abstract class TestdriverGeneration implements ITestdriverGeneration {

	protected IFunctionNode functionNode;

	protected String initialization = "";

	protected String functionCall = "";

	protected String testDriver = "";

	public TestdriverGeneration() {
	}

	public static void main(String[] args) throws Exception {
		ProjectParser parser = new ProjectParser(new File(Paths.TSDV_R1));
		FunctionNode testedFunction = (FunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "SimpleMethodTest()").get(0);

		List<String> values = new ArrayList<>();
		values.add("\tint x = 1;");

		TestdriverGeneration testdriverGen = new TestdriverGenerationforCpp();
		testdriverGen.setTestedFunction(testedFunction);
		testdriverGen.setRandomValues(values);
		testdriverGen.setFunctionCall("/*no function call*/");
		testdriverGen.generate();
		System.out.println("test driver=\n" + testdriverGen.getCompleteSourceFile());
	}

	protected String generateInstrumentedFunction() {
		String instrumentedFunction = "";
		// generate instrumented source code of the complete function
		IASTFunctionDefinition oldAST = getFunction().getAST();
		String completeFunction = getFunction().generateCompleteFunction();
		IASTFunctionDefinition newAST = Utils.getFunctionsinAST(completeFunction.toCharArray()).get(0);
		getFunction().setAST(newAST);

		IFunctionInstrumentationGeneration fnInstrumentation = null;
		int typeofCoverage = getFunction().getFunctionConfig().getTypeofCoverage();
		switch (typeofCoverage) {
		case IFunctionConfig.BRANCH_COVERAGE:
		case IFunctionConfig.STATEMENT_COVERAGE:
			fnInstrumentation = new FunctionInstrumentationForStatementvsBranch_Marker(getFunction());
			break;
		case IFunctionConfig.SUBCONDITION:
			fnInstrumentation = new FunctionInstrumentationForSubCondition(getFunction());
			break;
		}

		if (fnInstrumentation != null) {
			instrumentedFunction = fnInstrumentation.generateInstrumentedFunction();
			getFunction().setAST(oldAST);
		} else
			instrumentedFunction = "//error when instrumenting";
		return instrumentedFunction;
	}

	/**
	 * "static void test(...){...}" ------------> "void test(...){...}"
	 *
	 * @param function
	 */
	protected String removeStaticInFunctionDefinition(String function) {
		String oldDefinition = function.substring(0, function.indexOf("("));
		String newDefinition = oldDefinition.replaceAll("^static\\s*", "");
		function = function.replace(oldDefinition, newDefinition);
		return function;
	}

	@Override
	public IFunctionNode getFunction() {
		return functionNode;
	}

	public String getRandomValues() {
		return initialization;
	}

	public void setRandomValues(List<String> randomValues) {
		for (String item : randomValues)
			initialization += item;
	}

	@Override
	public String getFunctionCall() {
		return functionCall;
	}

	@Override
	public void setFunctionCall(String functionCall) {
		this.functionCall = functionCall;
	}

	@Override
	public String getCompleteSourceFile() {
		return testDriver;
	}

	public void setCompleteSourceFile(String completeSourceFile) {
		testDriver = completeSourceFile;
	}

	@Override
	public void setTestedFunction(IFunctionNode testedFunction) {
		functionNode = testedFunction;
	}

	@Override
	public void setInitialization(String randomValues) {
		initialization = randomValues;
	}
}
