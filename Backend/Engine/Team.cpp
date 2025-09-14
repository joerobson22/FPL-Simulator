#include "Team.h"
#include "Constants.h"
#include "HelperMethods.h"
#include "Player.h"
#include <iostream>
#include <algorithm>

Team::Team(){
    this->name = "";
    goals = 0;
}

Team::Team(std::string name){
    this->name = name;
    goals = 0;
}

void Team::scored(Player& scorer, Player* assister, int penalty){
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

void Team::missedPenalty(Player& misser){
    for(int i = 0; i < playingPlayers.size(); i++){
        if(misser.getID() == playingPlayers[i].getID()){
            playingPlayers[i].missPenalty();
        }
    }
}

void Team::getRedCard(int id){
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

void Team::getYellowCard(int id){
    for(int i = 0; i < playingPlayers.size(); i++){
        if(playingPlayers[i].getID() == id){
            if(playingPlayers[i].gotYellowCard()) playingPlayers[i].getRedCard();
            else playingPlayers[i].getYellowCard();
        }
    }
}

void Team::teamPlayAMinute(){
    for(int i = 0; i < playingPlayers.size(); i++){
        playingPlayers[i].playMinute();
    }
}

Player* Team::getPenaltyTaker(){
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


void Team::addPlayer(Player& player){ 
    players.push_back(player);
    playingPlayers.push_back(player);
    team[player.getGeneralPosition()].push_back(player);
}

std::vector<Player>& Team::getPlayers(){ return players; }

std::vector<Player>& Team::getPlayingPlayers(){ return playingPlayers; }

void Team::transferPlayingPlayersToPlayers(){
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


Player* Team::getPlayerFromPosition(std::string position){
    std::vector<Player*> positionPlayers;
    for(auto& p : playingPlayers){
        if(p.getSpecificPosition() == position){
            positionPlayers.push_back(&p);
        }
    }
    if(positionPlayers.size() == 0) return nullptr;

    return positionPlayers[HelperMethods::generateRandom(0, positionPlayers.size())];
}

std::vector<Player*> Team::getPlayersFromGeneralPosition(std::string generalPos){
    std::vector<Player*> positionPlayers;
    for(auto& p : players){
        if(p.getGeneralPosition() == generalPos){
            positionPlayers.push_back(&p);
        }
    }
    return positionPlayers;
}

std::string Team::getGoalScorersDictionary(){
    std::string output = "";
    for(int i = 0; i < players.size(); i++){
        for(int j = 0; j < players[i].getGoals(); j++){
            if(output != "") output += ",";
            output += std::to_string(players[i].getID());
        }
    }
    return output;
}
std::string Team::getAssistersDictionary(){
    std::string output = "";
    for(int i = 0; i < players.size(); i++){
        for(int j = 0; j < players[i].getAssists(); j++){
            if(output != "") output += ",";
            output += std::to_string(players[i].getID());
        }
    }
    return output;
}
std::string Team::getCleanSheetDictionary(bool cleanSheet){
    std::string output = "";
    if(cleanSheet){
        for(int i = 0; i < players.size(); i++){
            if(!HelperMethods::validCleanSheetPosition(players[i].getGeneralPosition())) continue;
            if(output != "") output += ",";

            output += std::to_string(players[i].getID());
        }
    }
    return output;
}
std::string Team::get60MinDictionary(){
    std::string output = "";
    for(int i = 0; i < players.size(); i++){
        if(players[i].getMinsPlayed() >= 60){
            if(output != "") output += ",";
            output += std::to_string(players[i].getID());
        }
    }
    return output;
}
std::string Team::get3SavesDictionary(){
    std::string output = "";
    for(int j = 0; j < players[0].getSaves() % 3; j++){
        if(output != "") output += ",";
        output += std::to_string(players[0].getID());
    }
    return output;
}
std::string Team::getDFConDictionary(){
    std::string output = "";
    for(int i = 0; i < players.size(); i++){
        if(players[i].getDFCon() >= HelperMethods::getDFConForPosition(players[i].getGeneralPosition())){
            if(output != "") output += ",";
            output += std::to_string(players[i].getID());
        }
    }
    return output;
}
std::string Team::getPenaltyGoalScorersDictionary(){
    std::string output = "";
    for(int i = 0; i < players.size(); i++){
        for(int j = 0; j < players[i].getPenaltyGoals(); j++){
            if(output != "") output += ",";
            output += std::to_string(players[i].getID());
        }
    }
    return output;
}
std::string Team::getPenaltyMissesDictionary(){
    std::string output = "";
    for(int i = 0; i < players.size(); i++){
        for(int j = 0; j < players[i].getPenaltyMisses(); j++){
            if(output != "") output += ",";
            output += std::to_string(players[i].getID());
        }
    }
    return output;
}
std::string Team::getPenaltySavesDictionary(){
    std::string output = "";
    for(int i = 0; i < players.size(); i++){
        for(int j = 0; j < players[i].getPenaltySaves(); j++){
            if(output != "") output += ",";
            output += std::to_string(players[i].getID());
        }
    }
    return output;
}
std::string Team::getYellowCardDictionary(){
    std::string output = "";
    for(int i = 0; i < players.size(); i++){
        if(players[i].gotYellowCard()){
            if(output != "") output += ",";
            output += std::to_string(players[i].getID());
        }
    }
    return output;
}
std::string Team::getRedCardDictionary(){
    std::string output = "";
    for(int i = 0; i < players.size(); i++){
        if(players[i].gotRedCard()){
            if(output != "") output += ",";
            output += std::to_string(players[i].getID());
        }
    }
    return output;
}

int Team::getGoals(){ return goals; }
Player* Team::getRandomPlayer(){
    return &playingPlayers[rand() % playingPlayers.size()];
}