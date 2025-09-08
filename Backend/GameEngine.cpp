#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <optional>
#include <unordered_map>
#include <map>
#include <cstdlib>
#include <ctime>

using namespace std;

const string inputFilePath = "../fixtureData.txt";
const string outputFilePath = "../fixtureOutcome.txt";

const int SEED = 0;
const int HOME_TEAM = 1;
const int AWAY_TEAM = 2;
const string HOME_PLAYERS_START = "HOME PLAYERS";
const string AWAY_PLAYERS_START = "AWAY PLAYERS";

int seed;

bool validCleanSheetPosition(string position){
    return position == "GK" || position == "DEF" || position == "MID";
}

int generateRandom(int min, int max) 
{
   return min + rand() % (max - min);
}


//PLAYER CLASS
class Player{
    private:
    int id;
    int rating;
    string specificPosition;
    string generalPosition;
    string name;
    int goals;
    int assists;
    int minsPlayed;
    bool sub;

    public:
    Player(vector<pair<string, string>> pairs){
        for(int i = 0; i < pairs.size(); i++){
            pair<string, string> p = pairs[i];
            setAttribute(p);
        }

        goals = 0;
        assists = 0;
        minsPlayed = 90;
        sub = false;
    }

    /*
    Player(int id, int rating, string position, string name){
        this->id = id;
        this->rating = rating;
        this->position = position;
        this->name = name;

        goals = 0;
        assists = 0;
    }
        */

    void setAttribute(pair<string, string> p){
        if(p.first == "id"){
            id = stoi(p.second);
        }
        else if(p.first == "rating"){
            rating = stoi(p.second);
        }
        else if(p.first == "specificPosition"){
            specificPosition = p.second;
        }
        else if(p.first == "generalPosition"){
            generalPosition = p.second;
        }
        else if(p.first == "name"){
            name = p.second;
        }
    }

    void scored(){ 
        goals++;
        cout << name + " scored!\n";
    }
    void assisted(){ 
        assists++;
        cout << name + " assisted!\n\n";
    }
    void playMinute(){
        if(!sub) minsPlayed++;
    }

    int getID(){ return id; }
    int getRating(){ return rating; }
    string getSpecificPosition(){ return specificPosition; }
    string getGeneralPosition(){ return generalPosition; }
    string getName(){ return name; }
    int getGoals(){ return goals; }
    int getAssists(){ return assists; }
    int getMinsPlayed(){ return minsPlayed; }

    void outputPlayer(){
        cout << "name: " + name + "\n";
        cout << "rating: " + to_string(rating);
        cout << "\nposition: " + specificPosition + "\n";
    }
};

//TEAM CLASS
class Team{
    private:
    string name;
    int goals;
    vector<Player> players;
    map<string, vector<Player>> team;
    
    public:
    Team(){
        this->name = "";
        goals = 0;
    }

    Team(string name){
        this->name = name;
        goals = 0;
    }

    void scored(Player& scorer, Player& assister){
        goals++;

        for(int i = 0; i < players.size(); i++){
            if(scorer.getID() == players[i].getID()){
                players[i].scored();
            }
        }
        for(int i = 0; i < players.size(); i++){
            if(assister.getID() == players[i].getID()){
                players[i].assisted();
            }
        }
    }
    void addPlayer(Player& player){ 
        players.push_back(player);
        team[player.getGeneralPosition()].push_back(player);
    }

    vector<Player>& getPlayers(){ return players; }

    Player* getPlayerFromPosition(string position){
        vector<Player*> positionPlayers;
        for(auto& p : players){
            if(p.getSpecificPosition() == position){
                positionPlayers.push_back(&p);
            }
        }
        if(positionPlayers.size() == 0) return nullptr;

        return positionPlayers[generateRandom(0, positionPlayers.size())];
    }

