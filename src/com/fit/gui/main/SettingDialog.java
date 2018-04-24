package com.fit.gui.main;

import com.alee.laf.button.WebButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.spinner.WebSpinner;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.text.WebTextField;
import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.parser.makefile.CompilerFolderParser;
import com.fit.parser.projectparser.ProjectLoader;
import com.fit.tree.object.INode;
import com.fit.tree.object.IProjectNode;
import com.fit.utils.Utils;
import com.fit.utils.search.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class SettingDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JTabbedPane tabPanel = new WebTabbedPane();
    private JCheckBox entry_cfg_line_max;
    private JSpinner entry_cfg_line;
    private JTextField jtfCompiler;
    private JSpinner entry_cfg_width;
    private JSpinner entry_cfg_margin_x;
    private JSpinner entry_cfg_margin_y;
    private JTextField jtfZ3Path;
    private JTextField jtfMcpp;
    private JTextField jtfGoogleTest;
    private JTextField jtfMsBuildPath;
    private JTextField jtfEclipsePath;
    private JFileChooser chooser = new JFileChooser();
    private JTextField jtfMingwmake;
    private JTextField jtfGcc;
    private JTextField jtfGplusplus;
    private JTextField jtfZ3Name;
    private JTextField jtfMcppName;
    /**
     * Create the dialog.
     */
    public SettingDialog(Frame owner) {
        super(owner, "Settings", true);

        setIconImage(Toolkit.getDefaultToolkit().getImage(SettingDialog.class.getResource("/image/setting.png")));
        this.setBounds(100, 100, 600, 562);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabPanel, BorderLayout.CENTER);

        setupEnvironment();

        setupSolver();

        setupMcpp();

        setupGoogleTest();

        setupGraphics();

        setupNegativeButton();

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((size.width - getWidth()) / 2, (size.height - getHeight()) / 2);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel(
                    // "javax.swing.plaf.metal.MetalLookAndFeel"
                    // "javax.swing.plaf.nimbus.NimbusLookAndFeel"
                    // "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
                    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
                    // "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"
            );

            SettingDialog dialog = new SettingDialog(null);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {

        }
    }

    /**
     * Ghi cÃ i Ä‘áº·t vÃ o há»‡ thá»‘ng
     */
    void applySettings() {
        /*
		 * Graphics tab
		 */
        if (entry_cfg_line_max.isSelected())
            AbstractSetting.setValue(ISettingv2.MAX_CFG_NODE_LINE, 0);
        else
            AbstractSetting.setValue(ISettingv2.MAX_CFG_NODE_LINE, ((Number) entry_cfg_line.getValue()).intValue());

        AbstractSetting.setValue(ISettingv2.MAX_CFG_NODE_WIDTH, ((Number) entry_cfg_width.getValue()).intValue());
        AbstractSetting.setValue(ISettingv2.CFG_MARGIN_X, ((Number) entry_cfg_margin_x.getValue()).intValue());
        AbstractSetting.setValue(ISettingv2.CFG_MARGIN_Y, ((Number) entry_cfg_margin_y.getValue()).intValue());

		/*
		 * Mcpp tab
		 */
        AbstractSetting.setValue(ISettingv2.MCPP_NAME, jtfMcppName.getText());
        AbstractSetting.setValue(ISettingv2.MCPP_EXE_PATH, jtfMcpp.getText());

		/*
		 * Solver tab
		 */
        AbstractSetting.setValue(ISettingv2.SOLVER_Z3_NAME, jtfZ3Name.getText());
        AbstractSetting.setValue(ISettingv2.SOLVER_Z3_PATH, jtfZ3Path.getText());

		/*
		 * Environment tab
		 */
        AbstractSetting.setValue(ISettingv2.GNU_GCC_NAME, jtfGcc.getText());
        AbstractSetting.setValue(ISettingv2.GNU_GPlusPlus_NAME, jtfGplusplus.getText());
        AbstractSetting.setValue(ISettingv2.GNU_MAKE_NAME, jtfMingwmake.getText());
        AbstractSetting.setValue(ISettingv2.GNU_GENERAL, jtfCompiler.getText());

        AbstractSetting.setValue(ISettingv2.MSBUILD_PATH, jtfMsBuildPath.getText());
    }

    /**
     * Náº¡p cÃ¡c cÃ i Ä‘áº·t tá»« há»‡ thá»‘ng
     */
    void loadSettings() {
		/*
		 * Mcpp tab
		 */
        jtfMcpp.setText(AbstractSetting.getValue(ISettingv2.MCPP_EXE_PATH));
        jtfMcppName.setText(AbstractSetting.getValue(ISettingv2.MCPP_NAME));
		/*
		 * Solver tab
		 */
        jtfZ3Name.setText(AbstractSetting.getValue(ISettingv2.SOLVER_Z3_NAME));
        jtfZ3Path.setText(AbstractSetting.getValue(ISettingv2.SOLVER_Z3_PATH));

		/*
		 * Environment tab
		 */
        jtfGcc.setText(AbstractSetting.getValue(ISettingv2.GNU_GCC_NAME));
        jtfMingwmake.setText(AbstractSetting.getValue(ISettingv2.GNU_MAKE_NAME));
        jtfGplusplus.setText(AbstractSetting.getValue(ISettingv2.GNU_GPlusPlus_NAME));
        jtfCompiler.setText(AbstractSetting.getValue(ISettingv2.GNU_GENERAL));

        jtfMsBuildPath.setText(AbstractSetting.getValue(ISettingv2.MSBUILD_PATH));

		/*
		 * Graphics tab
		 */
        boolean isAllLine = Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_CFG_NODE_LINE)) == 0;
        entry_cfg_line_max.setSelected(isAllLine);
        entry_cfg_line.setEnabled(!isAllLine);
        if (!isAllLine)
            entry_cfg_line.setValue(Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_CFG_NODE_LINE)));

        entry_cfg_width.setValue(Integer.parseInt(AbstractSetting.getValue(ISettingv2.MAX_CFG_NODE_WIDTH)));
        entry_cfg_margin_x.setValue(Integer.parseInt(AbstractSetting.getValue(ISettingv2.CFG_MARGIN_X)));
        entry_cfg_margin_y.setValue(Integer.parseInt(AbstractSetting.getValue(ISettingv2.CFG_MARGIN_Y)));

    }

    private void resizeComponentWidth(Component c, int width) {
        Dimension d = c.getPreferredSize();
        d.width = width;
        c.setPreferredSize(d);
    }

    @Override
    public void setVisible(boolean b) {
        if (b)
            try {
                loadSettings();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        super.setVisible(b);
    }

    /**
     * Kiá»ƒm tra tÃ­nh Ä‘Ãºng Ä‘áº¯n cá»§a cÃ¡c cÃ i Ä‘áº·t trÆ°á»›c khi lÆ°u
     *
     * @throws Exception giÃ¡ trá»‹ cÃ i sai
     */
    void validateSettings() throws Exception {
        final String NO_CONTENT = "";

		/*
		 * Mcpp tab
		 */
        if (!new File(jtfMcpp.getText()).exists() || jtfMcppName.getText().equals(NO_CONTENT))
            throw new Exception("Found error in Mcpp configuration!");

		/*
		 * Solver tab
		 */
        if (!new File(jtfZ3Path.getText()).exists() || jtfZ3Name.getText().equals(NO_CONTENT))
            throw new Exception("Found error in Solver configuration!");

		/*
		 * Environment tab
		 */
        if (new File(jtfCompiler.getText()).exists() || new File(jtfMsBuildPath.getText()).exists()) {
            // no thing to do
        } else
            throw new Exception("Found error in Environment configuration!");
    }

    private void setupNegativeButton() {

        JPanel buttonPane = new WebPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        {
            JButton okButton = new WebButton("OK");
            okButton.addActionListener(e -> {
                try {
                    SettingDialog.this.validateSettings();
                    SettingDialog.this.applySettings();
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    SettingDialog.this.dispose();

                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(SettingDialog.this, e1.getMessage(), "Validating error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
            okButton.setActionCommand("OK");
            buttonPane.add(okButton);
            getRootPane().setDefaultButton(okButton);
        }
        {
            JButton cancelButton = new WebButton("Cancel");
            cancelButton.addActionListener(e -> {
                JDialog.setDefaultLookAndFeelDecorated(true);
                SettingDialog.this.dispose();
            });
            cancelButton.setActionCommand("Cancel");
            buttonPane.add(cancelButton);
        }

    }

    private void setupGraphics() {

        JPanel panel = new WebPanel();

        JScrollPane scrollPane_graphic = new WebScrollPane(panel);
        scrollPane_graphic.setBorder(null);
        tabPanel.addTab("Graphics", null, scrollPane_graphic, null);
        {

            panel.setBackground(Color.WHITE);
            scrollPane_graphic.setViewportView(panel);
            GridBagLayout gbl_panel = new GridBagLayout();
            gbl_panel.columnWidths = new int[]{30, 150, 0, 0, 30, 0};
            gbl_panel.rowHeights = new int[]{10, 30, 30, 30, 30, 30, 0, 0};
            gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
            gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
            panel.setLayout(gbl_panel);
            {
                JLabel lblCfgDisplay = new WebLabel("CFG display");
                lblCfgDisplay
                        .setFont(lblCfgDisplay.getFont().deriveFont(lblCfgDisplay.getFont().getStyle() | Font.BOLD));
                GridBagConstraints gbc_lblCfgDisplay = new GridBagConstraints();
                gbc_lblCfgDisplay.anchor = GridBagConstraints.SOUTHWEST;
                gbc_lblCfgDisplay.insets = new Insets(0, 0, 5, 5);
                gbc_lblCfgDisplay.gridx = 1;
                gbc_lblCfgDisplay.gridy = 1;
                panel.add(lblCfgDisplay, gbc_lblCfgDisplay);
            }
            {
                JLabel lblMaxLinesIn = new WebLabel("Max lines in node");
                GridBagConstraints gbc_lblMaxLinesIn = new GridBagConstraints();
                gbc_lblMaxLinesIn.anchor = GridBagConstraints.WEST;
                gbc_lblMaxLinesIn.insets = new Insets(0, 0, 5, 5);
                gbc_lblMaxLinesIn.gridx = 1;
                gbc_lblMaxLinesIn.gridy = 2;
                panel.add(lblMaxLinesIn, gbc_lblMaxLinesIn);
            }
            {
                entry_cfg_line_max = new WebCheckBox("All lines");
                entry_cfg_line_max.addActionListener(e -> entry_cfg_line.setEnabled(!entry_cfg_line_max.isSelected()));
                {
                    entry_cfg_line = new WebSpinner();
                    entry_cfg_line
                            .setModel(new SpinnerNumberModel(new Integer(5), new Integer(1), null, new Integer(1)));
                    resizeComponentWidth(entry_cfg_line, 54);
                    GridBagConstraints gbc_entry_cfg_line = new GridBagConstraints();
                    gbc_entry_cfg_line.anchor = GridBagConstraints.WEST;
                    gbc_entry_cfg_line.insets = new Insets(0, 0, 5, 5);
                    gbc_entry_cfg_line.gridx = 2;
                    gbc_entry_cfg_line.gridy = 2;
                    panel.add(entry_cfg_line, gbc_entry_cfg_line);
                }
                GridBagConstraints gbc_entry_cfg_line_max = new GridBagConstraints();
                gbc_entry_cfg_line_max.anchor = GridBagConstraints.WEST;
                gbc_entry_cfg_line_max.insets = new Insets(0, 10, 5, 5);
                gbc_entry_cfg_line_max.gridx = 3;
                gbc_entry_cfg_line_max.gridy = 2;
                panel.add(entry_cfg_line_max, gbc_entry_cfg_line_max);
            }
            {
                JLabel lblMaxCharactersIn = new WebLabel("Max characters in node");
                GridBagConstraints gbc_lblMaxCharactersIn = new GridBagConstraints();
                gbc_lblMaxCharactersIn.anchor = GridBagConstraints.WEST;
                gbc_lblMaxCharactersIn.insets = new Insets(0, 0, 5, 5);
                gbc_lblMaxCharactersIn.gridx = 1;
                gbc_lblMaxCharactersIn.gridy = 3;
                panel.add(lblMaxCharactersIn, gbc_lblMaxCharactersIn);
            }
            {
                entry_cfg_width = new WebSpinner();
                entry_cfg_width
                        .setModel(new SpinnerNumberModel(new Integer(30), new Integer(10), null, new Integer(10)));
                resizeComponentWidth(entry_cfg_width, 54);
                GridBagConstraints gbc_entry_cfg_width = new GridBagConstraints();
                gbc_entry_cfg_width.insets = new Insets(0, 0, 5, 5);
                gbc_entry_cfg_width.gridx = 2;
                gbc_entry_cfg_width.gridy = 3;
                panel.add(entry_cfg_width, gbc_entry_cfg_width);
            }
            {
                JLabel lblNodeMarginX = new WebLabel("Node margin X");
                GridBagConstraints gbc_lblNodeMarginX = new GridBagConstraints();
                gbc_lblNodeMarginX.anchor = GridBagConstraints.WEST;
                gbc_lblNodeMarginX.insets = new Insets(0, 0, 5, 5);
                gbc_lblNodeMarginX.gridx = 1;
                gbc_lblNodeMarginX.gridy = 4;
                panel.add(lblNodeMarginX, gbc_lblNodeMarginX);
            }
            {
                entry_cfg_margin_x = new WebSpinner();
                entry_cfg_margin_x
                        .setModel(new SpinnerNumberModel(new Integer(120), new Integer(50), null, new Integer(5)));
                GridBagConstraints gbc_entry_cfg_margin_x = new GridBagConstraints();
                gbc_entry_cfg_margin_x.anchor = GridBagConstraints.WEST;
                gbc_entry_cfg_margin_x.gridwidth = 2;
                gbc_entry_cfg_margin_x.insets = new Insets(0, 0, 5, 5);
                gbc_entry_cfg_margin_x.gridx = 2;
                gbc_entry_cfg_margin_x.gridy = 4;
                panel.add(entry_cfg_margin_x, gbc_entry_cfg_margin_x);
            }
            {
                JLabel lblNodeMarginY = new WebLabel("Node margin Y");
                GridBagConstraints gbc_lblNodeMarginY = new GridBagConstraints();
                gbc_lblNodeMarginY.anchor = GridBagConstraints.WEST;
                gbc_lblNodeMarginY.insets = new Insets(0, 0, 5, 5);
                gbc_lblNodeMarginY.gridx = 1;
                gbc_lblNodeMarginY.gridy = 5;
                panel.add(lblNodeMarginY, gbc_lblNodeMarginY);
            }
            {
                entry_cfg_margin_y = new WebSpinner();
                entry_cfg_margin_y
                        .setModel(new SpinnerNumberModel(new Integer(50), new Integer(50), null, new Integer(5)));
                GridBagConstraints gbc_entry_cfg_margin_y = new GridBagConstraints();
                gbc_entry_cfg_margin_y.anchor = GridBagConstraints.WEST;
                gbc_entry_cfg_margin_y.gridwidth = 2;
                gbc_entry_cfg_margin_y.insets = new Insets(0, 0, 5, 5);
                gbc_entry_cfg_margin_y.gridx = 2;
                gbc_entry_cfg_margin_y.gridy = 5;
                panel.add(entry_cfg_margin_y, gbc_entry_cfg_margin_y);
            }
        }

    }

    private void setupGoogleTest() {
        JPanel panel = new WebPanel();
        panel.setBackground(Color.WHITE);

        JScrollPane scrollPane_googleTest = new JScrollPane();
        scrollPane_googleTest.setBorder(null);
        scrollPane_googleTest.setViewportView(panel);
        tabPanel.addTab("Google test", null, scrollPane_googleTest, null);
        panel.setLayout(null);

        {

            {
                jtfGoogleTest = new WebTextField(AbstractSetting.getValue(ISettingv2.ORIGINAL_GOOGLE_TEST));
                jtfGoogleTest.setBounds(107, 151, 372, 24);
                jtfGoogleTest.setBackground(Color.WHITE);
                jtfGoogleTest.setOpaque(true);
                panel.add(jtfGoogleTest);
                jtfGoogleTest.setColumns(10);
            }
        }
        JButton btnSelectGoogleTest = new WebButton("Select...");
        btnSelectGoogleTest.setBounds(484, 151, 59, 24);
        btnSelectGoogleTest.addActionListener(e -> {
            chooser.setDialogTitle(jtfGoogleTest.getText());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(SettingDialog.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                SettingDialog.this.applySettings();

                String path = chooser.getSelectedFile().getAbsolutePath();

				/*
				 * Google Test have an include folder and a src folder
				 */
                IProjectNode rootGoogleTestFolder = new ProjectLoader().load(new File(path));
                List<INode> includeFolders = Search.searchNodes(rootGoogleTestFolder, new FolderNodeCondition(),
                        File.separator + "include");

                List<INode> srcFolders = Search.searchNodes(rootGoogleTestFolder, new FolderNodeCondition(),
                        File.separator + "src");

                if (includeFolders.size() == 0 && srcFolders.size() == 0)
                    JOptionPane.showMessageDialog(SettingDialog.this, "The selected google test is not available!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    String originalGoogleTestPath = new File(includeFolders.get(0).getAbsolutePath()).getParent()
                            .replace("\\", File.separator);
                    AbstractSetting.setValue(ISettingv2.ORIGINAL_GOOGLE_TEST, originalGoogleTestPath);
                    jtfGoogleTest.setText(originalGoogleTestPath);
                    JOptionPane.showMessageDialog(SettingDialog.this, "Install Google test successfully", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panel.add(btnSelectGoogleTest);
        {
            JLabel lblGoogleTest = new JLabel("Google test");
            lblGoogleTest.setFont(new Font("SansSerif", Font.BOLD, 12));
            lblGoogleTest.setBounds(27, 156, 70, 14);
            panel.add(lblGoogleTest);
        }
    }

    private void setupMcpp() {
        JPanel panel = new WebPanel();
        panel.setBackground(Color.WHITE);

        JScrollPane scrollPane_mcpp = new JScrollPane();
        scrollPane_mcpp.setBorder(null);
        scrollPane_mcpp.setViewportView(panel);
        tabPanel.addTab("Mcpp", null, scrollPane_mcpp, null);
        panel.setLayout(null);

        {
            JLabel lblMcpp = new WebLabel("Mcpp");
            lblMcpp.setBounds(30, 155, 31, 16);
            lblMcpp.setFont(new Font("SansSerif", Font.BOLD, 12));
            panel.add(lblMcpp);
        }
        {

            {
                jtfMcpp = new WebTextField(AbstractSetting.getValue(ISettingv2.MCPP_EXE_PATH));
                jtfMcpp.setBounds(107, 151, 372, 24);
                jtfMcpp.setBackground(Color.WHITE);
                jtfMcpp.setOpaque(true);
                panel.add(jtfMcpp);
                jtfMcpp.setColumns(10);
            }
        }
        JButton btnSelectMcpp = new WebButton("Select...");
        btnSelectMcpp.setBounds(484, 151, 59, 24);
        btnSelectMcpp.addActionListener(e -> {
            chooser.setDialogTitle(jtfMcpp.getText());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(SettingDialog.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                SettingDialog.this.applySettings();

                String path = chooser.getSelectedFile().getAbsolutePath();

				/*
				 * Find mcpp.exe
				 */
                IProjectNode mcppFolderRoot = new ProjectLoader().load(new File(path));
                List<INode> mcppNodes = null;
                if (Utils.isWindows()) {
                    mcppNodes = Search.searchNodes(mcppFolderRoot, new McppConditionInWin());
                } else if (Utils.isUnix()) {
                    mcppNodes = Search.searchNodes(mcppFolderRoot, new McppConditionInUnix());
                }

                if (mcppNodes.size() == 0) {
                    JOptionPane.showMessageDialog(SettingDialog.this,
                            "Don't found " + AbstractSetting.getValue(ISettingv2.MCPP_NAME) + " in selected folder!",
                            "Error", JOptionPane.ERROR_MESSAGE);

                    SettingDialog.this.loadSettings();
                } else {
                    String mcppPath = mcppNodes.get(0).getAbsolutePath().replace("\\", "/");
                    jtfMcpp.setText(mcppPath);

                    AbstractSetting.setValue(ISettingv2.MCPP_EXE_PATH, mcppPath);

                    JOptionPane.showMessageDialog(SettingDialog.this, "Install mcpp successfully", "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panel.add(btnSelectMcpp);
        {
            JLabel lblMcppName = new JLabel("Mcpp name");
            lblMcppName.setFont(new Font("SansSerif", Font.BOLD, 12));
            lblMcppName.setBounds(30, 118, 74, 14);
            panel.add(lblMcppName);
        }
        {
            jtfMcppName = new WebTextField();
            jtfMcppName.setBounds(107, 112, 436, 24);
            panel.add(jtfMcppName);
            jtfMcppName.setColumns(10);
        }

    }

    private void setupSolver() {

        JPanel panel = new WebPanel();
        panel.setBackground(Color.WHITE);

        JScrollPane scrollPane_solver = new JScrollPane();
        scrollPane_solver.setBorder(null);
        tabPanel.addTab("Solver", null, scrollPane_solver, null);
        scrollPane_solver.setViewportView(panel);
        panel.setLayout(null);

        {
            JLabel lblZSolver = new WebLabel("Z3 Solver");
            lblZSolver.setBounds(30, 155, 53, 16);
            lblZSolver.setFont(new Font("SansSerif", Font.BOLD, 12));
            panel.add(lblZSolver);
        }
        {
            {
                jtfZ3Path = new WebTextField(AbstractSetting.getValue(ISettingv2.SOLVER_Z3_PATH));
                jtfZ3Path.setBounds(88, 151, 391, 24);
                jtfZ3Path.setBackground(Color.WHITE);
                jtfZ3Path.setOpaque(true);
                panel.add(jtfZ3Path);
                jtfZ3Path.setColumns(10);
            }
        }
        JButton btnSelectZ3 = new WebButton("Select...");
        btnSelectZ3.setBounds(484, 151, 59, 24);
        btnSelectZ3.addActionListener(e -> {
            chooser.setDialogTitle(jtfZ3Path.getText());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(SettingDialog.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                SettingDialog.this.applySettings();

                String path = chooser.getSelectedFile().getAbsolutePath();

				/*
				 * Find z3.exe
				 */
                IProjectNode z3FolderRoot = new ProjectLoader().load(new File(path));
                List<INode> z3Nodes = null;
                if (Utils.isWindows()) {
                    z3Nodes = Search.searchNodes(z3FolderRoot, new Z3ExecutionConditionInWin());
                } else if (Utils.isUnix()) {
                    z3Nodes = Search.searchNodes(z3FolderRoot, new Z3ExecutionConditionInUnix());
                }

                if (z3Nodes.size() == 0) {
                    JOptionPane.showMessageDialog(SettingDialog.this, "Don't found "
                                    + AbstractSetting.getValue(ISettingv2.SOLVER_Z3_NAME) + " in selected folder!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    SettingDialog.this.loadSettings();
                } else {
                    String z3Path = z3Nodes.get(0).getAbsolutePath().replace("\\", "/");
                    jtfZ3Path.setText(z3Path);

                    AbstractSetting.setValue(ISettingv2.SOLVER_Z3_PATH, z3Path);

                    JOptionPane.showMessageDialog(SettingDialog.this, "Install Z3 environment successfully",
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panel.add(btnSelectZ3);

        JLabel lblZName = new JLabel("Z3 name");
        lblZName.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblZName.setBounds(30, 122, 53, 14);
        panel.add(lblZName);

        jtfZ3Name = new WebTextField();
        jtfZ3Name.setToolTipText("The name of z3 execution");
        jtfZ3Name.setBounds(88, 116, 455, 24);
        panel.add(jtfZ3Name);
        jtfZ3Name.setColumns(10);

    }

    private void setupEnvironment() {

        JPanel panel = new JPanel();

        JScrollPane scrollPane_enviromnent = new JScrollPane();
        scrollPane_enviromnent.setBorder(null);
        tabPanel.addTab("Environment", null, scrollPane_enviromnent, null);
        panel.setBackground(Color.WHITE);
        scrollPane_enviromnent.setViewportView(panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{30, 100, 0, 0, 30, 0};
        gbl_panel.rowHeights = new int[]{9, 158, 0, 69, 0, 0};
        gbl_panel.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        {
            JPanel panel_1 = new JPanel();
            panel_1.setForeground(Color.WHITE);
            panel_1.setBackground(Color.WHITE);
            panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mingw32/Mingw64",
                    TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
            panel_1.setLayout(null);
            GridBagConstraints gbc_panel_1 = new GridBagConstraints();
            gbc_panel_1.gridwidth = 3;
            gbc_panel_1.insets = new Insets(0, 0, 5, 5);
            gbc_panel_1.fill = GridBagConstraints.BOTH;
            gbc_panel_1.gridx = 1;
            gbc_panel_1.gridy = 1;
            panel.add(panel_1, gbc_panel_1);
            {
                JLabel lblGccgComplier = new JLabel("Gcc/g++ complier");
                lblGccgComplier.setBounds(20, 172, 123, 14);
                panel_1.add(lblGccgComplier);
                lblGccgComplier.setFont(
                        lblGccgComplier.getFont().deriveFont(lblGccgComplier.getFont().getStyle() | Font.BOLD));
            }
            {
                jtfCompiler = new WebTextField("");
                jtfCompiler.setForeground(Color.BLACK);
                jtfCompiler.setBounds(143, 167, 248, 25);
                jtfCompiler.setBackground(Color.WHITE);
                jtfCompiler.setOpaque(true);
                panel_1.add(jtfCompiler);
            }
            {
                JButton btnSelect = new WebButton("Select...");
                btnSelect.setBounds(401, 167, 70, 24);
                panel_1.add(btnSelect);

                JLabel lblMakeCommand_1 = new JLabel("Make Command");
                lblMakeCommand_1.setFont(
                        lblMakeCommand_1.getFont().deriveFont(lblMakeCommand_1.getFont().getStyle() | Font.BOLD));
                lblMakeCommand_1.setBounds(20, 41, 123, 14);
                panel_1.add(lblMakeCommand_1);

                JLabel lblGccCommand = new JLabel("GCC Command");
                lblGccCommand
                        .setFont(lblGccCommand.getFont().deriveFont(lblGccCommand.getFont().getStyle() | Font.BOLD));
                lblGccCommand.setBounds(20, 82, 103, 14);
                panel_1.add(lblGccCommand);

                JLabel lblGCommand = new JLabel("G++ Command");
                lblGCommand.setFont(lblGCommand.getFont().deriveFont(lblGCommand.getFont().getStyle() | Font.BOLD));
                lblGCommand.setBounds(20, 125, 103, 14);
                panel_1.add(lblGCommand);

                jtfMingwmake = new WebTextField();
                jtfMingwmake.setBackground(SystemColor.controlLtHighlight);

                jtfMingwmake.setBounds(143, 34, 328, 24);
                panel_1.add(jtfMingwmake);
                jtfMingwmake.setColumns(10);

                jtfGcc = new WebTextField();
                jtfGcc.setBackground(SystemColor.controlLtHighlight);

                jtfGcc.setColumns(10);
                jtfGcc.setBounds(143, 77, 328, 25);
                panel_1.add(jtfGcc);
                {
                    jtfGplusplus = new WebTextField();
                    jtfGplusplus.setBackground(SystemColor.controlLtHighlight);

                    jtfGplusplus.setColumns(10);
                    jtfGplusplus.setBounds(143, 120, 328, 25);
                    panel_1.add(jtfGplusplus);
                }

                JPanel panel_2 = new JPanel();
                panel_2.setBackground(Color.WHITE);
                panel_2.setForeground(Color.WHITE);
                panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Visual Studio",
                        TitledBorder.LEADING, TitledBorder.TOP, null, UIManager.getColor("Button.focus")));
                panel_2.setLayout(null);
                GridBagConstraints gbc_panel_2 = new GridBagConstraints();
                gbc_panel_2.gridheight = 2;
                gbc_panel_2.gridwidth = 3;
                gbc_panel_2.insets = new Insets(0, 0, 5, 5);
                gbc_panel_2.fill = GridBagConstraints.BOTH;
                gbc_panel_2.gridx = 1;
                gbc_panel_2.gridy = 2;
                panel.add(panel_2, gbc_panel_2);

                JLabel lblMakeCommand = new JLabel("Make Command");
                lblMakeCommand.setBounds(10, 42, 110, 14);
                lblMakeCommand
                        .setFont(lblMakeCommand.getFont().deriveFont(lblMakeCommand.getFont().getStyle() | Font.BOLD));
                panel_2.add(lblMakeCommand);

                {
                    jtfMsBuildPath = new WebTextField(AbstractSetting.getValue(ISettingv2.MSBUILD_NAME));
                    jtfMsBuildPath.setPreferredSize(new Dimension(0, 0));
                    jtfMsBuildPath.setOpaque(true);
                    jtfMsBuildPath.setBackground(Color.WHITE);
                    jtfMsBuildPath.setBounds(142, 37, 248, 25);
                }
                panel_2.add(jtfMsBuildPath);

                WebButton webButton = new WebButton("Select...");
                webButton.setBounds(404, 37, 66, 24);
                panel_2.add(webButton);

                JPanel panel_3 = new JPanel();
                panel_3.setLayout(null);
                panel_3.setForeground(Color.WHITE);
                panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Eclipse",

                        TitledBorder.LEADING, TitledBorder.TOP, null, UIManager.getColor("Button.focus")));
                panel_3.setBackground(Color.WHITE);
                GridBagConstraints gbc_panel_3 = new GridBagConstraints();
                gbc_panel_3.gridwidth = 3;
                gbc_panel_3.insets = new Insets(0, 0, 0, 5);
                gbc_panel_3.fill = GridBagConstraints.BOTH;
                gbc_panel_3.gridx = 1;
                gbc_panel_3.gridy = 4;
                panel.add(panel_3, gbc_panel_3);
                {
                    JLabel label = new JLabel("Eclipse");
                    label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD));
                    label.setBounds(10, 42, 110, 14);
                    panel_3.add(label);
                }
                {
                    jtfEclipsePath = new WebTextField(AbstractSetting.getValue(ISettingv2.ECLIPSE_PATH));
                    jtfEclipsePath.setPreferredSize(new Dimension(0, 0));
                    jtfEclipsePath.setOpaque(true);
                    jtfEclipsePath.setBackground(Color.WHITE);
                    jtfEclipsePath.setBounds(142, 37, 248, 25);
                    panel_3.add(jtfEclipsePath);
                }

                WebButton webButton_1 = new WebButton("Select...");
                webButton_1.setBounds(404, 37, 66, 24);
                panel_3.add(webButton_1);

                webButton.addActionListener(e -> {
                    chooser.setDialogTitle(jtfMsBuildPath.getText());
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = chooser.showOpenDialog(SettingDialog.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        SettingDialog.this.applySettings();

                        String path = chooser.getSelectedFile().getAbsolutePath();

						/*
						 * TÃ¬m msbuild.exe
						 */
                        IProjectNode msbuildFolderRoot = new ProjectLoader().load(new File(path));
                        List<INode> msbuildNodes = Search.searchNodes(msbuildFolderRoot, new MSBuildCondition());

                        if (msbuildNodes.size() == 0) {
                            JOptionPane.showMessageDialog(SettingDialog.this, "Don't found "
                                            + AbstractSetting.getValue(ISettingv2.MSBUILD_NAME) + " in selected folder!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            SettingDialog.this.loadSettings();
                        } else {
                            String msbuildPath = msbuildNodes.get(0).getAbsolutePath().replace("\\", "/");

                            jtfMsBuildPath.setText(msbuildPath);

                            AbstractSetting.setValue(ISettingv2.MSBUILD_PATH, msbuildPath);

                            JOptionPane.showMessageDialog(SettingDialog.this,
                                    "Install msbuild environment successfully", "Information",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                });

                btnSelect.addActionListener(e -> {
                    chooser.setDialogTitle(jtfCompiler.getText());
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = chooser.showOpenDialog(SettingDialog.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        SettingDialog.this.applySettings();

                        String path = chooser.getSelectedFile().getAbsolutePath();

						/*
						 * Kiá»ƒm tra Ä‘Æ°á»�ng dáº«n há»£p lá»‡ khÃ´ng
						 */
                        CompilerFolderParser analysis = new CompilerFolderParser(new File(path));
                        analysis.parse();
                        if (analysis.getGccPath().length() > 0 && analysis.getgPlusPlusPath().length() > 0
                                && analysis.getMakePath().length() > 0) {
                            jtfCompiler.setText(path);
                            jtfCompiler.setToolTipText(path);
							/*
							 * LÆ°u láº¡i Ä‘Æ°á»�ng dáº«n sá»­ dá»¥ng / thay vÃ¬
							 * \\
							 * 
							 * bá»Ÿi vÃ¬ GNU biÃªn dá»‹ch sá»­ dá»¥ng /
							 */
                            AbstractSetting.setValue(ISettingv2.GNU_GENERAL, path.replace("\\", "/"));
                            AbstractSetting.setValue(ISettingv2.GNU_GCC_PATH, analysis.getGccPath().replace("\\", "/"));
                            AbstractSetting.setValue(ISettingv2.GNU_GPlusPlus_PATH,
                                    analysis.getgPlusPlusPath().replace("\\", "/"));
                            AbstractSetting.setValue(ISettingv2.GNU_MAKE_PATH,
                                    analysis.getMakePath().replace("\\", "/"));

                            JOptionPane.showMessageDialog(SettingDialog.this,
                                    "Install compilation environment successfully", "Information",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(SettingDialog.this,
                                    "Don't found compilation execution in selected folder!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            SettingDialog.this.loadSettings();
                        }
                    }
                });

                webButton_1.addActionListener(e -> {
                    chooser.setDialogTitle(jtfEclipsePath.getText());
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = chooser.showOpenDialog(SettingDialog.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        SettingDialog.this.applySettings();

                        String path = chooser.getSelectedFile().getAbsolutePath();

						/*
						 * Find eclipse
						 */
                        IProjectNode eclipseFolderRoot = new ProjectLoader().load(new File(path));
                        List<INode> eclipseNodes = Search.searchNodes(eclipseFolderRoot, new EclipseCondition());

                        if (eclipseNodes.size() == 0) {
                            JOptionPane.showMessageDialog(SettingDialog.this, "Don't found "
                                            + AbstractSetting.getValue(ISettingv2.ECLIPSE_NAME) + " in selected folder!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            SettingDialog.this.loadSettings();
                        } else {
                            try {
                                String eclipsePath = new File(eclipseNodes.get(0).getAbsolutePath()).getCanonicalPath();
                                jtfEclipsePath.setText(eclipsePath);

                                AbstractSetting.setValue(ISettingv2.ECLIPSE_PATH, eclipsePath);

                                JOptionPane.showMessageDialog(SettingDialog.this,
                                        "Install eclipse environment successfully", "Information",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                        }
                    }
                });
            }
        }

    }
}
