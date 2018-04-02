package com.console;

import java.util.HashSet;
import java.util.Set;

import com.fit.tree.object.IFunctionNode;

import test.testdatageneration.Bug;

public class ConsoleOutput {
	private IFunctionNode functionNode;

	// Real coverage
	private float coverge;

	private int numTestpath;

	private long runningTime;

	// Number of using solver
	private int numOfSolverCalls = 0;

	// Number of symbolic execution
	private int numOfSymbolicExecutions = 0;

	// Number of symbolic statements. In each symbolic execution, some of
	// statements will be symbolic.
	private int numOfSymbolicStatements = 0;

	// Number of execution
	private int numOfExecutions = 0;

	// Number of solver calls but its solution does not increase coverage
	private int numOfNoChangeToCoverage = 0;

	// Number of solver calls but can not solve (cause errors)
	private int numOfSolverCallsbutCannotSolve = 0;

	// The total time of running make command (ms)
	private long makeCommandRunningTime = 0;

	// The number of make file call
	private int makeCommandRunningNumber = 0;

	// The total time of execution
	private long executionTime = 0;

	// The total time of waiting solver to get solution (ms)
	private long solverRunningTime = 0;

	// The total time of normalizing function(ms)
	private long normalizationTime;

	// The total time of symbolic execution (ms)
	private long symbolicExecutionTime;

	// The total time of macro normalization (ms)
	public static long macroNormalizationTime;

	public static Set<Bug> bugs = new HashSet<>();

	public float getCoverge() {
		return coverge;
	}

	public void setCoverge(float coverge) {
		this.coverge = coverge;
	}

	public int getNumTestpath() {
		return numTestpath;
	}

	public void setNumTestpath(int numTestpath) {
		this.numTestpath = numTestpath;
	}

	public IFunctionNode getFunctionNode() {
		return functionNode;
	}

	public void setFunctionNode(IFunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	public long getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(long runningTime) {
		this.runningTime = runningTime;
	}

	public int getNumOfSolverCalls() {
		return numOfSolverCalls;
	}

	public void setNumOfSolverCalls(int numOfSolverCalls) {
		this.numOfSolverCalls = numOfSolverCalls;
	}

	public int getNumOfSymbolicExecutions() {
		return numOfSymbolicExecutions;
	}

	public void setNumOfSymbolicExecutions(int numOfSymbolicExecutions) {
		this.numOfSymbolicExecutions = numOfSymbolicExecutions;
	}

	public int getNumOfSymbolicStatements() {
		return numOfSymbolicStatements;
	}

	public void setNumOfSymbolicStatements(int numOfSymbolicStatements) {
		this.numOfSymbolicStatements = numOfSymbolicStatements;
	}

	public int getNumOfExecutions() {
		return numOfExecutions;
	}

	public void setNumOfExecutions(int numOfExecutions) {
		this.numOfExecutions = numOfExecutions;
	}

	public int getNumOfNoChangeToCoverage() {
		return numOfNoChangeToCoverage;
	}

	public void setNumOfNoChangeToCoverage(int numOfNoChangeToCoverage) {
		this.numOfNoChangeToCoverage = numOfNoChangeToCoverage;
	}

	public int getNumOfSolverCallsbutCannotSolve() {
		return numOfSolverCallsbutCannotSolve;
	}

	public void setNumOfSolverCallsbutCannotSolve(int numOfSolverCallsbutCannotSolve) {
		this.numOfSolverCallsbutCannotSolve = numOfSolverCallsbutCannotSolve;
	}

	public long getSolverRunningTime() {
		return solverRunningTime;
	}

	public void setSolverRunningTime(long solverRunningTime) {
		this.solverRunningTime = solverRunningTime;
	}

	public long getMakeCommandRunningTime() {
		return makeCommandRunningTime;
	}

	public void setMakeCommandRunningTime(long makeCommandRunningTime) {
		this.makeCommandRunningTime = makeCommandRunningTime;
	}

	public void setMakeCommandRunningNumber(int makeCommandRunningNumber) {
		this.makeCommandRunningNumber = makeCommandRunningNumber;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public long getNormalizationTime() {
		return normalizationTime;
	}

	public void setNormalizationTime(long normalizationTime) {
		this.normalizationTime = normalizationTime;
	}

	public long getSymbolicExecutionTime() {
		return symbolicExecutionTime;
	}

	public void setSymbolicExecutionTime(long symbolicExecutionTime) {
		this.symbolicExecutionTime = symbolicExecutionTime;
	}

	public int getMakeCommandRunningNumber() {
		return makeCommandRunningNumber;
	}

	public static long getMacroNormalizationTime() {
		return macroNormalizationTime;
	}

	public static void setMacroNormalizationTime(long macroNormalizationTime) {
		ConsoleOutput.macroNormalizationTime = macroNormalizationTime;
	}

	public static Set<Bug> getBugs() {
		return bugs;
	}

	public static void setBugs(Set<Bug> bugs) {
		ConsoleOutput.bugs = bugs;
	}
}
