package Java;
import java.util.ArrayList;

public class Team {
    private int rating;
    private String name;

    private int totalPoints;
    private int goalDifference;

    private ArrayList<Player> players;

    //constructor
    public Team(int rating, String name){
        this.rating = rating;
        this.name = name;

        totalPoints = 0;
        goalDifference = 0;
        players = new ArrayList<Player>();
    }


    //accessors
    public int getRating(){
        return rating;
    }

    public String getName(){
        return name;
    }

    public int getTotalPoints(){
        return totalPoints;
    }

    public int getGoalDifference(){
        return goalDifference;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }


    //mutators
    public void increaseTotalPoints(int increase){
        totalPoints += Math.abs(increase);
    }

    public void changeGoalDifference(int change){
        goalDifference += change;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }
}
