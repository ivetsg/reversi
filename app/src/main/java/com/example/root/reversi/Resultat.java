package com.example.root.reversi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by root on 26/04/18.
 */

public class Resultat extends AppCompatActivity {

    private String puntsBlack;
    private EditText editLog;
    private EditText editDataHora;
    private EditText editEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultat);
        Intent intent = getIntent();
        puntsBlack = intent.getStringExtra("log");

        editLog = (EditText) findViewById(R.id.editlog);
        editLog.setText(puntsBlack);

        editDataHora = (EditText) findViewById(R.id.editHora);
        editDataHora.setText(getDateTime());

        editEmail = (EditText) findViewById(R.id.editEmail);


    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void goBack(View clickedButton){
        finishAffinity();
    }

    public void novaPartida(View clickedButton){
        Intent in = new Intent(this, Configuracio.class);
        finish();
        startActivity(in);
    }

    public void enviarEmail(View view){
        Intent in;
        in = new Intent(Intent.ACTION_SEND);
        in.putExtra(Intent.EXTRA_EMAIL, new String[]{editEmail.getText().toString()});
        in.putExtra(Intent.EXTRA_SUBJECT, "Log - " + editDataHora.getText().toString());
        in.putExtra(Intent.EXTRA_TEXT, editLog.getText().toString());
        in.setType("message/rfc822");
        startActivity(Intent.createChooser(in, "Elegir app de correo a utilizar :"));
    }

    @Override
    public void onBackPressed(){
        Intent in = new Intent(this, Configuracio.class);
        finish();
        startActivity(in);
    }


}

