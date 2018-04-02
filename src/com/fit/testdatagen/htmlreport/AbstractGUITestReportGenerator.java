package com.fit.testdatagen.htmlreport;

import java.util.List;

public abstract class AbstractGUITestReportGenerator implements IAbstractGUITestReportGenerator {

    public AbstractGUITestReportGenerator() {
    }

    protected String refresh() {
        // return "<script>setTimeout(function(){ window.location.reload(1);},
        // 3000);</script>";
        return "";
    }

    protected String scrollToEndOfPage() {
        // return "<script>var scrollInterval = setInterval(function() {
        // document.body.scrollTop =
        // document.body.scrollHeight;},3000);</script>";

        return "";
    }

    protected String hiddenShow() {
        return "<script>var acc = document.getElementsByClassName(\"accordion\");var i;for (i = 0; i < acc.length; i++) {acc[i].onclick = function() {this.nextElementSibling.classList.toggle(\"panel\");}}</script>";
    }

    protected String returnString(String str) {
        if (str == null || str == "")
            return "";
        else
            return str;
    }

    protected String generateCoverage(ICoverage coverage) throws Exception {
        if (coverage == null)
            return "<td>" + "</td>" + "<td>" + "</td>";
        else
            return "<td>" + coverage.getNumStatic() + "</td>" + "<td>" + coverage.getTestpaths().size() + "</td>"
                    + "<td>" + coverage.getTime() + "s</td>" + "<td>" + coverage.getCoverage() + "%</td>";
    }

    @Override
    public abstract void generateFileHTML(List<ITestReport> listDataTestReport, String pathFolder) throws Exception;
}