    string getGoalScorersDictionary(){
        string output = "";
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < players[i].getGoals(); j++){
                if(output != "") output += ",";
                output += to_string(players[i].getID());
            }
        }
        return output;
    }
    string getAssistersDictionary(){
        string output = "";
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < players[i].getAssists(); j++){
                if(output != "") output += ",";
                output += to_string(players[i].getID());
            }
        }
        return output;
    }
    string getCleanSheetDictionary(bool cleanSheet){
        string output = "";
        if(cleanSheet){
            cout << "CLEAN SHEET!\n";
            for(int i = 0; i < players.size(); i++){
                if(!validCleanSheetPosition(players[i].getGeneralPosition())) continue;
                if(output != "") output += ",";

                output += to_string(players[i].getID());
            }
        }
        return output;
    }
    string get60MinDictionary(){
        string output = "";
        for(int i = 0; i < players.size(); i++){
            if(players[i].getMinsPlayed() >= 60){
                if(output != "") output += ",";
                output += to_string(players[i].getID());
            }
        }
        return output;
    }
    int getGoals(){ return goals; }

    //temporary testing functions
    void outputTeam(){
        cout << name + "\n\n";
        for(int i = 0; i < players.size(); i++){
            players[i].outputPlayer();
            cout << "\n";
        }
    }
    Player& getRandomPlayer(){
        return players[rand() % players.size()];
    }
};

//FILE INPUT FUNCTIONS
pair<string, string> parseKeyValuePair(string section){
    string key = "";
    string value = "";

    size_t colonPos = section.find(':');

    if(colonPos != string::npos){
        key = section.substr(0, colonPos);
        value = section.substr(colonPos + 1);
    }

    return make_pair(key, value);
}

void readInputFile(Team teams[]){
    string line;
    ifstream reader(inputFilePath);

    int teamPlayers = -1;

    int i = -1;
    while(getline(reader, line)){
        i++;
        //different specific cases
        if(i == SEED){
            seed = stoi(line);
            cout << "\n\n-----------SEED----------\n" + line + "\n\n";
            srand(static_cast<unsigned int>(seed));
            continue;
        }
        else if(i == HOME_TEAM){
            teams[0] = Team(line);
            continue;
        }
        else if(i == AWAY_TEAM){
            teams[1] = Team(line);
            continue;
        }
        else if(line == HOME_PLAYERS_START) {
            teamPlayers = 0;
            continue;
        }
        else if(line == AWAY_PLAYERS_START){
            teamPlayers = 1;
            continue;
        } 

        //otherwise the line is a new player's information
        //so find every comma, then pass that substring into parseKeyValuePair
        vector<pair<string, string>> keyValuePairs;
        
        int subStrStart = 0;
        int j = 0;
        for(j = 0; j < line.size(); j++){
            char c = line.at(j);

            if(c == ',' || j == line.size()){
                keyValuePairs.push_back(parseKeyValuePair(line.substr(subStrStart, j - subStrStart)));
                subStrStart = j + 1;
            }
        }
        keyValuePairs.push_back(parseKeyValuePair(line.substr(subStrStart, j - subStrStart)));

        Player p(keyValuePairs);
        teams[teamPlayers].addPlayer(p);
    }
}
//---------------------------------

//FILE OUTPUT FUNCTIONS
string getGoalScorersString(Team teams[]){
    string output = "";
    output += teams[0].getGoalScorersDictionary();

    if(output != "" && teams[1].getGoalScorersDictionary() != "") output += ",";
    output += teams[1].getGoalScorersDictionary();

    return output;
}

string getAssistersString(Team teams[]){
    string output = "";
    output += teams[0].getAssistersDictionary();

    if(output != "" && teams[1].getAssistersDictionary() != "") output += ",";
    output += teams[1].getAssistersDictionary();

    return output;
}

string getCleanSheetsString(Team teams[]){
    string output = "";
    output += teams[0].getCleanSheetDictionary(teams[1].getGoals() == 0);

    if(output != "" && teams[1].getCleanSheetDictionary(teams[0].getGoals() == 0) != "") output += ",";
    output += teams[1].getCleanSheetDictionary(teams[0].getGoals() == 0);

    return output;
}

string get60MinString(Team teams[]){
    string output = "";
    output += teams[0].get60MinDictionary();

    if(output != "" && teams[1].get60MinDictionary() != "") output += ",";
    output += teams[1].get60MinDictionary();

    return output;
}

void writeToOutputFile(Team teams[]){
    ofstream output(outputFilePath);

    output << to_string(teams[0].getGoals()) + "\n";
    output << to_string(teams[1].getGoals()) + "\n";
    output << "GOAL SCORERS," + getGoalScorersString(teams) + "\n";
    output << "ASSISTERS," + getAssistersString(teams) + "\n";
    output << "CLEAN SHEETS," + getCleanSheetsString(teams) + "\n";
    output << "60 MINS," + get60MinString(teams) + "\n";

    output.close();
}
//----------------------------------




