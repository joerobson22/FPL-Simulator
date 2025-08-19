package Java;
import java.util.ArrayList;

public class FixtureOutcome {
    private int homeGoals;
    private int awayGoals;

    private ArrayList<Player> goalScorers;
    private ArrayList<Player> assisters;
    private ArrayList<Player> cleanSheets;

    //constructors
    public FixtureOutcome(){
        homeGoals = -1;
        awayGoals = -1;

        goalScorers = new ArrayList<>();
        assisters = new ArrayList<>();
        cleanSheets = new ArrayList<>();
    }

    public FixtureOutcome(int homeGoals, int awayGoals, ArrayList<Player> goalScorers, ArrayList<Player> assisters, ArrayList<Player> cleanSheets){
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.goalScorers = goalScorers;
        this.assisters = assisters;
        this.cleanSheets = cleanSheets;
    }


    //accessors
    public int getHomeGoals(){
        return homeGoals;
    }

    public int getAwayGoals(){
        return awayGoals;
    }

    public ArrayList<Player> getGoalScorers(){
        return goalScorers;
    }

    public ArrayList<Player> getAssisters(){
        return assisters;
    }

    public ArrayList<Player> getCleanSheets(){
        return cleanSheets;
    }

    public String getScoreString(){
        return Integer.toString(homeGoals) + "-" + Integer.toString(awayGoals);
    }
}


