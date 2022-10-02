package com.snoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PodometreActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean isSensorPresent = false;
    private TextView mStepsSinceReboot;
    int stepCount = 0;
    float stepInitial;
    public static final String BUNDLE="BUNDLE";
    int checkCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podometre);
        Button b = (Button) findViewById(R.id.button_start_count);
        b.setOnClickListener(this);

        mStepsSinceReboot =
                (TextView)findViewById(R.id.stepssincereboot);

        mSensorManager = (SensorManager)
                this.getSystemService(Context.SENSOR_SERVICE);
        mSensor =
                mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Button alarmButton = findViewById(R.id.alarm_button);
        Button shopButton = findViewById(R.id.shop_button);
        Button mainButton = findViewById(R.id.acc_button);
        Button recButton = findViewById(R.id.recup_rec);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(PodometreActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });

        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alarmActivity = new Intent(PodometreActivity.this, AlarmActivity.class);
                startActivity(alarmActivity);
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shopActivity = new Intent(PodometreActivity.this, ShopActivity.class);
                startActivity(shopActivity);
            }
        });

        recButton.setOnClickListener(new View.OnClickListener() { // BOUTON RECUPERER REWARDD
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra(BUNDLE,(int)(checkCount*0.01));
                setResult(RESULT_OK,intent);
                finish();

            }
        });

    }
    @Override
    public void onClick(View view){
        Context context = getApplicationContext();
        CharSequence text = "On commence à compter les pas";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        mStepsSinceReboot.setText("0");


    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepCount < 1){
            stepInitial = event.values[0];
            stepCount = 1;
            mStepsSinceReboot.setText(String.valueOf(1));
        }
        else{
            mStepsSinceReboot.setText(String.valueOf(event.values[0]-stepInitial));
            checkCount = (int)(event.values[0]-stepInitial);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}