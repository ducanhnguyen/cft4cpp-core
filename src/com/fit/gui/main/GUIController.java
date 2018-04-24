package com.fit.gui.main;

import java.awt.Component;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.gui.cfg.CFGVisualizer;
import com.fit.gui.swing.FileView;
import com.fit.gui.testedfunctions.ManageSelectedFunctionsDisplayer;
import com.fit.gui.testreport.object.ProjectReport;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.ISourceNavigable;
import com.fit.utils.Utils;

public class GUIController {
	final static Logger logger = Logger.getLogger(GUIController.class);
	private GUIView view;

	public GUIController(GUIView view) {
		this.view = view;
	}

	public void createMenuProjectRecent() {
		view.mnProjectRecent.removeAll();
		if (ISettingv2.RECENT_PROJECTS.isEmpty()) {
			JRadioButtonMenuItem radioButtoMenuItem = new JRadioButtonMenuItem("Empty");
			view.mnProjectRecent.add(radioButtoMenuItem);
		} else {
			JRadioButtonMenuItem radioButtoMenuItem;
			int countOfProjectDisplay = 0;
			for (String loadedProject : ISettingv2.RECENT_PROJECTS) {
				String nameProject = new File(loadedProject).getName();
				radioButtoMenuItem = new JRadioButtonMenuItem(nameProject);
				radioButtoMenuItem.setIcon(new ImageIcon(GUIView.class.getResource("/image/node/FolderNode.png")));

				radioButtoMenuItem.addActionListener(e -> {
					try {
						if (new File(loadedProject).exists()) {
							new ProjectLoaderTask(new File(loadedProject), view).execute();
						} else {
							JOptionPane.showMessageDialog(view, "Dont found project!", "Error",
									JOptionPane.ERROR_MESSAGE);
							ISettingv2.RECENT_PROJECTS.remove(loadedProject);
							createMenuProjectRecent();
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});

				view.mnProjectRecent.add(radioButtoMenuItem);
				view.projectRecentGroup.add(radioButtoMenuItem);
				countOfProjectDisplay++;
				if (countOfProjectDisplay > Utils.toInt(AbstractSetting.getValue(ISettingv2.NUMBER_OF_PROJECT)))
					break;
			}
		}

	}

	/**
	 * Initialization
	 *
	 * @throws Exception
	 */
	public void initializeGuiElements() throws Exception {
		int margin = 50;
		view.setBounds(margin, margin, 1495, 796);

		view.chooserProject = new JFileChooser();
		view.chooserProject.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		ManageSelectedFunctionsDisplayer selectFunctionDisplayer = new ManageSelectedFunctionsDisplayer();
		selectFunctionDisplayer.setProjectReport(ProjectReport.getInstance());
		selectFunctionDisplayer.setVisible(false);
	}

	/**
	 * Open control flow graph
	 *
	 * @param fn
	 */
	public void openLogicCFG(IFunctionNode fn) {
		try {
			view.jtpCFG.openTab(fn.getNewType(), null, fn.toString(),
					CFGVisualizer.class.getConstructor(IFunctionNode.class, MouseListener.class, int.class, int.class),
					fn, view.cfgNodeClick, CFGVisualizer.LOGIC_CFG, 0);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Catch an error when generate CFG!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * View source code
	 *
	 * @param file
	 * @return
	 */
	public Component openSource(File file) {
		try {
			String realSourceCodeFile = "";
			switch (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT) {

			case ISettingv2.PROJECT_ECLIPSE: {
				realSourceCodeFile = file.getAbsolutePath().replace(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH,
						Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH);
				break;
			}
			case ISettingv2.PROJECT_DEV_CPP:
			case ISettingv2.PROJECT_CUSTOMMAKEFILE:
			case ISettingv2.PROJECT_CODEBLOCK:
			case ISettingv2.PROJECT_UNKNOWN_TYPE:
			case ISettingv2.PROJECT_VISUALSTUDIO:
				realSourceCodeFile = file.getAbsolutePath().replace(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH,
						Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);
				break;
			}

			return view.jtpFunctionConfiguration.openTab(file.getName(), null, realSourceCodeFile,
					FileView.class.getConstructor(File.class), new File(realSourceCodeFile));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Catch an error when open source code!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	public void openSource(ISourceNavigable sn) {
		File f = sn.getSourceFile();
		if (f != null) {
			FileView fv = (FileView) this.openSource(sn.getSourceFile());
			fv.setHightLight(sn.getNodeLocation());
		}
	}

	public void openStructureCFGatLevel(IFunctionNode fn, int levelCFG) {
		try {
			view.jtpCFG.openTab("Overview level:" + levelCFG + " " + fn.getNewType(), null, fn.toString(),
					CFGVisualizer.class.getConstructor(IFunctionNode.class, MouseListener.class, int.class, int.class),
					fn, view.cfgNodeClick, CFGVisualizer.OVERVIEW_CFG, levelCFG);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Catch an error when generate CFG!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setLookAndFeel(String nameLookAndFeel) {
		try {
			UIManager.setLookAndFeel(nameLookAndFeel);
			SwingUtilities.updateComponentTreeUI(view);
			AbstractSetting.setValue(ISettingv2.LOOK_AND_FEEL, nameLookAndFeel);
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(view, "The operating system does not support this layout!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void resetOld() {
		view.jtpCFG.closeAllTab();
		view.jtpFunctionConfiguration.closeAllTab();
		view.config_model.setRowCount(0);

		ProjectReport.getInstance().getSourcecodeFiles().removeAll(ProjectReport.getInstance().getSourcecodeFiles());
	}
}
