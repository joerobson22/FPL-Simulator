#include "Player.h"
#include "Constants.h"
#include <iostream>
#include <string>
#include <vector>
#include <utility>

//implementation of player methods declared in the header file
Player::Player(std::vector<std::pair<std::string, std::string>> pairs) {
    for(int i = 0; i < pairs.size(); i++){
        std::pair<std::string, std::string> p = pairs[i];
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

void Player::setAttribute(std::pair<std::string, std::string> p){
    std::string key = p.first;
    std::string value = p.second;
    if(key == "id"){
        id = std::stoi(value);
        return;
    }
    else if(key == "rating"){
        rating = std::stoi(value);
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

    if(generalPosition == "GK") value = std::to_string(rating);

    if(key == "PAC"){
        pace = std::stoi(value);
    }
    else if(key == "SHO"){
        shooting = std::stoi(value);
    }
    else if(key == "PAS"){
        passing = std::stoi(value);
    }
    else if(key == "DRI"){
        dribbling = std::stoi(value);
    }
    else if(key == "DEF"){
        defending = std::stoi(value);
    }
    else if(key == "PHY"){
        physical = std::stoi(value);
    }
}

void Player::scored(int penalty){ 
    goals++;
    std::cout << name + " scored!\n";
    penaltyGoals += penalty;
}
void Player::assisted(){ 
    assists++;
    std::cout << name + " assisted!\n\n";
}
void Player::getYellowCard(){
    yellowCard = true;
    std::cout << "\n\nYELLOW CARD: " + name + "\n\n";
}
void Player::getRedCard(){
    yellowCard = false;
    redCard = true;
    std::cout << "\n\nRED CARD: " + name + "\n\n";
}
void Player::missPenalty(){
    penaltyMisses++;
    std::cout << "\n\nMISSED PENALTY: " + name + "\n\n";
}
void Player::savePenalty(){
    penaltySaves++;
    std::cout << "\n\nSAVED PENALTY: " + name + "\n\n";
}

void Player::playMinute(){
    if(!sub) minsPlayed++;
}

void Player::makeDFCon(){
    dfCon++;
}

void Player::makeSave(){
    saves++;
}

int Player::getPace(){ return pace; }
int Player::getShooting(){ return shooting; }
int Player::getPassing(){ return passing; }
int Player::getDribbling(){ return dribbling; }
int Player::getDefending(){ return defending; }
int Player::getPhysical(){ return physical; }

int Player::getID(){ return id; }
int Player::getRating(){ return rating; }
std::string Player::getSpecificPosition(){ return specificPosition; }
std::string Player::getGeneralPosition(){ return generalPosition; }
std::string Player::getName(){ return name; }
int Player::getGoals(){ return goals; }
int Player::getPenaltyGoals(){ return penaltyGoals; }
int Player::getAssists(){ return assists; }
int Player::getMinsPlayed(){ return minsPlayed; }
int Player::getDFCon(){ return dfCon; }
int Player::getSaves(){ return saves; }
int Player::getPenaltyMisses(){ return penaltyMisses; }
int Player::getPenaltySaves(){ return penaltySaves; }
bool Player::gotYellowCard(){ return yellowCard; }
bool Player::gotRedCard(){ return redCard; }