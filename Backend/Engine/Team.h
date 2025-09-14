#ifndef TEAM_H
#define TEAM_H

#include "Player.h"
#include <string>
#include <vector>
#include <map>

class Team {
private:
    //private variables
    std::string name;
    int goals;
    std::vector<Player> players;
    std::vector<Player> playingPlayers;
    std::map<std::string, std::vector<Player>> team;

public:
    Team();
    Team(std::string name);
    
    // public methods
    void scored(Player& scorer, Player* assister, int penalty);
    void missedPenalty(Player& misser);
    void getRedCard(int id);
    void getYellowCard(int id);
    void teamPlayAMinute();
    Player* getPenaltyTaker();
    void addPlayer(Player& player);
    std::vector<Player>& getPlayers();
    std::vector<Player>& getPlayingPlayers();
    void transferPlayingPlayersToPlayers();
    Player* getPlayerFromPosition(std::string position);
    std::vector<Player*> getPlayersFromGeneralPosition(std::string generalPos);
    
    // dictionary methods
    std::string getGoalScorersDictionary();
    std::string getAssistersDictionary();
    std::string getCleanSheetDictionary(bool cleanSheet);
    std::string get60MinDictionary();
    std::string get3SavesDictionary();
    std::string getDFConDictionary();
    std::string getPenaltyGoalScorersDictionary();
    std::string getPenaltyMissesDictionary();
    std::string getPenaltySavesDictionary();
    std::string getYellowCardDictionary();
    std::string getRedCardDictionary();
    
    //miscellaneous methods
    int getGoals();
    void outputTeam();
    Player* getRandomPlayer();
};

#endif