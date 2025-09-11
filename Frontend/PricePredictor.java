package Frontend;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;

public class PricePredictor {
    private final static String GAME_ENGINE_PATH = "Backend/GameEngine.exe";
    private final static int NUM_SIMULATIONS = 10;
    private final static int NUM_GAMES_PER_ROUND = 38;
    private final static int PROGRESS_BAR_WIDTH = 100;

    private static Map<String, Double> maxPricePerPosition = Map.of(
        "GK", 5.5,
        "DEF", 6.0,
        "MID", 14.0,
        "ATT", 14.0
    );

    private static Map<String, Double> positionalMultipliers = Map.of(
        "GK", 1.25,
        "DEF", 1.5,
        "MID", 2.5,
        "ATT", 3.0
    );

    public static ArrayList<Player> getPlayerPrices(FixtureList fixtureList, ArrayList<Player> players){
        int numFailures = 0;

        for(int i = 0; i < NUM_SIMULATIONS; i++){
            for(int gw = 0; gw < NUM_GAMES_PER_ROUND; gw++){
                ArrayList<Fixture> thisWeekFixtures = fixtureList.getFixtures(gw);
                outputSimulationProgress(i, gw);
                for(Fixture f : thisWeekFixtures){
                    simulateFixture(f, players, gw);
                }
            }
            for(Player p : players) p.lightReset();
        }

        //then change all prices
        for(Player p : players){
            //System.out.println(p.getName() + ": " + String.valueOf(p.getWeeklyPoints()) + "pts");
            double price = (double)p.getWeeklyPoints() / (double)(NUM_SIMULATIONS * NUM_GAMES_PER_ROUND);
            price *= positionalMultipliers.get(p.getGeneralPosition());
            price = Math.min(Math.max(4.0, price), maxPricePerPosition.get(p.getGeneralPosition()));

            price = (Math.round(price) * 2);
            price /= 2.0;
            if(p.getWeeklyPoints() > 0) price += 0.5;
            p.setPrice(price);
        }

        //System.out.println("Num failures: " + String.valueOf(numFailures));

        return players;
    }

    private static void simulateFixture(Fixture fixture, ArrayList<Player> players, int gameWeek)
    {
        try{
            //write fixture details to a txt file
            IOHandler.writeFixtureData(fixture, new Random().nextInt());

            //GET THE MATCH SIMULATOR .EXE FILE PATH
            File exeFile = new File(GAME_ENGINE_PATH).getCanonicalFile();
            
            if (!exeFile.exists()) {
                System.err.println("ERROR: GameEngine.exe not found!");
                return;
            }
            
            //CREATE A NEW PROCESSBUILDER TO CALL THE MATCH SIMULATOR
            ProcessBuilder pb = new ProcessBuilder(exeFile.getAbsolutePath());
            pb.redirectErrorStream(true);
            pb.directory(new File("Backend"));

            //START THE MATCH SIMULATION
            Process process = pb.start();
            drainStream(process.getInputStream());
            int exitCode = process.waitFor();

            //read the result of the fixture if exit code is 0
            if(exitCode == 0){
                //READ THE FIXTURE OUTCOME
                fixture.playFixture(IOHandler.readFixtureOutcome(players));

                //ONCE a fixture is complete, allocate points to every player that played!
                fixture.allocatePointsAndChangeStats(gameWeek);
            }
            else{
                System.out.println("Failed");
                System.out.println(exitCode);
                
            }
        }
        catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
            e.printStackTrace();
        } 
        catch (InterruptedException e) {
            System.err.println("Process was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } 
        catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
        return;
    }



    private static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void outputSimulationProgress(int round, int gw){
        clearScreen();

        int targetSimulations = NUM_SIMULATIONS * NUM_GAMES_PER_ROUND;
        int numGamesSimulated = (round * NUM_GAMES_PER_ROUND) + gw;
        int progress = (int)((double)((double)numGamesSimulated / (double)targetSimulations) * PROGRESS_BAR_WIDTH);

        String outputString = "Simulation progress: " + String.valueOf(progress) + "%";

        System.out.println(outputString);
        System.out.printf("[");
        for(int i = 0; i < PROGRESS_BAR_WIDTH; i++){
            if(i <= progress) System.out.printf("#");
            else System.out.printf("=");
        }
        System.out.printf("]");
        System.out.println();
    }

    private static void drainStream(InputStream inputStream) {
    new Thread(() -> {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while (br.readLine() != null) {
                // discard or optionally log
            }
        } catch (IOException ignored) {}
    }).start();
}

}
