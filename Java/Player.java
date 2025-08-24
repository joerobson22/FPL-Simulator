package Java;

import java.util.ArrayList;

public class Player{
    private int playerID;

    private int rating;
    private double price;

    private String position;
    private String name;
    private Team team;

    private int totalPoints;
    private int weeklyPoints;
    private int numGoals;
    private int numAssists;
    private int numCleanSheets;
    private ArrayList<Integer> weeklyPointHistory;

    //constructor
    public Player(int id, int rating, double price, String position, String name, Team team)
    {
        this.playerID = id;

        this.rating = rating;
        this.price = price;
        this.position = position;
        this.name = name;
        this.team = team;
        weeklyPointHistory = new ArrayList<>();

        totalPoints = 0;
        numGoals = 0;
        numAssists = 0;
        numCleanSheets = 0;
    }


    //accessors
    public int getID(){
        return playerID;
    }

    public int getRating(){
        return rating;
    }

    public double getPrice(){
        return price;
    }

    public String getPosition(){
        return position;
    }

    public String getName(){
        return name;
    }

    public Team getTeam(){
        return team;
    }

    public int getTotalPoints(){
        return totalPoints;
    }

    public int getWeeklyPoints(){
        return weeklyPoints;
    }

    public int getNumGoals(){
        return numGoals;
    }

    public int getNumAssists(){
        return numAssists;
    }

    public int getNumCleanSheets(){
        return numCleanSheets;
    }

    public String toDictionaryString(){
        return String.format("id:%d,rating:%d,position:%s,name:%s", playerID, rating, position, name);
    }

    public int getGameWeekPoints(int gameWeek){
        if(gameWeek > weeklyPointHistory.size()) return 0;
        return weeklyPointHistory.get(gameWeek);
    }

    //mutators
    public void changePrice(double change){
        price += change;
    }

    public void changeTeam(Team newTeam){
        team = newTeam;
    }

    public void addWeeklyToTotal(){
        totalPoints += weeklyPoints;
        weeklyPointHistory.add(weeklyPoints);
    }

    public void resetWeeklyPoints(){
        weeklyPoints = 0;
    }

    public void changeWeeklyPoints(int change){
        weeklyPoints += change;
    }

    public void increaseNumGoals(int increase){
        numGoals += Math.abs(increase);
    }

    public void increaseNumAssists(int increase){
        numAssists += Math.abs(increase);
    }

    public void increaseNumCleanSheets(){
        numCleanSheets++;
    }
}