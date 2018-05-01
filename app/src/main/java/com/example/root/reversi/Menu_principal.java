package com.example.root.reversi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Menu_principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
    }

    public void showHelp(View clickedButton){
        Intent in = new Intent(this, Ajuda.class);
        startActivity(in);

    }
    public void showConfig(View clickedButton){
        Intent in = new Intent(this, Configuracio.class);
        startActivity(in);
    }
    public void finish(View clickedButton){
        finishAffinity();
    }

}
