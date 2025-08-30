package Frontend;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartWindow extends JFrame implements ActionListener{
    FantasyTeam fantasyTeam;
    MainWindow mainWindow;

    JPanel mainPanel;
    JLabel label;
    JTextField textEditBox;
    JButton goButton;

    public StartWindow(){
        mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        label = new JLabel("Enter team name!");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        textEditBox = new JTextField(15);
        textEditBox.setMaximumSize(new Dimension(200, 30));
        textEditBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        goButton = new JButton("Go");
        goButton.addActionListener(this);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(label);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(textEditBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(goButton);
        mainPanel.add(Box.createVerticalGlue());

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(500, 500);
        this.setTitle("FPL Simulator- Account Creation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(goButton == e.getSource() && textEditBox.getText().length() > 0)
        {
            fantasyTeam = new FantasyTeam(textEditBox.getText());

            mainWindow = new MainWindow(fantasyTeam);
            this.setVisible(false);
        }
    }
    
}

