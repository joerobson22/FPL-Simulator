package Frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayerTransferPanel extends JPanel implements ActionListener {
    
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

    public PlayerTransferPanel(Player p){
        this.setLayout(new GridLayout(1, 3));
        this.setPreferredSize(new Dimension(fixedWidth, fixedHeight));
        this.setMinimumSize(new Dimension(fixedWidth, fixedHeight));
        this.setMaximumSize(new Dimension(fixedWidth, fixedHeight));

        playerInfoPanel = new JPanel(new GridLayout(1, 2));
        upcomingFixturesPanel = new JPanel(new GridLayout(1, 3));
        actionButtonPanel = new JPanel();

        JPanel playerNamePanel = new JPanel(new GridLayout(2, 1));
        String padding = "  ";
        playerNamePanel.add(LabelCreator.createJLabel(padding + p.getName(), "SansSerif", playerNameTextSize, Font.BOLD, SwingConstants.LEFT, Color.BLACK));
        playerNamePanel.add(LabelCreator.createJLabel(padding + p.getTeam().getAbbrv() + "   " + p.getGeneralPosition(), "SansSerif", playerClubAndPosTextSize, Font.PLAIN, SwingConstants.LEFT, Color.BLACK));

        JPanel playerExtraInfoPanel = new JPanel(new GridLayout(1, 2));
        playerExtraInfoPanel.add(LabelCreator.createJLabel("£" + String.valueOf(p.getPrice()) + "m", "SansSerif", playerInfoTextSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK));
        playerExtraInfoPanel.add(LabelCreator.createJLabel(String.valueOf(p.getTotalPoints()), "SansSerif", playerInfoTextSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK));

        playerInfoPanel.add(playerNamePanel);
        playerInfoPanel.add(playerExtraInfoPanel);

        transferInButton = new JButton("Transfer In");
        transferInButton.setFont(new Font("SansSerif", Font.BOLD, transferInButtonTextSize));
        transferInButton.addActionListener(this);
        actionButtonPanel.add(transferInButton);

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

    }
}
