#include <map>
#include <iostream>
#include <unordered_map>
#include "MatchSim.h"

using namespace std;

std::unordered_map<std::string, std::unordered_map<std::string, int>> decisionMap;
std::unordered_map<std::string, std::unordered_map<std::string, int>> passMap;
std::unordered_map<std::string, std::string> defendingMap;
std::unordered_map<std::string, int> penaltyChanceMap;

int getPositionIndex(string position){
    for(int i = 0; i < numPositions; i++){
        if(position == allPositions[i]) return i;
    }
    return -1;
}

void dribbleAdvance(string& position){
    int newPositionIndex = getPositionIndex(position) + 1;
    if(newPositionIndex >= numPositions) newPositionIndex = numPositions - 1;

    position = allPositions[newPositionIndex];
}

void setupDecisionMap(){
    //setup every position's unordered maps
    std::unordered_map<string, int> gkDecisions;
    gkDecisions["PASS"] = GK_PASS_WEIGHT;
    gkDecisions["DRIBBLE"] = GK_DRIBBLE_WEIGHT;
    gkDecisions["SHOOT"] = GK_SHOOT_WEIGHT;

    std::unordered_map<string, int> cbDecisions;
    cbDecisions["PASS"] = CB_PASS_WEIGHT;
    cbDecisions["DRIBBLE"] = CB_DRIBBLE_WEIGHT;
    cbDecisions["SHOOT"] = CB_SHOOT_WEIGHT;

    std::unordered_map<string, int> fbDecisions;
    fbDecisions["PASS"] = FB_PASS_WEIGHT;
    fbDecisions["DRIBBLE"] = FB_DRIBBLE_WEIGHT;
    fbDecisions["SHOOT"] = FB_SHOOT_WEIGHT;

    std::unordered_map<string, int> wbDecisions;
    wbDecisions["PASS"] = WB_PASS_WEIGHT;
    wbDecisions["DRIBBLE"] = WB_DRIBBLE_WEIGHT;
    wbDecisions["SHOOT"] = WB_SHOOT_WEIGHT;

    std::unordered_map<string, int> cdmDecisions;
    cdmDecisions["PASS"] = CDM_PASS_WEIGHT;
    cdmDecisions["DRIBBLE"] = CDM_DRIBBLE_WEIGHT;
    cdmDecisions["SHOOT"] = CDM_SHOOT_WEIGHT;

    std::unordered_map<string, int> cmDecisions;
    cmDecisions["PASS"] = CM_PASS_WEIGHT;
    cmDecisions["DRIBBLE"] = CM_DRIBBLE_WEIGHT;
    cmDecisions["SHOOT"] = CM_SHOOT_WEIGHT;

    std::unordered_map<string, int> camDecisions;
    camDecisions["PASS"] = CAM_PASS_WEIGHT;
    camDecisions["DRIBBLE"] = CAM_DRIBBLE_WEIGHT;
    camDecisions["SHOOT"] = CAM_SHOOT_WEIGHT;

    std::unordered_map<string, int> wDecisions;
    wDecisions["PASS"] = W_PASS_WEIGHT;
    wDecisions["DRIBBLE"] = W_DRIBBLE_WEIGHT;
    wDecisions["SHOOT"] = W_SHOOT_WEIGHT;

    std::unordered_map<string, int> stDecisions;
    stDecisions["PASS"] = ST_PASS_WEIGHT;
    stDecisions["DRIBBLE"] = ST_DRIBBLE_WEIGHT;
    stDecisions["SHOOT"] = ST_SHOOT_WEIGHT;

    //then add every position to the overall decision map
    decisionMap["GK"] = gkDecisions;
    decisionMap["CB"] = cbDecisions;
    decisionMap["LB"] = fbDecisions;
    decisionMap["RB"] = fbDecisions;
    decisionMap["LWB"] = wbDecisions;
    decisionMap["RWB"] = wbDecisions;
    decisionMap["CDM"] = cdmDecisions;
    decisionMap["CM"] = cmDecisions;
    decisionMap["CAM"] = camDecisions;
    decisionMap["LW"] = wDecisions;
    decisionMap["RW"] = wDecisions;
    decisionMap["ST"] = stDecisions;

}

