package Java;
import java.util.ArrayList;

public class FixtureOutcome {
    private int homeGoals;
    private int awayGoals;

    private ArrayList<String> goalScorers;
    private ArrayList<String> assisters;
    private ArrayList<String> cleanSheets;

    //constructors
    public FixtureOutcome(){
        homeGoals = -1;
        awayGoals = -1;

        goalScorers = new ArrayList<>();
        assisters = new ArrayList<>();
        cleanSheets = new ArrayList<>();
    }

    public FixtureOutcome(int homeGoals, int awayGoals, ArrayList<String> goalScorers, ArrayList<String> assisters){
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.goalScorers = goalScorers;
        this.assisters = assisters;

        //generate clean sheeters
    }


    //accessors
    public int getHomeGoals(){
        return homeGoals;
    }

    public int getAwayGoals(){
        return awayGoals;
    }

    public ArrayList<String> getGoalScorers(){
        return goalScorers;
    }

    public ArrayList<String> getAssisters(){
        return assisters;
    }

    public ArrayList<String> getCleanSheets(){
        return cleanSheets;
    }

    public String getScoreString(){
        return Integer.toString(homeGoals) + "-" + Integer.toString(awayGoals);
    }
}


