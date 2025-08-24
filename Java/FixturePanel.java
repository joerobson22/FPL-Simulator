package Java;
import javax.swing.*;
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
    JLabel homeTeam;
    JLabel awayTeam;
    JLabel score;

    Fixture fixture;
    
    //constructor
    public FixturePanel(MainWindow mainWindow, Fixture fixture){
        this.mainWindow = mainWindow;
        this.fixture = fixture;

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
        homeTeam = setupDefaultLabel(fixture.getHomeTeam().getName());
        awayTeam = setupDefaultLabel(fixture.getAwayTeam().getName());
        score = setupDefaultLabel(" vs ");

        actionButton = new JButton("Simulate");
        actionButton.setFont(new Font(buttonFont, Font.BOLD, buttonFontSize));
        actionButton.addActionListener(this);

        //add components to score panel
        scorePanel.add(homeTeam);
        scorePanel.add(score);
        scorePanel.add(awayTeam);

        this.add(scorePanel);

        //add button to button panel if not played
        if(!fixture.hasPlayed()){
            buttonPanel.add(actionButton);
            this.add(buttonPanel);
        }
        //otherwise show the score and the fixture's scorers
        else {
            score.setText(fixture.getOutcome().getScoreString());

            actionButton.setText("View >>");
        }

        //set preferred size
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 120));

        this.setBackground(new Color(0, 0, 0));
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

    public JLabel setupDefaultLabel(String text){
        JLabel label = new JLabel(text);
        label.setForeground(new Color(0, 0, 0));
        label.setFont(new Font(fixtureFont, Font.PLAIN, fixtureFontSize));

        return label;
    }

    public void viewFixtureOutcome(){
        FixtureOutcomeWindow fow = new FixtureOutcomeWindow(fixture, scorePanel);
    }

    //action listener for button
    public void actionPerformed(ActionEvent e) {
        if(actionButton == e.getSource()){
            //if the action button is meant to trigger the simulation, simulate!
            if(!fixture.hasPlayed()){
                mainWindow.simulateFixture(this, fixture);
                return;
            }
            //otherwise open a new fixture outcome window
            viewFixtureOutcome();
        }
    }
}