void setupPassMap(){
    //manually create a graph of all chances of passing to another position

    std::unordered_map<string, int> gkPasses;
    gkPasses["GK"] = 0;
    gkPasses["CB"] = 100;
    gkPasses["LB"] = 50;
    gkPasses["RB"] = 50;
    gkPasses["LWB"] = 20;
    gkPasses["RWB"] = 20;
    gkPasses["CDM"] = 25;
    gkPasses["CM"] = 10;
    gkPasses["CAM"] = 5;
    gkPasses["LW"] = 2;
    gkPasses["RW"] = 2;
    gkPasses["ST"] = 1;

    std::unordered_map<string, int> cbPasses;
    cbPasses["GK"] = 50;
    cbPasses["CB"] = 75;
    cbPasses["LB"] = 25;
    cbPasses["RB"] = 25;
    cbPasses["LWB"] = 20;
    cbPasses["RWB"] = 20;
    cbPasses["CDM"] = 30;
    cbPasses["CM"] = 10;
    cbPasses["CAM"] = 5;
    cbPasses["LW"] = 2;
    cbPasses["RW"] = 2;
    cbPasses["ST"] = 1;

    std::unordered_map<string, int> lbPasses;
    lbPasses["GK"] = 10;
    lbPasses["CB"] = 30;
    lbPasses["LB"] = 0;
    lbPasses["RB"] = 5;
    lbPasses["LWB"] = 10;
    lbPasses["RWB"] = 3;
    lbPasses["CDM"] = 20;
    lbPasses["CM"] = 10;
    lbPasses["CAM"] = 5;
    lbPasses["LW"] = 40;
    lbPasses["RW"] = 1;
    lbPasses["ST"] = 3;
    
    std::unordered_map<string, int> rbPasses;
    rbPasses["GK"] = 10;
    rbPasses["CB"] = 30;
    rbPasses["LB"] = 5;
    rbPasses["RB"] = 0;
    rbPasses["LWB"] = 3;
    rbPasses["RWB"] = 10;
    rbPasses["CDM"] = 20;
    rbPasses["CM"] = 10;
    rbPasses["CAM"] = 5;
    rbPasses["LW"] = 1;
    rbPasses["RW"] = 40;
    rbPasses["ST"] = 3;

    std::unordered_map<string, int> lwbPasses;
    lwbPasses["GK"] = 5;
    lwbPasses["CB"] = 10;
    lwbPasses["LB"] = 5;
    lwbPasses["RB"] = 1;
    lwbPasses["LWB"] = 0;
    lwbPasses["RWB"] = 2;
    lwbPasses["CDM"] = 10;
    lwbPasses["CM"] = 20;
    lwbPasses["CAM"] = 22;
    lwbPasses["LW"] = 50;
    lwbPasses["RW"] = 1;
    lwbPasses["ST"] = 2;

    std::unordered_map<string, int> rwbPasses;
    rwbPasses["GK"] = 5;
    rwbPasses["CB"] = 10;
    rwbPasses["LB"] = 1;
    rwbPasses["RB"] = 5;
    rwbPasses["LWB"] = 2;
    rwbPasses["RWB"] = 0;
    rwbPasses["CDM"] = 10;
    rwbPasses["CM"] = 20;
    rwbPasses["CAM"] = 22;
    rwbPasses["LW"] = 1;
    rwbPasses["RW"] = 50;
    rwbPasses["ST"] = 2;

    std::unordered_map<string, int> cdmPasses;
    cdmPasses["GK"] = 2;
    cdmPasses["CB"] = 20;
    cdmPasses["LB"] = 5;
    cdmPasses["RB"] = 5;
    cdmPasses["LWB"] = 6;
    cdmPasses["RWB"] = 6;
    cdmPasses["CDM"] = 50;
    cdmPasses["CM"] = 80;
    cdmPasses["CAM"] = 35;
    cdmPasses["LW"] = 20;
    cdmPasses["RW"] = 20;
    cdmPasses["ST"] = 3;

    std::unordered_map<string, int> cmPasses;
    cmPasses["GK"] = 0;
    cmPasses["CB"] = 12;
    cmPasses["LB"] = 3;
    cmPasses["RB"] = 3;
    cmPasses["LWB"] = 5;
    cmPasses["RWB"] = 5;
    cmPasses["CDM"] = 50;
    cmPasses["CM"] = 70;
    cmPasses["CAM"] = 50;
    cmPasses["LW"] = 30;
    cmPasses["RW"] = 30;
    cmPasses["ST"] = 5;

    std::unordered_map<string, int> camPasses;
    camPasses["GK"] = 0;
    camPasses["CB"] = 1;
    camPasses["LB"] = 2;
    camPasses["RB"] = 2;
    camPasses["LWB"] = 4;
    camPasses["RWB"] = 4;
    camPasses["CDM"] = 40;
    camPasses["CM"] = 60;
    camPasses["CAM"] = 50;
    camPasses["LW"] = 40;
    camPasses["RW"] = 40;
    camPasses["ST"] = 10;

    std::unordered_map<string, int> lwPasses;
    lwPasses["GK"] = 0;
    lwPasses["CB"] = 0;
    lwPasses["LB"] = 10;
    lwPasses["RB"] = 1;
    lwPasses["LWB"] = 25;
    lwPasses["RWB"] = 1;
    lwPasses["CDM"] = 2;
    lwPasses["CM"] = 30;
    lwPasses["CAM"] = 20;
    lwPasses["LW"] = 0;
    lwPasses["RW"] = 5;
    lwPasses["ST"] = 15;

    std::unordered_map<string, int> rwPasses;
    rwPasses["GK"] = 0;
    rwPasses["CB"] = 0;
    rwPasses["LB"] = 1;
    rwPasses["RB"] = 10;
    rwPasses["LWB"] = 1;
    rwPasses["RWB"] = 30;
    rwPasses["CDM"] = 2;
    rwPasses["CM"] = 30;
    rwPasses["CAM"] = 20;
    rwPasses["LW"] = 5;
    rwPasses["RW"] = 0;
    rwPasses["ST"] = 15;

    std::unordered_map<string, int> stPasses;
    stPasses["GK"] = 0;
    stPasses["CB"] = 0;
    stPasses["LB"] = 0;
    stPasses["RB"] = 0;
    stPasses["LWB"] = 0;
    stPasses["RWB"] = 0;
    stPasses["CDM"] = 1;
    stPasses["CM"] = 5;
    stPasses["CAM"] = 50;
    stPasses["LW"] = 30;
    stPasses["RW"] = 30;
    stPasses["ST"] = 20;

    //add all maps to the passMap
    passMap["GK"] = gkPasses;
    passMap["CB"] = cbPasses;
    passMap["LB"] = lbPasses;
    passMap["RB"] = rbPasses;
    passMap["LWB"] = lwbPasses;
    passMap["RWB"] = rwbPasses;
    passMap["CDM"] = cdmPasses;
    passMap["CM"] = cmPasses;
    passMap["CAM"] = camPasses;
    passMap["LW"] = lwPasses;
    passMap["RW"] = rwPasses;
    passMap["ST"] = stPasses;
}

