package Frontend;
import java.util.ArrayList;

public class FantasyTeam {
    private String name;

    private ArrayList<Player> players;
    private ArrayList<Player> startingXI;
    private ArrayList<Player> bench;

    private Player captain;
    private Player viceCaptain;

    private ArrayList<Integer> gameWeekPointsHistory;

    private double budget;
    private int totalPoints;
    private int weeklyPoints;
    private int freeTransfers;

    //constructor
    public FantasyTeam(String name){
        this.name = name;

        players = new ArrayList<Player>();
        startingXI = new ArrayList<>();
        bench = new ArrayList<>();

        gameWeekPointsHistory = new ArrayList<>();
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

    public String getFreeTransferString(){
        if(freeTransfers == -1) return "INF";
        return String.valueOf(freeTransfers);
    }

    //mutators
    private void addPlayer(Player player){
        budget -= player.getPrice();
        players.add(player);
    }

    private void removePlayer(Player player){
        if(player == null) return;
        budget += player.getPrice();
        players.remove(player);
    }

    public void swapPlayers(Player player1, Player player2){
        int index1 = findPlayer(player1, players);
        int index2 = findPlayer(player2, players);

        players.set(index1, player2);
        players.set(index2, player1);
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
    }

    public void resetWeeklyTotal(){
        weeklyPoints = 0;
    }

    public boolean makeTransfer(Player oldPlayer, Player newPlayer){
        if(players.contains(newPlayer)) return false;

        addPlayer(newPlayer);
        removePlayer(oldPlayer);

        if(freeTransfers > -1 && freeTransfers > 0) freeTransfers--;
        else changeWeeklyPoints(PointLookupTable.getPointsForTransfer());

        return true;
    }

    public void makeCaptain(Player p){
        captain = p;
    }

    public void makeViceCaptain(Player p){
        viceCaptain = p;
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
