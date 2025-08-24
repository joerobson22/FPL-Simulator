package Java;
import java.util.ArrayList;

public class Fixture {
    private Team home;
    private Team away;
    private FixtureOutcome outcome;
    private boolean played;

    //constructor
    public Fixture(Team home, Team away){
        this.home = home;
        this.away = away;

        outcome = new FixtureOutcome();
        played = false;
    }


    //accessors
    public Team getHomeTeam(){
        return home;
    }

    public Team getAwayTeam(){
        return away;
    }

    public FixtureOutcome getOutcome(){
        return outcome;
    }

    public boolean hasPlayed(){
        return played;
    }

    //mutators
    public void playFixture(FixtureOutcome outcome){
        played = true;
        this.outcome = outcome;
    }

    public void allocatePointsAndChangeStats(){
        //allocate points for goals
        for(Player p : outcome.getGoalScorers()){
            p.scoreGoal();
        }
        //allocate points for assists
        for(Player p : outcome.getAssisters()){
            p.makeAssist();
        }
        //allocate points for clean sheets
        for(Player p : outcome.getCleanSheets()){
            p.keepCleanSheet();
        }

        //store weekly totals
        for(Player p : home.getPlayers()){
            p.addWeeklyToTotal();
        }
        for(Player p : away.getPlayers()){
            p.addWeeklyToTotal();
        }
    }
}
