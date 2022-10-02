package com.snoot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //Déclaration des variables
    Button alarmButton;
    Button podometreButton;
    Button shopButton;
    private ImageView player;
    int action;
    TextView affichageScore;
    private static final String BUNDLE = "BUNDLE";
    public static final int ALARM_ACTIVITY_CODE = 1;
    public static final int PODOM_ACTIVITY_CODE = 2;

    public static SharedPreferences mPreferences;
    private int coins;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ALARM_ACTIVITY_CODE == requestCode) {
            int coins = data.getIntExtra(MainActivity.BUNDLE, 0);
            final SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
            int coins2 = coins + settings.getInt("COINS", 0);; // on additionne le score récupéré au score précédent

            affichageScore.setText(String.valueOf(coins2));

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("COINS", coins2);
            editor.commit();


            // -> pour accéder au montant, utiliser mPreferences.edit.getintt("score")

        }
        else if (PODOM_ACTIVITY_CODE==requestCode){
            int coins = data.getIntExtra(MainActivity.BUNDLE, 0);
            int coins2 = coins + getPreferences(MODE_PRIVATE).getInt("coins", 0); // on additionne le score récupéré au score précédent

            mPreferences.edit().putInt("Portefeuille", coins).apply();
            affichageScore.setText(String.valueOf(coins2));

            SharedPreferences mPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putInt("coins", coins2);
            editor.apply();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = (ImageView) findViewById(R.id.image1);
        //ASssignation des variables à leur id
        alarmButton = findViewById(R.id.alarm_button2);
        podometreButton = findViewById(R.id.podometre_button2);
        shopButton = findViewById(R.id.shop_button2);
        //mPreferences=getPreferences(MODE_PRIVATE); // on devra peut etre mettre autre chose que MODE_PRIVATE pour avoir accès aux preferences dans les autres
        //int coins = getPreferences(MODE_PRIVATE).getInt("coins", 0);
        final SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        coins = settings.getInt("COINS", 0);//
        action = settings.getInt("ACTION", 1);
        affichageScore = (TextView)findViewById(R.id.score);
        affichageScore.setText(String.valueOf(coins));

        player.setImageResource(getResources().getIdentifier("player"+action+"a", "drawable", getPackageName()));

        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alarmActivity = new Intent(MainActivity.this, AlarmActivity.class);
                //startActivity(alarmActivity);
                startActivityForResult(alarmActivity,ALARM_ACTIVITY_CODE);
            }
        });

        podometreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent podometreActivity = new Intent(MainActivity.this, PodometreActivity.class);
                //startActivity(podometreActivity);
                startActivityForResult(podometreActivity,PODOM_ACTIVITY_CODE);
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shopActivity = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(shopActivity);
            }
        });
    }

    private SurfaceHolder getHolder() {
        return null;
    }
}
