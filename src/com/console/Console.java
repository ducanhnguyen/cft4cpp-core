package com.console;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fit.config.AbstractSetting;
import com.fit.config.Paths;
import com.fit.config.Settingv2;
import com.fit.utils.Utils;

/**
 * Run cft4cpp on console
 * <p>
 * Created by ducanhnguyen on 5/26/2017.
 */
public class Console {
	public static final String LOAD_PROJECT = "-p";
	public static final String TESTED_FUNCTIONS = "-f";
	public static final String CONFIG = "-c";
	public static final String LOG4J_LEVEL = "-log4j";
	final static Logger logger = Logger.getLogger(Console.class);
	private ConsoleInput input = new ConsoleInput();

	public Console(String[] args) {
		Paths.START_FROM_COMMANDLINE = true;
		// if (logger == null || Paths.START_FROM_COMMANDLINE)
		// BasicConfigurator.configure();

		if (!new File(AbstractSetting.settingPath).exists())
			Settingv2.create();

		input = constructInput(args);

		logger.info("Check the correctness of options");
		try {
			input.checkVariablesConfiguration();
			logger.info("OK. Start generating test data");
			try {
				input.findTestdata();
			} catch (Exception e) {
				logger.error("Error in generating test data");
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		// args = new String[] { Console.LOAD_PROJECT, new
		// File(Paths.COMBINED_STATIC_AND_DYNAMIC_GENERATION).getCanonicalPath(),
		// Console.TESTED_FUNCTIONS, "D:/ava/local/test.txt", Console.CONFIG,
		// "D:/ava/local/setting.properties", Console.LOG4J_LEVEL, "debug" };
		Console console = new Console(args);

		// Display output
		List<ConsoleOutput> outputList = console.getOutput();

		for (ConsoleOutput outputItem : outputList) {
			logger.info("Original function");
			logger.info(outputItem.getFunctionNode().getAST().getRawSignature());
			logger.info(
					"Total time = " + outputItem.getRunningTime() + "ms (" + outputItem.getRunningTime() / 1000 + "s)");
			logger.info("Solver running time = " + outputItem.getSolverRunningTime() + "ms ("
					+ outputItem.getSolverRunningTime() / outputItem.getRunningTime() * 100 + "%)");
			logger.info("Make file running time = " + outputItem.getMakeCommandRunningTime() + "ms ("
					+ outputItem.getMakeCommandRunningTime() / outputItem.getRunningTime() * 100 + "%)");
			logger.info("Num of effective solver calls = "
					+ (outputItem.getNumOfSolverCalls() - outputItem.getNumOfSolverCallsbutCannotSolve()) + "/"
					+ outputItem.getNumOfSolverCalls() + " times (Num of error Solver calls = "
					+ outputItem.getNumOfSolverCallsbutCannotSolve() + "/" + outputItem.getNumOfSolverCalls() + ")");
			logger.info("Num of no change to coverage iteration = " + outputItem.getNumOfNoChangeToCoverage());
			logger.info("Num of symbolic executions = " + outputItem.getNumOfSymbolicExecutions() + " times");
			logger.info("Num of symbolic statements = " + outputItem.getNumOfSymbolicStatements() + " times");
			logger.info("Num of executions = " + outputItem.getNumOfExecutions() + " times");
			logger.info("Reached coverage = " + outputItem.getCoverge() + "%)");
		}
	}

	private ConsoleInput constructInput(String[] args) {
		ConsoleInput input = new ConsoleInput();
		int count = 0;
		while (count < args.length - 1) {
			String current = args[count];
			String next = args[count + 1];

			switch (current) {
			case LOAD_PROJECT:
				input.projectFile = new File(next);
				count += 2;
				break;

			case CONFIG:
				input.variableConfigurationFile = new File(next);
				count += 2;
				break;

			case TESTED_FUNCTIONS:
				input.testFunctionsFile = new File(next);
				count += 2;
				break;

			case LOG4J_LEVEL:
				// A log request of level p in a logger with level
				// q is enabled if p >= q. This rule is at the
				// heart of log4j. It assumes that levels are
				// ordered. For the standard levels, we have ALL
				// < DEBUG < INFO < WARN < ERROR < FATAL < OFF.
				switch (next.toLowerCase()) {
				case "all":
					Logger.getRootLogger().setLevel(Level.ALL);
					break;
				case "debug":
					Logger.getRootLogger().setLevel(Level.DEBUG);
					break;
				case "error":
					Logger.getRootLogger().setLevel(Level.ERROR);
					break;
				case "info":
					Logger.getRootLogger().setLevel(Level.INFO);
					break;
				case "trace":
					Logger.getRootLogger().setLevel(Level.TRACE);
					break;
				case "warn":
					Logger.getRootLogger().setLevel(Level.WARN);
					break;
				default:
					break;
				}
				count += 2;
				break;
			default:
				count++;
				break;
			}
		}
		return input;
	}

	public ConsoleInput getInput() {
		return input;
	}

	/**
	 * Get output in console. Each test data generation result of each function is
	 * stored in an element in the given list.
	 *
	 * @return
	 */
	public List<ConsoleOutput> getOutput() {
		return input.getOutput();
	}

	static String tableHeader = toRow(toCell("Name") + toCell("Coverage") + toCell("Bug num") + toCell("Total time")
			+ toCell("Test data execution") + toCell("Compilation") + toCell("Z3 Solver") + toCell("Symbolic execution")
			+ toCell("Normalization time") + toCell("(Macro normalization time)"));

	static String allFunctionsTestReport = "";

	public void exportToHtml(File htmlFile, String methodName) {
		String fullHtml = "";
		String style = "<style> table { font-size: 12px; font-family: arial, sans-serif; border-collapse: collapse; width: 100%; } td, th { border: 1px solid #dddddd; text-align: left; padding: 8px; } tr:nth-child(even) { background-color: #dddddd; } </style>";

		for (ConsoleOutput item : this.getOutput()) {
			String row = "";

			long totalTime = (item.getRunningTime() - ConsoleOutput.getMacroNormalizationTime());
			// row += toCell(methodName)
			// + toCell(item.getCoverge() == 100f ? "100%"
			// : "<p style=\"color:red;\">" + item.getCoverge() + "</p>")
			// + toCell(toUpperRound(1.0f * totalTime / 1000) + " s")
			// + toCell(toUpperRound(100f * item.getExecutionTime() / totalTime) + "%")
			// + toCell(toUpperRound(100f * item.getMakeCommandRunningTime() / totalTime) +
			// "%")
			// + toCell(toUpperRound(100f
			// * (item.getNormalizationTime() - ConsoleOutput.getMacroNormalizationTime()) /
			// totalTime)
			// + "%")
			// + toCell(toUpperRound(100f * item.getSolverRunningTime() / totalTime) + "%")
			// + toCell(toUpperRound(100f * item.getSymbolicExecutionTime() / totalTime) +
			// "%")
			// + toCell(item.getNumOfSymbolicExecutions() + " times")
			// + toCell(item.getNumOfSymbolicStatements() + " stms")
			// + toCell(item.getMakeCommandRunningNumber() + " makes/ " +
			// item.getNumOfExecutions() + " executions"
			// + toCell(ConsoleOutput.getMacroNormalizationTime() / 1000 + " second"));

			row += toCell(methodName)
					+ toCell(item.getCoverge() == 100f ? "100%"
							: "<p style=\"color:red;\">" + item.getCoverge() + "</p>")
					+ toCell(ConsoleOutput.getBugs().size() + "")//
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
					+ toCell(round(
							1.0f * (item.getNormalizationTime() - ConsoleOutput.getMacroNormalizationTime()) / 1000)
							+ " s")
					+ toCell(ConsoleOutput.getMacroNormalizationTime() / 1000 + " second");

			allFunctionsTestReport += "<table>" + tableHeader + toRow(row) + "</table>" + "<pre>"
					+ item.getFunctionNode().getAST().getRawSignature() + "</pre>";
		}

		fullHtml = "<!DOCTYPE html> <html> <head>" + style + "</head><body>" + allFunctionsTestReport
				+ "</body></html>";
		Utils.writeContentToFile(fullHtml, htmlFile.getAbsolutePath());
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
