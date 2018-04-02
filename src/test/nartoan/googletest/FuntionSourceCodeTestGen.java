package test.nartoan.googletest;

import com.fit.config.*;
import com.fit.googletest.IRunAndExportResultsGTest;
import com.fit.googletest.RunAndExportResultsGTest;
import com.fit.googletest.SourcecodeTestsGeneration;
import com.fit.googletest.UnitTestProject;
import com.fit.gui.testedfunctions.ExpectedOutputTable;
import com.fit.gui.testedfunctions.ManageSelectedFunctionsDisplayer;
import com.fit.gui.testreport.object.IProjectReport;
import com.fit.gui.testreport.object.ITestedFunctionReport;
import com.fit.gui.testreport.object.ProjectReport;
import com.fit.parser.makefile.CompilerFolderParser;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdatagen.MarsTestdataGeneration2;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.IVariableNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Auto test instead of run by hand
 *
 * @author DucToan
 */
public class FuntionSourceCodeTestGen implements IEnvironment, ICheck, IGenerateVituralData {

    private List<IFunctionNode> testedFunctionNodes;

    private IProjectReport projectReport;

    private UnitTestProject unitTestProject;

    public FuntionSourceCodeTestGen() {
        projectReport = ProjectReport.getInstance();

    }

    public static void main(String[] args) throws Exception {
        ProjectParser parser = new ProjectParser(Utils.copy(Paths.SYMBOLIC_EXECUTION_TEST));
        List<INode> functions = Search.searchNodes(parser.getRootTree(), new FunctionNodeCondition());

        List<IFunctionNode> temp = new ArrayList<>();
        for (INode node : functions)
            if (!node.getNewType().equals("main()"))
                temp.add((IFunctionNode) node);

        FuntionSourceCodeTestGen fnTestGen = new FuntionSourceCodeTestGen();
        fnTestGen.setEnvironment(new UnitTestProject(), new File("C:/Dev-Cpp"), new File("C:/z3/bin/z3.exe"));
        fnTestGen.setTestedFunctionNode(temp);
        /**
         * play...
         */
        fnTestGen.check();
    }

    @Override
    public void check() throws Exception {
        /**
         * Report test of list current functions, which save in
         * TestedFunctionReport
         */
        for (INode temp : testedFunctionNodes)
            if (temp instanceof IFunctionNode) {
                ITestedFunctionReport fnReport = projectReport.getFunction((IFunctionNode) temp);
                testdataGeneration(temp, fnReport);
            }

        /**
         * Build test data of function need test Auto generate EO và EXPECT_EQ
         */
        generateTestUnitForATestpath();

        /**
         * Save into folder and run
         */
        updateGTestProject(unitTestProject, projectReport);
    }

    /**
     * Emulator Expected Output and EXPECT_EQ
     *
     * @throws Exception
     */
    private void generateTestUnitForATestpath() throws Exception {
        /**
         * Create GUI EO virtual
         */
        ManageSelectedFunctionsDisplayer manage = new ManageSelectedFunctionsDisplayer();
        manage.setProjectReport(projectReport);
        manage.getContentPane().setVisible(true);
        manage.setVisible(true);

        for (int i = 0; i < testedFunctionNodes.size(); i++) {
            int numTestpath = projectReport.getFunction(testedFunctionNodes.get(i)).getTestpaths().size();
            for (int j = 0; j < numTestpath; j++) {
                manage.getContentPane().getListFunctionTable().setRowSelectionInterval(i, i);
                manage.refresh();
                manage.getContentPane().getDetailFunctionTable().setRowSelectionInterval(j, j);
                manage.invalidate();
                manage.revalidate();

                // Auto display interface import EO
                manage.getContentPane().viewExpectedOutput(j - 12345, j);

                /**
                 * Generate virtual data
                 */
                List<IVariableNode> expectedNodeTypes = testedFunctionNodes.get(i).getExpectedNodeTypes();
                VirtualDataInRows vituralDataInRows = generateVirtualData(expectedNodeTypes);
                Vector<Vector<Object>> duongInputForTable = vituralDataInRows.convertVituralDataToVector();

                /**
                 * Save virtual data into table EO
                 */
                JTable table = manage.getContentPane().getExpectedOutputPanel().getExpectedOutputTable();
                ((ExpectedOutputTable) table).setData(duongInputForTable);
                table.invalidate();
                table.revalidate();

                /**
                 * Confirm save data
                 */
                JButton btnSubmit = manage.getContentPane().getExpectedOutputPanel().getBtnSubmit();
                btnSubmit.doClick();
            }
        }
    }

