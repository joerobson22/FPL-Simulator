#include <iostream>
#include <fstream>
#include <string>
#include <vector>
using namespace std;

const string filePath = "../fixtureData.txt";
const int HOME_TEAM = 1;
const int AWAY_TEAM = 2;
const string HOME_PLAYERS_START = "HOME PLAYERS";
const string AWAY_PLAYERS_START = "AWAY PLAYERS";

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
    }

    Player(int id, int rating, string position, string name){
        this->id = id;
        this->rating = rating;
        this->position = position;
        this->name = name;

        goals = 0;
        assists = 0;
    }

    void scored(){ goals++; }
    void assisted(){ assists++; }

    int getID(){ return id; }
    int getRating(){ return rating; }
    string getPosition(){ return position; }
    string getName(){ return name; }

    void outputPlayer(){
        cout << "name: " + name + "\n";
        cout << "rating: " + to_string(rating);
        cout << "\nposition: " + position + "\n";
    }
};

class Team{
    private:
    string name;
    int goals;
    vector<Player> players;

    public:
    Team(string name){
        this->name = name;
        goals = 0;
    }

    void scored(Player& scorer, Player& assister){
        goals++;

        bool scorerFound = false;
        bool assisterFound = false;

        for(int i = 0; i < players.size(); i++){
            if(scorer.getID() == players[i].getID()){
                players[i].scored();
            }
            else if(assister.getID() == players[i].getID()){
                players[i].assisted();
            }
        }
    }

    void addPlayer(Player& player){ players.push_back(player); }

    void outputTeam(){
        cout << name + "\n\n";
        for(int i = 0; i < players.size(); i++){
            players[i].outputPlayer();
            cout << "\n";
        }
    }
};

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

int main() {
    cout << "GameEngine.exe running\n";

    string line;
    ifstream reader(filePath);

    Team homeTeam("");
    Team awayTeam("");

    int teamPlayers = -1;

    int i = 0;
    while(getline(reader, line)){
        i++;
        //different specific cases
        if(i == HOME_TEAM){
            homeTeam = Team(line);
            continue;
        }
        else if(i == AWAY_TEAM){
            awayTeam = Team(line);
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
        (teamPlayers == 0) ? homeTeam.addPlayer(p) : awayTeam.addPlayer(p);
    }

    /* output the teams if need be
    homeTeam.outputTeam();
    cout << "\n";
    awayTeam.outputTeam();
    */

    return 0;
}
