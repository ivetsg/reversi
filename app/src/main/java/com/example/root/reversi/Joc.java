package com.example.root.reversi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by root on 23/04/18.
 */

public class Joc extends AppCompatActivity {

    private int mida;
    private int temps;
    private boolean control_temps;
    private String alias;
    public State state;
    private Board board;
    public GridView gridV;
    public Display display;
    private TextView puntsBlack;
    private TextView puntsWhite;
    private TextView quiTira;
    private String nivell;
    private String rival;
    private TextView casellesRestants;
    private TextView nivellDificultat;
    private CountDownTimer timer;
    private int tempsLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
        Intent intent = getIntent();

        mida = Integer.parseInt(intent.getStringExtra("Mida"));
        alias = intent.getStringExtra("Alias");
        temps = intent.getIntExtra("Temps", 0);
        control_temps = intent.getBooleanExtra("Control", false);

        rival = intent.getStringExtra("Rival");
        if (rival.equals("Màquina")) {
            nivell = intent.getStringExtra("Nivell");
            nivellDificultat = (TextView) findViewById(R.id.nivellDificultat);
            nivellDificultat.setText((String.format(getString(R.string.nivellDificultat), String.valueOf(this.nivell))));
        }

        board = new Board(mida);
        this.state = State.BLACK;

        puntsBlack = (TextView) findViewById(R.id.puntuacioBlack);
        puntsWhite = (TextView) findViewById(R.id.puntuacioWhite);
        quiTira = (TextView) findViewById(R.id.tirador);
        casellesRestants = (TextView) findViewById(R.id.casellesRestants);


        puntsBlack.setText(String.format(getString(R.string.puntsBlack), String.valueOf(this.board.black)));
        puntsWhite.setText(String.format(getString(R.string.puntsWhite), String.valueOf(this.board.white)));
        quiTira.setText(String.format(getString(R.string.torn), String.valueOf(this.state)));
        casellesRestants.setText((String.format(getString(R.string.casellesRestants), String.valueOf(this.board.casellesRestants))));


        gridV = findViewById(R.id.gridView);
        gridV.setNumColumns(mida);
        display = new Display(this, (board.cells), this);
        gridV.setAdapter(display);

        /*Ocultar barra superior*/
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        /* Temporitzadorr */

        final TextView textTimer = (TextView) findViewById(R.id.contador);

        if (control_temps) {
            textTimer.setTextColor(getResources().getColor(R.color.vermell));
            tempsLimit = temps;

        } else {
            textTimer.setTextColor(getResources().getColor(R.color.blau));
            tempsLimit=100000000;
            temps = 0;
        }

