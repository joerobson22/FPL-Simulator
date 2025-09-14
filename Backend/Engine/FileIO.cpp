#include "FileIO.h"
#include "Constants.h"
#include <fstream>
#include <iostream>
#include <string>
#include <vector>
#include <utility>

using namespace std;

//FILE INPUT FUNCTIONS
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

void readInputFile(Team teams[]){
    string line;
    ifstream reader(inputFilePath);

    int teamPlayers = -1;

    int i = -1;
    while(getline(reader, line)){
        i++;
        //different specific cases
        if(i == FILE_SEED_LINE){
            int seed = stoi(line);
            srand(static_cast<unsigned int>(seed));
            continue;
        }
        else if(i == FILE_HOME_TEAM_LINE){
            teams[0] = Team(line);
            continue;
        }
        else if(i == FILE_AWAY_TEAM_LINE){
            teams[1] = Team(line);
            continue;
        }
        else if(line == FILE_HOME_PLAYERS_START_LINE) {
            teamPlayers = 0;
            continue;
        }
        else if(line == FILE_AWAY_PLAYERS_START_LINE){
            teamPlayers = 1;
            continue;
        } 

        //otherwise the line is a new player's information
        //so find every comma, then pass that substring into parseKeyValuePair
        vector<pair<string, string>> keyValuePairs;
        
        int subStrStart = 0;
        int j = 0;
        for(j = 0; j < line.size(); j++){
            char c = line.at(j);

            if(c == ',' || j == line.size()){
                keyValuePairs.push_back(parseKeyValuePair(line.substr(subStrStart, j - subStrStart)));
                subStrStart = j + 1;
            }
        }
        keyValuePairs.push_back(parseKeyValuePair(line.substr(subStrStart, j - subStrStart)));

        Player p(keyValuePairs);
        teams[teamPlayers].addPlayer(p);
    }
}
//---------------------------------

//FILE OUTPUT FUNCTIONS
string getGoalScorersString(Team teams[]){
    string output = "";
    output += teams[0].getGoalScorersDictionary();

    if(output != "" && teams[1].getGoalScorersDictionary() != "") output += ",";
    output += teams[1].getGoalScorersDictionary();

    return output;
}

string getAssistersString(Team teams[]){
    string output = "";
    output += teams[0].getAssistersDictionary();

    if(output != "" && teams[1].getAssistersDictionary() != "") output += ",";
    output += teams[1].getAssistersDictionary();

    return output;
}

string getCleanSheetsString(Team teams[]){
    string output = "";
    output += teams[0].getCleanSheetDictionary(teams[1].getGoals() == 0);

    if(output != "" && teams[1].getCleanSheetDictionary(teams[0].getGoals() == 0) != "") output += ",";
    output += teams[1].getCleanSheetDictionary(teams[0].getGoals() == 0);

    return output;
}

string get60MinString(Team teams[]){
    string output = "";
    output += teams[0].get60MinDictionary();

    if(output != "" && teams[1].get60MinDictionary() != "") output += ",";
    output += teams[1].get60MinDictionary();

    return output;
}

string get3SavesString(Team teams[]){
    string output = "";
    output += teams[0].get3SavesDictionary();

    if(output != "" && teams[1].get3SavesDictionary() != "") output += ",";
    output += teams[1].get3SavesDictionary();

    return output;
}

string getDFConString(Team teams[]){
    string output = "";
    output += teams[0].getDFConDictionary();

    if(output != "" && teams[1].getDFConDictionary() != "") output += ",";
    output += teams[1].getDFConDictionary();

    return output;
}

string getYellowCardString(Team teams[]){
    string output = "";
    output += teams[0].getYellowCardDictionary();

    if(output != "" && teams[1].getYellowCardDictionary() != "") output += ",";
    output += teams[1].getYellowCardDictionary();

    return output;
}

string getRedCardString(Team teams[]){
    string output = "";
    output += teams[0].getRedCardDictionary();

    if(output != "" && teams[1].getRedCardDictionary() != "") output += ",";
    output += teams[1].getRedCardDictionary();

    return output;
}

string getPenaltyScorersString(Team teams[]){
    string output = "";
    output += teams[0].getPenaltyGoalScorersDictionary();

    if(output != "" && teams[1].getPenaltyGoalScorersDictionary() != "") output += ",";
    output += teams[1].getPenaltyGoalScorersDictionary();

    return output;
}

string getPenaltyMissesString(Team teams[]){
    string output = "";
    output += teams[0].getPenaltyMissesDictionary();

    if(output != "" && teams[1].getPenaltyMissesDictionary() != "") output += ",";
    output += teams[1].getPenaltyMissesDictionary();

    return output;
}

string getPenaltySavesString(Team teams[]){
    string output = "";
    output += teams[0].getPenaltySavesDictionary();

    if(output != "" && teams[1].getPenaltySavesDictionary() != "") output += ",";
    output += teams[1].getPenaltySavesDictionary();

    return output;
}

void writeToOutputFile(Team teams[]){
    teams[0].transferPlayingPlayersToPlayers();
    teams[1].transferPlayingPlayersToPlayers();

    ofstream output(outputFilePath);

    output << to_string(teams[0].getGoals()) + "\n";
    output << to_string(teams[1].getGoals()) + "\n";
    output << "GOAL SCORERS," + getGoalScorersString(teams) + "\n";
    output << "ASSISTERS," + getAssistersString(teams) + "\n";
    output << "CLEAN SHEETS," + getCleanSheetsString(teams) + "\n";
    output << "60 MINS," + get60MinString(teams) + "\n";
    output << "3 SAVES," + get3SavesString(teams) + "\n";
    output << "DFCON," + getDFConString(teams) + "\n";
    output << "YELLOW CARDS," + getYellowCardString(teams) + "\n";
    output << "RED CARDS," + getRedCardString(teams) + "\n";
    output << "PENALTY GOALS," + getPenaltyScorersString(teams) + "\n";
    output << "PENALTY MISSES," + getPenaltyMissesString(teams) + "\n";
    output << "PENALTY SAVES," + getPenaltySavesString(teams) + "\n";

    output.close();
}
//----------------------------------
