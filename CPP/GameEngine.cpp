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
};

void trimString(string& line){
    line = line.substr(1, line.length() - 2);
}

pair<string, string> parseKeyValuePair(string section){

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
        cout << line + "\n";
        trimString(line);
        cout << line + "\n";
    }


    return 0;
}
