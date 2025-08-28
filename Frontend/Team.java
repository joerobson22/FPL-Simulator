package Frontend;
import java.util.ArrayList;

public class Team {
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
        ArrayList<Player> starting = new ArrayList<Player>();
        for(Player p : players){
            String pos = p.getTeamPosition();
            System.out.println(pos);

            if (pos == null || pos.isBlank()) continue;
            if(pos.equals("SUB") || pos.equals("RES")) continue;

            System.out.println("Added!");
            starting.add(p);
        }

        starting = bubbleSortPlayers(starting);
        ArrayList<Player> finalXI = new ArrayList<Player>();

        for(int i = 0; i < 11; i++) finalXI.add(starting.get(i));

        return finalXI;
    }

    public ArrayList<Player> bubbleSortPlayers(ArrayList<Player> players){
        boolean swaps = true;
        while(swaps){
            swaps = false;
            int n = players.size();
            for(int i = 1; i < n; i++){
                if(players.get(i).getRating() > players.get(i - 1).getRating()){
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
