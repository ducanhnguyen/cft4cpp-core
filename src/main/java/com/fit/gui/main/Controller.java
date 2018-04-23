package com.fit.gui.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

import org.apache.log4j.Logger;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.config.Settingv2;
import com.fit.parser.projectparser.ProjectLoader;
import com.fit.tree.object.INode;
import com.fit.tree.object.IProjectNode;
import com.fit.utils.Utils;
import com.fit.utils.search.FolderNodeCondition;
import com.fit.utils.search.McppConditionInUnix;
import com.fit.utils.search.McppConditionInWin;
import com.fit.utils.search.Search;
import com.fit.utils.search.Z3ExecutionConditionInUnix;
import com.fit.utils.search.Z3ExecutionConditionInWin;

public class Controller {
	final static Logger logger = Logger.getLogger(Controller.class);
	private GUIView view;
	private GUIController guiController;

	public Controller(GUIView view) {
		this.view = view;
	}

	/**
	 * Create clone project
	 *
	 * @return
	 * @throws IOException
	 */
	public File createCloneProject(String path) throws IOException {
		String cloneProjectPath = "";

		try {
			// Create unique name for clone project
			cloneProjectPath = new File(new File(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH).getCanonicalPath())
					.getParentFile().getCanonicalPath() + File.separator + Paths.CURRENT_PROJECT.CLONE_PROJECT_NAME;

			while (new File(cloneProjectPath).exists()) {
				cloneProjectPath += "1";
			}

			// Clone project
			Utils.copyFolder(new File(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH), new File(cloneProjectPath));

			// Set "chmod 777" for the clone project
			new File(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH).setExecutable(true);
			new File(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH).setReadable(true);
			new File(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH).setWritable(true);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return new File(cloneProjectPath);
	}

	/**
	 * Get parsing project
	 *
	 * @return
	 * @throws IOException
	 */
	public File getParsingProject(int typeofProject) throws IOException {
		switch (typeofProject) {
		case ISettingv2.PROJECT_ECLIPSE: {
			return new File(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);
		}
		case ISettingv2.PROJECT_DEV_CPP:
		case ISettingv2.PROJECT_UNKNOWN_TYPE:
		case ISettingv2.PROJECT_CUSTOMMAKEFILE:
		case ISettingv2.PROJECT_VISUALSTUDIO:
		case ISettingv2.PROJECT_CODEBLOCK:
		default: {
			return new File(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH);
		}
		}
	}

	/**
	 * Auto configure path of z3, mcpp, and googletest
	 * 
	 * @throws IOException
	 */
	void autoConfigZ3AndMcppAndGoogleTest() throws IOException {
		/*
		 * Get current path of tool
		 */
		String pathCurrentOfTool = System.getProperty("user.dir");

		/*
		 * Setup path to Z3
		 */
		if (new File(AbstractSetting.getValue(ISettingv2.SOLVER_Z3_PATH)).exists()) {
			// nothing to do
		} else {
			IProjectNode z3FolderRoot = new ProjectLoader()
					.load(new File(pathCurrentOfTool + File.separator + "local"));
			List<INode> z3Nodes = null;
			if (Utils.isWindows()) {
				z3Nodes = Search.searchNodes(z3FolderRoot, new Z3ExecutionConditionInWin());
			} else if (Utils.isUnix()) {
				z3Nodes = Search.searchNodes(z3FolderRoot, new Z3ExecutionConditionInUnix());
			}
			if (z3Nodes.size() == 1) {
				String z3Path = Utils.normalizePath(z3Nodes.get(0).getAbsolutePath());
				AbstractSetting.setValue(ISettingv2.SOLVER_Z3_PATH, z3Path);
			}
		}

		/*
		 * Find mcpp.exe
		 */
		if (new File(AbstractSetting.getValue(ISettingv2.MCPP_EXE_PATH)).exists()) {
			// nothing to do
		} else {
			IProjectNode mcppFolderRoot = new ProjectLoader()
					.load(new File(pathCurrentOfTool + File.separator + "local"));
			List<INode> mcppNodes = null;
			if (Utils.isWindows()) {
				mcppNodes = Search.searchNodes(mcppFolderRoot, new McppConditionInWin());
			} else if (Utils.isUnix()) {
				mcppNodes = Search.searchNodes(mcppFolderRoot, new McppConditionInUnix());
			}
			if (mcppNodes.size() == 1) {
				String mcppPath = Utils.normalizePath(mcppNodes.get(0).getAbsolutePath());
				AbstractSetting.setValue(ISettingv2.MCPP_EXE_PATH, mcppPath);
			}
		}

		/*
		 * Google Test have an include folder and a src folder
		 */
		if (new File(AbstractSetting.getValue(ISettingv2.ORIGINAL_GOOGLE_TEST)).exists()) {
			// nothing to do
		} else {
			IProjectNode rootGoogleTestFolder = new ProjectLoader()
					.load(new File(pathCurrentOfTool + File.separator + "local"));
			List<INode> includeFolders = Search.searchNodes(rootGoogleTestFolder, new FolderNodeCondition(),
					File.separator + "include");

			List<INode> srcFolders = Search.searchNodes(rootGoogleTestFolder, new FolderNodeCondition(),
					File.separator + "src");

			if (includeFolders.size() == 0 && srcFolders.size() == 0)
				JOptionPane.showMessageDialog(view, "The google test is not available", "Error",
						JOptionPane.ERROR_MESSAGE);
			else {
				String originalGoogleTestPath = new File(includeFolders.get(0).getAbsolutePath()).getParentFile()
						.getCanonicalPath();
				AbstractSetting.setValue(ISettingv2.ORIGINAL_GOOGLE_TEST, originalGoogleTestPath);
			}
		}
	}

	public void initializeSettingFile() {
		// Initialize setting file if it does not exist
		if (!new File(AbstractSetting.getAbsoluteSettingPath()).exists()
				|| !AbstractSetting.getValue(ISettingv2.VERSION_NAME).equals(ISettingv2.VERSION)) {
			logger.info("setting.properties has been created");
			Settingv2.create();
		}
	}

	public void saveSetting() {
		AbstractSetting.setValue(ISettingv2.DEFAULT_CHARACTER_LOWER_BOUND, view.jpLowerASCII.getValue().toString());
		AbstractSetting.setValue(ISettingv2.DEFAULT_CHARACTER_UPPER_BOUND, view.jpUpperASCII.getValue().toString());
		AbstractSetting.setValue(ISettingv2.DEFAULT_NUMBER_LOWER_BOUND, view.jtfNumberLower.getText());
		AbstractSetting.setValue(ISettingv2.DEFAULT_NUMBER_UPPER_BOUND, view.jtfNumberUpper.getText());
		AbstractSetting.setValue(ISettingv2.SELECTED_SOLVING_STRATEGY,
				view.jcbSolvingStrategy.getSelectedItem().toString());
		AbstractSetting.setValue(ISettingv2.MAX_ITERATION_FOR_EACH_LOOP,
				view.jspMaximumIterations.getValue().toString());
	}

	/**
	 * Save in history path project
	 *
	 * @param pathProjectCurrent
	 */
	public void updateMenuProjectRecent(String pathProjectCurrent) {
		if (pathProjectCurrent != null && pathProjectCurrent != "" && new File(pathProjectCurrent).exists()) {
			if (ISettingv2.RECENT_PROJECTS.contains(pathProjectCurrent))
				ISettingv2.RECENT_PROJECTS.remove(pathProjectCurrent);

			ISettingv2.RECENT_PROJECTS.add(0, pathProjectCurrent);
		}

		guiController.createMenuProjectRecent();
	}

	/**
	 * Exit program
	 */
	public void exit() {
		if (JOptionPane.showConfirmDialog(view, "Are you sure to close this window?", "Question?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			AbstractSetting.setValue(ISettingv2.INPUT_PROJECT_PATH, "");

			// Save recent path project
			String valueInFile = "";
			int countOfProject = 0;
			for (String temp : ISettingv2.RECENT_PROJECTS) {
				valueInFile += temp + ";";
				countOfProject++;
				if (countOfProject > Utils.toInt(AbstractSetting.getValue(ISettingv2.NUMBER_OF_PROJECT)))
					break;
			}
			AbstractSetting.setValue(ISettingv2.LIST_PROJECT_OPENED, valueInFile);
			try {
				switch (Paths.CURRENT_PROJECT.TYPE_OF_PROJECT) {

				case ISettingv2.PROJECT_ECLIPSE: {
					Utils.deleteFileOrFolder(new File(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH));
					Utils.copyFolder(new File(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH),
							new File(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH));
					Utils.deleteFileOrFolder(new File(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH));
					break;
				}
				case ISettingv2.PROJECT_DEV_CPP: {
					Utils.deleteFileOrFolder(new File(Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH));
					break;
				}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(view,
						"Cannot delete the copy of project " + Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH, "Error",
						JOptionPane.ERROR_MESSAGE);

			} finally {
				System.exit(0);
			}
		}

	}

	/**
	 * Load configuration
	 *
	 * @throws Exception
	 */
	public void loadConfiguration() throws Exception {
		view.jcbSolvingStrategy.setSelectedItem(AbstractSetting.getValue(ISettingv2.SELECTED_SOLVING_STRATEGY));

		view.jtfNumberLower.setText(Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_NUMBER_LOWER_BOUND)) + "");

		view.jtfNumberUpper.setText(Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_NUMBER_UPPER_BOUND)) + "");

		view.jpUpperASCII.setValue(Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_CHARACTER_UPPER_BOUND)));

		view.jpLowerASCII.setValue(Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_CHARACTER_LOWER_BOUND)));

		view.jpSizeOfArray.setValue(Utils.toInt(AbstractSetting.getValue(ISettingv2.DEFAULT_TEST_ARRAY_SIZE)));

		view.jspMaximumIterations.setModel(new SpinnerNumberModel(
				Utils.toInt(AbstractSetting.getValue(ISettingv2.MAX_ITERATION_FOR_EACH_LOOP)), 1, 100, 1));
	}

	public void setGuiController(GUIController guiController) {
		this.guiController = guiController;
	}
}
