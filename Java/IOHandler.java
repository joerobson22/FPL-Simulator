package Java;
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

    public static void writeFixtureData(Fixture fixture){
        try{
            FileWriter writer = new FileWriter("fixtureData.txt");

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

    public static FixtureOutcome readFixtureOutcome(){
        int homeGoals = 0;
        int awayGoals = 0;
        ArrayList<String> goalScorers = new ArrayList<>();
        ArrayList<String> assisters = new ArrayList<>();
        ArrayList<String> cleanSheets = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader("fixtureResult.txt"));
            int i = 0;
            String line = reader.readLine();

            while (line != null) {
                if(i == HOME_GOALS) homeGoals = Integer.valueOf(line);
                else if(i == AWAY_GOALS) awayGoals = Integer.valueOf(line);
                else if(i == SCORERS) goalScorers = new ArrayList<String>(Arrays.asList(line.split(",")));
                else if(i == ASSISTERS) assisters = new ArrayList<String>(Arrays.asList(line.split(",")));
                else if(i == CLEAN_SHEETS) cleanSheets = new ArrayList<String>(Arrays.asList(line.split(",")));

                line = reader.readLine();

                i++;
            }

            reader.close();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
        
        FixtureOutcome outcome = new FixtureOutcome(homeGoals, awayGoals, goalScorers, assisters, cleanSheets);
        return outcome;
    }
}
