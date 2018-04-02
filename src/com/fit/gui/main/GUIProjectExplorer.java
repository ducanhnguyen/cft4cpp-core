package com.fit.gui.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.fit.cfg.overviewgraph.IOverviewCFGMaxLevelComputation;
import com.fit.cfg.overviewgraph.OverviewCFGMaxLevelComputation;
import com.fit.config.AbstractSetting;
import com.fit.config.Bound;
import com.fit.config.FunctionConfig;
import com.fit.config.IFunctionConfig;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.exception.GUINotifyException;
import com.fit.exception.UnsupportedTypeException;
import com.fit.gui.swing.FileView;
import com.fit.gui.testedfunctions.ManageSelectedFunctionsDisplayer;
import com.fit.gui.testreport.object.ITestedFunctionReport;
import com.fit.gui.testreport.object.ProjectReport;
import com.fit.testdatagen.FastTestdataGeneration;
import com.fit.testdatagen.ITestdataGeneration;
import com.fit.testdatagen.MarsTestdataGeneration2;
import com.fit.testdatagen.TestdataGenerationThread;
import com.fit.testdatagen.ThreadManager;
import com.fit.tree.object.CustomASTNode;
import com.fit.tree.object.FunctionNode;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.tree.object.ISourceNavigable;
import com.fit.tree.object.SourcecodeFileNode;
import com.fit.utils.Utils;

public class GUIProjectExplorer extends ProjectExplorer {
	final static Logger logger = Logger.getLogger(GUIProjectExplorer.class);
	private GUIView view;
	private Controller controller;
	private GUIController guiController;

	private static final long serialVersionUID = 1L;

	public GUIProjectExplorer(INode root, GUIView view) throws Exception {
		super(root);
		this.view = view;
		controller = new Controller(view);
		guiController = new GUIController(view);
		controller.setGuiController(guiController);

		BasicConfigurator.configure();
		setFunctionNodeClickEvent();
		setMenuController();
	}

	public ArrayList<INode> retainSelect(Class<?> cls) {
		ArrayList<INode> list = new ArrayList<>();
		view.listCurrentFunctions = new ArrayList<>();

		for (INode n : getSelectedItems())
			if (cls.isAssignableFrom(n.getClass())) {
				list.add(n);
				view.listCurrentFunctions.add(n);
			}

		return list;
	}

	public void setFunctionNodeClickEvent() throws Exception {

		addItemClickListener((item, e) -> {
			if (e.getClickCount() == 1)

				if (item instanceof FunctionNode) {
					FunctionNode fn = (FunctionNode) item;
					guiController.openSource(fn);

					File f = new File(Utils.getFileNode(item).getAbsolutePath());
					if (f != null) {
						FileView fv = (FileView) guiController.openSource(f);
						fv.setHightLight(((CustomASTNode) item).getAST().getFileLocation());
					}

				} else {
					INode tmp = Utils.getFileNode(item);
					if (tmp != null)
						guiController.openSource(new File(tmp.getAbsolutePath()));
				}
		});
	}

	public void setMenuController() {
		setMenuHandle(new MenuHandle<INode>() {

			private JMenuItem openSource, removeFunction, openCFGLogic;
			private JMenu openOverviewCFG, generateTestdata;

			@Override
			public void accept(JPopupMenu t) {
				setOverviewCFG(t);
				setLogicCFG(t);
				setFunctionRemover(t);
				setTestdataGeneration(t);
				setSourcecodeViewer(t);
			}

			@Override
			public void acceptList(List<INode> items) {
				generateTestdata.setVisible(false);
				openSource.setVisible(false);
				openOverviewCFG.setVisible(false);
				openCFGLogic.setVisible(false);
				removeFunction.setVisible(false);

				for (INode n : items)
					if (n instanceof IFunctionNode) {
						generateTestdata.setVisible(true);
						openCFGLogic.setVisible(true);

						openOverviewCFG.removeAll();
						setOverviewLevel(n);
						openOverviewCFG.setVisible(true);

						setRemoveFunction(n);
						removeFunction.setVisible(true);
					}
			}

			private void setFunctionRemover(JPopupMenu t) {
				removeFunction = new JMenuItem("Remove from tested functions");
				removeFunction.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.REMOVE_FUNCTION)));

				removeFunction.addActionListener(e -> {
					GUIProjectExplorer.this.retainSelect(FunctionNode.class).forEach(fn -> {
						ProjectReport.getInstance().removeFunction((IFunctionNode) fn);
						ThreadManager.getInstance().remove(fn);
						ManageSelectedFunctionsDisplayer.getInstance().refresh();
					});
				});

				t.add(removeFunction);
			}

