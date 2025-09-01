package Frontend;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class FPLPanel extends JPanel implements ActionListener{

    public final int NEUTRAL_STATUS = 0;
    public final int SUBSTITUTE_STATUS = 1;
    public final int TRANSFER_STATUS = 2;
    public final int CAPTAIN_STATUS = 3;
    public final int VICE_CAPTAIN_STATUS = 4;
    public final int CONFIRMED_STATUS = 5;

    private int status;
    private PlayerPanel focusPlayer;

    private final int fixedPitchWidth = 500;

    private MainWindow mainWindow;
    private ArrayList<Player> allPlayers;

    private Image backgroundImage;
    private final String imagePath = "Frontend/assets/pitch.png";

    private JPanel topPanel; //contains a display for all the players
    private JPanel bottomPanel; //contains bench, input buttons and transfer panel

    private JPanel titleScorePanel;
    private JPanel teamPanel;
    private JPanel benchPanel;
    private JPanel transferScrollPanel;
    private JScrollPane transferScrollWrapper;
    private JPanel transferPanel;
    private JLabel scoreLabel;
    private JLabel budgetLabel;
    private JLabel freeTransfersLabel;

    private JButton cancelButton;

    private final String[] positions = {"GK", "DEF", "MID", "ATT"};

    private Map<String, JPanel> teamSections;

    private final Map<String, Integer> positionCaps = Map.of(
        "GK", 2,
        "DEF", 5,
        "MID", 5,
        "ATT", 3
    );

    private final Map<String, Integer> startFormation = Map.of(
        "GK", 1,
        "DEF", 4,
        "MID", 4,
        "ATT", 2
    );

    private final Map<String, Integer> positionMins = Map.of(
        "GK", 1,
        "DEF", 3,
        "MID", 3,
        "ATT", 1
    );

    private ArrayList<PlayerPanel> playerPanels;

    FantasyTeam fantasyTeam;

    boolean teamConfirmed;

    public FPLPanel(MainWindow mainWindow, FantasyTeam fantasyTeam, ArrayList<Player> allPlayers){
        this.setLayout(new BorderLayout());
        this.mainWindow = mainWindow;
        this.fantasyTeam = fantasyTeam;
        this.allPlayers = allPlayers;
        teamConfirmed = false;

        status = 0;

        Border blackline = BorderFactory.createLineBorder(Color.black);

        //create top and bottom panels
        topPanel = new JPanel(new BorderLayout());
        BackgroundPanel pitchPanel = new BackgroundPanel(imagePath);
        int targetHeight = pitchPanel.getTargetHeight();
        JPanel titlePanel = new JPanel(new GridLayout(1, 3));
        scoreLabel = LabelCreator.createJLabel("0 pts", "SansSerif", 15, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        JPanel otherPanel = new JPanel(new BorderLayout());
        budgetLabel = LabelCreator.createJLabel("£" + String.valueOf(fantasyTeam.getBudget()) + "m", "SansSerif", 14, Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        freeTransfersLabel = LabelCreator.createJLabel(fantasyTeam.getFreeTransferString() + " Transfers", "SansSerif", 10, Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("SansSerif", Font.PLAIN, 10));
        cancelButton.setText("Confirm Team");
        cancelButton.addActionListener(this);
        otherPanel.add(freeTransfersLabel, "East");
        otherPanel.add(cancelButton, "Center");

        titlePanel.add(otherPanel);
        titlePanel.add(scoreLabel);
        titlePanel.add(budgetLabel);


        

        bottomPanel = new JPanel(new BorderLayout());

        //create every panel
        teamPanel = new JPanel(new GridLayout(4, 1));
        teamPanel.setOpaque(false);
        teamPanel.setPreferredSize(new Dimension(fixedPitchWidth, targetHeight));
        teamPanel.setMinimumSize(new Dimension(fixedPitchWidth, targetHeight));
        teamPanel.setMaximumSize(new Dimension(fixedPitchWidth, targetHeight));

        benchPanel = new JPanel();
        transferPanel = new JPanel(new BorderLayout());
        JPanel transferTitlePanel = new JPanel();
        transferTitlePanel.add(LabelCreator.createJLabel("Transfers", "SansSerif", 15, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        transferScrollPanel = new JPanel();
        transferScrollPanel.setLayout(new BoxLayout(transferScrollPanel, BoxLayout.Y_AXIS));

        transferScrollWrapper = new JScrollPane(transferScrollPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        transferScrollWrapper.setViewportView(transferScrollPanel);
        transferScrollWrapper.setPreferredSize(new Dimension(500, 330));

        transferPanel.add(transferScrollWrapper, "Center");
        transferPanel.add(transferTitlePanel, "North");
        pitchPanel.add(teamPanel);
        topPanel.add(pitchPanel, "Center");
        topPanel.add(titlePanel, "North");
        topPanel.add(benchPanel, "South");
        bottomPanel.add(transferPanel, "Center");

        pitchPanel.setBorder(blackline);
        bottomPanel.setBorder(blackline);
        benchPanel.setBorder(blackline);
        transferPanel.setBorder(blackline);

        this.add(topPanel, "North");
        this.add(bottomPanel, "Center");

        playerPanels = new ArrayList<>();
        teamSections = new HashMap<>();

        setupInitialTeam();
    }

    public void setupInitialTeam(){
        for(String pos : positions){
            int num = positionCaps.get(pos);
            teamSections.put(pos, new JPanel());
            teamSections.get(pos).setOpaque(false);

            for(int i = 0; i < num; i++){
                PlayerPanel p = new PlayerPanel(this, pos, false);
                //p.setPlayer(allPlayers.get(0)); <- TESTING
                playerPanels.add(p);
            }
        }

        Map<String, Integer> positionCounts = new HashMap<>();
        positionCounts.put("GK", 0);
        positionCounts.put("DEF", 0);
        positionCounts.put("MID", 0);
        positionCounts.put("ATT", 0);

        for(PlayerPanel p : playerPanels){
            String pos = p.getPosition();
            if(positionCounts.get(pos) >= startFormation.get(pos)){
                benchPanel.add(p);
                p.putOnBench();
                continue;
            }

            teamSections.get(pos).add(p);
            positionCounts.put(pos, positionCounts.get(pos) + 1);
        }

        for(String pos : positions){
            teamPanel.add(teamSections.get(pos));
        }
    }

    public void confirmTeam(){
        System.out.println("team valid: " + String.valueOf(fantasyTeam.isTeamValid()));
        System.out.println("team confirmed: " + String.valueOf(teamConfirmed));
        if(fantasyTeam.isTeamValid() && !teamConfirmed){
            mainWindow.confirmTeam();
            teamConfirmed = true;
            status = CONFIRMED_STATUS;
            focusPlayer = null;
        }
    }

    public void updateTeamVisuals(int viewingGameWeek, int currentGameWeek){
        if(viewingGameWeek > currentGameWeek || fantasyTeam.getPlayers().size() < 15) return;
        
        //clear each section
        for(String key : teamSections.keySet()){
            teamSections.get(key).removeAll();
        }
        benchPanel.removeAll();
        playerPanels = new ArrayList<>();

        if(viewingGameWeek < currentGameWeek){
            //get previous fantasy team
            PreviousFantasyTeam ft = fantasyTeam.getPreviousFantasyTeam(viewingGameWeek);
            //for each player in the starting lineup, make a new player panel for it and add it to the correct position
            for(Player p : ft.getStartingXI()){
                createAndAddPastPlayer(p, teamSections.get(p.getGeneralPosition()), ft.getCaptain(), ft.getViceCaptain(), false, viewingGameWeek, currentGameWeek);
            }
            for(Player p : ft.getBench()){
                createAndAddPastPlayer(p, benchPanel, ft.getCaptain(), ft.getViceCaptain(), true, viewingGameWeek, currentGameWeek);
            }
            scoreLabel.setText(String.valueOf(ft.getPoints()) + "pts");
        }
        else {
            //for each player in the starting lineup, make a new player panel for it and add it to the correct position
            for(Player p : fantasyTeam.getStartingXI()){
                createAndAddPastPlayer(p, teamSections.get(p.getGeneralPosition()), fantasyTeam.getCaptain(), fantasyTeam.getViceCaptain(), false, viewingGameWeek, currentGameWeek);
            }
            for(Player p : fantasyTeam.getBench()){
                createAndAddPastPlayer(p, benchPanel, fantasyTeam.getCaptain(), fantasyTeam.getViceCaptain(), true, viewingGameWeek, currentGameWeek);
            }
            scoreLabel.setText(String.valueOf(fantasyTeam.getWeeklyPoints()) + "pts");
        }

        for(String key : teamSections.keySet()){
            teamSections.get(key).revalidate();
            teamSections.get(key).repaint();
        }
        benchPanel.revalidate();
        benchPanel.repaint();
        teamPanel.revalidate();
        teamPanel.repaint();
        this.revalidate();
        this.repaint();
    }

    public void updateTeamPoints(){
        fantasyTeam.resetWeeklyTotal();
        for(PlayerPanel panel : playerPanels){
            Player player = panel.getPlayer();
            if(!player.hasPlayedThisWeek()) continue;

            if(!panel.isBenched()) fantasyTeam.changeWeeklyPoints(player);
            panel.setPoints(player.getWeeklyPoints());
        }
        scoreLabel.setText(String.valueOf(fantasyTeam.getWeeklyPoints()) + "pts");
    }

    public void createAndAddPastPlayer(Player p, JPanel panel, Player captain, Player viceCaptain, boolean benched, int viewingGameWeek, int currentGameWeek){
        PlayerPanel pp = new PlayerPanel(this, p.getGeneralPosition(), benched);
        pp.setPlayer(p);

        if(captain == p) pp.makeCaptain();
        else if(viceCaptain == p) pp.makeViceCaptain();
        
        // Always set points for past gameweeks
        if (viewingGameWeek < currentGameWeek) {
            int points = p.getGameWeekPoints(viewingGameWeek);
            pp.setPoints(points);
        } 
        // For current gameweek, set points only if the player has played
        else if (viewingGameWeek == currentGameWeek && p.hasPlayedThisWeek()) {
            pp.setPoints(p.getWeeklyPoints());
        }

        panel.add(pp);
        playerPanels.add(pp);
    }

    public void setConfirmed(boolean correctGameweek, boolean teamConfirmed){
        System.out.println("correct gameweek: " + String.valueOf(correctGameweek));
        System.out.println("team confirmed: " + String.valueOf(teamConfirmed));
        if(correctGameweek && !teamConfirmed){
            System.out.println("correct gameweek, team unconfirmed, so set teamconfirmed = false");
            this.teamConfirmed = false;
            status = NEUTRAL_STATUS;
        }
        else if(!correctGameweek){
            System.out.println("incorrect gameweek, status = confirmed status");
            this.teamConfirmed = true;
            status = CONFIRMED_STATUS;
        }
        
        focusPlayer = null;
    }

    public void updateTransferVisuals(String position){
        transferScrollPanel.removeAll();
        for(Player p : allPlayers){
            if(!p.getGeneralPosition().equals(position)) continue;

            transferScrollPanel.add(new PlayerTransferPanel(p, this, true));
        }

        transferScrollPanel.revalidate();
        transferScrollPanel.repaint();
    }

    public void actionPerformed(ActionEvent e){
        if(cancelButton == e.getSource()){
            if(cancelButton.getText().equals("Cancel")){
                status = NEUTRAL_STATUS;
                focusPlayer = null;
                updateTransferVisuals("");
                cancelButton.setText("Confirm Team");
            }
            else if(cancelButton.getText().equals("Confirm Team")){
                System.out.println("team confirmed: " + String.valueOf(teamConfirmed));
                System.out.println("confirm team pressed");
                confirmTeam();
            }
        }
    }

    public void makeChoice(PlayerPanel p){
        if(status == CONFIRMED_STATUS) return;
        if(status == SUBSTITUTE_STATUS){
            System.out.println("substitute " + p.getPlayer().getName() + " and " + focusPlayer.getPlayer().getName());
            substitutePlayers(p);
        }
        else if (status == NEUTRAL_STATUS) {
            focusPlayer = p;
            PlayerChoiceWindow pcw = new PlayerChoiceWindow(p.getPlayer(), this, CAPTAIN_STATUS, VICE_CAPTAIN_STATUS, SUBSTITUTE_STATUS, TRANSFER_STATUS);
        }
    }

    public void choiceMade(int flag){
        if(status == CONFIRMED_STATUS) return;
        System.out.println(flag);
        if(status != NEUTRAL_STATUS) return;

        status = flag;

        if(status == CAPTAIN_STATUS){
            System.out.println("captain!");
            setCaptain(focusPlayer);
        }
        else if(status == VICE_CAPTAIN_STATUS){
            System.out.println("vice!");
            setViceCaptain(focusPlayer);
        }
        else if(status == SUBSTITUTE_STATUS){
            System.out.println("substituting...");
            cancelButton.setText("Cancel");
        }
        else if(status  == TRANSFER_STATUS){
            System.out.println("transfer!");
            transferOutPlayer(focusPlayer);
        }
    }

    public void setCaptain(PlayerPanel p){
        if(status == CONFIRMED_STATUS) return;

        for(PlayerPanel playerPanel : playerPanels){
            if(playerPanel.isCaptain()){
                playerPanel.unmakeCV();
                break;
            }
        }

        fantasyTeam.makeCaptain(p.getPlayer());
        p.makeCaptain();
        p = null;
        status = NEUTRAL_STATUS;
    }

    public void setViceCaptain(PlayerPanel p){
        for(PlayerPanel playerPanel : playerPanels){
            if(playerPanel.isViceCaptain()){
                playerPanel.unmakeCV();
                break;
            }
        }

        fantasyTeam.makeViceCaptain(p.getPlayer());
        p.makeViceCaptain();
        p = null;
        status = NEUTRAL_STATUS;
    }

    public void substitutePlayers(PlayerPanel p){
        PlayerPanel panel1 = focusPlayer;
        PlayerPanel panel2 = p;

        boolean player1Bench = panel1.isBenched();
        boolean player2Bench = panel2.isBenched();

        Player player1 = panel1.getPlayer();
        Player player2 = panel2.getPlayer();

        if((panel1.getPosition().equals("GK") && !panel2.getPosition().equals("GK")) || (!panel1.getPosition().equals("GK") && panel2.getPosition().equals("GK"))){
            System.out.println("Can't sub a keeper with a non keeper idiot");
            return;
        }

        //obviously can't swap with yourself, so return but dont reset status
        if(panel1 == panel2 || (!panel1.isBenched() && !panel2.isBenched())){
            System.out.println("panels are equal or they are both on the pitch");
            return;
        }
        //if both benched, then we can swap just fine, no problems
        if(panel1.isBenched() && panel2.isBenched()){
            System.out.println("swap players casually");
            panel1.swapPlayers(panel2);
        }
        //if one is benched, swap their players
        //unbench the benched one, bench the unbenched one
        //add player panels to correct panels
        else if(panel1.isBenched() && !panel2.isBenched()){
            String panel1Pos = panel1.getPosition();
            String panel2Pos = panel2.getPosition();

            //CHECK THIS FORMATION WILL BE VALID
            if(!panel1Pos.equals(panel2Pos)){
                System.out.println("they are not the same position");
                int panel2SectionNumPlayers = teamSections.get(panel2Pos).getComponentCount();
                //if number of players left in a section is less than allowed, then give up
                if(panel2SectionNumPlayers - 1 < positionMins.get(panel2Pos)){
                    System.out.println("would result in invalid formation");
                    return;
                }
                //PANEL 1 IS ON THE BENCH IN THIS SCENARIO
                panel1.takeOffBench();
                panel2.putOnBench();
                
                teamSections.get(panel1Pos).add(panel1);
                teamSections.get(panel2Pos).remove(panel2);
                benchPanel.add(panel2);
            }
            else
            {
                panel1.swapPlayers(panel2);
            }
        }
        //and vice versa
        else if(!panel1.isBenched() && panel2.isBenched()){
            System.out.println("panel2 benched, panel1 not benched");
            String panel1Pos = panel1.getPosition();
            String panel2Pos = panel2.getPosition();

            //CHECK THIS FORMATION WILL BE VALID
            if(!panel1Pos.equals(panel2Pos)){
                System.out.println("they are not the same position");
                int panel1SectionNumPlayers = teamSections.get(panel1Pos).getComponentCount();
                //if number of players left in a section is less than allowed, then give up
                if(panel1SectionNumPlayers - 1 < positionMins.get(panel1Pos)){
                    System.out.println("would result in invalid formation");
                    return;
                }
                //PANEL 1 IS ON THE BENCH IN THIS SCENARIO
                panel2.takeOffBench();
                panel2.putOnBench();
                
                teamSections.get(panel2Pos).add(panel2);
                teamSections.get(panel1Pos).remove(panel1);
                benchPanel.add(panel1);
            }
            else{
                panel2.swapPlayers(panel1);
            }
        }

        System.out.println("done!");

        focusPlayer = null;
        status = NEUTRAL_STATUS;

        fantasyTeam.makeSubstitution(player1, player2, player1Bench, player2Bench);

        teamPanel.revalidate();
        teamPanel.repaint();
        benchPanel.revalidate();
        benchPanel.repaint();
        this.revalidate();
        this.repaint();
        cancelButton.setText("Confirm Team");
    }

    public void transferOutPlayer(PlayerPanel p){
        if(status == CONFIRMED_STATUS) return;
        if(status == NEUTRAL_STATUS || status == TRANSFER_STATUS){
            status = TRANSFER_STATUS;
            focusPlayer = p;

            updateTransferVisuals(focusPlayer.getPosition());

            cancelButton.setText("Cancel");
        }
    }

    public void transferInPlayer(Player p){
        if(status == CONFIRMED_STATUS) return;
        if(status != TRANSFER_STATUS) return;

        status = NEUTRAL_STATUS;

        Player playerOut = focusPlayer.getPlayer();
        Player playerIn = p;

        if(!p.getGeneralPosition().equals(focusPlayer.getPosition())){
            focusPlayer = null;
            return;
        }

        if(!fantasyTeam.makeTransfer(playerOut, playerIn, componentArrayHasElement(benchPanel.getComponents(), benchPanel.getComponentCount(), focusPlayer))){
            status = TRANSFER_STATUS;
            return;
        }

        focusPlayer.setPlayer(playerIn);
        budgetLabel.setText("£" + String.valueOf(fantasyTeam.getBudget()) + "m");
        freeTransfersLabel.setText(fantasyTeam.getFreeTransferString() + " Transfers");
        cancelButton.setText("Confirm Team");
        updateTransferVisuals("");
    }

    private boolean componentArrayHasElement(Component[] componentArray, int arraySize, Component c){
        for(int i = 0; i < arraySize; i++){
            if(componentArray[i] == c) return true;
        }
        return false;
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        private int targetHeight;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();

            int imgWidth = backgroundImage.getWidth(null);
            int imgHeight = backgroundImage.getHeight(null);

            targetHeight = (int) ((double) fixedPitchWidth / imgWidth * imgHeight);
            this.setPreferredSize(new Dimension(fixedPitchWidth, targetHeight));
            this.setMinimumSize(new Dimension(fixedPitchWidth, targetHeight));
            this.setMaximumSize(new Dimension(fixedPitchWidth, targetHeight));
        }

        public int getTargetHeight(){
            return targetHeight;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            int imgWidth = backgroundImage.getWidth(null);
            int imgHeight = backgroundImage.getHeight(null);

            int targetHeight = (int) ((double) fixedPitchWidth / imgWidth * imgHeight);

            g.drawImage(backgroundImage, 0, 0, fixedPitchWidth, targetHeight, this);
        }
    }
}
