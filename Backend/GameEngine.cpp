#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <optional>
#include <unordered_map>
#include <map>
#include <cstdlib>
#include <ctime>
#include <algorithm>

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

bool validDFConPosition(string position){
    return position == "DEF" || position == "MID";
}

int getDFConForPosition(string position){
    if(!validDFConPosition(position)) return 1000000;

    if(position == "DEF") return 10;
    else return 12;
}

int generateRandom(int min, int maxExclusive)
{
    if (maxExclusive <= min) return min;
    int range = maxExclusive - min;
    return min + (rand() % range);
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
    int penaltyGoals;
    int assists;
    int penaltyMisses;
    int penaltySaves;
    int minsPlayed;
    int dfCon;
    int saves;
    bool sub;
    bool yellowCard;
    bool redCard;

    int pace;
    int shooting;
    int passing;
    int dribbling;
    int defending;
    int physical;

    public:
    Player(vector<pair<string, string>> pairs){
        for(int i = 0; i < pairs.size(); i++){
            pair<string, string> p = pairs[i];
            setAttribute(p);
        }

        goals = 0;
        penaltyGoals = 0;
        assists = 0;
        penaltyMisses = 0;
        penaltySaves = 0;
        minsPlayed = 0;
        dfCon = 0;
        sub = false;
        saves = 0;
        yellowCard = false;
        redCard = false;

        if (generalPosition == "GK") {
            pace = shooting = passing = dribbling = defending = physical = rating;
        }
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
        string key = p.first;
        string value = p.second;
        if(key == "id"){
            id = stoi(value);
            return;
        }
        else if(key == "rating"){
            rating = stoi(value);
            return;
        }
        else if(key == "specificPosition"){
            specificPosition = value;
            return;
        }
        else if(key == "generalPosition"){
            generalPosition = value;
            return;
        }
        else if(key == "name"){
            name = value;
            return;
        }

        if(generalPosition == "GK") value = to_string(rating);

        if(key == "PAC"){
            pace = stoi(value);
        }
        else if(key == "SHO"){
            shooting = stoi(value);
        }
        else if(key == "PAS"){
            passing = stoi(value);
        }
        else if(key == "DRI"){
            dribbling = stoi(value);
        }
        else if(key == "DEF"){
            defending = stoi(value);
        }
        else if(key == "PHY"){
            physical = stoi(value);
        }
    }

    void scored(int penalty){ 
        goals++;
        cout << name + " scored!\n";
        penaltyGoals += penalty;
    }
    void assisted(){ 
        assists++;
        cout << name + " assisted!\n\n";
    }
    void getYellowCard(){
        yellowCard = true;
        cout << "\n\nYELLOW CARD: " + name + "\n\n";
    }
    void getRedCard(){
        yellowCard = false;
        redCard = true;
        cout << "\n\nRED CARD: " + name + "\n\n";
    }
    void missPenalty(){
        penaltyMisses++;
        cout << "\n\nMISSED PENALTY: " + name + "\n\n";
    }
    void savePenalty(){
        penaltySaves++;
        cout << "\n\nSAVED PENALTY: " + name + "\n\n";
    }

    void playMinute(){
        if(!sub) minsPlayed++;
    }

    void makeDFCon(){
        dfCon++;
    }

    void makeSave(){
        saves++;
    }

    int getPace(){ return pace; }
    int getShooting(){ return shooting; }
    int getPassing(){ return passing; }
    int getDribbling(){ return dribbling; }
    int getDefending(){ return defending; }
    int getPhysical(){ return physical; }

    int getID(){ return id; }
    int getRating(){ return rating; }
    string getSpecificPosition(){ return specificPosition; }
    string getGeneralPosition(){ return generalPosition; }
    string getName(){ return name; }
    int getGoals(){ return goals; }
    int getPenaltyGoals(){ return penaltyGoals; }
    int getAssists(){ return assists; }
    int getMinsPlayed(){ return minsPlayed; }
    int getDFCon(){ return dfCon; }
    int getSaves(){ return saves; }
    int getPenaltyMisses(){ return penaltyMisses; }
    int getPenaltySaves(){ return penaltySaves; }
    bool gotYellowCard(){ return yellowCard; }
    bool gotRedCard(){ return redCard; }

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
    vector<Player> playingPlayers;
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

    void scored(Player& scorer, Player* assister, int penalty){
        goals++;

        for(int i = 0; i < playingPlayers.size(); i++){
            if(scorer.getID() == playingPlayers[i].getID()){
                playingPlayers[i].scored(penalty);
            }
        }
        if(assister){
            for(int i = 0; i < playingPlayers.size(); i++){
                if(assister->getID() == playingPlayers[i].getID()){
                    playingPlayers[i].assisted();
                }
            }
        }
    }

    void missedPenalty(Player& misser){
        for(int i = 0; i < playingPlayers.size(); i++){
            if(misser.getID() == playingPlayers[i].getID()){
                playingPlayers[i].missPenalty();
            }
        }
    }

    void getRedCard(int id){
        Player* p = nullptr;
        for(int i = 0; i < playingPlayers.size(); i++){
            if(playingPlayers[i].getID() == id){
                p = &playingPlayers[i];
                p->getRedCard();
                break;
            }
        }

        for(int i = 0; i < players.size(); i++){
            if(p->getID() == id){
                players[i] = *p;
                break;
            }
        }

        playingPlayers.erase(
            std::remove_if(
                playingPlayers.begin(), playingPlayers.end(),
                [&](Player &p) { return p.getID() == id; }
            ),
            playingPlayers.end()
        );
    }

    void getYellowCard(int id){
        for(int i = 0; i < playingPlayers.size(); i++){
            if(playingPlayers[i].getID() == id){
                if(playingPlayers[i].gotYellowCard()) playingPlayers[i].getRedCard();
                else playingPlayers[i].getYellowCard();
            }
        }
    }

    void teamPlayAMinute(){
        for(int i = 0; i < playingPlayers.size(); i++){
            playingPlayers[i].playMinute();
        }
    }

    Player* getPenaltyTaker(){
        int highest = -1;
        Player* taker;
        for(auto &p : playingPlayers){
            if(p.getShooting() > highest && p.getGeneralPosition() != "GK"){
                //cout << to_string(highest) + "\n";
                highest = p.getShooting();
                taker = &p;
            }
        }
        return taker;
    }


    void addPlayer(Player& player){ 
        players.push_back(player);
        playingPlayers.push_back(player);
        team[player.getGeneralPosition()].push_back(player);
    }

    vector<Player>& getPlayers(){ return players; }

    vector<Player>& getPlayingPlayers(){ return playingPlayers; }

    void transferPlayingPlayersToPlayers(){
        for(int i = 0; i < playingPlayers.size(); i++){
            int iid = playingPlayers[i].getID();
            for(int j = 0; j < players.size(); j++){
                int jid = players[j].getID();
                if(jid == iid){
                    players[j] = playingPlayers[i];
                    break;
                }
            }
        }
    }


    Player* getPlayerFromPosition(string position){
        vector<Player*> positionPlayers;
        for(auto& p : playingPlayers){
            if(p.getSpecificPosition() == position){
                positionPlayers.push_back(&p);
            }
        }
        if(positionPlayers.size() == 0) return nullptr;

        return positionPlayers[generateRandom(0, positionPlayers.size())];
    }

    vector<Player*> getPlayersFromGeneralPosition(string generalPos){
        vector<Player*> positionPlayers;
        for(auto& p : players){
            if(p.getGeneralPosition() == generalPos){
                positionPlayers.push_back(&p);
            }
        }
        return positionPlayers;
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
    string get3SavesDictionary(){
        string output = "";
        for(int j = 0; j < players[0].getSaves() % 3; j++){
            if(output != "") output += ",";
            output += to_string(players[0].getID());
        }
        return output;
    }
    string getDFConDictionary(){
        string output = "";
        for(int i = 0; i < players.size(); i++){
            if(players[i].getDFCon() >= getDFConForPosition(players[i].getGeneralPosition())){
                if(output != "") output += ",";
                output += to_string(players[i].getID());
            }
        }
        return output;
    }
    string getPenaltyGoalScorersDictionary(){
        string output = "";
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < players[i].getPenaltyGoals(); j++){
                if(output != "") output += ",";
                output += to_string(players[i].getID());
            }
        }
        return output;
    }
    string getPenaltyMissesDictionary(){
        string output = "";
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < players[i].getPenaltyMisses(); j++){
                if(output != "") output += ",";
                output += to_string(players[i].getID());
            }
        }
        return output;
    }
    string getPenaltySavesDictionary(){
        string output = "";
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < players[i].getPenaltySaves(); j++){
                if(output != "") output += ",";
                output += to_string(players[i].getID());
            }
        }
        return output;
    }
    string getYellowCardDictionary(){
        string output = "";
        for(int i = 0; i < players.size(); i++){
            if(players[i].gotYellowCard()){
                if(output != "") output += ",";
                output += to_string(players[i].getID());
            }
        }
        return output;
    }
    string getRedCardDictionary(){
        string output = "";
        for(int i = 0; i < players.size(); i++){
            if(players[i].gotRedCard()){
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
    Player* getRandomPlayer(){
        return &playingPlayers[rand() % playingPlayers.size()];
    }
};

int getTeamIndex(Team teams[], Player p){
    for(int i = 0; i < 2; i++){
        for(int j = 0; j < 11; j++){
            if(p.getID() == teams[i].getPlayers()[j].getID()) return i;
        }
    }
    return -1;
}

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

string get3SavesString(Team teams[]){
    string output = "";
    output += teams[0].get3SavesDictionary();

    if(output != "" && teams[1].get3SavesDictionary() != "") output += ",";
    output += teams[1].get3SavesDictionary();

    return output;
}

string getDFConString(Team teams[]){
    string output = "";
    output += teams[0].getDFConDictionary();

    if(output != "" && teams[1].getDFConDictionary() != "") output += ",";
    output += teams[1].getDFConDictionary();

    return output;
}

string getYellowCardString(Team teams[]){
    string output = "";
    output += teams[0].getYellowCardDictionary();

    if(output != "" && teams[1].getYellowCardDictionary() != "") output += ",";
    output += teams[1].getYellowCardDictionary();

    return output;
}

string getRedCardString(Team teams[]){
    string output = "";
    output += teams[0].getRedCardDictionary();

    if(output != "" && teams[1].getRedCardDictionary() != "") output += ",";
    output += teams[1].getRedCardDictionary();

    return output;
}

string getPenaltyScorersString(Team teams[]){
    string output = "";
    output += teams[0].getPenaltyGoalScorersDictionary();

    if(output != "" && teams[1].getPenaltyGoalScorersDictionary() != "") output += ",";
    output += teams[1].getPenaltyGoalScorersDictionary();

    return output;
}

string getPenaltyMissesString(Team teams[]){
    string output = "";
    output += teams[0].getPenaltyMissesDictionary();

    if(output != "" && teams[1].getPenaltyMissesDictionary() != "") output += ",";
    output += teams[1].getPenaltyMissesDictionary();

    return output;
}

string getPenaltySavesString(Team teams[]){
    string output = "";
    output += teams[0].getPenaltySavesDictionary();

    if(output != "" && teams[1].getPenaltySavesDictionary() != "") output += ",";
    output += teams[1].getPenaltySavesDictionary();

    return output;
}

void writeToOutputFile(Team teams[]){
    teams[0].transferPlayingPlayersToPlayers();
    teams[1].transferPlayingPlayersToPlayers();

    ofstream output(outputFilePath);

    output << to_string(teams[0].getGoals()) + "\n";
    output << to_string(teams[1].getGoals()) + "\n";
    output << "GOAL SCORERS," + getGoalScorersString(teams) + "\n";
    output << "ASSISTERS," + getAssistersString(teams) + "\n";
    output << "CLEAN SHEETS," + getCleanSheetsString(teams) + "\n";
    output << "60 MINS," + get60MinString(teams) + "\n";
    output << "3 SAVES," + get3SavesString(teams) + "\n";
    output << "DFCON," + getDFConString(teams) + "\n";
    output << "YELLOW CARDS," + getYellowCardString(teams) + "\n";
    output << "RED CARDS," + getRedCardString(teams) + "\n";
    output << "PENALTY GOALS," + getPenaltyScorersString(teams) + "\n";
    output << "PENALTY MISSES," + getPenaltyMissesString(teams) + "\n";
    output << "PENALTY SAVES," + getPenaltySavesString(teams) + "\n";

    output.close();
}
//----------------------------------




//MATCH ENGINE!!!

const int PARTS = 2;
const int MINUTES_PER_PART = 46;
const int STEPS_PER_MINUTE = 5;

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
const int numChoices = 3;
const std::string allChoices[numChoices] = {
    "PASS",
    "SHOOT",
    "DRIBBLE"
};

//decision weightings for every position
const int GK_PASS_WEIGHT = 200;
const int GK_DRIBBLE_WEIGHT = 2;
const int GK_SHOOT_WEIGHT = 1;

const int CB_PASS_WEIGHT = 140;
const int CB_DRIBBLE_WEIGHT = 20;
const int CB_SHOOT_WEIGHT = 2;

//fb covers lb and rb
const int FB_PASS_WEIGHT = 80;
const int FB_DRIBBLE_WEIGHT = 25;
const int FB_SHOOT_WEIGHT = 4;

//wb covers lwb and rwb
const int WB_PASS_WEIGHT = 60;
const int WB_DRIBBLE_WEIGHT = 30;
const int WB_SHOOT_WEIGHT = 5;

const int CDM_PASS_WEIGHT = 80;
const int CDM_DRIBBLE_WEIGHT = 30;
const int CDM_SHOOT_WEIGHT = 5;

const int CM_PASS_WEIGHT = 100;
const int CM_DRIBBLE_WEIGHT = 50;
const int CM_SHOOT_WEIGHT = 6;

const int CAM_PASS_WEIGHT = 50;
const int CAM_DRIBBLE_WEIGHT = 45;
const int CAM_SHOOT_WEIGHT = 8;

//w covers lw and rw
const int W_PASS_WEIGHT = 35;
const int W_DRIBBLE_WEIGHT = 40;
const int W_SHOOT_WEIGHT = 5;

const int ST_PASS_WEIGHT = 40;
const int ST_DRIBBLE_WEIGHT = 25;
const int ST_SHOOT_WEIGHT = 7;
 


const int GK_PENALTY_CHANCE = 0;
const int CB_PENALTY_CHANCE = 1;
const int LB_PENALTY_CHANCE = 1;
const int RB_PENALTY_CHANCE = 1;
const int LWB_PENALTY_CHANCE = 1;
const int RWB_PENALTY_CHANCE = 1;
const int CDM_PENALTY_CHANCE = 2;
const int CM_PENALTY_CHANCE = 2;
const int CAM_PENALTY_CHANCE = 3;
const int LW_PENALTY_CHANCE = 4;
const int RW_PENALTY_CHANCE = 4;
const int ST_PENALTY_CHANCE = 5;

/*
const int GK_PENALTY_CHANCE = 100;
const int CB_PENALTY_CHANCE = 100;
const int LB_PENALTY_CHANCE = 100;
const int RB_PENALTY_CHANCE = 100;
const int LWB_PENALTY_CHANCE = 100;
const int RWB_PENALTY_CHANCE = 100;
const int CDM_PENALTY_CHANCE = 100;
const int CM_PENALTY_CHANCE = 100;
const int CAM_PENALTY_CHANCE = 100;
const int LW_PENALTY_CHANCE = 100;
const int RW_PENALTY_CHANCE = 100;
const int ST_PENALTY_CHANCE = 100;
*/


const double GK_POSITIVE_VARIATION = 0.25;
const double GK_NEGATIVE_VARIATION = 0.1;

const double DEF_BLOCK_CHANCE = 0.1;
const double DEF_BLOCK_VARIATION = 0.5;
const double SHOOTING_BLOCK_VARIATION = 0.5;
const double DEFENDING_VARIATION = 0.3;


const int FOUL_THRESHOLD = 20;
const int YELLOW_CARD_THRESHOLD = 10;
const int RED_CARD_THRESHOLD = 1;


/*
const int FOUL_THRESHOLD = 100;
const int YELLOW_CARD_THRESHOLD = 50;
const int RED_CARD_THRESHOLD = 25;
*/

const double PENALTY_SHOT_MODIFIER = 0.75;
const double PENALTY_SAVE_MODIFIER = 0.1;
//const double PENALTY_SAVE_MODIFIER = 100;

const int SHOOTING_ON_TARGET_THRESHOLD = 60;
const double SHOOTING_ON_TARGET_SHOOTING_BONUS_PROPORTION = 0.2;
const double SHOOTING_VARIATION = 0.25;
const double SHOT_PASS_MODIFIER = 0.1;
const double SHOT_PACE_MODIFIER = 0.1;
const double SHOT_PASS_VARIATION = 0.05;
const double SHOT_PACE_VARIATION = 0.05;
const double PASSING_VARIATION = 0.5;
const double PACE_VARIATION = 0.1;
const double PHYSICAL_VARIATION = 0.05;
const double DRIBBLING_VARIATION = 0.5;

const double PASS_BLOCK_MULTI = 0.75;
const double PASS_INTERCEPT_MULTI = 0.75;

std::unordered_map<std::string, std::unordered_map<std::string, int>> decisionMap;
std::unordered_map<std::string, std::unordered_map<std::string, int>> passMap;
std::unordered_map<std::string, std::string> defendingMap;
std::unordered_map<std::string, int> penaltyChanceMap;

int getPositionIndex(string position){
    for(int i = 0; i < numPositions; i++){
        if(position == allPositions[i]) return i;
    }
    return -1;
}

void dribbleAdvance(string& position){
    int newPositionIndex = getPositionIndex(position) + 1;
    if(newPositionIndex >= numPositions) newPositionIndex = numPositions - 1;

    position = allPositions[newPositionIndex];
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
    lwbPasses["ST"] = 2;

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
    rwbPasses["ST"] = 2;

    std::unordered_map<string, int> cdmPasses;
    cdmPasses["GK"] = 2;
    cdmPasses["CB"] = 20;
    cdmPasses["LB"] = 5;
    cdmPasses["RB"] = 5;
    cdmPasses["LWB"] = 6;
    cdmPasses["RWB"] = 6;
    cdmPasses["CDM"] = 50;
    cdmPasses["CM"] = 80;
    cdmPasses["CAM"] = 35;
    cdmPasses["LW"] = 20;
    cdmPasses["RW"] = 20;
    cdmPasses["ST"] = 3;

    std::unordered_map<string, int> cmPasses;
    cmPasses["GK"] = 0;
    cmPasses["CB"] = 12;
    cmPasses["LB"] = 3;
    cmPasses["RB"] = 3;
    cmPasses["LWB"] = 5;
    cmPasses["RWB"] = 5;
    cmPasses["CDM"] = 50;
    cmPasses["CM"] = 70;
    cmPasses["CAM"] = 50;
    cmPasses["LW"] = 30;
    cmPasses["RW"] = 30;
    cmPasses["ST"] = 5;

    std::unordered_map<string, int> camPasses;
    camPasses["GK"] = 0;
    camPasses["CB"] = 1;
    camPasses["LB"] = 2;
    camPasses["RB"] = 2;
    camPasses["LWB"] = 4;
    camPasses["RWB"] = 4;
    camPasses["CDM"] = 40;
    camPasses["CM"] = 60;
    camPasses["CAM"] = 50;
    camPasses["LW"] = 40;
    camPasses["RW"] = 40;
    camPasses["ST"] = 10;

    std::unordered_map<string, int> lwPasses;
    lwPasses["GK"] = 0;
    lwPasses["CB"] = 0;
    lwPasses["LB"] = 10;
    lwPasses["RB"] = 1;
    lwPasses["LWB"] = 25;
    lwPasses["RWB"] = 1;
    lwPasses["CDM"] = 2;
    lwPasses["CM"] = 30;
    lwPasses["CAM"] = 20;
    lwPasses["LW"] = 0;
    lwPasses["RW"] = 5;
    lwPasses["ST"] = 15;

    std::unordered_map<string, int> rwPasses;
    rwPasses["GK"] = 0;
    rwPasses["CB"] = 0;
    rwPasses["LB"] = 1;
    rwPasses["RB"] = 10;
    rwPasses["LWB"] = 1;
    rwPasses["RWB"] = 30;
    rwPasses["CDM"] = 2;
    rwPasses["CM"] = 30;
    rwPasses["CAM"] = 20;
    rwPasses["LW"] = 5;
    rwPasses["RW"] = 0;
    rwPasses["ST"] = 15;

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
    stPasses["ST"] = 20;

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

void setupDefendingMap(){
    defendingMap["GK"] = "ST";
    defendingMap["CB"] = "ST";
    defendingMap["LB"] = "LB";
    defendingMap["RB"] = "LW";
    defendingMap["LWB"] = "RWB";
    defendingMap["RWB"] = "LWB";
    defendingMap["CDM"] = "CAM";
    defendingMap["CM"] = "CM";
    defendingMap["CAM"] = "CDM";
    defendingMap["LW"] = "RB";
    defendingMap["RW"] = "LB";
    defendingMap["ST"] = "CB";
}

void setupPenaltyChanceMap(){
    penaltyChanceMap["GK"] = GK_PENALTY_CHANCE;
    penaltyChanceMap["CB"] = CB_PENALTY_CHANCE;
    penaltyChanceMap["LB"] = LB_PENALTY_CHANCE;
    penaltyChanceMap["RB"] = RB_PENALTY_CHANCE;
    penaltyChanceMap["LWB"] = LWB_PENALTY_CHANCE;
    penaltyChanceMap["RWB"] = RWB_PENALTY_CHANCE;
    penaltyChanceMap["CDM"] = CDM_PENALTY_CHANCE;
    penaltyChanceMap["CM"] = CM_PENALTY_CHANCE;
    penaltyChanceMap["CAM"] = CAM_PENALTY_CHANCE;
    penaltyChanceMap["LW"] = LW_PENALTY_CHANCE;
    penaltyChanceMap["RW"] = RW_PENALTY_CHANCE;
    penaltyChanceMap["ST"] = ST_PENALTY_CHANCE;
}

void setupMaps(){
    setupDecisionMap();
    setupPassMap();
    setupDefendingMap();
    setupPenaltyChanceMap();
}

string getRandomKeyFromMap(std::unordered_map<std::string, int> map, const std::string keys[], int numKeys){
    //add up all weights in the given map
    int total = 0;
    for(int i = 0; i < numKeys; i++){
        total += map[keys[i]];
    }
    //now generate a number and go back through the map until we know which position the number applies to
    int number = generateRandom(0, total);
    total = 0;
    for(int i = 0; i < numKeys; i++){
        total += map[keys[i]];
        if(total > number) return keys[i];
    }
    //otherwise return the last position
    return keys[numKeys - 1];
}




void goal(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position, int penalty){
    teams[*teamIndexOnBall].scored(*onBall, lastPass, penalty);

    *teamIndexOnBall = !(*teamIndexOnBall);
    lastPass = nullptr;
    onBall = teams[*teamIndexOnBall].getRandomPlayer();
    position = onBall->getSpecificPosition();
}

void save(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position, bool saved, bool penalty){
    *teamIndexOnBall = !(*teamIndexOnBall);
    lastPass = nullptr;
    onBall = teams[*teamIndexOnBall].getPlayerFromPosition("GK");

    if(!onBall) onBall = teams[*teamIndexOnBall].getRandomPlayer();

    position = "GK";

    if(saved) onBall->makeSave();
    if(penalty) onBall->savePenalty();
}

void penalty(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    cout << "PENALTY!\n";

    //decide which player should take
    onBall = teams[*teamIndexOnBall].getPenaltyTaker();
    cout << onBall->getName() + " taking the penalty\n";
    int shootingRating = onBall->getShooting();
    int shotRating = shootingRating + generateRandom(0, shootingRating * PENALTY_SHOT_MODIFIER);
    shotRating = generateRandom(shotRating * 0.5, shotRating);
    //cout << "chosen taker\n";

    Player* keeper = teams[!(*teamIndexOnBall)].getPlayerFromPosition("GK");
    int keeperRating = 10;
    if(keeper) keeperRating = keeper->getRating();
    cout << "keeper rating: " + to_string(keeperRating) + "\n";
    //cout << "found keeper\n";

    int saveRating = keeperRating + generateRandom(0, keeperRating * PENALTY_SAVE_MODIFIER);
    saveRating = generateRandom(saveRating * 0.5, saveRating);
    //cout << "got save rating\n";

    //figure out who gets credited with the assist
    if(onBall->getID() == lastPass->getID()){
        //cout << onBall->getName() + "\n";
        //cout << lastPass->getName() + "\n";
        //cout << "penalty taker is the penalty winner\n";
        lastPass = nullptr;
    }
    //cout << "comparing values now\n";

    cout << "Save rating: " + to_string(saveRating) + "\n";
    cout << "Shot rating: " + to_string(shotRating) + "\n";

    if(saveRating < shotRating){
        cout << "penalty scored\n";
        goal(teams, teamIndexOnBall, onBall, lastPass, position, 1);
    }
    else{
        cout << "penalty missed\n";
        teams[*teamIndexOnBall].missedPenalty(*onBall);
        save(teams, teamIndexOnBall, onBall, lastPass, position, true, true);
    }
}

void foul(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position, Player*& defender, int tackleRating){
    cout << "\nFOUL!\n";
    //check if it's a red or yellow card
    if(generateRandom(0, 100) < RED_CARD_THRESHOLD){
        teams[!(*teamIndexOnBall)].getRedCard(defender->getID());
    }
    else if(generateRandom(0, 100) < YELLOW_CARD_THRESHOLD){
        teams[!(*teamIndexOnBall)].getYellowCard(defender->getID());
    }

    //also check if it's a penalty!
    int penaltyChance = penaltyChanceMap[onBall->getSpecificPosition()];
    //cout << "chance of penalty: " + to_string(penaltyChance) + "\n";
    if(generateRandom(0, 1000) < penaltyChance){
        lastPass = onBall;
        penalty(teams, teamIndexOnBall, onBall, lastPass, position);
    }
}

void block(Player*& blocker, Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    *teamIndexOnBall = !(*teamIndexOnBall);
    lastPass = nullptr;
    onBall = blocker;
    position = blocker->getSpecificPosition();

    blocker->makeDFCon();
}

void pass(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    cout << "Pass\n";

    //first check for interception from passer (passing vs defending) ------------------------------------
    //get the passer's passing ability (with some random variation)
    int passAbility = onBall->getPassing();
    int passRating = passAbility + generateRandom(-passAbility * PASSING_VARIATION, passAbility * PASSING_VARIATION);

    //cout << "pass rating: " + to_string(passRating) + "\n";

    //cout << "pass ratings\n";

    //get position of the defender
    string passerPosition = position;
    string defenderPosition = defendingMap[passerPosition];
    Player* defender = teams[!(*teamIndexOnBall)].getPlayerFromPosition(defenderPosition);

    if(defender){
        //cout << "found defender\n";

        //get the defender's defending ability (with some random variation)
        int defendingAbility = defender->getDefending() * PASS_BLOCK_MULTI;
        //cout << "defending rating: " + to_string(defendingAbility) + "\n";

        if(defendingAbility > passRating){
            //cout << "pass blocked by " + defender->getName() + "\n";
            block(defender, teams, teamIndexOnBall, onBall, lastPass, position);
            return;
        }
    }
    

    //cout << "interception failed\n";


    //find a player to pass to randomly
    Player* player = nullptr;
    while(!player || player == onBall){
        player = teams[*teamIndexOnBall].getPlayerFromPosition(getRandomKeyFromMap(passMap[position], allPositions, numPositions));
    }
    //cout << "choose player to pass to\n";

    //then check for interception for person marking receiver (pace vs physicality) -----------------------------------------------
    //get the receiver's pace ability (with some random variation)
    int paceAbility = player->getPace();
    int paceRating = paceAbility + generateRandom(-paceAbility * PACE_VARIATION, paceAbility * PACE_VARIATION);
    //cout << "pace\n";
    //cout << "pace rating: " + to_string(paceRating) + "\n";

    //get position of the defender
    string receiverPosition = player->getSpecificPosition();
    string intercepterPosition = defendingMap[receiverPosition];
    Player* intercepter = teams[!(*teamIndexOnBall)].getPlayerFromPosition(intercepterPosition);
    //cout << "defender\n";

    if(intercepter){
        //get the defender's physical ability (with some random variation)
        int physicalAbility = intercepter->getPhysical() * PASS_INTERCEPT_MULTI;
        //cout << "physical rating: " + to_string(physicalAbility) + "\n";

        if(physicalAbility > passRating){
            //cout << "pass intercepted by " + intercepter->getName() + "\n";
            block(intercepter, teams, teamIndexOnBall, onBall, lastPass, position);
            return;
        }
    }
    
    //cout << "pass succeeded\n";

    lastPass = onBall;
    position = player->getSpecificPosition();
    onBall = player;
}

void dribble(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    cout << "dribble\n";
    //check being tackled
    //get dribbling ability
    int dribbleAbility = onBall->getDribbling();
    int dribbleRating = dribbleAbility + generateRandom(-dribbleAbility * DRIBBLING_VARIATION, dribbleAbility * DRIBBLING_VARIATION);

    //get position of the defender
    string dribblerPosition = position;
    string defenderPosition = defendingMap[dribblerPosition];
    Player* defender = teams[!(*teamIndexOnBall)].getPlayerFromPosition(defenderPosition);

    if(defender){
        //get defending ability
        int defendAbility = defender->getDefending();
        int defendRating = defendAbility + generateRandom(-defendAbility * DEFENDING_VARIATION, defendAbility * DEFENDING_VARIATION);

        //get the chance of the dribble succeeding
        int total = dribbleRating + defendRating;
        double chanceToSucceed = (double)dribbleRating / (double)total;
        double roll = (double)rand() / RAND_MAX;

        if(roll > chanceToSucceed){
            cout << "Tackled!\n";
            block(defender, teams, teamIndexOnBall, onBall, lastPass, position);
            return;
        }
        else{
            if(generateRandom(0, defendRating) < FOUL_THRESHOLD){
                foul(teams, teamIndexOnBall, onBall, lastPass, position, defender, defendRating);
            }
        }
    }

    //then increase position by 1 index
    dribbleAdvance(position);
}

void shoot(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    cout << "SHOOT!\n";
    //check how good the shot is
    //base value is how good they are at shooting (+- some random number)
    int playerShooting = onBall->getShooting();
    int shotRating = playerShooting;
    //cout << "shooting: " + to_string(playerShooting) + "\n";

    shotRating += generateRandom(-(double)playerShooting * SHOOTING_VARIATION, (double)playerShooting * SHOOTING_VARIATION);
    //cout << "shot rating: " + to_string(shotRating) + "\n";

    //if the current player was passed to, increase shooting by half the other player's passing
    if(lastPass){
        //cout << "Passed to\n";
        int passValue = lastPass->getPassing();
        //cout << "pass value\n";
        passValue += generateRandom(-passValue * SHOT_PASS_VARIATION, passValue * SHOT_PASS_VARIATION);
        //cout << "noise added\n";
        shotRating += passValue * SHOT_PASS_MODIFIER;
        //cout << "shot rating calculated\n";
    }
    //otherwise increase by the current player's pace
    else{
        //cout << "won it back\n";
        int paceValue = onBall->getPace();
        paceValue += generateRandom(-paceValue * SHOT_PACE_VARIATION, paceValue * SHOT_PACE_VARIATION);
        shotRating += paceValue * SHOT_PACE_MODIFIER;
    }
    //cout << "shot rating: " + to_string(shotRating) + "\n";
    
    //check for defensive blocks
    vector<Player*> defenders = teams[!(*teamIndexOnBall)].getPlayersFromGeneralPosition("DEF");
    for(auto player : defenders){
        if(generateRandom(0, 100) <= (DEF_BLOCK_CHANCE * 100) && 
        generateRandom(player->getDefending() - (player->getDefending() * DEF_BLOCK_VARIATION), player->getDefending() + (player->getDefending() * DEF_BLOCK_VARIATION)) > shotRating){
            cout << "BLOCKED by " + player->getName() + "\n";
            block(player, teams, teamIndexOnBall, onBall, lastPass, position);
            return;
        }
    }

    //is it even on target?
    if(generateRandom(0, 100) + (playerShooting * SHOOTING_ON_TARGET_SHOOTING_BONUS_PROPORTION) <= SHOOTING_ON_TARGET_THRESHOLD){
        cout << "OFF TARGET\n";
        save(teams, teamIndexOnBall, onBall, lastPass, position, false, false);
        return;
    }

    //now compare show rating to other team's gk ability
    int gkRating = teams[!(*teamIndexOnBall)].getPlayerFromPosition("GK")->getRating();
    int saveRating = gkRating;
    saveRating += generateRandom(-gkRating * GK_NEGATIVE_VARIATION, gkRating * GK_POSITIVE_VARIATION);
    //cout << "Keeper rating: " + to_string(saveRating) + "\n";

    int total = shotRating + saveRating;
    double chanceToScore = (double)shotRating / (double)total;
    chanceToScore /= 2.0;
    //cout << "Chance to score: " + to_string(chanceToScore) + "\n";

    double roll = (double)rand() / RAND_MAX;
    //cout << "roll: " + to_string(roll) + "\n";

    if(roll < chanceToScore){
        cout << "\nGOAL!!!\n\n\n\n";
        goal(teams, teamIndexOnBall, onBall, lastPass, position, 0);
    }
    else{
        cout << "SAVE!\n";
        save(teams, teamIndexOnBall, onBall, lastPass, position, true, false);
    }
}

void simulateStep(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    cout << onBall->getName() + " (" + position + ")\n";

    //every playing player, play one minute
    for(int i = 0; i < 2; i++){
        teams[i].teamPlayAMinute();
    }

    //player decision making:
    //first, use SPECIFIC position to look at the different options: passing, dribbling or shooting.
    string choice = getRandomKeyFromMap(decisionMap[position], allChoices, numChoices);
    
    //pass:
    //now look at the pass map: for every position, there is a pass map to every other position. choose one at random (if your team contains a player in that position)
    if(choice == "PASS") pass(teams, teamIndexOnBall, onBall, lastPass, position);

    //dribbling:
    //move one position up the position index
    else if(choice == "DRIBBLE") dribble(teams, teamIndexOnBall, onBall, lastPass, position);

    //shooting:
    //shoot at goal, chance of a block or deflection or saving
    else if(choice == "SHOOT") shoot(teams, teamIndexOnBall, onBall, lastPass, position);
}

int simulateMatch(Team teams[]) {
    //kickoff
    int teamIndexOnBall = generateRandom(0, 2);
    Player* onBall = teams[teamIndexOnBall].getRandomPlayer();
    Player* lastPass = nullptr;
    std::string position = onBall->getSpecificPosition();
    int totalMinutes = 1;

    for(int part = 0; part < PARTS; part++){
        for(int minute = 1; minute <= MINUTES_PER_PART; minute++, totalMinutes++){
            cout << to_string(totalMinutes) + "'\n";
            for(int step = 0; step < STEPS_PER_MINUTE; step++){
                simulateStep(teams, &teamIndexOnBall, onBall, lastPass, position);
            }
            cout << "\n\n";
        }

        if(part == 1) continue;
        cout << "\n\n\n\n\nHALF TIME\n";
        cout << to_string(teams[0].getGoals()) + "-" + to_string(teams[1].getGoals()) + "\n\n\n\n\n";

        teamIndexOnBall = !teamIndexOnBall;
        onBall = teams[teamIndexOnBall].getRandomPlayer();
        lastPass = nullptr;
        position = onBall->getSpecificPosition();
    }

    //fulltime
    cout << "Match finished\n";
    cout << to_string(teams[0].getGoals()) + "-" + to_string(teams[1].getGoals()) + "\n";

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

    //srand(time(NULL));

    int errorCode = simulateMatch(teams);
    if(errorCode != 0){
        cout << "Match simulation failed with error" + to_string(errorCode) + ".\n";
        return -1;
    }

    //cout << "TEAM 1 KEEPER SAVED " + to_string(teams[0].getPlayerFromPosition("GK")->getPenaltySaves()) + " PENALTIES\n";
    //cout << "TEAM 2 KEEPER SAVED " + to_string(teams[1].getPlayerFromPosition("GK")->getPenaltySaves()) + " PENALTIES\n";

    //write the match details to the 'fixtureOutcome.txt' file
    writeToOutputFile(teams);

    return 0;
}
