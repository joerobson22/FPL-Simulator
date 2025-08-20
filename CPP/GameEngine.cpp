#include <iostream>
#include <fstream>
#include <string>
#include <vector>
using namespace std;


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

    void conceded(){
        for(int i = 0; i < players.size(); i++){
            players[i].conceded();
        }
    }
};

class Player{
    private:
    int id;
    int rating;
    string position;
    string name;
    int goals;
    int assists;
    bool cleanSheet;

    public:
    Player(int rating, string position, string name){
        this->rating = rating;
        this->position = position;
        this->name = name;

        goals = 0;
        assists = 0;
        cleanSheet = true;
    }

    void scored(){ goals++; }
    void assisted(){ assists++; }
    void conceded(){ cleanSheet = false; }

    int getID(){ return id; }
    int getRating(){ return rating; }
    string getPosition(){ return position; }
    string getName(){ return name; }
};




int main() {
    string filePath = "fixtureData.txt";
    string line;

    ifstream reader(filePath);

    while(getline(reader, line)){

    }


    return 0;
}
