package console;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import config.AbstractSetting;
import config.IFunctionConfig;
import config.Paths;
import config.Settingv2;
import instrument.FunctionInstrumentationForStatementvsBranch_Marker;
import testdatagen.AbstractTestdataGeneration;
import testdatagen.TestdataInReport;
import utils.Utils;

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
		Paths.START_FROM_COMMANDLINE = false;
		input = analyzeArgs(args);

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

	/**
	 * Example: args = new String[] { Console.LOAD_PROJECT, new
	 * File(Paths.SYMBOLIC_EXECUTION_TEST).getCanonicalPath(),
	 * Console.TESTED_FUNCTIONS, "E:/workspace/java/cft4cpp-core/local/test.txt",
	 * Console.CONFIG, "E:/workspace/java/cft4cpp-core/local/setting.properties",
	 * Console.LOG4J_LEVEL, "debug" };
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// [executable name] -p [project path] -f [function test] -c
		// [setting.properties path] -log4j "debug"
		/**
		 * AUTHOR: PLEASE CONFIGURE HERE
		 */
		String TESTING_PROJET_PATH = "./data-test/demo_aka/Algorithm";
		String TESTING_FUNCTIONS_LIST = "./data-test/demo_aka/Algorithm/test.txt";
		String CONFIGURATION_FILE_PATH = "./data-test/demo_aka/Algorithm/setting.properties";

		/**
		 * AUTHOR: THE END OF CONFIGURATION
		 */
		args = new String[] { Console.LOAD_PROJECT, new File(TESTING_PROJET_PATH).getCanonicalPath(),
				Console.TESTED_FUNCTIONS, new File(TESTING_FUNCTIONS_LIST).getCanonicalPath(), Console.CONFIG,
				new File(CONFIGURATION_FILE_PATH).getCanonicalPath(), Console.LOG4J_LEVEL, "debug" };

		Console console = new Console(args);

		new HtmlExporter(console.getOutput()).exportToHtml(new File(AbstractSetting.getValue(Settingv2.TEST_REPORT)),
				"xxx");
	}

	private ConsoleInput analyzeArgs(String[] args) {
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
				AbstractSetting.settingPath = next;
				input.variableConfigurationFile = new File(next);
				count += 2;
				break;

			case TESTED_FUNCTIONS:
				input.testFunctionsFile = new File(next);
				count += 2;
				break;

			case LOG4J_LEVEL:
				// Note: If there exists log4j configuration outside, this
				// setLevel is
				// useless!

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
				case "off":
					Logger.getRootLogger().setLevel(Level.OFF);
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

}
