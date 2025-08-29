package Frontend;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Flow;

public class FixturePanel extends JPanel implements ActionListener{
    private final String fixtureFont = "SansSerif";
    private final int fixtureFontSize = 20;
    private final String buttonFont = "SansSerif";
    private final int buttonFontSize = 15;

    MainWindow mainWindow;

    JPanel scorePanel;
    JPanel buttonPanel;
    JPanel scorersPanel;

    JButton actionButton;
    boolean buttonPressed = false;
    JLabel homeTeam;
    JLabel awayTeam;
    JLabel score;

    boolean canSimulate;
    Fixture fixture;

    Border blackline = BorderFactory.createLineBorder(Color.black);
    
    //constructor
    public FixturePanel(MainWindow mainWindow, Fixture fixture, boolean canSimluate){
        this.mainWindow = mainWindow;
        this.fixture = fixture;
        this.canSimulate = canSimluate;

        setup();
    }

    //methods
    public void setup(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Score panel with FlowLayout for horizontal arrangement
        scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        scorePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        //setup the button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        //setup all the labels for the score panel
        homeTeam = LabelCreator.createJLabel(fixture.getHomeTeam().getAbbrv(), fixtureFont, fixtureFontSize, Font.BOLD, SwingConstants.LEFT, Color.BLACK);
        awayTeam = LabelCreator.createJLabel(fixture.getAwayTeam().getAbbrv(), fixtureFont, fixtureFontSize, Font.BOLD, SwingConstants.LEFT, Color.BLACK);
        score = LabelCreator.createJLabel(" vs ", fixtureFont, fixtureFontSize, Font.PLAIN, SwingConstants.LEFT, Color.BLACK);

        actionButton = new JButton("Simulate");
        actionButton.setFont(new Font("SansSerif", Font.BOLD, buttonFontSize));
        actionButton.addActionListener(this);

        //add components to score panel
        scorePanel.add(homeTeam);
        scorePanel.add(score);
        scorePanel.add(awayTeam);

        this.add(scorePanel);

        //add button to button panel if not played
        if(canSimulate | fixture.hasPlayed()){
            buttonPanel.add(actionButton);
            this.add(buttonPanel);
        }
        //otherwise show the score and the fixture's scorers
        
        if(fixture.hasPlayed()){
            score.setText(fixture.getOutcome().getScoreString());

            actionButton.setText("View >>");
        }

        //set preferred size
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 120));
    }

    //this fixture has been played now, so remove the button and display the score and scorers
    public void updateFixturePanel(){
        // Remove the button
        actionButton.setText("View >>");
        
        // Update the score text
        score.setText(fixture.getOutcome().getScoreString());
        
        // Refresh
        repaintUI();
    }

    public void repaintUI(){
        this.revalidate();
        this.repaint();
    }

    public void viewFixtureOutcome(){
        FixtureOutcomeWindow fow = new FixtureOutcomeWindow(fixture);
    }

    //action listener for button
    public void actionPerformed(ActionEvent e) {
        if(actionButton == e.getSource()){
            //if the action button is meant to trigger the simulation, simulate!
            if(!fixture.hasPlayed()){
                if(buttonPressed) return;

                buttonPressed = true;
                mainWindow.simulateFixture(this, fixture, false);
                return;
            }
            //otherwise open a new fixture outcome window
            viewFixtureOutcome();
        }
    }
}