void setupDefendingMap(){
    defendingMap["GK"] = "ST";
    defendingMap["CB"] = "ST";
    defendingMap["LB"] = "LB";
    defendingMap["RB"] = "LW";
    defendingMap["LWB"] = "RWB";
    defendingMap["RWB"] = "LWB";
    defendingMap["CDM"] = "CAM";
    defendingMap["CM"] = "CM";
    defendingMap["CAM"] = "CDM";
    defendingMap["LW"] = "RB";
    defendingMap["RW"] = "LB";
    defendingMap["ST"] = "CB";
}

void setupPenaltyChanceMap(){
    penaltyChanceMap["GK"] = GK_PENALTY_CHANCE;
    penaltyChanceMap["CB"] = CB_PENALTY_CHANCE;
    penaltyChanceMap["LB"] = LB_PENALTY_CHANCE;
    penaltyChanceMap["RB"] = RB_PENALTY_CHANCE;
    penaltyChanceMap["LWB"] = LWB_PENALTY_CHANCE;
    penaltyChanceMap["RWB"] = RWB_PENALTY_CHANCE;
    penaltyChanceMap["CDM"] = CDM_PENALTY_CHANCE;
    penaltyChanceMap["CM"] = CM_PENALTY_CHANCE;
    penaltyChanceMap["CAM"] = CAM_PENALTY_CHANCE;
    penaltyChanceMap["LW"] = LW_PENALTY_CHANCE;
    penaltyChanceMap["RW"] = RW_PENALTY_CHANCE;
    penaltyChanceMap["ST"] = ST_PENALTY_CHANCE;
}

