package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.normalizer.FunctionNormalizer;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

public class CFGGenerationForDisplayingTest {
	final static Logger logger = Logger.getLogger(CFGGenerationForDisplayingTest.class);

	@Test
	public void test01() throws Exception {
		Logger.getRootLogger().setLevel(Level.INFO);

		ProjectParser parser = new ProjectParser(new File(Paths.CORE_UTILS));

		List<INode> functions = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition());

		Paths.CURRENT_PROJECT.TYPE_OF_PROJECT = ISettingv2.PROJECT_ECLIPSE;
		Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH = Paths.CORE_UTILS;

		int error = 0;
		List<String> errorList = new ArrayList<>();
		int pass = 0;
		for (INode fn : functions) {
			try {
				FunctionNormalizer fnNorm = ((IFunctionNode) fn).getGeneralNormalizationFunction();

				String normalizedSourcecode = fnNorm.getNormalizedSourcecode();

				IFunctionNode clone = (IFunctionNode) fn.clone();
				clone.setAST(Utils.getFunctionsinAST(normalizedSourcecode.toCharArray()).get(0));

				CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(clone,
						ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
				ICFG cfg = cfgGen.generateCFG();
				pass++;
			} catch (Exception e) {
				error++;
				errorList.add(fn.getAbsolutePath());
				e.printStackTrace();

				logger.info("---------------------------------\nError Parse " + fn.getAbsolutePath());
				logger.info(((IFunctionNode) fn).getAST().getRawSignature());

			} finally {
				logger.info("Error : " + error + "| Pass : " + pass);
			}
		}
		logger.info("-------------------------------------------------");

		logger.info(errorList.toString());
		logger.info("Error : " + error + "| Pass : " + pass);
		// Error = 11, pass = 1064
		Assert.assertEquals(1064, pass);
	}

	@Test
	public void test02() throws Exception {
		Logger.getRootLogger().setLevel(Level.INFO);

		ProjectParser parser = new ProjectParser(new File(Paths.TSDV_R1_4));

		List<INode> functions = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition());

		Paths.CURRENT_PROJECT.TYPE_OF_PROJECT = ISettingv2.PROJECT_DEV_CPP;
		Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH = Paths.TSDV_R1_4;

		int error = 0;
		List<String> errorList = new ArrayList<>();
		int pass = 0;
		for (INode fn : functions) {
			try {
				FunctionNormalizer fnNorm = ((IFunctionNode) fn).getGeneralNormalizationFunction();

				String normalizedSourcecode = fnNorm.getNormalizedSourcecode();

				IFunctionNode clone = (IFunctionNode) fn.clone();
				clone.setAST(Utils.getFunctionsinAST(normalizedSourcecode.toCharArray()).get(0));

				CFGGenerationforBranchvsStatementCoverage cfgGen = new CFGGenerationforBranchvsStatementCoverage(clone,
						ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES);
				ICFG cfg = cfgGen.generateCFG();
				pass++;
			} catch (Exception e) {
				error++;
				errorList.add(fn.getAbsolutePath());
				e.printStackTrace();
			}
		}
		logger.info("-------------------------------------------------");

		logger.info(errorList.toString());
		logger.info("Error : " + error + "| Pass : " + pass);
		Assert.assertEquals(true, error == 0);
	}
}
