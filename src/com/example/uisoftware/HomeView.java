package com.example.uisoftware;

import com.example.uisoftware.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HomeView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_view, menu);
        return true;
    }
}
