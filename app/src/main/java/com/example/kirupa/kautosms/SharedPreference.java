package com.example.kirupa.kautosms;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Display;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;




public class SharedPreference {


    public static Context context;

    SharedPreference(Context context) {
        this.context = context;
    }

    public String[] getPhoneNumbers() {

        //Retrieve the values
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("phonePref", Context.MODE_PRIVATE);
        String jsonText = sharedPreferences.getString("phoneNumber", null);
        String[] text = gson.fromJson(jsonText, String[].class);  //EDIT: gso to gson
        return text;

    }
    public void savePhoneNumber(List<String> phoneNumber) {
        Log.d("savePhoneNumber","savePhoneNumber " +phoneNumber);
        Gson gson = new Gson();
        List textList = new ArrayList<>();
        textList.addAll(phoneNumber);
        String jsonText = gson.toJson(textList);
        SharedPreferences sharedPreferences = context.getSharedPreferences("phonePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phoneNumber", jsonText);

        editor.apply();
    }
}