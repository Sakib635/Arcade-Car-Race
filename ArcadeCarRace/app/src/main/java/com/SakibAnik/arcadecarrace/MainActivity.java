package com.SakibAnik.arcadecarrace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //temporary edit for adding sound by soundPool;
    private SoundPool soundPool,resultPagesoundPool=null,gearloop;
    private int coinSound,crashSound,gearSound,introSound,lifeSound,overSound;
    private int soundStreamId,selectedInitialized,sound,gameon=0,pausecheck=0,stop=0;
    //temporary edit for adding sound by soundPool done;

    //elements
    private TextView scoreLabel, startLabel;
    private ImageView car, enemy,enemy2,enemy3,heart,coin,whitebar,whitebar1,whitebar2,whitebar3,life2,life3,actualcar,agun;

    //Size
    private int frameWidth,frameHeight;
    private int screenHeight,screenWidth;
    private int carSideSize,carBottomSize;

    //Score
    private  int score,lifePoints;
    //button
    Button leftControl,rightControl;

    //speed
    private int enemycarSpeed,enemycar2Speed,enemycar3Speed,heartSpeed, coinSpeed,roadSpeed,myCarSpeed;

    //position
    float carX,carY,enemyX,enemyY,enemy2X,enemy2Y,enemy3X,enemy3Y,heartX,heartY,coinX,coinY,whitebarY1,whitebarY2,whitebarY,whitebarY3;
    float agunHeight, agunWidth,carWidth,carHeight,enemyWidth,enemyHeight,actualCarWidth,actualCarHeight,enemy2Width,enemy2Height,enemy3Width,enemy3Height,heightAdjust,widthAdjust;
    //lane thik korer jonno
    float lane1,lane2,lane3,lane4,addlane;
    int ditectedLane,lan1=0,lan2=0,lan3=0,lan4=0;
    int laneBookedby[]={0,0,0,0,0};//ith lane kon ememy book korse
    int enemyBooked[]={0,0,0,0,0};// ith eneymy kon lane ta book korse
    float laneValue[]={0,0,0,0,0};// lane value
    int templane[]={0,0,0,0,0};//ith lane kon ememy book korse




    //Timer
    private Timer time= new Timer();
    private  Handler handle = new Handler();

    //Status check korte lagbe
    //private boolean actionFlag= false;
    private boolean startFlag= false;
    boolean rightOn=false,leftOn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);

        //link ID of image views with all respective variables
        scoreLabel= findViewById(R.id.scoreLabel);
        startLabel= findViewById(R.id.startLabel);
        enemy     = findViewById(R.id.car2);
        enemy2     = findViewById(R.id.enemy2);
        enemy3     = findViewById(R.id.enemy3);
        heart     = findViewById(R.id.heart);
        coin      = findViewById(R.id.coin);
        car= findViewById(R.id.carone);
        actualcar=findViewById(R.id.actualCar);
        agun = findViewById(R.id.agun);

        //
        selectedInitialized=StartAction.selectedCar;
        if(selectedInitialized==1){
            car=findViewById(R.id.carone);
            car.setVisibility(View.VISIBLE);
        }else if(selectedInitialized==2){
            car=findViewById(R.id.cartwo);
            car.setVisibility(View.VISIBLE);
        }else if(selectedInitialized==3){
            car=findViewById(R.id.carfour);
            car.setVisibility(View.VISIBLE);
        }else {
            car=findViewById(R.id.carthree);
            car.setVisibility(View.VISIBLE);
        }
        //

        life2=findViewById(R.id.life2);
        life3=findViewById(R.id.life3);
        lifePoints=3;  // initialization of total life chance

        coin.setVisibility(View.VISIBLE);
        enemy.setVisibility(View.VISIBLE);
        enemy2.setVisibility(View.VISIBLE);
        enemy3.setVisibility(View.VISIBLE);
        heart.setVisibility(View.VISIBLE);
        coin.setVisibility(View.VISIBLE);

        //Screen er size (height,width)
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenHeight= size.y;
        screenWidth= size.x;

        scoreLabel.setText("Score :" + score);


        // road er kaj (initial position of 4 running white bars)

        whitebar=findViewById(R.id.whiteBar);
        whitebarY=-200;
        whitebar.setY(whitebarY);

        whitebar1=findViewById(R.id.whiteBar1);
        whitebarY1=-750;
        whitebar1.setY(whitebarY1);

        whitebar2=findViewById(R.id.whiteBar2);
        whitebarY2=-1300;
        whitebar2.setY(whitebarY2);

        whitebar3=findViewById(R.id.whiteBar3);
        whitebarY3=-1850;
        whitebar3.setY(whitebarY3);

        //road er kaj done

        //speed set
        myCarSpeed=Math.round(screenWidth / 60.0f);//20
        heartSpeed=Math.round(screenHeight/35.0f);//highSpeed
        coinSpeed=Math.round((screenHeight/70.0f));
        enemycarSpeed=Math.round(screenHeight/60.0f);
        enemycar2Speed=Math.round(screenHeight/55.0f);//was 55
        enemycar3Speed=Math.round(screenHeight/65.0f);//was 65
        roadSpeed=Math.round(screenHeight/45.0f);
        //speed set done

        //soundPool  : for diffent sdk version initialization
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)//cntrol+B
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)//cntrol+B
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
            gearloop= new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_SYSTEM, 0);//cntrol+B
            gearloop=new SoundPool(6, AudioManager.STREAM_SYSTEM, 0);//cntrol+B
        }
        // load all sound to ids
        coinSound=soundPool.load(this,R.raw.coin,1);
        crashSound= soundPool.load(this,R.raw.crash,1);
        gearSound= gearloop.load(this,R.raw.gear,1);
        introSound= soundPool.load(this,R.raw.intro,1);
        lifeSound= soundPool.load(this,R.raw.life,1);
        overSound= soundPool.load(this,R.raw.over,1);
        //sound load done
        sound=StartAction.soundcheck;

    }


    public void changePosition(){
        if(stop==1)return;
        if(gameon==1)return;
        ///checking car and (enemy,heart,coin) collision----------
        collisionCheck();
        if(stop==1) {
            startLabel.setVisibility(View.VISIBLE);

            return;
        }
        ///-----------------------------rasta

        //===== shada dag-0
        whitebarY+=roadSpeed;
        if(whitebarY>screenHeight){
            whitebarY=-200;
        }
        whitebar.setY(whitebarY);

        //===== shada dag-1
        whitebarY1+=roadSpeed;
        if(whitebarY1>screenHeight){
            whitebarY1=-200;
        }
        whitebar1.setY(whitebarY1);

        //===== shada dag-2
        whitebarY2+=roadSpeed;
        if(whitebarY2>screenHeight){
            whitebarY2=-200;
        }
        whitebar2.setY(whitebarY2);

        //===== shada dag-3
        whitebarY3+=roadSpeed;
        if(whitebarY3>screenHeight){
            whitebarY3=-200;
        }
        whitebar3.setY(whitebarY3);

        //----------------------------------------rasta done

        ///----------------enemy1
        enemyY+=enemycarSpeed;


        if(enemyY>screenHeight+car.getX()){
            laneBookedby[enemyBooked[1]]=0;
            enemyY=-350;
           // enemyX=(float)Math.floor(Math.random() * (frameWidth - enemy.getWidth()));
            enemyX=laneRequest(1);
            enemy.setX(enemyX);//enemyx
        }

        enemy.setY(enemyY);
        ///---------------enemy1

        ///----------------enemy2
        enemy2Y+=enemycar2Speed;

        if(enemy2Y>screenHeight+car.getX()){
            laneBookedby[enemyBooked[2]]=0;
            enemy2Y=-500;
          //  enemy2X=(float)Math.floor(Math.random() * (frameWidth - enemy2.getWidth()));
            enemy2X=laneRequest(2);
            enemy2.setX(enemy2X);
        }

        enemy2.setY(enemy2Y);
        ///---------------enemy2

        ///----------------enemy3
        enemy3Y+=enemycar3Speed;

        if(enemy3Y>screenHeight+car.getX()){
            laneBookedby[enemyBooked[3]]=0;
            enemy3Y=-200;//1500
            enemy3X=laneRequest(3);
            enemy3.setX(enemy3X);
        }

        enemy3.setY(enemy3Y);
        ///---------------enemy3


        ///-------------------heart
        heartY+=heartSpeed;
        if(heartY>screenHeight+heart.getX()){
            heartY=-1*(screenHeight+1000);//10000
            heartX=(float)Math.floor(Math.random() * (frameWidth - heart.getWidth()));
        }
        heart.setX(heartX);
        heart.setY(heartY);
        ///---------------heart

        ///-------------------Coin
        coinY+=coinSpeed;
        if(coinY>screenHeight+coin.getX()){
            coinY=-30;
            coinX=(float)Math.floor(Math.random() * (frameWidth - coin.getWidth()));
        }
        coin.setX(coinX);
        coin.setY(coinY);
        ///--------------------Coin
        // position change of main car towords x-axis based on rightside click or leftside click;
        if (rightOn){
            //Touch kora thakle
            carX+=myCarSpeed;
        } else if(leftOn){
            //Touch shoray felle
            carX-=myCarSpeed;
        }

        if(carX<0){
            carX=0;
        }
        if(carX>frameWidth-carBottomSize){
            carX=frameWidth-carBottomSize;
        }

        car.setX(carX);

        scoreLabel.setText("Score :" + score); // update score on screen

    }

    //\/\/\/\/\/Method for checking car and (enemy,heart,coin) collision----------\/\/\/\/\/
    public void collisionCheck(){

        //\/\/\/\/\/-----------------------------------------COIN COLLISION---\/\/\/\/\/
        float coinCentreX = coinX + coin.getWidth()/2.0f;
        float coinCentreY = coinY + coin.getHeight()/2.0f;

        carY=car.getY();
        carSideSize= car.getHeight();

        if(carX<=coinCentreX && coinCentreX<=carX+carBottomSize && carY<=coinCentreY && coinCentreY<=carY+carSideSize){
            coinY=screenHeight+100.0f;
            score +=5;
            if(sound==1)coinPlay();//sound
        }
        //\/\/\/\/\/-----------------------------------------COIN COLLISION---\/\/\/\/\/

        //\/\/\/\/\/-----------------------------------------heart COLLISION---\/\/\/\/\/
        float heartCentreX = heartX + heart.getWidth()/2.0f;
        float heartCentreY = heartY + heart.getHeight()/2.0f;

        carY=car.getY();
        carX=car.getX();
        carSideSize= car.getHeight();

        if(carX<=heartCentreX && heartCentreX<=carX+carBottomSize && carY<=heartCentreY && heartCentreY<=carY+carSideSize){
            heartY=screenHeight+30000.0f;// starting point for heart;
            if(lifePoints==3){
                score+=5;// if life full, 5 exrta points given;
                if(sound==1)lifePlay();
            }else if(lifePoints==2){
                life3=findViewById(R.id.life3);
                life3.setVisibility(View.VISIBLE);lifePoints++;
                if(sound==1)lifePlay();
            }else if(lifePoints==1){
                life2=findViewById(R.id.life2);
                life2.setVisibility(View.VISIBLE);lifePoints++;// life heart visible, life point increase for code;
                if(sound==1)lifePlay();// life sound play
            }
        }
        //\/\/\/\/\/-----------------------------------------heart COLLISION---\/\/\/\/\/

        //\/\/\/\/\/-----------------------------------------Enemy1 COLLISION---\/\/\/\/\/
        if(enemy1col()){

            //onPause();
            if(lifePoints!=1){// get chance for extra life
                plasticCollison();
                if(stop==1)return;

            }else {   //game over
                if (time != null) {
                    time.cancel();
                    time = null;
                }
                if(sound==1)gearloop.autoPause();
                if(sound==1)overPlay();
                //Show Result Page---here---
                Intent intent = new Intent(getApplicationContext(), Result_Page.class);
                intent.putExtra("SCORE", score);
                startActivity(intent);
            }

        }
        //\/\/\/\/\/-----------------------------------------Enemy1 COLLISION---\/\/\/\/\/

        //\/\/\/\/\/-----------------------------------------Enemy3 COLLISION---\/\/\/\/\/
        if(enemy3col()){

           // onPause();
            if(lifePoints!=1){// get chance for extra life
                plasticCollison();
                if(stop==1)return;
            }else {   //game over
                if (time != null) {
                    time.cancel();
                    time = null;
                }
                if(sound==1)gearloop.autoPause();
                if(sound==1)overPlay();
                //Show Result Page---here---
                Intent intent = new Intent(getApplicationContext(), Result_Page.class);
                intent.putExtra("SCORE", score);
                startActivity(intent);
            }

        }
        //\/\/\/\/\/-----------------------------------------Enemy3 COLLISION---\/\/\/\/\/

        //\/\/\/\/\/-----------------------------------------Enemy2 COLLISION---\/\/\/\/\/
       if(enemy2col()){

            //onPause();
            if(lifePoints!=1){// get chance for extra life
                plasticCollison();
                if(stop==1)return;
            }else {   //game over
                if (time != null) {
                    time.cancel();
                    time = null;
                }
                if(sound==1)gearloop.autoPause();
                if(sound==1)overPlay();
                //Show Result Page---here---
                Intent intent = new Intent(getApplicationContext(), Result_Page.class);
                intent.putExtra("SCORE", score);
                startActivity(intent);
            }

        }
        //\/\/\/\/\/-----------------------------------------Enemy2 COLLISION---\/\/\/\/\/

    }

    public void plasticCollison(){
        gearloop.autoPause();
        startLabel.setText("New Chance");
        if(lifePoints==3){
            life3.setVisibility(View.INVISIBLE);
            if(sound==1)crashPlay();
        }else if(lifePoints==2){
            life2.setVisibility(View.INVISIBLE);
            if(sound==1)crashPlay();
        }
        lifePoints--;
        stop=1;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ontouchevenerviterkajTOhandefirstTabaKajKorena();
        return super.onTouchEvent(event);
    }
    public void ontouchevenerviterkajTOhandefirstTabaKajKorena(){ // onTouchEvent's acutal work does here (extra funtion for call from diffent part of the code)

        if(!startFlag){
            //  intro sound terminator which came from Result_Page
            if(Result_Page.check==1){
                if(sound==1)resultPagesoundPool=Result_Page.soundPool;
                if(sound==1)resultPagesoundPool.autoPause();
            }else if(StartAction.startActivitySoundCheck==1){
                if(sound==1)StartAction.startActivitySoundCheck=0;
                if(sound==1)resultPagesoundPool=StartAction.soundPoolFromStartActivity;
                if(sound==1)resultPagesoundPool.autoPause();
            }
            //terminate all sound of MainActivity class before game start
           // if(sound==1)allStop();
            //car gear sound play (for game vibe)
            if(sound==1)gearPlay();


            // game start
            startFlag=true;
            rightActualworkdone();// right button for game car control activate;
            leftActualWorkDone();// left button for game car control activate;


            //frame er WIDTH
           FrameLayout frame=findViewById(R.id.mainFrame);
           frameWidth= frame.getWidth();

            //car
           carX=car.getX();
           carBottomSize= car.getWidth();

           startLabel.setVisibility(View.INVISIBLE);  // tap to play  get invisible

            //road visibility
            whitebar.setVisibility(View.VISIBLE);
            whitebar1.setVisibility(View.VISIBLE);
            whitebar2.setVisibility(View.VISIBLE);
            whitebar3.setVisibility(View.VISIBLE);
            //road visibility done
            //enymy set

            coin.setY(screenHeight+50);
            enemyWidth=enemy.getWidth();
            addlane=screenWidth/(float)4.0;
            addlane-=enemyWidth;
            addlane/=(float)2.0;
            lane1=addlane;
            lane2=lane1+enemyWidth+addlane+addlane;
            lane3=lane2+enemyWidth+addlane+addlane;
            lane4=lane3+enemyWidth+addlane+addlane;
           enemyInitialpos();


            //enymy set

            //fixed var initialization
            actualCarHeight=actualcar.getHeight();
            actualCarWidth=actualcar.getWidth();
            carHeight=car.getHeight();
            carWidth=car.getWidth();
            enemyHeight=enemy.getHeight();
            enemyWidth=enemy.getWidth();
            enemy2Height=enemy2.getHeight();
            enemy2Width=enemy2.getWidth();
            enemy3Height=enemy3.getHeight();
            enemy3Width=enemy3.getWidth();
            widthAdjust=enemyWidth-actualCarWidth;
            heightAdjust=enemyHeight-actualCarHeight;
            widthAdjust/=2.0;
            heightAdjust/=2.0;
            agunHeight=agun.getHeight();
            agunWidth=agun.getWidth();
            agunHeight/=2.0;
            agunWidth/=2.0;
        //    Toast.makeText(this, ""+Float.toString(widthAdjust)+" "+Float.toString(heightAdjust), Toast.LENGTH_SHORT).show();
            //done


            time.schedule(new TimerTask() {
                @Override
                public void run() {
                    handle.post(new Runnable() {
                        @Override
                        public void run() {

                            changePosition();
                        }
                    }) ;
                }
            },0,20);

        }
    }
    public void enemyInitialpos(){
        enemyY=-100;enemyX=laneRequest(1);
        enemy2Y=-500;enemy2X=laneRequest(2);
        enemy3Y=-1000;enemy3X=laneRequest(3);
        enemy.setX(enemyX); enemy.setY(enemyY);
        enemy2.setX(enemy2X);enemy2.setY(enemy2Y);
        enemy3.setX(enemy3X);enemy3.setY(enemy3Y);
    }
    public float laneRequest(int n){
        int freeLane=0,mn=1,mx;int laneNum;
        for(int i=1,j=1;i<=4;i++){
            if(laneBookedby[i]==0) {
                freeLane++;
                if(i==1){
                     laneValue[j]=lane1;
                }else if(i==2){
                    laneValue[j]=lane2;
                }else if(i==3){
                    laneValue[j]=lane3;
                }else {
                    laneValue[j]=lane4;
                }
                templane[j]=i;
                j++;
            }
        }
        mx=freeLane;
        laneNum=(int) ((float) Math.random() * ((mx - mn) + 1) + mn);
        laneBookedby[templane[laneNum]]=n;
        enemyBooked[n]=templane[laneNum];
        return laneValue[laneNum];
    }

    public void rightCarControl(View v){// right button clicked

        rightActualworkdone();
    }
    @SuppressLint("ClickableViewAccessibility")//   call right button work from different part of code
    public void rightActualworkdone(){
        if(!startFlag) {
            ontouchevenerviterkajTOhandefirstTabaKajKorena();
        }
        rightControl=findViewById(R.id.rightButton);
        rightControl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(stop==1){
                        notonSuru();
                    }else {
                        rightOn = true;//pressed
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    rightOn=false;   //release
                }

                return true;
            }
        });
    }

    public void notonSuru(){
        agun.setVisibility(View.INVISIBLE);
        startLabel.setVisibility(View.INVISIBLE);
        for(int i=1;i<5;i++)laneBookedby[i]=0;
        enemyInitialpos();
        stop=0;
        gearloop.autoResume();
    }

    public void leftCarControl(View v){// left button clicked

       leftActualWorkDone();
    }

    @SuppressLint("ClickableViewAccessibility")// call left button work from different  part of  the code
    public void leftActualWorkDone(){
        //extra
        if(!startFlag)ontouchevenerviterkajTOhandefirstTabaKajKorena();
        //extra
        leftControl=findViewById(R.id.leftButton);
        leftControl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(stop==1){
                        notonSuru();
                    }else {
                        leftOn = true; //pressed
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    leftOn=false;//reliese
                }
                return true;
            }
        });
    }




