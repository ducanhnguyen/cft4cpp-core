package com.fit.googletest;

import com.fit.gui.testreport.object.ProjectReport;
import com.fit.utils.Utils;

import java.io.File;

/**
 * Represent unit test project. * The structure of the unit test project is
 * described in details here:
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
 * @author ducanhnguyen
 */
public class UnitTestProject {
    public static final String GOOGLE_TEST_FOLDER_NAME = "GoogleTest";
    /**
     * Represent the path of the unit test project
     */
    private File path;

    /**
     * Represent the path of make file
     */
    private File makeFile;

    /**
     * Represent the path of google test
     */
    private File clonedGoogleTest;

    private File originalGoogleTest;
    /**
     * Represent the clone tested project
     */
    private File cloneTestedProject;

    /**
     * Represent the original tested project
     */
    private File originalTestedProject;

    public UnitTestProject() {
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public File getExe() throws Exception {
        if (!makeFile.exists())
            throw new Exception("the path of unit test source code is not set up!");
        else {
            String exeName = Utils.getNameOfExeInDevCppMakefile(makeFile.getAbsolutePath());
            return new File(getPath().getAbsolutePath() + File.separator + exeName);
        }
    }

    public File getClonedGoogleTest() {
        return clonedGoogleTest;
    }

    public void setClonedGoogleTest(File googleTest) {
        clonedGoogleTest = googleTest;
    }

    public File getMakeFile() {
        return makeFile;
    }

    public void setMakeFile(File makeFile) {
        this.makeFile = makeFile;
    }

    public File getCloneTestedProject() {
        return cloneTestedProject;
    }

    public void setCloneTestedProject(File clonedTestedProject) {
        cloneTestedProject = clonedTestedProject;
    }

    public File getOriginalTestedProject() {
        return originalTestedProject;
    }

    public void setOriginalTestedProject(File orgiginalTestedProject) {
        originalTestedProject = orgiginalTestedProject;
    }

    public File getOriginalGoogleTest() {
        return originalGoogleTest;
    }

    public void setOriginalGoogleTest(File originalGoogleTest) {
        this.originalGoogleTest = originalGoogleTest;
    }

    /**
     * Execute unit test project
     *
     * @throws Exception
     */
    public IRunAndExportResultsGTest execute() throws Exception {
        IRunAndExportResultsGTest unitTestProjectRunner = new RunAndExportResultsGTest();
        unitTestProjectRunner.setEnvironment(ProjectReport.getInstance(), this);
        unitTestProjectRunner.exportResults();
        return unitTestProjectRunner;
    }
}
