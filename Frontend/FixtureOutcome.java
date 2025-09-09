package Frontend;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class FixtureOutcome {
    private int homeGoals;
    private int awayGoals;

    private ArrayList<Player> goalScorers;
    private ArrayList<Player> assisters;
    private ArrayList<Player> cleanSheets;
    private ArrayList<Player> sixtyMins;
    private ArrayList<Player> threeSaves;
    private ArrayList<Player> dfCon;

    //constructors
    public FixtureOutcome(){
        homeGoals = -1;
        awayGoals = -1;

        goalScorers = new ArrayList<>();
        assisters = new ArrayList<>();
        cleanSheets = new ArrayList<>();
    }

    public FixtureOutcome(int homeGoals, int awayGoals, ArrayList<Player> goalScorers, ArrayList<Player> assisters, ArrayList<Player> cleanSheets, ArrayList<Player> sixtyMins, ArrayList<Player> threeSaves, ArrayList<Player> dfCon){
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.goalScorers = goalScorers;
        this.assisters = assisters;
        this.cleanSheets = cleanSheets;
        this.sixtyMins = sixtyMins;
        this.threeSaves = threeSaves;
        this.dfCon = dfCon;
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

    public ArrayList<Player> getThreeSaves(){
        return threeSaves;
    }

    public ArrayList<Player> getDFCon(){
        return dfCon;
    }

    public String getScoreString(){
        return Integer.toString(homeGoals) + "-" + Integer.toString(awayGoals);
    }
}


