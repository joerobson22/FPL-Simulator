package Frontend;

import java.util.Map;

public class ManagerApprovalLookupTable {
    private static Map<String, Integer> approvalForGoal = Map.of(
        "GK", 3,
        "DEF", 5,
        "MID", 8,
        "ATT", 10
    );

    private static Map<String, Integer> approvalForAssist = Map.of(
        "GK", 2,
        "DEF", 3,
        "MID", 10,
        "ATT", 8
    );

    private static Map<String, Integer> approvalForNoGA = Map.of(
        "GK", 0,
        "DEF", -1,
        "MID", -8,
        "ATT", -10
    );


    
    private static Map<String, Integer> approvalForCleanSheet = Map.of(
        "GK", 10,
        "DEF", 8,
        "MID", 2,
        "ATT", 0
    );

    private static Map<String, Integer> approvalForGoalConceded = Map.of(
        "GK", -2,
        "DEF", -2,
        "MID", -1,
        "ATT", 0
    );




    public static int getApprovalForGoal(String pos){ return approvalForGoal.get(pos); }
    public static int getApprovalForAssist(String pos){ return approvalForAssist.get(pos); }
    public static int getApprovalForCleanSheet(String pos){ return approvalForCleanSheet.get(pos); }

    public static int getApprovalForBlank(String pos){ return approvalForNoGA.get(pos); }
    public static int getApprovalForGoalConceded(String pos){ return approvalForGoalConceded.get(pos); }
}