//MATCH ENGINE!!!

const int NUM_STEPS = 1000;
const int GK_SHOOTING = 10;
const int GK_DRIBBLING = 30;

//FINAL POSITION MAP:
//gk
//cb, lb, cb, lwb, rwb
//cdm, cm, cam, lw, rw
//st
const int numPositions = 12;
const std::string allPositions[numPositions] = {
    "GK", "CB", "LB", "RB", "LWB", "RWB", 
    "CDM", "CM", "CAM", "LW", "RW", "ST"
};

//decision weightings for every position
const int GK_PASS_WEIGHT = 100;
const int GK_DRIBBLE_WEIGHT = 2;
const int GK_SHOOT_WEIGHT = 1;

const int CB_PASS_WEIGHT = 70;
const int CB_DRIBBLE_WEIGHT = 10;
const int CB_SHOOT_WEIGHT = 2;

//fb covers lb and rb
const int FB_PASS_WEIGHT = 60;
const int FB_DRIBBLE_WEIGHT = 15;
const int FB_SHOOT_WEIGHT = 4;

//wb covers lwb and rwb
const int WB_PASS_WEIGHT = 50;
const int WB_DRIBBLE_WEIGHT = 15;
const int WB_SHOOT_WEIGHT = 8;

const int CDM_PASS_WEIGHT = 60;
const int CDM_DRIBBLE_WEIGHT = 30;
const int CDM_SHOOT_WEIGHT = 15;

const int CM_PASS_WEIGHT = 80;
const int CM_DRIBBLE_WEIGHT = 50;
const int CM_SHOOT_WEIGHT = 20;

const int CAM_PASS_WEIGHT = 70;
const int CAM_DRIBBLE_WEIGHT = 50;
const int CAM_SHOOT_WEIGHT = 30;

//w covers lw and rw
const int W_PASS_WEIGHT = 50;
const int W_DRIBBLE_WEIGHT = 50;
const int W_SHOOT_WEIGHT = 40;

const int ST_PASS_WEIGHT = 30;
const int ST_DRIBBLE_WEIGHT = 40;
const int ST_SHOOT_WEIGHT = 50;


//const double FORWARD_PASS_MODIFIER = 0.2;
//const double BACKWARD_PASS_MODIFIER = -0.2;

std::unordered_map<std::string, std::unordered_map<string, int>> decisionMap;
std::unordered_map<std::string, std::unordered_map<string, int>> passMap;

string getRandomPosition(Team team, std::unordered_map<std::string, int> map){
    //add up all weights in the given map
    int total = 0;
    for(int i = 0; i < numPositions; i++){
        total += map[allPositions[i]];
    }
    //now generate a number and go back through the map until we know which position the number applies to
    int number = generateRandom(0, total);
    total = 0;
    for(int i = 0; i < numPositions; i++){
        total += map[allPositions[i]];
        if(total > number) return allPositions[i];
    }
    //otherwise return the last position
    return allPositions[numPositions - 1];
}

