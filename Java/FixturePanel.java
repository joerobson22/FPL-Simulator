package Java;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FixturePanel extends JPanel implements ActionListener{
    private final String fixtureFont = "SansSerif";
    private final int fixtureFontSize = 20;
    private final String buttonFont = "SansSerif";
    private final int buttonFontSize = 15;

    MainWindow mainWindow;

    JButton simulateButton;
    JLabel homeTeam;
    JLabel awayTeam;
    JLabel score;
    JLabel paddingLabel;

    Fixture fixture;
    
    //constructor
    public FixturePanel(MainWindow mainWindow, Fixture fixture){
        this.mainWindow = mainWindow;
        this.fixture = fixture;

        displayFixture();
    }

    //methods
    public void displayFixture(){
        this.removeAll();

        homeTeam = new JLabel(fixture.getHomeTeam().getName());
        homeTeam.setForeground(new Color(0, 0, 0));
        homeTeam.setFont(new Font(fixtureFont, Font.PLAIN, fixtureFontSize));

        awayTeam = new JLabel(fixture.getAwayTeam().getName());
        awayTeam.setForeground(new Color(0, 0, 0));
        awayTeam.setFont(new Font(fixtureFont, Font.PLAIN, fixtureFontSize));

        score = new JLabel(" vs ");
        score.setForeground(new Color(0, 0, 0));
        score.setFont(new Font(fixtureFont, Font.PLAIN, fixtureFontSize));

        simulateButton = new JButton("Simulate");
        simulateButton.setFont(new Font(buttonFont, Font.BOLD, buttonFontSize));
        simulateButton.addActionListener(this);

        paddingLabel = new JLabel("   ");

        //add everything
        this.add(homeTeam);
        this.add(score);
        this.add(awayTeam);

        //update score text and if the button exists or not
        if(fixture.hasPlayed()){
            score.setText(fixture.getOutcome().getScoreString());
        }
        else{
            this.add(paddingLabel);
            this.add(simulateButton);
        }

        repaintUI();
    }

    //this fixture had been played
    public void updateFixturePanel(){
        //remove padding between team names and the simulate button
        this.remove(paddingLabel);
        this.remove(simulateButton);

        //update the score text
        score.setText(fixture.getOutcome().getScoreString());

        repaintUI();
    }

    public void repaintUI(){
        //revalidate and repaint ui
        this.revalidate();
        this.repaint();
    }

    //action listener for button
    public void actionPerformed(ActionEvent e)
    {
        if(simulateButton == e.getSource() && !fixture.hasPlayed()){
            mainWindow.simulateFixture(this, fixture);
        }
    }
}
