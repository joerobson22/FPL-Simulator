package Frontend;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainWindow extends JFrame implements ActionListener{
    private final String GAME_ENGINE_PATH = "Backend/GameEngine.exe";

    private final int WINDOW_WIDTH = 1500;
    private final int WINDOW_HEIGHT = 1000;

    private final String titleLabelFont = "SansSerif";
    private final int titleLabelSize = 40;
    private final Color titleLabelColor = new Color(255, 255, 255);

    JPanel mainPanel;
    JPanel contentPanel;
    JPanel titlePanel;
    JPanel teamSelectionPanel;
    JPanel fixtureListPanel;

    Color titlePanelBackgroundColor = new Color(85, 131, 237);
    Color teamSelectionPanelBackgroundColor = new Color(255, 255, 255);
    Color statsPanelBackgroundColor = new Color(255, 255, 255);
    Color fixtureListPanelBackgroundColor = new Color(255, 255, 255);

    FantasyTeam fantasyTeam;
    FixtureList fixtureList;
    StatsPanel statsPanel;
    FPLPanel fplPanel;
    ArrayList<Team> allTeams;
    ArrayList<Player> allPlayers;
    ArrayList<FixturePanel> fixturePanelList;
    PlayerStatsWindow psw;

    JButton simAllButton;
    JButton prevGWButton;
    JButton nextGWButton;

    int currentGameWeek = 0;
    int viewingGameWeek = 0;

    boolean simulating = false;
    boolean teamConfirmed = false;

    //setup methods

    public MainWindow(FantasyTeam fantasyTeam){
        setupTeams();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int oneThird = screenWidth / 3;

        this.fantasyTeam = fantasyTeam;

        Border blackline = BorderFactory.createLineBorder(Color.black);

        titlePanel = new JPanel(new BorderLayout());

        // left button
        prevGWButton = new JButton("<");
        prevGWButton.addActionListener(this);
        titlePanel.add(prevGWButton, BorderLayout.WEST);

        // center label
        JLabel titleLabel = LabelCreator.createJLabel(fantasyTeam.getName(), titleLabelFont, titleLabelSize, Font.BOLD, SwingConstants.CENTER, titleLabelColor);
        titlePanel.add(titleLabel, "Center");

        // right button
        nextGWButton = new JButton(">");
        nextGWButton.addActionListener(this);
        titlePanel.add(nextGWButton, BorderLayout.EAST);

        titlePanel.setBackground(titlePanelBackgroundColor);
        titlePanel.setBorder(blackline);

        teamSelectionPanel = new JPanel();
        teamSelectionPanel.setBackground(teamSelectionPanelBackgroundColor);
        teamSelectionPanel.setBorder(blackline);

        statsPanel = new StatsPanel(this, allTeams);
        statsPanel.setBackground(statsPanelBackgroundColor);
        statsPanel.setBorder(blackline);

        fplPanel = new FPLPanel(this, fantasyTeam, allPlayers, oneThird);
        teamSelectionPanel.add(fplPanel);

        fixtureListPanel = new JPanel(new GridLayout(11, 1));
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
        this.setTitle("FPL Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //Rectangle bounds = ge.getMaximumWindowBounds();
        //this.setBounds(bounds);

        fixtureList = new FixtureList();
        setupFixtures();
        updateAllVisuals();
    }

    private void setupTeams(){
        allTeams = IOHandler.readAllTeamData(allTeams);
        allPlayers = IOHandler.readAllPlayerData(allTeams, allPlayers);
    }

    private void setupFixtures(){
        fixtureList = IOHandler.readAllFixtures(allTeams);

        for(Fixture f : fixtureList.getAllFixtures()){
            f.getHomeTeam().addFixture(f);
            f.getAwayTeam().addFixture(f);
        }
    }

    private void setStats(int gameWeek){
        statsPanel.updateStats(allPlayers, gameWeek);
    }


    //method to show all the fixtures for the upcoming gameweek
    private void setFixtures(int gameWeek){
        fixtureListPanel.removeAll();
        fixturePanelList = new ArrayList<>();

        for(Fixture f : fixtureList.getFixtures(gameWeek)){
            FixturePanel fp = new FixturePanel(this, f, currentGameWeek == viewingGameWeek);
            fixtureListPanel.add(fp);
            fixturePanelList.add(fp);
        }

        if(teamConfirmed) confirmTeam();

        fixtureListPanel.revalidate();
        fixtureListPanel.repaint();
    }


    //method to update all the window's visuals- fixture panel, stats panel, fpl panel and input buttons
    public void updateAllVisuals(){
        setFixtures(viewingGameWeek);
        setStats(viewingGameWeek);
        fplPanel.changeGW(viewingGameWeek, currentGameWeek, teamConfirmed);

        showAndHideButtons();
    }

    public void showAndHideButtons(){
        if(viewingGameWeek == 0) prevGWButton.setVisible(false);
        else prevGWButton.setVisible(true);

        if(viewingGameWeek == 37) nextGWButton.setVisible(false);
        else nextGWButton.setVisible(true);
    }

    public void confirmTeam(){
        teamConfirmed = true;
        for(FixturePanel fp : fixturePanelList){
            fp.showSimulate();
        }

        simAllButton = new JButton("Simulate All");
        simAllButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        simAllButton.addActionListener(this);
        fixtureListPanel.add(simAllButton);

        fplPanel.canEdit(true);
    }


    //fixture simulating- communication with C++ backend
    public void simulateFixture(FixturePanel fixturePanel, Fixture fixture, boolean simulatingAll)
    {
        simulating = true;
        try{
            //write fixture details to a txt file
            System.out.println("Writing fixture details to text file");
            IOHandler.writeFixtureData(fixture, new Random().nextInt());

            //call the GameEngine.exe file
            System.out.println("Calling GameEngine.exe");

            //GET THE MATCH SIMULATOR .EXE FILE PATH
            File exeFile = new File(GAME_ENGINE_PATH).getCanonicalFile();
            System.out.println("GameEngine path: " + exeFile.getAbsolutePath());
            System.out.println("File exists: " + exeFile.exists());
            
            if (!exeFile.exists()) {
                System.err.println("ERROR: GameEngine.exe not found!");
                return;
            }
            
            //CREATE A NEW PROCESSBUILDER TO CALL THE MATCH SIMULATOR
            ProcessBuilder pb = new ProcessBuilder(exeFile.getAbsolutePath());
            pb.redirectErrorStream(true);

            pb.directory(new File("Backend"));
            System.out.println("Working directory: " + pb.directory().getAbsolutePath());
            
            //START THE MATCH SIMULATOR
            System.out.println("Calling GameEngine.exe");
            Process process = pb.start();

            // read the output stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            String line;
            System.out.println("=== GAME ENGINE OUTPUT ===");
            System.out.println();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();
            System.out.println("=== END ENGINE OUTPUT ===");

            System.out.println("Waiting for GameEngine.exe to finish running");
            int exitCode = process.waitFor();

            System.out.println("GameEngine.exe exited with code " + exitCode + "!");


            System.out.println("Complete!");

            //read the result of the fixture if exit code is 0
            if(exitCode == 0){
                System.out.println("Reading game results");

                //READ THE FIXTURE OUTCOME
                fixture.playFixture(IOHandler.readFixtureOutcome(allPlayers));
                fixturePanel.updateFixturePanel();
            }
            else{
                System.out.println("Failed");
            }
            

            

            System.out.println("Allocating points");
            //ONCE a fixture is complete, allocate points to every player that played!
            fixture.allocatePointsAndChangeStats(currentGameWeek);

            statsPanel.sortLeagueTable();

            if(!simulatingAll){
                System.out.println("Update stats");
                statsPanel.updateStats(allPlayers, currentGameWeek);
                fplPanel.updateTeamPoints();
                if(isGameWeekFinished(currentGameWeek)){
                    nextGameWeek();
                }
            }
        


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
        simulating = false;
    }

    //simulating all fixtures
    public void simulateAllFixtures(){
        int i = 0;
        for(Fixture f : fixtureList.getFixtures(currentGameWeek)){
            if(!f.hasPlayed()){
                simulateFixture((FixturePanel)fixtureListPanel.getComponent(i), f, true);
            }
            i++;
        }

        statsPanel.updateStats(allPlayers, currentGameWeek);
        fplPanel.updateTeamPoints();
        nextGameWeek();
    }


    public boolean isGameWeekFinished(int gameWeek){
        for(Fixture f : fixtureList.getFixtures(gameWeek)){
            if(!f.hasPlayed()) return false;
        }
        return true;
    }

    public void nextGameWeek(){
        currentGameWeek++;
        simAllButton.setVisible(false);
        teamConfirmed = false;
        fantasyTeam.saveTeam();
        fantasyTeam.resetWeeklyTotal();
        fantasyTeam.addFreeTransfer();
        fplPanel.unconfirmTeam();

        for(Player p : allPlayers){
            p.resetWeeklyPoints();
        }
    }

    public void viewPlayerStats(){
        if(psw != null) psw.setVisible(false);
        psw = new PlayerStatsWindow(allPlayers);
    }


    //action listener
    public void actionPerformed(ActionEvent e)
    {
        if(simulating) return;
        
        if(simAllButton == e.getSource()){
            simulateAllFixtures();
        }
        else if(prevGWButton == e.getSource() && viewingGameWeek > 0){
            viewingGameWeek--;
            fplPanel.changeGW(viewingGameWeek, currentGameWeek, teamConfirmed);
            updateAllVisuals();
        }
        else if(nextGWButton == e.getSource() && viewingGameWeek < 37){
            viewingGameWeek++;
            fplPanel.changeGW(viewingGameWeek, currentGameWeek, teamConfirmed);
            updateAllVisuals();
        }
    }
}
