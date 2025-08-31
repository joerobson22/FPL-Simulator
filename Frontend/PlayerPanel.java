package Frontend;

import java.awt.event.*;
import javax.swing.border.Border;

import javax.swing.*;
import java.awt.image.*;
import java.io.File;
import java.awt.*;

public class PlayerPanel extends JButton implements ActionListener {
    
    private final Color captainColor = new Color(252, 186, 3);
    private final Color viceCaptainColor = new Color(255, 225, 143);
    private final Color normalColor = new Color(255, 252, 245);

    //the entire team panel gets 500px -> 400px wide pitch, therefore 400 / 5 (5 at most in any pos) -> 80
    private final int fixedWidth = 95;
    private final int fixedHeight = 60;

    private final int imageWidth = 70;
    private final int imageHeight = 70;

    private final int positionLabelTextSize = 10;
    private final int nameLabelTextSize = 10;
    private final int fixturePointsLabelTextSize = 14;

    private FPLPanel FPLPanel;

    private Player player;
    private String logoPath;
    private String position;

    private JPanel panel;
    private JLabel positionLabel;
    private JLabel nameLabel;
    private JLabel fixturePointsLabel;

    private boolean captain;
    private boolean viceCaptain;
    private boolean benched;

    public PlayerPanel(FPLPanel FPLpanel, String position, boolean benched){
        this.FPLPanel = FPLpanel;
        this.benched = benched;
        captain = false;
        viceCaptain = false;

        this.setPreferredSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        this.setMinimumSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        this.setMaximumSize(new java.awt.Dimension(fixedWidth, fixedHeight));

        panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        panel.setMinimumSize(new java.awt.Dimension(fixedWidth, fixedHeight));
        panel.setMaximumSize(new java.awt.Dimension(fixedWidth, fixedHeight));

        this.position = position;
        positionLabel = LabelCreator.createJLabel(position, "SansSerif", positionLabelTextSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
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
                FPLPanel.makeChoice(this);
            }
            else{
                FPLPanel.transferOutPlayer(this);
            }
        }
        
    }

    public void swapPlayers(PlayerPanel panel){
        Player player2 = panel.getPlayer();
        panel.setPlayer(player);
        panel.updateVisuals();
        setPlayer(player2);
    }

    public void updateVisuals(){
        positionLabel.setText(position + " - £" + String.valueOf(player.getPrice()) + "m");

        String nameLabelText = player.getName();
        if(captain){
            nameLabelText = "C- " + nameLabelText;
            this.setBorder(BorderFactory.createLineBorder(captainColor, 2));
        }
        else if(viceCaptain){
            nameLabelText = "V- " + nameLabelText;
            this.setBorder(BorderFactory.createLineBorder(viceCaptainColor, 2));
        }
        else{
            this.setBorder(BorderFactory.createLineBorder(normalColor, 2));
        }

        nameLabel.setText(nameLabelText);
        fixturePointsLabel.setText("-");

        if(player != null && player.getTeam() != null && logoPath != null && !logoPath.isEmpty()){
            this.setIcon(getTransparentIcon(logoPath, imageWidth, imageHeight, 0.3f));
        }
    }

    private ImageIcon getTransparentIcon(String logoPath, int width, int height, float alpha) {
        System.out.println("Loading logo from: " + logoPath);
        File f = new File(logoPath);
        System.out.println("Exists? " + f.exists());

        ImageIcon icon = new ImageIcon(logoPath);
    
        // Force-load into a BufferedImage first
        BufferedImage original = new BufferedImage(
            icon.getIconWidth(),
            icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = original.createGraphics();
        g.drawImage(icon.getImage(), 0, 0, null);
        g.dispose();

        // Scale into target size
        Image scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Create transparent buffer
        BufferedImage transparentImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = transparentImg.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(transparentImg);
    }


    public void setPlayer(Player p){
        player = p;
        logoPath = player.getTeam().getLogoPath();
        updateVisuals();
    }

    public boolean isBenched(){
        return benched;
    }

    public void putOnBench(){
        benched = true;
    }

    public void takeOffBench(){
        benched = false;
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
