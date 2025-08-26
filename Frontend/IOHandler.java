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

    public static void writeFixtureData(Fixture fixture, int seed){
        try{
            FileWriter writer = new FileWriter("fixtureData.txt");

            //seed
            writer.write(String.valueOf(seed) + "\n");

            //home team
            writer.write(fixture.getHomeTeam().getName() + "\n");

            //away team
            writer.write(fixture.getAwayTeam().getName() + "\n");

            //all players
            writer.write("HOME PLAYERS\n");
            for(Player p : fixture.getHomeTeam().getPlayers()){
                writer.write(p.toDictionaryString() + "\n");
            }

            writer.write("AWAY PLAYERS\n");
            for(Player p : fixture.getAwayTeam().getPlayers()){
                writer.write(p.toDictionaryString() + "\n");
            }

            writer.close();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
    }

    public static FixtureOutcome readFixtureOutcome(ArrayList<Player> players){
        System.out.println("Reading fixture outcome");

        int homeGoals = 0;
        int awayGoals = 0;
        ArrayList<String> goalScorers = new ArrayList<>();
        ArrayList<String> assisters = new ArrayList<>();
        ArrayList<String> cleanSheets = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader("fixtureOutcome.txt"));
            int i = 0;
            String line = reader.readLine();

            while (line != null) {
                if(i == HOME_GOALS){
                    System.out.println("Home team goals");
                    homeGoals = Integer.valueOf(line);
                }
                else if(i == AWAY_GOALS){
                    System.out.println("Away team goals");
                    awayGoals = Integer.valueOf(line);
                }
                else if(i == SCORERS){
                    System.out.println("Goal scorers");
                    goalScorers = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == ASSISTERS){
                    System.out.println("Assisters");
                    assisters = new ArrayList<String>(Arrays.asList(line.split(",")));
                }
                else if(i == CLEAN_SHEETS){
                    System.out.println("Clean sheets");
                    cleanSheets = new ArrayList<String>(Arrays.asList(line.split(",")));
                }

                line = reader.readLine();

                i++;
            }

            reader.close();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
        
        System.out.println("Generating FixtureOutcome object");
        FixtureOutcome outcome = new FixtureOutcome(homeGoals, awayGoals, getPlayersFromID(players, goalScorers), getPlayersFromID(players, assisters), getPlayersFromID(players, cleanSheets));
        return outcome;
    }

    private static ArrayList<Player> getPlayersFromID(ArrayList<Player> allPlayers, ArrayList<String> ids){
        ArrayList<Player> players = new ArrayList<>();

        for(String id : ids){
            if(!id.equals("") && !id.equals("GOAL SCORERS") && !id.equals("ASSISTERS") && !id.equals("CLEAN SHEETS")) 
                players.add(allPlayers.get(Integer.parseInt(id)));
        }

        return players;
    }

    public static ArrayList<Team> readAllTeamData(){
        ArrayList<Team> teams = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("allTeams.txt"));
            String line = reader.readLine();

            while(line != null)
            {
                ArrayList<String> lineInfo = new ArrayList<String>(Arrays.asList(line.split(",")));

                String name = lineInfo.get(0);
                String abbrv = lineInfo.get(1);

                teams.add(new Team(0, name, abbrv));
                line = reader.readLine();
            }
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }

        return teams;
    }
}
