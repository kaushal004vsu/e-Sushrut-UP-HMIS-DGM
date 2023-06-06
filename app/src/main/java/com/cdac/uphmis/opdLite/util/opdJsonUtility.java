package com.cdac.uphmis.opdLite.util;

public class opdJsonUtility {

    public static String sideCodetoSideName(String sideCode) {

        if (sideCode.equalsIgnoreCase("0")) {
            return "Side";
        } else if (sideCode.equalsIgnoreCase("1")) {
            return "NR";
        } else if (sideCode.equalsIgnoreCase("2")) {
            return "Left";
        } else if (sideCode.equalsIgnoreCase("3")) {
            return "Right";
        } else if (sideCode.equalsIgnoreCase("4")) {
            return "Bilateral";
        } else {
            return "Side";
        }


    }

    public static String durationCodeToName(String durationCode) {

        if (durationCode.equalsIgnoreCase("1")) {
            return "Day/s";
        } else if (durationCode.equalsIgnoreCase("2")) {
            return "Week/s";
        } else if (durationCode.equalsIgnoreCase("3")) {
            return "Month/s";
        } else if (durationCode.equalsIgnoreCase("4")) {
            return "Year/s";
        }  else {
            return "Day/s";
        }


    }

    public static String sideNameToCode(String sideName) {

        if (sideName.equalsIgnoreCase("Side")) {
            return "0";
        } else if (sideName.equalsIgnoreCase("NR")) {
            return "1";
        } else if (sideName.equalsIgnoreCase("Left")) {
            return "2";
        } else if (sideName.equalsIgnoreCase("Right")) {
            return "3";
        } else if (sideName.equalsIgnoreCase("Bilateral")) {
            return "4";
        } else {
            return "Side";
        }


    }


    public static  String getTypeNameFromCode(String typeCode)
    {
        String typeName = "Provisional";
        if (typeCode.equalsIgnoreCase("11")) {
            typeName = "Provisional";
        } else if (typeCode.equalsIgnoreCase("12")) {
            typeName = "Differential";
        } else if (typeCode.equalsIgnoreCase("14")) {
            typeName = "Final";
        }
        return typeName;
    }
}
