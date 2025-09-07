package Frontend;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class FPLPanel extends JPanel implements ActionListener{

    private final int NEUTRAL_STATUS = 0;
    private final int SUBSTITUTE_STATUS = 1;
    private final int TRANSFER_STATUS = 2;
    private final int CAPTAIN_STATUS = 3;
    private final int VICE_CAPTAIN_STATUS = 4;
    private final int CONFIRMED_STATUS = 5;
    private final String IMAGE_PATH = "Frontend/assets/pitch.png";
    private final String CONFIRM_TEXT = "Confirm";
    private final String CANCEL_TEXT = "Cancel";

    private final String font = "SansSerif";
    private final int chipButtonFontSize = 12;
    private final int scoreLabelFontSize = 15;
    private final int freeTransferLabelFontSize = 10;
    private final int budgetLabelFontSize = 14;
    private final int cancelConfirmButtonFontSize = 13;


    private int fixedPitchWidth = 500;
    private final int BENCH_PANEL_HEIGHT = 100;
    private final int TITLE_PANEL_HEIGHT = 80;
    Border blackline;

    private MainWindow mainWindow;
    private ArrayList<Player> allPlayers;

    private Image backgroundImage;

    private JPanel topPanel; //contains a display for all the players
    private JPanel bottomPanel; //contains bench, input buttons and transfer panel

    private JPanel teamPanel;
    private JPanel benchPanel;
    private JPanel transferScrollPanel;
    private JScrollPane transferScrollWrapper;
    private JPanel transferPanel;
    private JLabel scoreLabel;
    private JLabel budgetLabel;
    private JLabel freeTransfersLabel;
    private JPanel chipsPanel;
    private JPanel titlePanel;

    private JButton cancelConfirmButton;
    private JButton wildcardButton;
    private JButton freeHitButton;
    private JButton tripleCaptainButton;
    private JButton benchBoostButton;

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
    private FantasyTeam fantasyTeam;

    private int status;
    private PlayerPanel focusPlayer;
    private String viewingPosition = " ";
    private boolean teamConfirmed;
    private PlayerChoiceWindow pcw;
    private int captainMultiplier = 2;
    private int viewingGameWeek = 0;
    private int currentGameWeek = 0;

    public FPLPanel(MainWindow mainWindow, FantasyTeam fantasyTeam, ArrayList<Player> allPlayers, int width){
        this.setLayout(new BorderLayout());
        this.mainWindow = mainWindow;
        this.fantasyTeam = fantasyTeam;
        this.allPlayers = allPlayers;
        fixedPitchWidth = width;
        teamConfirmed = false;

        status = NEUTRAL_STATUS;

        blackline = BorderFactory.createLineBorder(Color.black);

        topPanel = setupTopPanel();
        bottomPanel = setupBottomPanel();

        this.add(topPanel, "North");
        this.add(bottomPanel, "Center");

        playerPanels = new ArrayList<>();
        teamSections = new HashMap<>();

        setupInitialTeam();
        this.revalidate();
        this.repaint();
    }

    public JPanel setupTopPanel(){
        //create top panels
        JPanel topPanel = new JPanel(new BorderLayout());
        //pitch panel background for the team display
        BackgroundPanel pitchPanel = new BackgroundPanel(IMAGE_PATH);
        int targetHeight = pitchPanel.getTargetHeight();
        //title panel- displays chips as well as weekly team information (confirm/cancel button, free transfers, points, budgets)
        titlePanel = new JPanel(new GridLayout(2, 1));
        chipsPanel = new JPanel();
        JPanel weeklyInfoPanel = new JPanel(new GridLayout(1, 3));
        JPanel ccAndFTPanel = new JPanel(new BorderLayout());
        //create all the buttons for the chips panel
        wildcardButton = createJButton("Wildcard", font, Font.BOLD, chipButtonFontSize);
        freeHitButton = createJButton("Free Hit", font, Font.BOLD, chipButtonFontSize);
        tripleCaptainButton = createJButton("Triple Captain", font, Font.BOLD, chipButtonFontSize);
        benchBoostButton = createJButton("Bench Boost", font, Font.BOLD, chipButtonFontSize);
        //create all the labels for the weekly info panel
        scoreLabel = LabelCreator.createJLabel("0 pts", font, scoreLabelFontSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        budgetLabel = LabelCreator.createJLabel("£" + String.valueOf(fantasyTeam.getBudget()) + "m", font, budgetLabelFontSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        freeTransfersLabel = LabelCreator.createJLabel("   " + fantasyTeam.getFreeTransferString() + " Transfers", font, freeTransferLabelFontSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK);
        cancelConfirmButton = createJButton(CONFIRM_TEXT, font, Font.BOLD, cancelConfirmButtonFontSize);
        //add all the components to their respective panels now
        //first do the weekly info panel
        ccAndFTPanel.add(freeTransfersLabel, "East");
        ccAndFTPanel.add(cancelConfirmButton, "Center");
        weeklyInfoPanel.add(ccAndFTPanel);
        weeklyInfoPanel.add(scoreLabel);
        weeklyInfoPanel.add(budgetLabel);
        //now do the chips panel
        chipsPanel.add(wildcardButton);
        chipsPanel.add(freeHitButton);
        chipsPanel.add(tripleCaptainButton);
        chipsPanel.add(benchBoostButton);
        //now add all the panels to the title panel
        titlePanel.add(chipsPanel);
        titlePanel.add(weeklyInfoPanel);
        //set fixed size for title panel
        titlePanel.setPreferredSize(new Dimension(fixedPitchWidth, TITLE_PANEL_HEIGHT));
        titlePanel.setMinimumSize(new Dimension(fixedPitchWidth, TITLE_PANEL_HEIGHT));
        titlePanel.setMaximumSize(new Dimension(fixedPitchWidth, TITLE_PANEL_HEIGHT));

        //create the team display
        teamPanel = new JPanel(new GridLayout(4, 1));
        teamPanel.setOpaque(false);
        teamPanel.setPreferredSize(new Dimension(fixedPitchWidth, targetHeight));
        teamPanel.setMinimumSize(new Dimension(fixedPitchWidth, targetHeight));
        teamPanel.setMaximumSize(new Dimension(fixedPitchWidth, targetHeight));
        //and the bench panel
        benchPanel = new JPanel();
        //set fixed size for bench panel
        benchPanel.setPreferredSize(new Dimension(fixedPitchWidth, BENCH_PANEL_HEIGHT));
        benchPanel.setMinimumSize(new Dimension(fixedPitchWidth, BENCH_PANEL_HEIGHT));
        benchPanel.setMaximumSize(new Dimension(fixedPitchWidth, BENCH_PANEL_HEIGHT));

        pitchPanel.add(teamPanel);
        topPanel.add(pitchPanel, "Center");
        topPanel.add(titlePanel, "North");
        topPanel.add(benchPanel, "South");

        pitchPanel.setBorder(blackline);

        return topPanel;
    }

    public JPanel setupBottomPanel(){
        //now setup the bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        //create each wrapping panel
        transferPanel = new JPanel(new BorderLayout());
        transferScrollPanel = new JPanel();
        //create the title panel, containing the header and the table titles
        JPanel transferTitlePanel = new JPanel(new GridLayout(2, 1));
        transferTitlePanel.add(LabelCreator.createJLabel("Transfers", "SansSerif", 15, Font.BOLD, SwingConstants.CENTER, Color.BLACK));
        //create the transfer scroll panel and setup the scrolll mode
        transferScrollPanel.setLayout(new BoxLayout(transferScrollPanel, BoxLayout.Y_AXIS));
        transferScrollWrapper = new JScrollPane(transferScrollPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        transferScrollWrapper.setViewportView(transferScrollPanel);
        transferScrollWrapper.setPreferredSize(new Dimension(500, 330));
        //add the two panels to the overall panel
        transferPanel.add(transferScrollWrapper, "Center");
        transferPanel.add(transferTitlePanel, "North");
        //add the transfer panel
        bottomPanel.add(transferPanel, "Center");
        //set borders to separate each panel
        bottomPanel.setBorder(blackline);
        benchPanel.setBorder(blackline);
        transferPanel.setBorder(blackline);

        return bottomPanel;
    }

    //setup the initial team
    public void setupInitialTeam(){
        //create all team sections and player panels
        for(String pos : positions){
            int num = positionCaps.get(pos);
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            teamSections.put(pos, panel);

            for(int i = 0; i < num; i++){
                PlayerPanel p = new PlayerPanel(this, pos, false);
                //p.setPlayer(allPlayers.get(i)); <- TESTING
                playerPanels.add(p);
            }
        }

        Map<String, Integer> positionCounts = new HashMap<>();
        positionCounts.put("GK", 0);
        positionCounts.put("DEF", 0);
        positionCounts.put("MID", 0);
        positionCounts.put("ATT", 0);

        //put player panels into correct position panels
        for(PlayerPanel p : playerPanels){
            String pos = p.getPosition();
            if(positionCounts.get(pos) >= startFormation.get(pos)){
                benchPanel.add(p);
                p.putOnBench(viewingGameWeek);
                continue;
            }

            teamSections.get(pos).add(p);
            positionCounts.put(pos, positionCounts.get(pos) + 1);
        }
        //put each panel into the correct place in the team panel
        for(String pos : positions){
            teamPanel.add(teamSections.get(pos));
        }
    }

    //helper method to create a jbutton
    public JButton createJButton(String text, String font, int fontType, int fontSize){
        JButton button = new JButton(text);
        button.setFont(new Font(font, fontType, fontSize));
        button.addActionListener(this);
        return button;
    }

    //the viewing gameweek has been changed, so update all visuals
    public void changeGW(int viewingGameWeek, int currentGameWeek, boolean teamConfirmed){
        this.viewingGameWeek = viewingGameWeek;
        this.currentGameWeek = currentGameWeek;
        setConfirmed(currentGameWeek == viewingGameWeek, teamConfirmed);
        updateAllVisuals();
        focusPlayer = null;
        status = NEUTRAL_STATUS;
        cancelConfirmButton.setText(CONFIRM_TEXT);
    }

    //update all visuals- change up the player panels, show or hide information labels and their text
    public void updateAllVisuals(){
        updateTeamVisuals();
        updateTransferInformationVisibility(viewingGameWeek == currentGameWeek);
        updateTransferInformation();
    }

    //updates all the player panels
    public void updateTeamVisuals(){
        if(viewingGameWeek > currentGameWeek || fantasyTeam.getPlayers().size() < 15){
            for(PlayerPanel p : playerPanels){
                p.updateVisuals(false, viewingGameWeek);
            }
            return;
        }
        
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
                createAndAddPastPlayer(p, teamSections.get(p.getGeneralPosition()), ft.getCaptain(), ft.getViceCaptain(), false);
            }
            for(Player p : ft.getBench()){
                createAndAddPastPlayer(p, benchPanel, ft.getCaptain(), ft.getViceCaptain(), true);
            }
            scoreLabel.setText(String.valueOf(ft.getPoints()) + "pts");
        }
        else {
            //for each player in the starting lineup, make a new player panel for it and add it to the correct position
            for(Player p : fantasyTeam.getStartingXI()){
                createAndAddPastPlayer(p, teamSections.get(p.getGeneralPosition()), fantasyTeam.getCaptain(), fantasyTeam.getViceCaptain(), false);
            }
            for(Player p : fantasyTeam.getBench()){
                createAndAddPastPlayer(p, benchPanel, fantasyTeam.getCaptain(), fantasyTeam.getViceCaptain(), true);
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

    //helper method to create and then add a player panel to a given panel
    public void createAndAddPastPlayer(Player p, JPanel panel, Player captain, Player viceCaptain, boolean benched){
        PlayerPanel pp = new PlayerPanel(this, p.getGeneralPosition(), benched);
        pp.setPlayer(p, viewingGameWeek);

        if(captain == p) pp.makeCaptain(viewingGameWeek);
        else if(viceCaptain == p) pp.makeViceCaptain(viewingGameWeek);
        
        // Always set points for past gameweeks
        if (viewingGameWeek < currentGameWeek) {
            int points = p.getGameWeekPoints(viewingGameWeek);
            pp.setPoints(points, captainMultiplier);
        } 
        // For current gameweek, set points only if the player has played
        else if (viewingGameWeek == currentGameWeek && p.hasPlayedThisWeek()) {
            pp.setPoints(p.getWeeklyPoints(), captainMultiplier);
        }

        panel.add(pp);
        playerPanels.add(pp);
    }
    
    //updates the free transfer, budget and cancelconfirm stuff
    public void updateTransferInformation(){
        budgetLabel.setText("£" + String.valueOf(fantasyTeam.getBudget()) + "m");
        freeTransfersLabel.setText("   " + fantasyTeam.getFreeTransferString() + " Transfers");
        cancelConfirmButton.setText(CONFIRM_TEXT);
    }

    //update all labels and buttons
    public void updateTransferInformationVisibility(boolean show){
        freeTransfersLabel.setVisible(show);
        cancelConfirmButton.setVisible(show);
        budgetLabel.setVisible(show);
        chipsPanel.setVisible(show);
    }

    //calculate and update the team's weekly score
    public void updateTeamPoints(){
        fantasyTeam.resetWeeklyTotal();
        for(PlayerPanel panel : playerPanels){
            Player player = panel.getPlayer();
            if(!player.hasPlayedThisWeek()) continue;

            if(!panel.isBenched()) fantasyTeam.changeWeeklyPoints(player);
            panel.setPoints(player.getWeeklyPoints(), captainMultiplier);
        }
        scoreLabel.setText(String.valueOf(fantasyTeam.getWeeklyPoints()) + "pts");
    }

    //updating the transfer panel- only does it if currently not viewing the same position
    public void updateTransferVisuals(String position){
        if(viewingPosition == position) return;

        transferScrollPanel.removeAll();
        for(Player p : allPlayers){
            if(!p.getGeneralPosition().equals(position)) continue;

            transferScrollPanel.add(new PlayerTransferPanel(p, this, true, viewingGameWeek));
        }

        transferScrollPanel.revalidate();
        transferScrollPanel.repaint();
    }

    public void clearTransferPanel(){
        viewingPosition = " ";
        updateTransferVisuals(viewingPosition);
    }

    
    

    //confirm team
    public void confirmTeam(){
        System.out.println("team valid: " + String.valueOf(fantasyTeam.isTeamValid()));
        System.out.println("team confirmed: " + String.valueOf(teamConfirmed));
        if(fantasyTeam.isTeamValid() && !teamConfirmed){
            mainWindow.confirmTeam();
            
            teamConfirmed = true;
            status = CONFIRMED_STATUS;
            focusPlayer = null;

            updateTransferInformationVisibility(false);
            clearTransferPanel();
        }
    }

    public void setConfirmed(boolean correctGameweek, boolean teamConfirmed){
        if(correctGameweek && !teamConfirmed){
            this.teamConfirmed = false;
            status = NEUTRAL_STATUS;
        }
        else if(!correctGameweek){
            this.teamConfirmed = true;
            status = CONFIRMED_STATUS;
        }
        
        focusPlayer = null;
    }

    
    //INPUTS

    //action performed!!!
    public void actionPerformed(ActionEvent e){
        //reset the currently focussed player
        if(focusPlayer != null){
            focusPlayer.updateVisuals(false, viewingGameWeek);
            focusPlayer = null;
        }

        if(cancelConfirmButton == e.getSource()){
            //if cancelling
            if(cancelConfirmButton.getText().equals(CANCEL_TEXT)){
                //set status to netural, update the focus player's visuals
                status = NEUTRAL_STATUS;
                
                //clear the transfer panel
                clearTransferPanel();
                cancelConfirmButton.setText(CONFIRM_TEXT);
            }
            //otherwise, if confirming team
            else if(cancelConfirmButton.getText().equals(CONFIRM_TEXT)){
                confirmTeam();
            }
        }
    }

    //a player panel has been clicked, unhighlight any currently selected panels and do whatever input
    public void playerPanelClicked(PlayerPanel p){
        if(status == CONFIRMED_STATUS) return;

        //reset visuals for currently selected focus player
        if(focusPlayer != null){
            focusPlayer.updateVisuals(false, viewingGameWeek);
        }

        //if currently substituting, then sub players
        if(status == SUBSTITUTE_STATUS){
            p.changeBorder(false);
            substitutePlayers(p);
        }
        //otherwise highlight the just clicked player
        else{
            p.changeBorder(true);
            //set them as the active focus player
            focusPlayer = p;
            //transfer in a player if there is no current player
            if(p.getPlayer() == null){
                transferOutPlayer(p);
                return;
            }
            //get the choice
            if(pcw != null) pcw.setVisible(false);
            pcw = new PlayerChoiceWindow(p.getPlayer(), this, CAPTAIN_STATUS, VICE_CAPTAIN_STATUS, SUBSTITUTE_STATUS, TRANSFER_STATUS);
        }
    }

    //a button has been clicked in the player choice window so do whatever the flag dictates
    public void playerPanelChoiceMade(int flag){
        if(status == CONFIRMED_STATUS) return;
        if(status != NEUTRAL_STATUS){
            return;
        }

        status = flag;

        if(status == CAPTAIN_STATUS){
            //focusPlayer.changeBorder(false);
            setCaptain(focusPlayer);
        }
        else if(status == VICE_CAPTAIN_STATUS){
            //focusPlayer.changeBorder(false);
            setViceCaptain(focusPlayer);
        }
        else if(status == SUBSTITUTE_STATUS){
            cancelConfirmButton.setText(CANCEL_TEXT);
        }
        else if(status  == TRANSFER_STATUS){
            transferOutPlayer(focusPlayer);
        }
    }

    //set the captain!
    public void setCaptain(PlayerPanel p){
        if(status == CONFIRMED_STATUS) return;

        focusPlayer = null;
        status = NEUTRAL_STATUS;
        cancelConfirmButton.setText(CONFIRM_TEXT);

        if(p.isBenched()){
            p.updateVisuals(false, viewingGameWeek);
            return;
        }

        //uncaptain any player currently captained
        for(PlayerPanel playerPanel : playerPanels){
            if(playerPanel.isCaptain()){
                playerPanel.unmakeCV(viewingGameWeek);
                break;
            }
        }

        fantasyTeam.makeCaptain(p.getPlayer());
        p.makeCaptain(viewingGameWeek);
    }

    //set the vice captain
    public void setViceCaptain(PlayerPanel p){
        if(status == CONFIRMED_STATUS) return;

        focusPlayer = null;
        status = NEUTRAL_STATUS;
        cancelConfirmButton.setText(CONFIRM_TEXT);

        if(p.isBenched()){
            p.updateVisuals(false, viewingGameWeek);
            return;
        }

        //uncaptain any player currently captained
        for(PlayerPanel playerPanel : playerPanels){
            if(playerPanel.isViceCaptain()){
                playerPanel.unmakeCV(viewingGameWeek);
                break;
            }
        }

        fantasyTeam.makeViceCaptain(p.getPlayer());
        p.makeViceCaptain(viewingGameWeek);
    }

    //substitute 2 players
    public void substitutePlayers(PlayerPanel p){
        if(p.getPlayer() == null){
            focusPlayer = null;
            status = NEUTRAL_STATUS;
            cancelConfirmButton.setText(CONFIRM_TEXT);
            return;
        }

        PlayerPanel panel1 = focusPlayer;
        PlayerPanel panel2 = p;

        boolean player1Bench = panel1.isBenched();
        boolean player2Bench = panel2.isBenched();

        Player player1 = panel1.getPlayer();
        Player player2 = panel2.getPlayer();

        focusPlayer = null;
        status = NEUTRAL_STATUS;
        cancelConfirmButton.setText(CONFIRM_TEXT);

        if((panel1.getPosition().equals("GK") && !panel2.getPosition().equals("GK")) || (!panel1.getPosition().equals("GK") && panel2.getPosition().equals("GK"))){
            return;
        }

        //obviously can't swap with yourself, so return but dont reset status
        if(panel1 == panel2 || (!player1Bench && !player2Bench)) return;
        //if either is the captain or vice captain this won't work
        if(panel1.isCaptainOrVice() || panel2.isCaptainOrVice()) return;
        //if both benched, then we can swap just fine, no problems
        if(player1Bench && player2Bench){
            panel1.swapPlayers(panel2, viewingGameWeek);
        }
        //if one is benched, swap their players
        //unbench the benched one, bench the unbenched one
        //add player panels to correct panels
        else if(player1Bench && !player2Bench){
            String panel1Pos = panel1.getPosition();
            String panel2Pos = panel2.getPosition();

            //CHECK THIS FORMATION WILL BE VALID
            int numPlayers = teamSections.get(panel2Pos).getComponentCount() - 1;
            if(panel1Pos.equals(panel2Pos)) numPlayers++;

            //if number of players left in a section is less than allowed, then give up
            if(numPlayers < positionMins.get(panel2Pos)){
                return;
            }
            //PANEL 1 IS ON THE BENCH IN THIS SCENARIO
            panel1.takeOffBench(viewingGameWeek);
            panel2.putOnBench(viewingGameWeek);
            
            teamSections.get(panel1Pos).add(panel1);
            teamSections.get(panel2Pos).remove(panel2);
            benchPanel.add(panel2);
        }
        //and vice versa
        else if(!player1Bench && player2Bench){
            String panel1Pos = panel1.getPosition();
            String panel2Pos = panel2.getPosition();

            //CHECK THIS FORMATION WILL BE VALID
            int numPlayers = teamSections.get(panel1Pos).getComponentCount() - 1;
            if(panel2Pos.equals(panel1Pos)) numPlayers++;
            
            //if number of players left in a section is less than allowed, then give up
            if(numPlayers < positionMins.get(panel1Pos)){
                return;
            }
            //PANEL 2 IS ON THE BENCH IN THIS SCENARIO
            panel2.takeOffBench(viewingGameWeek);
            panel1.putOnBench(viewingGameWeek);
            
            teamSections.get(panel2Pos).add(panel2);
            teamSections.get(panel1Pos).remove(panel1);
            benchPanel.add(panel2);

            //sort out captaincy
            if(panel1.isCaptain()){
                panel1.unmakeCV(viewingGameWeek);
                panel2.makeCaptain(viewingGameWeek);
                fantasyTeam.makeCaptain(panel2.getPlayer());
            }
            else if(panel1.isViceCaptain()){
                panel1.unmakeCV(viewingGameWeek);
                panel2.makeViceCaptain(viewingGameWeek);
                fantasyTeam.makeViceCaptain(panel2.getPlayer());
            }
        }

        fantasyTeam.makeSubstitution(player1, player2, player1Bench, player2Bench);

        this.revalidate();
        this.repaint();
        cancelConfirmButton.setText("Confirm Team");

        stabilizeLayout();
    }

    private void stabilizeLayout() {
        // Force consistent sizing of key panels
        benchPanel.setPreferredSize(new Dimension(fixedPitchWidth, BENCH_PANEL_HEIGHT));
        titlePanel.setPreferredSize(new Dimension(fixedPitchWidth, TITLE_PANEL_HEIGHT));
        
        // Update the UI in a specific order
        SwingUtilities.invokeLater(() -> {
            benchPanel.revalidate();
            benchPanel.repaint();
            titlePanel.revalidate();
            titlePanel.repaint();
            topPanel.revalidate();
            topPanel.repaint();
            revalidate();
            repaint();
        });
    }

    //transfer out a player
    public void transferOutPlayer(PlayerPanel p){
        if(status == CONFIRMED_STATUS) return;

        if(status == NEUTRAL_STATUS || status == TRANSFER_STATUS){
            status = TRANSFER_STATUS;
            focusPlayer = p;

            updateTransferVisuals(focusPlayer.getPosition());
            cancelConfirmButton.setText(CANCEL_TEXT);
        }
    }

    //transfer in a chosen player
    public void transferInPlayer(Player p){
        if(status == CONFIRMED_STATUS) return;
        if(status != TRANSFER_STATUS) return;

        Player playerOut = focusPlayer.getPlayer();
        Player playerIn = p;

        if(!p.getGeneralPosition().equals(focusPlayer.getPosition())){
            return;
        }

        if(!fantasyTeam.makeTransfer(playerOut, playerIn, componentArrayHasElement(benchPanel.getComponents(), benchPanel.getComponentCount(), focusPlayer))){
            status = TRANSFER_STATUS;
            return;
        }

        if(focusPlayer.isCaptain()){
            fantasyTeam.makeCaptain(playerIn);
        }
        else if(focusPlayer.isViceCaptain()){
            fantasyTeam.makeViceCaptain(playerIn);
        }

        status = NEUTRAL_STATUS;
        focusPlayer.setPlayer(playerIn, viewingGameWeek);
        focusPlayer = null;
        updateTransferInformation();
    }

    //helper method to check is a component array has an element in it
    private boolean componentArrayHasElement(Component[] componentArray, int arraySize, Component c){
        for(int i = 0; i < arraySize; i++){
            if(componentArray[i] == c) return true;
        }
        return false;
    }



    //private subclass backgroundpanel
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
