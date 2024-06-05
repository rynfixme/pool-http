package com.rynfixme;

import burp.api.montoya.logging.Logging;
import burp.api.montoya.persistence.PersistedObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PoolHttpGUI extends JPanel {
    Logging logging;
    PersistedObject persist;
    PoolHttpFileUtil fileUtil;

    public PoolHttpGUI(PersistedObject persist, PoolHttpFileUtil fileUtil, Logging logging) {
        this.logging = logging;
        this.persist = persist;
        this.fileUtil = fileUtil;

        JPanel panel = new JPanel();
        GridLayout grid = new GridLayout(5,2);
        grid.setHgap(4);
        grid.setVgap(4);
        panel.setLayout(grid);

        // switch mode
        JLabel runningStatusLabel = new JLabel("Current status: CAPTURING...");
        JButton runningStatusToggleButton = new JButton("Toggle");
        panel.add(runningStatusLabel);
        panel.add(runningStatusToggleButton);

        // change directory
        JLabel dirChooseLabel = new JLabel("Change Directory");
        JButton dirChooseButton = new JButton("Change");
        panel.add(dirChooseLabel);
        panel.add(dirChooseButton);

        // create a new file
        JLabel createNewFileLabel = new JLabel("Create new file");
        JButton createNewFileButton = new JButton("Create");
        panel.add(createNewFileLabel);
        panel.add(createNewFileButton);

        // current directory name
        JLabel currentDirNameLabel = new JLabel("Current directory: ");
        String currentDirName = this.fileUtil.getCurrentDirectoryName();
        JLabel currentDirectoryLabel = new JLabel(currentDirName);
        panel.add(currentDirNameLabel);
        panel.add(currentDirectoryLabel);

        // current file name
        JLabel currentFileNameLabel = new JLabel("Current file: ");
        String currentFileName = this.fileUtil.getCurrentFileName();
        JLabel currentFileLabel = new JLabel(currentFileName);
        panel.add(currentFileNameLabel);
        panel.add(currentFileLabel);

        this.add(panel);

        runningStatusToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean doCapture = persist.getBoolean(Main.DO_CAPTURE);

                if (doCapture) {
                    runningStatusLabel.setText("Current status: STOP");
                    persist.setBoolean(Main.DO_CAPTURE, false);
                } else {
                    runningStatusLabel.setText("Current status: CAPTURING...");
                    persist.setBoolean(Main.DO_CAPTURE, true);

                }
            }
        });

        dirChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new java.io.File("."));
                fileChooser.setDialogTitle("Choose directory");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.showOpenDialog(PoolHttpGUI.this);

                File dir = fileChooser.getSelectedFile();
                String dirPath = dir.getPath();
                fileUtil.setDirectoryName(dirPath);
                currentDirectoryLabel.setText(dirPath);
            }
        });

        createNewFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = fileUtil.createFileName();
                currentFileLabel.setText("file: " + fileName);
            }
        });
    }
}
