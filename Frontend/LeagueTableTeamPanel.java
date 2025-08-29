package Frontend;
import javax.swing.*;
import java.awt.*;

public class LeagueTableTeamPanel extends JPanel {

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
        positionLabel = new JLabel(String.valueOf(position));
        teamNameLabel = new JLabel(team.getName());
        //points panel: this team's wins, draws, losses, gd and points
        winsLabel = new JLabel(String.valueOf(team.getNumWins()));
        drawsLabel = new JLabel(String.valueOf(team.getNumDraws()));
        lossesLabel = new JLabel(String.valueOf(team.getNumLosses()));
        gdLabel = new JLabel(String.valueOf(team.getGoalDifference()));
        pointsLabel = new JLabel(String.valueOf(team.getTotalPoints()));

        teamPanel = new JPanel();
        pointsPanel = new JPanel();

        teamPanel.add(positionLabel);
        teamPanel.add(teamNameLabel);

        pointsPanel.add(winsLabel);
        pointsPanel.add(drawsLabel);
        pointsPanel.add(lossesLabel);
        pointsPanel.add(gdLabel);
        pointsPanel.add(pointsLabel);

        this.add(teamPanel);
        this.add(pointsPanel);
    }

    public void update(){
        positionLabel.setText(String.valueOf(position));
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
