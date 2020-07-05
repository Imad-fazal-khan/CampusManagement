package com.campus.management;

public class Campus {

    public static String CAMPUS_NAME;

    public Campus() {
    }

    public static String getCampusName() {
        return CAMPUS_NAME;
    }

    public static void setCampusName(String campusName) {
        CAMPUS_NAME = campusName;
    }
}