void setupMaps(){
    setupDecisionMap();
    setupPassMap();
    setupDefendingMap();
    setupPenaltyChanceMap();
}

string getRandomKeyFromMap(std::unordered_map<std::string, int> map, const std::string keys[], int numKeys){
    //add up all weights in the given map
    int total = 0;
    for(int i = 0; i < numKeys; i++){
        total += map[keys[i]];
    }
    //now generate a number and go back through the map until we know which position the number applies to
    int number = HelperMethods::generateRandom(0, total);
    total = 0;
    for(int i = 0; i < numKeys; i++){
        total += map[keys[i]];
        if(total > number) return keys[i];
    }
    //otherwise return the last position
    return keys[numKeys - 1];
}




void goal(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position, int penalty){
    teams[*teamIndexOnBall].scored(*onBall, lastPass, penalty);

    *teamIndexOnBall = !(*teamIndexOnBall);
    lastPass = nullptr;
    onBall = teams[*teamIndexOnBall].getRandomPlayer();
    position = onBall->getSpecificPosition();
}

void save(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position, bool saved, bool penalty){
    *teamIndexOnBall = !(*teamIndexOnBall);
    lastPass = nullptr;
    onBall = teams[*teamIndexOnBall].getPlayerFromPosition("GK");

    if(!onBall) onBall = teams[*teamIndexOnBall].getRandomPlayer();

    position = "GK";

    if(saved) onBall->makeSave();
    if(penalty) onBall->savePenalty();
}

void penalty(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    //decide which player should take
    onBall = teams[*teamIndexOnBall].getPenaltyTaker();
    cout << onBall->getName() + " taking the penalty\n";
    int shootingRating = onBall->getShooting();
    int shotRating = shootingRating + HelperMethods::generateRandom(0, shootingRating * PENALTY_SHOT_MODIFIER);
    shotRating = HelperMethods::generateRandom(shotRating * 0.5, shotRating);
    //cout << "chosen taker\n";

    Player* keeper = teams[!(*teamIndexOnBall)].getPlayerFromPosition("GK");
    int keeperRating = 10;
    if(keeper) keeperRating = keeper->getRating();
    cout << "keeper rating: " + to_string(keeperRating) + "\n";
    //cout << "found keeper\n";

    int saveRating = keeperRating + HelperMethods::generateRandom(0, keeperRating * PENALTY_SAVE_MODIFIER);
    saveRating = HelperMethods::generateRandom(saveRating * 0.5, saveRating);
    //cout << "got save rating\n";

    //figure out who gets credited with the assist
    if(onBall->getID() == lastPass->getID()){
        //cout << onBall->getName() + "\n";
        //cout << lastPass->getName() + "\n";
        //cout << "penalty taker is the penalty winner\n";
        lastPass = nullptr;
    }
    //cout << "comparing values now\n";

    cout << "Save rating: " + to_string(saveRating) + "\n";
    cout << "Shot rating: " + to_string(shotRating) + "\n";

    if(saveRating < shotRating){
        cout << "penalty scored\n";
        goal(teams, teamIndexOnBall, onBall, lastPass, position, 1);
    }
    else{
        cout << "penalty missed\n";
        teams[*teamIndexOnBall].missedPenalty(*onBall);
        save(teams, teamIndexOnBall, onBall, lastPass, position, true, true);
    }
}

