package Java;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    ArrayList<Team> allTeams;

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

        setupTeams();
        setupFixtures();

        setFixtures(0);
    }

    private void setupTeams(){
        allTeams = new ArrayList<>();

        allTeams.add(new Team(90, "Liverpool"));
        allTeams.add(new Team(85, "Man City"));
        allTeams.add(new Team(60, "Sunderland"));
        allTeams.add(new Team(75, "Tottenham"));
        allTeams.add(new Team(78, "Nottingham Forest"));
        allTeams.add(new Team(88, "Arsenal"));
        allTeams.add(new Team(56, "Leeds"));
        allTeams.add(new Team(70, "Brighton"));
        allTeams.add(new Team(65, "Fulham"));
        allTeams.add(new Team(80, "Aston Villa"));
        allTeams.add(new Team(82, "Chelsea"));
        allTeams.add(new Team(76, "Palace"));
        allTeams.add(new Team(81, "Newcastle"));
        allTeams.add(new Team(69, "Everton"));
        allTeams.add(new Team(73, "Man United"));
        allTeams.add(new Team(72, "Bournemouth"));
        allTeams.add(new Team(59, "Brentford"));
        allTeams.add(new Team(50, "Burnley"));
        allTeams.add(new Team(60, "West Ham"));
        allTeams.add(new Team(54, "Wolves"));
    }

    private void setupFixtures(){
        fixtureList = new FixtureList();

        Team homeTeam = null;
        for(int i = 0; i < allTeams.size(); i++)
        {
            if(i % 2 == 0){
                homeTeam = allTeams.get(i);
            }
            else{
                fixtureList.addFixture(0, new Fixture(homeTeam, allTeams.get(i)));
            }
        }
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
        try{
            //write fixture details to a JSON file

            //call the GameEngine.exe file
            Process process = new ProcessBuilder("../CPP/GameEngine").start();
            process.waitFor();

            //read the result of the fixture
            fixture.playFixture(IOHandler.readFixtureOutcome());
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
        catch(InterruptedException e){
            System.out.println("Interrupted");
        }
        
    }

    //action listener
    public void actionPerformed(ActionEvent e)
    {
        
    }
}
