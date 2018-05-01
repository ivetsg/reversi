package com.example.root.reversi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import org.w3c.dom.Text;

/**
 * Created by root on 19/04/18.
 */

public class Configuracio extends AppCompatActivity {

    private TextView seekBarValue;
    private TextView temps_tv;
    private EditText alias;
    private CheckBox checkTemps;
    private String mPlantillaMensajeCheck;
    private String mPlantillaMensajeUnCheck;
    private LinearLayout level;
    private TextView txtLevel;
    private RadioGroup rdg;
    private RadioButton maquina;
    private RadioButton jugador;
    private RadioGroup rdgNivell;
    private RadioButton facil;
    private RadioButton normal;
    private RadioButton dificil;
    private int temps = 40;
    private String contra = "Màquina";
    private String nivell = "Fàcil";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracio);
        mPlantillaMensajeCheck = getString(R.string.checkControl);
        mPlantillaMensajeUnCheck = getString(R.string.uncheckControl);
        temps_tv = findViewById(R.id.temps_total);
        SeekBar seekBar = findViewById(R.id.seekGraella);
        alias = findViewById(R.id.alias);
        checkTemps = findViewById(R.id.checkbox_tiempo);
        seekBarValue = findViewById(R.id.seekNumber);
        txtLevel = findViewById(R.id.dificultatTxt);
        level = findViewById(R.id.levelLinear);

        temps_tv.setText(String.format(getString(R.string.segons), String.valueOf(temps)));

        seekBar.setProgress(0);
        seekBar.setMax(8);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf((progress * 2) + 4));
                temps_tv.setText(String.format(getString(R.string.segons), String.valueOf((progress + 1) * 40)));
                temps = ((progress + 1) * 40);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }


        });

        rdg = (RadioGroup) findViewById(R.id.radioLevel);
        maquina = (RadioButton) findViewById(R.id.radio_maquina);
        jugador = (RadioButton) findViewById(R.id.radio_jugador);

        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_maquina) {
                    contra = maquina.getText().toString();
                    txtLevel.setVisibility(View.VISIBLE);
                    level.setVisibility(View.VISIBLE);
                } else if (i == R.id.radio_jugador) {
                    contra = jugador.getText().toString();
                    txtLevel.setVisibility(View.INVISIBLE);
                    level.setVisibility(View.INVISIBLE);

                }
            }
        });

        rdgNivell = (RadioGroup) findViewById(R.id.radioNivell);
        facil = (RadioButton) findViewById(R.id.radio_facil);
        normal = (RadioButton) findViewById(R.id.radio_normal);
        dificil = (RadioButton) findViewById(R.id.radio_dificil);

        rdgNivell.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_facil) {
                    nivell = facil.getText().toString();
                } else if (i == R.id.radio_normal) {
                    nivell = normal.getText().toString();
                }else{
                    nivell = dificil.getText().toString();

                }
            }
        });

    }

    public void goBack(View clickedButton){
        Intent in = new Intent(this, Menu_principal.class);
        finish();
        startActivity(in);
    }

    public void muestraTextoCheck(View clickedButton) {
        CheckBox button = (CheckBox) clickedButton;
        if(button.isChecked()){
            String message = String.format(mPlantillaMensajeCheck, temps_tv.getText());
            showToast(message);
        }else{
            String message = String.format(mPlantillaMensajeUnCheck, "");
            showToast(message);
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void inici(View view){

        Intent in = new Intent(this, Joc.class);
        in.putExtra("Alias", alias.getText().toString());
        in.putExtra("Mida", seekBarValue.getText());
        in.putExtra("Control", checkTemps.isChecked());
        in.putExtra("Temps", temps);
        in.putExtra("Rival", contra);
        in.putExtra("Nivell", nivell);
        startActivity(in);
    }

    @Override
    public void onBackPressed(){
        Intent in = new Intent(this, Menu_principal.class);
        finish();
        startActivity(in);

    }

}


