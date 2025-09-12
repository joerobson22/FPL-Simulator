package Frontend;
import java.util.ArrayList;

public class FantasyTeam {
    private String name;

    private ArrayList<PreviousFantasyTeam> teamHistory;

    private ArrayList<Player> players;
    private ArrayList<Player> startingXI;
    private ArrayList<Player> bench;
    private ArrayList<Player> subs;

    private Player captain;
    private Player viceCaptain;

    private double budget;
    private int totalPoints;
    private int weeklyPoints;
    private int freeTransfers;
    private boolean tripleCaptain;

    //constructor
    public FantasyTeam(String name){
        this.name = name;

        players = new ArrayList<Player>();
        startingXI = new ArrayList<>();
        bench = new ArrayList<>();
        teamHistory = new ArrayList<>();
        subs = new ArrayList<>();

        budget = 100.0;
        weeklyPoints = 0;
        totalPoints = 0;
        freeTransfers = -1; //negative free transfers indicates infinite
    }


    //accessors
    public void saveTeam(){
        teamHistory.add(new PreviousFantasyTeam(startingXI, bench, captain, viceCaptain, weeklyPoints, tripleCaptain));
    }

    public boolean isTeamValid(){
        //System.out.println(players.size());
        //System.out.println(checkTeamMax());
        //System.out.println(captain != null);
        //System.out.println(viceCaptain != null);
        return (players.size() == 15 && checkTeamMax() && captain != null && viceCaptain != null);
    }

    public PreviousFantasyTeam getPreviousFantasyTeam(int gameWeek){
        return teamHistory.get(gameWeek);
    }

    public String getName(){
        return name;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public ArrayList<Player> getStartingXI() { return startingXI; }
    public ArrayList<Player> getBench() { return bench; }
    public Player getCaptain() { return captain; }
    public Player getViceCaptain() { return viceCaptain; }
    public int getPoints() { return weeklyPoints; }

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

    public ArrayList<Player> getSubs(){
        return subs;
    }

    //mutators
    public void wildCardPlayed(){
        freeTransfers = -1;
    }

    public void freeHitPlayed(){
        freeTransfers = -1;
        //save team state
    }

    public void tripleCaptainPlayed(){
        //idk note this in some variable i suppose
        tripleCaptain = true;
    }

    public void nextGameWeek(){
        saveTeam();
        resetWeeklyTotal();
        addFreeTransfer();
        subs.clear();
    }

    public void addSub(Player player){
        subs.add(player);
    }

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

    public void changeWeeklyPoints(Player p){
        int change = p.getWeeklyPoints();
        int captainMulti = tripleCaptain ? 3 : 2;
        if(p == captain) change *= captainMulti;
        weeklyPoints += change;
    }

    public void addWeeklyToTotal(){
        totalPoints += weeklyPoints;
    }

    public void resetWeeklyTotal(){
        weeklyPoints = 0;
    }

    public void addFreeTransfer(){
        if(freeTransfers == -1) freeTransfers = 1;
        else freeTransfers++;
    }

    public boolean makeTransfer(Player oldPlayer, Player newPlayer, boolean onBench){
        if(players.contains(newPlayer)) return false;

        addPlayer(newPlayer);
        removePlayer(oldPlayer);

        bench.remove(oldPlayer);
        startingXI.remove(oldPlayer);

        if(onBench) bench.add(newPlayer);
        else startingXI.add(newPlayer);

        if(freeTransfers == -1) freeTransfers = -1;
        else if(freeTransfers > 0) freeTransfers--;
        else weeklyPoints += PointLookupTable.getPointsForTransfer();

        return true;
    }

    public void makeSubstitution(Player player1, Player player2, boolean player1Bench, boolean player2Bench){
        if(player1Bench){
            bench.remove(player1);
            bench.add(player2);
        }
        else{
            startingXI.remove(player1);
            startingXI.add(player2);
        } 

        if(player2Bench){
            bench.remove(player2);
            bench.add(player1);
        }
        else{
            startingXI.remove(player2);
            startingXI.add(player1);
        }


    }

    public void makeCaptain(Player p){
        captain = p;
    }

    public void makeViceCaptain(Player p){
        viceCaptain = p;
    }


    //helper methods
    private boolean checkTeamMax(){
        ArrayList<Team> checkedTeam = new ArrayList<>();
        for(Player p1 : players){
            if(checkedTeam.contains(p1.getTeam())) continue;

            int teamCount = 1;
            for(Player p2 : players){
                if(p1 == p2) continue;

                if(p1.getTeam() == p2.getTeam()){
                    teamCount++;
                    if(teamCount > 3) return false;
                }
            }
        }
        return true;
    }
}
