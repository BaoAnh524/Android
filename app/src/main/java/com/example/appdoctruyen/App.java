package com.example.appdoctruyen;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

public class App extends Application {
    public static ArrayList<Comic> comics;
    public static ArrayList<Comic> getComic(Context context){
        if (comics == null) {
            DatabaseHelper db = new DatabaseHelper(context);
            comics = db.getAllComic();
        }
        return comics;

    }
 }
