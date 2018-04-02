package com.fit.testdatagen.htmlreport;

import interfaces.IGeneration;

import java.util.List;

public interface IAbstractGUITestReportGenerator extends IGeneration {

    void generateFileHTML(List<ITestReport> listDataTestReport, String pathFolder) throws Exception;

}