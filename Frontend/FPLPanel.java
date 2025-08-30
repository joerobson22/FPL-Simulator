package Frontend;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class FPLPanel extends JPanel implements ActionListener{

    private final int fixedPitchWidth = 500;

    private MainWindow mainWindow;
    private ArrayList<Player> allPlayers;

    private Image backgroundImage;
    private final String imagePath = "Frontend/assets/pitch.png";

    private JPanel topPanel; //contains a display for all the players
    private JPanel bottomPanel; //contains bench, input buttons and transfer panel

    private JPanel titleScorePanel;
    private JPanel teamPanel;
    private JPanel benchPanel;
    private JPanel transferScrollPanel;
    private JScrollPane transferScrollWrapper;
    private JPanel transferPanel;
    private JLabel scoreLabel;

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

    private ArrayList<PlayerPanel> playerPanels;

    FantasyTeam fantasyTeam;

    public FPLPanel(MainWindow mainWindow, FantasyTeam fantasyTeam, ArrayList<Player> allPlayers){
        this.setLayout(new BorderLayout());
        this.mainWindow = mainWindow;
        this.fantasyTeam = fantasyTeam;
        this.allPlayers = allPlayers;

        Border blackline = BorderFactory.createLineBorder(Color.black);

        //create top and bottom panels
        topPanel = new JPanel(new BorderLayout());
        BackgroundPanel pitchPanel = new BackgroundPanel(imagePath);
        int targetHeight = pitchPanel.getTargetHeight();
        JPanel titlePanel = new JPanel();
        scoreLabel = LabelCreator.createJLabel("0 pts", "SansSerif", 15, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        titlePanel.add(scoreLabel);

        

        bottomPanel = new JPanel(new BorderLayout());

        //create every panel
        teamPanel = new JPanel(new GridLayout(4, 1));
        teamPanel.setOpaque(false);
        teamPanel.setPreferredSize(new Dimension(fixedPitchWidth, targetHeight));
        teamPanel.setMinimumSize(new Dimension(fixedPitchWidth, targetHeight));
        teamPanel.setMaximumSize(new Dimension(fixedPitchWidth, targetHeight));

        benchPanel = new JPanel();
        transferPanel = new JPanel(new BorderLayout());
        JPanel transferTitlePanel = new JPanel();
        transferTitlePanel.add(LabelCreator.createJLabel("Transfers", "SansSerif", 15, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        transferScrollPanel = new JPanel();
        transferScrollPanel.setLayout(new BoxLayout(transferScrollPanel, BoxLayout.Y_AXIS));

        transferScrollWrapper = new JScrollPane(transferScrollPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        transferScrollWrapper.setViewportView(transferScrollPanel);
        transferScrollWrapper.setPreferredSize(new Dimension(500, 330));

        transferPanel.add(transferScrollWrapper, "Center");
        transferPanel.add(transferTitlePanel, "North");
        pitchPanel.add(teamPanel);
        topPanel.add(pitchPanel, "Center");
        topPanel.add(titlePanel, "North");
        topPanel.add(benchPanel, "South");
        bottomPanel.add(transferPanel, "Center");

        pitchPanel.setBorder(blackline);
        bottomPanel.setBorder(blackline);
        benchPanel.setBorder(blackline);
        transferPanel.setBorder(blackline);

        this.add(topPanel, "North");
        this.add(bottomPanel, "Center");

        playerPanels = new ArrayList<>();
        teamSections = new HashMap<>();

        setupInitialTeam();

        updateTransferVisuals("MID");
    }

    public void setupInitialTeam(){
        for(String pos : positions){
            int num = positionCaps.get(pos);
            teamSections.put(pos, new JPanel());
            teamSections.get(pos).setOpaque(false);

            for(int i = 0; i < num; i++){
                playerPanels.add(new PlayerPanel(this, pos));
            }
        }

        Map<String, Integer> positionCounts = new HashMap<>();
        positionCounts.put("GK", 0);
        positionCounts.put("DEF", 0);
        positionCounts.put("MID", 0);
        positionCounts.put("ATT", 0);

        for(PlayerPanel p : playerPanels){
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

    public void updateTeamVisuals(int gameWeek){

    }

    public void updateTransferVisuals(String position){
        transferScrollPanel.removeAll();
        for(Player p : allPlayers){
            if(!p.getGeneralPosition().equals(position)) continue;

            transferScrollPanel.add(new PlayerTransferPanel(p));
        }

        transferScrollPanel.revalidate();
        transferScrollPanel.repaint();
    }

    public void actionPerformed(ActionEvent e){

    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        private int targetHeight;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();

            int imgWidth = backgroundImage.getWidth(null);
            int imgHeight = backgroundImage.getHeight(null);

            targetHeight = (int) ((double) fixedPitchWidth / imgWidth * imgHeight);
            this.setPreferredSize(new Dimension(fixedPitchWidth, targetHeight));
            this.setMinimumSize(new Dimension(fixedPitchWidth, targetHeight));
            this.setMaximumSize(new Dimension(fixedPitchWidth, targetHeight));
        }

        public int getTargetHeight(){
            return targetHeight;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            int imgWidth = backgroundImage.getWidth(null);
            int imgHeight = backgroundImage.getHeight(null);

            int targetHeight = (int) ((double) fixedPitchWidth / imgWidth * imgHeight);

            g.drawImage(backgroundImage, 0, 0, fixedPitchWidth, targetHeight, this);
        }
    }
}
