package Java;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FixtureOutcomeWindow extends JFrame implements ActionListener{
    Fixture fixture;

    JPanel mainPanel;
    JPanel titlePanel;
    JPanel infoPanel;

    public FixtureOutcomeWindow(Fixture fixture, JPanel titlePanel){
        this.fixture = fixture;
        this.titlePanel = titlePanel;

        setupWindow();
    }

    private void setupWindow(){
        infoPanel = new JPanel(new GridLayout(1, 2));

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titlePanel, "North");
        mainPanel.add(infoPanel, "Center");

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(500, 500);
        this.setTitle(fixture.getHomeTeam().getName() + " vs " + fixture.getAwayTeam().getName());
        this.setResizable(false);
    }

    public void actionPerformed(ActionEvent e){

    }
}
