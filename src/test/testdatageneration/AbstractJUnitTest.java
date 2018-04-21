package test.testdatageneration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.LineUnavailableException;

import org.apache.log4j.Logger;

import com.console.Console;
import com.console.ConsoleOutput;
import com.fit.config.AbstractSetting;
import com.fit.config.IFunctionConfig;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.config.Settingv2;
import com.fit.testdatagen.ITestdataGeneration;
import com.fit.testdatagen.htmlreport.ITestReport;
import com.fit.utils.Utils;

public abstract class AbstractJUnitTest {
	final static Logger logger = Logger.getLogger(AbstractJUnitTest.class);

	public static Logger getLogger() {
		return logger;
	}

	public boolean generateTestdata(String originalProjectPath, String methodName, EO expectedOutput, int coverageType,
			ITestReport testReport) throws LineUnavailableException {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// Create file for storing function
		String folderTestPath = "";
		try {
			folderTestPath = new File(AbstractSetting.settingPath).getParentFile().getCanonicalPath() + File.separator;
		} catch (IOException e) {
			e.printStackTrace();
		}

		String inputPath = folderTestPath + "test.txt";
		methodName = (methodName.startsWith(File.separator) ? "" : File.separator) + methodName;
		Utils.writeContentToFile(methodName, inputPath);

		// Create setting.properties
		String configurePath = folderTestPath + "setting.properties";
		Settingv2.create();
		AbstractSetting.setValue(ISettingv2.DEFAULT_CHARACTER_LOWER_BOUND, 32);
		AbstractSetting.setValue(ISettingv2.DEFAULT_CHARACTER_UPPER_BOUND, 126);
		AbstractSetting.setValue(ISettingv2.DEFAULT_NUMBER_LOWER_BOUND, 0);
		AbstractSetting.setValue(ISettingv2.DEFAULT_NUMBER_UPPER_BOUND, 30);
		AbstractSetting.setValue(ISettingv2.DEFAULT_TEST_ARRAY_SIZE, 12);
		AbstractSetting.setValue(ISettingv2.MAX_ITERATION_FOR_EACH_LOOP, 11);
		AbstractSetting.setValue(ISettingv2.IN_TEST_MODE, "true");
		AbstractSetting.setValue(ISettingv2.TESTDATA_STRATEGY,
				ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.FAST_MARS);
		AbstractSetting.setValue(ISettingv2.SMT_LIB_FILE_PATH, folderTestPath + "constraints.smt2");

		if (coverageType == IFunctionConfig.BRANCH_COVERAGE)
			AbstractSetting.setValue(ISettingv2.COVERAGE, ISettingv2.SUPPORT_COVERAGE_CRITERIA[0]);
		else if (coverageType == IFunctionConfig.STATEMENT_COVERAGE)
			AbstractSetting.setValue(ISettingv2.COVERAGE, ISettingv2.SUPPORT_COVERAGE_CRITERIA[1]);
		else if (coverageType == IFunctionConfig.SUBCONDITION)
			AbstractSetting.setValue(ISettingv2.COVERAGE, ISettingv2.SUPPORT_COVERAGE_CRITERIA[2]);

		AbstractSetting.setValue(ISettingv2.SELECTED_SOLVING_STRATEGY, ISettingv2.SUPPORT_SOLVING_STRATEGIES[0]);

		//
		Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH = originalProjectPath;
		AbstractSetting.setValue(ISettingv2.INPUT_PROJECT_PATH, Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);
		logger.debug("original project: " + Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);

		Paths.CURRENT_PROJECT.TYPE_OF_PROJECT = Utils.getTypeOfProject(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);

		expectedOutput = expectedOutput == null ? new EO(EO.UNSPECIFIED_NUM_OF_TEST_PATH, EO.MAXIMUM_COVERAGE)
				: expectedOutput;

		// Generate test data
		int MAX_GENERATION = 1;
		Console[] console = new Console[MAX_GENERATION];

		boolean reachCoverageObjective = false;
		for (int i = 0; i < MAX_GENERATION; i++) {
			String[] args = new String[] { Console.LOAD_PROJECT, Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH,
					Console.TESTED_FUNCTIONS, inputPath, Console.CONFIG, configurePath, Console.LOG4J_LEVEL, "debug" };
			console[i] = new Console(args);
			// Display output
			List<ConsoleOutput> outputList = console[i].getOutput();

			for (ConsoleOutput outputItem : outputList) {
				// logger.info("Original function: " +
				// outputItem.getFunctionNode().getAST().getRawSignature());
				long totalRunningTime = outputItem.getRunningTime() - outputItem.getMacroNormalizationTime();
				logger.info("");
				logger.info("|--SUMMARY - not include macro normalization-----|");
				logger.info("| Total time = " + totalRunningTime + "ms (~" + totalRunningTime / 1000 + "s)");
				logger.info("| Execution time = " + outputItem.getExecutionTime() + "ms ("
						+ outputItem.getExecutionTime() * 1.0 / totalRunningTime * 100 + "%)");
				logger.info("| Solver running time = " + outputItem.getSolverRunningTime() + "ms ("
						+ outputItem.getSolverRunningTime() * 1.0 / totalRunningTime * 100 + "%)");
				logger.info("| Make file running time = " + outputItem.getMakeCommandRunningTime() + "ms ("
						+ outputItem.getMakeCommandRunningTime() * 1.0 / totalRunningTime * 100 + "%) ("
						+ outputItem.getMakeCommandRunningNumber() + "/" + outputItem.getNumOfExecutions() + " makes)");
				logger.info("| Normalization time = "
						+ (outputItem.getNormalizationTime() - outputItem.getMacroNormalizationTime()) + "ms ("
						+ (outputItem.getNormalizationTime() - outputItem.getMacroNormalizationTime()) * 1.0
								/ totalRunningTime * 100
						+ "%)");
				logger.info("| Symbolic execution time = " + outputItem.getSymbolicExecutionTime() + "ms ("
						+ outputItem.getSymbolicExecutionTime() * 1.0 / totalRunningTime * 100 + "%)");
				logger.info("| Num of effective solver calls = "
						+ (outputItem.getNumOfSolverCalls() - outputItem.getNumOfSolverCallsbutCannotSolve()) + "/"
						+ outputItem.getNumOfSolverCalls() + " times (Num of error Solver calls = "
						+ outputItem.getNumOfSolverCallsbutCannotSolve() + "/" + outputItem.getNumOfSolverCalls()
						+ ")");
				logger.info("| Num of no change to coverage iteration = " + outputItem.getNumOfNoChangeToCoverage());
				logger.info("| Num of symbolic executions = " + outputItem.getNumOfSymbolicExecutions() + " times");
				logger.info("| Num of symbolic statements = " + outputItem.getNumOfSymbolicStatements() + " times");
				logger.info("| Num of executions = " + outputItem.getNumOfExecutions() + " times");
				logger.info("| Reached coverage = " + outputItem.getCoverge() + "% (Expected coverage = "
						+ expectedOutput.getExpectedCoverage() + "%)");
				logger.info("| Bug = " + outputItem.getBugs());
				logger.info("|--END SUMMARY - not include macro normalization-----|");
				logger.info("| Macro normalization time = " + outputItem.getMacroNormalizationTime() / 1000
						+ " seconds");
				logger.info("");
				logger.info("");

				// Compare the actual output and expected output
				reachCoverageObjective = outputItem.getCoverge() >= expectedOutput.getExpectedCoverage() ? true : false;

				console[i].exportToHtml(new File(JUNIT_REPORT), methodName);
			}
		}

		// mergeAllReportIntoOneSummary(console, MAX_GENERATION, inputPath,
		// configurePath, "[SUMMARY] " + methodName);
		AbstractSetting.setValue(ISettingv2.IN_TEST_MODE, "true");
		return reachCoverageObjective;
	}