////////////////////////////////////////////////////////////////////
    //--ALL SOUND EFFECTS & MUSICS--///////
//////////////////////////////////////////////////////////////////

    // functions for different sound play and pause
    protected void introPlay(){
        soundPool.play(introSound, 2, 2, 0, -1, 1);
    }
    protected void gearPlay(){
        gearloop.play(gearSound, 2, 2, 0, -1, 1);
    }
    protected void lifePlay(){
        soundPool.play(lifeSound, 1, 1, 0, 0, 1);
    }
    protected void coinPlay(){
        soundPool.play(coinSound, 1, 1, 0, 0, 1);
    }
    protected void crashPlay(){
        soundPool.play(crashSound, 1, 1, 0, 0, 1);
    }
    protected void allStop(){
        soundPool.autoPause();
    } // pause all sound play from this activity;
    protected void overPlay(){ soundPool.play(overSound, 1, 1, 0, 0, 1); }


    @Override
    protected void onPause() {
        super.onPause();
        if(startFlag){
            gameon=1;
            if(sound==1)gearloop.autoPause();
        }else if (Result_Page.check==1){
            if(sound==1)resultPagesoundPool=Result_Page.soundPool;
            if(sound==1)resultPagesoundPool.autoPause();
            pausecheck=1;
        }
        else if(StartAction.startActivitySoundCheck==1){
            if(sound==1)resultPagesoundPool=StartAction.soundPoolFromStartActivity;
            if(sound==1)resultPagesoundPool.autoPause();
            pausecheck=2;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(startFlag){
            gameon=0;
            if(sound==1)gearloop.autoResume();
        }else if (pausecheck==1){
            if(sound==1)resultPagesoundPool=Result_Page.soundPool;
            if(sound==1)resultPagesoundPool.autoResume();
            pausecheck=0;
        }else if(pausecheck==2){
            if(sound==1)resultPagesoundPool=StartAction.soundPoolFromStartActivity;
            if(sound==1)resultPagesoundPool.autoResume();
            pausecheck=0;
        }
        gameon=0;
    }


    ////////////////////////////////////////////////////////////////////
    //--ALL SOUND EFFECTS & MUSICS--///////  DONE DONE
//////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {///back button disable;
        // Do nothing
        return;
    }

    /*public void tryThenWork(float n){
        Toast.makeText(this, ""+Float.toString()+"   yes Kaj to kortese vai1", Toast.LENGTH_SHORT).show();
    }*/

    public boolean enemy1col(){
        //gari diye dan theke bame enemy cark dakka
        if((carX>=enemyX && carX<=(enemyX+enemyWidth-widthAdjust))&&(carY>=enemyY && carY<=(enemyY+enemyHeight-heightAdjust))) {
            agunDekha(carX,carY);
            return true;
        }
        //gari diye bam theke daner enemy cark dakka
        if(((carX+carWidth-(widthAdjust))>=enemyX && (carX+carWidth-(widthAdjust))<=(enemyX+enemyWidth-widthAdjust))&&(carY>=enemyY && carY<=(enemyY+enemyHeight-heightAdjust))) {
            agunDekha(carX+carWidth,carY);
            return true;
        }
        //garir  dan pacha diye bame enemy dakka
        if((carX>=enemyX && carX<=(enemyX+enemyWidth-widthAdjust))&&((carY+carHeight-heightAdjust)>=enemyY && (carY+carHeight-heightAdjust)<=(enemyY+enemyHeight-heightAdjust))) {
            agunDekha(carX,carY+carHeight);
            return true;
        }
        //garir basha diye bam theke daner enemy cark dakka
        if(((carX+carWidth-(widthAdjust))>=enemyX && (carX+carWidth-(widthAdjust))<=(enemyX+enemyWidth-widthAdjust))&&((carY+carHeight-heightAdjust)>=enemyY && (carY+carHeight-heightAdjust)<=(enemyY+enemyHeight-heightAdjust))) {
            agunDekha(carX+carWidth,carY+carHeight);
            return true;
        }
        return false;
    }

    public boolean enemy2col() {
        //gari diye dan theke bame enemy cark dakka
        if ((carX >= enemy2X && carX <= (enemy2X + enemy2Width - widthAdjust)) && (carY >= enemy2Y && carY <= (enemy2Y + enemy2Height - heightAdjust))) {
            agunDekha(carX, carY);
            return true;
        }
        //gari diye bam theke daner enemy cark dakka
        if (((carX + carWidth - (widthAdjust)) >= enemy2X && (carX + carWidth - (widthAdjust)) <= (enemy2X + enemy2Width - widthAdjust)) && (carY >= enemy2Y && carY <= (enemy2Y + enemy2Height - heightAdjust))) {
            agunDekha(carX + carWidth, carY);
            return true;
        }
        if ((carX >= enemy2X && carX <= (enemy2X + enemy2Width - widthAdjust)) && ((carY + carHeight - heightAdjust) >= enemy2Y && (carY + carHeight - heightAdjust) <= (enemy2Y + enemy2Height - heightAdjust))) {
            agunDekha(carX, carY + carHeight);
            return true;
        }
        //garir basha diye bam theke daner enemy cark dakka
        if (((carX + carWidth - (widthAdjust)) >= enemy2X && (carX + carWidth - (widthAdjust)) <= (enemy2X + enemy2Width - widthAdjust)) && ((carY + carHeight - heightAdjust) >= enemy2Y && (carY + carHeight - heightAdjust) <= (enemy2Y + enemy2Height - heightAdjust))){
            agunDekha(carX+carWidth,carY+carHeight);
            return true;
        }
        return false;

    }

    public boolean enemy3col() {
        //gari diye dan theke bame enemy cark dakka
        if ((carX >= enemy3X && carX <= (enemy3X + enemy3Width - widthAdjust)) && (carY >= enemy3Y && carY <= (enemy3Y + enemy3Height - heightAdjust))) {
            agunDekha(carX, carY);
            return true;
        }
        //gari diye bam theke daner enemy cark dakka
        if (((carX + carWidth - (widthAdjust)) >= enemy3X && (carX + carWidth - (widthAdjust)) <= (enemy3X + enemy3Width - widthAdjust)) && (carY >= enemy3Y && carY <= (enemy3Y + enemy3Height - heightAdjust))) {
            agunDekha(carX + carWidth, carY);
            return true;
        }

        if ((carX >= enemy3X && carX <= (enemy3X + enemy3Width - widthAdjust)) && ((carY + carHeight - heightAdjust) >= enemy3Y && (carY + carHeight - heightAdjust) <= (enemy3Y + enemy3Height - heightAdjust))) {
            agunDekha(carX, carY + carHeight);
            return true;
        }
        //garir basha diye bam theke daner enemy cark dakka
        if (((carX + carWidth - (widthAdjust)) >= enemy3X && (carX + carWidth - (widthAdjust)) <= (enemy3X + enemy3Width - widthAdjust)) && ((carY + carHeight - heightAdjust) >= enemy3Y && (carY + carHeight - heightAdjust) <= (enemy3Y + enemy3Height - heightAdjust))){
            agunDekha(carX+carWidth,carY+carHeight);
            return true;
        }
        return false;

    }
    public void agunDekha(float x,float y){
        x-=agunWidth;
        y-=agunHeight;
        agun.setX(x);
        agun.setY(y);
        agun.setVisibility(View.VISIBLE);
    }
}























