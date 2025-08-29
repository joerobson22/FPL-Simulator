package Frontend;
import java.util.ArrayList;

public class FixtureOutcome {
    private int homeGoals;
    private int awayGoals;

    private ArrayList<Player> goalScorers;
    private ArrayList<Player> assisters;
    private ArrayList<Player> cleanSheets;
    private ArrayList<Player> sixtyMins;

    //constructors
    public FixtureOutcome(){
        homeGoals = -1;
        awayGoals = -1;

        goalScorers = new ArrayList<>();
        assisters = new ArrayList<>();
        cleanSheets = new ArrayList<>();
    }

    public FixtureOutcome(int homeGoals, int awayGoals, ArrayList<Player> goalScorers, ArrayList<Player> assisters, ArrayList<Player> cleanSheets, ArrayList<Player> sixtyMins){
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.goalScorers = goalScorers;
        this.assisters = assisters;
        this.cleanSheets = cleanSheets;
        this.sixtyMins = sixtyMins;
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

    public ArrayList<Player> get60Mins(){
        return sixtyMins;
    }

    public String getScoreString(){
        return Integer.toString(homeGoals) + "-" + Integer.toString(awayGoals);
    }
}


