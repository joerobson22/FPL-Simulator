#ifndef PLAYER_H
#define PLAYER_H

#include <string>
#include <vector>
#include <utility>

class Player {
private:
    //private variables
    int id;
    int rating;
    std::string specificPosition;
    std::string generalPosition;
    std::string name;
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
    Player(std::vector<std::pair<std::string, std::string>> pairs);
    
    // public methods
    void scored(int penalty);
    void assisted();
    void getYellowCard();
    void getRedCard();
    void missPenalty();
    void savePenalty();
    void playMinute();
    void makeDFCon();
    void makeSave();
    
    // getter method
    int getID();
    int getRating();
    std::string getSpecificPosition();
    std::string getGeneralPosition();
    std::string getName();
    int getGoals();
    int getPenaltyGoals();
    int getAssists();
    int getMinsPlayed();
    int getDFCon();
    int getSaves();
    int getPenaltyMisses();
    int getPenaltySaves();
    bool gotYellowCard();
    bool gotRedCard();
    int getPace();
    int getShooting();
    int getPassing();
    int getDribbling();
    int getDefending();
    int getPhysical();
    
    void outputPlayer();
    void setAttribute(std::pair<std::string, std::string> p);
};

#endif