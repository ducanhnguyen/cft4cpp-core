package com.fit.testdatagen.se.solver;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fit.testdatagen.se.CustomJeval;
import com.fit.testdatagen.se.ISymbolicExecution;
import com.fit.testdatagen.se.memory.ISymbolicVariable;

/**
 * Convert solution in SMT-Solver z3 to the readable human format
 * <p>
 * <p>
 * Z3 link:https://github.com/Z3Prover/z3/releases
 *
 * @author anhanh
 */
public class Z3SolutionParser implements IZ3SolutionParser {

	public Z3SolutionParser() {
	}

	public static void main(String[] args) {
		// String Z3Solution = "unknown\n(error \"line 27 column 10: model is
		// not available\")";
		String Z3Solution = "(error \"line 6 column 30: invalid function application, wrong number of arguments\")\nsat\n(model\n\n  (define-fun tvwp ((x!1 Int) (x!2 Int)) Int\n    (ite (and (= x!1 0) (= x!2 0)) 10\n      10))\n)";
		// String Z3Solution = "(model\n(define-fun tvwx () Real\n(/ 1.0
		// 10000.0))\n)";

		// String Z3Solution = "(model\n(define-fun tvwhe ()
		// Real\n1.0)\n(define-fun tvwb_w () Real\n(- (/ 9981.0 10000.0))))";
		System.out.println(new Z3SolutionParser().getSolution(Z3Solution));
	}

	@Override
	public String getSolution(String Z3Solution) {
		String solution = "";
		if (Z3Solution.equals(ISymbolicExecution.UNSAT_IN_Z3)) {

		} else {
			String[] lineList = Z3Solution.split("\n");
			String name = "";
			String value = "";
			boolean ignoreEndLine = false;

			for (String line : lineList)
				switch (getTypeOfLine(line)) {
				case KHAI_BAO_ID:
					ignoreEndLine = false;
					name = getName(line);
					break;

				case IF_THEN_ELSE_ID:
					ArrayList<String> indexList = getIndex(line);
					value = getValueOfIte(line);
					solution += name;
					for (String i : indexList)
						solution += "[" + i + "]";
					solution += "=" + value + ",";
					ignoreEndLine = true;
					break;

				case VALUE_ID:
					if (!ignoreEndLine) {
						value = getValueOfVariable(line);
						value = new CustomJeval().evaluate(value);
						solution += name + "=" + value + ",";
					}
					break;

				case ERROR:
				case UNKNOWN_ID:
					break;
				}
			if (solution.lastIndexOf(",") > 0)
				solution = solution.substring(0, solution.lastIndexOf(","));

			// Restore solution to its original format
			solution = solution.replace(ISymbolicVariable.PREFIX_SYMBOLIC_VALUE, "")
					.replace(ISymbolicVariable.SEPARATOR_BETWEEN_STRUCTURE_NAME_AND_ITS_ATTRIBUTES, ".")
					.replace(ISymbolicVariable.ARRAY_CLOSING, "]").replace(ISymbolicVariable.ARRAY_OPENING, "[")
					.replace(",", ";");

			if (solution.length() > 0 && !solution.endsWith(";"))
				solution += ";";
		}
		return solution;
	}

	/**
	 * Láº¥y cÃ¡c chá»‰ sá»‘ cá»§a hÃ m
	 *
	 * @param ifThenElse
	 * @return
	 */
	private ArrayList<String> getIndex(String ifThenElse) {
		ArrayList<String> indexList = new ArrayList<>();
		Matcher m = Pattern.compile("=\\s(\\w+)!(\\d+)\\s([^\\)]+)").matcher(ifThenElse);
		while (m.find())
			indexList.add(m.group(3));
		return indexList;
	}

	/**
	 * Láº¥y tÃªn hÃ m
	 *
	 * @param defineFun
	 * @return
	 */
	private String getName(String defineFun) {
		String nameFunction = "";
		Matcher m = Pattern.compile("\\(define-fun\\s(\\w+)").matcher(defineFun);
		while (m.find()) {
			nameFunction = m.group(1);
			break;
		}
		return nameFunction;
	}

	/**
	 * XÃ¡c Ä‘á»‹nh loáº¡i dÃ²ng lá»‡nh
	 *
	 * @param line
	 * @return
	 */
	private int getTypeOfLine(String line) {
		if (line.contains(IZ3SolutionParser.KHAI_BAO))
			return IZ3SolutionParser.KHAI_BAO_ID;

		else if (line.contains(IZ3SolutionParser.IF_THEN_ELSE))
			return IZ3SolutionParser.IF_THEN_ELSE_ID;

		else if (line.contains(IZ3SolutionParser.MODEL))
			return IZ3SolutionParser.MODEL_ID;

		else if (line.equals(IZ3SolutionParser.END))
			return IZ3SolutionParser.END_ID;

		else if (line.equals(IZ3SolutionParser.SAT))
			return IZ3SolutionParser.SAT_ID;

		else if (line.equals(IZ3SolutionParser.UNSAT))
			return IZ3SolutionParser.UNSAT_ID;

		else if (line.equals(IZ3SolutionParser.UNKNOWN))
			return IZ3SolutionParser.UNKNOWN_ID;

		else if (line.startsWith("(error") || line.length() == 0)
			return IZ3SolutionParser.ERROR;

		return IZ3SolutionParser.VALUE_ID;
	}

	/**
	 * Láº¥y giÃ¡ trá»‹ cá»§a biáº¿n máº£ng, vá»›i giÃ¡ trá»‹ chá»‰ sá»‘ Ä‘Ã£ xÃ¡c
	 * Ä‘á»‹nh á»Ÿ hÃ m khÃ¡c
	 *
	 * @param ifThenElse
	 * @return
	 */
	private String getValueOfIte(String ifThenElse) {
		String value = "";
		Matcher m = Pattern.compile("(\\(=\\s\\w+!\\d+\\s\\d+\\)\\s*)+(.*)").matcher(ifThenElse);
		while (m.find()) {
			value = m.group(2).replace(") ", "").replace(" ", "");
			break;
		}
		return value;
	}

	/**
	 * Láº¥y giÃ¡ trá»‹ biáº¿n sá»‘ nguyÃªn, sá»‘ thá»±c
	 *
	 * @param src
	 * @return
	 */
	private String getValueOfVariable(String line) {
		String value = "";
		final String DEVIDE = "/";
		final String NEGATIVE = "-";
		/*
		 * Ex: (/ 1.0 10000.0))
		 */
		if (line.contains(DEVIDE) && !line.contains(NEGATIVE)) {
			int start = line.indexOf("(") + 1;
			int end = line.indexOf(")");
			String reducedLine = line.substring(start, end);

			String[] elements = reducedLine.split(" ");
			if (elements.length >= 3)
				value = elements[1] + elements[0] + elements[2];

		} else
		/*
		 * Ex: (- (/ 9981.0 10000.0))
		 */
		if (line.contains(DEVIDE) && line.contains(NEGATIVE)) {
			int start = line.lastIndexOf("(") + 1;
			int end = line.indexOf(")");
			String reducedLine = line.substring(start, end);

			String[] elements = reducedLine.split(" ");
			if (elements.length >= 3)
				value = NEGATIVE + "(" + elements[1] + elements[0] + elements[2] + ")";

		} else {
			Matcher m = Pattern.compile("(.*)\\)").matcher(line);
			while (m.find()) {
				value = m.group(1).replace(" ", "");
				break;
			}
		}
		return value;
	}
}
