package Frontend;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class PlayerStatsWindow extends JFrame{

    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 400;

    private final String font = "SansSerif";
    private final int columnTitleSize = 15;
    private final int columnBodySize = 12;

    private final String padding = "    ";

    JPanel mainPanel;
    JPanel topPointsPanel;
    JPanel topScorerPanel;
    JPanel topAssistsPanel;
    JPanel topCleanSheetsPanel;

    ArrayList<Player> allPlayers;

    String[] titles = {"Points", "Goals", "Assists", "Clean Sheets"};

    Border blackline = BorderFactory.createLineBorder(Color.black);

    public PlayerStatsWindow(ArrayList<Player> allPlayers){
        this.allPlayers = allPlayers;

        setup();
    }

    public void setup(){
        topPointsPanel = setupColumnPanel(titles[0], getTopPointsList());
        topScorerPanel = setupColumnPanel(titles[1], getTopScorerList());
        topAssistsPanel = setupColumnPanel(titles[2], getTopAssistsList());
        topCleanSheetsPanel = setupColumnPanel(titles[3], getTopCleanSheetsList());

        mainPanel = new JPanel(new GridLayout(1, 4));
        mainPanel.add(topPointsPanel);
        mainPanel.add(topScorerPanel);
        mainPanel.add(topAssistsPanel);
        mainPanel.add(topCleanSheetsPanel);

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle("FPL Simulator- Player Stats");
        this.setResizable(false);
    }

    public ArrayList<Player> getTopPointsList(){
        ArrayList<Player> finalList = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            int top = Integer.MIN_VALUE;
            Player bestPlayer = allPlayers.get(0);
            for(Player p : allPlayers){
                if(p.getTotalPoints() > top && !finalList.contains(p)){
                    bestPlayer = p;
                    top = p.getTotalPoints();
                }
            }
            finalList.add(bestPlayer);
        }
        
        return finalList;
    }

    public ArrayList<Player> getTopScorerList(){
        ArrayList<Player> finalList = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            int top = Integer.MIN_VALUE;
            Player bestPlayer = allPlayers.get(0);
            for(Player p : allPlayers){
                if(p.getNumGoals() > top && !finalList.contains(p)){
                    bestPlayer = p;
                    top = p.getNumGoals();
                }
            }
            finalList.add(bestPlayer);
        }
        
        return finalList;
    }

    public ArrayList<Player> getTopAssistsList(){
        ArrayList<Player> finalList = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            int top = Integer.MIN_VALUE;
            Player bestPlayer = allPlayers.get(0);
            for(Player p : allPlayers){
                if(p.getNumAssists() > top && !finalList.contains(p)){
                    bestPlayer = p;
                    top = p.getNumAssists();
                }
            }
            finalList.add(bestPlayer);
        }
        
        return finalList;
    }

    public ArrayList<Player> getTopCleanSheetsList(){
        ArrayList<Player> finalList = new ArrayList<>();

        for(int i = 0; i < 10; i++)
        {
            int top = Integer.MIN_VALUE;
            Player bestPlayer = allPlayers.get(0);
            for(Player p : allPlayers){
                if(p.getNumCleanSheets() > top && !finalList.contains(p) && p.getGeneralPosition().equals("GK")){
                    bestPlayer = p;
                    top = p.getNumCleanSheets();
                }
            }
            finalList.add(bestPlayer);
        }
        
        return finalList;
    }

    public JPanel setupColumnPanel(String title, ArrayList<Player> top10){
        JPanel panel = new JPanel(new GridLayout(11, 1));
        panel.setBorder(blackline);
        JLabel titleLabel = LabelCreator.createJLabel(padding + title, font, columnTitleSize, Font.BOLD, SwingConstants.LEFT, Color.BLACK);
        panel.add(titleLabel);

        for(int i = 0; i < 10; i++){
            Player p = top10.get(i);
            int quantity = 0;

            if(title == "Points") quantity = p.getTotalPoints();
            else if(title == "Goals") quantity = p.getNumGoals();
            else if(title == "Assists") quantity = p.getNumAssists();
            else if(title == "Clean Sheets") quantity = p.getNumCleanSheets();

            String text = String.valueOf(quantity) + " " + "| " + p.getTeam().getAbbrv() + " (" + p.getGeneralPosition() + ")  " + p.getName();
            JLabel entryLabel = LabelCreator.createJLabel(padding + text, font, columnBodySize, Font.PLAIN, SwingConstants.LEFT, Color.BLACK);

            panel.add(entryLabel);
        }

        return panel;
    }
}
