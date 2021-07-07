package com.SakibAnik.arcadecarrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.Objects;

public class Result_Page extends AppCompatActivity {
    //temporary edit for adding sound by soundPool;
    public static SoundPool soundPool=null;
    public static int introSound,check=0,sound;
    private int soundStreamId;
    //temporary edit for adding sound by soundPool done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_result__page);

        TextView scoreList = findViewById(R.id.scoreLabel);
        TextView highScoreList = findViewById(R.id.highScoreLabel);

        int score = getIntent().getIntExtra("SCORE",0);
        scoreList.setText(score+"");

        //----Storing high score----
        SharedPreferences sharedPreferences= getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("HIGH SCORE",0);

        if(score>highScore){

            // update high score
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HIGH SCORE",score);
            editor.apply();
            highScoreList.setText("HIGH SCORE : " + score);

        } else{
            highScoreList.setText("HIGH SCORE : " + highScore);
        }

        //temporary edit for sound by SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_SYSTEM, 0);
        }
        introSound= soundPool.load(this,R.raw.intro,1);
        sound=StartAction.soundcheck;

        //temporary edit for sound by SoundPool done


    }

    public void  tryAgain(View view){
        check=1;
        if(sound==1)soundPool.play(introSound, 1, 1, 0, -1, 1);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));


    }
    public void allStop(){
        soundPool.autoPause();
    }

    @Override
    public void onBackPressed() {///back button disable;
        // Do nothing
        return;
    }

    public void backtohome(View v){
        Intent intent = new Intent(getApplicationContext(), StartAction.class);
        startActivity(intent);
    }
    public void appclose(View v){
        finishAffinity();
        System.exit(0);
    }

}