			private void setLogicCFG(JPopupMenu t) {
				openCFGLogic = new JMenuItem("Open CFG Logic");
				openCFGLogic.setToolTipText("Show the logic of CFG at the maximum level");
				openCFGLogic.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.VIEW_LOGIC_GRAPH)));

				openCFGLogic.addActionListener(e -> {
					GUIProjectExplorer.this.retainSelect(FunctionNode.class)
							.forEach(fn -> guiController.openLogicCFG((FunctionNode) fn));
				});

				t.add(openCFGLogic);
			}

			private void setOverviewCFG(JPopupMenu t) {
				openOverviewCFG = new JMenu("Open CFG Structure");
				openOverviewCFG.setToolTipText("Only show the structure of CFG, not be used for logic CFG");
				openOverviewCFG.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.VIEW_STRUCTURE_GRAPH)));

				t.add(openOverviewCFG);
			}

			/**
			 * Set up the maximum level of the overview CFG
			 *
			 * @param n
			 */
			private void setOverviewLevel(INode n) {
				IOverviewCFGMaxLevelComputation overviewCfgComputer = new OverviewCFGMaxLevelComputation(
						(IFunctionNode) n);
				overviewCfgComputer.computeMaxLevel();
				int maxLevelOfOverviewCfg = overviewCfgComputer.getMaxLevel();

				for (int i = 0; i < maxLevelOfOverviewCfg; i++) {
					final int temp = i + 1;
					JMenuItem openOverviewCFGLevelI = new JMenuItem("Level " + temp);
					try {
						openOverviewCFGLevelI.setIcon(new ImageIcon(GUIView.class
								.getResource(ImageConstant.STRUCTURE_LEVEL_PREFIX + "level" + temp + ".png")));
					} catch (Exception e) {
						System.out.println("Level of overview graph is exceed!");
					}
					openOverviewCFGLevelI.addActionListener(e -> {
						GUIProjectExplorer.this.retainSelect(FunctionNode.class)
								.forEach(fn -> guiController.openStructureCFGatLevel((FunctionNode) fn, temp));
					});
					openOverviewCFG.add(openOverviewCFGLevelI);
				}
			}

			/**
			 * Set item removeFunction enable/disable
			 *
			 * @param n
			 */
			private void setRemoveFunction(INode n) {
				if (n instanceof IFunctionNode)
					if (ProjectReport.getInstance().getFunction((IFunctionNode) n) != null)
						removeFunction.setEnabled(true);
					else
						removeFunction.setEnabled(false);
			}

			private void setSourcecodeViewer(JPopupMenu t) {
				openSource = new JMenuItem("View Sourcecode");
				openSource.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.VIEW_SOURCE)));
				openSource.addActionListener(e -> {
					List<INode> select = GUIProjectExplorer.this.getSelectedItems();

					if (select.size() == 1 && select.get(0) instanceof ISourceNavigable)
						guiController.openSource((ISourceNavigable) select.get(0));
					else
						GUIProjectExplorer.this.retainSelect(SourcecodeFileNode.class)
								.forEach(n -> guiController.openSource(((SourcecodeFileNode) n).getFile()));
				});
				t.add(openSource);
			}

			/**
			 * Check kind of project
			 * 
			 * @param pathProject
			 * @return
			 * @throws Exception
			 */
			private boolean isSupportProject(String pathProject) {
				boolean isSupport = false;

				if (Utils.isWindows()) {
					switch (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT) {
					case ISettingv2.PROJECT_DEV_CPP: {
						if (!new File(AbstractSetting.getValue(ISettingv2.GNU_GCC_PATH)).exists()
								|| !new File(AbstractSetting.getValue(ISettingv2.GNU_GPlusPlus_PATH)).exists()
								|| !new File(AbstractSetting.getValue(ISettingv2.GNU_MAKE_PATH)).exists()) {
							JOptionPane.showMessageDialog(view, "This is a Dev-Cpp project. Please configure gcc/g++!",
									"Warning", JOptionPane.WARNING_MESSAGE);
							isSupport = false;
						} else {
							isSupport = true;
						}
						break;
					}
					case ISettingv2.PROJECT_CODEBLOCK: {
						JOptionPane.showMessageDialog(view, "Doesn't support for project written in CodeBlock!",
								"Warning", JOptionPane.WARNING_MESSAGE);
						isSupport = false;
						break;
					}
					case ISettingv2.PROJECT_VISUALSTUDIO: {
						if (!new File(AbstractSetting.getValue(ISettingv2.MSBUILD_PATH)).exists()) {
							JOptionPane.showMessageDialog(view,
									"This is a Visual studio project. Please configure MsBuild!", "Warning",
									JOptionPane.WARNING_MESSAGE);
							isSupport = false;
						} else {
							isSupport = true;
						}
						break;
					}
					case ISettingv2.PROJECT_ECLIPSE: {
						String eclipsePath = AbstractSetting.getValue(ISettingv2.ECLIPSE_PATH);
						logger.debug("Eclipse path: " + eclipsePath + "[" + new File(ISettingv2.ECLIPSE_PATH).exists()
								+ "]");

						File metadataFolder = new File(
								new File(pathProject).getParentFile().getAbsolutePath() + File.separator + ".metadata");
						if (!metadataFolder.exists()) {
							JOptionPane.showMessageDialog(view,
									"This is a Eclipse project. Dont found .metadata folder in the given project",
									"Warning", JOptionPane.WARNING_MESSAGE);
							isSupport = false;
						} else if (!new File(eclipsePath).exists()) {
							JOptionPane.showMessageDialog(view,
									"This is a Eclipse project. Please configure path to eclipse!", "Warning",
									JOptionPane.WARNING_MESSAGE);
							isSupport = false;
						} else {
							isSupport = true;
						}
						break;
					}
					case ISettingv2.PROJECT_CUSTOMMAKEFILE: {
						isSupport = true;
						break;
					}

					case ISettingv2.PROJECT_UNKNOWN_TYPE: {
						JOptionPane.showMessageDialog(view, "Cannot detect kind of this project", "Warning",
								JOptionPane.WARNING_MESSAGE);
						isSupport = false;
						break;
					}
					}
				} else if (Utils.isUnix()) {

					switch (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT) {
					case ISettingv2.PROJECT_DEV_CPP: {
						JOptionPane.showMessageDialog(view, "Can not run Dev-Cpp project in Unix", "Warning",
								JOptionPane.WARNING_MESSAGE);
						isSupport = false;
						break;
					}
					case ISettingv2.PROJECT_CODEBLOCK: {
						JOptionPane.showMessageDialog(view, "Can not run Code Block project in Unix", "Warning",
								JOptionPane.WARNING_MESSAGE);
						isSupport = false;
						break;
					}
					case ISettingv2.PROJECT_VISUALSTUDIO: {
						JOptionPane.showMessageDialog(view, "Can not run Visual Studio project in Unix", "Warning",
								JOptionPane.WARNING_MESSAGE);
						isSupport = false;
						break;
					}
					case ISettingv2.PROJECT_ECLIPSE: {
						String eclipsePath = AbstractSetting.getValue(ISettingv2.ECLIPSE_PATH);
						logger.debug("Eclipse path: " + eclipsePath + "[" + new File(ISettingv2.ECLIPSE_PATH).exists()
								+ "]");

						File metadataFolder = new File(
								new File(pathProject).getParentFile().getAbsolutePath() + File.separator + ".metadata");
						if (!metadataFolder.exists()) {
							JOptionPane.showMessageDialog(view, "Dont found .metadata folder in the given project",
									"Warning", JOptionPane.WARNING_MESSAGE);
							isSupport = false;
						} else if (!new File(eclipsePath).exists()) {
							JOptionPane.showMessageDialog(view,
									"This is a Eclipse project. Please configure path to eclipse!", "Warning",
									JOptionPane.WARNING_MESSAGE);
							isSupport = false;
						} else {
							isSupport = true;
						}
						break;
					}
					case ISettingv2.PROJECT_CUSTOMMAKEFILE: {
						isSupport = true;
						break;
					}
					case ISettingv2.PROJECT_UNKNOWN_TYPE: {
						JOptionPane.showMessageDialog(view, "Cannot detect kind of this project", "Warning",
								JOptionPane.WARNING_MESSAGE);
						isSupport = false;
						break;
					}
					}

				}
				return isSupport;
			}

			private boolean checkZ3() {
				boolean isConfigured = false;
				if (!new File(AbstractSetting.getValue(ISettingv2.SOLVER_Z3_PATH)).exists()) {
					isConfigured = false;
					JOptionPane.showMessageDialog(view, "Do not found Z3", "Error", JOptionPane.ERROR_MESSAGE);
				} else
					isConfigured = true;
				return isConfigured;
			}

			private boolean checkMcpp() {
				boolean isConfigured = false;
				if (!new File(AbstractSetting.getValue(ISettingv2.MCPP_EXE_PATH)).exists()) {
					isConfigured = false;
					JOptionPane.showMessageDialog(view, "Do not found mcpp", "Error", JOptionPane.ERROR_MESSAGE);
				} else
					isConfigured = true;
				return isConfigured;
			}

			private void setTestdataGeneration(JPopupMenu t) {
				generateTestdata = new JMenu("Generate test data");
				generateTestdata.setIcon(GuiUtils.getScaledImage(ImageConstant.GENERATE_TESTDATA, ImageConstant.WIDTH,
						ImageConstant.HEIGHT));

				// Create menu item when clicking right mouse
				JMenuItem[] jmiCoverages = new JMenuItem[2];
				jmiCoverages[0] = new JMenuItem("Statement coverage");
				jmiCoverages[0].setIcon(
						GuiUtils.getScaledImage(ImageConstant.STATEMENT, ImageConstant.WIDTH, ImageConstant.HEIGHT));
				jmiCoverages[1] = new JMenuItem("Branch coverage");
				jmiCoverages[1].setIcon(
						GuiUtils.getScaledImage(ImageConstant.BRANCH, ImageConstant.WIDTH, ImageConstant.HEIGHT));

				generateTestdata.add(jmiCoverages[0]);
				generateTestdata.add(jmiCoverages[1]);

				// Action when clicking
				for (JMenuItem coverage : jmiCoverages)
					coverage.addActionListener(e -> {
						if (isSupportProject(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH) && checkZ3() && checkMcpp())
							GUIProjectExplorer.this.retainSelect(FunctionNode.class).forEach(n -> {
								guiController.openStructureCFGatLevel((FunctionNode) n, 1);

								/*
								 * Add function to the list of the tested functions
								 */
								if (ProjectReport.getInstance().getFunction((IFunctionNode) n) == null) {
									/*
									 * Lưu cấu hình kiểm thử
									 */
									FunctionConfig fnConfig = new FunctionConfig();
									// coverage
									if (coverage.getText().toLowerCase().contains("branch"))
										fnConfig.setTypeofCoverage(IFunctionConfig.BRANCH_COVERAGE);
									else if (coverage.getText().toLowerCase().contains("statement"))
										fnConfig.setTypeofCoverage(IFunctionConfig.STATEMENT_COVERAGE);
									else if (coverage.getText().toLowerCase().contains("sub"))
										fnConfig.setTypeofCoverage(IFunctionConfig.SUBCONDITION);

									// size of array
									fnConfig.setSizeOfArray(Utils.toInt(view.jpSizeOfArray.getValue() + ""));

									// bound
									fnConfig.setCharacterBound(
											new Bound(Utils.toInt(view.jpLowerASCII.getValue().toString()),
													Utils.toInt(view.jpUpperASCII.getValue().toString())));

									fnConfig.setIntegerBound(new Bound(Utils.toInt(view.jtfNumberLower.getText()),
											Utils.toInt(view.jtfNumberUpper.getText())));

									// max loop
									fnConfig.setMaximumInterationsForEachLoop(
											(Integer) view.jspMaximumIterations.getValue());

									// solving strategy
									fnConfig.setSolvingStrategy(view.jcbSolvingStrategy.getSelectedItem().toString());

									((FunctionNode) n).setFunctionConfig(fnConfig);

									// add to the project
									// report
									ProjectReport.getInstance().addFunction((FunctionNode) n);
									ProjectReport.getInstance().getFunction((IFunctionNode) n).setState("initializing");

									TestdataGenerationThread t2 = new TestdataGenerationThread(() -> {

										ITestedFunctionReport functionReport = ProjectReport.getInstance()
												.getFunction((IFunctionNode) n);

										try {
											switch (AbstractSetting.getValue(ISettingv2.TESTDATA_STRATEGY)) {
											case ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.MARS2 + "":
												new MarsTestdataGeneration2((IFunctionNode) n, functionReport)
														.generateTestdata();
												break;
											case ITestdataGeneration.TESTDATA_GENERATION_STRATEGIES.FAST_MARS + "":
												new FastTestdataGeneration((IFunctionNode) n, functionReport)
														.generateTestdata();
												break;

											default:
												throw new Exception("Wrong test data generation strategy");
											}

											ProjectReport.getInstance().getFunction((IFunctionNode) n)
													.setState("finished");
										} catch (UnsupportedTypeException e11) {
											JOptionPane.showMessageDialog(view, e11.getMessage(), "Error",
													JOptionPane.ERROR_MESSAGE);
											ProjectReport.getInstance().removeFunction((IFunctionNode) n);
											e11.printStackTrace();

										} catch (GUINotifyException e12) {
											JOptionPane.showMessageDialog(view, e12.getMessage(), "Error",
													JOptionPane.ERROR_MESSAGE);
											ProjectReport.getInstance().getFunction((IFunctionNode) n)
													.setState("error");

											e12.printStackTrace();
										} catch (Exception e13) {
											e13.printStackTrace();
										}
									});
									t2.setFunctionNode(n);
									ThreadManager.getInstance().add(t2);
								} else
									JOptionPane.showMessageDialog(view, "This function has existed!", "Information",
											JOptionPane.INFORMATION_MESSAGE);
							});
					});
				t.add(generateTestdata);
			}
		});
	}

}