void setupDecisionMap(){
    //setup every position's unordered maps
    std::unordered_map<string, int> gkDecisions;
    gkDecisions["PASS"] = GK_PASS_WEIGHT;
    gkDecisions["DRIBBLE"] = GK_DRIBBLE_WEIGHT;
    gkDecisions["SHOOT"] = GK_SHOOT_WEIGHT;

    std::unordered_map<string, int> cbDecisions;
    cbDecisions["PASS"] = CB_PASS_WEIGHT;
    cbDecisions["DRIBBLE"] = CB_DRIBBLE_WEIGHT;
    cbDecisions["SHOOT"] = CB_SHOOT_WEIGHT;

    std::unordered_map<string, int> fbDecisions;
    fbDecisions["PASS"] = FB_PASS_WEIGHT;
    fbDecisions["DRIBBLE"] = FB_DRIBBLE_WEIGHT;
    fbDecisions["SHOOT"] = FB_SHOOT_WEIGHT;

    std::unordered_map<string, int> wbDecisions;
    wbDecisions["PASS"] = WB_PASS_WEIGHT;
    wbDecisions["DRIBBLE"] = WB_DRIBBLE_WEIGHT;
    wbDecisions["SHOOT"] = WB_SHOOT_WEIGHT;

    std::unordered_map<string, int> cdmDecisions;
    cdmDecisions["PASS"] = CDM_PASS_WEIGHT;
    cdmDecisions["DRIBBLE"] = CDM_DRIBBLE_WEIGHT;
    cdmDecisions["SHOOT"] = CDM_SHOOT_WEIGHT;

    std::unordered_map<string, int> cmDecisions;
    cmDecisions["PASS"] = CM_PASS_WEIGHT;
    cmDecisions["DRIBBLE"] = CM_DRIBBLE_WEIGHT;
    cmDecisions["SHOOT"] = CM_SHOOT_WEIGHT;

    std::unordered_map<string, int> camDecisions;
    camDecisions["PASS"] = CAM_PASS_WEIGHT;
    camDecisions["DRIBBLE"] = CAM_DRIBBLE_WEIGHT;
    camDecisions["SHOOT"] = CAM_SHOOT_WEIGHT;

    std::unordered_map<string, int> wDecisions;
    wDecisions["PASS"] = W_PASS_WEIGHT;
    wDecisions["DRIBBLE"] = W_DRIBBLE_WEIGHT;
    wDecisions["SHOOT"] = W_SHOOT_WEIGHT;

    std::unordered_map<string, int> stDecisions;
    stDecisions["PASS"] = ST_PASS_WEIGHT;
    stDecisions["DRIBBLE"] = ST_DRIBBLE_WEIGHT;
    stDecisions["SHOOT"] = ST_SHOOT_WEIGHT;

    //then add every position to the overall decision map
    decisionMap["GK"] = gkDecisions;
    decisionMap["CB"] = cbDecisions;
    decisionMap["LB"] = fbDecisions;
    decisionMap["RB"] = fbDecisions;
    decisionMap["LWB"] = wbDecisions;
    decisionMap["RWB"] = wbDecisions;
    decisionMap["CDM"] = cdmDecisions;
    decisionMap["CM"] = cmDecisions;
    decisionMap["CAM"] = camDecisions;
    decisionMap["LW"] = wDecisions;
    decisionMap["RW"] = wDecisions;
    decisionMap["ST"] = stDecisions;

}

