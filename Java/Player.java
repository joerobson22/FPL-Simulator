package Java;

public class Player{
    private int rating;
    private double price;

    private String position;
    private String name;
    private Team team;

    private int totalPoints;
    private int numGoals;
    private int numAssists;
    private int numCleanSheets;

    //constructor
    public Player(int rating, double price, String position, String name, Team team)
    {
        this.rating = rating;
        this.price = price;
        this.position = position;
        this.name = name;
        this.team = team;

        totalPoints = 0;
        numGoals = 0;
        numAssists = 0;
        numCleanSheets = 0;
    }


    //accessors
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

    public int getNumGoals(){
        return numGoals;
    }

    public int getNumAssists(){
        return numAssists;
    }

    public int getNumCleanSheets(){
        return numCleanSheets;
    }


    //mutators
    public void changePrice(double change){
        price += change;
    }

    public void changeTeam(Team newTeam){
        team = newTeam;
    }

    public void changeTotalPoints(int change){
        totalPoints += change;
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