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
    final String font = "SansSerif";
    final int infoSectionTitleFontSize = 15;
    final int infoSectionBodyFontSize = 12;
    final String infoSectionBodyPadding = "     ";

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
        infoPanel = new JPanel();
        setupInfoPanel();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titlePanel, "North");
        mainPanel.add(infoPanel, "Center");

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(500, 500);
        this.setTitle(fixture.getHomeTeam().getName() + " vs " + fixture.getAwayTeam().getName());
        this.setResizable(false);
    }

    public void setupInfoPanel(){
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JPanel goalContributionsPanel = new JPanel();
        goalContributionsPanel.setLayout(new BoxLayout(goalContributionsPanel, BoxLayout.Y_AXIS));
        JPanel extraInfoPanel = new JPanel();
        extraInfoPanel.setLayout(new BoxLayout(extraInfoPanel, BoxLayout.Y_AXIS));

        goalContributionsPanel.add(setupInfoSection("Goals", fixture.getOutcome().getGoalScorers()));
        goalContributionsPanel.add(setupInfoSection("Assists", fixture.getOutcome().getAssisters()));

        extraInfoPanel.add(setupInfoSection("Clean Sheets", fixture.getOutcome().getCleanSheets()));

        infoPanel.add(goalContributionsPanel);
        infoPanel.add(extraInfoPanel);
    }

    public JPanel setupInfoSection(String title, ArrayList<Player> players){
        JPanel panel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(font, Font.BOLD, infoSectionTitleFontSize));
        titlePanel.add(titleLabel);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));

        JPanel homePanel = new JPanel();
        JPanel awayPanel = new JPanel();
        ArrayList<JLabel> homePanelLabels = new ArrayList<JLabel>();
        ArrayList<JLabel> awayPanelLabels = new ArrayList<JLabel>();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        awayPanel.setLayout(new BoxLayout(awayPanel, BoxLayout.Y_AXIS));
        homePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        awayPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        for(Player p : players){
            JLabel label = setupDefaultLabel(p.getName());
            if(p.getTeam() == fixture.getHomeTeam()){
                //label.setText(label.getText() + infoSectionBodyPadding);
                homePanelLabels.add(label);
                label.setAlignmentX(RIGHT_ALIGNMENT);
            }
            else if(p.getTeam() == fixture.getAwayTeam()){
                //label.setText(infoSectionBodyPadding + label.getText());
                awayPanelLabels.add(label);
                label.setAlignmentX(LEFT_ALIGNMENT);
            }
        }

        homePanel = compressInfoPanel(homePanel, homePanelLabels, true);
        awayPanel = compressInfoPanel(awayPanel, awayPanelLabels, false);

        contentPanel.add(homePanel);
        contentPanel.add(awayPanel);

        panel.add(titlePanel, "North");
        panel.add(contentPanel, "Center");

        return panel;
    }

    private JPanel compressInfoPanel(JPanel panel, ArrayList<JLabel> labels, boolean home){
        for(int i = 0; i < labels.size(); i++){
            JLabel l1 = labels.get(i);
            int quantity = 1;
            String text1 = l1.getText();

            for(int j = 0; j < labels.size(); j++){
                JLabel l2 = labels.get(j);

                if(l1 == l2) continue;

                String text2 = l2.getText();
                if(text1.equals(text2)){
                    quantity++;
                    labels.remove(l2);
                    j--;
                }
            }

            if(quantity > 1) l1.setText(l1.getText() + " x" + String.valueOf(quantity));
            else l1.setText(l1.getText() + "  ");

            if(home) l1.setText(l1.getText() + infoSectionBodyPadding);
            else l1.setText(infoSectionBodyPadding + l1.getText());
        }

        for(JLabel l : labels){
            panel.add(l);
        }

        return panel;
    }

    public JLabel setupDefaultLabel(String text){
        JLabel label = new JLabel(text);
        label.setForeground(new Color(0, 0, 0));
        label.setFont(new Font(font, Font.PLAIN, infoSectionBodyFontSize));

        return label;
    }

    public void actionPerformed(ActionEvent e){

    }
}