void setupPassMap(){
    /*
    std::unordered_map<std::string, double> selfPassWeights;
    selfPassWeights["GK"] = 0;
    selfPassWeights["CB"] = 0.75;
    selfPassWeights["LB"] = 0;
    selfPassWeights["RB"] = 0;
    selfPassWeights["LWB"] = 0;
    selfPassWeights["RWB"] = 0;
    selfPassWeights["CDM"] = 0.75;
    selfPassWeights["CM"] = 0.75;
    selfPassWeights["CAM"] = 0.75;
    selfPassWeights["LW"] = 0;
    selfPassWeights["RW"] = 0;
    selfPassWeights["ST"] = 0.5;

    //setup something to do with distance between positions in the 'positions' array?
    //could even get team tactics from fifa database and use different functions to make teams play differently

    int maxDistance = numPositions * numPositions;

    for(int i = 0; i < numPositions; i++){
        std::unordered_map<string, int> passes;
        for(int j = 0; j < numPositions; j++){
            int distance = j - i;
            
            //weight = max distance - (max distance / distance)
            int weight = maxDistance - (distance * distance);
            //change weighting depending on how far up the pitch the current position (i) is

            //change weighting depending on if the pass goes forward or backward
            cout << to_string(weight) + "\n";
            if(distance > 0) weight += (double)weight * FORWARD_PASS_MODIFIER;
            else weight += (double)weight * BACKWARD_PASS_MODIFIER;
            cout << to_string(weight) + "\n\n";

            if(i == j) weight = (double)weight * selfPassWeights[allPositions[i]];

            passes[allPositions[j]] = weight;
        }
        passMap[allPositions[i]] = passes;
    }*/

    //manually create a graph of all chances of passing to another position

    std::unordered_map<string, int> gkPasses;
    gkPasses["GK"] = 0;
    gkPasses["CB"] = 100;
    gkPasses["LB"] = 50;
    gkPasses["RB"] = 50;
    gkPasses["LWB"] = 20;
    gkPasses["RWB"] = 20;
    gkPasses["CDM"] = 25;
    gkPasses["CM"] = 10;
    gkPasses["CAM"] = 5;
    gkPasses["LW"] = 2;
    gkPasses["RW"] = 2;
    gkPasses["ST"] = 1;

    std::unordered_map<string, int> cbPasses;
    cbPasses["GK"] = 50;
    cbPasses["CB"] = 75;
    cbPasses["LB"] = 25;
    cbPasses["RB"] = 25;
    cbPasses["LWB"] = 20;
    cbPasses["RWB"] = 20;
    cbPasses["CDM"] = 30;
    cbPasses["CM"] = 10;
    cbPasses["CAM"] = 5;
    cbPasses["LW"] = 2;
    cbPasses["RW"] = 2;
    cbPasses["ST"] = 1;

    std::unordered_map<string, int> lbPasses;
    lbPasses["GK"] = 10;
    lbPasses["CB"] = 30;
    lbPasses["LB"] = 0;
    lbPasses["RB"] = 5;
    lbPasses["LWB"] = 10;
    lbPasses["RWB"] = 3;
    lbPasses["CDM"] = 20;
    lbPasses["CM"] = 10;
    lbPasses["CAM"] = 5;
    lbPasses["LW"] = 40;
    lbPasses["RW"] = 1;
    lbPasses["ST"] = 3;
    
    std::unordered_map<string, int> rbPasses;
    rbPasses["GK"] = 10;
    rbPasses["CB"] = 30;
    rbPasses["LB"] = 5;
    rbPasses["RB"] = 0;
    rbPasses["LWB"] = 3;
    rbPasses["RWB"] = 10;
    rbPasses["CDM"] = 20;
    rbPasses["CM"] = 10;
    rbPasses["CAM"] = 5;
    rbPasses["LW"] = 1;
    rbPasses["RW"] = 40;
    rbPasses["ST"] = 3;

    std::unordered_map<string, int> lwbPasses;
    lwbPasses["GK"] = 5;
    lwbPasses["CB"] = 10;
    lwbPasses["LB"] = 5;
    lwbPasses["RB"] = 1;
    lwbPasses["LWB"] = 0;
    lwbPasses["RWB"] = 2;
    lwbPasses["CDM"] = 10;
    lwbPasses["CM"] = 20;
    lwbPasses["CAM"] = 22;
    lwbPasses["LW"] = 50;
    lwbPasses["RW"] = 1;
    lwbPasses["ST"] = 5;

    std::unordered_map<string, int> rwbPasses;
    rwbPasses["GK"] = 5;
    rwbPasses["CB"] = 10;
    rwbPasses["LB"] = 1;
    rwbPasses["RB"] = 5;
    rwbPasses["LWB"] = 2;
    rwbPasses["RWB"] = 0;
    rwbPasses["CDM"] = 10;
    rwbPasses["CM"] = 20;
    rwbPasses["CAM"] = 22;
    rwbPasses["LW"] = 1;
    rwbPasses["RW"] = 50;
    rwbPasses["ST"] = 5;

    std::unordered_map<string, int> cdmPasses;
    cdmPasses["GK"] = 2;
    cdmPasses["CB"] = 20;
    cdmPasses["LB"] = 5;
    cdmPasses["RB"] = 5;
    cdmPasses["LWB"] = 6;
    cdmPasses["RWB"] = 6;
    cdmPasses["CDM"] = 30;
    cdmPasses["CM"] = 40;
    cdmPasses["CAM"] = 35;
    cdmPasses["LW"] = 20;
    cdmPasses["RW"] = 20;
    cdmPasses["ST"] = 10;

    std::unordered_map<string, int> cmPasses;
    cmPasses["GK"] = 0;
    cmPasses["CB"] = 12;
    cmPasses["LB"] = 3;
    cmPasses["RB"] = 3;
    cmPasses["LWB"] = 5;
    cmPasses["RWB"] = 5;
    cmPasses["CDM"] = 25;
    cmPasses["CM"] = 50;
    cmPasses["CAM"] = 50;
    cmPasses["LW"] = 30;
    cmPasses["RW"] = 30;
    cmPasses["ST"] = 20;

    std::unordered_map<string, int> camPasses;
    camPasses["GK"] = 0;
    camPasses["CB"] = 1;
    camPasses["LB"] = 2;
    camPasses["RB"] = 2;
    camPasses["LWB"] = 4;
    camPasses["RWB"] = 4;
    camPasses["CDM"] = 15;
    camPasses["CM"] = 40;
    camPasses["CAM"] = 50;
    camPasses["LW"] = 40;
    camPasses["RW"] = 40;
    camPasses["ST"] = 30;

    std::unordered_map<string, int> lwPasses;
    lwPasses["GK"] = 0;
    lwPasses["CB"] = 0;
    lwPasses["LB"] = 10;
    lwPasses["RB"] = 1;
    lwPasses["LWB"] = 15;
    lwPasses["RWB"] = 1;
    lwPasses["CDM"] = 2;
    lwPasses["CM"] = 15;
    lwPasses["CAM"] = 30;
    lwPasses["LW"] = 0;
    lwPasses["RW"] = 5;
    lwPasses["ST"] = 40;

    std::unordered_map<string, int> rwPasses;
    rwPasses["GK"] = 0;
    rwPasses["CB"] = 0;
    rwPasses["LB"] = 1;
    rwPasses["RB"] = 10;
    rwPasses["LWB"] = 1;
    rwPasses["RWB"] = 15;
    rwPasses["CDM"] = 2;
    rwPasses["CM"] = 15;
    rwPasses["CAM"] = 30;
    rwPasses["LW"] = 5;
    rwPasses["RW"] = 0;
    rwPasses["ST"] = 40;

    std::unordered_map<string, int> stPasses;
    stPasses["GK"] = 0;
    stPasses["CB"] = 0;
    stPasses["LB"] = 0;
    stPasses["RB"] = 0;
    stPasses["LWB"] = 0;
    stPasses["RWB"] = 0;
    stPasses["CDM"] = 1;
    stPasses["CM"] = 5;
    stPasses["CAM"] = 50;
    stPasses["LW"] = 30;
    stPasses["RW"] = 30;
    stPasses["ST"] = 40;

    //add all maps to the passMap
    passMap["GK"] = gkPasses;
    passMap["CB"] = cbPasses;
    passMap["LB"] = lbPasses;
    passMap["RB"] = rbPasses;
    passMap["LWB"] = lwbPasses;
    passMap["RWB"] = rwbPasses;
    passMap["CDM"] = cdmPasses;
    passMap["CM"] = cmPasses;
    passMap["CAM"] = camPasses;
    passMap["LW"] = lwPasses;
    passMap["RW"] = rwPasses;
    passMap["ST"] = stPasses;
}

