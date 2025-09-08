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

    vector<Player> getPlayers(){ return players; }

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
    std::optional<Player> getPlayer(int id){
        for(int i = 0; i < players.size(); i++){
            if(players[i].getID() == id){
                return players[i];
            }
        }
        //check is getPlayer.has_value()
        return std::nullopt;
    }

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
        for(j; j < line.size(); j++){
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

const int NUM_STEPS = 90;
const int GK_SHOOTING = 10;
const int GK_DRIBBLING = 30;

//FINAL POSITION MAP:
//gk
//cb, lb, cb, lwb, rwb
//cdm, cm, cam, lw, rw
//st

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

std::unordered_map<std::string, std::unordered_map<string, int>> decisionMap;
std::unordered_map<std::string, std::unordered_map<string, int>> passMap;

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

}

void setupMaps(){
    setupDecisionMap();
    setupPassMap();
}

void simulateStep(Team teams[], Player& onBall){
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
    Player onBall = teams[0].getPlayers()[10];
    for(int step = 0; step < NUM_STEPS / 2; step++){
        simulateStep(teams, onBall);
    }
    //half time
    onBall = teams[1].getPlayers()[10];
    for(int step = NUM_STEPS / 2; step < NUM_STEPS; step++){
        simulateStep(teams, onBall);
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
    int errorCode = simulateMatch(teams);
    if(errorCode != 0){
        cout << "Match simulation failed with error" + to_string(errorCode) + ".\n";
        return -1;
    }

    //write the match details to the 'fixtureOutcome.txt' file
    writeToOutputFile(teams);

    return 0;
}
