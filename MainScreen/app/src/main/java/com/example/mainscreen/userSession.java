package com.example.mainscreen;

/**
 * Created by Vaaiibhav on 22-Dec-17.
 */

public class userSession {
    String usrname;
    int  usrcoins;

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public String getUsrname() {
        return usrname;
    }

    public int getUsrcoins() {
        return usrcoins;
    }


    private static final userSession ourInstance = new userSession();

    public static userSession getInstance() {
        return ourInstance;
    }
}
