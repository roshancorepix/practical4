package com.example.practical4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Chronometer simpleChronometer;
    private LinearLayout ll_laps;
    private Button btn_lap;
    private boolean isChronometerStart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       simpleChronometer = findViewById(R.id.simpleChronometer);
       ll_laps = findViewById(R.id.ll_laps);
       btn_lap = findViewById(R.id.btn_lap);

        registerForContextMenu(simpleChronometer);
        btn_lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChronometerStart) {
                    showElapsedTime();
                }
            }
        });
    }


    private void showElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - simpleChronometer.getBase();


        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(elapsedMillis),
                TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedMillis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)));

        ll_laps.addView(createNewTextView(hms));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.setHeaderTitle("Select The Action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId()==R.id.start){
            int stoppedMilliseconds = 0;

            String chronoText = simpleChronometer.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 2) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                        + Integer.parseInt(array[1]) * 1000;
            } else if (array.length == 3) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                        + Integer.parseInt(array[1]) * 60 * 1000
                        + Integer.parseInt(array[2]) * 1000;
            }

            simpleChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
            simpleChronometer.start();
            isChronometerStart = true;
        }
        else if(item.getItemId()==R.id.stop){
            simpleChronometer.stop();
            showElapsedTime();
            isChronometerStart = false;
        }else if(item.getItemId()==R.id.reset){
            simpleChronometer.setBase(SystemClock.elapsedRealtime());
            simpleChronometer.stop();
            ll_laps.removeAllViews();
            isChronometerStart = false;
        }else{
            return false;
        }
        return true;
    }

    private TextView createNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText("New LAP: " + text);
        return textView;
    }
}