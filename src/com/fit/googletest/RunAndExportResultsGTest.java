package com.fit.googletest;

import com.fit.config.Paths;
import com.fit.googletest.xmlparser.IGoogleTestTestcase;
import com.fit.googletest.xmlparser.XMLofGoogleTestParser;
import com.fit.gui.testreport.object.IProjectReport;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.testdataexec.ConsoleExecution;
import com.fit.utils.Utils;
import com.fit.utils.UtilsVu;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

/**
 * Create makefile => create file .exe => parse .exe to get .xml =>get test
 * cases => fill "pass/fail" The structure of the unit test source code project
 * is described in details here:
 * <ul>
 * <li><b>Unit test source code project</b>
 * <ul>
 * <li>Google Test
 * <ul>
 * <li>include</li>
 * <li>src</li>
 * </ul>
 * </li>
 * <li>Makefile.win</li>
 * <li>[A copy of the tested project]</li>
 * <li>*.cpp/*.h (unit tests)</li>
 * </ul>
 * </li>
 * <ul>
 *
 * @author DucToan, nguyenducanh
 */
public class RunAndExportResultsGTest implements IRunAndExportResultsGTest {
    final static Logger logger = Logger.getLogger(RunAndExportResultsGTest.class);
    private List<IGoogleTestTestcase> testcases;
    private UnitTestProject unitTestProject;
    private IProjectReport projectReport;

    public static void main(String[] args) throws Exception {
        new ProjectParser(Utils.copy(Paths.SYMBOLIC_EXECUTION_TEST));
        File unitTestSourcecodeProject = new File("C:\\Users\\ducanhnguyen\\Desktop\\TestedGoogleTest");
        IProjectReport projectReport = null;// TODO: Gia lap project report

        if (projectReport != null) {
            IRunAndExportResultsGTest temp = new RunAndExportResultsGTest();
            temp.setEnvironment(projectReport, projectReport.generateUnitTest(unitTestSourcecodeProject));
            temp.exportResults();
        }
    }

    @Override
    public void setEnvironment(IProjectReport projectReport, UnitTestProject unitTestProject) throws Exception {
        this.unitTestProject = unitTestProject;
        this.projectReport = projectReport;
    }

    @Override
    public void exportResults() throws Exception {
        ConsoleExecution.compileMakefile(unitTestProject.getMakeFile());
        logger.debug("compile make file done...");

		/*
         * Run .exe to get result pass/fail stored in .xml file
		 */
        String xmlName = "test-report.xml";
        File xmlPath = new File(unitTestProject.getPath().getAbsolutePath() + File.separator + xmlName);
        String exportToXmlCommand = "--gtest_output=\"xml:./" + xmlName;
        UtilsVu.runCommand(unitTestProject.getExe(), null, unitTestProject.getPath(), exportToXmlCommand);
        logger.debug("run .exe done...");

		/*
		 * Parse file .xml
		 */
        try {
            testcases = new XMLofGoogleTestParser().parseXML(xmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        projectReport.updateTestcaseStates(testcases);
        logger.debug("update pass/fail done...");
    }

    @Override
    public List<IGoogleTestTestcase> getTestcases() {
        return testcases;
    }

}
