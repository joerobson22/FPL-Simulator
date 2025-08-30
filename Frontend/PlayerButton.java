package Frontend;

import java.awt.BorderLayout;

import javax.swing.*;

public class PlayerButton extends JButton {
    
    FPLPanel FPLPanel;

    Player player;
    String position;

    JPanel panel;
    JLabel positionLabel;
    JLabel nameLabel;
    JLabel fixturePointsLabel;

    public PlayerButton(FPLPanel FPLpanel, String position){
        this.FPLPanel = FPLpanel;

        panel = new JPanel(new BorderLayout());

        this.position = position;
        positionLabel = new JLabel(position);
        nameLabel = new JLabel("+");
        fixturePointsLabel = new JLabel("-");

        panel.add(positionLabel, "North");
        panel.add(nameLabel, "Center");
        panel.add(fixturePointsLabel, "South");

        this.add(panel);
        this.addActionListener(FPLpanel);
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
