package Frontend;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class IOHandler {
    protected static final int HOME_GOALS = 0;
    protected static final int AWAY_GOALS = 1;
    protected static final int SCORERS = 2;
    protected static final int ASSISTERS = 3;
    protected static final int CLEAN_SHEETS = 4;
    protected static final int SIXTY_MINUTES = 5;
    protected static final int THREE_SAVES = 6;
    protected static final int DFCON = 7;
    protected static final int YELLOW_CARDS = 8;
    protected static final int RED_CARDS = 9;
    protected static final int PENALTY_GOAL = 10;
    protected static final int PENALTY_MISS = 11;
    protected static final int PENALTY_SAVE = 12;

    private static final String[] IGNORE_STRINGS = {"", "GOAL SCORERS", "ASSISTERS", "CLEAN SHEETS", "60 MINS", "3 SAVES", "DFCON", "YELLOW CARDS", "RED CARDS", "PENALTY GOALS", "PENALTY MISSES", "PENALTY SAVES"};

    public static void writeFixtureData(Fixture fixture, int seed){
        try{
            FileWriter writer = new FileWriter("fixtureData.txt");

            ArrayList<Player> homeLineup = fixture.getHomeTeam().getStartingXI();
            ArrayList<Player> awayLineup = fixture.getAwayTeam().getStartingXI();
            fixture.setHomeLineup(homeLineup);
            fixture.setAwayLineup(awayLineup);

            //seed
            writer.write(String.valueOf(seed) + "\n");

            //home team
            writer.write(fixture.getHomeTeam().getName() + "\n");

            //away team
            writer.write(fixture.getAwayTeam().getName() + "\n");

            //all players
            writer.write("HOME PLAYERS\n");
            for(Player p : homeLineup){
                writer.write(p.toDictionaryString() + "\n");
            }

            writer.write("AWAY PLAYERS\n");
            for(Player p : awayLineup){
                writer.write(p.toDictionaryString() + "\n");
            }

            writer.close();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
    }

    public static FixtureOutcome readFixtureOutcome(ArrayList<Player> players){
        //System.out.println("Reading fixture outcome");

        int homeGoals = 0;
        int awayGoals = 0;
        ArrayList<String> goalScorers = new ArrayList<>();
        ArrayList<String> assisters = new ArrayList<>();
        ArrayList<String> cleanSheets = new ArrayList<>();
        ArrayList<String> sixtyMins = new ArrayList<>();
        ArrayList<String> threeSaves = new ArrayList<>();
        ArrayList<String> dfCon = new ArrayList<>();
        ArrayList<String> yellowCards = new ArrayList<>();
        ArrayList<String> redCards = new ArrayList<>();
        ArrayList<String> penaltyGoal = new ArrayList<>();
        ArrayList<String> penaltyMiss = new ArrayList<>();
        ArrayList<String> penaltySave = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader("fixtureOutcome.txt"));
            int i = 0;
            String line = reader.readLine();

            while (line != null) {
                if(i == HOME_GOALS){
                    //System.out.println("Home team goals");
                    homeGoals = Integer.valueOf(line);
                }
                else if(i == AWAY_GOALS){
                    //System.out.println("Away team goals");
                    awayGoals = Integer.valueOf(line);
                }
                else if(i == SCORERS){
                    //System.out.println("Goal scorers");
                    goalScorers = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == ASSISTERS){
                    //System.out.println("Assisters");
                    assisters = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == CLEAN_SHEETS){
                    //System.out.println("Clean sheets");
                    cleanSheets = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == SIXTY_MINUTES){
                    //System.out.println("60 mins");
                    sixtyMins = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == THREE_SAVES){
                    //System.out.println("3 saves");
                    threeSaves = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == DFCON){
                    //System.out.println("DFCon");
                    dfCon = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == YELLOW_CARDS){
                    //System.out.println("DFCon");
                    yellowCards = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == RED_CARDS){
                    //System.out.println("DFCon");
                    redCards = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == PENALTY_GOAL){
                    penaltyGoal = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == PENALTY_MISS){
                    //System.out.println("DFCon");
                    penaltyMiss = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == PENALTY_SAVE){
                    //System.out.println("DFCon");
                    penaltySave = new ArrayList<String>(Arrays.asList(line.split(",")));
                }

                line = reader.readLine();

                i++;
            }

            reader.close();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
        
        //System.out.println("Generating FixtureOutcome object");
        FixtureOutcome outcome = new FixtureOutcome(homeGoals, awayGoals, 
                                    getPlayersFromID(players, goalScorers), getPlayersFromID(players, assisters), 
                                    getPlayersFromID(players, cleanSheets), getPlayersFromID(players, sixtyMins), 
                                    getPlayersFromID(players, threeSaves), getPlayersFromID(players, dfCon),
                                    getPlayersFromID(players, yellowCards), getPlayersFromID(players, redCards),
                                    getPlayersFromID(players, penaltyGoal), getPlayersFromID(players, penaltyMiss), 
                                    getPlayersFromID(players, penaltySave));
        return outcome;
    }

    private static ArrayList<Player> getPlayersFromID(ArrayList<Player> allPlayers, ArrayList<String> ids){
        ArrayList<Player> players = new ArrayList<>();

        for(String id : ids){
            boolean valid = true;
            for(String ignore : IGNORE_STRINGS){
                if(ignore.equals(id)){
                    valid = false;
                    break;
                }
            }
            if(!valid) continue;

            int integerID = Integer.parseInt(id);

            for(Player p : allPlayers){
                if(p.getID() == integerID){
                    players.add(p);
                    break;
                }
            }
        }

        return players;
    }

    public static ArrayList<Team> readAllTeamData(ArrayList<Team> allTeams){
        allTeams = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("allTeams.txt"));
            //read the first year line
            reader.readLine();
            String line = reader.readLine();

            int id = 1;

            while(line != null)
            {
                ArrayList<String> lineInfo = new ArrayList<String>(Arrays.asList(line.split("\t")));

                String name = lineInfo.get(0);
                String abbrv = lineInfo.get(1);
                String logoPath = "teamLogos/" + name.replace(' ', '_').replace('/', '_') + ".png";

                allTeams.add(new Team(id++, 0, name, abbrv, logoPath));
                line = reader.readLine();
            }

            reader.close();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }

        return allTeams;
    }

    public static FixtureList readAllFixtures(ArrayList<Team> allTeams){
        FixtureList fixtureList = new FixtureList();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("allFixtures.txt"));
            //read the first year line
            reader.readLine();
            String line = reader.readLine();

            int i = 0;
            int gameWeek = 0;

            while(line != null)
            {
                ArrayList<String> lineInfo = new ArrayList<String>(Arrays.asList(line.split(",")));

                String home = lineInfo.get(0);
                String away = lineInfo.get(1);

                Team homeTeam = findTeam(home, allTeams);
                Team awayTeam = findTeam(away, allTeams);
                
                fixtureList.addFixture(gameWeek, new Fixture(homeTeam, awayTeam));
                i++;

                if(i >= 10){
                    i = 0;
                    gameWeek++;
                }

                line = reader.readLine();
            }

            reader.close();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }

        return fixtureList;
    }

    private static Team findTeam(String name, ArrayList<Team> allTeams){
        for(Team t : allTeams){
            if(t.getName().equals(name)) return t;
        }
        return new Team(0, 0, "NULL", "NULL", "NULL");
    }

    public static ArrayList<Player> readAllPlayerData(ArrayList<Team> allTeams, ArrayList<Player> allPlayers){
        allPlayers = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader("allPlayers.txt"));
            String line = reader.readLine();

            //ArrayList<String> columns = new ArrayList<String>(Arrays.asList(line.split("\t")));

            int id = 0;
            line = reader.readLine();

            while(line != null)
            {
                ArrayList<String> lineInfo = new ArrayList<String>(Arrays.asList(line.split("\t")));

                String name = lineInfo.get(1);
                String teamName = lineInfo.get(2);
                String specificPosition = new ArrayList<String>(Arrays.asList(lineInfo.get(3).split(","))).get(0);
                String teamPosition = lineInfo.get(4);
                int rating = Integer.parseInt(lineInfo.get(5));
                
                ArrayList<Integer> attributes = new ArrayList<>();

                if(!specificPosition.equals("GK")){
                    for(int i = 6; i < lineInfo.size(); i++){
                        attributes.add((int) Double.parseDouble(lineInfo.get(i)));
                    }
                }

                Team playerTeam = findTeam(teamName, allTeams);
                if(!playerTeam.getName().equals("NULL")){
                    Player newPlayer = new Player(id++, name, rating, specificPosition, teamPosition, playerTeam, attributes);
                    playerTeam.addPlayer(newPlayer);
                    allPlayers.add(newPlayer);
                }

                
            
                line = reader.readLine();
            }

            reader.close();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }

        return allPlayers;
    }

    public static void writeSeason(int season){
        try{
            FileWriter writer = new FileWriter("Backend/FootballAPI/season.txt");
            writer.write(String.valueOf(season) + "\n");
            writer.close();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
        
    }

}
