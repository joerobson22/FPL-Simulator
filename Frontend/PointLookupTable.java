package Frontend;

import java.util.Map;

public class PointLookupTable {
    private static Map<String, Integer> pointsForGoals = Map.of(
        "GK", 10,
        "DEF", 6,
        "MID", 5,
        "ATT", 4
    );

    private static Map<String, Integer> pointsForCleanSheet = Map.of(
        "GK", 4,
        "DEF", 4,
        "MID", 1,
        "ATT", 0
    );
    private static int pointsForAssist = 3;

    private static Map<String, Integer> pointsFor2GoalsConceded = Map.of(
        "GK", -1,
        "DEF", -1
    );

    private static int pointsForTransfer = -4;
    private static int pointsFor3Saves = 1;
    private static int pointsForDFCon = 2;
    private static int pointsForYellowCard = -1;
    private static int pointsForRedCard = -1;
    private static int pointsForPenaltyMiss = -2;
    private static int pointsForPenaltySave = 5;


    //2 goals conceded  : -1 (only gk and def)  -  
    //3 saves           : 1                     !
    //penalty save      : 5
    //penalty miss      : -2
    //yellow card       : -1
    //red cards         : -3
    //own goal          : -2
    //dfcon             : 2                     !
    //minutes played??  : 1 -> 2                -
    //bonus points      : 1-3       

    public static int getPointsForGoal(String position){
        return pointsForGoals.get(position);
    }

    public static int getPointsForCleanSheet(String position){
        return pointsForCleanSheet.get(position);
    }

    public static int getPointsForAssist(){
        return pointsForAssist;
    }

    public static int getPointsForGoalsConceded(String position, int numGoals){
        if(numGoals == 0 || position.equals("ATT") || position.equals("MID")) return 0;

        int multi = numGoals / 2;
        return pointsFor2GoalsConceded.get(position) * multi;
    }

    public static int getPointsForTransfer(){
        return pointsForTransfer;
    }

    public static int getPointsFor3Saves(){
        return pointsFor3Saves;
    }

    public static int getPointsForDFCon(){
        return pointsForDFCon;
    }

    public static int getPointsForYellowCard(){
        return pointsForYellowCard;
    }

    public static int getPointsForRedCard(){
        return pointsForRedCard;
    }

    public static int getPointsForPenaltyMiss(){
        return pointsForPenaltyMiss;
    }

    public static int getPointsForPenaltySave(){
        return pointsForPenaltySave;
    }
}
