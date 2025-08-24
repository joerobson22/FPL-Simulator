package Java;

import java.util.Map;

public class PointLookupTable {
    private static Map<String, Integer> pointsForGoals = Map.of(
        "GK", 6,
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

    public static int getPointsForGoal(String position){
        return pointsForGoals.get(position);
    }

    public static int getPointsForCleanSheet(String position){
        return pointsForCleanSheet.get(position);
    }

    public static int getPointsForAssist(){
        return pointsForAssist;
    }
}
