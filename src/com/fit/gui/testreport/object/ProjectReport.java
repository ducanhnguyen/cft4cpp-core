package com.fit.gui.testreport.object;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.googletest.UnitTestProjectGeneration;
import com.fit.googletest.xmlparser.IGoogleTestTestcase;
import com.fit.gui.testreport.ExcelExporter;
import com.fit.gui.testreport.IExcelExporter;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.ISourcecodeFileNode;
import com.fit.utils.Utils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DucAnh
 */
public class ProjectReport implements IProjectReport {
    final static Logger logger = Logger.getLogger(ProjectReport.class);
    /**
     * Singleton pattern
     */
    private static IProjectReport instance = null;
    protected List<SourcecodeFileReport> sourcecodeFiles = new ArrayList<>();

    private ProjectReport() {
    }

    public ProjectReport(List<SourcecodeFileReport> sourcecodeFiles) {
        this.sourcecodeFiles = sourcecodeFiles;
    }

    public ProjectReport(SourcecodeFileReport... sourcecodeFiles) {
        for (SourcecodeFileReport sourcecodeFile : sourcecodeFiles)
            this.sourcecodeFiles.add(sourcecodeFile);
    }

    public static IProjectReport getInstance() {
        if (ProjectReport.instance == null)
            ProjectReport.instance = new ProjectReport();
        return ProjectReport.instance;
    }

    @Override
    public void addFunction(IFunctionNode function) {
        ISourcecodeFileNode sourcecodeFileNode = Utils.getSourcecodeFile(function);
        /**
         * Hai trường hợp xảy ra:
         *
         * - Tệp chứa hàm đã thêm vào trước đó
         *
         * - Tệp chứa hàm chưa thêm vào
         */
        ISourcecodeFileReport report = existSourcecodeFile(sourcecodeFileNode);
        TestedFunctionReport functionReport = new TestedFunctionReport(function);

        if (report == null) {
            SourcecodeFileReport sourcecodeFileReport = new SourcecodeFileReport(sourcecodeFileNode);
            getSourcecodeFiles().add(sourcecodeFileReport);
            //
            sourcecodeFileReport.addTestFunctionReport(functionReport);
        } else
            report.addTestFunctionReport(functionReport);
    }

    @Override
    public boolean contain(IFunctionNode n) {
        return getFunction(n) == null ? false : true;
    }

    /**
     * Kiểm tra report đã thêm tệp nào đó chưa
     *
     * @return
     */
    private ISourcecodeFileReport existSourcecodeFile(ISourcecodeFileNode sourcecode) {
        for (ISourcecodeFileReport sourcecodeFileReport : ProjectReport.getInstance().getSourcecodeFiles())
            if (sourcecodeFileReport.getSourcecodeFileNode().equals(sourcecode))
                return sourcecodeFileReport;

        return null;
    }

    @Override
    public ITestedFunctionReport getFunction(IFunctionNode n) {
        for (ISourcecodeFileReport sourcecode : getSourcecodeFiles())
            for (ITestedFunctionReport function : sourcecode.getTestedFunctionReports())
                if (function.getFunctionNode().equals(n))
                    return function;
        return null;
    }

    @Override
    public List<SourcecodeFileReport> getSourcecodeFiles() {
        return sourcecodeFiles;
    }

    @Override
    public void removeAll() {
        sourcecodeFiles.removeAll(sourcecodeFiles);
    }

    @Override
    public boolean removeFunction(IFunctionNode n) {
        ITestedFunctionReport deletedFunction = getFunction(n);
        if (n == null)
            return false;
        for (ISourcecodeFileReport sourcecode : getSourcecodeFiles())
            if (sourcecode.getTestedFunctionReports().contains(deletedFunction))
                sourcecode.getTestedFunctionReports().remove(deletedFunction);
        return false;
    }

    @Override
    public String toString() {
        String output = "";
        for (ISourcecodeFileReport s : getSourcecodeFiles())
            output += s.toString() + "\n";
        return output;
    }

    @Override
    public void exportToExcel(String excelPath) {
        try {
            IExcelExporter excelExporter = new ExcelExporter(this, excelPath);
            excelExporter.exportToExcel();
            logger.debug("Export excel done...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTestcaseStates(List<IGoogleTestTestcase> testCases) {
        for (ISourcecodeFileReport sourcecode : getSourcecodeFiles())
            for (ITestedFunctionReport function : sourcecode.getTestedFunctionReports())
                for (ITestpathReport testpath : function.getTestpaths())
                /**
                 * Compare with input value
                 */
                    for (IGoogleTestTestcase testCase : testCases)
                        if (testCase.getTestSuite().equals(testpath.getIdentification().getTestSuite())
                                && testCase.getTestCase().equals(testpath.getIdentification().getTestCase()))
                            testpath.setPass(testCase.getResult() + "");
    }

    @Override
    public com.fit.googletest.UnitTestProject generateUnitTest(File unitTestProjectPath) throws Exception {
        UnitTestProjectGeneration unitTestGen = new UnitTestProjectGeneration();

        unitTestGen.setEnvironment(new File(AbstractSetting.getValue(ISettingv2.GNU_GENERAL)),
                new File(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH), unitTestProjectPath, this,
                new File(AbstractSetting.getValue(ISettingv2.ORIGINAL_GOOGLE_TEST)));
        unitTestGen.generateUnitTestProject();
        return unitTestGen.getUnitTestProject();
    }

}
