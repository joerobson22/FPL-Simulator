#include "HelperMethods.h"
#include "Constants.h"
#include <string>

bool HelperMethods::validCleanSheetPosition(std::string position){
    return position == "GK" || position == "DEF" || position == "MID";
}

bool HelperMethods::validDFConPosition(std::string position){
    return position == "DEF" || position == "MID";
}

int HelperMethods::getDFConForPosition(std::string position){
    if(!validDFConPosition(position)) return DFCON_OTHER;

    if(position == "DEF") return DFCON_DEFENDER;
    else return DFCON_MIDFIELDER;
}

int HelperMethods::generateRandom(int min, int maxExclusive)
{
    if (maxExclusive <= min) return min;
    int range = maxExclusive - min;
    return min + (rand() % range);
}

int HelperMethods::getTeamIndex(Team teams[], Player p){
    for(int i = 0; i < 2; i++){
        for(int j = 0; j < 11; j++){
            if(p.getID() == teams[i].getPlayers()[j].getID()) return i;
        }
    }
    return -1;
}