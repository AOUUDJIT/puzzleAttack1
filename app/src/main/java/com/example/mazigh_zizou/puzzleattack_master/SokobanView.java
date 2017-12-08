package com.example.mazigh_zizou.puzzleattack_master;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SokobanView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    // Declaration des images
    private Bitmap yellow;
    private Bitmap withe;
    private Bitmap vide;
    private Bitmap orange;
    private Bitmap blue;
    private Bitmap win;
    private Bitmap background;
    private Bitmap timer;
    private long beginTimer, endTimer;
    boolean loadNextLevel  = false;
    boolean replay         = false;
    boolean displayDialog = true;
    p8_Sokoban class1;
    p8_Sokoban class2;


    int xDown = 0;
    int yDown = 0;
    int   nbTouch = 0;
    Paint text = new Paint();
    int   level = 0;


    private Resources mRes;
    private Context mContext;

    int[][] carte;

    int carteTopAnchor;
    int carteLeftAnchor;

    static final int carteWidth    = 6;
    static final int carteHeight   = 7;
    static final int sizeCST = 53;

    static final int CST_vide  = 0;
    static final int CST_brique1 = 1;// yellow
    static final int CST_brique2 = 2;//orange
    static final int CST_brique3 = 3;//bleu


    int[][][] ref = {
            {
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
                    {CST_brique2, CST_brique2, CST_vide, CST_brique2, CST_vide, CST_vide}
            }, {
                    {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                    {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                    {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                    {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                    {CST_vide,CST_vide,CST_vide,CST_brique2,CST_vide,CST_vide},
                    {CST_vide,CST_vide,CST_vide,CST_brique2,CST_vide,CST_vide},
                    {CST_brique1,CST_brique1,CST_brique2,CST_brique1,CST_vide,CST_vide}
             }, {
                    {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                    {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                    {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                    {CST_vide,CST_vide,CST_vide,CST_vide,CST_brique2,CST_vide},
                    {CST_vide,CST_vide,CST_vide,CST_vide,CST_brique3,CST_vide},
                    {CST_vide,CST_vide,CST_brique2,CST_brique2,CST_brique3,CST_vide},
                    {CST_vide,CST_brique1,CST_brique1,CST_brique3,CST_brique1,CST_vide}
            }
    };


    private boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;


    public SokobanView(Context context, AttributeSet attrs) {

        super(context, attrs);
        class1   = new p8_Sokoban();
        class2 = new p8_Sokoban();

        holder = getHolder();
        holder.addCallback(this);

        mContext = context;
        mRes     = mContext.getResources();

        yellow = BitmapFactory.decodeResource(mRes, R.drawable.yellow);
        //sky    = BitmapFactory.decodeResource(mRes, R.drawable.sky);
        orange = BitmapFactory.decodeResource(mRes, R.drawable.orange);
        blue   = BitmapFactory.decodeResource(mRes, R.drawable.blue);
        vide  = BitmapFactory.decodeResource(mRes, R.drawable.empty);
        win    = BitmapFactory.decodeResource(mRes, R.drawable.win);
        timer = BitmapFactory.decodeResource(mRes, R.drawable.timer);
        background   = BitmapFactory.decodeResource(mRes,R.drawable.background);
        withe   = BitmapFactory.decodeResource(mRes, R.drawable.withe);

        // creation of the thread
        cv_thread = new Thread(this);

        setFocusable(true);
    }

    public void startTimer(){
        beginTimer = System.currentTimeMillis();
    }
    public void stopTimer(){
        endTimer = System.currentTimeMillis();
    }
    public double getTimer() {
        return ((endTimer - beginTimer) / 1000);
    }




    // load a level from our table
    private void loadlevel()
    {
        for (int i = 0; i < carteWidth; i++)
        {
            for (int j = 0; j < carteHeight; j++)
            {
                carte[j][i] = ref[level][j][i];
            }
        }
        startTimer();
    }

    // initialisation du jeu
    public void initparameters() {
        carte = new int[carteHeight][carteWidth];
        loadlevel();
        carteTopAnchor = getHeight()-371;
        carteLeftAnchor = (getWidth()) / carteWidth;

        if ((cv_thread != null) && (!cv_thread.isAlive()))
        {
            cv_thread.start();
            Log.e("-TEST-", "cv_thread.start()");
        }
    }


   private void paintWin(Canvas canvas) {
        canvas.drawBitmap(win, carteLeftAnchor + 40, carteTopAnchor+ 10, null);
    }
    private void paintTimer(Canvas canvas) {
        canvas.drawBitmap(timer, getWidth()-40, 10 , null);
    }

    private void paintWithe(Canvas canvas) {
        canvas.drawBitmap(withe, -10, -10, null);
    }


    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas)
    {
        for (int i = 0; i < carteHeight; i++)
        {
            for (int j = 0; j < carteWidth; j++)
            {
                switch (carte[i][j])
                {
                    case CST_vide:
                        canvas.drawBitmap(vide, j * sizeCST, carteTopAnchor + i * sizeCST, null);
                        break;
                    case CST_brique2:
                        canvas.drawBitmap(orange, j * sizeCST, carteTopAnchor + i * sizeCST, null);
                        break;
                    case CST_brique1:
                        canvas.drawBitmap(yellow, j * sizeCST, carteTopAnchor + i * sizeCST, null);
                        break;
                    case CST_brique3:
                        canvas.drawBitmap(blue, j * sizeCST, carteTopAnchor + i * sizeCST, null);
                        break;
                }
            }
        }
    }

    //dessin du fond
    private void paintBackground(Canvas canvas) {
        canvas.drawBitmap(background, 1, 1, null);
    }
    private void paintMessage(Canvas canvas)
    {
        int time = (int) getTimer();
        text.setTextSize(15);
        text.setStyle(Paint.Style.FILL_AND_STROKE);
        if (nbTouch == 2)
        {
            if (isWon())
            {
                if(level == 2)
                {   paintWithe(canvas);
                    paintWin(canvas); }
            }
            else
            {
                class2.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                        //nb_touch == 2 est détécté, mais isWon pas encore.
                        //afin d'éviter que AlertDialog s'affiche quand on à gagné le niveau 3 :
                        //on met un sleep
                        //puis on revérifie le isWon
                        try{
                            Thread.sleep(100);
                        }catch(Exception e){}

                        if (displayDialog && !isWon()) {
                            AlertDialog();
                            displayDialog = false;
                        }
                    }
                });

                if (replay) {
                    nbTouch = 0;
                    loadlevel();
                    displayDialog = false;
                    replay = false;
                }
            }
        }


        if (nbTouch == 1)
        {
            if(!isWon())
            {
                displayDialog = true;
                text.setColor(Color.WHITE);
                canvas.drawText("Il vous reste 1 déplacement à faire", 12, carteTopAnchor / 5, text);
                paintTimer(canvas);
                canvas.drawText(""+ time + "", getWidth()-30, 60, text);
            }
        }

        if (nbTouch == 0)
        {
            text.setColor(Color.WHITE);
            canvas.drawText("Il vous reste 2 déplacement à faire", 12, carteTopAnchor / 5, text);
            paintTimer(canvas);
            canvas.drawText(""+ time + "", getWidth()-30, 60, text);
        }

    }

    //Si gagne
    public boolean isWon()
    {
        boolean won = true;
        int i = 0;
        int j = 0;

        while (won & i < carteHeight)
        {
            j = 0;
            while (won & j < carteWidth)
            {
                if (carte[i][j] != CST_vide)
                {
                    won = false;
                }
                j++;
            }
            i++;
        }
        return won;
    }

    private void nDraw(Canvas canvas) {
        paintBackground(canvas);
        paintMessage(canvas);

        if (isWon()) {
            class1.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (displayDialog) {
                        if(level != 2) {
                            AlertDialog();
                        }
                        displayDialog = false;
                    }
                }
            });

            if (loadNextLevel) {
                level++;
                loadlevel();
                paintcarte(canvas);

                nbTouch = 0;
                loadNextLevel = false;
            }
        }

        //s'il n'a pas gagné ( soit perdu - soit début du jeu )
        else {
            paintcarte(canvas);
            delet();
        }
    }

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("-> TEST <-", "surfaceChanged " + width + " - " + height);
        initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("-> TEST <-", "surfaceCreated");
    }


    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i("-> TEST <-", "surfaceDestroyed");
    }


    public void run() {
        Canvas c = null;
        while (in) {
            try {
                cv_thread.sleep(300);
                stopTimer();

                try {
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            } catch (Exception e) {
            }
        }
    }




    public void delet()
    {
        boolean vert=true;
        boolean hor=true;
        do{
            hor = false;
            for (int i = 0; i < 7; i++) {
                boolean bool1 = true;
                for (int j = 0; j < 4; j++) {
                    if (carte[i][j] == carte[i][j + 1] && carte[i][j] == carte[i][j + 2] && carte[i][j] != CST_vide) {
                        hor = true;
                        for (int k = j + 3; k < 6; k++) {
                            while (bool1) {
                                bool1 = false;
                                if (carte[i][j] == carte[i][k]) {
                                    bool1 = true;
                                    carte[i][k] = CST_vide;
                                    int b = i;
                                    while (i > 0) {
                                        carte[i][k] = carte[i - 1][k];
                                        carte[i - 1][k] = CST_vide;
                                        i--;
                                    }
                                    i = b;
                                    k++;
                                }
                            }
                        }
                        carte[i][j] = CST_vide;
                        carte[i][j + 1] = CST_vide;
                        carte[i][j + 2] = CST_vide;


                        if (i > 0) {
                            int a = i;
                            while (i > 0) {
                                carte[i][j] = carte[i - 1][j];
                                carte[i - 1][j] = CST_vide;
                                i--;
                            }
                            i = a;
                            while (i > 0) {
                                carte[i][j + 1] = carte[i - 1][j + 1];
                                carte[i - 1][j + 1] = CST_vide;
                                i--;
                            }
                            i = a;
                            while (i > 0) {
                                carte[i][j + 2] = carte[i - 1][j + 2];
                                carte[i - 1][j + 2] = CST_vide;
                                i--;
                            }
                            i = a;
                        }

                    }
                }
            }


            vert = false;
            for (int i = 0; i < 7; i++) {
                boolean bool1 = true;
                for (int j = 0; j < 5; j++) {
                    if (carte[j][i] == carte[j + 1][i] && carte[j][i] == carte[j + 2][i] && carte[j][i] != CST_vide) {
                        vert = true;
                        int s = carte[j][i];
                        carte[j][i] = CST_vide;

                        int b = j;
                        while (j > 0) {
                            carte[j][i] = carte[j - 1][i];
                            carte[j - 1][i] = CST_vide;
                            j--;
                        }
                        carte[j + 1][i] = CST_vide;
                        j = b;
                        while (j > 0) {
                            carte[j + 1][i] = carte[j][i];
                            carte[j][i] = CST_vide;
                            j--;
                        }
                        carte[j + 2][i] = CST_vide;
                        j = b;
                        b = j;
                        while (j > 0) {
                            carte[j + 2][i] = carte[j + 1][i];
                            carte[j + 1][i] = CST_vide;
                            j--;
                        }
                        j = b;


                        for (int k = j + 3; k < 7; k++) {
                            while (bool1) {
                                bool1 = false;
                                if (s == carte[k][i]) {
                                    bool1 = true;
                                    carte[k][i] = CST_vide;
                                    int o = k;
                                    while (k > 0) {
                                        carte[k][i] = carte[k - 1][i];
                                        carte[k - 1][i] = CST_vide;
                                        k--;
                                    }
                                    k = o;
                                    k++;
                                }
                            }
                        }

                    }
                }
            }
        }while(hor || vert);
    }



    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event) {
        if (nbTouch < 2) {
            boolean empty = true;
            int temp = 0;
            int direction = 0;
            int action = event.getAction();
            int x = (int) (event.getX() / sizeCST);
            int y = (int) ((event.getY() - carteTopAnchor) / sizeCST);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    xDown = x;
                    yDown = y;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    return true;

                case MotionEvent.ACTION_UP:


                    if ((x - xDown) > 0) { direction = 1;  }  //Déplacement à droite
                    if ((x - xDown) < 0) { direction = -1; }  //Déplacement à gauche

                    //si on est pas dans la petite marge qu'on à laissée en haut ( carteTopAnchor )
                    if (yDown >= 0)
                    {
                        //si la case qu'on a touchée n'est pas vide
                        if (carte[yDown][xDown] != CST_vide)
                        {
                           if (direction != 0) { nbTouch++; }

                            //on verifie que la case n'est pas à l'extrémité de la carte X
                            if ((direction == 1 && xDown < carteWidth) || ( direction == -1 && xDown > 0))
                            {
                                //on fait notre permutation
                                temp = carte[yDown][xDown];
                                carte[yDown][xDown] = carte[yDown][xDown + (direction)];
                                carte[yDown][xDown + ( direction )] = temp;

                                //si notre case initiale a été remplacée par une case vide, on fait descendre
                                //ce qu'il y'a au dessus ( si ce n'est pas vide )
                                if (carte[yDown][xDown] == CST_vide)
                                {
                                    if (yDown > 0)
                                    {
                                        if (carte[yDown - 1][xDown] != CST_vide)
                                        {
                                            int a = yDown;
                                            while (a > 0)
                                            {
                                                carte[a][xDown] = carte[a - 1][xDown];
                                                carte[a - 1][xDown] = CST_vide;
                                                a--;
                                            }
                                        }
                                    }
                                }

                                //on verifie que la case n'est pas à l'extrémité de la carte Y
                                if (yDown < carteHeight - 1)
                                {
                                    //si après permutation notre case est suspendu sur un vide, on la fait descendre
                                    if (carte[yDown + 1][xDown + ( direction) ] == CST_vide)
                                    {
                                        int x1 = xDown + ( direction );
                                        int y1 = yDown + 1;
                                        do{
                                            empty = false;
                                            if (carte[y1][x1] == CST_vide)
                                            {
                                                empty=true;
                                                carte[y1][x1] = carte[y1 - 1][x1];
                                                carte[y1 - 1][x1] = CST_vide;
                                            }
                                            y1++;
                                        }while (empty & y1 < carteHeight);
                                    }
                                }
                            }
                        }
                    }
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }



    private void AlertDialog(){
        AlertDialog.Builder about = new AlertDialog.Builder(mContext);

        //nbTouch == 2 est détécté avant isWon
        //afin d'éviter que AlertDialog affiche un "Lose :/" on met un sleep avant de vérifier la condition isWon

        try{
            Thread.sleep(301);
        }catch(Exception e){}

        if(isWon()) {
            about.setTitle("Congratulations");
        }
        else {
            about.setTitle("Lose :/");
        }

        TextView l_viewabout = new TextView(mContext);
        l_viewabout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        l_viewabout.setPadding(10, 10, 10, 10);
        l_viewabout.setTextSize(20);

        if(isWon()) {
            l_viewabout.setText("Voulez vous continuer ?");
        }
        else {
            l_viewabout.setText("Voulez vous rejouer ?");
        }
        l_viewabout.setMovementMethod(LinkMovementMethod.getInstance());
        about.setView(l_viewabout);


        about.setPositiveButton("oui", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isWon()) {loadNextLevel = true;}
                else        {replay = true;}
            }
        });
        about.setNegativeButton("non", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isWon()) {
                    loadNextLevel = false;
                }
                else        {
                    replay = false;
                }

                class1.finish();
            }
        });
        about.show();
    }
}
