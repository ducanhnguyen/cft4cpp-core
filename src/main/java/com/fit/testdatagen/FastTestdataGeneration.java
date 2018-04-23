package com.fit.testdatagen;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.fit.cfg.CFGGenerationSubCondition;
import com.fit.cfg.CFGGenerationforBranchvsStatementCoverage;
import com.fit.cfg.ICFG;
import com.fit.cfg.ICFGGeneration;
import com.fit.cfg.object.ConditionCfgNode;
import com.fit.cfg.testpath.IPartialTestpath;
import com.fit.cfg.testpath.IStaticSolutionGeneration;
import com.fit.cfg.testpath.ITestpathInCFG;
import com.fit.cfg.testpath.PartialTestpaths;
import com.fit.cfg.testpath.StaticSolutionGeneration;
import com.fit.config.Bound;
import com.fit.config.FunctionConfig;
import com.fit.config.IFunctionConfig;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.gui.testreport.object.ITestedFunctionReport;
import com.fit.gui.testreport.object.ProjectReport;
import com.fit.gui.testreport.object.TestpathReport;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdata.object.TestpathString_Marker;
import com.fit.testdatagen.coverage.CFGUpdater_Mark;
import com.fit.testdatagen.fast.random.BasicTypeRandom;
import com.fit.testdatagen.se.IPathConstraints;
import com.fit.testdatagen.se.ISymbolicExecution;
import com.fit.testdatagen.se.Parameter;
import com.fit.testdatagen.se.PathConstraints;
import com.fit.testdatagen.se.SymbolicExecution;
import com.fit.testdatagen.se.memory.ISymbolicVariable;
import com.fit.testdatagen.testdatainit.VariableTypes;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.IVariableNode;
import com.fit.tree.object.StructureNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;
import com.ibm.icu.util.Calendar;

import testdatageneration.Bug;
import testdatageneration.TestdataInReport;

/**
 * 
 * Use fast function execution (compile the testing project once)
 * 
 * @author DucAnh
 */
public class FastTestdataGeneration extends MarsTestdataGeneration2 {
	final static Logger logger = Logger.getLogger(FastTestdataGeneration.class);

	public static void main(String[] args) throws Exception {
		File clone = Utils.copy(Paths.SYMBOLIC_EXECUTION_TEST);
		ProjectParser parser = new ProjectParser(clone);
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "Tritype(int,int,int)").get(0);

		FunctionConfig functionConfig = new FunctionConfig();
		functionConfig.setCharacterBound(new Bound(30, 120));
		functionConfig.setIntegerBound(new Bound(10, 200));
		functionConfig.setSizeOfArray(5);
		functionConfig.setMaximumInterationsForEachLoop(3);
		functionConfig.setSolvingStrategy(ISettingv2.SUPPORT_SOLVING_STRATEGIES[0]);
		function.setFunctionConfig(functionConfig);

		ProjectReport.getInstance().addFunction(function);

