package com.SakibAnik.arcadecarrace;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.Objects;

public class StartAction extends AppCompatActivity {

    public static SoundPool soundPoolFromStartActivity=null;
    public static int introSound,startActivitySoundCheck=0,selectedCar=1,soundcheck=1,StartActionIntro=0;

    public ImageView car1,car2,car3,car4,soundSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
       setContentView(R.layout.activity_start_action2);

        //soundPool  : for diffent sdk version initialization
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)//cntrol+B
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)//cntrol+B
                    .build();
            soundPoolFromStartActivity = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPoolFromStartActivity = new SoundPool(6, AudioManager.STREAM_SYSTEM, 0);//cntrol+B
        }
        // load all sound to ids
        introSound=soundPoolFromStartActivity.load(this,R.raw.intro,1);
        selectedCar=1;

        car1=findViewById(R.id.slmycar1);
        car2=findViewById(R.id.slmycar2);
        car3=findViewById(R.id.slmycar3);
        car4=findViewById(R.id.slmycar4);
        soundSetting=findViewById(R.id.soundSettings);
        soundSetting.setImageResource(R.drawable.unmute);
    }
    public void  start(View view){

        startActivitySoundCheck=1;
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
        if(soundcheck==1)introPlay();
    }

    public void  dev(View view){
        Intent intents = new Intent(getApplicationContext(), developers.class);
        startActivity(intents);
    }

    public void  gamePlay(View view){
        Intent intent = new Intent(getApplicationContext(), gamePlay.class);
        startActivity(intent);
    }

    protected void introPlay(){
        soundPoolFromStartActivity.play(introSound, 2, 2, 0, -1, 1);
    }

    public void selectCar1(View v){
        removeSelectmark();
        selectedCar=1;
        setSelectmark();
    }
    public void selectCar2(View v){
        removeSelectmark();
        selectedCar=2;
        setSelectmark();
    }
    public void selectCar3(View v){
        removeSelectmark();
        selectedCar=3;
        setSelectmark();
    }
    public void selectCar4(View v){
        removeSelectmark();
        selectedCar=4;
        setSelectmark();
    }

    public void removeSelectmark(){
        if(selectedCar==1)car1.setVisibility(View.INVISIBLE);
        else if(selectedCar==2)car2.setVisibility(View.INVISIBLE);
        else if(selectedCar==3)car3.setVisibility(View.INVISIBLE);
        else if(selectedCar==4)car4.setVisibility(View.INVISIBLE);
    }

    public void setSelectmark(){
        if(selectedCar==1)car1.setVisibility(View.VISIBLE);
        else if(selectedCar==2)car2.setVisibility(View.VISIBLE);
        else if(selectedCar==3)car3.setVisibility(View.VISIBLE);
        else if(selectedCar==4)car4.setVisibility(View.VISIBLE);
    }


    public void soundSettingsChange(View v){
        soundcheck++;
        soundcheck%=2;
        if(soundcheck==0){
            soundSetting.setImageResource(R.drawable.mute);
        }else {
            soundSetting.setImageResource(R.drawable.unmute);
        }
    }

}