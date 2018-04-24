package com.fit.testdatagen.htmlreport;

import com.fit.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author DucAnh
 */
public class GUITestReportGenerator2 extends AbstractGUITestReportGenerator {

    public GUITestReportGenerator2() {
    }

    public static void main(String[] args) throws IOException {

    }

    /**
     * Generate &lt; head&gt;...&lt;/head&gt;
     *
     * @return
     */
    protected String generateHeader() {
        String header = "<head>";
        try {
            File cssFile = new File("../ava/src/com/fit/testdatagen/htmlreport/index.css");
            header += "<style>" + Utils.readFileContent(cssFile.getCanonicalPath()) + "</style>";
        } catch (IOException ex) {
            header += "";
        }
        header += "</head>";
        return header;
    }

    /**
     * Generate &lt; table&gt;...&lt;/table&gt;
     *
     * @param listDataTestReport
     * @return
     */
    protected String generateTable(List<ITestReport> listDataTestReport) throws Exception {
        String table = "<table>" + "<tr>" + "<th rowspan=\"2\">Id</th>" + "<th rowspan=\"2\">Sample</th>"
                + "<th rowspan=\"2\">Num.tp</th>" + "<th colspan=\"2\">Branch</th>" + "<th colspan=\"2\">Statement</th>"
                + "</tr>" + "<tr>" + "<th>Cov</th>" + "<th>Test Data</th>" + "<th>Cov</th>" + "<th>Test Data</th>"
                + "</tr>";

        for (ITestReport dataTestReport : listDataTestReport) {
            if (dataTestReport.getSignal() == ITestReport.ERROR)
                table += "<tr class=\"accordion error\">";
            else if (dataTestReport.getSignal() == ITestReport.GOOD)
                table += "<tr class=\"accordion good\">";
            else
                table += "<tr class=\"accordion\">";

            table += "<td class=\"id\">" + dataTestReport.getNo_K() + "</td>" + "<td class=\"functionName\">"
                    + dataTestReport.getFunctionName() + "</td>" + "<td>"
                    + dataTestReport.getTestedFunctionReport().getTotalPossibleTestpath() + "</td>"
                    + generateCoverage(dataTestReport.getBranchCoverage())
                    + generateCoverage(dataTestReport.getStatementCoverage());

            table += "</tr>";

            table += "<tr class=\"panel\">" + getConfiguration(dataTestReport) + "</tr>";
        }
        table += "</table>";
        return table;
    }

    protected String generateLeftSide(List<ITestReport> listDataTestReport) throws Exception {
        String left = "<div id=\"left\">";
        left += generateTable(listDataTestReport);
        left += "</div>";
        return left;
    }

    protected String generateRightSide(List<ITestReport> listDataTestReport) {
        String left = "<div id=\"right\">";
        // left += " <pre>" + dataTestReport.getSourceCode() + "</pre>";
        left += "</div>";
        return left;
    }

    protected String generateContent(List<ITestReport> listDataTestReport) throws Exception {
        return "<!DOCTYPE html><html>" + generateHeader() + "<body>" + refresh() + scrollToEndOfPage()
                + generateLeftSide(listDataTestReport) + generateRightSide(listDataTestReport) + hiddenShow()
                + "</body></html>";
    }

    protected String getConfiguration(ITestReport report) {
        String configuration = "";
        configuration += "<td colspan=\"7\" class=\"config\">";
        configuration += "<pre class=\"sourcecode\">" + report.getSourceCode() + "</pre>";
        configuration += "Range of random character: " + report.getRangeOfRandomCharacter() + ", "
                + "Range of random number: " + report.getRangeOfRandomNumber() + ", " + "Maximum size of array: "
                + report.getMaximumSizeArray() + ", " + "Maximum iterations for each loop: "
                + report.getMaximumIterationsForEachLoop() + "<br/> " + report.getNote();

        return configuration;
    }

    @Override
    public void generateFileHTML(List<ITestReport> listDataTestReport, String pathFolder) throws Exception {
        String content = generateContent(listDataTestReport);
        Utils.writeContentToFile(content, pathFolder);
    }
}
