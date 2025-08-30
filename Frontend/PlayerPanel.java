package Frontend;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;
import javax.swing.*;

public class PlayerPanel extends JButton implements ActionListener {
    
    //the entire team panel gets 500px -> 400px wide pitch, therefore 400 / 5 (5 at most in any pos) -> 80
    private final int fixedWidth = 80;
    private final int fixedHeight = 60;

    private FPLPanel FPLPanel;

    private Player player;
    private String position;

    private JPanel panel;
    private JLabel positionLabel;
    private JLabel nameLabel;
    private JLabel fixturePointsLabel;

    public PlayerPanel(FPLPanel FPLpanel, String position){
        this.FPLPanel = FPLpanel;

        this.setPreferredSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        this.setMinimumSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        this.setMaximumSize(new java.awt.Dimension(fixedWidth, fixedHeight));

        panel = new JPanel(new BorderLayout());

        this.position = position;
        positionLabel = LabelCreator.createJLabel(position, "SansSerif", 10, Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        nameLabel = LabelCreator.createJLabel("Bruno Fernandes", "SansSerif", 10, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        fixturePointsLabel = LabelCreator.createJLabel("-", "SansSerif", 14, Font.BOLD, SwingConstants.CENTER, Color.BLACK);

        panel.add(positionLabel, "North");
        panel.add(nameLabel, "Center");
        panel.add(fixturePointsLabel, "South");
        panel.setOpaque(false);

        this.add(panel);
        this.addActionListener(this);
        this.setOpaque(false);
    }

    public void actionPerformed(ActionEvent e){
        //create player large window
    }

    public void updateVisuals(){
        positionLabel.setText(position + " - £" + String.valueOf(player.getPrice()));
        nameLabel.setText(player.getName());
        fixturePointsLabel.setText("-");
    }

    public void setPlayer(Player p){
        player = p;
    }

    public Player getPlayer(){
        return player;
    }

    public String getPosition(){
        return position;
    }
}