void foul(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position, Player*& defender, int tackleRating){
    cout << "\nFOUL!\n";
    //check if it's a red or yellow card
    if(HelperMethods::generateRandom(0, 100) < RED_CARD_THRESHOLD){
        teams[!(*teamIndexOnBall)].getRedCard(defender->getID());
    }
    else if(HelperMethods::generateRandom(0, 100) < YELLOW_CARD_THRESHOLD){
        teams[!(*teamIndexOnBall)].getYellowCard(defender->getID());
    }

    //also check if it's a penalty!
    int penaltyChance = penaltyChanceMap[onBall->getSpecificPosition()];
    //cout << "chance of penalty: " + to_string(penaltyChance) + "\n";
    if(HelperMethods::generateRandom(0, 1000) < penaltyChance){
        lastPass = onBall;
        penalty(teams, teamIndexOnBall, onBall, lastPass, position);
    }
}

void block(Player*& blocker, Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    *teamIndexOnBall = !(*teamIndexOnBall);
    lastPass = nullptr;
    onBall = blocker;
    position = blocker->getSpecificPosition();

    blocker->makeDFCon();
}

void pass(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    cout << "Pass\n";

    //first check for interception from passer (passing vs defending) ------------------------------------
    //get the passer's passing ability (with some random variation)
    int passAbility = onBall->getPassing();
    int passRating = passAbility + HelperMethods::generateRandom(-passAbility * PASSING_VARIATION, passAbility * PASSING_VARIATION);

    //cout << "pass rating: " + to_string(passRating) + "\n";

    //cout << "pass ratings\n";

    //get position of the defender
    string passerPosition = position;
    string defenderPosition = defendingMap[passerPosition];
    Player* defender = teams[!(*teamIndexOnBall)].getPlayerFromPosition(defenderPosition);

    if(defender){
        //cout << "found defender\n";

        //get the defender's defending ability (with some random variation)
        int defendingAbility = defender->getDefending() * PASS_BLOCK_MULTI;
        //cout << "defending rating: " + to_string(defendingAbility) + "\n";

        if(defendingAbility > passRating){
            //cout << "pass blocked by " + defender->getName() + "\n";
            block(defender, teams, teamIndexOnBall, onBall, lastPass, position);
            return;
        }
    }
    

    //cout << "interception failed\n";


    //find a player to pass to randomly
    Player* player = nullptr;
    while(!player || player == onBall){
        player = teams[*teamIndexOnBall].getPlayerFromPosition(getRandomKeyFromMap(passMap[position], allPositions, numPositions));
    }
    //cout << "choose player to pass to\n";

    //then check for interception for person marking receiver (pace vs physicality) -----------------------------------------------
    //get the receiver's pace ability (with some random variation)
    int paceAbility = player->getPace();
    int paceRating = paceAbility + HelperMethods::generateRandom(-paceAbility * PACE_VARIATION, paceAbility * PACE_VARIATION);
    //cout << "pace\n";
    //cout << "pace rating: " + to_string(paceRating) + "\n";

    //get position of the defender
    string receiverPosition = player->getSpecificPosition();
    string intercepterPosition = defendingMap[receiverPosition];
    Player* intercepter = teams[!(*teamIndexOnBall)].getPlayerFromPosition(intercepterPosition);
    //cout << "defender\n";

    if(intercepter){
        //get the defender's physical ability (with some random variation)
        int physicalAbility = intercepter->getPhysical() * PASS_INTERCEPT_MULTI;
        //cout << "physical rating: " + to_string(physicalAbility) + "\n";

        if(physicalAbility > passRating){
            //cout << "pass intercepted by " + intercepter->getName() + "\n";
            block(intercepter, teams, teamIndexOnBall, onBall, lastPass, position);
            return;
        }
    }
    
    //cout << "pass succeeded\n";

    lastPass = onBall;
    position = player->getSpecificPosition();
    onBall = player;
}

