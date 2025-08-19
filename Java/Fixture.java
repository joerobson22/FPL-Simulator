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
    public void playFixture(int homeGoals, int awayGoals, ArrayList<Player> goalScorers, ArrayList<Player> assisters, ArrayList<Player> cleanSheets){
        played = true;
        outcome = new FixtureOutcome(homeGoals, awayGoals, goalScorers, assisters, cleanSheets);
    }
}
