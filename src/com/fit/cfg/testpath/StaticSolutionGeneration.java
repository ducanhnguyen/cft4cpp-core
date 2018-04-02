package com.fit.cfg.testpath;

import org.apache.log4j.Logger;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.testdatagen.ITestdataGeneration;
import com.fit.testdatagen.fast.SmtLibGeneration2;
import com.fit.testdatagen.se.IPathConstraints;
import com.fit.testdatagen.se.ISymbolicExecution;
import com.fit.testdatagen.se.Parameter;
import com.fit.testdatagen.se.PathConstraint;
import com.fit.testdatagen.se.PathConstraints;
import com.fit.testdatagen.se.SymbolicExecution;
import com.fit.testdatagen.se.memory.ISymbolicVariable;
import com.fit.testdatagen.se.solver.ISmtLibGeneration;
import com.fit.testdatagen.se.solver.IZ3SolutionParser;
import com.fit.testdatagen.se.solver.RunZ3OnCMD;
import com.fit.testdatagen.se.solver.SmtLibGeneration;
import com.fit.testdatagen.se.solver.Z3SolutionParser;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.IVariableNode;
import com.fit.utils.SpecialCharacter;
import com.fit.utils.Utils;

public class StaticSolutionGeneration implements IStaticSolutionGeneration {
	final static Logger logger = Logger.getLogger(StaticSolutionGeneration.class);

	private ITestpathInCFG testpath;

	private IFunctionNode functionNode;

	private String staticSolution;

	public static void main(String[] args) {
	}

	@Override
	public void generateStaticSolution() throws Exception {
		if (this.testpath != null && this.functionNode != null) {
			Parameter variables = this.removeExternVariables(this.getPassingVariables(this.functionNode));
			ISymbolicExecution se = new SymbolicExecution(this.testpath, variables, this.functionNode);

			this.staticSolution = solve(se.getConstraints(), functionNode);
		} else
			this.staticSolution = IStaticSolutionGeneration.NO_SOLUTION;
	}

	public String solve(IPathConstraints pathConstraints, IFunctionNode functionNode) throws Exception {
		this.staticSolution = IZ3SolutionParser.NO_SOLUTION;

		if (((PathConstraints) pathConstraints).size() >= 1) {

			// Step 1. Find solution of not-null path constraints
			if (pathConstraints.getNormalConstraints().size() >= 1) {
				ISmtLibGeneration smtLibGeneration = null;
				switch (AbstractSetting.getValue(ISettingv2.TESTDATA_STRATEGY)) {
				case ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.MARS2 + "":
					smtLibGeneration = new SmtLibGeneration(functionNode.getPassingVariables(),
							pathConstraints.getNormalConstraints());
					break;
				case ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.FAST_MARS + "":
					smtLibGeneration = new SmtLibGeneration2(pathConstraints.getVariablesTableNode(),
							pathConstraints.getNormalConstraints());
					break;
				}

				smtLibGeneration.generate();
				String smtLibContent = smtLibGeneration.getSmtLibContent();

				if (smtLibContent.equals(ISmtLibGeneration.EMPTY_SMT_LIB_FILE)) {
					// In this case, the content of the SMT-Lib file is empty. We dont need to use
					// SMT-Solver because the path constraints does not have any solution
					// definitely.
					this.staticSolution = NO_SOLUTION;
				} else {
					String constraintsFile = AbstractSetting.getValue(ISettingv2.SMT_LIB_FILE_PATH);
					Utils.writeContentToFile(smtLibContent, constraintsFile);

					RunZ3OnCMD z3Runner = new RunZ3OnCMD(AbstractSetting.getValue(ISettingv2.SOLVER_Z3_PATH),
							constraintsFile);
					z3Runner.execute();

					this.staticSolution = new Z3SolutionParser().getSolution(z3Runner.getSolution());
				}
			} else
				this.staticSolution = EVERY_SOLUTION;

			// Step 2. Add null path constraints (e.g., x!=NULL, x==NULL)
			if (this.staticSolution.equals(NO_SOLUTION)) {
				// nothing to do
			} else {
				// e.g., "x==NULL" ---------> add "sizeof(x)=0"
				for (PathConstraint nullConstraint : pathConstraints.getNullorNotNullConstraints())
					this.staticSolution += nullConstraint.getConstraint().replace("==", "=")
							.replace(ISymbolicVariable.PREFIX_SYMBOLIC_VALUE, "")
							.replace(ISymbolicVariable.SEPARATOR_BETWEEN_STRUCTURE_NAME_AND_ITS_ATTRIBUTES, ".")
							.replace(ISymbolicVariable.ARRAY_CLOSING, "]").replace(ISymbolicVariable.ARRAY_OPENING, "[")
							+ SpecialCharacter.END_OF_STATEMENT;
			}
		} else
			// In this case, the size of the path constraints is equivalent to 0.
			this.staticSolution = IStaticSolutionGeneration.EVERY_SOLUTION;
		return this.staticSolution;
	}

	/**
	 * Get all passing variables of a function
	 *
	 * @param fn
	 * @return
	 */
	private Parameter getPassingVariables(IFunctionNode fn) {
		Parameter vars = new Parameter();
		for (IVariableNode n : fn.getPassingVariables())
			vars.add(n);
		return vars;
	}

	/**
	 * Remove all extern variables from the passing variable.Ex: remove variable
	 * "extern int x = 1"
	 *
	 * @param paramaters
	 * @return
	 */
	private Parameter removeExternVariables(Parameter paramaters) {
		for (int i = paramaters.size() - 1; i >= 0; i--) {
			INode n = paramaters.get(i);

			if (n instanceof IVariableNode)
				if (((IVariableNode) n).isExtern())
					paramaters.remove(n);

		}
		return paramaters;
	}

	@Override
	public ITestpathInCFG getTestpath() {
		return this.testpath;
	}

	@Override
	public void setTestpath(AbstractTestpath testpath) {
		this.testpath = testpath;
	}

	@Override
	public IFunctionNode getFunctionNode() {
		return this.functionNode;
	}

	@Override
	public void setFunctionNode(IFunctionNode functionNode) {
		this.functionNode = functionNode;
	}

	@Override
	public String getStaticSolution() {
		return this.staticSolution;
	}
}
