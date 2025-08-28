package Frontend;

public class PositionLookupTable {
    private static String[] gk = {"gk"};
    private static String[] def = {"lwb", "rwb", "lb", "lcb", "cb", "rcb", "rb"};
    private static String[] mid = {"lw", "rw", "lam", "cam", "ram", "lm", "lcm", "cm", "rcm", "rm", "ldm", "cdm", "rdm"};
    private static String[] att = {"ls", "st", "rs", "lf", "cf", "rf"};

    public static String getGeneralPosition(String specificPosition){
        for(String position : gk){
            if(specificPosition.toUpperCase().equals(position.toUpperCase())) return "GK";
        }

        for(String position : def){
            if(specificPosition.toUpperCase().equals(position.toUpperCase())) return "DEF";
        }

        for(String position : mid){
            if(specificPosition.toUpperCase().equals(position.toUpperCase())) return "MID";
        }

        for(String position : att){
            if(specificPosition.toUpperCase().equals(position.toUpperCase())) return "ATT";
        }

        return "NULL";
    }
}
