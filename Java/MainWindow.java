package Java;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
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

        Team liverpool = new Team(90, "Liverpool");
        Team manCity = new Team(85, "Man City");

        liverpool.addPlayer(new Player(0, 95, 14.5, "ATT", "Salah", liverpool));
        liverpool.addPlayer(new Player(1, 90, 8.5, "MID", "Wirtz", liverpool));
        liverpool.addPlayer(new Player(2, 85, 6.0, "DEF", "Van Dijk", liverpool));
        liverpool.addPlayer(new Player(3, 85, 5.5, "GK", "Alisson", liverpool));

        manCity.addPlayer(new Player(4, 92, 14.0, "ATT", "Haaland", manCity));
        manCity.addPlayer(new Player(5, 84, 8.5, "MID", "Reijnders", manCity));
        manCity.addPlayer(new Player(6, 82, 6.0, "DEF", "Gvardiol", manCity));
        manCity.addPlayer(new Player(7, 80, 5.5, "GK", "Ederson", manCity));

        allTeams.add(liverpool);
        allTeams.add(manCity);
        /*
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
        */
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
            System.out.println("Writing fixture details to text file");
            IOHandler.writeFixtureData(fixture);

            //call the GameEngine.exe file
            System.out.println("Calling GameEngine.exe");


            File exeFile = new File("CPP/GameEngine.exe").getCanonicalFile();
            System.out.println("GameEngine path: " + exeFile.getAbsolutePath());
            System.out.println("File exists: " + exeFile.exists());
            
            if (!exeFile.exists()) {
                System.err.println("ERROR: GameEngine.exe not found!");
                return;
            }
            
            ProcessBuilder pb = new ProcessBuilder(exeFile.getAbsolutePath());
            pb.redirectErrorStream(true);
            
            System.out.println("Calling GameEngine.exe");
            Process process = pb.start();


            System.out.println("Waiting for GameEngine.exe to finish running");
            int exitCode = process.waitFor();

            System.out.println("GameEngine.exe exited with code " + exitCode + "!");

            //read the result of the fixture if exit code is 0
            if(exitCode == 0){
                System.out.println("Reading game results");
                fixture.playFixture(IOHandler.readFixtureOutcome());
            }
            else{
                System.out.println("Failed");
            }
            

            System.out.println("Complete!");
        }
        catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
            e.printStackTrace();
        } 
        catch (InterruptedException e) {
            System.err.println("Process was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } 
        catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    //action listener
    public void actionPerformed(ActionEvent e)
    {
        
    }
}
