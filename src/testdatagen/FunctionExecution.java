package testdatagen;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cfg.testpath.ITestpathInCFG;
import config.AbstractSetting;
import config.ISettingv2;
import config.Paths;
import config.Settingv2;
import exception.GUINotifyException;
import instrument.FunctionInstrumentationForStatementvsBranch_Marker;
import normalizer.PrivateToPublicNormalizer;
import parser.projectparser.ProjectLoader;
import parser.projectparser.ProjectParser;
import testdata.object.TestpathString_Marker;
import testdatagen.module.DataTreeGeneration;
import testdatagen.module.IDataTreeGeneration;
import testdatagen.structuregen.ChangedTokens;
import testdatagen.testdataexec.ConsoleExecution;
import testdatagen.testdataexec.TestdriverGeneration;
import testdatagen.testdataexec.TestdriverGenerationforC;
import testdatagen.testdataexec.TestdriverGenerationforCpp;
import tree.object.CFileNode;
import tree.object.CppFileNode;
import tree.object.FunctionNode;
import tree.object.IFunctionNode;
import tree.object.INode;
import tree.object.IProjectNode;
import utils.Utils;
import utils.search.ExeNodeCondition;
import utils.search.FunctionNodeCondition;
import utils.search.ObjectNodeCondition;
import utils.search.Search;
import utils.search.SourcecodeFileNodeCondition;

/**
 * Executing function under test data to get test path
 * 
 * @author DucAnh
 */
public class FunctionExecution implements ITestdataExecution {
	final static Logger logger = Logger.getLogger(FunctionExecution.class);
	protected static int id = 0;
	protected String initialization = "";
	protected TestpathString_Marker encodedTestpath = new TestpathString_Marker();
	protected IDataTreeGeneration dataGen = new DataTreeGeneration();
	protected ChangedTokens changedTokens = new ChangedTokens();

	public FunctionExecution() {
	}

