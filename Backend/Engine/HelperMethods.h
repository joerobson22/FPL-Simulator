#ifndef HELPERMETHODS_H
#define HELPERMETHODS_H

#include "Team.h"
#include "Player.h"

#include <string>

class HelperMethods{
    public:
    static bool validCleanSheetPosition(std::string position);
    static bool validDFConPosition(std::string position);
    static int getDFConForPosition(std::string position);
    static int generateRandom(int min, int maxExclusive);
    static int getTeamIndex(Team teams[], Player p);
};


#endif