package Frontend;

import java.util.ArrayList;

public class PreviousFantasyTeam{
    private ArrayList<Player> startingXI;
    private ArrayList<Player> bench;
    private Player captain;
    private Player viceCaptain;
    private int points;

    public PreviousFantasyTeam(ArrayList<Player> startingXI, ArrayList<Player> bench, Player captain, Player viceCaptain, int points){
        this.startingXI = new ArrayList<>();
        this.bench = new ArrayList<>();
        this.captain = captain;
        this.viceCaptain = viceCaptain;
        this.points = points;

        //make copies of the starting 11 and bench otherwise confusing james
        for(Player p : startingXI) this.startingXI.add(p);
        for(Player p : bench) this.bench.add(p);
    }

    public ArrayList<Player> getStartingXI() { return startingXI; }
    public ArrayList<Player> getBench() { return bench; }
    public Player getCaptain() { return captain; }
    public Player getViceCaptain() { return viceCaptain; }
    public int getPoints() { return points; }
}

