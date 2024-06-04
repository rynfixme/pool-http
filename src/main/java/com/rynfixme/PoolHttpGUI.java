package com.rynfixme;

import burp.api.montoya.logging.Logging;
import burp.api.montoya.persistence.PersistedObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PoolHttpGUI extends JPanel {
    Logging logging;
    PersistedObject persist;
    public PoolHttpGUI(Logging logging, PersistedObject persist) {
        this.logging = logging;
        this.persist = persist;

        JPanel panel = new JPanel();
        GridLayout grid = new GridLayout(3,2);
        grid.setHgap(4);
        grid.setVgap(4);
        panel.setLayout(grid);

        JLabel runningStatusLabel = new JLabel("Current status: capturing");
        JButton runningStatusToggleButton = new JButton("Toggle");
        panel.add(runningStatusLabel);
        panel.add(runningStatusToggleButton);

        JLabel createNewFileLabel = new JLabel("Create new file");
        JButton createNewFileButton = new JButton("Create");
        panel.add(createNewFileLabel);
        panel.add(createNewFileButton);

        String currentFileName = this.persist.getString(Main.STORE_FILE_NAME);
        JLabel currentFileLabel = new JLabel("Current file: " + currentFileName);
        panel.add(currentFileLabel);

        this.add(panel);

        runningStatusToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean doCapture = persist.getBoolean(Main.DO_CAPTURE);

                if (doCapture) {
                    runningStatusLabel.setText("Current status: skip");
                    persist.setBoolean(Main.DO_CAPTURE, false);
                } else {
                    runningStatusLabel.setText("Current status: capturing");
                    persist.setBoolean(Main.DO_CAPTURE, true);

                }
            }
        });

        createNewFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PoolHttpFileUtil fileUtil = new PoolHttpFileUtil();
                String fileName = fileUtil.createFileName();
                persist.setString(Main.STORE_FILE_NAME, fileName);
                currentFileLabel.setText("Current file: " + fileName);
            }
        });
    }
}
