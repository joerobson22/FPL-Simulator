package Java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public class StatsPanel extends JPanel implements ActionListener{
    private final String topScorerFont = "SansSerif";
    private final int topScorerTitleSize = 20;
    private final int topScorerTableSize = 15;

    private JPanel topScorerPanel;
    private JPanel topScorerContentPanel;
    private JPanel buttonsPanel;

    private JButton viewLeagueButton;
    private JButton viewLeaderboardButton;

    private Map<String, ArrayList<JLabel>> topScorersMap;

    String[] positions = {"GK", "DEF", "MID", "ATT"};

    public StatsPanel(){
        //set this panel's layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        //setup weekly top scorer panel
        //create title panel
        topScorerPanel = new JPanel(new BorderLayout());
        JPanel topScorerTitlePanel = new JPanel();
        JLabel topScorerTitleLabel = new JLabel("Weekly Top Scorers");
        topScorerTitleLabel.setFont(new Font(topScorerFont, Font.BOLD, topScorerTitleSize));
        topScorerTitlePanel.add(topScorerTitleLabel);
        //create table of top scorers
        topScorerContentPanel = new JPanel(new GridLayout(1, 4));

        //setup topscorers hashmap and create new JLabels for the titles
        topScorersMap = new HashMap<>();
        for(String s : positions){
            JPanel column = new JPanel();
            column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
            column.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            JLabel tableHeader = setupDefaultLabel(s, topScorerFont, topScorerTableSize, Font.BOLD, SwingConstants.CENTER);
            //tableHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            column.add(tableHeader);

            topScorersMap.put(s, new ArrayList<>());

            for(int i = 1; i < 11; i++)
            {
                JLabel label = setupDefaultLabel(String.valueOf(i), topScorerFont, topScorerTableSize, Font.PLAIN, SwingConstants.LEFT);
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
                topScorersMap.get(s).add(label);
                column.add(label);
                column.add(Box.createVerticalStrut(4));
            }
            topScorerContentPanel.add(column);
        }

        //add both sub panels to the top scorer panel
        topScorerPanel.add(topScorerTitlePanel, "North");
        topScorerPanel.add(topScorerContentPanel, "Center");


        //create buttons panel
        buttonsPanel = new JPanel();
        //create associated buttons
        viewLeagueButton = new JButton("League >>");
        viewLeaderboardButton = new JButton("Leaderboards >>");
        //add to button panel
        buttonsPanel.add(viewLeagueButton);
        buttonsPanel.add(viewLeaderboardButton);

        //add both top scorer panel and buttons panel to the stats panel
        this.add(topScorerPanel);
        this.add(buttonsPanel);
    }

    public JLabel setupDefaultLabel(String text, String font, int fontSize, int fontType,  int hAlignment){
        JLabel label = new JLabel(text, hAlignment);
        label.setForeground(new Color(0, 0, 0));
        label.setFont(new Font(font, fontType, fontSize));

        return label;
    }

    public void updateStats(int gameWeek){

    }

    public void actionPerformed(ActionEvent e){

    }
}
