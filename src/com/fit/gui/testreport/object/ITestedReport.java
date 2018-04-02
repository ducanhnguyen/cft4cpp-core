package com.fit.gui.testreport.object;

/**
 * Represent test report
 *
 * @author ducanhnguyen
 */
public interface ITestedReport {
    default IProjectReport getProjectReport() {
        return ProjectReport.getInstance();
    }
}
