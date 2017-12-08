package com.example.mazigh_zizou.puzzleattack_master;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;



import android.widget.Toast;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Mazigh-zizou on 03/12/2017.
 */

public class menu  extends Activity {


    public RadioGroup sound;
    public RadioButton music_on;
    public Button exit;

    public Button howToPlay;
    public Button about;

    public AlertDialog.Builder builder;
    private static final int alertDialogueHowToPlay = 0;

    private static final int alertDialogueAbout = 1;
    private static final String TAG = "MyApp";

    private boolean music_Off=true;

    MediaPlayer music;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case alertDialogueHowToPlay:    // pour le boutton de how to play : création des AlertDialogue lors de l'appuie sur notre boutton
                builder = new AlertDialog.Builder(this);
                builder.setMessage("notre message");
                //builder.setIcon(R.drawable.blue);

                builder.setTitle("HowToPlay");
                builder.setCancelable(true);

                builder.setIcon(R.drawable.question);
                builder.setPositiveButton("OK", new menu.CancelOnClickListener());
                AlertDialog alertDialogHowToPlay = builder.create();
                alertDialogHowToPlay.show();
                break;

            case alertDialogueAbout: // pour le boutton about : création des AlertDialogue lors de l'appuie sur notre boutton

                builder = new AlertDialog.Builder(this);
                builder.setMessage("notre message");
                builder.setTitle("About");
                builder.setIcon(R.drawable.about);
                builder.setCancelable(true);

                builder.setPositiveButton("OK", new menu.CancelOnClickListener());
                AlertDialog alertDialogAbout = builder.create();
                alertDialogAbout.show();
                break;
        }
        return super.onCreateDialog(id);


    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu);


        about        = (Button) findViewById(R.id.about);
        howToPlay = (Button) findViewById(R.id.htp);
        sound        = (RadioGroup) findViewById(R.id.sound);
        exit         = (Button) findViewById(R.id.exit);
        music_on     = (RadioButton)findViewById(R.id.music_on);



        final Button play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent MyIntent = new Intent(menu.this, p8_Sokoban.class);
                music_Off = false;
                startActivity(MyIntent);} });

        music= MediaPlayer.create(getBaseContext(),R.raw.son);
        music.start();

        sound.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.music_on :
                        music.start();
                        break;
                    case R.id.music_off :
                        music.pause();
                        break;
                }
            }
        });

        howToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(alertDialogueHowToPlay);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(alertDialogueAbout);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG,"Exit");
                Toast.makeText(getApplicationContext(),"Exit",Toast.LENGTH_SHORT)
                        .show();
                music.pause();
                finish();
            }
        });
    }


    public void onResume(Bundle savedInstanceState){
        super.onResume();
    }
    public void onStop(){
        super.onStop();
        if (music_Off) {
            music.pause();
        }
    }
    public void onRestart(){
        super.onRestart();
        if (music_on.isChecked()){
            music.start();
        }else{
            music.pause();
        }
    }
}