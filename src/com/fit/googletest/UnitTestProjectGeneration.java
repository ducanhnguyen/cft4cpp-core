package com.fit.googletest;

import com.fit.config.Paths;
import com.fit.gui.testreport.object.IProjectReport;
import com.fit.normalizer.MakefileOfDevCppNormalizer;
import com.fit.parser.makefile.IMakefileGeneration;
import com.fit.parser.makefile.MakefileforGoogleTestProjectGeneration;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.parser.projectparser.RawProjectParser;
import com.fit.utils.Utils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Generate complete unit test project.
 * <p>
 * The structure of the unit test project is described in details here:
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
public class UnitTestProjectGeneration {
    final static Logger logger = Logger.getLogger(UnitTestProjectGeneration.class);
    private UnitTestProject unitTestProject = new UnitTestProject();

    /**
     * The absolute path of the Dev-Cpp folder
     */
    private File devCppFolder;

    private IProjectReport projectReport;

    public static void main(String[] args) throws Exception {
        new ProjectParser(Utils.copy(Paths.SYMBOLIC_EXECUTION_TEST));
        File unitTestProject = new File("C:\\Users\\ducanhnguyen\\Desktop\\xxx");
        File originalGoogleTest = new File("C:\\Users\\ducanhnguyen\\Desktop\\OriginalGoogleTest");
        File originalTestedProject = new File("");
        IProjectReport projectReport = null;

        UnitTestProjectGeneration temp = new UnitTestProjectGeneration();
        temp.setEnvironment(new File("D:/Dev-Cpp"), originalTestedProject, unitTestProject, projectReport,
                originalGoogleTest);
        temp.generateUnitTestProject();
    }

    /**
     * @param devCppFolder          Path of Dev-Cpp
     * @param originalTestedProject The root of the tested project
     * @param unitTestProjectPath   The path of unit test project where you want to put into
     * @param projectReport         project report
     * @throws Exception
     */
    public void setEnvironment(File devCppFolder, File originalTestedProject, File unitTestProjectPath,
                               IProjectReport projectReport, File originalGoogleTest) throws Exception {
        this.devCppFolder = devCppFolder;

        unitTestProject.setOriginalTestedProject(originalTestedProject.getCanonicalFile());
        unitTestProject.setCloneTestedProject(
                new File(unitTestProjectPath.getAbsoluteFile() + File.separator + originalTestedProject.getName()));

        unitTestProject.setPath(unitTestProjectPath);

        unitTestProject.setClonedGoogleTest(
                new File(unitTestProject.getPath() + File.separator + UnitTestProject.GOOGLE_TEST_FOLDER_NAME));
        unitTestProject.setOriginalGoogleTest(originalGoogleTest);

        this.projectReport = projectReport;
    }

    public void generateUnitTestProject() throws Exception {
        /*
		 * Create base google test project
		 */
        if (!new File(unitTestProject.getPath().getAbsolutePath()).exists())
            new File(unitTestProject.getPath().getAbsolutePath()).mkdirs();
        Utils.copyFolder(unitTestProject.getOriginalGoogleTest(), unitTestProject.getClonedGoogleTest());

		/*
		 * Add clone of the tested project into unit test project
		 */
        Utils.copyFolder(new File(unitTestProject.getOriginalTestedProject().getAbsolutePath()),
                new File(unitTestProject.getCloneTestedProject().getAbsolutePath()));
        logger.debug("add tested project done...");

		/*
		 * Delete main of the cloned tested project
		 */
        Utils.deleteMain(new RawProjectParser(new File(unitTestProject.getCloneTestedProject().getAbsolutePath()))
                .getRootTree());
        logger.debug("delete main of the unit test project done...");

		/*
		 * Generate unit tests
		 */
        SourcecodeTestsGeneration sourceCodeGen = generateUnitTestSourcecodes(projectReport, unitTestProject);
        logger.debug("generate unit tests done...");

		/*
		 * 
		 */
        File makefile = generateMakefile(unitTestProject.getPath(), devCppFolder, sourceCodeGen);
        unitTestProject.setMakeFile(makefile);
        logger.debug("generate make file of google test done...");
    }

    /**
     * Generate unit test source code files
     *
     * @param projectReport
     * @return
     */
    private SourcecodeTestsGeneration generateUnitTestSourcecodes(IProjectReport projectReport,
                                                                  UnitTestProject unitTestProject) {
        SourcecodeTestsGeneration sourcecodeTestGen = new SourcecodeTestsGeneration(projectReport, unitTestProject);
        sourcecodeTestGen.generateUnitTest();

        // write to file
        final int CPP_FILE = 0;
        for (ISourceCodeGoogleTest sourceCode : sourcecodeTestGen.getSourcecodeTest()) {
            File sourcecodePath = new File(unitTestProject.getPath() + File.separator + sourceCode.getFileName());
            String completeSourcecode = sourceCode.getIncludes().get(CPP_FILE) + sourceCode.getSourceCode();
            Utils.writeContentToFile(completeSourcecode, sourcecodePath.getAbsolutePath());

            logger.debug("\n\n" + completeSourcecode);
        }
        return sourcecodeTestGen;
    }

    /**
     * Generate make file of the unit test source code project. The make file is
     * put at the same level with Google Test folder
     *
     * @param dest
     * @return
     * @throws IOException
     */
    private File generateMakefile(File dest, File devCppFolder, SourcecodeTestsGeneration sourceCodeGen)
            throws IOException {
        // generate make file
        IMakefileGeneration makeFileGen = new MakefileforGoogleTestProjectGeneration(devCppFolder.getAbsolutePath(),
                sourceCodeGen.getNameUnitTestFiles(), dest.getAbsolutePath());
        makeFileGen.generate();
        final File makefilePath = new File(dest + File.separator + "Makefile.win");
        Utils.writeContentToFile(makeFileGen.getGeneratedSourcecode(), makefilePath.getAbsolutePath());

        // normalize make file
        MakefileOfDevCppNormalizer makefileNorm = new MakefileOfDevCppNormalizer(makefilePath);
        makefileNorm.normalize();
        Utils.writeContentToFile(makefileNorm.getNormalizedSourcecode(), makefilePath.getAbsolutePath());

        return makefilePath;
    }

    public UnitTestProject getUnitTestProject() {
        return unitTestProject;
    }
}
