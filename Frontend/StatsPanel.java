package Frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;

public class StatsPanel extends JPanel implements ActionListener{
    MainWindow mainWindow;

    private final String topScorerFont = "SansSerif";
    private final int topScorerTitleSize = 25;
    private final int topScorerTableSize = 11;
    private final int rowGaps = 8;
    private final int padding = 10;
    private final int leagueTableStatHeaderSize = 13;
    private final int leagueTablePointHeaderSize = 15;

    private JPanel topScorerPanel;
    private JPanel topScorerContentPanel;
    private JPanel buttonsPanel;

    private JPanel leagueTablePanel;

    private JButton viewPlayerStatsButton;
    private JButton viewLeaderboardButton;

    private JLabel topScorerTitleLabel;

    private Map<String, ArrayList<JLabel>> topScorersMap;
    ArrayList<Team> teams;
    ArrayList<LeagueTableTeamPanel> leagueTable;

    String[] positions = {"GK", "DEF", "MID", "ATT"};

    public StatsPanel(MainWindow mainWindow, ArrayList<Team> teams){
        this.mainWindow = mainWindow;

        //set this panel's layout
        this.setLayout(new BorderLayout());

        Border blackLine = BorderFactory.createLineBorder(Color.BLACK);

        //setup weekly top scorer panel
        //create title panel
        topScorerPanel = new JPanel(new BorderLayout());
        JPanel topScorerTitlePanel = new JPanel();
        topScorerTitleLabel = LabelCreator.createJLabel("Weekly Top Scorers", topScorerFont, topScorerTitleSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        topScorerTitlePanel.add(topScorerTitleLabel);
        
        //create table of top scorers
        topScorerContentPanel = new JPanel(new GridLayout(1, 4));

        //setup topscorers hashmap and create new JLabels for the titles
        topScorersMap = new HashMap<>();
        for(String s : positions){
            JPanel column = new JPanel();
            column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
            column.setBorder(BorderFactory.createEmptyBorder(10, padding, 10, padding));

            JLabel tableHeader = LabelCreator.createJLabel(s, topScorerFont, topScorerTableSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
            //tableHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            column.add(tableHeader);

            topScorersMap.put(s, new ArrayList<>());

            for(int i = 1; i < 11; i++)
            {
                JLabel label = LabelCreator.createJLabel(String.valueOf(i), topScorerFont, topScorerTableSize, Font.PLAIN, SwingConstants.LEFT, Color.BLACK);
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
                topScorersMap.get(s).add(label);
                column.add(label);
                column.add(Box.createVerticalStrut(rowGaps));
            }
            topScorerContentPanel.add(column);
        }

        //add both sub panels to the top scorer panel
        topScorerPanel.add(topScorerTitlePanel, "North");
        topScorerPanel.add(topScorerContentPanel, "Center");


        this.teams = teams;
        leagueTable = new ArrayList<>();
        leagueTablePanel = new JPanel();
        leagueTablePanel.setLayout(new BoxLayout(leagueTablePanel, BoxLayout.Y_AXIS));
        leagueTablePanel.setBorder(blackLine);

        //create headers
        JPanel headerPanel = new JPanel(new GridLayout(1, 2));
        headerPanel.add(new JLabel(""));
        JPanel headerPanel2 = new JPanel(new GridLayout(1, 5));
        headerPanel2.add(LabelCreator.createJLabel("W", topScorerFont, leagueTableStatHeaderSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        headerPanel2.add(LabelCreator.createJLabel("D", topScorerFont, leagueTableStatHeaderSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        headerPanel2.add(LabelCreator.createJLabel("L", topScorerFont, leagueTableStatHeaderSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        headerPanel2.add(LabelCreator.createJLabel("GD", topScorerFont, leagueTableStatHeaderSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        headerPanel2.add(LabelCreator.createJLabel("PTS", topScorerFont, leagueTablePointHeaderSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        headerPanel.add(headerPanel2);

        leagueTablePanel.add(headerPanel);

        for(int i = 0; i < teams.size(); i++){
            leagueTable.add(new LeagueTableTeamPanel(teams.get(i), i + 1));
        }

        for(LeagueTableTeamPanel panel : leagueTable){
            leagueTablePanel.add(panel);
        }


        //create buttons panel
        buttonsPanel = new JPanel();
        //create associated buttons
        viewPlayerStatsButton = new JButton("Player Stats >>");
        viewPlayerStatsButton.setFont(new Font("SansSerif", Font.PLAIN, 15));
        viewPlayerStatsButton.addActionListener(this);
        viewLeaderboardButton = new JButton("Leaderboards >>");
        viewLeaderboardButton.setFont(new Font("SansSerif", Font.PLAIN, 15));
        viewLeaderboardButton.addActionListener(this);
        //add to button panel
        buttonsPanel.add(viewPlayerStatsButton);
        buttonsPanel.add(viewLeaderboardButton);

        //add both top scorer panel and buttons panel to the stats panel
        this.add(topScorerPanel, "North");
        this.add(leagueTablePanel, "Center");
        this.add(buttonsPanel, "South");
    }

    public void updateStats(ArrayList<Player> allPlayers, int gameWeek){
        //SORT ALL PLAYERS BY POINTS SCORED THIS GAMEWEEK
        Map<String, ArrayList<Player>> players = new HashMap<>();

        topScorerTitleLabel.setText("Gameweek " + String.valueOf(gameWeek + 1) + " Top Scorers");

        for(String p : positions){
            players.put(p, new ArrayList<>());
        }
        for(Player p : allPlayers){
            players.get(p.getGeneralPosition()).add(p);
        }

        for(String p : positions){
            ArrayList<Player> playerList = players.get(p);
            players.put(p, bubbleSortPlayers(playerList, gameWeek));

            for(int i = 1; i < Math.min(playerList.size() + 1, 11); i++){
                Player player = playerList.get(i - 1);
                String text = String.valueOf(player.getGameWeekPoints(gameWeek)) + "pts| " + player.getName();
                topScorersMap.get(p).get(i - 1).setText(text);
            }
        }
    }

    public void sortLeagueTable(){
        //SORT THE LEAGUE TABLE
        bubbleSortLeagueTable();
        //update the league table visuals
        for(LeagueTableTeamPanel panel : leagueTable){
            panel.update();
        }
    }

    public ArrayList<Player> bubbleSortPlayers(ArrayList<Player> players, int gameWeek){
        boolean swaps = true;
        while(swaps){
            swaps = false;
            int n = players.size();
            for(int i = 1; i < n; i++){
                if(players.get(i).getGameWeekPoints(gameWeek) > players.get(i - 1).getGameWeekPoints(gameWeek)){
                    //swap the players
                    Player temp = players.get(i);
                    players.set(i, players.get(i - 1));
                    players.set(i - 1, temp);
                    
                    swaps = true;
                }
            }
            n--;
        }

        return players;
    }

    public void bubbleSortLeagueTable(){
        boolean swaps = true;
        while(swaps){
            swaps = false;
            for(int i = 1; i < leagueTable.size(); i++){
                LeagueTableTeamPanel current = leagueTable.get(i);
                LeagueTableTeamPanel prev = leagueTable.get(i - 1);
                if(current.checkSwapWith(prev)){
                    swaps = true;
                    current.swapTeamWith(prev);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e){
        if(viewPlayerStatsButton == e.getSource()){
            mainWindow.viewPlayerStats();
        }
    }
}
