#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <optional>
#include <map>
#include <cstdlib>
#include <ctime>

using namespace std;

const string inputFilePath = "../fixtureData.txt";
const string outputFilePath = "../fixtureOutcome.txt";

const int HOME_TEAM = 1;
const int AWAY_TEAM = 2;
const string HOME_PLAYERS_START = "HOME PLAYERS";
const string AWAY_PLAYERS_START = "AWAY PLAYERS";

//PLAYER CLASS
class Player{
    private:
    int id;
    int rating;
    string position;
    string name;
    int goals;
    int assists;

    public:
    Player(vector<pair<string, string>> pairs){
        for(int i = 0; i < pairs.size(); i++){
            pair<string, string> p = pairs[i];
            setAttribute(p);
        }

        goals = 0;
        assists = 0;
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
        else if(p.first == "position"){
            position = p.second;
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

    int getID(){ return id; }
    int getRating(){ return rating; }
    string getPosition(){ return position; }
    string getName(){ return name; }
    int getGoals(){ return goals; }
    int getAssists(){ return assists; }

    void outputPlayer(){
        cout << "name: " + name + "\n";
        cout << "rating: " + to_string(rating);
        cout << "\nposition: " + position + "\n";
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
        team[player.getPosition()].push_back(player);
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

    int i = 0;
    while(getline(reader, line)){
        i++;
        //different specific cases
        if(i == HOME_TEAM){
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

void writeToOutputFile(Team teams[]){
    ofstream output(outputFilePath);

    output << to_string(teams[0].getGoals()) + "\n";
    output << to_string(teams[1].getGoals()) + "\n";
    output << "GOAL SCORERS," + getGoalScorersString(teams) + "\n";
    output << "ASSISTERS," + getAssistersString(teams) + "\n";
    output << "CLEAN SHEETS," + getCleanSheetsString(teams) + "\n";

    output.close();
}
//----------------------------------




//MATCH ENGINE!!!
int simulateMatch(Team teams[]){
    for(int i = 0; i < (rand() % 10) + 3; i++){
        teams[0].scored(teams[0].getRandomPlayer(), teams[0].getRandomPlayer());
    }

    for(int i = 0; i < (rand() % 10) + 3; i++){
        teams[1].scored(teams[1].getRandomPlayer(), teams[1].getRandomPlayer());
    }

    return 0;
}






int main() {
    srand(time(NULL));

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
