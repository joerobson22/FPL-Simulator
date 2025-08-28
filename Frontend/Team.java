package Frontend;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Team {
    private static Map<String, Integer[]> minMaxPositions = Map.of(
        "GK", new Integer[]{1, 1},
        "DEF", new Integer[]{3, 5},
        "MID", new Integer[]{3, 6},
        "ATT", new Integer[]{1, 2}
    );

    private int rating;
    private String name;
    private String abbrv;

    private int totalPoints;
    private int goalDifference;

    private ArrayList<Player> players;

    //constructor
    public Team(int rating, String name, String abbrv){
        this.rating = rating;
        this.name = name;
        this.abbrv = abbrv;

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

    public String getAbbrv(){
        return abbrv;
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

    public ArrayList<Player> getStartingXI(){
        //FIRST sort all players in order of manager favour
        ArrayList<Player> starting = bubbleSortPlayersMAR(players);

        //SECOND run through each position in minMaxPositions
        Map<String, Integer> formation = new HashMap<>(Map.of(
        "GK", 0,
        "DEF", 0,
        "MID", 0,
        "ATT", 0
        ));

        int totalPlayers = 0;

        while(totalPlayers < 11){
            for(String key : minMaxPositions.keySet()){
                if(formation.get(key) >= minMaxPositions.get(key)[1]) continue;

                for(Player p : players){
                    if(!p.getGeneralPosition().equals(key)) continue;

                    starting.add(p);
                    formation.merge(key, 1, Integer::sum);
                }
            }
        }
        

        return starting;
    }

    public ArrayList<Player> bubbleSortPlayersMAR(ArrayList<Player> players){
        boolean swaps = true;
        while(swaps){
            swaps = false;
            int n = players.size();
            for(int i = 1; i < n; i++){
                if(players.get(i).getManagerApprovalRating() > players.get(i - 1).getManagerApprovalRating()){
                    //swap the players
                    Player temp = players.get(i);
                    players.set(i, players.get(i - 1));
                    players.set(i - 1, temp);

                    swaps = true;
                }
            }
            n--;
        }

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
