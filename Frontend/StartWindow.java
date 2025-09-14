package Frontend;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class StartWindow extends JFrame implements ActionListener{
    FantasyTeam fantasyTeam;
    MainWindow mainWindow;

    JPanel contentPanel;
    JPanel mainPanel;
    JPanel inputPanel;
    JLabel label;
    JTextField textEditBox;
    JButton goButton;

    public StartWindow(){
        mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //TITLE LABEL
        label = LabelCreator.createJLabel("Enter Team Name", "SansSerif", 15, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        //text edit box
        textEditBox = new JTextField(15);
        textEditBox.setMaximumSize(new Dimension(200, 30));
        textEditBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        //go button
        goButton = new JButton(">>>");
        goButton.addActionListener(this);

        //input panel
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS)); // Use BoxLayout for input panel too
        inputPanel.add(Box.createHorizontalGlue()); // Add space before
        inputPanel.add(textEditBox);
        inputPanel.add(goButton);
        inputPanel.add(Box.createHorizontalGlue()); // Add space after

        // Add vertical spacing
        mainPanel.add(Box.createVerticalGlue()); // Space at top
        mainPanel.add(label);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between label and input
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalGlue()); // Space at bottom

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER); // Use BorderLayout.CENTER constant

        this.setContentPane(contentPanel);
        this.setVisible(true);
        this.setSize(500, 500);
        this.setTitle("FPL Simulator- Account Creation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        // Center the window on screen
        this.setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(goButton == e.getSource() && textEditBox.getText().length() > 0)
        {
            this.setVisible(false);
            fantasyTeam = new FantasyTeam(textEditBox.getText());
            mainWindow = new MainWindow(fantasyTeam);
        }
    }
    
}

