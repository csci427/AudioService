package com.example.cobyg.lineintest;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Audio audioObj = new Audio();
        final AudioManager am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        final TextView statusLabel = (TextView) findViewById(R.id.txtStatus);
        final TextView micStatusLabel = (TextView) findViewById(R.id.txtMicStatus);
        statusLabel.setText(audioObj.getStatus());

        final Button startButton = (Button) findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                audioObj.open();
                statusLabel.setText(audioObj.getStatus());
            }
        });

        final Button endButton = (Button) findViewById(R.id.btnEnd);
        endButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                audioObj.close();
                statusLabel.setText(audioObj.getStatus());
            }
        });

        final Button testButton = (Button) findViewById(R.id.btnTest);
        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Boolean tStat = am.isWiredHeadsetOn();
                String finalMStat = "";
                if (tStat == true){
                    finalMStat = "Mic is connected";
                }
                else{
                    finalMStat = "Mic is not connected";
                }
               micStatusLabel.setText(finalMStat);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}