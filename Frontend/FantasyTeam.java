package Frontend;
import java.util.ArrayList;

public class FantasyTeam {
    private String name;

    private ArrayList<Player> players;
    private ArrayList<Player> bench;

    private double budget;
    private int totalPoints;
    private int weeklyPoints;
    private int freeTransfers;

    //constructor
    public FantasyTeam(String name){
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

    public ArrayList<Player> getBench(){
        return bench;
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

    public void swapPlayers(Player player1, Player player2){
        boolean player1Bench = false;
        boolean player2Bench = false;

        int index1 = findPlayer(player1, players);
        player1Bench = index1 == -1;
        if(player1Bench) index1 = findPlayer(player1, bench);

        int index2 = findPlayer(player2, players);
        player2Bench = index2 == -1;
        if(player2Bench) index2 = findPlayer(player2, bench);


        ArrayList<Player> player1List;
        ArrayList<Player> player2List;

        if(player1Bench) player1List = players;
        else player1List = bench;
        if(player2Bench) player2List = players;
        else player2List = bench;

        player1List.set(index1, player2);
        player2List.set(index2, player1);
    }

    private int findPlayer(Player p, ArrayList<Player> list){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i) == p) return i;
        }
        return -1;
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
