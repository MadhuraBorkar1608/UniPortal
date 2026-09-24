package com.uniportal;

import com.uniportal.util.DBConnection;

public class TestDBInit {
    public static void main(String[] args) {
        System.out.println("Initializing DB...");
        DBConnection.getConnection();
        System.out.println("Done!");
    }
}
