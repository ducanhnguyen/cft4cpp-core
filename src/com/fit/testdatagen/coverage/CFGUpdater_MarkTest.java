package com.fit.testdatagen.coverage;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.fit.cfg.ICFG;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.testdata.object.TestpathString_Marker;
import com.fit.tree.object.IFunctionNode;
import com.fit.utils.search.FunctionNodeCondition;
import com.fit.utils.search.Search;

public class CFGUpdater_MarkTest {
	@Test
	public void simple() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "simple()").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={", "line-in-function=2###offset=18###statement=int a = 0;",
				"line-in-function=3###offset=31###statement=int b, c;", "statement=}" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(2, cfg.getVisitedStatements().size());
	}

	@Test
	public void if_control_block_true_branch() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "if_control_block(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={",
				"line-in-function=2###offset=37###statement=a>1###control-block=if",
				"statement={###additional-code=true", "line-in-function=3###offset=45###statement=a++;",
				"statement=}###additional-code=true", "statement=}" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(2, cfg.getVisitedStatements().size());
		Assert.assertEquals(1, cfg.getVisitedBranches().size());
	}

	@Test
	public void if_control_block_false_branch() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "if_control_block(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={",
				"line-in-function=2###offset=37###statement=a>1###control-block=if", "statement=}" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(1, cfg.getVisitedStatements().size());
		Assert.assertEquals(1, cfg.getVisitedBranches().size());
	}

	@Test
	public void if_else_control_block() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "if_else_control_block(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={",
				"line-in-function=2###offset=42###statement=a>0###control-block=if",
				"statement={###additional-code=true", "statement={###additional-code=true",
				"line-in-function=5###offset=65###statement=a--;", "statement=}" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(2, cfg.getVisitedStatements().size());
		Assert.assertEquals(1, cfg.getVisitedBranches().size());
	}

	@Test
	public void if_else_control_block_2() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "if_else_control_block_2(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={###line-of-blockin-function=1",
				"line-in-function=2###offset=44###statement=a>0###control-block=if",
				"statement={###line-of-blockin-function=2", "line-in-function=3###offset=53###statement=a++;",
				"statement=}###line-of-blockin-function=2", "statement=}###line-of-blockin-function=1"

		};
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(2, cfg.getVisitedStatements().size());
		Assert.assertEquals(1, cfg.getVisitedBranches().size());
	}

	@Test
	public void if_else_control_block_3() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "if_else_control_block_2(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={###line-of-blockin-function=1",
				"line-in-function=2###offset=44###statement=a>0###control-block=if",
				"statement={###line-of-blockin-function=4", "line-in-function=5###offset=70###statement=a--;",
				"statement=}###line-of-blockin-function=4", "statement=}###line-of-blockin-function=1"

		};
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(2, cfg.getVisitedStatements().size());
		Assert.assertEquals(1, cfg.getVisitedBranches().size());
	}

	@Test
	public void if_else_control_block_4() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "if_else_control_block_3(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={###line-of-blockin-function=1",
				"line-in-function=2###offset=44###statement=a>0###control-block=if",
				"statement={###line-of-blockin-function=4", "line-in-function=5###offset=70###statement=a++;",
				"statement=}###line-of-blockin-function=4", "statement=}###line-of-blockin-function=1"

		};
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(2, cfg.getVisitedStatements().size());
		Assert.assertEquals(1, cfg.getVisitedBranches().size());
	}

	@Test
	public void if_else_control_block_5() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "if_else_control_block_3(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={###line-of-blockin-function=1",
				"line-in-function=2###offset=44###statement=a>0###control-block=if",
				"statement={###line-of-blockin-function=2", "line-in-function=3###offset=53###statement=a++;",
				"statement=}###line-of-blockin-function=2", "statement=}###line-of-blockin-function=1" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(2, cfg.getVisitedStatements().size());
		Assert.assertEquals(1, cfg.getVisitedBranches().size());
	}

	@Test
	public void for_control_block() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "for_control_block(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={",
				"statement={###additional-code=true###surrounding-control-block=for",
				"line-in-function=2###offset=39###statement=int i=0;", "line-in-function=2###offset=48###statement=i<a",
				"statement={", "line-in-function=3###offset=63###statement=a++;", "statement=}",
				"line-in-function=2###offset=53###statement=i++", "line-in-function=2###offset=48###statement=i<a",
				"statement=}###additional-code=true###surrounding-control-block=for", "statement=}" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();

		Assert.assertEquals(4, cfg.getVisitedStatements().size());

		System.out.println(cfg.getVisitedBranches());
		Assert.assertEquals(2, cfg.getVisitedBranches().size());
	}

	@Test
	public void for_control_block_2() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "for_control_block_2(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={", "line-in-function=2###offset=36###statement=int i = 0;",
				"statement={###additional-code=true###surrounding-control-block=for",
				"line-in-function=3###offset=56###statement=i<a", "statement={",
				"line-in-function=4###offset=71###statement=a++;", "statement=}",
				"line-in-function=3###offset=61###statement=i++", "line-in-function=3###offset=56###statement=i<a",
				"statement=}###additional-code=true###surrounding-control-block=for", "statement=}" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(4, cfg.getVisitedStatements().size());
	}

	@Test
	public void while_control_block() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "while_control_block(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={", "line-in-function=2###offset=43###statement=a<1", "statement={",
				"line-in-function=3###offset=53###statement=a--;", "statement=}",
				"line-in-function=2###offset=43###statement=a<1", "statement=}" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(2, cfg.getVisitedStatements().size());
	}

	@Test
	public void while_control_block_2() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "while_control_block(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={", "line-in-function=2###offset=43###statement=a<1",
				"statement=}" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(1, cfg.getVisitedStatements().size());
	}

	@Test
	public void do_control_block() {
		ProjectParser parser = new ProjectParser(new File("..\\ava\\data-test\\ducanh\\instrument"));
		IFunctionNode function = (IFunctionNode) Search
				.searchNodes(parser.getRootTree(), new FunctionNodeCondition(), "do_control_block(int)").get(0);

		// Create a test path
		TestpathString_Marker testpath = new TestpathString_Marker();
		String[] nodes = new String[] { "statement={", "statement={", "line-in-function=3###offset=41###statement=a--;",
				"statement=}",

				"line-in-function=4###offset=56###statement=a<1",

				"statement=}" };
		testpath.setEncodedTestpath(nodes);

		// Mapping
		ICFG cfg = function.generateCFGToFindStaticSolution();
		cfg.setFunctionNode(function);
		cfg.setIdforAllNodes();
		CFGUpdater_Mark updater = new CFGUpdater_Mark(testpath, cfg);
		cfg.resetVisitedStateOfNodes();
		updater.updateVisitedNodes();
		Assert.assertEquals(2, cfg.getVisitedStatements().size());
	}

	@Test
	public void try_catch() {
		// Some failures
		Assert.assertEquals(true, false);
	}
}
