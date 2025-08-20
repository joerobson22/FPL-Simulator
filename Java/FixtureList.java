package Java;
import java.util.ArrayList;

public class FixtureList {
    private ArrayList<ArrayList<Fixture>> fixtureList;

    //constructor
    public FixtureList(){
        fixtureList = new ArrayList<>();
    }

    //accessors
    public ArrayList<Fixture> getFixtures(int gameWeek){
        return fixtureList.get(gameWeek);
    }

    public Fixture getFixture(int gameWeek, String teamName){
        for(Fixture f : fixtureList.get(gameWeek)){
            if(f.getHomeTeam().getName() == teamName | f.getAwayTeam().getName() == teamName){
                return f;
            }
        }
        return null;
    }

    //mutators
    public void addFixture(int gameWeek, Fixture fixture){
        if(fixtureList.size() == 0) fixtureList.add(new ArrayList<Fixture>());

        fixtureList.get(gameWeek).add(fixture);
    }
}
