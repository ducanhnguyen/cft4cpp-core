package com.fit.gui.main;

import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.parser.projectparser.ProjectParser;
import com.fit.tree.object.IProcessNotify;
import com.fit.tree.object.IProjectNode;
import com.fit.tree.object.Node;
import com.fit.utils.Utils;

/**
 * Load the given project
 *
 * @author DucAnh
 */
public class ProjectLoaderTask extends SwingWorker<IProjectNode, String> {
	final static Logger logger = Logger.getLogger(GUIView.class);

	private GUIView view;
	private File path;
	private JDialog dialog;
	private JPanel contentPane;
	private JLabel lblText = new JLabel("");
	private Controller controller;
	private GUIController guiController;

	private IProcessNotify notify = new IProcessNotify() {

		@Override
		public void notify(int status) {
		}

		@Override
		public void notify(String message) {
			ProjectLoaderTask.this.publish(message);
		}
	};

	public ProjectLoaderTask(File path, GUIView view) {
		this.path = path;
		this.view = view;
		controller = new Controller(view);
		guiController = new GUIController(view);
		controller.setGuiController(guiController);

		Thread thread = new Thread() {
			@Override
			public void run() {
				dialog = new JDialog(view, TITLE_JDIALOG, true);
				dialog.setResizable(false);
				dialog.setSize(400, 400);

				JLabel lblTitleInsideDialog = new JLabel(TEXT_INSIDE_JDIALOG);
				Border border0 = lblTitleInsideDialog.getBorder();
				lblTitleInsideDialog.setBorder(new CompoundBorder(border0, MARGIN_FOR_TITLE_LABEL));

				lblText.setFont(lblText.getFont().deriveFont((float) FONT_SIZE));
				lblText.setBorder(new CompoundBorder(lblText.getBorder(), MARGIN_FOR_TEXT_LABEL));

				JLabel lblProcess = new JLabel();
				lblProcess.setIcon(LOADING_PROJECT);
				lblProcess.setBorder(new CompoundBorder(lblProcess.getBorder(), MARGIN_FOR_PROCESS_LABEL));

				contentPane = new JPanel();
				contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
				contentPane.add(lblTitleInsideDialog);
				contentPane.add(lblProcess);
				contentPane.add(lblText);

				dialog.setContentPane(contentPane);
				dialog.setLocationRelativeTo(null);
				dialog.pack();
				dialog.setVisible(true);
			}
		};
		thread.start();
	}

	@Override
	protected IProjectNode doInBackground() throws Exception {
		Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH = path.getAbsolutePath();
		AbstractSetting.setValue(ISettingv2.INPUT_PROJECT_PATH, Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);
		logger.debug("original project: " + Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);

		logger.debug("Creating clone");
		Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH = controller.createCloneProject(path.getAbsolutePath())
				.getAbsolutePath();
		logger.debug("clone project: " + Paths.CURRENT_PROJECT.CLONE_PROJECT_PATH);

		Paths.CURRENT_PROJECT.TYPE_OF_PROJECT = Utils.getTypeOfProject(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);
		File parsingProject = controller.getParsingProject(Paths.CURRENT_PROJECT.TYPE_OF_PROJECT);

		if (parsingProject != null) {
			controller.updateMenuProjectRecent(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH);
			ProjectParser projectParser = new ProjectParser(parsingProject, notify);
			return projectParser.getRootTree();
		} else {
			return null;
		}
	}

	@Override
	protected void done() {
		if (dialog != null)
			dialog.dispose();
		try {
			// Display the tree of project
			IProjectNode current_project_root = get();

			if (current_project_root != null) {
				((Node) current_project_root).setName(new File(Paths.CURRENT_PROJECT.ORIGINAL_PROJECT_PATH).getName());
				ProjectExplorer pe = new GUIProjectExplorer(current_project_root, view);
				view.jspProjectTree.setViewportView(pe);

				guiController.resetOld();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void process(List<String> chunks) {
		if (lblText != null)
			lblText.setText(chunks.get(chunks.size() - 1));
	}

	public static final ImageIcon LOADING_PROJECT = new ImageIcon(
			GUIView.class.getResource(ImageConstant.LOADING_PROJECT));
	public static final Border MARGIN_FOR_TITLE_LABEL = new EmptyBorder(20, 30, 5, 30);
	public static final Border MARGIN_FOR_PROCESS_LABEL = new EmptyBorder(0, 30, 5, 30);
	public static final Border MARGIN_FOR_TEXT_LABEL = new EmptyBorder(10, 50, 30, 5);
	public static final String TITLE_JDIALOG = "Progress";

	public static final String TEXT_INSIDE_JDIALOG = "Loading project";
	public static final int FONT_SIZE = 12;
}