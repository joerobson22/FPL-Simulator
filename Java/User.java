package Java;
import java.util.ArrayList;

public class User {
    private String name;

    private ArrayList<Player> players;
    private double budget;
    private int totalPoints;
    private int weeklyPoints;
    private int freeTransfers;

    //constructor
    public User(String name){
        this.name = name;

        players = new ArrayList<Player>();
        budget = 100.0;
        weeklyPoints = 0;
        totalPoints = 0;
        freeTransfers = -1; //negative free transfers indicates infinite
    }


    //accessors
    public String getName(){
        return name;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public double getBudget(){
        return budget;
    }

    public int getWeeklyPoints(){
        return weeklyPoints;
    }

    public int getTotalPoints(){
        return totalPoints;
    }

    public int getFreeTransfers(){
        return freeTransfers;
    }


    //mutators
    private void addPlayer(Player player){
        budget -= player.getPrice();
        players.add(player);
    }

    private void removePlayer(Player player){
        budget += player.getPrice();
        players.remove(player);
    }

    public void changeWeeklyPoints(int change){
        weeklyPoints += change;
    }

    public void addWeeklyToTotal(){
        totalPoints += weeklyPoints;
        weeklyPoints = 0;
    }

    public void makeTransfer(Player oldPlayer, Player newPlayer){
        addPlayer(newPlayer);
        removePlayer(oldPlayer);
    }

    public boolean confirmTransfers(){
        return budget >= 0.0;
    }

    public void revertTransfers(ArrayList<Player> originalPlayers, ArrayList<Player> invalidPlayers){
        for(int i = 0; i < originalPlayers.size(); i++){
            makeTransfer(invalidPlayers.get(i), originalPlayers.get(i));
        }
    }
}
