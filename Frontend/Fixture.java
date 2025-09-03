package Frontend;
import java.util.ArrayList;


public class Fixture {
    private Team home;
    private Team away;
    private ArrayList<Player> homeLineup;
    private ArrayList<Player> awayLineup;
    private FixtureOutcome outcome;
    private boolean played;

    //constructor
    public Fixture(Team home, Team away){
        this.home = home;
        this.away = away;

        outcome = new FixtureOutcome();
        played = false;

        homeLineup = new ArrayList<>();
        awayLineup = new ArrayList<>();
    }


    //accessors
    public String getFixtureString(boolean homeTeam){
        if(homeTeam) return away.getAbbrv() + " (H)";
        else return home.getAbbrv() + " (A)";
    }

    public Team getHomeTeam(){
        return home;
    }

    public Team getAwayTeam(){
        return away;
    }

    public ArrayList<Player> getLineups(){
        ArrayList<Player> lineups = new ArrayList<>();
        for(Player p : homeLineup) lineups.add(p);
        for(Player p : awayLineup) lineups.add(p);
        return lineups;
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

        home.calculateThisOutcome(this);
        away.calculateThisOutcome(this);
    }

    public void setHomeLineup(ArrayList<Player> players){
        homeLineup = players;
    }

    public void setAwayLineup(ArrayList<Player> players){
        awayLineup = players;
    }

    public void allocatePointsAndChangeStats(int gameWeek){
        for(Player p : home.getPlayers()){
            p.playMatch();
        }
        for(Player p : away.getPlayers()){
            p.playMatch();
        }
        
        //allocate manager favour for any goals conceded
        for(Player p : homeLineup){
            p.concedeGoals(outcome.getAwayGoals());
        }
        for(Player p : awayLineup){
            p.concedeGoals(outcome.getHomeGoals());
        }
        
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
        //allocate points for playing 60 mins
        for(Player p : outcome.get60Mins()){
            p.play60Mins();
        }
        //allocate manager favour for any blanks
        for(Player p : homeLineup){
            p.inStartingLineup();
            if(outcome.getGoalScorers().contains(p) || outcome.getAssisters().contains(p)) continue;
            p.blank();
        }
        for(Player p : awayLineup){
            p.inStartingLineup();
            if(outcome.getGoalScorers().contains(p) || outcome.getAssisters().contains(p)) continue;
            p.blank();
        }

        //store weekly totals
        for(Player p : home.getPlayers()){
            p.addWeeklyToTotal(gameWeek);
        }
        for(Player p : away.getPlayers()){
            p.addWeeklyToTotal(gameWeek);
        }
    }
}
