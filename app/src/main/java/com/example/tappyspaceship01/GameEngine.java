package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // Android debug variables
    final static String TAG="DINO-RAINBOWS";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;




    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------

    // represent the TOP LEFT CORNER OF THE GRAPHIC
    int playerXposition;
    int playerYposition;
    Bitmap playerImage;
    Rect playerHitbox;

    int itemXposition;
    int itemYposition;
    Bitmap itemImage;
    Rect itemHitbox;







    // ----------------------------
    // ## GAME STATS
    // ----------------------------


    int lives = 5;
    int score = 0;


    public GameEngine(Context context, int w, int h) {
        super(context);

        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;

        this.printScreenInfo();
        this.playerImage = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.dino32);

        this.playerXposition = 1450;
        this.playerYposition = 475;
// 1. create the hitbox
        this.playerHitbox = new Rect(1450,
                475,
                1450+playerImage.getWidth(),
                475+playerImage.getHeight()
        );


        this.itemImage = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.rainbow32);

        this.itemXposition = 50;
        this.itemYposition = 100;

        this.itemHitbox = new Rect(50,100,50+itemImage.getWidth(),100+itemImage.getHeight());



    }
//


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnPlayer() {
        //@TODO: Start the player at the left side of screen

    }
    private void spawnEnemyShips() {


        Random random = new Random();

        //@TODO: Place the enemies in a random location

    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------

    public void updatePositions() {





        this.itemXposition = this.itemXposition + 20;

        this.itemHitbox.left = this.itemXposition;
        this.itemHitbox.top = this.itemYposition;
        this.itemHitbox.right = this.itemXposition + this.itemImage.getWidth();
        this.itemHitbox.bottom = this.itemYposition + this.itemImage.getHeight();

        this.playerYposition = this.playerYposition - 10;
        this.playerHitbox.left = this.playerXposition;
        this.playerHitbox.top = this.playerYposition;
        this.playerHitbox.right = this.playerXposition + this.playerImage.getWidth();
        this.playerHitbox.bottom = this.playerYposition + this.playerImage.getHeight();

        if(playerMoving == "UP") {
            this.playerYposition = this.playerYposition - 20;

            if (this.playerYposition >= this.screenHeight) {
                this.playerYposition = this.playerYposition;
                fingerAction = "";

            }

        }
        if(playerMoving == "DOWN") {
            this.playerYposition = this.playerYposition + 20;

            if (this.playerYposition <= 0) {
                this.playerYposition = this.playerYposition;
                fingerAction = "";

            }
        }


        if(fingerAction.contentEquals("TOP")){
            this.playerYposition = this.playerYposition - 20;
        }
        else if(fingerAction.contentEquals("BOTTOM")){
            this.playerYposition = this.playerYposition + 20;
        }

    // detecting collison
        if(
                (itemXposition + 10) >= (this.playerXposition) &&
                        itemYposition >= this.playerYposition &&
                        itemYposition <= this.playerYposition + 20

        ){
            this.score = this.score + 1;
        }





    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.GRAY);



            // DRAW THE PLAYER HITBOX
            // ------------------------
            // 1. change the paintbrush settings so we can see the hitbox
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            canvas.drawBitmap(playerImage,playerXposition,playerYposition,paintbrush);
            canvas.drawRect(this.playerHitbox,paintbrush);

            canvas.drawBitmap(itemImage,itemXposition,itemYposition,paintbrush);
            canvas.drawRect(this.itemHitbox,paintbrush);




            paintbrush.setColor(Color.RED);
            canvas.drawRect(50,200,1400,230,paintbrush);
            canvas.drawRect(50,400,1400,430,paintbrush);
            canvas.drawRect(50,600,1400,630,paintbrush);
            canvas.drawRect(50,800,1400,830,paintbrush);

            paintbrush.setTextSize(35);
            paintbrush.setColor(Color.BLACK);
            canvas.drawText("Lives : " + lives,
                    1420,
                    50,
                    paintbrush
            );
            paintbrush.setTextSize(35);
            canvas.drawText("Score : " + score,1420,850,paintbrush);







            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(120);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------


    String fingerAction = "";
    String playerMoving = "up";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {

            float fingerXposition = event.getX();
            float fingerYposition = event.getY();
            Log.d(TAG,"User Touched :" + fingerXposition + "," + fingerYposition);

            int halfScreen = this.screenWidth / 2;
            if(fingerXposition <= halfScreen) {
                fingerAction = "Top";
            }
            else if (fingerXposition > halfScreen) {
                fingerAction = "BOTTOM";
            }

        }
        else if (userAction == MotionEvent.ACTION_UP) {

        }

        return true;
    }
}
