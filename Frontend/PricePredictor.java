package Frontend;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;

public class PricePredictor {
    private final static String GAME_ENGINE_PATH = "Backend/GameEngine.exe";
    private final static int NUM_SIMULATIONS = 1;
    private final static int NUM_GAMES_PER_ROUND = 38;
    private final static int PROGRESS_BAR_WIDTH = 100;

    private static Map<String, Double> maxPricePerPosition = Map.of(
        "GK", 6.0,
        "DEF", 7.0,
        "MID", 12.5,
        "ATT", 12.0
    );

    private static Map<String, Double> quantityMultiPerPosition = Map.of(
        "GK", 3.0,
        "DEF", 2.5,
        "MID", 1.5,
        "ATT", 1.25
    );

    public static ArrayList<Player> getPlayerPrices(FixtureList fixtureList, ArrayList<Player> players){
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
        players = bubbleSortPlayersWeeklyPoints(players);
        players = setPlayerPrices(players);

        return players;
    }


    private static ArrayList<Player> bubbleSortPlayersWeeklyPoints(ArrayList<Player> players){
        boolean swaps = true;
        while(swaps){
            swaps = false;
            int n = players.size();
            for(int i = 1; i < n; i++){
                if(players.get(i).getWeeklyPoints() > players.get(i - 1).getWeeklyPoints()){
                    //swap the players
                    Player p1 = players.get(i);
                    Player p2 = players.get(i - 1);

                    players.set(i, p2);
                    players.set(i - 1, p1);
                    
                    swaps = true;
                }
            }
            n--;
        }

        return players;
    }

    private static ArrayList<Player> setPlayerPrices(ArrayList<Player> players){
        double minPrice = 4.0;
        int maxPositionQuantity = 50;
        double normalPriceDecrease = 0.5;
        double largePriceDecrease = 1.0;
        double priceDecreaseChangeThreshold = 8.0;

        //what we want here
        //we have a map of the maximum price for each position
        //then for every 0.5m price we go down, lowest being 4.0m, double the number of players at the next price point
        String[] positions = {"GK", "DEF", "MID", "ATT"};
        Map<String, Double> positionPrices = new HashMap<>();
        Map<String, Double[]> positionQuantities = new HashMap<>();
        //index 0 = max quantity, index 1 = quantity to add (max being 50)
        for(String pos : positions){
            positionPrices.put(pos, maxPricePerPosition.get(pos));
            positionQuantities.put(pos, new Double[]{1.0, 0.0});
        }
        
        for(Player p : players){
            String position = p.getGeneralPosition();
            double price = positionPrices.get(position);
            Double maxQuantity = positionQuantities.get(position)[0];
            Double currentQuantity = positionQuantities.get(position)[1];

            //if we've reached the correct number of prices in this position
            if(currentQuantity >= maxQuantity){
                currentQuantity = 0.0;
                double quantityMulti = quantityMultiPerPosition.get(position);
                maxQuantity = Math.min(maxQuantity * quantityMulti, maxPositionQuantity);

                double priceChange = normalPriceDecrease;
                if(price > priceDecreaseChangeThreshold) priceChange = largePriceDecrease;

                price = Math.max(price - priceChange, minPrice);
                positionPrices.put(position, price);
            }

            currentQuantity++;
            p.setPrice(price);

            positionQuantities.put(position, new Double[]{maxQuantity, currentQuantity});
        }

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
