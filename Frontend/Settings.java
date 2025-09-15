package Frontend;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Settings extends JFrame implements ActionListener{
    final int WINDOW_WIDTH = 300;
    final int WINDOW_HEIGHT = 200;

    JPanel mainPanel;
    JPanel simulationDepthPanel;
    JPanel seasonChoicePanel;
    JPanel goButtonPanel;

    JButton goButton;

    JComboBox<Integer> simulationDepthComboBox;
    JComboBox<Integer> seasonChoiceComboBox;

    String teamName;

    Integer[] simulationDepths = {1, 5, 10, 20, 50, 100};
    String[] simulationDepthTimeEstimations = {"10 seconds", "30 seconds", "1 minute", "2 minutes", "5 minutes", "10 minutes"};
    Integer[] seasons = {2021, 2022, 2023};//, 2024, 2025};   <- WHEN EAFC26 kaggle database comes out, pay £15 to get fixture data and whatnot and then allow the use of these

    public Settings(String teamName){
        this.teamName = teamName;

        simulationDepthComboBox = new JComboBox<>(simulationDepths);
        seasonChoiceComboBox = new JComboBox<>(seasons);
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        simulationDepthComboBox.setRenderer(listRenderer);
        seasonChoiceComboBox.setRenderer(listRenderer);

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
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle("FPL Simulator- Settings");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("Frontend/assets/logo.png");
        this.setIconImage(icon.getImage());
    }

    public void actionPerformed(ActionEvent e){
        if(goButton == e.getSource()){
            this.setVisible(false);
            int simulationDepth = simulationDepths[simulationDepthComboBox.getSelectedIndex()];
            int season = seasons[seasonChoiceComboBox.getSelectedIndex()];
            System.out.println("simulationDepth: " + String.valueOf(simulationDepth));
            System.out.println("season: " + String.valueOf(season));

            FantasyTeam fantasyTeam = new FantasyTeam(teamName);
            MainWindow mainWindow = new MainWindow(fantasyTeam, simulationDepth, season);
        }
    }
}
