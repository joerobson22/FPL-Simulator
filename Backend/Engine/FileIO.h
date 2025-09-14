#ifndef FILEIO_H
#define FILEIO_H

#include "Team.h"
#include <string>

// File input functions
std::pair<std::string, std::string> parseKeyValuePair(std::string section);
void readInputFile(Team teams[]);

// File output functions
void writeToOutputFile(Team teams[]);
std::string getGoalScorersString(Team teams[]);
std::string getAssistersString(Team teams[]);
std::string getCleanSheetsString(Team teams[]);
std::string get60MinString(Team teams[]);
std::string get3SavesString(Team teams[]);
std::string getDFConString(Team teams[]);
std::string getYellowCardString(Team teams[]);
std::string getRedCardString(Team teams[]);
std::string getPenaltyScorersString(Team teams[]);
std::string getPenaltyMissesString(Team teams[]);
std::string getPenaltySavesString(Team teams[]);

#endif