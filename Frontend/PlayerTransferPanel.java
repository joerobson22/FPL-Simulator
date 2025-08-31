package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerTransferPanel extends JPanel implements ActionListener {
    
    private FPLPanel fplPanel;
    private Player player;

    private final int playerNameTextSize = 12;
    private final int playerClubAndPosTextSize = 10;
    private final int playerInfoTextSize = 11;
    private final int transferInButtonTextSize = 10;

    private final int fixedWidth = 500;
    private final int fixedHeight = 40;

    JPanel playerInfoPanel;
    JPanel upcomingFixturesPanel;
    JPanel actionButtonPanel;
    JButton transferInButton;

    

    public PlayerTransferPanel(Player p, FPLPanel fplPanel, boolean transferrable){
        this.setLayout(new GridLayout(1, 3));
        this.setPreferredSize(new Dimension(fixedWidth, fixedHeight));
        this.setMinimumSize(new Dimension(fixedWidth, fixedHeight));
        this.setMaximumSize(new Dimension(fixedWidth, fixedHeight));
        this.fplPanel = fplPanel;
        this.player = p;

        playerInfoPanel = new JPanel(new GridLayout(1, 2));
        upcomingFixturesPanel = new JPanel(new GridLayout(1, 3));
        actionButtonPanel = new JPanel();

        JPanel playerNamePanel = new JPanel(new GridLayout(2, 1));
        String padding = "  ";
        playerNamePanel.add(LabelCreator.createJLabel(padding + p.getName(), "SansSerif", playerNameTextSize, Font.BOLD, SwingConstants.LEFT, Color.BLACK));
        JPanel posAndTeamPanel = new JPanel(new GridLayout(1, 2));
        posAndTeamPanel.add(LabelCreator.getIconLabel(player.getTeam().getLogoPath(), 20, 20));
        posAndTeamPanel.add(LabelCreator.createJLabel(player.getSpecificPosition().toUpperCase(), "SansSerif", playerClubAndPosTextSize, Font.PLAIN, SwingConstants.LEFT, Color.BLACK));
        playerNamePanel.add(posAndTeamPanel);

        JPanel playerExtraInfoPanel = new JPanel(new GridLayout(1, 2));
        playerExtraInfoPanel.add(LabelCreator.createJLabel("£" + String.valueOf(p.getPrice()) + "m", "SansSerif", playerInfoTextSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK));
        playerExtraInfoPanel.add(LabelCreator.createJLabel(String.valueOf(p.getTotalPoints()), "SansSerif", playerInfoTextSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK));

        playerInfoPanel.add(playerNamePanel);
        playerInfoPanel.add(playerExtraInfoPanel);

        transferInButton = new JButton("Transfer In");
        transferInButton.setFont(new Font("SansSerif", Font.BOLD, transferInButtonTextSize));
        transferInButton.addActionListener(this);
        actionButtonPanel.add(transferInButton);
        transferInButton.setVisible(transferrable);

        this.add(playerInfoPanel);
        this.add(upcomingFixturesPanel);
        this.add(actionButtonPanel);

        playerNamePanel.setOpaque(false);
        playerExtraInfoPanel.setOpaque(false);
        playerInfoPanel.setOpaque(false);
        upcomingFixturesPanel.setOpaque(false);
        actionButtonPanel.setOpaque(false);
    }

    public void actionPerformed(ActionEvent e){
        if(this.transferInButton == e.getSource()){
            fplPanel.transferInPlayer(player);
        }
    }
}