    /**
     * Create virtual data
     *
     * @Toan: code here
     */
    @Override
    public VirtualDataInRows generateVirtualData(List<IVariableNode> expectedNodeTypes) {
        ToanClass toanClass = new ToanClass(expectedNodeTypes);
        return toanClass.generateVirtualData();
    }

    public IProjectReport getProjectReport() {
        return projectReport;
    }

    public void setProjectReport(IProjectReport projectReport) {
        this.projectReport = projectReport;
    }

    public List<IFunctionNode> getTestedFunctionNode() {
        return testedFunctionNodes;
    }

    public void setTestedFunctionNode(List<IFunctionNode> testedFunctionNode) {
        testedFunctionNodes = testedFunctionNode;
        /**
         * Reset object ProjectReport
         */
        projectReport.removeAll();
        for (IFunctionNode temp : testedFunctionNodes) {
            setConfig(temp);
            projectReport.addFunction(temp);
        }
        /**
         * Delete function main in project
         */
        INode root = Utils.getRoot(testedFunctionNodes.get(0));
        Utils.deleteMain(root);
    }

    private void setConfig(IFunctionNode function) {
        FunctionConfig functionConfig = new FunctionConfig();
        functionConfig.setCharacterBound(new Bound(32, 36));
        functionConfig.setIntegerBound(new Bound(45, 50));
        functionConfig.setSizeOfArray(3);
        function.setFunctionConfig(functionConfig);
    }

    @Override
    public void setEnvironment(UnitTestProject unitTestProject, File compliationFolder, File z3Solver)
            throws Exception {
        this.unitTestProject = unitTestProject;
        /**
         *
         */
        CompilerFolderParser compiler = new CompilerFolderParser(compliationFolder);
        compiler.parse();
        if (new File(compiler.getMakePath()).exists() && new File(compiler.getGccPath()).exists()
                && new File(compiler.getgPlusPlusPath()).exists()) {
            AbstractSetting.setValue(ISettingv2.GNU_MAKE_PATH, compiler.getMakePath());
            AbstractSetting.setValue(ISettingv2.GNU_GCC_PATH, compiler.getGccPath());
            AbstractSetting.setValue(ISettingv2.GNU_GPlusPlus_PATH, compiler.getgPlusPlusPath());
        } else
            throw new Exception("Đường dẫn biên dịch sai");
        /**
         *
         */
        AbstractSetting.setValue(ISettingv2.SOLVER_Z3_PATH, z3Solver.getAbsoluteFile());
    }

    /**
     * Generate test data of function need test and save in TestedFunctionReport
     *
     * @param testedFunctionNode
     * @param fnReport
     * @throws Exception
     */
    private void testdataGeneration(INode testedFunctionNode, ITestedFunctionReport fnReport) throws Exception {
        new MarsTestdataGeneration2((IFunctionNode) testedFunctionNode, fnReport).generateTestdata();
    }

    /**
     * Additional folder test into folder gtest
     *
     * @param unitTestSourcecode
     * @throws IOException
     * @throws Exception
     */
    public void updateGTestProject(UnitTestProject unitTestProject, IProjectReport projectReport) throws IOException {
        SourcecodeTestsGeneration srcTest = new SourcecodeTestsGeneration(projectReport, unitTestProject);
        srcTest.generateUnitTest();

        try {
            IRunAndExportResultsGTest temp = new RunAndExportResultsGTest();
            temp.setEnvironment(projectReport, unitTestProject);
            temp.exportResults();

        } catch (Exception e1) {

        }

    }

}
