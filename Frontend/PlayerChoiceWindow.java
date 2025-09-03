package Frontend;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class PlayerChoiceWindow extends JFrame implements ActionListener {
    
    private final int WINDOW_WIDTH = 300;
    private final int WINDOW_HEIGHT = 100;

    private int captainFlag;
    private int viceCaptainFlag;
    private int subFlag;
    private int transferFlag;

    private FPLPanel fplPanel;

    private JPanel mainPanel;
    private JPanel playerInfoPanel;
    private JPanel inputButtonsPanel;

    private JButton captainButton;
    private JButton viceCaptainButton;
    private JButton subButton;
    private JButton transferButton;

    private Player player;

    public PlayerChoiceWindow(Player p, FPLPanel fplPanel, int captainFlag, int viceCaptainFlag, int subFlag, int transferFlag){
        this.player = p;
        this.fplPanel = fplPanel;
        this.captainFlag = captainFlag;
        this.viceCaptainFlag = viceCaptainFlag;
        this.subFlag = subFlag;
        this.transferFlag = transferFlag;

        mainPanel = new JPanel(new BorderLayout());

        playerInfoPanel = new JPanel(new GridLayout(1, 3));
        playerInfoPanel.add(LabelCreator.createJLabel("£" + p.getPrice() + "m", "SansSerif", 13, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        playerInfoPanel.add(LabelCreator.createJLabel(p.getName(), "SansSerif", 15, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        playerInfoPanel.add(LabelCreator.createJLabel(p.getTotalPoints() + "pts", "SansSerif", 13, Font.BOLD, SwingConstants.CENTER, Color.BLACK));

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

        mainPanel.add(playerInfoPanel, "Center");
        mainPanel.add(inputButtonsPanel, "South");

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle("FPL Simulator- Player Choice");
        this.setResizable(false);
    }
    
    public void actionPerformed(ActionEvent e){
        System.out.println("button press...");
        if(captainButton == e.getSource()){
            System.out.println("captain!");
            fplPanel.playerPanelChoiceMade(captainFlag);
        }
        else if(viceCaptainButton == e.getSource()){
            System.out.println("vice!");
            fplPanel.playerPanelChoiceMade(viceCaptainFlag);
        }
        else if(subButton == e.getSource()){
            System.out.println("sub!");
            fplPanel.playerPanelChoiceMade(subFlag);
        }
        else if(transferButton == e.getSource()){
            System.out.println("transfer!");
            fplPanel.playerPanelChoiceMade(transferFlag);
        }
        this.setVisible(false);
    }
}
