package com.example.root.reversi;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import android.os.Handler;

/**
 * Created by root on 24/04/18.
 */

public class CelaListener implements View.OnClickListener {

    private Position posicio;
    private Context context;
    private Cela cela;
    private Joc joc;

    CelaListener(Context context, Position posicio, Cela cela, Joc joc){
        this.context = context;
        this.posicio = posicio;
        this.cela = cela;
        this.joc = joc;

    }
    @Override
    public void onClick(View v) {

        if(!joc.isFinished()){
            if(cela.isHint()) {
                joc.move(posicio);
                joc.setHints();
                joc.display.notifyDataSetChanged();
                joc.gridV.setAdapter(joc.display);

                if (joc.getRival().equals("Màquina") && (joc.state == State.WHITE)) {
                    while(joc.state == State.WHITE) {

                                joc.nivellMaquina();
                                joc.setHints();
                                joc.display.notifyDataSetChanged();
                                joc.gridV.setAdapter(joc.display);

                    }
                }
            }else{
                Toast.makeText(context,R.string.novalida, Toast.LENGTH_LONG).show();
            }
        }

    }

}
