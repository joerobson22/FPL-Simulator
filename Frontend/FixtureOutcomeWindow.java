package Frontend;

import java.util.ArrayList;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class FixtureOutcomeWindow extends JFrame implements ActionListener{
    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 800;

    private final String fixtureFont = "SansSerif";
    private final int fixtureFontSize = 20;
    final String font = "SansSerif";
    final int infoSectionTitleFontSize = 15;
    final int infoSectionTitleSmallerFontSize = 13;
    final int infoSectionBodyFontSize = 12;
    final int infoSectionSmallerBodyFontSize = 10;
    final String infoSectionBodyPadding = "     ";

    Fixture fixture;

    JPanel mainPanel;
    JPanel infoPanel;
    JPanel scorePanel;

    public FixtureOutcomeWindow(Fixture fixture){
        this.fixture = fixture;

        setupWindow();
    }

    private void setupWindow(){
        infoPanel = new JPanel();
        setupInfoPanel();

        scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        scorePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        //setup all the labels for the score panel
        JLabel homeTeam = setupDefaultScoreLabel(fixture.getHomeTeam().getName());
        JLabel awayTeam = setupDefaultScoreLabel(fixture.getAwayTeam().getName());
        JLabel score = setupDefaultScoreLabel(fixture.getOutcome().getScoreString());

        //add components to score panel
        scorePanel.add(homeTeam);
        scorePanel.add(score);
        scorePanel.add(awayTeam);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scorePanel, "North");
        mainPanel.add(infoPanel, "Center");

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle(fixture.getHomeTeam().getName() + " vs " + fixture.getAwayTeam().getName());
        this.setResizable(false);
    }

    public void setupInfoPanel(){
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JPanel startingLineupPanel = new JPanel();
        startingLineupPanel.add(setupInfoSection("Lineups", fixture.getLineups(), infoSectionTitleSmallerFontSize, infoSectionSmallerBodyFontSize));

        JPanel fplInfoPanel = new JPanel();
        fplInfoPanel.setLayout(new BoxLayout(fplInfoPanel, BoxLayout.Y_AXIS));

        if(fixture.getOutcome().getGoalScorers().size() > 0) fplInfoPanel.add(setupInfoSection("Goals", fixture.getOutcome().getGoalScorers(), infoSectionTitleFontSize, infoSectionBodyFontSize));
        if(fixture.getOutcome().getAssisters().size() > 0) fplInfoPanel.add(setupInfoSection("Assists", fixture.getOutcome().getAssisters(), infoSectionTitleFontSize, infoSectionBodyFontSize));

        if(fixture.getOutcome().getCleanSheets().size() > 0) fplInfoPanel.add(setupInfoSection("Clean Sheets", fixture.getOutcome().getCleanSheets(), infoSectionTitleSmallerFontSize, infoSectionSmallerBodyFontSize));
        if(fixture.getOutcome().getThreeSaves().size() > 0) fplInfoPanel.add(setupInfoSection("3x Saves", fixture.getOutcome().getThreeSaves(), infoSectionTitleSmallerFontSize, infoSectionSmallerBodyFontSize));
        if(fixture.getOutcome().getDFCon().size() > 0) fplInfoPanel.add(setupInfoSection("Defensive Contributions", fixture.getOutcome().getDFCon(), infoSectionTitleSmallerFontSize, infoSectionSmallerBodyFontSize));
        if(fixture.getOutcome().getYellowCards().size() > 0) fplInfoPanel.add(setupInfoSection("Yellow Cards", fixture.getOutcome().getYellowCards(), infoSectionTitleSmallerFontSize, infoSectionSmallerBodyFontSize));
        if(fixture.getOutcome().getRedCards().size() > 0) fplInfoPanel.add(setupInfoSection("Red Cards", fixture.getOutcome().getRedCards(), infoSectionTitleSmallerFontSize, infoSectionSmallerBodyFontSize));
        if(fixture.getOutcome().getPenaltyGoals().size() > 0) fplInfoPanel.add(setupInfoSection("Penalty Goals", fixture.getOutcome().getPenaltyGoals(), infoSectionTitleSmallerFontSize, infoSectionSmallerBodyFontSize));
        if(fixture.getOutcome().getPenaltyMisses().size() > 0) fplInfoPanel.add(setupInfoSection("Penalty Misses", fixture.getOutcome().getPenaltyMisses(), infoSectionTitleSmallerFontSize, infoSectionSmallerBodyFontSize));
        if(fixture.getOutcome().getPenaltySaves().size() > 0) fplInfoPanel.add(setupInfoSection("Penalty Saves", fixture.getOutcome().getPenaltySaves(), infoSectionTitleSmallerFontSize, infoSectionSmallerBodyFontSize));

        infoPanel.add(startingLineupPanel);
        infoPanel.add(fplInfoPanel);
    }

    public JPanel setupInfoSection(String title, ArrayList<Player> players, int headerFontSize, int bodyFontSize) {
        // Main vertical container
        JPanel panel = new JPanel();
        Box section = Box.createVerticalBox();
        section.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Title label
        JLabel titleLabel = LabelCreator.createJLabel(
                title, "SansSerif", headerFontSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK
        );
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(titleLabel);

        // Content panel (2 columns: home / away)
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 2, 0)); // tiny gap
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Panels for home / away players
        JPanel homePanel = new JPanel();
        JPanel awayPanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        awayPanel.setLayout(new BoxLayout(awayPanel, BoxLayout.Y_AXIS));
        homePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        awayPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        ArrayList<JLabel> homePanelLabels = new ArrayList<>();
        ArrayList<JLabel> awayPanelLabels = new ArrayList<>();

        for (Player p : players) {
            JLabel label = LabelCreator.createJLabel(
                    p.getName(), "SansSerif", bodyFontSize, Font.PLAIN, SwingConstants.LEFT, Color.BLACK
            );
            if (p.getTeam() == fixture.getHomeTeam()) {
                homePanelLabels.add(label);
            } else if (p.getTeam() == fixture.getAwayTeam()) {
                awayPanelLabels.add(label);
            }
        }

        // Compress duplicate players
        homePanel = compressInfoPanel(homePanel, homePanelLabels, true);
        awayPanel = compressInfoPanel(awayPanel, awayPanelLabels, false);

        // Add to content panel
        contentPanel.add(homePanel);
        contentPanel.add(awayPanel);

        // Add to main section
        section.add(contentPanel);
        panel.add(section);

        return panel;
    }

    /*
    public JPanel setupInfoSection(String title, ArrayList<Player> players, int headerFontSize, int bodyFontSize){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = LabelCreator.createJLabel(title, "SansSerif", headerFontSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
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
            JLabel label = LabelCreator.createJLabel(p.getName(), "SansSerif", bodyFontSize, Font.PLAIN, SwingConstants.LEFT, Color.BLACK);
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
    }*/

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

    public JLabel setupDefaultScoreLabel(String text){
        JLabel label = new JLabel(text);
        label.setForeground(new Color(0, 0, 0));
        label.setFont(new Font(fixtureFont, Font.PLAIN, fixtureFontSize));

        return label;
    }

    public void actionPerformed(ActionEvent e){

    }
}
