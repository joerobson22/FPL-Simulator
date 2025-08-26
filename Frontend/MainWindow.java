package Frontend;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.lang.Math;

public class MainWindow extends JFrame implements ActionListener{
    private final String GAME_ENGINE_PATH = "Backend/GameEngine.exe";

    JPanel mainPanel;
    JPanel contentPanel;
    JPanel titlePanel;
    JPanel teamSelectionPanel;
    JPanel fixtureListPanel;

    Color titlePanelBackgroundColor = new Color(85, 131, 237);
    Color teamSelectionPanelBackgroundColor = new Color(255, 255, 255);
    Color statsPanelBackgroundColor = new Color(255, 255, 255);
    Color fixtureListPanelBackgroundColor = new Color(255, 255, 255);

    User user;
    FixtureList fixtureList;
    StatsPanel statsPanel;
    ArrayList<Team> allTeams;
    ArrayList<Player> allPlayers;

    JButton simAllButton;
    JButton prevGWButton;
    JButton nextGWButton;

    int currentGameWeek = 0;
    int viewingGameWeek = 0;

    public MainWindow(User user){
        this.user = user;

        Border blackline = BorderFactory.createLineBorder(Color.black);

        titlePanel = new JPanel(new BorderLayout());

        // left button
        prevGWButton = new JButton("<");
        prevGWButton.addActionListener(this);
        titlePanel.add(prevGWButton, BorderLayout.WEST);

        // center label
        JLabel titleLabel = new JLabel(user.getName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        titleLabel.setForeground(new Color(255, 255, 255));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // right button
        nextGWButton = new JButton(">");
        nextGWButton.addActionListener(this);
        titlePanel.add(nextGWButton, BorderLayout.EAST);

        titlePanel.setBackground(titlePanelBackgroundColor);
        titlePanel.setBorder(blackline);

        teamSelectionPanel = new JPanel();
        teamSelectionPanel.setBackground(teamSelectionPanelBackgroundColor);
        teamSelectionPanel.setBorder(blackline);

        statsPanel = new StatsPanel();
        statsPanel.setBackground(statsPanelBackgroundColor);
        statsPanel.setBorder(blackline);

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
        this.setSize(1500, 1000);
        this.setTitle("FPL Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        fixtureList = new FixtureList();

        setupTeams();
        setupFixtures();

        updateAllVisuals();
    }

    public void updateAllVisuals(){
        setFixtures(viewingGameWeek);
        setStats(viewingGameWeek);

        showAndHideButtons();
    }

    public void showAndHideButtons(){
        if(viewingGameWeek == 0) prevGWButton.setVisible(false);
        else prevGWButton.setVisible(true);

        if(viewingGameWeek == 37) nextGWButton.setVisible(false);
        else nextGWButton.setVisible(true);
    }

    private void setupTeams(){
        allTeams = new ArrayList<>();

        Team liverpool = new Team(90, "Liverpool");
        Team manCity = new Team(85, "Man City");
        Team nttmForest = new Team(78, "Nottingham Forest");
        Team arsenal = new Team(88, "Arsenal");

        liverpool.addPlayer(new Player(0, 95, 14.5, "ATT", "Salah", liverpool));
        liverpool.addPlayer(new Player(1, 90, 8.5, "MID", "Wirtz", liverpool));
        liverpool.addPlayer(new Player(2, 85, 6.0, "DEF", "Van Dijk", liverpool));
        liverpool.addPlayer(new Player(3, 85, 5.5, "GK", "Alisson", liverpool));

        manCity.addPlayer(new Player(4, 92, 14.0, "ATT", "Haaland", manCity));
        manCity.addPlayer(new Player(5, 84, 8.5, "MID", "Reijnders", manCity));
        manCity.addPlayer(new Player(6, 82, 6.0, "DEF", "Gvardiol", manCity));
        manCity.addPlayer(new Player(7, 80, 5.5, "GK", "Ederson", manCity));

        nttmForest.addPlayer(new Player(8, 80, 7.5, "ATT", "Wood", nttmForest));
        nttmForest.addPlayer(new Player(9, 82, 8.5, "MID", "Gibbs-White", nttmForest));
        nttmForest.addPlayer(new Player(10, 83, 6.0, "DEF", "Murillo", nttmForest));
        nttmForest.addPlayer(new Player(11, 80, 5.0, "GK", "Sels", nttmForest));

        arsenal.addPlayer(new Player(12, 86, 9.0, "ATT", "Gyokeres", arsenal));
        arsenal.addPlayer(new Player(13, 90, 10.0, "MID", "Saka", arsenal));
        arsenal.addPlayer(new Player(14, 85, 6.0, "DEF", "Saliba", arsenal));
        arsenal.addPlayer(new Player(15, 85, 5.5, "GK", "Raya", arsenal));

        allTeams.add(liverpool);
        allTeams.add(manCity);
        allTeams.add(nttmForest);
        allTeams.add(arsenal);
        
        Team chelsea = new Team(84, "Chelsea");
        Team tottenham = new Team(83, "Tottenham");
        Team astonVilla = new Team(82, "Aston Villa");
        Team newcastle = new Team(83, "Newcastle");
        Team brighton = new Team(80, "Brighton");
        Team westHam = new Team(79, "West Ham");

        // Chelsea Players
        chelsea.addPlayer(new Player(16, 88, 12.0, "ATT", "Palmer", chelsea));
        chelsea.addPlayer(new Player(17, 85, 9.5, "MID", "Fernandez", chelsea));
        chelsea.addPlayer(new Player(18, 83, 7.0, "DEF", "James", chelsea));
        chelsea.addPlayer(new Player(19, 82, 5.0, "GK", "Petrovic", chelsea));

        // Tottenham Players
        tottenham.addPlayer(new Player(20, 89, 13.0, "ATT", "Son", tottenham));
        tottenham.addPlayer(new Player(21, 86, 9.0, "MID", "Maddison", tottenham));
        tottenham.addPlayer(new Player(22, 82, 6.5, "DEF", "Romero", tottenham));
        tottenham.addPlayer(new Player(23, 81, 5.0, "GK", "Vicario", tottenham));

        // Aston Villa Players
        astonVilla.addPlayer(new Player(24, 87, 11.5, "ATT", "Watkins", astonVilla));
        astonVilla.addPlayer(new Player(25, 86, 8.5, "MID", "Luiz", astonVilla));
        astonVilla.addPlayer(new Player(26, 83, 6.0, "DEF", "Pau Torres", astonVilla));
        astonVilla.addPlayer(new Player(27, 82, 4.5, "GK", "Martinez", astonVilla));

        // Newcastle Players
        newcastle.addPlayer(new Player(28, 88, 12.5, "ATT", "Isak", newcastle));
        newcastle.addPlayer(new Player(29, 85, 8.0, "MID", "Guimaraes", newcastle));
        newcastle.addPlayer(new Player(30, 84, 6.5, "DEF", "Botman", newcastle));
        newcastle.addPlayer(new Player(31, 83, 5.0, "GK", "Pope", newcastle));

        // Brighton Players
        brighton.addPlayer(new Player(32, 85, 10.0, "ATT", "Ferguson", brighton));
        brighton.addPlayer(new Player(33, 84, 8.0, "MID", "Gilmour", brighton));
        brighton.addPlayer(new Player(34, 80, 5.5, "DEF", "Dunk", brighton));
        brighton.addPlayer(new Player(35, 78, 4.0, "GK", "Verbruggen", brighton));

        // West Ham Players
        westHam.addPlayer(new Player(36, 84, 9.5, "ATT", "Kudus", westHam));
        westHam.addPlayer(new Player(37, 83, 7.5, "MID", "Paqueta", westHam));
        westHam.addPlayer(new Player(38, 81, 5.0, "DEF", "Aguerd", westHam));
        westHam.addPlayer(new Player(39, 79, 4.0, "GK", "Areola", westHam));

        // Add all new teams to the list
        allTeams.add(chelsea);
        allTeams.add(tottenham);
        allTeams.add(astonVilla);
        allTeams.add(newcastle);
        allTeams.add(brighton);
        allTeams.add(westHam);

        Team manUnited = new Team(83, "Man United");
        Team crystalPalace = new Team(78, "Crystal Palace");
        Team fulham = new Team(77, "Fulham");
        Team bournemouth = new Team(76, "Bournemouth");
        Team wolves = new Team(77, "Wolves");
        Team everton = new Team(76, "Everton");
        Team brentford = new Team(76, "Brentford");
        Team southampton = new Team(75, "Southampton");
        Team leicester = new Team(75, "Leicester");
        Team ipswich = new Team(74, "Ipswich");

        // Man United Players
        manUnited.addPlayer(new Player(40, 87, 13.0, "ATT", "Højlund", manUnited));
        manUnited.addPlayer(new Player(41, 86, 10.5, "MID", "Fernandes", manUnited));
        manUnited.addPlayer(new Player(42, 83, 7.0, "DEF", "Martínez", manUnited));
        manUnited.addPlayer(new Player(43, 82, 5.5, "GK", "Onana", manUnited));

        // Crystal Palace Players
        crystalPalace.addPlayer(new Player(44, 85, 10.5, "ATT", "Eze", crystalPalace));
        crystalPalace.addPlayer(new Player(45, 84, 9.5, "MID", "Olise", crystalPalace));
        crystalPalace.addPlayer(new Player(46, 80, 5.0, "DEF", "Andersen", crystalPalace));
        crystalPalace.addPlayer(new Player(47, 78, 4.0, "GK", "Henderson", crystalPalace));

        // Fulham Players
        fulham.addPlayer(new Player(48, 82, 8.5, "ATT", "Muniz", fulham));
        fulham.addPlayer(new Player(49, 81, 7.5, "MID", "Pereira", fulham));
        fulham.addPlayer(new Player(50, 79, 4.5, "DEF", "Bassey", fulham));
        fulham.addPlayer(new Player(51, 77, 3.5, "GK", "Leno", fulham));

        // Bournemouth Players
        bournemouth.addPlayer(new Player(52, 83, 9.0, "ATT", "Solanke", bournemouth));
        bournemouth.addPlayer(new Player(53, 79, 6.5, "MID", "Scott", bournemouth));
        bournemouth.addPlayer(new Player(54, 78, 4.5, "DEF", "Zabarnyi", bournemouth));
        bournemouth.addPlayer(new Player(55, 76, 3.5, "GK", "Neto", bournemouth));

        // Wolves Players
        wolves.addPlayer(new Player(56, 84, 9.5, "ATT", "Cunha", wolves));
        wolves.addPlayer(new Player(57, 80, 6.0, "MID", "Gomes", wolves));
        wolves.addPlayer(new Player(58, 79, 4.5, "DEF", "Kilman", wolves));
        wolves.addPlayer(new Player(59, 77, 3.5, "GK", "Bentley", wolves));

        // Everton Players
        everton.addPlayer(new Player(60, 81, 8.0, "ATT", "Calvert-Lewin", everton));
        everton.addPlayer(new Player(61, 80, 6.5, "MID", "Onana", everton));
        everton.addPlayer(new Player(62, 79, 4.5, "DEF", "Tarkowski", everton));
        everton.addPlayer(new Player(63, 78, 3.5, "GK", "Pickford", everton));

        // Brentford Players
        brentford.addPlayer(new Player(64, 82, 8.5, "ATT", "Toney", brentford));
        brentford.addPlayer(new Player(65, 79, 6.0, "MID", "Nørgaard", brentford));
        brentford.addPlayer(new Player(66, 78, 4.5, "DEF", "Collins", brentford));
        brentford.addPlayer(new Player(67, 76, 3.5, "GK", "Flekken", brentford));

        // Southampton Players (Promoted)
        southampton.addPlayer(new Player(68, 80, 7.5, "ATT", "Armstrong", southampton));
        southampton.addPlayer(new Player(69, 78, 6.0, "MID", "Smallbone", southampton));
        southampton.addPlayer(new Player(70, 76, 4.0, "DEF", "THB", southampton));
        southampton.addPlayer(new Player(71, 75, 3.0, "GK", "McCarthy", southampton));

        // Leicester Players (Promoted)
        leicester.addPlayer(new Player(72, 82, 8.0, "ATT", "Vardy", leicester));
        leicester.addPlayer(new Player(73, 80, 7.0, "MID", "Winks", leicester));
        leicester.addPlayer(new Player(74, 77, 4.5, "DEF", "Faes", leicester));
        leicester.addPlayer(new Player(75, 76, 3.5, "GK", "Hermansen", leicester));

        // Ipswich Players (Promoted)
        ipswich.addPlayer(new Player(76, 78, 6.5, "ATT", "Moore", ipswich));
        ipswich.addPlayer(new Player(77, 77, 5.5, "MID", "Luongo", ipswich));
        ipswich.addPlayer(new Player(78, 75, 4.0, "DEF", "Burgess", ipswich));
        ipswich.addPlayer(new Player(79, 74, 3.0, "GK", "Hladky", ipswich));

        // Add all new teams to the list
        allTeams.add(manUnited);
        allTeams.add(crystalPalace);
        allTeams.add(fulham);
        allTeams.add(bournemouth);
        allTeams.add(wolves);
        allTeams.add(everton);
        allTeams.add(brentford);
        allTeams.add(southampton);
        allTeams.add(leicester);
        allTeams.add(ipswich);

        allPlayers = new ArrayList<Player>();
        for(Team t : allTeams){
            for(Player p : t.getPlayers()){
                allPlayers.add(p);
            }
        }

        setStats(currentGameWeek);
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

        Team awayTeam = null;
        for(int i = 0; i < allTeams.size(); i++)
        {
            if(i % 2 == 0){
                awayTeam = allTeams.get(i);
            }
            else{
                fixtureList.addFixture(1, new Fixture(allTeams.get(i), awayTeam));
            }
        }
    }

    private void setStats(int gameWeek){
        statsPanel.updateStats(allPlayers, gameWeek);
    }

    //displaying all the upcoming fixtures
    private void setFixtures(int gameWeek){
        fixtureListPanel.removeAll();

        for(Fixture f : fixtureList.getFixtures(gameWeek)){
            FixturePanel fp = new FixturePanel(this, f, currentGameWeek == viewingGameWeek);
            fixtureListPanel.add(fp);
        }

        if(currentGameWeek == viewingGameWeek){
            simAllButton = new JButton("Simulate All");
            simAllButton.addActionListener(this);
            fixtureListPanel.add(simAllButton);
        }

        fixtureListPanel.revalidate();
        fixtureListPanel.repaint();
    }

    public void simulateAllFixtures(){
        int i = 0;
        for(Fixture f : fixtureList.getFixtures(currentGameWeek)){
            if(!f.hasPlayed()){
                simulateFixture((FixturePanel)fixtureListPanel.getComponent(i), f, true);
            }
            i++;
        }

        statsPanel.updateStats(allPlayers, currentGameWeek);
        nextGameWeek();
    }


    //fixture simulating- communication with C++ backend
    public void simulateFixture(FixturePanel fixturePanel, Fixture fixture, boolean simulatingAll)
    {
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
            

            System.out.println("Complete!");

            //ONCE a fixture is complete, allocate points to every player that played!
            fixture.allocatePointsAndChangeStats();

            if(!simulatingAll){
                statsPanel.updateStats(allPlayers, currentGameWeek);
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

        for(Player p : allPlayers){
            p.resetWeeklyPoints();
        }
    }


    //action listener
    public void actionPerformed(ActionEvent e)
    {
        if(simAllButton == e.getSource()){
            simulateAllFixtures();
        }
        else if(prevGWButton == e.getSource() && viewingGameWeek > 0){
            viewingGameWeek--;
            updateAllVisuals();
        }
        else if(nextGWButton == e.getSource() && viewingGameWeek < 37){
            viewingGameWeek++;
            updateAllVisuals();
        }
    }
}
