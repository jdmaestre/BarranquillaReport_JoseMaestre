package com.jdmaestre.uninorte.barranquillareport;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseTwitterUtils;

/**
 * Created by Jose on 22/11/2014.
 */
public class BarranquillaReportApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "wxRgc6TqQnkvj51xcf0uyl9IPBbxUnMpqXJrCZmj", "CGLz5cB0cgUNUwiZj9oSTYfVWSqXBn0dbIUHB7UT");
        ParseTwitterUtils.initialize("t5bax4CR4CGJT39LyOrU50mVl ", "Y7IDDe9kdoRScuTg8OP3SZp0DAZiV7tXKN8D62uX8tJ0Huwplu");


    }



}
