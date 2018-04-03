package com.fit.testdatagen;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fit.cfg.ICFG;
import com.fit.cfg.testpath.ITestpathInCFG;
import com.fit.config.AbstractSetting;
import com.fit.config.IFunctionConfig;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.exception.GUINotifyException;
import com.fit.gui.testedfunctions.ManageSelectedFunctionsDisplayer;
import com.fit.gui.testreport.object.IInputReport;
import com.fit.gui.testreport.object.IOutputReport;
import com.fit.gui.testreport.object.ITestedFunctionReport;
import com.fit.gui.testreport.object.InputReport;
import com.fit.gui.testreport.object.OutputReport;
import com.fit.gui.testreport.object.TestpathReport;
import com.fit.instrument.FunctionInstrumentationForStatementvsBranch_Marker;
import com.fit.parser.projectparser.ProjectLoader;
import com.fit.testdata.object.TestpathString_Marker;
import com.fit.testdatagen.fast.FastFunctionExecution;
import com.fit.testdatagen.htmlreport.BranchCoverage;
import com.fit.testdatagen.htmlreport.StatementCoverage;
import com.fit.testdatagen.htmlreport.SubConditionCoverage;
import com.fit.testdatagen.structuregen.ChangedToken;
import com.fit.testdatagen.structuregen.ChangedTokens;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.IProjectNode;
import com.fit.utils.Utils;
import com.fit.utils.search.ExeNodeCondition;
import com.fit.utils.search.Search;
import com.ibm.icu.util.Calendar;

import test.testdatageneration.Bug;

public abstract class AbstractTestdataGeneration implements ITestdataGeneration {
	final static Logger logger = Logger.getLogger(AbstractTestdataGeneration.class);
	// Number of using solver
	public static int numOfSolverCalls = 0;
	// Number of symbolic execution
	public static int numOfSymbolicExecutions = 0;
	// Number of symbolic statements. In each symbolic execution, some of
	// statements will be symbolic.
	public static int numOfSymbolicStatements = 0;
	// Number of execution
	public static int numOfExecutions = 0;
	// Number of solver calls but its solution does not increase coverage
	public static int numOfNotChangeToCoverage = 0;
	// Number of solver calls but can not solve (cause errors)
	public static int numOfSolverCallsbutCannotSolve = 0;
	// The total time of running make command (ms)
	public static long makeCommandRunningTime = 0;
	// The number of running make command
	public static int makeCommandRunningNumber = 0;
	// The total time of execution time (ms)
	public static long executionTime = 0;
	// The total time of waiting solver to get solution (ms)
	public static long solverRunningTime = 0;
	// The total time of generating test data (ms)
	public static long totalRunningTime;
	// The total time of normalizing function (ms)
	public static long normalizationTime;
	// The total time of symbolic execution (ms)
	public static long symbolicExecutionTime;
	// The total time of macro normalization (ms)
	public static long macroNormalizationTime;
	// Bugs detected while executing the program
	public static Set<Bug> bugs = new HashSet<>();

	protected IFunctionNode fn = null;
	protected ITestedFunctionReport fnReport = null;
	protected int numPossibleTestpath = -1;

	@Override
	public void generateTestdata() throws Exception {
		numOfSolverCalls = 0;
		numOfSymbolicExecutions = 0;
		numOfSymbolicStatements = 0;
		numOfExecutions = 0;
		numOfNotChangeToCoverage = 0;
		numOfSolverCallsbutCannotSolve = 0;
		makeCommandRunningTime = 0;
		makeCommandRunningNumber = 0;
		solverRunningTime = 0;
		totalRunningTime = 0;
		executionTime = 0;
		normalizationTime = 0;
		symbolicExecutionTime = 0;
		macroNormalizationTime = 0;
		bugs = new HashSet<>();

		Date startTime = Calendar.getInstance().getTime();

		this.generateTestdata(fn, fnReport);

		Date end = Calendar.getInstance().getTime();
		totalRunningTime = end.getTime() - startTime.getTime();
	}

	public AbstractTestdataGeneration(IFunctionNode fn, ITestedFunctionReport fnReport) {
		this.fnReport = fnReport;
		this.fn = fn;
		fnReport.setStrategy("Mars");

		// Initialize coverage based on configuration
		switch (this.fn.getFunctionConfig().getTypeofCoverage()) {
		case IFunctionConfig.BRANCH_COVERAGE:
			fnReport.setCoverage(new BranchCoverage(this.fn));
			break;
		case IFunctionConfig.SUBCONDITION:
			fnReport.setCoverage(new SubConditionCoverage(this.fn));
			break;
		case IFunctionConfig.STATEMENT_COVERAGE:
			fnReport.setCoverage(new StatementCoverage(this.fn));
			break;
		}
	}

	protected abstract void generateTestdata(IFunctionNode originalFunction, ITestedFunctionReport fnReport)
			throws Exception;

