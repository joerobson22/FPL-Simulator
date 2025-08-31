package Frontend;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;
import javax.swing.*;

public class PlayerPanel extends JButton implements ActionListener {
    
    //the entire team panel gets 500px -> 400px wide pitch, therefore 400 / 5 (5 at most in any pos) -> 80
    private final int fixedWidth = 100;
    private final int fixedHeight = 60;

    private final int positionLabelTextSize = 10;
    private final int nameLabelTextSize = 10;
    private final int fixturePointsLabelTextSize = 14;

    private FPLPanel FPLPanel;

    private Player player;
    private String position;

    private JPanel panel;
    private JLabel positionLabel;
    private JLabel nameLabel;
    private JLabel fixturePointsLabel;

    private boolean captain = false;
    private boolean viceCaptain = false;

    public PlayerPanel(FPLPanel FPLpanel, String position){
        this.FPLPanel = FPLpanel;

        this.setPreferredSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        this.setMinimumSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        this.setMaximumSize(new java.awt.Dimension(fixedWidth, fixedHeight));

        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        panel.setMinimumSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        panel.setMaximumSize(new java.awt.Dimension(fixedWidth, fixedHeight));

        this.position = position;
        positionLabel = LabelCreator.createJLabel(position, "SansSerif", positionLabelTextSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        nameLabel = LabelCreator.createJLabel("+", "SansSerif", nameLabelTextSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        fixturePointsLabel = LabelCreator.createJLabel("-", "SansSerif", fixturePointsLabelTextSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK);

        panel.add(positionLabel, "North");
        panel.add(nameLabel, "Center");
        panel.add(fixturePointsLabel, "South");
        panel.setOpaque(false);

        this.add(panel);
        this.addActionListener(this);
        this.setOpaque(false);
    }

    public void actionPerformed(ActionEvent e){
        if(this == e.getSource()){
            if(player != null){
                //create player large window
                System.out.println("Make choice");
                FPLPanel.makeChoice(this);
            }
            else{
                FPLPanel.transferOutPlayer(this);
            }
        }
        
    }

    public void updateVisuals(){
        positionLabel.setText(position + " - £" + String.valueOf(player.getPrice()));

        String nameLabelText = player.getName();
        if(captain){
            nameLabelText = "C- " + nameLabelText;
        }
        else if(viceCaptain){
            nameLabelText = "V- " + nameLabelText;
        }

        nameLabel.setText(nameLabelText);
        fixturePointsLabel.setText("-");
    }

    public void setPlayer(Player p){
        player = p;
        updateVisuals();
    }

    public Player getPlayer(){
        return player;
    }

    public String getPosition(){
        return position;
    }

    public void makeCaptain(){
        captain = true;
        viceCaptain = false;
        updateVisuals();
    }

    public void makeViceCaptain(){
        viceCaptain = true;
        captain = false;
        updateVisuals();
    }

    public void unmakeCV(){
        captain = false;
        viceCaptain = false;
        updateVisuals();
    }

    public boolean isCaptain(){ return captain; }
    public boolean isViceCaptain(){ return viceCaptain; }
}