void dribble(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    cout << "dribble\n";
    //check being tackled
    //get dribbling ability
    int dribbleAbility = onBall->getDribbling();
    int dribbleRating = dribbleAbility + HelperMethods::generateRandom(-dribbleAbility * DRIBBLING_VARIATION, dribbleAbility * DRIBBLING_VARIATION);

    //get position of the defender
    string dribblerPosition = position;
    string defenderPosition = defendingMap[dribblerPosition];
    Player* defender = teams[!(*teamIndexOnBall)].getPlayerFromPosition(defenderPosition);

    if(defender){
        //get defending ability
        int defendAbility = defender->getDefending();
        int defendRating = defendAbility + HelperMethods::generateRandom(-defendAbility * DEFENDING_VARIATION, defendAbility * DEFENDING_VARIATION);

        //get the chance of the dribble succeeding
        int total = dribbleRating + defendRating;
        double chanceToSucceed = (double)dribbleRating / (double)total;
        double roll = (double)rand() / RAND_MAX;

        if(roll > chanceToSucceed){
            cout << "Tackled!\n";
            block(defender, teams, teamIndexOnBall, onBall, lastPass, position);
            return;
        }
        else{
            if(HelperMethods::generateRandom(0, defendRating) < FOUL_THRESHOLD){
                foul(teams, teamIndexOnBall, onBall, lastPass, position, defender, defendRating);
            }
        }
    }

    //then increase position by 1 index
    dribbleAdvance(position);
}

void shoot(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    cout << "SHOOT!\n";
    //check how good the shot is
    //base value is how good they are at shooting (+- some random number)
    int playerShooting = onBall->getShooting();
    int shotRating = playerShooting;
    //cout << "shooting: " + to_string(playerShooting) + "\n";

    shotRating += HelperMethods::generateRandom(-(double)playerShooting * SHOOTING_VARIATION, (double)playerShooting * SHOOTING_VARIATION);
    //cout << "shot rating: " + to_string(shotRating) + "\n";

    //if the current player was passed to, increase shooting by half the other player's passing
    if(lastPass){
        //cout << "Passed to\n";
        int passValue = lastPass->getPassing();
        //cout << "pass value\n";
        passValue += HelperMethods::generateRandom(-passValue * SHOT_PASS_VARIATION, passValue * SHOT_PASS_VARIATION);
        //cout << "noise added\n";
        shotRating += passValue * SHOT_PASS_MODIFIER;
        //cout << "shot rating calculated\n";
    }
    //otherwise increase by the current player's pace
    else{
        //cout << "won it back\n";
        int paceValue = onBall->getPace();
        paceValue += HelperMethods::generateRandom(-paceValue * SHOT_PACE_VARIATION, paceValue * SHOT_PACE_VARIATION);
        shotRating += paceValue * SHOT_PACE_MODIFIER;
    }
    //cout << "shot rating: " + to_string(shotRating) + "\n";
    
    //check for defensive blocks
    vector<Player*> defenders = teams[!(*teamIndexOnBall)].getPlayersFromGeneralPosition("DEF");
    for(auto player : defenders){
        if(HelperMethods::generateRandom(0, 100) <= (DEF_BLOCK_CHANCE * 100) && 
        HelperMethods::generateRandom(player->getDefending() - (player->getDefending() * DEF_BLOCK_VARIATION), player->getDefending() + (player->getDefending() * DEF_BLOCK_VARIATION)) > shotRating){
            cout << "BLOCKED by " + player->getName() + "\n";
            block(player, teams, teamIndexOnBall, onBall, lastPass, position);
            return;
        }
    }

    //is it even on target?
    if(HelperMethods::generateRandom(0, 100) + (playerShooting * SHOOTING_ON_TARGET_SHOOTING_BONUS_PROPORTION) <= SHOOTING_ON_TARGET_THRESHOLD){
        cout << "OFF TARGET\n";
        save(teams, teamIndexOnBall, onBall, lastPass, position, false, false);
        return;
    }

    //now compare show rating to other team's gk ability
    int gkRating = teams[!(*teamIndexOnBall)].getPlayerFromPosition("GK")->getRating();
    int saveRating = gkRating;
    saveRating += HelperMethods::generateRandom(-gkRating * GK_NEGATIVE_VARIATION, gkRating * GK_POSITIVE_VARIATION);
    //cout << "Keeper rating: " + to_string(saveRating) + "\n";

    int total = shotRating + saveRating;
    double chanceToScore = (double)shotRating / (double)total;
    chanceToScore /= 2.0;
    //cout << "Chance to score: " + to_string(chanceToScore) + "\n";

    double roll = (double)rand() / RAND_MAX;
    //cout << "roll: " + to_string(roll) + "\n";

    if(roll < chanceToScore){
        cout << "\nGOAL!!!\n\n\n\n";
        goal(teams, teamIndexOnBall, onBall, lastPass, position, 0);
    }
    else{
        cout << "SAVE!\n";
        save(teams, teamIndexOnBall, onBall, lastPass, position, true, false);
    }
}

