package com.fit.testdatagen;

import java.io.File;
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

import test.testdatageneration.Bug;

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

	private void DART_negatedTheLastCondition2(IFunctionNode originalFunction) throws Exception {
		getExePath(originalFunction);
		ICFG normalizedCfg = generateNormalizedCFG(originalFunction);
		if (normalizedCfg != null) {

			// Initialize data at random
			String testdata = initializeTestdataAtRandom();

			// Generate test data
			int iteration = 1;
			float currentCoverage = 0.0f;

			boolean hasNextTestdata = false;

			PartialTestpaths solvedTestpaths = new PartialTestpaths();
			do {
				hasNextTestdata = false;
				logger.debug("\n\n\n" + "---------------------ITERATION " + iteration + "-------------------------");

				ITestdataExecution testdataExecution = executeTestdata(originalFunction, testdata);

				if (testdataExecution != null) {
					// Update CFG
					CFGUpdater_Mark cfgUpdater = new CFGUpdater_Mark(testdataExecution.getEncodedTestpath(),
							normalizedCfg);
					cfgUpdater.storeTheCurrentVisitedStateOfCfg();
					logger.debug("Previous Visited Nodes: " + cfgUpdater.getPreviousVisitedNodes());
					logger.debug("Previous Visited Branches: " + cfgUpdater.getPreviousVisitedBranches());
					cfgUpdater.updateVisitedNodes();

					// CASE 1. The latest test path is uncomplete (there arises an error when the
					// test data execute)
					if (cfgUpdater.isUncompleteTestpath()) {
						cfgUpdater.unrollChangesOfTheLatestPath();
						logger.debug("The testpath is uncomplete! Unroll the state of CFG!");
						logger.debug(
								"Current statement coverage = " + cfgUpdater.getCfg().computeStatementCoverage() * 100);
						logger.debug("Current branch coverage = " + cfgUpdater.getCfg().computeBranchCoverage() * 100);
					}
					// CASE 2. No errors during test data execution
					else {
						logger.debug("The testpath is complete!");
						logger.debug(
								"Current statement coverage = " + cfgUpdater.getCfg().computeStatementCoverage() * 100);
						logger.debug("Current branch coverage = " + cfgUpdater.getCfg().computeBranchCoverage() * 100);

						// Add to test report
						fnReport.addTestpath(new TestpathReport(testdataExecution.getEncodedTestpath(),
								testdataExecution.getEncodedTestpath().getEncodedTestpath(), null, null, null, "-",
								originalFunction));

						// Select the best uncovered partial test paths
						PartialTestpaths uncoveredPartialTestpaths = null;
						IPartialTestpath theBestPartialTestpath = null;

						switch (originalFunction.getFunctionConfig().getTypeofCoverage()) {

						case IFunctionConfig.BRANCH_COVERAGE:
							uncoveredPartialTestpaths = normalizedCfg
									.getPartialTestpathcontainingUnCoveredBranches_Strategy1();
							// Remove test paths solved before
							for (int i = uncoveredPartialTestpaths.size() - 1; i >= 0; i--)
								if (solvedTestpaths.contains(uncoveredPartialTestpaths.get(i))) {
									uncoveredPartialTestpaths.remove(i);
								}

							currentCoverage = normalizedCfg.computeBranchCoverage() * 100;
							break;

						case IFunctionConfig.STATEMENT_COVERAGE:
							uncoveredPartialTestpaths = normalizedCfg.getPartialTestpathcontainingUnCoveredStatements(); // chua
																															// code
							currentCoverage = normalizedCfg.computeStatementCoverage() * 100;
							break;

						case IFunctionConfig.SUBCONDITION:
							throw new Exception("Dont code sub-condition coverage");
						}

						if (uncoveredPartialTestpaths.size() >= 1)
							do {
								logger.debug(
										"Num of potential uncovered test path = " + uncoveredPartialTestpaths.size());
								// Choose a random test path in the candidate set
								theBestPartialTestpath = uncoveredPartialTestpaths
										.get(new Random().nextInt(uncoveredPartialTestpaths.size()));
								uncoveredPartialTestpaths.remove(theBestPartialTestpath);
								solvedTestpaths.add(theBestPartialTestpath);

								logger.debug("Find the best potential partial test path: "
										+ theBestPartialTestpath.getFullPath());

								// Create constraints of the chosen test path
								if (theBestPartialTestpath != null) {
									Parameter paramaters = new Parameter();
									for (INode n : ((FunctionNode) originalFunction).getArguments())
										paramaters.add(n);
									for (INode n : ((FunctionNode) originalFunction).getReducedExternalVariables())
										paramaters.add(n);

									logger.debug("Performing symbolic execution on this test path");
									ISymbolicExecution se = new SymbolicExecution(theBestPartialTestpath, paramaters,
											originalFunction);

									logger.debug("Done. Constraints: \n" + se.getConstraints().toString()
											.replace(ISymbolicVariable.PREFIX_SYMBOLIC_VALUE, "")
											.replace(
													ISymbolicVariable.SEPARATOR_BETWEEN_STRUCTURE_NAME_AND_ITS_ATTRIBUTES,
													".")
											.replace(ISymbolicVariable.ARRAY_CLOSING, "]")
											.replace(ISymbolicVariable.ARRAY_OPENING, "["));

									// Find test data
									if (se.getConstraints().size() >= 1) {
										testdata = new StaticSolutionGeneration().solve(se.getConstraints(),
												originalFunction);

										if (!testdata.equals(IStaticSolutionGeneration.NO_SOLUTION)) {
											hasNextTestdata = true;
											logger.debug("Done solving. Next test data = " + testdata);
										}
									}
								}
							} while (!hasNextTestdata && uncoveredPartialTestpaths.size() >= 1);
					}
				} else
					logger.debug("Current test data causes errors.");

				if (!hasNextTestdata && currentCoverage < 100f) {
					testdata = initializeTestdataAtRandom();
					logger.debug("Generate random test data= " + testdata);
				}

			} while (++iteration < MAX_ITERATIONS && currentCoverage < 100f);
		}
	}

	/**
	 * Step 1. Generate test data at random <br/>
	 * Step 2. Execute test data to get test path. If error occurs, back to Step 1,
	 * else Step 2 <br/>
	 * Step 3. Get constraints<br/>
	 * Step 4. Negate constraints (from the last constraint)<br/>
	 * 
	 * @param originalFunction
	 * @throws Exception
	 */
	private void DART_negatedTheLastCondition(IFunctionNode originalFunction) throws Exception {
		// Config: no limit loop, no limit recursive
		getExePath(originalFunction);
		ICFG normalizedCfg = generateNormalizedCFG(originalFunction);

		if (normalizedCfg != null) {

			String testdata = "";
			int iteration = 0;
			float currentCoverage = 0.0f;

			while (iteration < MAX_ITERATIONS && currentCoverage < 100f) {
				logger.debug("\n\n\n" + "---------------------ITERATION " + iteration + "-------------------------");
				iteration++;
				/**
				 * STEP 1. INITIALIZE TEST DATA AT RANDOM
				 */
				if (testdata.length() == 0) {
					testdata = initializeTestdataAtRandom();
					logger.debug("Generate random test data= " + testdata);
				}

				/**
				 * STEP 2. EXECUTE TEST DATA TO GET TEST PATH
				 */
				ITestdataExecution testdataExecution = executeTestdata(originalFunction, testdata);

				if (testdataExecution != null) {
					// Update CFG
					CFGUpdater_Mark cfgUpdater = new CFGUpdater_Mark(testdataExecution.getEncodedTestpath(),
							normalizedCfg);
					cfgUpdater.storeTheCurrentVisitedStateOfCfg();
					logger.debug("Previous Visited Nodes: " + cfgUpdater.getPreviousVisitedNodes());
					logger.debug("Previous Visited Branches: " + cfgUpdater.getPreviousVisitedBranches());
					cfgUpdater.updateVisitedNodes();
					logger.debug("The testpath is uncomplete! We still update CFG");
					logger.debug(
							"Current statement coverage = " + cfgUpdater.getCfg().computeStatementCoverage() * 100);
					logger.debug("Current branch coverage = " + cfgUpdater.getCfg().computeBranchCoverage() * 100);

					// CASE 1. The latest test path is uncomplete (there arises an error when the
					// test data execute)
					if (cfgUpdater.isUncompleteTestpath()) {
						// cfgUpdater.unrollChangesOfTheLatestPath(); DART does not unroll
						logger.debug("Found a bug. Uncomplete test path");

						Bug bug = new Bug(testdata, testdataExecution.getEncodedTestpath(), originalFunction);
						AbstractTestdataGeneration.bugs.add(bug);
					}
					// CASE 2. No errors during test data execution
					else {
						// Add to test report
						fnReport.addTestpath(new TestpathReport(testdataExecution.getEncodedTestpath(),
								testdataExecution.getEncodedTestpath().getEncodedTestpath(), null, null, null, "-",
								originalFunction));

						switch (originalFunction.getFunctionConfig().getTypeofCoverage()) {

						case IFunctionConfig.BRANCH_COVERAGE:
							currentCoverage = normalizedCfg.computeBranchCoverage() * 100;
							ITestpathInCFG executedTestpath = cfgUpdater.getUpdatedCFGNodes();

							if (currentCoverage == 100f)
								break;
							else
							/**
							 * STEP 3. GET PATH CONSTRAINTS
							 */
							if (executedTestpath != null) {
								Parameter paramaters = new Parameter();
								for (INode n : ((FunctionNode) originalFunction).getArguments())
									paramaters.add(n);
								for (INode n : ((FunctionNode) originalFunction).getReducedExternalVariables())
									paramaters.add(n);

								logger.debug("Performing symbolic execution on this test path");
								ISymbolicExecution se = new SymbolicExecution(executedTestpath, paramaters,
										originalFunction);

								logger.debug("Done. Constraints: \n" + se.getConstraints().toString()
										.replace(ISymbolicVariable.PREFIX_SYMBOLIC_VALUE, "")
										.replace(ISymbolicVariable.SEPARATOR_BETWEEN_STRUCTURE_NAME_AND_ITS_ATTRIBUTES,
												".")
										.replace(ISymbolicVariable.ARRAY_CLOSING, "]")
										.replace(ISymbolicVariable.ARRAY_OPENING, "["));

								/**
								 * STEP 4. NEGATE PATH CONSTRAINTS
								 */

								logger.debug("NEGATE PATH CONSTRAINTS");

								boolean isFoundSolution = false;
								boolean canNegateCondition = true;

								Set<Integer> negatedIndexs = new HashSet<>();
								while (!isFoundSolution && canNegateCondition) {
									// Choose a random constraint to negate
									IPathConstraints negatedConstraints = null;
									boolean foundNegatedCondition = false;
									do {
										int negatedConstraintsIndexCandidate = new Random()
												.nextInt(se.getConstraints().size());
										if (!negatedIndexs.contains(negatedConstraintsIndexCandidate)) {
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
											logger.debug("No solution. Continue negating.");
											isFoundSolution = false;
										} else if (testdata.equals(IStaticSolutionGeneration.EVERY_SOLUTION)) {
											isFoundSolution = true;
											// Just pick a random test data
											testdata = initializeTestdataAtRandom();
											logger.debug("Found a solution. Next test data = " + testdata);
										} else {
											isFoundSolution = true;
											logger.debug("Found a solution. Next test data = " + testdata);
										}
									} else {
										logger.debug(
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
				} else
					logger.debug("Current test data causes errors.");
			}
		}
	}

	@Override
	protected void generateTestdata(IFunctionNode originalFunction, ITestedFunctionReport fnReport) throws Exception {
		logger.debug("Test data generation strategy: Fast Mars");
		Date startTime = Calendar.getInstance().getTime();

		DART_negatedTheLastCondition(originalFunction);

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
								BasicTypeRandom.generateInt(functionConfig.getCharacterBound().getLower(),
										functionConfig.getCharacterBound().getUpper()) + "");
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
		// logger.debug("Normalized function: \n" +
		// normalizedFunction.getAST().getRawSignature());

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

	public static final int MAX_ITERATIONS = 22; // default
}
