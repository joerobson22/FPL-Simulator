package Frontend;

import java.util.HashMap;
import java.util.Map;

public class PositionLookupTable {
    private static String[] gk = {"gk"};
    private static String[] def = {"cb", "lb", "rb", "lwb", "rwb"};
    private static String[] mid = {"cdm", "cm", "cam", "lw", "rw"};
    private static String[] att = {"st"};

    private static final Map<String, String> positionMap = new HashMap<>();

    static {
        positionMap.put("GK", "GK");
        positionMap.put("LWB", "LWB");
        positionMap.put("RWB", "RWB");
        positionMap.put("LB", "LB");
        positionMap.put("RB", "RB");
        positionMap.put("LCB", "CB");
        positionMap.put("RCB", "CB");
        positionMap.put("CB", "CB");
        positionMap.put("LDM", "CDM");
        positionMap.put("CDM", "CDM");
        positionMap.put("RDM", "CDM");
        positionMap.put("LM", "LW");
        positionMap.put("LCM", "CM");
        positionMap.put("CM", "CM");
        positionMap.put("RCM", "CM");
        positionMap.put("RM", "RW");
        positionMap.put("LAM", "CAM");
        positionMap.put("RAM", "CAM");
        positionMap.put("CAM", "CAM");
        positionMap.put("LW", "LW");
        positionMap.put("RW", "RW");
        positionMap.put("LS", "ST");
        positionMap.put("RS", "ST");
        positionMap.put("LF", "ST");
        positionMap.put("CF", "ST");
        positionMap.put("RF", "ST");
        positionMap.put("ST", "ST");
        positionMap.put("SUB", "SUB");
        positionMap.put("RES", "RES");
    }
    //FINAL POSITION MAP:
    //gk
    //cb, lb, cb, lwb, rwb
    //cdm, cm, cam, lw, rw
    //st

    public static String getMappedPosition(String position){
        return positionMap.get(position);
    }


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
