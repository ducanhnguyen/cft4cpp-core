package com.fit.gui.main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.alee.laf.WebLookAndFeel;
import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.gui.swing.LightTabbedPane;
import com.fit.gui.testedfunctions.ManageSelectedFunctionsDisplayer;
import com.fit.gui.testreport.object.ProjectReport;
import com.fit.tree.object.IFunctionNode;
import com.fit.tree.object.INode;
import com.fit.utils.Utils;

public class GUIView extends JFrame {
	final static Logger logger = Logger.getLogger(GUIView.class);

	private Controller controller = new Controller(this);
	private GUIController guiController = new GUIController(this);
	public DefaultTableModel config_model;
	public ArrayList<INode> listCurrentFunctions;
	public SettingDialog setting_dialog;
	public ButtonGroup projectRecentGroup;
	public JMenu mnProjectRecent;
	public JScrollPane jspProjectTree;
	public JFileChooser chooserProject;
	public LightTabbedPane jtpCFG;
	public LightTabbedPane jtpFunctionConfiguration;
	public JSpinner jpSizeOfArray;
	public final ButtonGroup buttonGroup = new ButtonGroup();
	public JButton jbManageSelectedFunctions;
	public JTable jtbVariableConfiguration;
	public JScrollPane jspVariableConfiguration;
	public JPanel jpFutherConfig;
	public JSpinner jpLowerASCII;
	public JSpinner jpUpperASCII;
	public JTextField jtfNumberLower;
	public JTextField jtfNumberUpper;
	public JComboBox jcbSolvingStrategy;

	/**
	 * Singleton pattern
	 */
	private static GUIView instance = null;

	public static final long serialVersionUID = 1L;
	public static int COLUMN_LOWER = 2, COLUMN_UPPER = 3;

	public static int NUMBER_LOAD_MENU = 0;

