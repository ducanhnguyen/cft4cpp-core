package com.fit.gui.testreport.object;

import com.fit.googletest.SourceCodeGoogleTest;
import com.fit.tree.object.ISourcecodeFileNode;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a source code file report
 *
 * @author DucAnh
 */
public class SourcecodeFileReport implements ISourcecodeFileReport {

    protected ISourcecodeFileNode sourcecodeFileNode;
    protected String name = "";
    protected List<ITestedFunctionReport> functions = new ArrayList<>();

    public SourcecodeFileReport(ISourcecodeFileNode sourcecodeFileNode) {
        this.sourcecodeFileNode = sourcecodeFileNode;
        name = sourcecodeFileNode.getAbsolutePath()
                .substring(sourcecodeFileNode.getAbsolutePath().lastIndexOf(File.separator) + 1);
    }

    public SourcecodeFileReport(String name, List<ITestedFunctionReport> functions) {
        this.name = name;
        this.functions = functions;

        sourcecodeFileNode.setName("sourcecodeFileNode");
        sourcecodeFileNode.setAbsolutePath("sourcecodeFileNode");
    }

    public SourcecodeFileReport(String name, TestedFunctionReport... functions) {
        this.name = name;
        for (TestedFunctionReport function : functions)
            this.functions.add(function);
    }

    @Override
    public void addTestFunctionReport(ITestedFunctionReport functionReport) {
        /**
         * Kiem tra function da duoc add chua
         */
        boolean isExist = false;
        for (ITestedFunctionReport item : getTestedFunctionReports())
            if (item.getFunctionNode().equals(functionReport.getFunctionNode())) {
                isExist = true;
                break;
            }
        /**
         * Neu chua add
         */
        if (!isExist)
            getTestedFunctionReports().add(functionReport);
    }

    @Override
    public List<ITestedFunctionReport> getTestedFunctionReports() {
        return functions;
    }

    @Override
    public void setTestedFunctionReports(List<ITestedFunctionReport> functions) {
        this.functions = functions;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ISourcecodeFileNode getSourcecodeFileNode() {
        return sourcecodeFileNode;
    }

    @Override
    public void setSourcecodeFileNode(ISourcecodeFileNode sourcecodeFileNode) {
        this.sourcecodeFileNode = sourcecodeFileNode;
    }

    @Override
    public String toString() {
        String output = "";
        for (ITestedFunctionReport s : getTestedFunctionReports())
            output += s.toString() + "\n";
        return output;
    }

    @Override
    public String generateUnitTest(File originalTestedProject, File cloneTestedProject) {
        String sourcecodeTest = "";

		/*
         * Check function whether it can generate unit test
		 */
        boolean canbeGenUnitTest = false;
        for (ITestedFunctionReport function : getTestedFunctionReports())
            for (ITestpathReport testpath : function.getTestpaths())
                if (testpath.isCanBeExportToUnitTest())
                    canbeGenUnitTest = true;
		/*
		 * Build source google test for a file .cpp/.h
		 */
        if (canbeGenUnitTest) {
			/*
			 * Save list source code test
			 */
            String nameFileTest = getName();
            String extension = getName().substring(getName().indexOf("."));
            nameFileTest = nameFileTest.replace(extension, "_test" + extension);
            sourcecodeTest = new SourceCodeGoogleTest(nameFileTest,
                    generateIncludes(originalTestedProject, cloneTestedProject), sourcecodeTest).getSourceCode();
        }
        return sourcecodeTest;
    }

    /**
     * Generate #includes of a file test to run auto and export to file
     *
     * @return
     */
    private List<String> generateIncludes(File originalTestedProject, File cloneTestedProject) {
        List<String> includes = new ArrayList<>();

        // include google test header
        String includeGoogleTest = "#include " + SpecialCharacter.DOUBLE_QUOTES + "gtest/gtest.h"
                + SpecialCharacter.DOUBLE_QUOTES + SpecialCharacter.LINE_BREAK;
        includes.add(includeGoogleTest);

		/*
		 * The path of the source code is relative to the unit test project
		 */
        List<String> includedPaths = Utils.findHeaderFiles(originalTestedProject.getAbsolutePath());

        for (String include : includedPaths) {
            String testedProjectPath = Utils.normalizePath(originalTestedProject.getParent());
            String shortenInclude = Utils.normalizePath(include).replace(testedProjectPath, ".");
            includes.add(
                    "#include " + SpecialCharacter.DOUBLE_QUOTES + shortenInclude + SpecialCharacter.DOUBLE_QUOTES);
        }

        return includes;
    }

    @Override
    public boolean canBeExportToUnitTest() {
        for (ITestedFunctionReport tp : getTestedFunctionReports())
            if (tp.canBeExporttoUnitTest())
                return true;
        return false;
    }

}
