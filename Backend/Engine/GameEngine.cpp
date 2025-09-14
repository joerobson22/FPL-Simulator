#include <iostream>

#include "Constants.h"
#include "Player.h"
#include "Team.h"
#include "FileIO.h"
#include "MatchSim.h"

using namespace std;


//main function:
//1. reads fixture data .txt file
//2. creates player and team objects
//3. simulates the match
//4. writes outcome to output file

int main() {
    cout << "GameEngine.exe running\n";

    Team teams[2];
    //read the 'fixtureData.txt' file and create 2 teams with correct players
    readInputFile(teams);

    //simulate the match
    setupMaps();

    //srand(time(NULL)); << testing

    int errorCode = simulateMatch(teams);
    if(errorCode != 0){
        cout << "Match simulation failed with error" + to_string(errorCode) + ".\n";
        return -1;
    }

    //write the match details to the 'fixtureOutcome.txt' file
    writeToOutputFile(teams);

    return 0;
}
