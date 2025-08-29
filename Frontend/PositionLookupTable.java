package Frontend;

public class PositionLookupTable {
    private static String[] gk = {"gk"};
    private static String[] def = {"lwb", "rwb", "lb", "rb", "lcb", "cb", "rcb"};
    private static String[] mid = { "ldm", "cdm", "rdm",  "lm", "lcm", "cm", "rcm", "rm", "lam", "cam", "ram", "lw", "rw"};
    private static String[] att = {"ls", "rs", "lf", "cf", "rf", "st"};

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

    public static int getPositionIndex(String specificPosition){
        int i = 0;
        for(String position : gk){
            if(specificPosition.toUpperCase().equals(position.toUpperCase())) return i;
            i++;
        }

        for(String position : def){
            if(specificPosition.toUpperCase().equals(position.toUpperCase())) return i;
            i++;
        }

        for(String position : mid){
            if(specificPosition.toUpperCase().equals(position.toUpperCase())) return i;
            i++;
        }

        for(String position : att){
            if(specificPosition.toUpperCase().equals(position.toUpperCase())) return i;
            i++;
        }

        return -1;
    }
}
