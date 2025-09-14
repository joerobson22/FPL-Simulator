#ifndef MATCHSIM_H
#define MATCHSIM_H

#include "Team.h"
#include "HelperMethods.h"
#include "Constants.h"
#include <string>

// Match simulation functions
void setupMaps();
void setupDecisionMap();
void setupPassMap();
void setupDefendingMap();
void setupPenaltyChanceMap();
std::string getRandomKeyFromMap(std::unordered_map<std::string, int> map, const std::string keys[], int numKeys);
void dribbleAdvance(std::string& position);
int getPositionIndex(std::string position);

// Action functions
void goal(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position, int penalty);
void save(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position, bool saved, bool penalty);
void penalty(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position);
void foul(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position, Player*& defender, int tackleRating);
void block(Player*& blocker, Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position);
void pass(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position);
void dribble(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position);
void shoot(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position);
void simulateStep(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position);
int simulateMatch(Team teams[]);

#endif