package Frontend;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class LeagueTableTeamPanel extends JPanel {
    
    private final Color WINNER_COLOR = new Color(255, 209, 56);
    private final Color CHAMPIONS_LEAGUE_COLOR = new Color(22, 182, 245);
    private final Color EUROPA_LEAGUE_COLOR = new Color(230, 98, 46);
    private final Color CONFERENCE_LEAGUE_COLOR = new Color(99, 199, 95);
    private final Color ORDINARY_COLOR = new Color(230, 230, 230);
    private final Color RELEGATION_COLOR = new Color(201, 115, 115);
    private final Color BOTTOM_COLOR = new Color(171, 84, 84);

    private final String font = "SansSerif";
    private final int teamNameSize = 12;
    private final int teamAbbrvSize = 14;
    private final int positionSize = 13;
    private final int statSize = 12;
    private final int pointSize = 14;

    private final int fixedIconWidth = 20;
    private final int fixedIconHeight = 20;

    private final Map<Integer, Color> COLOR_MAP = Map.ofEntries(
        Map.entry(1, WINNER_COLOR),
        Map.entry(2, CHAMPIONS_LEAGUE_COLOR),
        Map.entry(3, CHAMPIONS_LEAGUE_COLOR),
        Map.entry(4, CHAMPIONS_LEAGUE_COLOR),
        Map.entry(5, EUROPA_LEAGUE_COLOR),
        Map.entry(6, CONFERENCE_LEAGUE_COLOR),
        Map.entry(7, ORDINARY_COLOR),
        Map.entry(8, ORDINARY_COLOR),
        Map.entry(9, ORDINARY_COLOR),
        Map.entry(10, ORDINARY_COLOR),
        Map.entry(11, ORDINARY_COLOR),
        Map.entry(12, ORDINARY_COLOR),
        Map.entry(13, ORDINARY_COLOR),
        Map.entry(14, ORDINARY_COLOR),
        Map.entry(15, ORDINARY_COLOR),
        Map.entry(16, ORDINARY_COLOR),
        Map.entry(17, ORDINARY_COLOR),
        Map.entry(18, RELEGATION_COLOR),
        Map.entry(19, RELEGATION_COLOR),
        Map.entry(20, BOTTOM_COLOR)
    );


    JPanel teamPanel;
    JPanel pointsPanel;

    JLabel positionLabel;
    JLabel teamNameLabel;
    JLabel teamAbbrvLabel;
    JLabel winsLabel;
    JLabel drawsLabel;
    JLabel lossesLabel;
    JLabel gdLabel;
    JLabel pointsLabel;
    JLabel teamBadge;

    Team team;
    int position;

    public LeagueTableTeamPanel(Team team, int position){
        this.team = team;
        this.position = position;

        setup();
    }

    private void setup(){
        this.setLayout(new GridLayout(1, 2));
        //team panel: this panel's position and team name
        positionLabel = LabelCreator.createJLabel(String.valueOf(position) + ".    ", font, positionSize, Font.BOLD, SwingConstants.LEFT, Color.BLACK);
        teamNameLabel = LabelCreator.createJLabel(" " + team.getName(), font, teamNameSize, Font.PLAIN, SwingConstants.LEFT, Color.BLACK);
        teamAbbrvLabel = LabelCreator.createJLabel(team.getAbbrv() + "  ", font, teamAbbrvSize, Font.BOLD, SwingConstants.RIGHT, Color.BLACK);
        //points panel: this team's wins, draws, losses, gd and points
        winsLabel = LabelCreator.createJLabel(String.valueOf(team.getNumWins()), font, statSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        drawsLabel = LabelCreator.createJLabel(String.valueOf(team.getNumDraws()), font, statSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        lossesLabel = LabelCreator.createJLabel(String.valueOf(team.getNumLosses()), font, statSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        gdLabel = LabelCreator.createJLabel(String.valueOf(team.getGoalDifference()), font, statSize, Font.PLAIN, SwingConstants.CENTER, Color.BLACK);
        pointsLabel = LabelCreator.createJLabel(String.valueOf(team.getTotalPoints()), font, pointSize, Font.BOLD, SwingConstants.CENTER, Color.BLACK);

        teamPanel = new JPanel(new BorderLayout());
        pointsPanel = new JPanel(new GridLayout(1, 5));

        teamPanel.add(positionLabel, "West");
        JPanel teamNameAndBadgePanel = new JPanel(new BorderLayout());
        teamNameAndBadgePanel.add(teamNameLabel, "Center");
        teamBadge = LabelCreator.getIconLabel(team.getLogoPath(), fixedIconWidth, fixedIconHeight);
        teamNameAndBadgePanel.add(teamBadge, "West");
        teamNameAndBadgePanel.setBackground(COLOR_MAP.get(position));
        teamPanel.add(teamNameAndBadgePanel);
        teamPanel.add(teamAbbrvLabel, "East");
        teamPanel.setBackground(COLOR_MAP.get(position));

        pointsPanel.add(winsLabel);
        pointsPanel.add(drawsLabel);
        pointsPanel.add(lossesLabel);
        pointsPanel.add(gdLabel);
        pointsPanel.add(pointsLabel);
        pointsPanel.setBackground(COLOR_MAP.get(position));

        this.add(teamPanel);
        this.add(pointsPanel);
    }

    public void update(){
        teamNameLabel.setText(" " + team.getName());
        teamAbbrvLabel.setText(team.getAbbrv());
        teamBadge.setIcon(LabelCreator.getImageIcon(team.getLogoPath(), fixedIconWidth, fixedIconHeight));
        
        winsLabel.setText(String.valueOf(team.getNumWins()));
        drawsLabel.setText(String.valueOf(team.getNumDraws()));
        lossesLabel.setText(String.valueOf(team.getNumLosses()));
        gdLabel.setText(String.valueOf(team.getGoalDifference()));
        pointsLabel.setText(String.valueOf(team.getTotalPoints()));
    }

    public boolean checkSwapWith(LeagueTableTeamPanel slot){
        Team otherTeam = slot.getTeam();
        int thisPoints = team.getTotalPoints();
        int otherPoints = otherTeam.getTotalPoints();
        int thisGD = team.getGoalDifference();
        int otherGD = otherTeam.getGoalDifference();

        return (thisPoints > otherPoints || (thisPoints == otherPoints && thisGD > otherGD));
    }

    public void swapTeamWith(LeagueTableTeamPanel slot){
        Team temp = slot.getTeam();
        slot.setTeam(this.getTeam());
        this.setTeam(temp);
    }

    public Team getTeam(){
        return team;
    }

    public void setTeam(Team t){
        team = t;
    }
}
