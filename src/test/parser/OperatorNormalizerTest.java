package test.parser;

import java.io.File;

import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDefinition;
import org.junit.Assert;
import org.junit.Test;

import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFGGeneration;
import com.fit.cfg.testpath.ITestpathInCFG;
import com.fit.cfg.testpath.PossibleTestpathGeneration;
import com.fit.config.Paths;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.normalizer.UnaryNormalizer;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

/**
 * Lớp này kiểm tra tên các node trong chương trình đúng chuẩn
 * chưa * @author DucAnh
 */
public class OperatorNormalizerTest {

	/**
	 * <pre>
	 * int basicTest17(int a, int b, int c) {
	 * 	if (a-- &gt; ++b + c--) {
	 * 		if (c == a)
	 * 			return 1;
	 * 		else
	 * 			return 0;
	 * 	} else
	 * 		return 0;
	 * }
	 * </pre>
	 *
	 * @throws Exception
	 */
	@Test
	public void test01() throws Exception {
		Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH = new File(
				Utils.normalizePath(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH)).getAbsolutePath();
		ProjectParser parser = new ProjectParser(new File(Utils.normalizePath(Paths.SYMBOLIC_EXECUTION_TEST)));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "basicTest17(int,int,int)").get(0);

		Paths.CURRENT_PROJECT.TYPE_OF_PROJECT = Utils.getTypeOfProject(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);
		/*
		 * Normalize function
		 */
		FunctionNormalizer fnNormalizer = function.getGeneralNormalizationFunction();
		String newFunctionInStr = fnNormalizer.getNormalizedSourcecode();
		ICPPASTFunctionDefinition newAST = Utils.getFunctionsinAST(newFunctionInStr.toCharArray()).get(0);
		((FunctionNode) function).setAST(newAST);

		/*
		 * Generate CFG of the normalized functionF
		 */
		CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function,
				ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);

		/*
		 * Choose a random test path to test
		 */
		PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG(), 1);
		tpGen.generateTestpaths();
		ITestpathInCFG specialTestpath = tpGen.getPossibleTestpaths().get(0);

		UnaryNormalizer norm = new UnaryNormalizer();
		norm.setOriginalTestpath(specialTestpath);
		norm.normalize();
		ITestpathInCFG normalizedTestpath = norm.getNormalizedTestpath();
		Assert.assertEquals(8, normalizedTestpath.getAllCfgNodes().size());
	}

	/**
	 * <pre>
	 * int basicTest23(int& a, int& b, int& c){
	 * if (a--> ++b + c--){
	 * if (c==a)
	 * return c+ ++a;
	 * else
	 * return c++;
	 * }
	 * else
	 * return --c;
	 * }
	 * </pre>
	 *
	 * @throws Exception
	 */
	@Test
	public void test02() throws Exception {
		Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH = new File(
				Utils.normalizePath(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH)).getAbsolutePath();
		ProjectParser parser = new ProjectParser(new File(Utils.normalizePath(Paths.SYMBOLIC_EXECUTION_TEST)));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "basicTest23(int&,int&,int&)").get(0);

		/*
		 * Normalize function
		 */
		FunctionNormalizer fnNormalizer = function.getGeneralNormalizationFunction();
		String newFunctionInStr = fnNormalizer.getNormalizedSourcecode();
		ICPPASTFunctionDefinition newAST = Utils.getFunctionsinAST(newFunctionInStr.toCharArray()).get(0);
		((FunctionNode) function).setAST(newAST);

		/*
		 * Generate CFG of the normalized functionF
		 */
		CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(function,
				ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);

		/*
		 * Choose a random test path to test
		 */
		PossibleTestpathGeneration tpGen = new PossibleTestpathGeneration(cfgGen.generateCFG(), 1);
		tpGen.generateTestpaths();
		ITestpathInCFG specialTestpath = tpGen.getPossibleTestpaths().get(0);

		UnaryNormalizer norm = new UnaryNormalizer();
		norm.setOriginalTestpath(specialTestpath);
		norm.normalize();
		ITestpathInCFG normalizedTestpath = norm.getNormalizedTestpath();
		Assert.assertEquals(9, normalizedTestpath.getAllCfgNodes().size());

	}
}