	/**
	 * Execute a function under testing
	 *
	 * @param staticSolution
	 *            test data
	 * @param changedTokens
	 * @param normalizedCFG
	 * @param originalFunction
	 * @return
	 * @throws Exception
	 */
	protected ITestdataExecution executeFunction(String staticSolution, ChangedTokens changedTokens,
			ITestedFunctionReport fnReport, ICFG normalizedCFG, IFunctionNode originalFunction) throws Exception {
		ITestdataExecution testdataExecution = executeTestdata(originalFunction, staticSolution);

		// If the function is executed successfully under test data
		if (fnReport != null && testdataExecution != null) {
			changedTokens.addAll(testdataExecution.getChangedTokens());
			boolean isCoverageIncreased = updateGUI(testdataExecution, changedTokens, originalFunction, staticSolution);
			TestpathString_Marker executionTestpath = testdataExecution.getEncodedTestpath();
			// logger.debug("[dynamic] execution tp = " + executionTestpath);

			if (isCoverageIncreased) {
				// Update the state of the CFG
				normalizedCFG.updateVisitedNodes_Marker(executionTestpath);
			} else {
				logger.debug("Hmm. No change to coverage...");
				numOfNotChangeToCoverage++;
			}

		} else {
			fnReport.setState("error");
			logger.error("Dynamic Error...");
		}
		return testdataExecution;
	}

	private boolean updateGUI(ITestdataExecution dynamic, ChangedTokens changedTokens, IFunctionNode originalFunction,
			String staticSolution) throws Exception {
		/*
		 * The test path generated by executing function is belonged to the normalized
		 * function. Therefore, this test path is transformed into the original test
		 * path.
		 * 
		 * Ex: we have a normalized test path "sv.age>0 => return 0". After transforming
		 * to the original test path, the test path becomes "sv.getAge()>0"
		 */
		TestpathString_Marker executionTestpath = dynamic.getEncodedTestpath();

		// update the changed token
		changedTokens.addAll(dynamic.getChangedTokens());

		String normalizedTestpath = restoreTestpath(executionTestpath.getStandardTestpathByProperty(
				FunctionInstrumentationForStatementvsBranch_Marker.STATEMENT), changedTokens);
		// logger.debug("Tp display in GUI = " + normalizedTestpath);

		// Create an input for report storage
		String exexutionTestpathInStr = "";
		for (String item : executionTestpath
				.getStandardTestpathByProperty(FunctionInstrumentationForStatementvsBranch_Marker.STATEMENT))
			exexutionTestpathInStr += item + ITestpathInCFG.SEPARATE_BETWEEN_NODES;
		/*
		 * Convert the test data generated from dynamic test data generation into it
		 * original form
		 */
		String initialize = dynamic.getInitialization();
		initialize = restoreTestdata(initialize, changedTokens);
		// logger.debug("Test data source code=" + initialize);

		/*
		 * Save test data in the tested function report
		 */
		IInputReport inputReport = new InputReport();
		inputReport.setDataTree(dynamic.getDataGen());
		inputReport.setChangedTokens(changedTokens);

		IOutputReport expectedOutputReport = new OutputReport("-");

		IOutputReport actualOutputReport = new OutputReport();

		String status = "-";
		TestpathReport tpReport = new TestpathReport(executionTestpath, normalizedTestpath, inputReport,
				expectedOutputReport, actualOutputReport, status, originalFunction);

		tpReport.setTestdata(initialize);

		fnReport.setTotalPossibleTestpath(numPossibleTestpath);
		/*
		 * Save the old coverage before adding new test path into the tested function
		 * report
		 */
		float oldCoverage = 0.0f; // assuming

		// Compute the latest coverage
		fnReport.addTestpath(tpReport);
		fnReport.setCurrentCoverage(fnReport.computeCoverage());

		boolean isExistTestpath = fnReport.checkLatestTestpath();

		/*
		 * If the new test path is not existed before
		 */
		if (!isExistTestpath)
			try {
				float newCoverage = fnReport.getCurrentCoverage();
				/*
				 * If coverage is increased, it means that the latest test path is useful :D
				 * 
				 * Otherwise, we remove the latest test path
				 */
				if (oldCoverage >= newCoverage)
					fnReport.removeTestpath(tpReport);
				else {
					if (Paths.START_FROM_COMMANDLINE) {
						// Not need to update GUI

					} else {
						ManageSelectedFunctionsDisplayer.getInstance().refresh();
						logger.debug("Refresh GUI done...");
					}
					return true;
				}

			} catch (Exception e) {
				logger.error("Compute coverage error...");
				e.printStackTrace();
			}
		return false;
	}

	/**
	 * Restore a test path to its original test path.
	 *
	 * @param testpath
	 * @param tokens
	 * @return
	 */
	protected String restoreTestpath(ArrayList<String> testpath, ChangedTokens tokens) {
		String originalTestpath = "";
		for (String testpathItem : testpath) {
			for (ChangedToken item : tokens)
				testpathItem = testpathItem.replaceAll(Utils.toRegex(item.getNewName()), item.getOldName());
			originalTestpath += testpathItem + ITestpathInCFG.SEPARATE_BETWEEN_NODES;
		}
		return originalTestpath.substring(0,
				originalTestpath.length() - new String(ITestpathInCFG.SEPARATE_BETWEEN_NODES).length());
	}