void setupMaps(){
    setupDecisionMap();
    setupPassMap();
}

void simulateStep(Team teams[], Player& onBall, string* position){
    //player decision making:
    //first, use SPECIFIC position to look at the different options: passing, dribbling or shooting.

    //pass:
    //now look at the pass map: for every position, there is a pass map to every other position. choose one at random (if your team contains a player in that position)

    //dribbling:
    //move one position up the position index

    //shooting:
    //shoot at goal, chance of a block or deflection or saving
}

int simulateMatch(Team teams[]){
    /*
    for(int i = 0; i < rand() % 4; i++){
        teams[0].scored(teams[0].getRandomPlayer(), teams[0].getRandomPlayer());
    }

    for(int i = 0; i < rand() % 4; i++){
        teams[1].scored(teams[1].getRandomPlayer(), teams[1].getRandomPlayer());
    }
        */

    //kickoff
    Player& onBall = teams[0].getPlayers()[10];
    std::string position = onBall.getSpecificPosition();

    for(int step = 0; step < NUM_STEPS / 2; step++){
        simulateStep(teams, onBall, &position);
    }
    //half time
    onBall = teams[1].getPlayers()[10];
    for(int step = NUM_STEPS / 2; step < NUM_STEPS; step++){
        simulateStep(teams, onBall, &position);
    }

    return 0;
}




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

    for(int i = 0; i < numPositions; i++){
        string position1 = allPositions[i];
        for(int j = 0; j < numPositions; j++){
            string position2 = allPositions[j];
            cout << position1 + ">>" + position2 + ": " + to_string(passMap[position1][position2]) + "\n";
        }
    }

    int errorCode = simulateMatch(teams);
    if(errorCode != 0){
        cout << "Match simulation failed with error" + to_string(errorCode) + ".\n";
        return -1;
    }

    //write the match details to the 'fixtureOutcome.txt' file
    writeToOutputFile(teams);

    return 0;
}
