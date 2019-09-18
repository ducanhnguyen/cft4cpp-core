package console;

import java.io.File;
import java.util.List;

import config.IFunctionConfig;
import config.Settingv2;
import instrument.FunctionInstrumentationForStatementvsBranch_Marker;
import testdatagen.AbstractTestdataGeneration;
import testdatagen.TestdataInReport;
import utils.Utils;

public class HtmlExporter {
	private List<ConsoleOutput> output;

	public HtmlExporter(List<ConsoleOutput> output) {
		this.output = output;
	}

	static String tableHeader = toRow(toCell("Name") + toCell("Coverage") + toCell("Bug num") + toCell("Total time")
			+ toCell("Test data execution") + toCell("Compilation") + toCell("Z3 Solver") + toCell("Symbolic execution")
			+ toCell("Normalization time") + toCell("(Macro normalization time)"));

	static String allFunctionsTestReport = "";

	public void exportToHtml(File htmlFile, String methodName) {
		String fullHtml = "";
		String style = "<style> table { font-size: 12px; font-family: arial, sans-serif; border-collapse: collapse; width: 100%; } td, th { border: 1px solid #dddddd; text-align: left; padding: 8px; } tr:nth-child(even) { background-color: #dddddd; } </style>";

		for (ConsoleOutput item : this.output) {
			String row = "";

			long totalTime = (item.getRunningTime() - item.getMacroNormalizationTime());

			row += toCell(methodName)
					+ toCell(item.getBranchCoverge() == 100f ? "100%"
							: "<p style=\"color:red;\">" + item.getBranchCoverge() + "</p>")
					+ toCell(item.getBugs().size() + "")//
					+ toCell(round(1.0f * totalTime / 1000) + " s")//
					+ toCell(round(1.0f * item.getExecutionTime() / 1000) + " s" + " (" + item.getNumOfExecutions()
							+ " executions)")
					+ toCell(round(1.0f * item.getMakeCommandRunningTime() / 1000) + " s" + " ("
							+ item.getMakeCommandRunningNumber() + " makes)")

					+ toCell(round(1.0f * item.getSolverRunningTime() / 1000) + " s" + " (" + item.getNumOfSolverCalls()
							+ " calls)")
					+ toCell(round(1.0f * item.getSymbolicExecutionTime() / 1000) + " s ("
							+ item.getNumOfSymbolicExecutions() + " times, " + item.getNumOfSymbolicStatements()
							+ " stms" + ")")
					+ toCell(round(1.0f * (item.getNormalizationTime() - item.getMacroNormalizationTime()) / 1000)
							+ " s")
					+ toCell(item.getMacroNormalizationTime() / 1000 + " second");

			allFunctionsTestReport += "<table>" + tableHeader + toRow(row) + "</table>" + "<pre>"
					+ item.getFunctionNode().getAST().getRawSignature() + "</pre>";
			allFunctionsTestReport += "</pre>";

			// display test data
			for (TestdataInReport testdata : item.getTestdata()) {
				String testdataStatus = "["
						+ (testdata.outputCompleteTestpath() == false ? "<b><font color='red'> incomplete </font></b>"
								: "<b><font color='blue'> complete </font></b>")
						+ "]&nbsp;";

				String testdataStr = testdata.getValue();
				if (testdata.isGeneratedBySDART())
					testdataStr = "<span style='background-color: green'>" + testdataStr + "</span>";
				else
					testdataStr = "<span >" + testdataStr + "</span>";

				String statementCov = "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "Stm cov="
						+ testdata.getCurrentStatementCodeCoverage();

				String branchCov = "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "Branch cov="
						+ testdata.getCurrentBranchCodeCoverage();

				List<String> stms = testdata.getTestpath()
						.getStandardTestpathByProperty(FunctionInstrumentationForStatementvsBranch_Marker.STATEMENT);
				String testpath = "<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ (stms.size() <= 50 ? stms : "Too long. size=" + stms.size()) + "<br/>";

				if (testdata.isHighlight())
					allFunctionsTestReport += "<span style=\"opacity:1\">" + testdataStatus + testdataStr + statementCov
							+ branchCov + testpath + "</span>";
				else
					allFunctionsTestReport += "<span style=\"opacity:0.5\">" + testdataStatus + testdataStr
							+ statementCov + branchCov + testpath + "</span>";
			}

		}
		// Summary of result
		for (ConsoleOutput item : this.output) {
			AbstractTestdataGeneration.totalNumOfExecution += item.getNumOfExecutions();
			AbstractTestdataGeneration.totalSolverCalls += item.getNumOfSolverCalls();
			AbstractTestdataGeneration.totalSymbolicStatements += item.getNumOfSymbolicStatements();
		}
		String summary = ("The number of SMT-Solver calls = " + AbstractTestdataGeneration.totalSolverCalls + "<br/>")
				+ ("The number of iterations =  " + AbstractTestdataGeneration.totalNumOfExecution + "<br/>")
				+ ("The number of statements performed symbolically: "
						+ AbstractTestdataGeneration.totalSymbolicStatements + "<br/>")
				+ (AbstractTestdataGeneration.numOfVisitedBranches + " visited branches / "
						+ AbstractTestdataGeneration.numOfBranches + " total branches");
		allFunctionsTestReport += "<br/>" + summary + "<br/>";

		// Add configuration
		IFunctionConfig conf = this.output.get(0).getFunctionNode().getFunctionConfig();
		String config1 = "Character bound: [" + conf.getCharacterBound().getLower() + ".."
				+ conf.getCharacterBound().getUpper() + "]";
		String config2 = "Integer bound: [" + conf.getIntegerBound().getLower() + ".."
				+ conf.getIntegerBound().getUpper() + "]";
		String solver = "Solver: " + conf.getSolvingStrategy();
		String iterations = "Max iteration for each loop: " + conf.getMaximumInterationsForEachLoop();
		String sizeofArray = "Max size of array: " + conf.getSizeOfArray();

		// Help
		String testDataDescription = "";
		testDataDescription += "THIS FILE DESCRIBES THE PROCESS OF TEST DATA GENERATION<br/>";
		testDataDescription += "<h1>Test data description</h1></br>";
		testDataDescription += "<span>xxx</span> &nbsp;&nbsp;&nbsp;Test data is generated by DART<br/>";
		testDataDescription += "<span style='background-color: green'>xxx</span> &nbsp;&nbsp;&nbsp;Test data is generated by using SDART<br/>";
		testDataDescription += "<b><font color='red'> incomplete </font></b>&nbsp;&nbsp;&nbsp;The test data causes an exception<br/>";
		testDataDescription += "<b><font color='blue'> complete </font></b>&nbsp;&nbsp;&nbsp;The test data does not cause exceptions<br/>";

		testDataDescription += "<span style=\"opacity:1\">test data</span>&nbsp;&nbsp;&nbsp;This test data increases code coverage (opacity = 1)<br/>";
		testDataDescription += "<span style=\"opacity:0.5\">test data</span>&nbsp;&nbsp;&nbsp;This test data does not increase code coverage (opacity = 0.5)<br/>";
		testDataDescription += "<h1>CONFIGURATION</h1></br>";
		testDataDescription += "<br/>" + config1 + "<br/>" + config2 + "<br/>" + solver + "<br/>" + iterations + "<br/>"
				+ sizeofArray + "<br/>";
		testDataDescription += "Path selection strategy: " + Settingv2.getValue(Settingv2.PATH_SELECTION_STRATEGY)
				+ "<br/>";
		testDataDescription += "<h1>DETAILS</h1>";
		// Others
		String removeConstraints = "Removed constraints size = " + AbstractTestdataGeneration.removedConstraints
				+ "<br/>";
		String removeTestdata = "Removed test data size = " + AbstractTestdataGeneration.removedTestdata + "<br/>";
		allFunctionsTestReport += removeConstraints + removeTestdata;

		fullHtml = "<!DOCTYPE html> <html> <head>" + style + "</head><body>" + testDataDescription
				+ allFunctionsTestReport + "</body></html>";
		Utils.writeContentToFile(fullHtml, htmlFile.getAbsolutePath());

		// OTHER FILES
		// Present changes in visited branches over iterations
		String visitedBranchesChangesOverIterations = "";
		for (Integer[] iterationInfor : AbstractTestdataGeneration.visitedBranchesInfor) {
			Integer numVisitedBranches = iterationInfor[1];
			visitedBranchesChangesOverIterations += toRow(toCell(numVisitedBranches + ""));
		}
		visitedBranchesChangesOverIterations = "<table>" + visitedBranchesChangesOverIterations + "</table>";
		String iterationInforHtml = "<!DOCTYPE html> <html> <head>" + style + "</head><body>"
				+ visitedBranchesChangesOverIterations + "</body></html>";
		Utils.writeContentToFile(iterationInforHtml, htmlFile.getParent() + File.separator + "infor.html");
	}

	static String toCell(String content) {
		return "<td>" + content + "</td>";
	}

	static String toRow(String content) {
		return "<tr>" + content + "</tr>";
	}

	int toUpperRound(float n) {
		return (int) Math.ceil(n);
	}

	float round(float n) {
		String str = n + "";
		int posDot = str.indexOf(".");
		if (posDot == -1)
			return n;
		else {
			if (posDot + 1 == str.length() || posDot + 2 == str.length())
				return n;
			else
				return Float.parseFloat(str.substring(0, posDot + 2));
		}
	}
}
