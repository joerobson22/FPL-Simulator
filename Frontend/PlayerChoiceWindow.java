package Frontend;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class PlayerChoiceWindow extends JFrame implements ActionListener {
    
    private final int WINDOW_WIDTH = 300;
    private final int WINDOW_HEIGHT = 125;

    private int captainFlag;
    private int viceCaptainFlag;
    private int subFlag;
    private int transferFlag;

    private FPLPanel fplPanel;

    private JPanel mainPanel;
    private JPanel playerInfoPanel;
    private JPanel inputButtonsPanel;
    private JPanel furtherInfoPanel;
    private JPanel recentPanel;
    private JPanel fixturePanel;

    private JButton captainButton;
    private JButton viceCaptainButton;
    private JButton subButton;
    private JButton transferButton;

    private Player player;
    private int gameWeek;

    public PlayerChoiceWindow(Player p, FPLPanel fplPanel, int captainFlag, int viceCaptainFlag, int subFlag, int transferFlag, int gameWeek){
        this.player = p;
        this.fplPanel = fplPanel;
        this.captainFlag = captainFlag;
        this.viceCaptainFlag = viceCaptainFlag;
        this.subFlag = subFlag;
        this.transferFlag = transferFlag;
        this.gameWeek = gameWeek;

        mainPanel = new JPanel(new BorderLayout());

        playerInfoPanel = new JPanel(new GridLayout(1, 3));
        playerInfoPanel.add(LabelCreator.createJLabel("£" + p.getPrice() + "m", "SansSerif", 13, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        playerInfoPanel.add(LabelCreator.createJLabel(p.getName(), "SansSerif", 15, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        playerInfoPanel.add(LabelCreator.createJLabel(p.getTotalPoints() + "pts", "SansSerif", 13, Font.BOLD, SwingConstants.CENTER, Color.BLACK));

        furtherInfoPanel = new JPanel(new GridLayout(1, 2));
        recentPanel = new JPanel(new GridLayout(1, 3));
        fixturePanel = new JPanel(new GridLayout(1, 3));
        furtherInfoPanel.add(recentPanel);
        furtherInfoPanel.add(fixturePanel);

        captainButton = new JButton("C");
        captainButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        captainButton.addActionListener(this);
        viceCaptainButton = new JButton("V");
        viceCaptainButton.setFont(new Font("SansSerif", Font.PLAIN, 15));
        viceCaptainButton.addActionListener(this);

        subButton = new JButton("SUB");
        subButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subButton.addActionListener(this);

        transferButton = new JButton("TRANSFER");
        transferButton.setFont(new Font("SansSerif", Font.BOLD, 10));
        transferButton.addActionListener(this);

        inputButtonsPanel = new JPanel(new GridLayout(1, 3));
        JPanel CVPanel = new JPanel(new GridLayout(1, 2));
        CVPanel.add(captainButton);
        CVPanel.add(viceCaptainButton);
        inputButtonsPanel.add(CVPanel);
        inputButtonsPanel.add(subButton);
        inputButtonsPanel.add(transferButton);

        mainPanel.add(playerInfoPanel, "North");
        mainPanel.add(furtherInfoPanel, "Center");
        mainPanel.add(inputButtonsPanel, "South");

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle("FPL Simulator- Player Choice");
        this.setResizable(false);

        setupExtraInfo();
    }

    private void setupExtraInfo(){
        for(int i = gameWeek - 3; i < gameWeek; i++){
            if(i < 0) continue;
            recentPanel.add(LabelCreator.createJLabel(String.valueOf(player.getGameWeekPoints(i)) + "pts", "SansSerif", 12, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        }
        for(Fixture f : player.getTeam().getNext3Fixtures(gameWeek)){
            fixturePanel.add(new FixtureTile(f, player.getTeam() == f.getHomeTeam()));
        }
    }
    
    public void actionPerformed(ActionEvent e){
        if(captainButton == e.getSource()){
            fplPanel.playerPanelChoiceMade(captainFlag);
        }
        else if(viceCaptainButton == e.getSource()){
            fplPanel.playerPanelChoiceMade(viceCaptainFlag);
        }
        else if(subButton == e.getSource()){
            fplPanel.playerPanelChoiceMade(subFlag);
        }
        else if(transferButton == e.getSource()){
            fplPanel.playerPanelChoiceMade(transferFlag);
        }
        this.setVisible(false);
    }
}
