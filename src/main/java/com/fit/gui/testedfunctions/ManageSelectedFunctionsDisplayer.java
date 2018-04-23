package com.fit.gui.testedfunctions;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.fit.config.AbstractSetting;
import com.fit.config.ISettingv2;
import com.fit.config.Paths;
import com.fit.googletest.UnitTestProject;
import com.fit.gui.testreport.object.IProjectReport;
import com.fit.utils.Utils;
import com.fit.utils.UtilsVu;

/**
 * Represent the tested function management displayer
 *
 * @author Duong Td
 */
public class ManageSelectedFunctionsDisplayer extends JFrame implements IManageSelectedFunctionsDisplayer {
    final static Logger logger = Logger.getLogger(ManageSelectedFunctionsDisplayer.class);
    private static final long serialVersionUID = 1L;
    private static final String KEY_NAME_SAVE = "FRAME_MANAGE_TESTED_FUNCTION_SIZE";
    /**
     * Singleton pattern
     */
    private static ManageSelectedFunctionsDisplayer instance = null;
    private UnitTestProject unitTestProject = null;
    private ContentPanelDisplay contentPane;
    private IProjectReport projectReport;

    public ManageSelectedFunctionsDisplayer() {
        if (Paths.START_FROM_COMMANDLINE == false) {
            if (Utils.isWindows()) {
                setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else if (Utils.isUnix()) {
                setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            }
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    ManageSelectedFunctionsDisplayer.this.writeSaveData();
                }

                @Override
                public void windowOpened(WindowEvent e) {
                    ManageSelectedFunctionsDisplayer.this.restoreDataSave();
                }
            });

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    ManageSelectedFunctionsDisplayer.this.writeSaveData();
                }

                @Override
                public void componentShown(ComponentEvent e) {
                    ManageSelectedFunctionsDisplayer.this.restoreDataSave();
                    ManageSelectedFunctionsDisplayer.this.setJframeAtCenter();
                }
            });

            addWindowStateListener(e -> {
                if (e.getNewState() == Frame.ICONIFIED && e.getOldState() == Frame.NORMAL)
                    ManageSelectedFunctionsDisplayer.this.writeSaveData();
                if (e.getNewState() == Frame.NORMAL && e.getOldState() == Frame.ICONIFIED)
                    ManageSelectedFunctionsDisplayer.this.restoreDataSave();
            });
        }
    }

    public static ManageSelectedFunctionsDisplayer getInstance() {
        if (ManageSelectedFunctionsDisplayer.instance == null)
            ManageSelectedFunctionsDisplayer.instance = new ManageSelectedFunctionsDisplayer();
        return ManageSelectedFunctionsDisplayer.instance;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {

        });

    }

    @Override
    public void changeState() {
        if (getState() == Frame.ICONIFIED) {
            setState(Frame.NORMAL);
            restoreDataSave();
        }
    }

    void displayInformation(IProjectReport input) {
        contentPane.insertData(input);
    }

    @Override
    public void exportExcelFile() {
        contentPane.getBtnExport().addActionListener(e -> {
            // Show dialog to set the location export
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose Export File Location");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setApproveButtonText("OK...");
            chooser.setApproveButtonMnemonic('k');
            int rVal = chooser.showSaveDialog(ManageSelectedFunctionsDisplayer.this);

            if (rVal == JFileChooser.APPROVE_OPTION) {
                String exportPath = chooser.getSelectedFile().toString();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            projectReport.exportToExcel(exportPath);

                            JOptionPane.showMessageDialog(ManageSelectedFunctionsDisplayer.this, "Export sucessfully!");

                            Runtime.getRuntime().exec("explorer \"" + exportPath + "\"");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(ManageSelectedFunctionsDisplayer.this, "Export failed!");
                        }
                    }
                }.start();
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
            }
        });
    }

    @Override
    public void exportUnitTest() {
        contentPane.getBtnExportUnitTest().addActionListener(e -> {
            // Show dialog to set the location export
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose Unit Test Location");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setApproveButtonText("OK...");
            chooser.setApproveButtonMnemonic('k');
            int rVal = chooser.showSaveDialog(ManageSelectedFunctionsDisplayer.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                /*
				 * Update Button
				 */
                JButton btnExportUnitTest = ManageSelectedFunctionsDisplayer.getInstance().getContentPane()
                        .getBtnExportUnitTest();
                String oldText = btnExportUnitTest.getText();
                btnExportUnitTest.setText("Generating unit test project...");

				/*
				 * Generate unit test project
				 */

                String unitTestFolderPath = "";
                if (new File(unitTestFolderPath).exists())
                    unitTestFolderPath = chooser.getSelectedFile().toString();
                else
                    unitTestFolderPath = chooser.getSelectedFile().toString() + File.separator + "unit-test";

                try {
                    Utils.deleteFileOrFolder(new File(unitTestFolderPath));
                    unitTestProject = projectReport.generateUnitTest(new File(unitTestFolderPath));
                    logger.debug("Generate unit test done...");

                    ManageSelectedFunctionsDisplayer.this.style(unitTestFolderPath);

                    ManageSelectedFunctionsDisplayer.this.openUnitTestProject(unitTestFolderPath);

                    int reply = JOptionPane.showConfirmDialog(null, "Do you want to execute unit test project?",
                            "Execute unit test project", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        btnExportUnitTest.setText("Executing unit test project...");
                        Thread t = new Thread(() -> {
                            try {
                                unitTestProject.execute();
                                JOptionPane.showMessageDialog(ManageSelectedFunctionsDisplayer.this,
                                        "Execute unit test project done.");
                            } catch (Exception e1) {
                                JOptionPane.showMessageDialog(ManageSelectedFunctionsDisplayer.this,
                                        "Execute unit test project failed!");

                            } finally {
                                /**
                                 * Restore state of button
                                 */
                                btnExportUnitTest.setText(oldText);
                                ManageSelectedFunctionsDisplayer.getInstance().getContentPane().getBtnExportUnitTest()
                                        .setIcon(null);

                                ManageSelectedFunctionsDisplayer.getInstance().refresh();
                            }
                        });
                        t.run();
                    } else {
                        /**
                         * Restore state of button
                         */
                        btnExportUnitTest.setText(oldText);
                        ManageSelectedFunctionsDisplayer.getInstance().getContentPane().getBtnExportUnitTest()
                                .setIcon(null);

                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(ManageSelectedFunctionsDisplayer.this,
                            "Export unit test project failed!");
                }

            }
        });
    }

    @Override
    public ContentPanelDisplay getContentPane() {
        return contentPane;
    }

    @Override
    public String getExpectedOutputData() {
        return null;
    }

    @Override
    public void refresh() {
        if (Paths.START_FROM_COMMANDLINE) {
            // Not need to update GUI

        } else if (contentPane != null)
            contentPane.refresh();
    }

    @Override
    public void restoreDataSave() {
        String strSizeSaved = AbstractSetting.getValue(ManageSelectedFunctionsDisplayer.KEY_NAME_SAVE);
        if (strSizeSaved != null && !strSizeSaved.equals("")) {
            String[] tempStrs = strSizeSaved.split(",");
            if (tempStrs.length == 3) {
                int width = Integer.parseInt(tempStrs[0]);
                int height = Integer.parseInt(tempStrs[1]);
                this.setSize(width, height);

                int state = Integer.parseInt(tempStrs[2]);
                if (state == Frame.ICONIFIED)
                    state = Frame.NORMAL;
                setExtendedState(state);
            }
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if (contentPane != null) {
                        contentPane.setSizeSaveBeforeForComponents();
                        Thread.sleep(50);
                        contentPane.setSizeSaveBeforeForComponents();
                        this.stop();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NullPointerException f) {
                    f.printStackTrace();
                }
            }
        };
        thread.start();
    }

    void setJframeAtCenter() {
        if (getExtendedState() != Frame.MAXIMIZED_BOTH) {
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        }
    }

    @Override
    public void setLookAndFeel(String nameLookAndFeel) {
        try {
            UIManager.setLookAndFeel(nameLookAndFeel);
            SwingUtilities.updateComponentTreeUI(ManageSelectedFunctionsDisplayer.this);
            AbstractSetting.setValue(ISettingv2.LOOK_AND_FEEL, nameLookAndFeel);

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(ManageSelectedFunctionsDisplayer.this,
                    "The operating system does not support this layout!", "Error", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }

    @Override
    public void setProjectReport(IProjectReport input) {
        setTitle("Manage selected functions");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(1200, 600);
        contentPane = new ContentPanelDisplay(1200, 600);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        setJframeAtCenter();

        projectReport = input;

        displayInformation(input);

        exportExcelFile();

        exportUnitTest();

    }

    @Override
    public void writeSaveData() {
        AbstractSetting.setValue(ManageSelectedFunctionsDisplayer.KEY_NAME_SAVE,
                getWidth() + "," + getHeight() + "," + getExtendedState());
        contentPane.writeSaveData();
    }

    /**
     * Format .cpp/.c files
     */
    private void style(String unitTestFolderPath) {
        try {
            String pathAStyle = System.getProperty("user.dir") + File.separator + "lib" + File.separator + "AStyle.exe";
            if (pathAStyle.contains("/"))
                pathAStyle = pathAStyle.replace("/", File.separator);
            UtilsVu.runCommand(pathAStyle, null, new File(unitTestFolderPath), "-n", "*.cpp");
            UtilsVu.runCommand(pathAStyle, null, new File(unitTestFolderPath), "-n", "*.c");
            logger.debug("Run Astyle done...");
        } catch (Exception a) {
            logger.debug("Format style file .cpp error!!!!");
        }
    }

    private void openUnitTestProject(String unitTestFolderPath) {
		/*
		 * Open unit test project automatically
		 */
        if (!new File(unitTestFolderPath).exists())
            JOptionPane.showMessageDialog(ManageSelectedFunctionsDisplayer.this,
                    "Export error!! \nPlease click \"Export unit test\" again!");
        else {
            JOptionPane.showMessageDialog(ManageSelectedFunctionsDisplayer.this, "Export done!!");
            try {
                Runtime.getRuntime().exec("explorer \"" + unitTestFolderPath + "\"");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
