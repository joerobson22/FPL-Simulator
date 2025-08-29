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
    JLabel winsLabel;
    JLabel drawsLabel;
    JLabel lossesLabel;
    JLabel gdLabel;
    JLabel pointsLabel;

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
        positionLabel = new JLabel(String.valueOf(position) + ".    ");
        teamNameLabel = new JLabel(team.getName());
        //points panel: this team's wins, draws, losses, gd and points
        winsLabel = new JLabel(String.valueOf(team.getNumWins()), SwingConstants.CENTER);
        drawsLabel = new JLabel(String.valueOf(team.getNumDraws()), SwingConstants.CENTER);
        lossesLabel = new JLabel(String.valueOf(team.getNumLosses()), SwingConstants.CENTER);
        gdLabel = new JLabel(String.valueOf(team.getGoalDifference()), SwingConstants.CENTER);
        pointsLabel = new JLabel(String.valueOf(team.getTotalPoints()), SwingConstants.CENTER);

        teamPanel = new JPanel(new BorderLayout());
        pointsPanel = new JPanel(new GridLayout(1, 5));

        teamPanel.add(positionLabel, "West");
        teamPanel.add(teamNameLabel, "Center");
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
        teamNameLabel.setText(team.getName());
        
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