	/**
	 * Restore a test data to its original test data.
	 *
	 * @param testdata
	 * @param changedTokens
	 * @return
	 */
	protected String restoreTestdata(String testdata, ChangedTokens changedTokens) {
		String originalTestdata = testdata;
		for (ChangedToken item : changedTokens)
			originalTestdata = originalTestdata.replaceAll(Utils.toRegex(item.getNewName()), item.getOldName());
		return originalTestdata;
	}

	/**
	 * Execute function under a test data
	 *
	 * @param fn
	 * @param staticSolution
	 * @return
	 * @throws Exception
	 */
	protected ITestdataExecution executeTestdata(IFunctionNode fn, String staticSolution) throws Exception {
		ITestdataExecution dynamic = null;
		int MAX_NUMBER_OF_EXECUTION = 4;
		int MIN_NUMBER_OF_EXECUTION = 1;
		do {
			switch (AbstractSetting.getValue(ISettingv2.TESTDATA_STRATEGY)) {
			case ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.MARS2 + "":
				dynamic = new FunctionExecution(fn, staticSolution);
				break;
			case ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.FAST_MARS + "":
				dynamic = new FastFunctionExecution(fn, staticSolution);
				break;

			default:
				throw new Exception("Wrong test data generation strategy");
			}

			MIN_NUMBER_OF_EXECUTION++;
		} while (dynamic != null && dynamic.getEncodedTestpath().getEncodedTestpath().length() == 0
				&& MIN_NUMBER_OF_EXECUTION <= MAX_NUMBER_OF_EXECUTION);

		if (MIN_NUMBER_OF_EXECUTION > MAX_NUMBER_OF_EXECUTION)
			return null;
		else
			return dynamic;
	}

	/**
	 * Find the path of .exe
	 * 
	 * @param rootProject
	 *            The root of the testing project
	 * @throws Exception
	 */
	protected void getExePath(INode rootProject) throws Exception {
		// Create the absolute path of exe
		if (Utils.isWindows()) {

			if (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT == ISettingv2.PROJECT_DEV_CPP) {

				String nameExeFile = Utils.getNameOfExeInDevCppMakefile(Paths.CURRENT_PROJECT.MAKEFILE_PATH);

				if (!nameExeFile.equals("")) {
					String pathFolderEXE = Paths.CURRENT_PROJECT.MAKEFILE_PATH.substring(0,
							Paths.CURRENT_PROJECT.MAKEFILE_PATH.lastIndexOf(File.separator));

					Paths.CURRENT_PROJECT.EXE_PATH = pathFolderEXE + File.separator + nameExeFile;
				} else
					throw new GUINotifyException(
							"Compile project don't generate file \"*.exe\"!!!\nTool don't support for this project!!!");

			} else if (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT == ISettingv2.PROJECT_VISUALSTUDIO) {

				String nameProject = rootProject.getNewType() + ".exe";
				IProjectNode root = new ProjectLoader().load(new File(Utils.getRoot(rootProject).getAbsolutePath()));
				List<INode> nodeExe = Search.searchNodes(root, new ExeNodeCondition(), nameProject);

				if (nodeExe != null)
					Paths.CURRENT_PROJECT.EXE_PATH = nodeExe.get(0).getAbsolutePath();
				else
					throw new GUINotifyException(
							"Compile project don't generate file \"*.exe\"!!!\nTool don't support for this project!!!");
			} else if (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT == ISettingv2.PROJECT_ECLIPSE) {
				Paths.CURRENT_PROJECT.EXE_PATH = Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH + File.separator
						+ "Release" + File.separator + new File(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH).getName()
						+ ".exe";
				logger.debug("Exe name: " + Paths.CURRENT_PROJECT.EXE_PATH);
			}
		} else if (Utils.isUnix()) {
			switch (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT) {

			case ISettingv2.PROJECT_ECLIPSE: {
				Paths.CURRENT_PROJECT.EXE_PATH = Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH + File.separator
						+ "Release" + File.separator + new File(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH).getName();

				if (!new File(Paths.CURRENT_PROJECT.EXE_PATH).exists())
					Paths.CURRENT_PROJECT.EXE_PATH += ".exe";
				logger.debug("Exe name: " + Paths.CURRENT_PROJECT.EXE_PATH);
				break;
			}

			default: {
				String nameExeFile = Utils.getNameOfExeInDevCppMakefile(Paths.CURRENT_PROJECT.MAKEFILE_PATH);

				String pathFolderEXE = Paths.CURRENT_PROJECT.MAKEFILE_PATH.substring(0,
						Paths.CURRENT_PROJECT.MAKEFILE_PATH.lastIndexOf(File.separator));

				Paths.CURRENT_PROJECT.EXE_PATH = pathFolderEXE + File.separator + nameExeFile;
				break;
			}
			}
		}
	}

}
