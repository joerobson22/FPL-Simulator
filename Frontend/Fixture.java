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
        System.out.println("Allocating points");

        
        System.out.println("Goals conceded");
        //allocate manager favour for any goals conceded
        for(Player p : homeLineup){
            p.concedeGoals(outcome.getAwayGoals());
        }
        for(Player p : awayLineup){
            p.concedeGoals(outcome.getHomeGoals());
        }
        
        System.out.println("Goals scored");
        //allocate points for goals
        for(Player p : outcome.getGoalScorers()){
            p.scoreGoal();
        }
        System.out.println("Assists");
        //allocate points for assists
        for(Player p : outcome.getAssisters()){
            p.makeAssist();
        }
        System.out.println("Clean sheets");
        //allocate points for clean sheets
        for(Player p : outcome.getCleanSheets()){
            p.keepCleanSheet();
        }
        System.out.println("60 mins");
        //allocate points for playing 60 mins
        for(Player p : outcome.get60Mins()){
            p.play60Mins();
        }

        System.out.println("Manager favour for blanks");
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


        System.out.println("Store weekly totals");
        //store weekly totals
        for(Player p : home.getPlayers()){
            p.addWeeklyToTotal(gameWeek);
        }
        for(Player p : away.getPlayers()){
            p.addWeeklyToTotal(gameWeek);
        }
    }
}
