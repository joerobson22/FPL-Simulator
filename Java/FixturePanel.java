package Java;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FixturePanel extends JPanel implements ActionListener{
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
        displayFixture();

        simulateButton.addActionListener(this);
    }

    //methods
    public void displayFixture(){
        homeTeam.setText(fixture.getHomeTeam().getName());
        awayTeam.setText(fixture.getAwayTeam().getName());

        if(fixture.hasPlayed()){
            simulateButton.setVisible(false);

            score.setText(" vs ");
        }
        else{
            simulateButton.setVisible(true);

            score.setText(fixture.getOutcome().getScoreString());
        }
    }

    //action listener for button
    public void actionPerformed(ActionEvent e)
    {
        if(simulateButton == e.getSource() && !fixture.hasPlayed()){
            mainWindow.simulateFixture(fixture);
        }
    }
}
