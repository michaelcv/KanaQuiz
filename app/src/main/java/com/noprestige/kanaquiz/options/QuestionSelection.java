package com.noprestige.kanaquiz.options;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.noprestige.kanaquiz.R;
import com.noprestige.kanaquiz.themes.ThemeManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class QuestionSelection extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ThemeManager.setTheme(this);
        setContentView(R.layout.activity_tabbed_screen);

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new QuestionSelectionPager(getSupportFragmentManager(), this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        //TODO: Make this check more dynamic, accounting for screen width and actual tab width
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ThemeManager.permissionRequestReturn(this, permissions, grantResults);
    }
}
