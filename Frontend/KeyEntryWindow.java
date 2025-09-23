package Frontend;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class KeyEntryWindow extends JFrame implements ActionListener{
    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 500;

    private JPanel contentPanel;
    private JPanel mainPanel;
    private JPanel inputPanel;
    private JLabel titleLabel;
    private JLabel explanationLabel;
    private JTextField textEditBox;
    private JButton goButton;

    private FantasyTeam fantasyTeam;
    private int simulationDepth;
    private int season;

    public KeyEntryWindow(FantasyTeam fantasyTeam, int simulationDepth, int season){
        this.fantasyTeam = fantasyTeam;
        this.simulationDepth = simulationDepth;
        this.season = season;

        mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //TITLE LABEL
        titleLabel = LabelCreator.createJLabel("Enter API Key", "SansSerif", 15, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        explanationLabel = LabelCreator.createJLabel("Your personal API key on api-football.com", "SansSerif", 10,  Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //text edit box
        textEditBox = new JTextField(15);
        textEditBox.setMaximumSize(new Dimension(200, 30));
        textEditBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        //go button
        goButton = new JButton(">>>");
        goButton.addActionListener(this);

        //input panel
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.add(Box.createHorizontalGlue());
        inputPanel.add(textEditBox);
        inputPanel.add(goButton);
        inputPanel.add(Box.createHorizontalGlue());

        //add vertical spacing
        mainPanel.add(Box.createVerticalGlue()); 
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(explanationLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalGlue());

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER); 

        this.setContentPane(contentPanel);
        this.setVisible(true);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle("FPL Simulator- API Key Entry");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("Frontend/assets/logo.png");
        this.setIconImage(icon.getImage());
    }

    public void actionPerformed(ActionEvent e)
    {
        if(goButton == e.getSource() && textEditBox.getText().length() > 0)
        {
            IOHandler.writeSingleLine(textEditBox.getText(), "Backend/FootballAPI/apiKey.txt");

            this.setVisible(false);
            MainWindow mainWindow = new MainWindow(fantasyTeam, simulationDepth, season);
        }
    }
    
}