	public static GUIView getInstance() throws Exception {
		if (GUIView.instance == null)
			GUIView.instance = new GUIView();
		return GUIView.instance;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				WebLookAndFeel.install();
				WebLookAndFeel.initializeManagers();
				GUIView window = new GUIView();
				window.setVisible(true);
			} catch (Exception e) {

			}
		});
	}

	public MouseListener cfgNodeClick = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			// CFGNode n = (CFGNode) e.getComponent();
			// IASTFileLocation loc = n.getElement().getAstLocation();
			// if (loc == null)
			// return;
			//
			// IFunctionNode fn = ((CFGCanvas) n.getParent()).getFunction();
			// FileView fv = (FileView) GUIView.this
			// .openSource(fn.getSourceFile());
			// fv.setHightLight(loc);
		}

	};

	JSpinner jspMaximumIterations = new JSpinner();

	public GUIView() throws Exception {
		controller.setGuiController(guiController);
		controller.initializeSettingFile();
		controller.autoConfigZ3AndMcppAndGoogleTest();

		getContentPane().setBackground(Color.WHITE);
		setBackground(Color.LIGHT_GRAY);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				controller.saveSetting();
				controller.exit();
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(GUIView.class.getResource(ImageConstant.APPLICATION)));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("C++ Project Testing");
		setExtendedState(Frame.MAXIMIZED_BOTH);

		JSplitPane jsp = new JSplitPane();
		jsp.setDividerSize(2);

		JPanel jpLeft = new JPanel();
		jsp.setLeftComponent(jpLeft);

		jbManageSelectedFunctions = new JButton("View tested functions");
		ImageIcon testedFunctionIcon = GuiUtils.getScaledImage(ImageConstant.VIEW_FUNCTIONS, ImageConstant.WIDTH,
				ImageConstant.HEIGHT);
		jbManageSelectedFunctions.setIcon(testedFunctionIcon);
		jbManageSelectedFunctions.addActionListener(e -> {
			ManageSelectedFunctionsDisplayer.getInstance().setVisible(true);
			ManageSelectedFunctionsDisplayer.getInstance().changeState();
			try {
				ManageSelectedFunctionsDisplayer.getInstance().setProjectReport(ProjectReport.getInstance());
			} catch (Exception e1) {

			}
			ManageSelectedFunctionsDisplayer.getInstance().refresh();
		});
		JPanel jpProjectTree = new JPanel();
		jpProjectTree.setBackground(Color.WHITE);
		jspProjectTree = new JScrollPane(jpProjectTree);

		GroupLayout gl_jpLeft = new GroupLayout(jpLeft);
		gl_jpLeft.setHorizontalGroup(gl_jpLeft.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING,
						gl_jpLeft.createSequentialGroup().addContainerGap()
								.addComponent(jbManageSelectedFunctions, GroupLayout.PREFERRED_SIZE, 186,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap(203, Short.MAX_VALUE))
				.addComponent(jspProjectTree, GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE));
		gl_jpLeft.setVerticalGroup(gl_jpLeft.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_jpLeft.createSequentialGroup()
						.addComponent(jspProjectTree, GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(jbManageSelectedFunctions)
						.addContainerGap()));

		jpLeft.setLayout(gl_jpLeft);
		jsp.setDividerLocation(400);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(jsp,
				GroupLayout.DEFAULT_SIZE, 1489, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(jsp,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE));

		JSplitPane jspRight = new JSplitPane();
		jspRight.setDividerSize(2);
		jsp.setRightComponent(jspRight);

		jtpCFG = new LightTabbedPane(SwingConstants.TOP);
		jtpCFG.setOpaqueIfEmpty(true);
		jtpCFG.setMaxTab(Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_GUI_TAB)));
		jtpCFG.setBackground(Color.WHITE);
		jspRight.setLeftComponent(jtpCFG);

		jtpFunctionConfiguration = new LightTabbedPane(SwingConstants.TOP);
		jtpFunctionConfiguration.setMaxTab(Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_GUI_TAB)));
		jspRight.setRightComponent(jtpFunctionConfiguration);

		JSplitPane jspFunctionConfiguration = new JSplitPane();
		jspFunctionConfiguration.setDividerLocation(500);

		jspFunctionConfiguration.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jtpFunctionConfiguration.addTab("Configuration", null, jspFunctionConfiguration, null);

		jspVariableConfiguration = new JScrollPane();
		jspVariableConfiguration.setBorder(null);
		jspVariableConfiguration.getViewport().setBackground(Color.WHITE);
		jspFunctionConfiguration.setTopComponent(jspVariableConfiguration);

		jpFutherConfig = new JPanel();
		jpFutherConfig.setBackground(SystemColor.controlHighlight);
		jspFunctionConfiguration.setBottomComponent(jpFutherConfig);

		jpSizeOfArray = new JSpinner();
		jpSizeOfArray.setBounds(190, 8, 78, 20);
		jpSizeOfArray.addChangeListener(e -> {
			int value = (Integer) jpSizeOfArray.getValue();
			if (listCurrentFunctions != null && listCurrentFunctions.size() != 0)
				for (INode currentFunction : listCurrentFunctions)
					if (currentFunction != null && ((IFunctionNode) currentFunction).getFunctionConfig() != null)
						((IFunctionNode) currentFunction).getFunctionConfig().setSizeOfArray(value);

			AbstractSetting.setValue(ISettingv2.DEFAULT_TEST_ARRAY_SIZE, value);
		});
		jpFutherConfig.setLayout(null);

		JLabel jlbSizeOfArray = new JLabel("Size of array");
		jlbSizeOfArray.setBounds(15, 8, 107, 14);
		jlbSizeOfArray.setHorizontalAlignment(SwingConstants.LEFT);
		jlbSizeOfArray.setVerticalAlignment(SwingConstants.TOP);
		jlbSizeOfArray.setToolTipText("Define the size of array that does not specify the fixed size");
		jlbSizeOfArray.setText("Maximum size of array");
		jpFutherConfig.add(jlbSizeOfArray);
		jpSizeOfArray.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		jpFutherConfig.add(jpSizeOfArray);

		JLabel lblCharacterRange = new JLabel("Range of character");
		lblCharacterRange.setBounds(15, 42, 162, 14);
		lblCharacterRange.setToolTipText("ASCII from 32 to ");
		jpFutherConfig.add(lblCharacterRange);

		JLabel lblTo = new JLabel("to");
		lblTo.setBounds(254, 45, 19, 14);
		jpFutherConfig.add(lblTo);

		jpLowerASCII = new JSpinner();
		jpLowerASCII.setModel(new SpinnerNumberModel(32, 0, 126, 1));
		jpLowerASCII.setBounds(190, 39, 54, 20);

		jpFutherConfig.add(jpLowerASCII);

		jpUpperASCII = new JSpinner();
		jpUpperASCII.setModel(new SpinnerNumberModel(126, 0, 126, 1));
		jpUpperASCII.setBounds(283, 39, 55, 20);

		jpFutherConfig.add(jpUpperASCII);

		JLabel lblIntegerRange = new JLabel("Range of number");
		lblIntegerRange.setBounds(15, 74, 132, 14);
		jpFutherConfig.add(lblIntegerRange);

		jtfNumberLower = new JTextField();
		jtfNumberLower.setBounds(190, 70, 57, 20);
		jpFutherConfig.add(jtfNumberLower);
		jtfNumberLower.setColumns(10);

		JLabel label = new JLabel("to");
		label.setBounds(267, 73, 19, 14);
		jpFutherConfig.add(label);

		jtfNumberUpper = new JTextField();
		jtfNumberUpper.setBounds(286, 70, 54, 20);
		jpFutherConfig.add(jtfNumberUpper);
		jtfNumberUpper.setColumns(10);

		JLabel lblMaximumIterationsFor = new JLabel("Maximum iterations for each loop");
		lblMaximumIterationsFor.setBounds(15, 104, 172, 14);
		jpFutherConfig.add(lblMaximumIterationsFor);

		jspMaximumIterations.setBounds(190, 101, 78, 20);
		jpFutherConfig.add(jspMaximumIterations);

		jcbSolvingStrategy = new JComboBox(ISettingv2.SUPPORT_SOLVING_STRATEGIES);
		jcbSolvingStrategy.setMaximumRowCount(2);
		jcbSolvingStrategy.setBounds(190, 132, 152, 20);
		jpFutherConfig.add(jcbSolvingStrategy);

		JLabel lblSolvingStrategy = new JLabel("Solving strategy");
		lblSolvingStrategy.setBounds(15, 135, 172, 14);
		jpFutherConfig.add(lblSolvingStrategy);

		jtbVariableConfiguration = new JTable();
		jtbVariableConfiguration.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Name", "Type" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return (column == GUIView.COLUMN_LOWER || column == GUIView.COLUMN_UPPER)
						&& getValueAt(row, column) != null;
			}

		});
		config_model = (DefaultTableModel) jtbVariableConfiguration.getModel();
		jspVariableConfiguration.setViewportView(jtbVariableConfiguration);

		jspRight.setDividerLocation(500);
		getContentPane().setLayout(groupLayout);

		JMenuBar jmbMenuBar = new JMenuBar();
		setJMenuBar(jmbMenuBar);

		JMenu jmnFile = new JMenu("    File");
		jmbMenuBar.add(jmnFile);

		JMenuItem mntmOpen = new JMenuItem("Open Project");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOpen.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.OPEN_PROJECT)));

		mntmOpen.addActionListener(e -> {
			int result = chooserProject.showDialog(this, "Open project folder");
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					new ProjectLoaderTask(new File(chooserProject.getSelectedFile().getCanonicalPath()), this)
							.execute();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(GUIView.this, "Cannot load project", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		jmnFile.add(mntmOpen);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmExit.addActionListener(e -> controller.exit());

		mnProjectRecent = new JMenu("Project Recent");
		mnProjectRecent.setIcon(new ImageIcon(GUIView.class.getResource("/image/open-project.png")));
		jmnFile.add(mnProjectRecent);

		// Change this section
		projectRecentGroup = new ButtonGroup();

		if (GUIView.NUMBER_LOAD_MENU < 1)
			AbstractSetting.initialListProjectOpened();
		guiController.createMenuProjectRecent();
		GUIView.NUMBER_LOAD_MENU++;

		mntmExit.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.EXIT_APP)));
		jmnFile.add(mntmExit);

		JMenu jmnSettings = new JMenu("Settings");
		jmnSettings.setIcon(null);
		jmbMenuBar.add(jmnSettings);

		JMenuItem mntmConfiguration = new JMenuItem("Configuration");
		mntmConfiguration.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.CONFIGURATION)));
		mntmConfiguration.addActionListener(arg0 -> {
			if (setting_dialog == null)
				setting_dialog = new SettingDialog(GUIView.this);
			setting_dialog.setVisible(true);
		});
		jmnSettings.add(mntmConfiguration);

		JMenu jmnNewMenu = new JMenu("Look and Feel");
		jmnNewMenu.setIcon(new ImageIcon(GUIView.class.getResource(ImageConstant.LOOK_AND_FEEL)));
		buttonGroup.add(jmnNewMenu);
		jmnSettings.add(jmnNewMenu);

		// Change look and feel
		// 1
		JRadioButtonMenuItem rbtnMetalLookAndFeel = new JRadioButtonMenuItem("MetalLookAndFeel");
		rbtnMetalLookAndFeel.addItemListener(e -> {
			String tempLookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
			guiController.setLookAndFeel(tempLookAndFeel);
			ManageSelectedFunctionsDisplayer.getInstance().setLookAndFeel(tempLookAndFeel);
		});
		jmnNewMenu.add(rbtnMetalLookAndFeel);
		// 2
		JRadioButtonMenuItem rbtnNimbusLookAndFeel = new JRadioButtonMenuItem("NimbusLookAndFeel");
		rbtnNimbusLookAndFeel.addItemListener(e -> {
			String tempLookAndFeel = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
			guiController.setLookAndFeel(tempLookAndFeel);
			ManageSelectedFunctionsDisplayer.getInstance().setLookAndFeel(tempLookAndFeel);
		});
		jmnNewMenu.add(rbtnNimbusLookAndFeel);

		//
		ButtonGroup lookandFeelGroup = new ButtonGroup();
		lookandFeelGroup.add(rbtnMetalLookAndFeel);
		lookandFeelGroup.add(rbtnNimbusLookAndFeel);
		if (Utils.isWindows()) {
			// 4
			JRadioButtonMenuItem rbtnWindowsLookAndFeel = new JRadioButtonMenuItem("WindowsLookAndFeel");
			rbtnWindowsLookAndFeel.addItemListener(e -> {
				String tempLookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				guiController.setLookAndFeel(tempLookAndFeel);
				ManageSelectedFunctionsDisplayer.getInstance().setLookAndFeel(tempLookAndFeel);
			});
			jmnNewMenu.add(rbtnWindowsLookAndFeel);
			// 5
			JRadioButtonMenuItem rbtnWindowsClassicLookAndFeel = new JRadioButtonMenuItem("WindowsClassicLookAndFeel");
			rbtnWindowsClassicLookAndFeel.addItemListener(e -> {
				String tempLookAndFeel = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
				guiController.setLookAndFeel(tempLookAndFeel);
				ManageSelectedFunctionsDisplayer.getInstance().setLookAndFeel(tempLookAndFeel);
			});
			jmnNewMenu.add(rbtnWindowsClassicLookAndFeel);

			lookandFeelGroup.add(rbtnWindowsLookAndFeel);
			lookandFeelGroup.add(rbtnWindowsClassicLookAndFeel);
			lookandFeelGroup.setSelected(rbtnWindowsLookAndFeel.getModel(), true);
		} else if (Utils.isUnix()) {
			// 4
			JRadioButtonMenuItem rbtnUnixLookAndFeel = new JRadioButtonMenuItem("GTKLookAndFeel");
			rbtnUnixLookAndFeel.addItemListener(e -> {
				String tempLookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
				guiController.setLookAndFeel(tempLookAndFeel);
				ManageSelectedFunctionsDisplayer.getInstance().setLookAndFeel(tempLookAndFeel);
			});
			jmnNewMenu.add(rbtnUnixLookAndFeel);
			lookandFeelGroup.add(rbtnUnixLookAndFeel);
			lookandFeelGroup.setSelected(rbtnMetalLookAndFeel.getModel(), true);
		}

		guiController.initializeGuiElements();
		controller.loadConfiguration();
		/*
		 * Open FunctionDisplayer for the first time to avoid popup menu not
		 * showing
		 */
		try {
			ManageSelectedFunctionsDisplayer.getInstance().setVisible(true);
			ManageSelectedFunctionsDisplayer.getInstance().dispose();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