        timer = new CountDownTimer(tempsLimit * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                textTimer.setText(checkDigit(temps));
                if(control_temps){
                    temps--;
                }else{
                    temps++;
                }

            }
            public void onFinish() {
                textTimer.setText(R.string.temps_esgotat);
                if (rival.equals("Màquina")) {
                    finalitzarPartida();
                }
            }
        }.start();
    }

        public String checkDigit(int number) {
            return number <= 9 ? "0" + number : String.valueOf(number);
        }

    private void pauseTimer(){
        timer.cancel();
    }

        /* Fi temporitzador */

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putSerializable("Board", board.cells);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        //super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            board.cells = (Cela[][]) savedInstanceState.getSerializable("Board");
            display = new Display(this, (board.cells), this);
            gridV.setAdapter(display);

        }
    }

    public void nivellMaquina(){
                switch (nivell) {
                    case "Fàcil":
                        heuristicaFacil();

                        break;
                    case "Normal":
                        heuristicaNormal();
                        break;
                    default:
                        heuristicaDificil();

                        break;
                }
    }

    public String getRival(){
        return this.rival;
    }

    public void setHints(){
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if(this.board.cells[i][j].isHint()){
                    this.board.cells[i][j] = Cela.empty();
                }
                if(canPlayPosition(state, new Position(i,j))){
                    this.board.cells[i][j] = Cela.hint();
                }
            }
        }
    }


    public void heuristicaFacil(){
        Position pos = new Position(0,0);
        int peorNumFichas = 100;
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if(this.board.cells[i][j].isHint()){
                    int numFitxes = 0;
                    numFitxes = checkFitxesGirades(State.WHITE,new Position(i,j));
                    if(numFitxes<=peorNumFichas){
                        peorNumFichas=numFitxes;
                        pos =  new Position(i,j);
                    }
                }
            }
        }
        this.move(pos);
    }


    public void heuristicaNormal(){
        Position pos = new Position(0,0);
        int peorNumFichas = 100;
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if(this.board.cells[i][j].isHint()){
                    int numFitxes = 0;
                    numFitxes = checkFitxesGirades(State.WHITE,new Position(i,j));
                    if(numFitxes<=peorNumFichas){
                        peorNumFichas=numFitxes;
                        pos =  new Position(i,j);
                    }
                }
            }
        }
        this.move(pos);
    }


    public void heuristicaDificil(){
        Position pos = new Position(0,0);
        int mejorNumFichas = 0;
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if(this.board.cells[i][j].isHint()){
                    int numFitxes = 0;
                    numFitxes = checkFitxesGirades(State.WHITE,new Position(i,j));
                    if(numFitxes>mejorNumFichas){
                        mejorNumFichas = numFitxes;
                        pos = new Position(i,j);
                    }
                }
            }
        }
        this.move(pos);
    }



    public int checkFitxesGirades(State estat, Position position){
        boolean [] movimentsValids = directionsOfReverse(estat, position);
        boolean[] result = new boolean[Direction.ALL.length];
        int sumaTotal = 0;
        for (int i = 0; i < movimentsValids.length; i++) {
            int suma = 0;
            if(movimentsValids[i]){
                suma = contarFitxes(position.move(Direction.ALL[i]), Direction.ALL[i], suma);
                sumaTotal+=suma;

            }
        }
        return sumaTotal;
    }

    public int contarFitxes(Position position, Direction direction, int suma){
       if(this.board.isBlack(position) && state.equals(State.WHITE)){
            suma+=1;
            contarFitxes(position.move(direction), direction, suma);
       }
       return suma;
    }

    public boolean isFinished() {
        return this.state.equals(State.FINISHED);
    }

    public boolean isSame(State player, Position position) {
        return player.equals(State.BLACK) && this.board.isBlack(position) || player.equals(State.WHITE) && this.board.isWhite(position);
    }

    public boolean isOther(State player, Position position) {
        return player.equals(State.BLACK) && this.board.isWhite(position) || player.equals(State.WHITE) && this.board.isBlack(position);
    }

    public boolean someSame(State player, Position position, Direction direction) {
        return trobat(player, position, direction);
    }

    private boolean trobat(State player, Position position, Direction direction) {
        return !(!this.board.contains(position) || this.board.isEmpty(position)) && ((this.board.isBlack(position) && player.equals(State.BLACK)) || (this.board.isWhite(position) && player.equals(State.WHITE)) || trobat(player, position.move(direction), direction));
    }


    public boolean isReverseDirection(State player, Position position, Direction direction) {
        return isOther(player, position.move(direction)) && someSame(player, position.move(direction), direction);
    }


    public boolean[] directionsOfReverse(State player, Position position) {
        boolean[] result = new boolean[Direction.ALL.length];
        for (int i = 0; i < Direction.ALL.length; i++) {
            result[i] = isReverseDirection(player, position, Direction.ALL[i]);
        }
        return result;
    }

    private static boolean allFalse(boolean[] bools) {
        for (boolean bool : bools) {
            if (bool) {
                return false;
            }
        }
        return true;
    }


    public boolean canPlayPosition(State player, Position position) {
        return this.board.isEmpty(position) && !allFalse(directionsOfReverse(player, position));
    }

    public boolean canPlay(State player) {
        for (int i = 0; i < this.board.size(); i++) {
            for (int j = 0; j < this.board.size(); j++) {
                if (canPlayPosition(player, new Position(i, j))) {
                    return true;
                }
            }
        }
        return false;
    }


    private void disk(Position position) {
        if (State.BLACK == this.state) {
            this.board.setBlack(position);
        } else {
            this.board.setWhite(position);
        }
    }

    private void reverse(Position position, Direction direction) {
        reversePieces(position.move(direction), direction);
    }

    private void reverse(Position position, boolean[] directions) {
        for (int i = 0; i < Direction.ALL.length; i++) {
            if (directions[i]) {
                reverse(position, Direction.ALL[i]);
            }
        }
    }

    private void reversePieces(Position position, Direction direction) {
        if (this.state == State.BLACK) {
            reverseToBlack(position, direction);
        } else {
            reverseToWhite(position, direction);
        }
    }

    private void reverseToWhite(Position position, Direction direction) {
        while (this.board.isBlack(position)) {
            reverseFinal(position);
            position = position.move(direction);
        }
    }

    private void reverseToBlack(Position position, Direction direction) {
        while (this.board.isWhite(position)) {
            reverseFinal(position);
            position = position.move(direction);
        }
    }


    private void reverseFinal(Position position) {
        this.board.reverse(position);
    }

    private void changeTurn() {
        if (canPlay(getJugadorContrario())) {
            this.state = getJugadorContrario();
        } else if (canPlay(this.state)) {
            this.state = this.state;
        } else {
            this.state = State.FINISHED;
        }
    }

    public void move(Position position) {
        if (!this.board.isEmpty(position)) {
            return;
        }
        boolean[] directions = this.directionsOfReverse(this.state, position);
        if (allFalse(directions)) {
            return;
        }
        this.disk(position);
        this.reverse(position, directions);
        this.changeTurn();
        mostrarPuntuacio();
        if (this.state == State.FINISHED) {
            finalitzarPartida();
        } else {
            quiTira.setText(String.format(getString(R.string.torn), String.valueOf(this.state)));
        }
    }

    private void mostrarPuntuacio(){
        puntsBlack.setText(String.format(getString(R.string.puntsBlack),String.valueOf(this.board.black)));
        puntsWhite.setText(String.format(getString(R.string.puntsWhite),String.valueOf(this.board.white)));
        casellesRestants.setText((String.format(getString(R.string.casellesRestants), String.valueOf(this.board.casellesRestants))));
    }

    private void finalitzarPartida() {
        pauseTimer();
        if (guanyador() == State.EMPAT) {
            quiTira.setText(R.string.empat);
        } else {
            quiTira.setText(String.format(getString(R.string.guanyador), String.valueOf(guanyador())));
        }

        if(rival.equals("Jugador")){

        }else{
            mostrarToastFinal();
            enviarDadesResultats();

        }

    }

    private void mostrarToastFinal(){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_personalizado, (ViewGroup) this.findViewById(R.id.toastLinear));
        ImageView imageView = (ImageView) layout.findViewById(R.id.toastImage);
        TextView txtToast = (TextView) layout.findViewById(R.id.toastTxt);
        layout.setBackgroundResource(R.drawable.toast_shape);

        if(control_temps && temps==0){
            txtToast.setText(R.string.tempsEsgotat);
            imageView.setBackgroundResource(R.drawable.timeout);

        }else if(this.board.casellesRestants!=0){
            txtToast.setText(R.string.bloqueigPartida);
            imageView.setBackgroundResource(R.drawable.stop);

        }else if(guanyador()==State.BLACK){
            txtToast.setText(R.string.hasGuanyat);
            imageView.setBackgroundResource(R.drawable.thumbup);

        }else if(guanyador()==State.WHITE){
            txtToast.setText(R.string.hasPerdut);
            imageView.setBackgroundResource(R.drawable.thumbdown);

        }else if(guanyador()==State.EMPAT){
            txtToast.setText(R.string.bloqueigPartida);
            imageView.setBackgroundResource(R.drawable.empate);

        }

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }

    private void enviarDadesResultats(){
        String log;
        int diferencia = this.board.black - this.board.white;
        log = "Alias: " + alias + "\n";
        log += "Mida taulell: " + String.valueOf(this.mida) + "\n";
        log += this.puntsBlack.getText().toString() + " " + this.puntsWhite.getText().toString() + "\n";


        if(this.board.casellesRestants == 0 && temps != 0) {
            if (guanyador().equals(State.BLACK)) {
                log += "Has guanyat!! \n";
                log += Math.abs(diferencia) + " caselles de diferència \n";
            } else if (guanyador().equals(State.WHITE)) {
                log += "Has perdut!! \n";
                log += Math.abs(diferencia) + " caselles de diferència \n";
            } else {
                log += "Heu empatat!! \n";
            }
        }

        if(this.board.casellesRestants != 0){
            log += "Us heu quedat sense poder completar la graella \n";
            log += "Han quedat " + this.board.casellesRestants + " caselles per cobrir \n";
        }


        if (control_temps) {
            if(temps == 0){
                log += "Has esgotat el temps! \n";
            }else{
                log += "Han sobrat " + String.valueOf(temps) + " segons \n";
            }
        }else{
            log += "La partida ha durat " + String.valueOf(temps) + " segons \n";
        }


        Intent in = new Intent(this, Resultat.class);
        in.putExtra("log", log);
        startActivity(in);
    }

    private State getJugadorContrario() {
        if (this.state == State.BLACK) {
            return State.WHITE;
        } else {
            return State.BLACK;
        }
    }

    private State guanyador(){
        if (this.board.black > this.board.white) {
            return State.BLACK;
        } else if (this.board.white > this.board.black) {
            return State.WHITE;
        } else {
            return State.EMPAT;
        }

    }

    @Override
    public void onBackPressed(){
        pauseTimer();
        finish();
    }


}