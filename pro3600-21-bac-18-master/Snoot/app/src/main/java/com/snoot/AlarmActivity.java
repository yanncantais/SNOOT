package com.snoot;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity implements SensorEventListener {

    //Déclaration des variables
    int tHour = -1;
    int tMinute = -1;
    SharedPreferences mPreferences2;

    TextView tvTimer;
    Button buttonSetAlarm;
    TextView affichagePas;
    Button desacAlarm;
    int reward = 5;
    public static final String BUNDLE="BUNDLE";

    //Déclaration des variables relatives au compteur de pas
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean isSensorPresence = false;
    int stepCount = 0;
    float stepInitial;
    int objectifDePas = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        // Initialisation de shared pref
        mPreferences2=getPreferences(MODE_PRIVATE);
        //Assignation aux iddentifiants
        affichagePas = findViewById(R.id.pas);
        tvTimer = findViewById(R.id.timer);
        buttonSetAlarm = findViewById(R.id.prog_alarm);
        desacAlarm = findViewById(R.id.desac_alarm);
        sensorManager = (SensorManager)
                this.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Button podometreButton = findViewById(R.id.podometre_button);
        Button shopButton = findViewById(R.id.shop_button);
        Button mainButton = findViewById(R.id.acc_button);

        tvTimer.setText("Pas d'alarme");

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(AlarmActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });

        podometreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent podometreActivity = new Intent(AlarmActivity.this, PodometreActivity.class);
                startActivity(podometreActivity);
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shopActivity = new Intent(AlarmActivity.this, ShopActivity.class);
                startActivity(shopActivity);
            }
        });

        desacAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tHour = -1;
                tMinute = -1;
                tvTimer.setText("Pas d'alarme");
            }
        });

        //Choix de l'heure de programmation avec l'affichage du time picker interractif
        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialisation du timePicker
                TimePickerDialog timePickerDialog = new TimePickerDialog(

                        AlarmActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Initialisation de l'horaire

                                tHour = hourOfDay;
                                tMinute = minute;

                                //Initialisation du calendrier
                                Calendar calendar = Calendar.getInstance();

                                //Réglage horraire
                                calendar.set(0, 0, 0, tHour, tMinute);

                                //Réglage de l'alarme programée
                                tvTimer.setText(android.text.format.DateFormat.format("hh:mm aa", calendar));
                            }
                        },12, 0, true
                );
                //Affichage de l'horaire sélectionnée
                timePickerDialog.updateTime(tHour, tMinute);
                timePickerDialog.show();
                if(tHour >= 0){
                    tvTimer.setText(tHour + ":" + tMinute);
                }
            }
        });

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //Mise à jour de la variable contenant l'heure
                String currentTime = DateFormat.getTimeInstance().format(new Date());

                //On enlève l'attribut des secondes (pas utile pour nous)
                currentTime = currentTime.substring(0, 5);
                String cT = currentTime;
                int heure = Integer.parseInt(currentTime.substring(0,2));
                int minute = Integer.parseInt(cT.substring(3,5));


                if ((heure == tHour) && (minute == tMinute)){
                    alarmPod();
                }

                else{

                }

            }
        }, 0, 10000);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (stepCount < 1) {
            stepInitial = event.values[0];
            stepCount = 1;
        }
        else {
            stepCount = (int)(event.values[0]-stepInitial);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void alarmPod() {
        sensorManager.registerListener(this, sensor,
                sensorManager.SENSOR_DELAY_FASTEST);

        final Ringtone sonnerie = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        //int pasIni = stepCount + 0;
        int nb_pas = stepCount;
        int restant;

        while(nb_pas < objectifDePas){
            sonnerie.play();
            nb_pas = stepCount;
            restant = objectifDePas - nb_pas;
            affichagePas.setText(String.valueOf(restant));
        }
        Intent intent = new Intent();
        intent.putExtra(BUNDLE,reward);
        setResult(RESULT_OK,intent);
        finish();

    }

}