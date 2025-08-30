package Frontend;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class FPLPanel extends JPanel implements ActionListener{

    private MainWindow mainWindow;
    private ArrayList<Player> allPlayers;

    private Image backgroundImage;
    private final String imagePath = "Frontend/assets/pitch.png";

    private JPanel topPanel; //contains a display for all the players
    private JPanel bottomPanel; //contains bench, input buttons and transfer panel

    private JPanel teamPanel;
    private JPanel benchPanel;
    private JPanel inputButtonPanel;
    private JScrollPane transferScrollPanel;
    private JPanel transferPanel;

    private JButton captainButton;
    private JButton viceCaptainButton;
    private JButton subButton; //click player, click sub, click player to sub with
    private JButton transferButton; //turns into confirm transfer button -> click player, click transfer, click new player, click confirm

    private final String[] positions = {"GK", "DEF", "MID", "ATT"};

    private Map<String, JPanel> teamSections;

    private final Map<String, Integer> positionCaps = Map.of(
        "GK", 2,
        "DEF", 5,
        "MID", 5,
        "ATT", 3
    );

    private final Map<String, Integer> startFormation = Map.of(
        "GK", 1,
        "DEF", 4,
        "MID", 4,
        "ATT", 2
    );

    private ArrayList<PlayerButton> playerButtons;

    FantasyTeam fantasyTeam;

    public FPLPanel(MainWindow mainWindow, FantasyTeam fantasyTeam, ArrayList<Player> allPlayers){
        this.setLayout(new GridLayout(2, 1));
        this.mainWindow = mainWindow;
        this.fantasyTeam = fantasyTeam;
        this.allPlayers = allPlayers;

        Border blackline = BorderFactory.createLineBorder(Color.black);

        //create top and bottom panels
        topPanel = new BackgroundPanel(imagePath);
        bottomPanel = new JPanel(new BorderLayout());

        //create every panel
        teamPanel = new JPanel(new GridLayout(4, 1));
        teamPanel.setOpaque(false);
        benchPanel = new JPanel();
        inputButtonPanel = new JPanel(new BorderLayout());
        transferPanel = new JPanel();
        transferPanel.setLayout(new BoxLayout(transferPanel, BoxLayout.Y_AXIS));
        transferScrollPanel = new JScrollPane();

        transferScrollPanel.add(transferPanel);

        JPanel captainPanel = new JPanel();
        captainButton = new JButton("C");
        viceCaptainButton = new JButton("V");
        JPanel teamSelectionPanel = new JPanel();
        subButton = new JButton("Sub");
        transferButton = new JButton("Transfer");

        captainPanel.add(captainButton);
        captainPanel.add(viceCaptainButton);
        teamSelectionPanel.add(subButton);
        teamSelectionPanel.add(transferButton);

        inputButtonPanel.add(captainPanel, "West");
        inputButtonPanel.add(teamSelectionPanel, "Center");

        //top panel add background image

        topPanel.add(teamPanel);
        bottomPanel.add(benchPanel, "North");
        //bottomPanel.add(inputButtonPanel, "North");
        bottomPanel.add(transferPanel, "Center");

        topPanel.setBorder(blackline);
        bottomPanel.setBorder(blackline);
        teamPanel.setBorder(blackline);
        benchPanel.setBorder(blackline);
        inputButtonPanel.setBorder(blackline);
        transferPanel.setBorder(blackline);

        this.add(topPanel);
        this.add(bottomPanel);

        playerButtons = new ArrayList<>();
        teamSections = new HashMap<>();

        setupInitialTeam();
    }

    public void setupInitialTeam(){
        for(String pos : positions){
            int num = positionCaps.get(pos);
            teamSections.put(pos, new JPanel());
            teamSections.get(pos).setOpaque(false);

            for(int i = 0; i < num; i++){
                playerButtons.add(new PlayerButton(this, pos));
            }
        }

        Map<String, Integer> positionCounts = new HashMap<>();
        positionCounts.put("GK", 0);
        positionCounts.put("DEF", 0);
        positionCounts.put("MID", 0);
        positionCounts.put("ATT", 0);

        for(PlayerButton p : playerButtons){
            String pos = p.getPosition();
            if(positionCounts.get(pos) >= startFormation.get(pos)){
                benchPanel.add(p);
                continue;
            }

            teamSections.get(pos).add(p);
            positionCounts.put(pos, positionCounts.get(pos) + 1);
        }

        for(String pos : positions){
            teamPanel.add(teamSections.get(pos));
        }
    }

    public void actionPerformed(ActionEvent e){

    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
