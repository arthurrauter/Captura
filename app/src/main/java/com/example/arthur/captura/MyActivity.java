package com.example.arthur.captura;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class MyActivity extends ActionBarActivity {


    private TextView matchOutput;
    private Button switchRunModeButton;
    private static final String TAG = MyActivity.class.getSimpleName();
    private String gameName = new String();
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void selectPretwa(View view) {
        size = 2;
        gameName ="pretwa";
        runGame(view);
    }

    public void selectAlquerque(View view) {
        size = 5;
        gameName = "alquerque";
        runGame(view);

    }

    public void createFelli(View view) {
        size = 5;
        gameName = "felli";
        runGame(view);
    }

    public void createSerpent(View view) {
        size = 5;
        gameName = "serpent";
        runGame(view);
    }


    public void createCheckers(View view) {
        size = 5;
        gameName = "checkers";
        runGame(view);

    }

    public void coreRun(View view) {
        Match newMatch = new Match(size, gameName);
        List<String> matchLog = newMatch.capture.movesHistory;
        matchOutput = (TextView) findViewById(R.id.logOutput);
        matchOutput.setText("");
        for (String s : matchLog) {
            matchOutput.append(s);
            matchOutput.append("   ");
        }
        matchOutput.append("The Winner is: " + newMatch.winner);
    }

    private void runGame(View view) {
        if (coreRunOnly) coreRun(view);
        else runWithGraphics(view);
    }

    public void runWithGraphics(View view) {
       setContentView(new DrawingSurface(this, gameName));
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }

    private boolean coreRunOnly = true;
    public void switchRunMode(View view) {
        switchRunModeButton = (Button) findViewById(R.id.switchRunModeButton);

        String t = new String();
        if (coreRunOnly) {
            coreRunOnly = false;
            t = "Graphic Mode ON / Core Run Only OFF";
        }
        else {
            coreRunOnly = true;
            t = "Graphic Mode OFF / Core Run Only ON";
        }
        switchRunModeButton.setText(t);

    }

    public void selectSeries(View view) {
        coreRunOnly = false;
        gameName = "series";
        runGame(view);
    }
}