	/**
	 * Create function execution
	 * 
	 * @param fn
	 *            function
	 * @param variableValues
	 *            value of variables.
	 * @throws Exception
	 */
	public FunctionExecution(IFunctionNode fn, String variableValues) throws Exception {

		if (isInitializedCompilerEnvironment()) {
			Backup backup = saveCurrentState(fn);
			try {
				INode rootProject = Utils.findRootProject(fn);

				// create for storing execution test path
				Paths.CURRENT_PROJECT.CURRENT_TESTDRIVER_EXECUTION_NAME = FunctionExecution.id++
						+ Paths.CURRENT_PROJECT.TESTDRIVER_EXECUTION_NAME_POSTFIX;
				String executionFilePath = "";

				switch (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT) {

				case ISettingv2.PROJECT_ECLIPSE: {
					executionFilePath = new File(Utils.getSourcecodeFile(fn).getAbsolutePath()).getParent()
							+ File.separator + Paths.CURRENT_PROJECT.CURRENT_TESTDRIVER_EXECUTION_NAME;
					break;
				}
				case ISettingv2.PROJECT_DEV_CPP:
				case ISettingv2.PROJECT_CODEBLOCK:
				case ISettingv2.PROJECT_CUSTOMMAKEFILE:
				case ISettingv2.PROJECT_VISUALSTUDIO:
					executionFilePath = new File(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH).getCanonicalPath()
							+ File.separator + Paths.CURRENT_PROJECT.CURRENT_TESTDRIVER_EXECUTION_NAME;
					break;
				}
				executionFilePath = executionFilePath.replace("\\", "/");
				Paths.CURRENT_PROJECT.CURRENT_TESTDRIVER_EXECUTION_PATH = executionFilePath;
				Utils.writeContentToFile("", executionFilePath);

				/*
				 * Normalize function before executing it
				 */
				logger.debug("Normalize function before executing it");
				IFunctionNode clone = (IFunctionNode) fn.clone();
				clone.setAST(clone.getGeneralNormalizationFunction().getNormalizedAST());
				changedTokens = clone.getGeneralNormalizationFunction().getTokens();
				updateProject(backup, fn);

				/*
				 * Generate test driver
				 */
				logger.debug("Generate test driver");
				dataGen = clone.generateDataTree(FunctionExecution.staticSolutionsGen(variableValues));
				dataGen.setFunctionNode(clone);
				dataGen.generateTree();
				initialization = dataGen.getInputforGoogleTest();
				String functionCall = dataGen.getFunctionCall();

				logger.debug("driver=" + initialization.replace("\n", "").replace("\t", "")
						+ functionCall.replace("\n", "") + "...");

				TestdriverGeneration testdriverGen = null;
				if (Utils.getSourcecodeFile(fn) instanceof CFileNode)
					testdriverGen = new TestdriverGenerationforC();
				else if (Utils.getSourcecodeFile(fn) instanceof CppFileNode)
					testdriverGen = new TestdriverGenerationforCpp();
				else
					throw new Exception("Dont support this type of file source code");
				if (testdriverGen != null) {
					testdriverGen.setTestedFunction(clone);
					testdriverGen.setInitialization(initialization);
					testdriverGen.setFunctionCall(functionCall);
					testdriverGen.generate();

					Utils.writeContentToFile(testdriverGen.getCompleteSourceFile(), backup.getFnParent());

					ConsoleExecution.compileMakefile(new File(Paths.CURRENT_PROJECT.MAKEFILE_PATH));

					getExePath(rootProject, executionFilePath);
					executeExecutableFile(rootProject, executionFilePath);
				}
			} catch (GUINotifyException e) {
				e.printStackTrace();
				throw new GUINotifyException(e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				initialization = "";

			} finally {
				backup.restore();
				killExeProcess(Paths.CURRENT_PROJECT.EXE_PATH);
				if (encodedTestpath.getEncodedTestpath().length() == 0)
					initialization = "";
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Settingv2.create();
		AbstractSetting.setValue(ISettingv2.SOLVER_Z3_PATH, "C:/z3/bin/z3.exe");
		AbstractSetting.setValue(ISettingv2.GNU_MAKE_PATH, "C:/Dev-Cpp/MinGW64/bin/mingw32-make.exe");
		AbstractSetting.setValue(ISettingv2.GNU_GCC_PATH, "C:/Dev-Cpp/MinGW64/bin/gcc.exe");
		AbstractSetting.setValue(ISettingv2.GNU_GPlusPlus_PATH, "C:/Dev-Cpp/MinGW64/bin/g++.exe");

		ProjectParser parser = new ProjectParser(new File(Paths.TSDV_R1));
		FunctionNode testedFunction = (FunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "IntTest(int)").get(0);

		String preparedInput = "";
		new FunctionExecution(testedFunction, preparedInput);
	}

	/**
	 * Convert the static solution to map for analyzing more easily
	 * 
	 * @param staticSolution
	 * @return
	 */
	protected static Map<String, Object> staticSolutionsGen(String staticSolution) {
		Map<String, Object> staticSolutions = new HashMap<>();
		if (staticSolution.length() > 0) {
			String[] elements = staticSolution.split(";");
			for (String element : elements) {
				String key = element.split("=")[0];
				String value = element.split("=")[1];
				Utils.toInt(value);
				if (Utils.toInt(value) == Utils.UNDEFINED_TO_INT)
					staticSolutions.put(key, value);
				else
					staticSolutions.put(key, new Integer(Utils.toInt(value)));
			}
		}
		return staticSolutions;
	}

	@Override
	public String normalizeTestpathFromFile(String testpath) {
		testpath = testpath.replace("\r\n", ITestpathInCFG.SEPARATE_BETWEEN_NODES)
				.replace("\n\r", ITestpathInCFG.SEPARATE_BETWEEN_NODES)
				.replace("\n", ITestpathInCFG.SEPARATE_BETWEEN_NODES)
				.replace("\r", ITestpathInCFG.SEPARATE_BETWEEN_NODES);
		if (testpath.equals(ITestpathInCFG.SEPARATE_BETWEEN_NODES))
			testpath = "";
		return testpath;
	}

	/**
	 * Get path of the .exe file
	 * 
	 * @param rootProject
	 * @param executionFilePath
	 * @throws Exception
	 */
	protected void getExePath(INode rootProject, String executionFilePath) throws Exception {
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

	/**
	 * Execute the .exe file
	 *
	 * @param rootProject
	 * @param executionFilePath
	 * @throws Exception
	 */
	protected void executeExecutableFile(INode rootProject, String executionFilePath) throws Exception {
		// Execute .exe
		if (!new File(Paths.CURRENT_PROJECT.EXE_PATH).exists()) {
			throw new Exception("Dont found exe");

		} else {
			// logger.debug("Start executing function");
			boolean isTerminated = ConsoleExecution.executeExe(new File(Paths.CURRENT_PROJECT.EXE_PATH));
			if (isTerminated)
				logger.info("Terminate .exe due to too long!!!");

			// logger.debug("Start executing function...done");
			AbstractTestdataGeneration.numOfExecutions++;

			// Read hard disk until the test path is written into file completely
			int MAX_READ_FILE_NUMBER = 10;
			int countReadFile = 0;
			do {
				logger.info("Finish. We are getting a execution path from hard disk");
				encodedTestpath.setEncodedTestpath(normalizeTestpathFromFile(Utils.readFileContent(executionFilePath)));

				if (encodedTestpath.getEncodedTestpath().length() == 0) {
					initialization = "";
					Thread.sleep(10);
				}

				countReadFile++;
			} while (encodedTestpath.getEncodedTestpath().length() == 0 && countReadFile <= MAX_READ_FILE_NUMBER);

			// Only for log
			if (encodedTestpath.getEncodedTestpath().length() > 0) {
				String tp = "";
				List<String> stms = encodedTestpath
						.getStandardTestpathByProperty(FunctionInstrumentationForStatementvsBranch_Marker.STATEMENT);
				for (String stm : stms)
					tp += stm + "=>";
				tp = tp.substring(0, tp.length() - 2);
				logger.debug("Done. Execution test path = " + tp);
			} else
				logger.debug("Done. Empty test path");
		}
	}

	/**
	 * Create compiler environment
	 * 
	 * @return
	 * @throws Exception
	 */
	protected boolean isInitializedCompilerEnvironment() throws Exception {
		switch (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT) {
		case ISettingv2.PROJECT_DEV_CPP:
			if (!Utils.isAvailable(AbstractSetting.getValue(ISettingv2.GNU_MAKE_PATH)))
				throw new GUINotifyException("mingw32-make.exe not found!");
			if (!Utils.isAvailable(AbstractSetting.getValue(ISettingv2.GNU_GCC_PATH)))
				throw new Exception("gcc not found!");
			if (!Utils.isAvailable(AbstractSetting.getValue(ISettingv2.GNU_GPlusPlus_PATH)))
				throw new GUINotifyException("g++.exe not found!");

			if (!Utils.isAvailable(Paths.CURRENT_PROJECT.MAKEFILE_PATH))
				throw new GUINotifyException("Makefile.win not found!");
			break;
		case ISettingv2.PROJECT_VISUALSTUDIO:
			if (!Utils.isAvailable(AbstractSetting.getValue(ISettingv2.MSBUILD_PATH)))
				throw new GUINotifyException("Msbuild not found!");
			break;
		case ISettingv2.PROJECT_CODEBLOCK:
			break;
		case ISettingv2.PROJECT_UNKNOWN_TYPE:
			break;
		case ISettingv2.PROJECT_CUSTOMMAKEFILE:
			break;
		}

		return true;
	}

	protected void killExeProcess(String pathEXE) throws Exception {
		File f = new File(pathEXE);
		if (!f.delete()) {
			String processName = pathEXE.substring(pathEXE.lastIndexOf(File.separator) + 1);

			if (new File(pathEXE).exists())
				ConsoleExecution.killProcess(processName);
			if (f.exists()) {
				Thread.sleep(10);

				killExeProcess(pathEXE);
			} else {
			}
		}
	}

	/**
	 * Create back up of the given project
	 */
	protected Backup saveCurrentState(INode fn) throws Exception {
		Backup state = new Backup();

		state.setFnParent(Utils.getSourcecodeFile(fn));
		state.setContentOfTestFunctionParent(Utils.readFileContent(state.getFnParent()));

		//
		INode nodeRoot = Utils.findRootProject(fn);
		if (nodeRoot != null) {
			List<INode> mainNodesType1 = Search.searchNodes(nodeRoot, new FunctionNodeCondition(), "main()");
			List<INode> mainNodesType2 = Search.searchNodes(nodeRoot, new FunctionNodeCondition(), "main(int,char**)");
			mainNodesType1.addAll(mainNodesType2);

			if (mainNodesType1.size() >= 2)
				throw new GUINotifyException(
						"Project has more than a \"main\" function!!\nPlease check struct's project again!!");
			if (Utils.isAvailable(mainNodesType1)) {
				state.setMainParent(mainNodesType1.get(0).getParent());
				state.setContentOfMainParent(Utils.readFileContent(state.getMainParent()));
			}
		} else {
			logger.error("Dont found root project of " + fn.getNewType());
		}

		return state;

	}

	protected void updateProject(Backup backup, INode fn) throws Exception {
		// Update file that contains the tested function
		String updateTestedFunctionParent = Utils.deleteOrRenameTestedFunction((IFunctionNode) fn);
		Utils.writeContentToFile(updateTestedFunctionParent, backup.getFnParent());

		// Delete all object files
		List<INode> objectFiles = Search.searchNodes(Utils.getRoot(fn), new ObjectNodeCondition());
		for (INode objectFile : objectFiles)
			Utils.deleteFileOrFolder(new File(objectFile.getAbsolutePath()));

		// Remove main() from function
		if (backup.getMainParent() != null) {
			Utils.deleteMain(backup.getMainParent());
		}

		// Convert all private keyword to public. It tries to replace all "private"
		// words with "public".
		// Actually, this is only a raw strategy. It should be upgrade more in the next
		// version.
		for (INode fileNode : Search.searchNodes(Utils.getRoot(fn), new SourcecodeFileNodeCondition())) {
			String content = Utils.readFileContent(fileNode.getAbsolutePath());
			PrivateToPublicNormalizer privateNorm = new PrivateToPublicNormalizer();
			privateNorm.setOriginalSourcecode(content);
			privateNorm.normalize();
			content = privateNorm.getNormalizedSourcecode();
			Utils.writeContentToFile(content, fileNode.getAbsolutePath());
		}
	}

	@Override
	public IDataTreeGeneration getDataGen() {
		return dataGen;
	}

	@Override
	public String getInitialization() {
		return initialization;
	}

	@Override
	public void setInitialization(String initialize) {
		initialization = initialize;
	}

	@Override
	public TestpathString_Marker getEncodedTestpath() {
		return encodedTestpath;
	}

	@Override
	public void setEncodedTestpath(TestpathString_Marker testpath) {
		this.encodedTestpath = testpath;
	}

	@Override
	public ChangedTokens getChangedTokens() {
		return changedTokens;
	}

	@Override
	public void setChangedTokens(ChangedTokens changedTokens) {
		this.changedTokens = changedTokens;
	}

}
