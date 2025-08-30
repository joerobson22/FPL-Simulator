package Frontend;

import java.util.ArrayList;
import java.util.Map;

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

    private Map<String, ArrayList<PlayerButton>> players = Map.of(
        "GK", new ArrayList<>(),
        "DEF", new ArrayList<>(),
        "MID", new ArrayList<>(),
        "ATT", new ArrayList<>(),
        "BENCH", new ArrayList<>()
    );

    public FPLPanel(MainWindow mainWindow, ArrayList<Player> allPlayers){
        this.setLayout(new GridLayout(2, 1));
        this.mainWindow = mainWindow;
        this.allPlayers = allPlayers;

        Border blackline = BorderFactory.createLineBorder(Color.black);

        //create top and bottom panels
        topPanel = new BackgroundPanel(imagePath);
        bottomPanel = new JPanel(new BorderLayout());

        //create every panel
        teamPanel = new JPanel(new GridLayout(1, 4));
        teamPanel.setOpaque(false);
        benchPanel = new JPanel();
        inputButtonPanel = new JPanel(new BorderLayout());
        transferPanel = new JPanel();
        transferPanel.setLayout(new BoxLayout(transferPanel, BoxLayout.Y_AXIS));
        transferScrollPanel = new JScrollPane();

        transferScrollPanel.add(transferPanel);

        //top panel add background image

        topPanel.add(teamPanel);
        bottomPanel.add(benchPanel, "North");
        bottomPanel.add(inputButtonPanel, "North");
        bottomPanel.add(transferPanel, "Center");

        topPanel.setBorder(blackline);
        bottomPanel.setBorder(blackline);
        teamPanel.setBorder(blackline);
        benchPanel.setBorder(blackline);
        inputButtonPanel.setBorder(blackline);
        transferPanel.setBorder(blackline);

        this.add(topPanel);
        this.add(bottomPanel);
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
