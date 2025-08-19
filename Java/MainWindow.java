package Java;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener{

    JPanel mainPanel;
    JPanel contentPanel;
    JPanel titlePanel;
    JPanel westPanel;
    JPanel centerPanel;
    JPanel eastPanel;

    Color titlePanelBackgroundColor = new Color(0, 8, 255);
    Color westPanelBackgroundColor = new Color(0, 255, 157);
    Color centerPanelBackgroundColor = new Color(255, 0, 191);
    Color eastPanelBackgroundColor = new Color(221, 255, 0);


    public MainWindow(User user){
        titlePanel = new JPanel();
        titlePanel.setBackground(titlePanelBackgroundColor);

        westPanel = new JPanel();
        westPanel.setBackground(westPanelBackgroundColor);

        centerPanel = new JPanel();
        centerPanel.setBackground(centerPanelBackgroundColor);

        eastPanel = new JPanel();
        eastPanel.setBackground(eastPanelBackgroundColor);

        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, 3));
        contentPanel.add(westPanel);
        contentPanel.add(centerPanel);
        contentPanel.add(eastPanel);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add("North", titlePanel);
        mainPanel.add("Center", contentPanel);

        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.setSize(1500, 1000);
        this.setTitle("FPL Simulator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }






    //fixture simulating- communication with C++ backend
    public void simulateFixture(Fixture fixture)
    {

    }

    //action listener
    public void actionPerformed(ActionEvent e)
    {
        
    }
}
