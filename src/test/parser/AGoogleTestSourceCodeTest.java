package test.parser;

import com.fit.config.*;
import com.fit.googletest.IRunAndExportResultsGTest;
import com.fit.googletest.UnitTestProject;
import com.fit.gui.testedfunctions.ExceptedExceptionOutputItem;
import com.fit.gui.testedfunctions.ExpectedOutputData;
import com.fit.gui.testedfunctions.ExpectedOutputItem;
import com.fit.gui.testreport.object.*;
import com.fit.parser.makefile.CompilerFolderParser;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.FunctionExecution;
import com.fit.testdatagen.ITestdataExecution;
import com.fit.testdatagen.htmlreport.BranchCoverage;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.SoundUtils;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AGoogleTestSourceCodeTest {
    /**
     * Source code of google test, including "include" and "src" folder
     */
    public static final String ORIGINAL_GOOGLE_TEST_FOLDER_PATH = "C:\\Users\\ducanhnguyen\\Desktop\\OriginalGoogleTest";
    /**
     * Destination of the unit test source code project
     */
    public static final String YOUR_UNIT_TEST_PROJECT_PATH = "C:\\Users\\ducanhnguyen\\Desktop\\YourGoogleTest";
    final static Logger logger = Logger.getLogger(AGoogleTestSourceCodeTest.class);
    public final boolean PASS_TEST = true;
    public final boolean FALSE_TEST = false;
    public final String devCpp = "D:\\Dev-Cpp";
    public List<String> passedFunctions = new ArrayList<>();
    public List<String> failedFunctions = new ArrayList<>();

    protected void initializeEnvironment(String devCpp, String originalGoogleTest, String yourUnitTestProject) {
        try {
            CompilerFolderParser compiler = new CompilerFolderParser(new File(devCpp));
            compiler.parse();
            AbstractSetting.setValue(ISettingv2.GNU_MAKE_PATH, compiler.getMakePath());
            AbstractSetting.setValue(ISettingv2.GNU_GCC_PATH, compiler.getGccPath());
            AbstractSetting.setValue(ISettingv2.GNU_GPlusPlus_PATH, compiler.getgPlusPlusPath());

            //
            Utils.deleteFileOrFolder(new File(yourUnitTestProject));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    protected void setConfig(IFunctionNode function) {
        FunctionConfig functionConfig = new FunctionConfig();
        functionConfig.setCharacterBound(new Bound(32, 126));
        functionConfig.setIntegerBound(new Bound(-100, 200));
        functionConfig.setSizeOfArray(10);
        functionConfig.setMaximumInterationsForEachLoop(2);
        functionConfig.setSolvingStrategy(ISettingv2.SUPPORT_SOLVING_STRATEGIES[0]);
        function.setFunctionConfig(functionConfig);
    }

    protected IFunctionNode getFunctionNode(String projectPath, String nameMethod) {
        ProjectParser parser = new ProjectParser(new File(projectPath));
        IFunctionNode testedFunction = (IFunctionNode) Search
                .searchNodes(parser.getRootTree(), new FunctionNodeCondition(), nameMethod).get(0);
        setConfig(testedFunction);
        return testedFunction;
    }

    /**
     * @param expectOutput
     * @return
     */
    protected ExpectedOutputData convertToExpectedOutput(String[] expectedOutput) {
        ExpectedOutputData expectedOutputSet = new ExpectedOutputData();
        for (String item : expectedOutput) {
            String type = item.split(",")[0];
            String name = IGoogleTestNameRule.EO_EXPECTED_OUTPUT_PREFIX_IN_SOURCECODE + item.split(",")[1];
            String value = item.split(",")[2];
            if (type.equals("throw"))
                expectedOutputSet.add(new ExceptedExceptionOutputItem(name, type, value));
            else
                expectedOutputSet.add(new ExpectedOutputItem(name, type, value));
        }
        return expectedOutputSet;
    }

    /**
     * Test a function is declared and defined in class
     *
     * @throws Exception
     */
    protected void test(String projectPath, String nameMethod, String staticSolution, String[] expectedOutput,
                        boolean expectedResultState) throws Exception {
        File clonedProject = Utils.copy(projectPath);
        logger.debug(nameMethod + "(" + staticSolution + ")");
        Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH = clonedProject.getAbsolutePath();
        /*
		 * Initialization
		 */
        initializeEnvironment(devCpp, AGoogleTestSourceCodeTest.ORIGINAL_GOOGLE_TEST_FOLDER_PATH,
                AGoogleTestSourceCodeTest.YOUR_UNIT_TEST_PROJECT_PATH);

        ProjectReport.getInstance().removeAll();
        IFunctionNode testedFunction = getFunctionNode(clonedProject.getAbsolutePath(), nameMethod);
        logger.debug(testedFunction.getAST().getRawSignature());
        ProjectReport.getInstance().addFunction(testedFunction);

		/*
		 * 
		 */
        ITestedFunctionReport fnReport = ProjectReport.getInstance().getFunction(testedFunction);
        fnReport.setCoverage(new BranchCoverage(testedFunction));

        // Generate input report
        IInputReport inputReport = new InputReport();
        ITestdataExecution dynamic = new FunctionExecution(testedFunction, staticSolution);
        inputReport.setDataTree(dynamic.getDataGen());

        // Generate output report
        IOutputReport expectedOutputReport = new OutputReport();
        expectedOutputReport.setExpectedValues(convertToExpectedOutput(expectedOutput));

        String status = "-";

        // Add to the tested function
        ITestpathReport tpReport = new TestpathReport(null, "", inputReport, expectedOutputReport, new OutputReport(),
                status, testedFunction);
        fnReport.addTestpath(tpReport);

		/*
		 * Create unit test project
		 */
        UnitTestProject unitTestProject = ProjectReport.getInstance()
                .generateUnitTest(new File(AGoogleTestSourceCodeTest.YOUR_UNIT_TEST_PROJECT_PATH));

		/*
		 * Run unit test project
		 */
        IRunAndExportResultsGTest unitTestRunner = unitTestProject.execute();
        logger.debug("run google test project done...");
		/*
		 * 
		 */
        logger.debug("Actual test report = " + unitTestRunner.getTestcases().toString());
        logger.debug("Expected output = " + expectedResultState);

        boolean result = unitTestRunner.getTestcases().get(0).getResult();
        if (result == expectedResultState) {
            passedFunctions.add(nameMethod + "(" + staticSolution + ")");
            logger.debug("PASS");
            SoundUtils.notifySuccess();
        } else {
            failedFunctions.add(nameMethod + "(" + staticSolution + ")");
            logger.debug("FAILED");
            SoundUtils.notifyFailed();
        }
    }

}