package com.example.rita_ola.sens;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tv_steps;
    private TextView tv_calories;
    private TextView count_donut;
    SensorManager sensorManager;
    Sensor sensorLight;
    boolean running = false;
    MediaPlayer mediaPlayer;
    ScrollView layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_steps = (TextView) findViewById(R.id.tv_steps);
        tv_calories = (TextView) findViewById(R.id.tv_calories);
        count_donut = (TextView) findViewById(R.id.count_donut);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.eye);
        layout = (ScrollView) findViewById(R.id.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (countSensor != null || lightSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double a = 2000;
        int b = 1;
        int c = 10;
        int d = (int) sensorEvent.values[0];
        int e = (int) ((int) d * 0.05);
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (running) {
                tv_steps.setText(String.valueOf(d));
                tv_calories.setText(String.valueOf(e));
                if (sensorEvent.values[0]%c==0) {
                    Toast.makeText(this, "Good job fattie!", Toast.LENGTH_LONG).show();
                }
                if (sensorEvent.values[0]%a==0) {
                   count_donut.setText(String.valueOf(b));
              }
            }
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            if (sensorEvent.values[0] > 40 && running) {
                mediaPlayer.start();
                layout.setBackgroundColor(Color.BLUE);
            }
            else if(sensorEvent.values[0] < 40 && sensorEvent.values[0] > 20 && running) {
                layout.setBackgroundColor(Color.MAGENTA);
            }
            else if(sensorEvent.values[0] < 20 && running){
                layout.setBackgroundColor(Color.BLACK);
            }
            }
        }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pause:
                pause();
                return true;
            case R.id.startBtn:
                start();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void pause() {
        Toast.makeText(getApplicationContext(), getString(R.string.pause), Toast.LENGTH_LONG).show();
        onPause();
    }

    private void start() {
        onResume();
        Toast.makeText(getApplicationContext(), getString(R.string.resume), Toast.LENGTH_LONG).show();
    }
        }
