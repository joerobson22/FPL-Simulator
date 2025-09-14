#ifndef CONSTANTS_H
#define CONSTANTS_H

#include <string>

const std::string inputFilePath = "../fixtureData.txt";
const std::string outputFilePath = "../fixtureOutcome.txt";

const int FILE_SEED_LINE = 0;
const int FILE_HOME_TEAM_LINE = 1;
const int FILE_AWAY_TEAM_LINE = 2;
const std::string FILE_HOME_PLAYERS_START_LINE = "HOME PLAYERS";
const std::string FILE_AWAY_PLAYERS_START_LINE = "AWAY PLAYERS";

const int PARTS = 2;
const int MINUTES_PER_PART = 45;
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

const int FOUL_THRESHOLD = 20;
const int YELLOW_CARD_THRESHOLD = 10;
const int RED_CARD_THRESHOLD = 1;

const double PENALTY_SHOT_MODIFIER = 0.75;
const double PENALTY_SAVE_MODIFIER = 0.1;

const double GK_POSITIVE_VARIATION = 0.25;
const double GK_NEGATIVE_VARIATION = 0.1;

const double DEF_BLOCK_CHANCE = 0.1;
const double DEF_BLOCK_VARIATION = 0.5;
const double SHOOTING_BLOCK_VARIATION = 0.5;
const double DEFENDING_VARIATION = 0.3;

const int SHOOTING_ON_TARGET_THRESHOLD = 60;
const double SHOOTING_ON_TARGET_SHOOTING_BONUS_PROPORTION = 0.2;

const double SHOOTING_VARIATION = 0.25;
const double SHOT_PASS_VARIATION = 0.05;
const double SHOT_PACE_VARIATION = 0.05;
const double SHOT_PASS_MODIFIER = 0.1;
const double SHOT_PACE_MODIFIER = 0.1;

const double PASSING_VARIATION = 0.5;
const double PACE_VARIATION = 0.1;
const double PHYSICAL_VARIATION = 0.05;
const double DRIBBLING_VARIATION = 0.5;

const double PASS_BLOCK_MULTI = 0.75;
const double PASS_INTERCEPT_MULTI = 0.75;

#endif