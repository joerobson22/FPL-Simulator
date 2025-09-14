package Frontend;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Settings extends JFrame implements ActionListener{
    JPanel mainPanel;
    JPanel simulationDepthPanel;
    JPanel seasonChoicePanel;
    JPanel goButtonPanel;

    JButton goButton;

    JComboBox simulationDepthComboBox;
    JComboBox seasonChoiceComboBox;

    String teamName;

    Integer[] simulationDepths = {1, 5, 10, 20, 50, 100};
    String[] simulationDepthTimeEstimations = {"10 seconds", "30 seconds", "1 minute", "2 minutes", "5 minutes", "10 minutes"};
    Integer[] seasons = {2023, 2024, 2025};

    public Settings(String teamName){
        this.teamName = teamName;

        simulationDepthComboBox = new JComboBox<>(simulationDepths);
        seasonChoiceComboBox = new JComboBox<>(seasons);

        simulationDepthPanel = new JPanel(new BorderLayout());
        seasonChoicePanel = new JPanel(new BorderLayout());

        simulationDepthPanel.add(simulationDepthComboBox, "Center");
        simulationDepthPanel.add(LabelCreator.createJLabel("Simulation Depth", "SansSerif", 12, Font.BOLD, SwingConstants.CENTER, Color.BLACK), "North");
        seasonChoicePanel.add(seasonChoiceComboBox, "Center");
        seasonChoicePanel.add(LabelCreator.createJLabel("Season", "SansSerif", 12, Font.BOLD, SwingConstants.CENTER, Color.BLACK), "North");

        goButton = new JButton(">>>");
        goButton.addActionListener(this);

        goButtonPanel = new JPanel(new BorderLayout());
        goButtonPanel.add(goButton, "Center");

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(simulationDepthPanel);
        mainPanel.add(seasonChoicePanel);
        mainPanel.add(goButtonPanel);

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(500, 500);
        this.setTitle("FPL Simulator- Settings");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void actionPerformed(ActionEvent e){
        if(goButton == e.getSource()){
            this.setVisible(false);
            FantasyTeam fantasyTeam = new FantasyTeam(teamName);
            MainWindow mainWindow = new MainWindow(fantasyTeam);
        }
    }
}
