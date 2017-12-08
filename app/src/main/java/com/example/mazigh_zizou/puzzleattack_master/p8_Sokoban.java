package com.example.mazigh_zizou.puzzleattack_master;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

// déclaration de notre activity héritée de Activity
public class p8_Sokoban extends Activity {

        private SokobanView mSokobanView;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            // initialise notre activity avec le constructeur parent
            super.onCreate(savedInstanceState);
            // charge le fichier main.xml comme vue de l'activité
            setContentView(R.layout.main);
            // recuperation de la vue une fois crée à partir de son id
            mSokobanView = (SokobanView)findViewById(R.id.SokobanView);
            // rend visible la vue
            mSokobanView.setVisibility(View.VISIBLE);

        }
}