void simulateStep(Team teams[], int* teamIndexOnBall, Player*& onBall, Player*& lastPass, std::string& position){
    cout << onBall->getName() + " (" + position + ")\n";

    //every playing player, play one minute
    for(int i = 0; i < 2; i++){
        teams[i].teamPlayAMinute();
    }

    //player decision making:
    //first, use SPECIFIC position to look at the different options: passing, dribbling or shooting.
    string choice = getRandomKeyFromMap(decisionMap[position], allChoices, numChoices);
    
    //pass:
    //now look at the pass map: for every position, there is a pass map to every other position. choose one at random (if your team contains a player in that position)
    if(choice == "PASS") pass(teams, teamIndexOnBall, onBall, lastPass, position);

    //dribbling:
    //move one position up the position index
    else if(choice == "DRIBBLE") dribble(teams, teamIndexOnBall, onBall, lastPass, position);

    //shooting:
    //shoot at goal, chance of a block or deflection or saving
    else if(choice == "SHOOT") shoot(teams, teamIndexOnBall, onBall, lastPass, position);
}

int simulateMatch(Team teams[]) {
    //kickoff
    int teamIndexOnBall = HelperMethods::generateRandom(0, 2);
    Player* onBall = teams[teamIndexOnBall].getRandomPlayer();
    Player* lastPass = nullptr;
    std::string position = onBall->getSpecificPosition();
    int totalMinutes = 1;

    for(int part = 0; part < PARTS; part++){
        for(int minute = 1; minute <= MINUTES_PER_PART; minute++, totalMinutes++){
            cout << to_string(totalMinutes) + "'\n";
            for(int step = 0; step < STEPS_PER_MINUTE; step++){
                simulateStep(teams, &teamIndexOnBall, onBall, lastPass, position);
            }
            cout << "\n\n";
        }

        if(part == 1) continue;
        cout << "\n\n\n\n\nHALF TIME\n";
        cout << to_string(teams[0].getGoals()) + "-" + to_string(teams[1].getGoals()) + "\n\n\n\n\n";

        teamIndexOnBall = !teamIndexOnBall;
        onBall = teams[teamIndexOnBall].getRandomPlayer();
        lastPass = nullptr;
        position = onBall->getSpecificPosition();
    }

    //fulltime
    cout << "Match finished\n";
    cout << to_string(teams[0].getGoals()) + "-" + to_string(teams[1].getGoals()) + "\n";

    return 0;
}

