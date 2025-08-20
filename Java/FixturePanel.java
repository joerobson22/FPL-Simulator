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

    Fixture fixture;
    
    //constructor
    public FixturePanel(MainWindow mainWindow, Fixture fixture){
        this.mainWindow = mainWindow;
        this.fixture = fixture;

        simulateButton = new JButton("Simulate");
        simulateButton.setFont(new Font(buttonFont, Font.BOLD, buttonFontSize));
        simulateButton.addActionListener(this);

        displayFixture();
    }

    //methods
    public void displayFixture(){
        homeTeam = new JLabel(fixture.getHomeTeam().getName());
        homeTeam.setForeground(new Color(0, 0, 0));
        homeTeam.setFont(new Font(fixtureFont, Font.PLAIN, fixtureFontSize));

        awayTeam = new JLabel(fixture.getAwayTeam().getName());
        awayTeam.setForeground(new Color(0, 0, 0));
        awayTeam.setFont(new Font(fixtureFont, Font.PLAIN, fixtureFontSize));

        score = new JLabel(" vs ");
        score.setForeground(new Color(0, 0, 0));
        score.setFont(new Font(fixtureFont, Font.PLAIN, fixtureFontSize));

        if(fixture.hasPlayed()){
            simulateButton.setVisible(false);

            score = new JLabel(fixture.getOutcome().getScoreString());
        }
        else{
            simulateButton.setVisible(true); 
        }

        this.add(homeTeam);
        this.add(score);
        this.add(awayTeam);

        this.add(new Label(" "));
        this.add(new Label(" "));
        this.add(new Label(" "));

        this.add(simulateButton);
    }

    //action listener for button
    public void actionPerformed(ActionEvent e)
    {
        if(simulateButton == e.getSource() && !fixture.hasPlayed()){
            mainWindow.simulateFixture(fixture);
        }
    }
}
