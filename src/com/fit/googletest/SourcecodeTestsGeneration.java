package com.fit.googletest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fit.gui.testreport.object.IProjectReport;
import com.fit.gui.testreport.object.ISourcecodeFileReport;
import com.fit.gui.testreport.object.ITestedFunctionReport;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.Utils;

/**
 * Generate source code unit test for all file .cpp/.h which chosen to test
 *
 * @author ducanh
 */
public class SourcecodeTestsGeneration {

    /**
     * The path of the original tested project
     */
    private File originalTestedProject;
    private File cloneTestedProject;
    private IProjectReport projectReport;
    private List<ISourceCodeGoogleTest> sourcecodeTests = new ArrayList<>();

    /**
     * @param projectReport
     * @param unitTestProject the path of the tested project
     */
    public SourcecodeTestsGeneration(IProjectReport projectReport, UnitTestProject unitTestProject) {
        this.projectReport = projectReport;
        originalTestedProject = unitTestProject.getOriginalTestedProject();
        cloneTestedProject = unitTestProject.getCloneTestedProject();
    }

    public static void main(String[] args) throws Exception {
    }

    /**
     * Generate #includes of a file test to run auto and export to file
     *
     * @return
     */
    private List<String> generateIncludes(ISourcecodeFileReport sourcecode, File originalTestedProject,
                                          File cloneTestedProject) {
        List<String> includes = new ArrayList<>();

        String includeGoogleTest = "#include " + SpecialCharacter.DOUBLE_QUOTES + "gtest/gtest.h"
                + SpecialCharacter.DOUBLE_QUOTES + SpecialCharacter.LINE_BREAK;

        List<String> includedPaths = Utils.findHeaderFiles(originalTestedProject.getAbsolutePath());

        List<String> soucecodePaths = Utils.findSourcecodeFiles(originalTestedProject.getAbsolutePath());
        includedPaths.addAll(soucecodePaths);

		/*
         * The path of the source code is relative to the unit test project
		 */
        for (String include : includedPaths) {
            String testedProjectPath = Utils.normalizePath(originalTestedProject.getParent());
            String shortenInclude = Utils.normalizePath(include).replace(testedProjectPath, ".");
            includeGoogleTest += "#include " + SpecialCharacter.DOUBLE_QUOTES + shortenInclude
                    + SpecialCharacter.DOUBLE_QUOTES + SpecialCharacter.LINE_BREAK;
        }

        includes.add(includeGoogleTest);
        return includes;
    }

    /**
     * Build source code test for all functions in project
     *
     * @throws Exception
     */
    public void generateUnitTest() {
        if (projectReport != null && originalTestedProject.exists())
            for (ISourcecodeFileReport sourcecode : projectReport.getSourcecodeFiles())
                if (sourcecode.canBeExportToUnitTest()) {
                    String funcTest = "";
                    for (ITestedFunctionReport fun : sourcecode.getTestedFunctionReports())
                        funcTest += fun.generateUnitTest();

					/*
					 * Get new name of source code test
					 */
                    String nameFileTest = sourcecode.getName();
                    String extension = sourcecode.getName().substring(sourcecode.getName().indexOf("."));
                    nameFileTest = nameFileTest.replace(extension, "_test" + extension);

                    String sourcecodeTest = sourcecode.generateUnitTest(originalTestedProject, cloneTestedProject);
                    sourcecodeTest += funcTest;
                    sourcecodeTests.add(new SourceCodeGoogleTest(nameFileTest,
                            generateIncludes(sourcecode, originalTestedProject, cloneTestedProject), sourcecodeTest));
                }
    }

    /**
     * Get name of all unit tests files
     *
     * @throws IOException
     */
    public List<String> getNameUnitTestFiles() throws IOException {
        List<String> output = new ArrayList<>();
        for (ISourceCodeGoogleTest sourceCode : getSourcecodeTest())
            output.add(sourceCode.getFileName());

        return output;
    }

    public List<ISourceCodeGoogleTest> getSourcecodeTest() {
        return sourcecodeTests;
    }

    public void setSourcecodeTest(List<ISourceCodeGoogleTest> sourcecodeTest) {
        sourcecodeTests = sourcecodeTest;
    }

}
