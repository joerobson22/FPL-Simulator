package Java;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener{

    JPanel mainPanel;
    JPanel contentPanel;
    JPanel titlePanel;
    JPanel teamSelectionPanel;
    JPanel statsPanel;
    JPanel fixtureListPanel;

    Color titlePanelBackgroundColor = new Color(85, 131, 237);
    Color teamSelectionPanelBackgroundColor = new Color(255, 255, 255);
    Color statsPanelBackgroundColor = new Color(255, 255, 255);
    Color fixtureListPanelBackgroundColor = new Color(255, 255, 255);

    User user;
    FixtureList fixtureList;

    public MainWindow(User user){
        this.user = user;

        Border blackline = BorderFactory.createLineBorder(Color.black);

        titlePanel = new JPanel();
        titlePanel.setAlignmentY(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(user.getName());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        titleLabel.setForeground(new Color(255, 255, 255));

        titlePanel.add(titleLabel);
        titlePanel.setBackground(titlePanelBackgroundColor);
        titlePanel.setBorder(blackline);


        teamSelectionPanel = new JPanel();
        teamSelectionPanel.setBackground(teamSelectionPanelBackgroundColor);
        teamSelectionPanel.setBorder(blackline);

        statsPanel = new JPanel();
        statsPanel.setBackground(statsPanelBackgroundColor);
        statsPanel.setBorder(blackline);

        fixtureListPanel = new JPanel();
        fixtureListPanel.setBackground(fixtureListPanelBackgroundColor);
        fixtureListPanel.setBorder(blackline);

        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, 3));
        contentPanel.add(teamSelectionPanel);
        contentPanel.add(statsPanel);
        contentPanel.add(fixtureListPanel);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add("North", titlePanel);
        mainPanel.add("Center", contentPanel);

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(1500, 1000);
        this.setTitle("FPL Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        fixtureList = new FixtureList();

        testSetupTeams("Liverpool", "Chelsea");

        setFixtures(0);
    }

    private void testSetupTeams(String home, String away){
        fixtureList.addFixture(0, new Fixture(new Team(90, home), new Team(78, away)));
    }


    //displaying all the upcoming fixtures
    private void setFixtures(int gameWeek){
        fixtureListPanel.removeAll();

        for(Fixture f : fixtureList.getFixtures(gameWeek)){
            fixtureListPanel.add(new FixturePanel(this, f));
        }
    }


    //fixture simulating- communication with C++ backend
    public void simulateFixture(Fixture fixture)
    {

    }

    //action listener
    public void actionPerformed(ActionEvent e)
    {
        
    }
}
