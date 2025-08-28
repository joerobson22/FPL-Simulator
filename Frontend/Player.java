package Frontend;

import java.util.ArrayList;

public class Player{
    private int playerID;

    private int rating;
    private double price;

    private String specificPosition;
    private String generalPosition;
    private String teamPosition;
    private String name;
    private Team team;

    private int pace;
    private int shooting;
    private int passing;
    private int dribbling;
    private int defending;
    private int physical;

    private int totalPoints;
    private int weeklyPoints;
    private int numGoals;
    private int numAssists;
    private int numCleanSheets;
    private ArrayList<Integer> weeklyPointHistory;

    //constructor
    public Player(int id, String name, int rating, String specificPosition, String teamPosition, Team team, ArrayList<Integer> attributes)
    {
        this.playerID = id;
        this.name = name;
        this.rating = rating;
        this.specificPosition = specificPosition;
        this.teamPosition = teamPosition;
        this.team = team;

        if(!specificPosition.equals("GK")) setAttributes(attributes);
        generalPosition = PositionLookupTable.getGeneralPosition(specificPosition);

        price = 0;

        weeklyPointHistory = new ArrayList<>();
        totalPoints = 0;
        numGoals = 0;
        numAssists = 0;
        numCleanSheets = 0;
    }

    private void setAttributes(ArrayList<Integer> attributes){
        this.pace = attributes.get(0);
        this.shooting = attributes.get(1);
        this.passing = attributes.get(2);
        this.dribbling = attributes.get(3);
        this.defending = attributes.get(4);
        this.physical = attributes.get(5);
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

    public String getSpecificPosition(){
        return specificPosition;
    }

    public String getGeneralPosition(){
        return generalPosition;
    }

    public String getTeamPosition(){
        return teamPosition;
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
        return String.format("id:%d,rating:%d,specificPosition:%s,generalPosition:%s,name:%s", playerID, rating, specificPosition, generalPosition, name);
    }

    public int getGameWeekPoints(int gameWeek){
        if(gameWeek >= weeklyPointHistory.size()) return 0;
        return weeklyPointHistory.get(gameWeek);
    }

    //mutators
    public void scoreGoal(){
        numGoals++;
        weeklyPoints += PointLookupTable.getPointsForGoal(generalPosition);
    }

    public void makeAssist(){
        numAssists++;
        weeklyPoints += PointLookupTable.getPointsForAssist();
    }

    public void keepCleanSheet(){
        numCleanSheets++;
        weeklyPoints += PointLookupTable.getPointsForCleanSheet(generalPosition);
    }

    public void setPrice(double price){
        this.price = price;
    }

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
}