	/**
	 * Only for testing
	 * 
	 * @param console
	 * @param MAX_GENERATION
	 * @param inputPath
	 * @param configurePath
	 * @param methodName
	 */
	private void mergeAllReportIntoOneSummary(Console[] console, int MAX_GENERATION, String inputPath,
			String configurePath, String methodName) {
		// Merge all into one summary
		long executionTime = 0;
		int makeCommandRunningNumber = 0;
		long makeCommandRunningTime = 0;
		long normalizationTime = 0;
		int numOfExecutions = 0;
		int numOfSolverCalls = 0;
		int numOfSymbolicExecutions = 0;
		int numOfSymbolicStatements = 0;
		long solverRunningTime = 0;
		long symbolicExecutionTime = 0;
		long runningTime = 0;
		Set<Bug> bugs = new HashSet<>();
		for (int i = 0; i < MAX_GENERATION; i++) {
			List<ConsoleOutput> outputList = console[i].getOutput();

			for (ConsoleOutput outputItem : outputList) {
				executionTime += outputItem.getExecutionTime();
				makeCommandRunningNumber += outputItem.getMakeCommandRunningNumber();
				makeCommandRunningTime += outputItem.getMakeCommandRunningTime();
				normalizationTime += outputItem.getNormalizationTime();
				numOfExecutions += outputItem.getNumOfExecutions();
				numOfSolverCalls += outputItem.getNumOfSolverCalls();
				numOfSymbolicExecutions += outputItem.getNumOfSymbolicExecutions();
				numOfSymbolicStatements += outputItem.getNumOfSymbolicStatements();
				solverRunningTime += outputItem.getSolverRunningTime();
				symbolicExecutionTime += outputItem.getSymbolicExecutionTime();
				runningTime += outputItem.getRunningTime();
				bugs.addAll(outputItem.getBugs());
			}
		}

		Console resultsAllInOne = new Console(new String[] { Console.LOAD_PROJECT,
				Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH, Console.TESTED_FUNCTIONS, inputPath, Console.CONFIG,
				configurePath, Console.LOG4J_LEVEL, "debug" });

		runningTime /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setRunningTime(runningTime);

		executionTime /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setExecutionTime(executionTime);

		makeCommandRunningNumber /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setMakeCommandRunningNumber(makeCommandRunningNumber);

		makeCommandRunningTime /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setMakeCommandRunningTime(makeCommandRunningTime);

		normalizationTime /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setNormalizationTime(normalizationTime);

		numOfExecutions /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setNumOfExecutions(numOfExecutions);

		numOfSolverCalls /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setNumOfSolverCalls(numOfSolverCalls);

		numOfSymbolicExecutions /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setNumOfSymbolicExecutions(numOfSymbolicExecutions);

		numOfSymbolicStatements /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setNumOfSymbolicStatements(numOfSymbolicStatements);

		solverRunningTime /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setSolverRunningTime(solverRunningTime);

		symbolicExecutionTime /= MAX_GENERATION;
		resultsAllInOne.getOutput().get(0).setSymbolicExecutionTime(symbolicExecutionTime);

		resultsAllInOne.exportToHtml(new File(JUNIT_REPORT), methodName);
	}

	protected class EO {

		public static final int UNSPECIFIED_NUM_OF_TEST_PATH = -1;
		public static final float MAXIMUM_COVERAGE = 100f;
		int nTestPath;
		float reachCoverage;

		public EO(int nTestPath, float reachCoverage) {
			super();
			this.nTestPath = nTestPath;
			this.reachCoverage = reachCoverage;
		}

		public int getnTestPath() {
			return nTestPath;
		}

		public float getExpectedCoverage() {
			return reachCoverage;
		}
	}

	// Most of functions do not contain macro, so that we do not perform macro
	// normalization to reduce the test data generation time.
	public static boolean ENABLE_MACRO_NORMALIZATION = false; // default value

	public static String JUNIT_REPORT = "C:\\Users\\Duc Anh Nguyen\\Desktop\\test.html";
	public static String LOG_CONFIGURATION_FILE = "./bin/log4j.properties";
}