		FastTestdataGeneration gen = new FastTestdataGeneration(function,
				ProjectReport.getInstance().getSourcecodeFiles().get(0).getTestedFunctionReports().get(0));
		gen.generateTestdata();
	}

	public FastTestdataGeneration(IFunctionNode function, ITestedFunctionReport iTestedFunctionReport) {
		super(function, iTestedFunctionReport);
	}

	protected void findPathOfExe(IFunctionNode originalFunction) throws Exception {
		// Find the path of .exe
		getExePath(Utils.getRoot(originalFunction));
		if (new File(Paths.CURRENT_PROJECT.EXE_PATH).exists())
			new File(Paths.CURRENT_PROJECT.EXE_PATH).delete();
	}

	/**
	 * Initialize data at random
	 * 
	 * @return
	 */
	protected String initializeTestdataAtRandom() {
		String testdata = ""; // Ex: a=1;b=2
		Map<String, String> initialization = constructRandomInput(fn.getArguments(), fn.getFunctionConfig(), "");
		for (String key : initialization.keySet())
			testdata += key + "=" + initialization.get(key) + ";";
		return testdata;
	}

	private void DART_IMPROVEMENT(IFunctionNode originalFunction) throws Exception {
		// Config: no limit loop, no limit recursive
		// Ignore execution when there exists duplicated constraints; duplicate test
		// data
		// If code coverage is less than 100%, perform static testing
		getExePath(originalFunction);
		ICFG normalizedCfg = generateNormalizedCFG(originalFunction);

		if (normalizedCfg != null) {
			// IMPROVEMENT HERE - BEGIN
			boolean improvedTestdata = false;
			List<IPartialTestpath> analyzedTestpaths = new ArrayList<>();
			List<String> existingTestdata = new ArrayList<>();
			List<IPathConstraints> existingConstraints = new ArrayList<>();
			// IMPROVEMENT HERE - END

			String testdata = "";
			int iteration_in_one_depth = 0;
			int depth = 0;
			float currentCoverage = 0.0f;

			logger.info("STRATEGY: DART_IMPROVEMENT");

			float tmp_currentStatementCoverage = 0.0f;
			float tmp_currentBranchCoverage = 0.0f;

			float tmp_previous_currentStatementCoverage = 0.0f;
			float tmp_previous_currentBranchCoverage = 0.0f;

			while (depth < DEPTH && currentCoverage < 100f) {
				logger.info("\n\n\n" + "====================<b>DEPTH " + depth
						+ "</b>==========================================================");
				while (iteration_in_one_depth < MAX_ITERATIONS_IN_ONE_DEPTH && currentCoverage < 100f) {
					logger.info("\n\n\n" + "---------------------ITERATION " + iteration_in_one_depth
							+ "-------------------------");
					iteration_in_one_depth++;
					/**
					 * STEP 1. INITIALIZE TEST DATA AT RANDOM
					 */
					boolean tmp_isGenerateRandomly = false;
					if (testdata.length() == 0) {
						testdata = initializeTestdataAtRandom();
						logger.info("Generate a random test data: <b>" + testdata + "</b>");
						tmp_isGenerateRandomly = true;
					} else
						tmp_isGenerateRandomly = false;

					// IMPROVEMENT HERE - BEGIN
					if (existingTestdata.contains(testdata)) {
						testdata = initializeTestdataAtRandom();
						logger.info("Duplicate. Generate a random test data: <b>" + testdata + "</b>");
						tmp_isGenerateRandomly = true;
						improvedTestdata = true;
					} else
						existingTestdata.add(testdata);
					// IMPROVEMENT HERE - END

					/**
					 * STEP 2. EXECUTE TEST DATA TO GET TEST PATH
					 */
					ITestdataExecution testdataExecution = executeTestdata(originalFunction, testdata);

					tmp_previous_currentBranchCoverage = tmp_currentBranchCoverage;
					tmp_previous_currentStatementCoverage = tmp_currentStatementCoverage;

					String fullTestdata = Utils.readFileContent(Paths.CURRENT_PROJECT.TESTDATA_INPUT_FILE_PATH)
							.replace("\n", "; ");
					if (testdataExecution != null) {
						// Update CFG
						CFGUpdater_Mark cfgUpdater = new CFGUpdater_Mark(testdataExecution.getEncodedTestpath(),
								normalizedCfg);

						currentNumOfVisitedBranches -= normalizedCfg.getVisitedBranches().size();

						// logger.debug("Previous Visited Nodes: " +
						// cfgUpdater.getPreviousVisitedNodes());
						// logger.debug("Previous Visited Branches: " +
						// cfgUpdater.getPreviousVisitedBranches());
						cfgUpdater.updateVisitedNodes();

						tmp_currentStatementCoverage = cfgUpdater.getCfg().computeStatementCoverage() * 100;
						logger.info("Current statement coverage = " + tmp_currentStatementCoverage);

						tmp_currentBranchCoverage = cfgUpdater.getCfg().computeBranchCoverage() * 100;
						logger.info("Current branch coverage = " + tmp_currentBranchCoverage);

						currentCoverage = normalizedCfg.computeBranchCoverage() * 100;
						// Add to test report
						fnReport.addTestpath(new TestpathReport(testdataExecution.getEncodedTestpath(),
								testdataExecution.getEncodedTestpath().getEncodedTestpath(), null, null, null, "-",
								originalFunction));

						currentNumOfVisitedBranches += normalizedCfg.getVisitedBranches().size();
						AbstractTestdataGeneration.visitedBranchesInfor
								.add(new Integer[] { ++tmp_iterations, currentNumOfVisitedBranches });

						// CASE 1. The latest test path is uncomplete (there arises an error when the
						// test data execute)
						if (!cfgUpdater.isCompleteTestpath()) {
							logger.info("The testpath is uncomplete! We still update CFG");
							logger.info("Found a bug. Uncomplete test path");

							AbstractTestdataGeneration.bugs.add(
									new Bug(fullTestdata, testdataExecution.getEncodedTestpath(), originalFunction));

							AbstractTestdataGeneration.testdata.add(new TestdataInReport(fullTestdata,
									testdataExecution.getEncodedTestpath(), false, tmp_currentStatementCoverage,
									tmp_currentBranchCoverage,
									tmp_currentBranchCoverage > tmp_previous_currentBranchCoverage ? true
											: false || tmp_currentStatementCoverage > tmp_previous_currentStatementCoverage
													? true
													: false,
									tmp_isGenerateRandomly, improvedTestdata)); // MODIFIED
							improvedTestdata = false; // ADDED
						}
						// CASE 2. No errors during test data execution
						else {
							AbstractTestdataGeneration.testdata.add(new TestdataInReport(fullTestdata,
									testdataExecution.getEncodedTestpath(), true, tmp_currentStatementCoverage,
									tmp_currentBranchCoverage,
									tmp_currentBranchCoverage > tmp_previous_currentBranchCoverage ? true
											: false || tmp_currentStatementCoverage > tmp_previous_currentStatementCoverage
													? true
													: false,
									tmp_isGenerateRandomly, improvedTestdata));// MODIFIED
							improvedTestdata = false; // ADDED

							switch (originalFunction.getFunctionConfig().getTypeofCoverage()) {

							case IFunctionConfig.BRANCH_COVERAGE:

								ITestpathInCFG executedTestpath = cfgUpdater.getUpdatedCFGNodes();

								/**
								 * STEP 3. GET PATH CONSTRAINTS
								 */
								if (currentCoverage < 100f && executedTestpath != null) {
									Parameter paramaters = new Parameter();
									for (INode n : ((FunctionNode) originalFunction).getArguments())
										paramaters.add(n);
									for (INode n : ((FunctionNode) originalFunction).getReducedExternalVariables())
										paramaters.add(n);

									logger.info("Performing symbolic execution on this test path");
									ISymbolicExecution se = new SymbolicExecution(executedTestpath, paramaters,
											originalFunction);

									logger.debug("Done. Constraints: \n" + se.getConstraints().toString()
											.replace(ISymbolicVariable.PREFIX_SYMBOLIC_VALUE, "")
											.replace(
													ISymbolicVariable.SEPARATOR_BETWEEN_STRUCTURE_NAME_AND_ITS_ATTRIBUTES,
													".")
											.replace(ISymbolicVariable.ARRAY_CLOSING, "]")
											.replace(ISymbolicVariable.ARRAY_OPENING, "["));

									/**
									 * STEP 4. NEGATE PATH CONSTRAINTS
									 */

									logger.info("NEGATE PATH CONSTRAINTS");

									boolean isFoundSolution = false;
									boolean canNegateCondition = true;

									Set<Integer> negatedIndexs = new HashSet<>();
									while (!isFoundSolution && canNegateCondition && se.getConstraints().size() >= 1) {
										// Choose a random constraint to negate
										IPathConstraints negatedConstraints = null;
										boolean foundNegatedCondition = false;
										do {
											int negatedConstraintsIndexCandidate = new Random()
													.nextInt(se.getConstraints().size());

											if (((PathConstraints) se.getConstraints())
													.get(negatedConstraintsIndexCandidate).getCfgNode() == null) {
												negatedIndexs.add(new Integer(negatedConstraintsIndexCandidate));

											} else if (!negatedIndexs.contains(negatedConstraintsIndexCandidate)) {
												negatedIndexs.add(new Integer(negatedConstraintsIndexCandidate));

												// If the negated condition is not visited in all of two branches
												negatedConstraints = se.getConstraints()
														.negateConditionAt(negatedConstraintsIndexCandidate);
												ConditionCfgNode negatedCfgNode = (ConditionCfgNode) ((PathConstraints) negatedConstraints)
														.get(negatedConstraintsIndexCandidate).getCfgNode();
												if (!negatedCfgNode.isVisitedFalseBranch()
														|| !negatedCfgNode.isVisitedTrueBranch()) {
													foundNegatedCondition = true;
												}
											}
										} while (!foundNegatedCondition
												&& negatedIndexs.size() < se.getConstraints().size());

										// IMPROVEMENT HERE - BEGIN
										if (foundNegatedCondition)
											if (!existingConstraints.contains(negatedConstraints))
												existingConstraints.add(negatedConstraints);
											else {
												foundNegatedCondition = false;
											}
										// IMPROVEMENT HERE - END
										if (foundNegatedCondition) {
											// Solve path constraints
											logger.debug("Negated Constraints: \n" + negatedConstraints.toString()
													.replace(ISymbolicVariable.PREFIX_SYMBOLIC_VALUE, "")
													.replace(
															ISymbolicVariable.SEPARATOR_BETWEEN_STRUCTURE_NAME_AND_ITS_ATTRIBUTES,
															".")
													.replace(ISymbolicVariable.ARRAY_CLOSING, "]")
													.replace(ISymbolicVariable.ARRAY_OPENING, "["));
											testdata = new StaticSolutionGeneration().solve(negatedConstraints,
													originalFunction);

											if (testdata.equals(IStaticSolutionGeneration.NO_SOLUTION)) {
												logger.info("No solution. Continue negating.");
												isFoundSolution = false;
											} else if (testdata.equals(IStaticSolutionGeneration.EVERY_SOLUTION)) {
												isFoundSolution = true;
												// Just pick a random test data
												testdata = initializeTestdataAtRandom();
												logger.info("May solution. Choose a solution. Next test data = <b>"
														+ testdata + "</b>");
											} else {
												isFoundSolution = true;
												logger.info(
														"Found a solution. Next test data = <b>" + testdata + "</b>");
											}
										} else {
											logger.info(
													"Can not negate any condition. We start generating test data at random");
											canNegateCondition = false;
											testdata = "";
											break;
										}
									}
								}
								break;

							case IFunctionConfig.STATEMENT_COVERAGE:
							case IFunctionConfig.SUBCONDITION:
								throw new Exception("Dont code this kind of code coverage");
							}

						}
					} else {
						logger.debug("Current test data causes errors.");
						AbstractTestdataGeneration.testdata.add(new TestdataInReport(fullTestdata,
								new TestpathString_Marker(), false, tmp_currentStatementCoverage,
								tmp_currentBranchCoverage, false, tmp_isGenerateRandomly, improvedTestdata));// MODIFIED
						improvedTestdata = false; // ADDED
					}
				}
				depth++;
			}

			// IMPROVEMENT HERE-BEGIN
			// Perform static testing to generate new test data covering unvisited branches
			if (normalizedCfg.computeBranchCoverage() < 100f) {
				logger.debug("Use static analysis to detect a directed test data");
				PartialTestpaths uncoveredTestpaths = normalizedCfg
						.getPartialTestpathcontainingUnCoveredBranches_Strategy1();

				while (uncoveredTestpaths.size() >= 1 && normalizedCfg.computeBranchCoverage() < 100f) {
					IPartialTestpath uncoveredTestpath = uncoveredTestpaths.get(0);

					if (analyzedTestpaths.contains(uncoveredTestpath)) {
						uncoveredTestpaths.remove(0);
						continue;
					} else {
						analyzedTestpaths.add(uncoveredTestpath);
						uncoveredTestpaths.remove(0);

						Parameter paramaters = new Parameter();
						for (INode n : ((FunctionNode) originalFunction).getArguments())
							paramaters.add(n);
						for (INode n : ((FunctionNode) originalFunction).getReducedExternalVariables())
							paramaters.add(n);

						logger.info("Performing symbolic execution on this test path");
						ISymbolicExecution se = new SymbolicExecution(uncoveredTestpath, paramaters, originalFunction);

						if (!existingConstraints.contains(se.getConstraints())) {
							existingConstraints.add(se.getConstraints());

							String currentTestdata = new StaticSolutionGeneration().solve(se.getConstraints(),
									originalFunction);

							if (currentTestdata.equals(IStaticSolutionGeneration.NO_SOLUTION)) {
								logger.info("No solution. Seek another unvisited test path.");
								testdata = "";
								improvedTestdata = false;
							} else if (currentTestdata.equals(IStaticSolutionGeneration.EVERY_SOLUTION)) {
								// Just pick a random test data
								currentTestdata = initializeTestdataAtRandom();
								logger.info("May solution. Choose a solution. Next test data = <b>" + currentTestdata
										+ "</b>");
								testdata = currentTestdata;
								improvedTestdata = true;
							} else {
								logger.info("Found a solution. Next test data = <b>" + currentTestdata + "</b>");
								testdata = currentTestdata;
								improvedTestdata = true;
							}

							// Execute new test data
							if (testdata.length() > 0) {
								if (!existingTestdata.contains(testdata)) {
									existingTestdata.add(testdata);

									ITestdataExecution testdataExecution = executeTestdata(originalFunction, testdata);

									tmp_previous_currentBranchCoverage = tmp_currentBranchCoverage;
									tmp_previous_currentStatementCoverage = tmp_currentStatementCoverage;

									String fullTestdata = Utils
											.readFileContent(Paths.CURRENT_PROJECT.TESTDATA_INPUT_FILE_PATH)
											.replace("\n", "; ");
									if (testdataExecution != null) {
										// Update CFG
										CFGUpdater_Mark cfgUpdater = new CFGUpdater_Mark(
												testdataExecution.getEncodedTestpath(), normalizedCfg);

										currentNumOfVisitedBranches -= normalizedCfg.getVisitedBranches().size();

										cfgUpdater.updateVisitedNodes();

										tmp_currentStatementCoverage = cfgUpdater.getCfg().computeStatementCoverage()
												* 100;
										logger.info("Current statement coverage = " + tmp_currentStatementCoverage);

										tmp_currentBranchCoverage = cfgUpdater.getCfg().computeBranchCoverage() * 100;
										logger.info("Current branch coverage = " + tmp_currentBranchCoverage);

										currentCoverage = normalizedCfg.computeBranchCoverage() * 100;
										// Add to test report
										fnReport.addTestpath(new TestpathReport(testdataExecution.getEncodedTestpath(),
												testdataExecution.getEncodedTestpath().getEncodedTestpath(), null, null,
												null, "-", originalFunction));

										currentNumOfVisitedBranches += normalizedCfg.getVisitedBranches().size();
										AbstractTestdataGeneration.visitedBranchesInfor
												.add(new Integer[] { ++tmp_iterations, currentNumOfVisitedBranches });

										// CASE 1. The latest test path is uncomplete (there arises an error when the
										// test data execute)
										if (!cfgUpdater.isCompleteTestpath()) {
											logger.info("The testpath is uncomplete! We still update CFG");
											logger.info("Found a bug. Uncomplete test path");

											AbstractTestdataGeneration.bugs.add(new Bug(fullTestdata,
													testdataExecution.getEncodedTestpath(), originalFunction));

											AbstractTestdataGeneration.testdata.add(new TestdataInReport(fullTestdata,
													testdataExecution.getEncodedTestpath(), false,
													tmp_currentStatementCoverage, tmp_currentBranchCoverage,
													tmp_currentBranchCoverage > tmp_previous_currentBranchCoverage
															? true
															: false || tmp_currentStatementCoverage > tmp_previous_currentStatementCoverage
																	? true
																	: false,
													false, true));

											testdata = "";
										}
										// CASE 2. No errors during test data execution
										else {
											AbstractTestdataGeneration.testdata.add(new TestdataInReport(fullTestdata,
													testdataExecution.getEncodedTestpath(), true,
													tmp_currentStatementCoverage, tmp_currentBranchCoverage,
													tmp_currentBranchCoverage > tmp_previous_currentBranchCoverage
															? true
															: false || tmp_currentStatementCoverage > tmp_previous_currentStatementCoverage
																	? true
																	: false,
													false, true));
										}
									}
								}
							}
						}
					}
				}
			}
			AbstractTestdataGeneration.removedConstraints += existingConstraints.size();
			AbstractTestdataGeneration.removedTestdata += existingTestdata.size();
			// IMPROVEMENT HERE-END

			AbstractTestdataGeneration.numOfBranches += normalizedCfg.getUnvisitedBranches().size()
					+ normalizedCfg.getVisitedBranches().size();
			AbstractTestdataGeneration.numOfVisitedBranches += normalizedCfg.getVisitedBranches().size();
		}
	}

	/**
	 * Step 1. Generate test data at random <br/>
	 * Step 2. Execute test data to get test path. If error occurs, back to Step 1,
	 * else Step 2 <br/>
	 * Step 3. Get constraints<br/>
	 * Step 4. Negate constraints (from the last constraint). If exist, back to Step
	 * 2, else back to step 1<br/>
	 * 
	 * @param originalFunction
	 * @throws Exception
	 */
	private void DART(IFunctionNode originalFunction) throws Exception {
		// Config: no limit loop, no limit recursive
		getExePath(originalFunction);
		ICFG normalizedCfg = generateNormalizedCFG(originalFunction);

		if (normalizedCfg != null) {

			String testdata = "";
			int iteration_in_one_depth = 0;
			int depth = 0;
			float currentCoverage = 0.0f;

			logger.info("STRATEGY: DART");

			float tmp_currentStatementCoverage = 0.0f;
			float tmp_currentBranchCoverage = 0.0f;

			float tmp_previous_currentStatementCoverage = 0.0f;
			float tmp_previous_currentBranchCoverage = 0.0f;

			while (depth < DEPTH && currentCoverage < 100f) {
				logger.info("\n\n\n" + "====================<b>DEPTH " + depth
						+ "</b>==========================================================");
				while (iteration_in_one_depth < MAX_ITERATIONS_IN_ONE_DEPTH && currentCoverage < 100f) {
					logger.info("\n\n\n" + "---------------------ITERATION " + iteration_in_one_depth
							+ "-------------------------");
					iteration_in_one_depth++;
					/**
					 * STEP 1. INITIALIZE TEST DATA AT RANDOM
					 */
					boolean tmp_isGenerateRandomly = false;
					if (testdata.length() == 0) {
						testdata = initializeTestdataAtRandom();
						logger.info("Generate a random test data: <b>" + testdata + "</b>");
						tmp_isGenerateRandomly = true;
					} else
						tmp_isGenerateRandomly = false;

					/**
					 * STEP 2. EXECUTE TEST DATA TO GET TEST PATH
					 */
					ITestdataExecution testdataExecution = executeTestdata(originalFunction, testdata);

					tmp_previous_currentBranchCoverage = tmp_currentBranchCoverage;
					tmp_previous_currentStatementCoverage = tmp_currentStatementCoverage;

					String fullTestdata = Utils.readFileContent(Paths.CURRENT_PROJECT.TESTDATA_INPUT_FILE_PATH)
							.replace("\n", "; ");
					if (testdataExecution != null) {
						// Update CFG
						CFGUpdater_Mark cfgUpdater = new CFGUpdater_Mark(testdataExecution.getEncodedTestpath(),
								normalizedCfg);

						currentNumOfVisitedBranches -= normalizedCfg.getVisitedBranches().size();

						// logger.debug("Previous Visited Nodes: " +
						// cfgUpdater.getPreviousVisitedNodes());
						// logger.debug("Previous Visited Branches: " +
						// cfgUpdater.getPreviousVisitedBranches());
						cfgUpdater.updateVisitedNodes();

						tmp_currentStatementCoverage = cfgUpdater.getCfg().computeStatementCoverage() * 100;
						logger.info("Current statement coverage = " + tmp_currentStatementCoverage);

						tmp_currentBranchCoverage = cfgUpdater.getCfg().computeBranchCoverage() * 100;
						logger.info("Current branch coverage = " + tmp_currentBranchCoverage);

						currentCoverage = normalizedCfg.computeBranchCoverage() * 100;
						// Add to test report
						fnReport.addTestpath(new TestpathReport(testdataExecution.getEncodedTestpath(),
								testdataExecution.getEncodedTestpath().getEncodedTestpath(), null, null, null, "-",
								originalFunction));

						currentNumOfVisitedBranches += normalizedCfg.getVisitedBranches().size();
						AbstractTestdataGeneration.visitedBranchesInfor
								.add(new Integer[] { ++tmp_iterations, currentNumOfVisitedBranches });

						// CASE 1. The latest test path is uncomplete (there arises an error when the
						// test data execute)
						if (!cfgUpdater.isCompleteTestpath()) {
							logger.info("The testpath is uncomplete! We still update CFG");
							logger.info("Found a bug. Uncomplete test path");

							AbstractTestdataGeneration.bugs.add(
									new Bug(fullTestdata, testdataExecution.getEncodedTestpath(), originalFunction));

							AbstractTestdataGeneration.testdata.add(new TestdataInReport(fullTestdata,
									testdataExecution.getEncodedTestpath(), false, tmp_currentStatementCoverage,
									tmp_currentBranchCoverage,
									tmp_currentBranchCoverage > tmp_previous_currentBranchCoverage ? true
											: false || tmp_currentStatementCoverage > tmp_previous_currentStatementCoverage
													? true
													: false,
									tmp_isGenerateRandomly, false));

							testdata = "";
						}
						// CASE 2. No errors during test data execution
						else {
							AbstractTestdataGeneration.testdata.add(new TestdataInReport(fullTestdata,
									testdataExecution.getEncodedTestpath(), true, tmp_currentStatementCoverage,
									tmp_currentBranchCoverage,
									tmp_currentBranchCoverage > tmp_previous_currentBranchCoverage ? true
											: false || tmp_currentStatementCoverage > tmp_previous_currentStatementCoverage
													? true
													: false,
									tmp_isGenerateRandomly, false));

							switch (originalFunction.getFunctionConfig().getTypeofCoverage()) {

							case IFunctionConfig.BRANCH_COVERAGE:

								ITestpathInCFG executedTestpath = cfgUpdater.getUpdatedCFGNodes();

								/**
								 * STEP 3. GET PATH CONSTRAINTS
								 */
								if (currentCoverage < 100f && executedTestpath != null) {
									Parameter paramaters = new Parameter();
									for (INode n : ((FunctionNode) originalFunction).getArguments())
										paramaters.add(n);
									for (INode n : ((FunctionNode) originalFunction).getReducedExternalVariables())
										paramaters.add(n);

									logger.info("Performing symbolic execution on this test path");
									ISymbolicExecution se = new SymbolicExecution(executedTestpath, paramaters,
											originalFunction);

									logger.debug("Done. Constraints: \n" + se.getConstraints().toString()
											.replace(ISymbolicVariable.PREFIX_SYMBOLIC_VALUE, "")
											.replace(
													ISymbolicVariable.SEPARATOR_BETWEEN_STRUCTURE_NAME_AND_ITS_ATTRIBUTES,
													".")
											.replace(ISymbolicVariable.ARRAY_CLOSING, "]")
											.replace(ISymbolicVariable.ARRAY_OPENING, "["));

									/**
									 * STEP 4. NEGATE PATH CONSTRAINTS
									 */

									logger.info("NEGATE PATH CONSTRAINTS");

									boolean isFoundSolution = false;
									boolean canNegateCondition = true;

									Set<Integer> negatedIndexs = new HashSet<>();
									while (!isFoundSolution && canNegateCondition && se.getConstraints().size() >= 1) {
										// Choose a random constraint to negate
										IPathConstraints negatedConstraints = null;
										boolean foundNegatedCondition = false;
										do {
											int negatedConstraintsIndexCandidate = new Random()
													.nextInt(se.getConstraints().size());

											if (((PathConstraints) se.getConstraints())
													.get(negatedConstraintsIndexCandidate).getCfgNode() == null) {
												negatedIndexs.add(new Integer(negatedConstraintsIndexCandidate));

											} else if (!negatedIndexs.contains(negatedConstraintsIndexCandidate)) {
												negatedIndexs.add(new Integer(negatedConstraintsIndexCandidate));

												// If the negated condition is not visited in all of two branches
												negatedConstraints = se.getConstraints()
														.negateConditionAt(negatedConstraintsIndexCandidate);
												ConditionCfgNode negatedCfgNode = (ConditionCfgNode) ((PathConstraints) negatedConstraints)
														.get(negatedConstraintsIndexCandidate).getCfgNode();
												if (!negatedCfgNode.isVisitedFalseBranch()
														|| !negatedCfgNode.isVisitedTrueBranch()) {
													foundNegatedCondition = true;
												}
											}
										} while (!foundNegatedCondition
												&& negatedIndexs.size() < se.getConstraints().size());

										if (foundNegatedCondition) {
											// Solve path constraints
											logger.debug("Negated Constraints: \n" + negatedConstraints.toString()
													.replace(ISymbolicVariable.PREFIX_SYMBOLIC_VALUE, "")
													.replace(
															ISymbolicVariable.SEPARATOR_BETWEEN_STRUCTURE_NAME_AND_ITS_ATTRIBUTES,
															".")
													.replace(ISymbolicVariable.ARRAY_CLOSING, "]")
													.replace(ISymbolicVariable.ARRAY_OPENING, "["));
											testdata = new StaticSolutionGeneration().solve(negatedConstraints,
													originalFunction);

											if (testdata.equals(IStaticSolutionGeneration.NO_SOLUTION)) {
												logger.info("No solution. Continue negating.");
												isFoundSolution = false;
											} else if (testdata.equals(IStaticSolutionGeneration.EVERY_SOLUTION)) {
												isFoundSolution = true;
												// Just pick a random test data
												testdata = initializeTestdataAtRandom();
												logger.info("May solution. Choose a solution. Next test data = <b>"
														+ testdata + "</b>");
											} else {
												isFoundSolution = true;
												logger.info(
														"Found a solution. Next test data = <b>" + testdata + "</b>");
											}
										} else {
											logger.info(
													"Can not negate any condition. We start generating test data at random");
											canNegateCondition = false;
											testdata = "";
											break;
										}
									}
								}
								break;

							case IFunctionConfig.STATEMENT_COVERAGE:
							case IFunctionConfig.SUBCONDITION:
								throw new Exception("Dont code this kind of code coverage");
							}
						}
					} else {
						logger.debug("Current test data causes errors.");
						AbstractTestdataGeneration.testdata.add(new TestdataInReport(fullTestdata,
								new TestpathString_Marker(), false, tmp_currentStatementCoverage,
								tmp_currentBranchCoverage, false, tmp_isGenerateRandomly, false));
					}
				}
				depth++;
			}

			AbstractTestdataGeneration.numOfBranches += normalizedCfg.getUnvisitedBranches().size()
					+ normalizedCfg.getVisitedBranches().size();
			AbstractTestdataGeneration.numOfVisitedBranches += normalizedCfg.getVisitedBranches().size();
		}
	}

	@Override
	protected void generateTestdata(IFunctionNode originalFunction, ITestedFunctionReport fnReport) throws Exception {
		logger.debug("Test data generation strategy: Fast Mars");
		Date startTime = Calendar.getInstance().getTime();

		// DART(originalFunction);
		DART_IMPROVEMENT(originalFunction);

		// Calculate the running time
		Date end = Calendar.getInstance().getTime();
		long runningTime = end.getTime() - startTime.getTime();
		runningTime = runningTime / 1000;// seconds

		fnReport.getCoverage().setTime(runningTime);
		fnReport.getCoverage().setNumofSolverCalls(numOfSolverCalls);
		logger.debug("Generate test data done");
		logger.debug("\n\n\n\n\n\n\n\n\n\n");
	}

	/**
	 * Ex: Consider this function:
	 * 
	 * <pre>
	 *  int struct_test1(SinhVien sv){
	 *		char* s = sv.other[0].eeee;
	 *		if (sv.age > 0){
	 *			if (s[0] == 'a')
	 *				return 0;
	 *			else
	 *				return 1;
	 *		}else{
	 *			return 2;		
	 *		}
	 *	}
	 * </pre>
	 * 
	 * The above function has only one argument and it has been configured. <br/>
	 * Example of output: sv.age=306;sv.name=NULL;sv.other[0].eeee=NULL;
	 * 
	 * @param arguments
	 * @param functionConfig
	 * @param prefixName
	 * @return
	 */
	protected Map<String, String> constructRandomInput(List<IVariableNode> arguments, IFunctionConfig functionConfig,
			String prefixName) {
		Map<String, String> input = new TreeMap<>();
		for (IVariableNode argument : arguments) {
			String type = argument.getRawType();

			// Number
			if (VariableTypes.isBool(type)) {
				// 0 - false; 1 - true
				input.put(prefixName + argument.getName(), BasicTypeRandom.generateInt(0, 1) + "");
			} else if (VariableTypes.isNumBasic(type)) {
				if (VariableTypes.isNumBasicFloat(type)) {
					input.put(prefixName + argument.getName(),
							BasicTypeRandom.generateFloat(functionConfig.getIntegerBound().getLower(),
									functionConfig.getIntegerBound().getUpper()) + "");
				} else {
					input.put(prefixName + argument.getName(),
							BasicTypeRandom.generateInt(functionConfig.getIntegerBound().getLower(),
									functionConfig.getIntegerBound().getUpper()) + "");
				}

			} else if (VariableTypes.isNumOneDimension(type)) {
				for (int i = 0; i < functionConfig.getSizeOfArray(); i++)
					if (VariableTypes.isNumOneDimensionFloat(type)) {
						input.put(prefixName + argument.getName() + "[" + i + "]",
								BasicTypeRandom.generateFloat(functionConfig.getIntegerBound().getLower(),
										functionConfig.getIntegerBound().getUpper()) + "");
					} else {
						input.put(prefixName + argument.getName() + "[" + i + "]",
								BasicTypeRandom.generateInt(functionConfig.getIntegerBound().getLower(),
										functionConfig.getIntegerBound().getUpper()) + "");
					}

			} else if (VariableTypes.isNumOneLevel(type)) {
				if (assignPointerToNull()) {
					input.put(prefixName + argument.getName(), "NULL");
				} else {
					for (int i = 0; i < functionConfig.getSizeOfArray(); i++)
						input.put(prefixName + argument.getName() + "[" + i + "]",
								BasicTypeRandom.generateInt(functionConfig.getIntegerBound().getLower(),
										functionConfig.getIntegerBound().getUpper()) + "");
				}
			}
			// Character
			else if (VariableTypes.isChBasic(type)) {
				input.put(prefixName + argument.getName(),
						BasicTypeRandom.generateInt(functionConfig.getCharacterBound().getLower(),
								functionConfig.getCharacterBound().getUpper()) + "");

			} else if (VariableTypes.isChOneDimension(type)) {
				for (int i = 0; i < functionConfig.getSizeOfArray(); i++)
					input.put(prefixName + argument.getName() + "[" + i + "]",
							BasicTypeRandom.generateInt(functionConfig.getCharacterBound().getLower(),
									functionConfig.getCharacterBound().getUpper()) + "" + "");

			} else if (VariableTypes.isChOneLevel(type)) {
				if (assignPointerToNull()) {
					input.put(prefixName + argument.getName(), "NULL");
				} else {
					for (int i = 0; i < functionConfig.getSizeOfArray(); i++)
						input.put(prefixName + argument.getName() + "[" + i + "]",
								BasicTypeRandom.generateInt(functionConfig.getCharacterBound().getLower(),
										functionConfig.getCharacterBound().getUpper()) + "" + "");
				}
			}
			// Structure
			else if (VariableTypes.isStructureSimple(type)) {
				INode correspondingNode = argument.resolveCoreType();
				if (correspondingNode != null && correspondingNode instanceof StructureNode) {
					input.putAll(constructRandomInput(((StructureNode) correspondingNode).getAttributes(),
							functionConfig, prefixName + argument.getName() + "."));
				}

			} else if (VariableTypes.isStructureOneDimension(type)) {
				INode correspondingNode = argument.resolveCoreType();

				if (correspondingNode != null && correspondingNode instanceof StructureNode)
					for (int i = 0; i < functionConfig.getSizeOfArray(); i++) {
						input.putAll(constructRandomInput(((StructureNode) correspondingNode).getAttributes(),
								functionConfig, prefixName + argument.getName() + "[" + i + "]" + "."));
					}

			} else if (VariableTypes.isStructureOneLevel(type)) {
				if (assignPointerToNull()) {
					input.put(prefixName + argument.getName(), "NULL");
				} else {
					INode correspondingNode = argument.resolveCoreType();

					if (correspondingNode != null && correspondingNode instanceof StructureNode) {
						List<IVariableNode> attributes = ((StructureNode) correspondingNode).getAttributes();

						// Consider the linked list case (e.g., "class A{A* next}"), we assign value of
						// "next" to NULL. Besides, we assume the size of the structure pointer is
						// equivalent to 0.
						for (int i = attributes.size() - 1; i >= 0; i--)
							if (attributes.get(i).getReducedRawType().equals(argument.getReducedRawType())) {
								input.put(prefixName + argument.getName() + "[0]." + attributes.get(i).getName(),
										"NULL");
								attributes.remove(i);
							}

						//
						input.putAll(constructRandomInput(attributes, functionConfig,
								prefixName + argument.getName() + "[0]."));
					}
				}
			}
		}
		return input;
	}

	/**
	 * Generate normalized control flow graph<br/>
	 * The testing function should be normalized into a unique format. <br/>
	 * Ex: "int test(int a){a=a>0?1:2;}"---normalize---->"int test(int a){if (a>0)
	 * a=1; else a=2;}".
	 * 
	 * @param originalFunction
	 *            The original function
	 * @return
	 * @throws Exception
	 */
	private ICFG generateNormalizedCFG(IFunctionNode originalFunction) throws Exception {
		ICFG normalizedCfg = null;

		IFunctionNode normalizedFunction = (IFunctionNode) originalFunction.clone();
		normalizedFunction.setAST(originalFunction.normalizedAST().getNormalizedAST());

		if (normalizedFunction.getAST().getRawSignature().equals(originalFunction.getAST().getRawSignature()))
			logger.info("We normalize the current CFG. Normalized function: the same");
		else
			logger.info("We normalize the current CFG. Normalized function: \n"
					+ normalizedFunction.getAST().getRawSignature());

		switch (originalFunction.getFunctionConfig().getTypeofCoverage()) {
		case IFunctionConfig.BRANCH_COVERAGE:
		case IFunctionConfig.STATEMENT_COVERAGE:
			normalizedCfg = new CFGGenerationforBranchvsStatementCoverage(normalizedFunction,
					ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES).generateCFG();
			break;
		case IFunctionConfig.SUBCONDITION:
			normalizedCfg = new CFGGenerationSubCondition(normalizedFunction,
					ICFGGeneration.SEPARATE_FOR_INTO_SEVERAL_NODES).generateCFG();
			break;
		}

		normalizedCfg.setFunctionNode(normalizedFunction);
		normalizedCfg.resetVisitedStateOfNodes();
		normalizedCfg.setIdforAllNodes();
		logger.info("Num of statements in normalized CFG: " + normalizedCfg.getUnvisitedStatements().size());
		logger.info("Num of branches in normalized CFG: " + normalizedCfg.getUnvisitedBranches().size());
		return normalizedCfg;
	}

	protected boolean assignPointerToNull() {
		return new Random().nextInt(2/* default */) == 1;
	}

	public static final int MAX_ITERATIONS_IN_ONE_DEPTH = 20; // default
	public static final int DEPTH = 5; // default
}
