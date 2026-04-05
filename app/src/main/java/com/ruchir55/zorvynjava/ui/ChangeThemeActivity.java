package com.ruchir55.zorvynjava.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.ruchir55.zorvynjava.databinding.ActivityChangeThemeBinding;

public class ChangeThemeActivity extends AppCompatActivity {
    ActivityChangeThemeBinding changeThemeBinding;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeThemeBinding = ActivityChangeThemeBinding.inflate(getLayoutInflater());
        setContentView(changeThemeBinding.getRoot());

        changeThemeBinding.toolbar2.setNavigationOnClickListener(v -> {
            finish();
        });

        changeThemeBinding.mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            sharedPreferences = this.getSharedPreferences("com.ruchir55.zorvynjava", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("switch", true);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("switch", false);
            }
            editor.apply();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = this.getSharedPreferences("com.ruchir55.zorvynjava", Context.MODE_PRIVATE);
        changeThemeBinding.mySwitch.setChecked(sharedPreferences.getBoolean("switch",false));
    }


}