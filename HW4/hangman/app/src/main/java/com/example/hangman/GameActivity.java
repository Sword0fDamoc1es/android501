package com.example.hangman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    public String current;
    public TextView wordtextView;
    public String answer;
    public ImageView imageView;
    public int errorCount;
    public TextView gameResult;
    public Button newGame;
    public Boolean gameState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        wordtextView = (TextView) findViewById(R.id.wordtextView);
        imageView = (ImageView) findViewById(R.id.imageView);
        gameResult = (TextView) findViewById(R.id.gameResult);
        newGame = (Button) findViewById(R.id.newGame);
        gameState = Boolean.TRUE;
        gameResult.setVisibility(View.GONE);
        // we will set answer. currently, this is a word.
        // But later, we need to 1. set several word set for different field: cars, countries, etc.
        // 2. get word from different set randomly. set the word as answer below.
        // for different sets of words, we can do "hint 1 method" on it, which gives a description.
        // I will finish hint 2 method. But hint button may need further discuss.
        answer = "RUSSIA"; // #TODO answer must fit the string length of chosen words.
        errorCount = 0;
        // # important! if we rotate, we need to store:
        // # 1. string in wordtextView, which is the process of ur game.
        // # 2. string current. which is what u've chosen(?? but once we click, it activates, does that need?)
        // # 3. errorCount.
        // # 4. ImageView path. what image we're using.
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        errorCount =  savedInstanceState.getInt("err");
        // # important!
        // do changeImage immediately after we get the errorCount.
        changeImage();
        answer = savedInstanceState.getString("ans");
        wordtextView.setText(savedInstanceState.getString("wordtext"));
        gameState = savedInstanceState.getBoolean("state");

        gameResult.setVisibility(savedInstanceState.getInt("resultInt"));
        gameResult.setText(savedInstanceState.getString("resultText"));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putInt("err",errorCount);
        outState.putString("ans",answer);
        outState.putString("wordtext",wordtextView.getText().toString());
        outState.putBoolean("state",gameState);

        outState.putInt("resultInt",gameResult.getVisibility());
        outState.putString("resultText",gameResult.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void changeImage(){
        switch (errorCount){
            case 1:
                imageView.setImageResource(R.drawable.p1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.p2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.p3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.p4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.p5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.p6);
                break;

        }
    }

    public void initializedGame(View view){
        // 1. error count.
        errorCount = 0;
        // 2. answer and textview
        // the following is set answer and textview.
        // TODO need change to random.
        // remember to fit the length!!
        answer = "RUSSIA";
        wordtextView.setText("______");
        // 3. image
        imageView.setImageResource(R.drawable.p0);
        // 4. game result
        gameResult.setVisibility(View.GONE);
        // 5. game state.
        // only onCreate and initialize can change state to true (meaning: start game).
        gameState = Boolean.TRUE;

    }

    private void doJudge(String s){
        // first edition.
        // 1. click and change:
//        wordtextView.setText(s);
        // second edition, check if is in the string, than change string.
        // third edition. based on second one, first check if exists.
        // if not exists: do change image.
        // else: do second condition.
        // forth edition. check if current text view is the answer. if it is, return. Thus done nothing.
        if(!gameState){
            return;
        }
        if(wordtextView.getText().toString().compareTo(answer)==0){
            gameResult.setText(R.string.game_result_win);
            gameResult.setVisibility(View.VISIBLE);
            // 5. gameState: end of the game: false
            gameState = Boolean.FALSE;
            return;
        }// check if win. even after the game ends with win.
        // Can be optimized using game state bool.

        if (answer.indexOf(current,0)==-1){
            errorCount += 1;
            changeImage();
            if(errorCount>=6){
                // TODO
                // output defeat! like: set textview to visible.
                gameResult.setText(R.string.game_result_loose);
                gameResult.setVisibility(View.VISIBLE);
                gameState = Boolean.FALSE;
                return;
            }

        }else {

            String currentStr = (String) wordtextView.getText();
            String dupStr = currentStr;
            // need optimized.
            int flag = 0;
            while (answer.indexOf(current, flag) != -1) {
                flag = answer.indexOf(current, flag);
                dupStr = dupStr.substring(0, flag) + current + dupStr.substring(flag + 1);
                flag = flag + 1;
            }
            wordtextView.setText(dupStr);
            if(wordtextView.getText().toString().compareTo(answer)==0){
                gameResult.setText(R.string.game_result_win);
                gameResult.setVisibility(View.VISIBLE);
                gameState = Boolean.FALSE;
                return;
            }// check game win after the input.
        }

    }
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.a:
                current = "A";
                doJudge("A");
                // count ++ ?
                // do judge here?
                break;
            case R.id.b:
                current = "B";
                doJudge("B");
                // count ++ ?
                // do judge here?
                break;
            case R.id.c:
                current = "C";
                doJudge("C");
                // count ++ ?
                // do judge here?
                break;
            case R.id.d:
                current = "D";
                doJudge("D");
                // count ++ ?
                // do judge here?
                break;
            case R.id.e:
                current = "E";
                doJudge("E");
                // count ++ ?
                // do judge here?
                break;
            case R.id.f:
                current = "F";
                doJudge("F");
                // count ++ ?
                // do judge here?
                break;
            case R.id.g:
                current = "G";
                doJudge("G");
                // count ++ ?
                // do judge here?
                break;
            case R.id.h:
                current = "H";
                doJudge("H");
                // count ++ ?
                // do judge here?
                break;
            case R.id.i:
                current = "I";
                doJudge("I");
                // count ++ ?
                // do judge here?
                break;
            case R.id.j:
                current = "J";
                doJudge("J");
                // count ++ ?
                // do judge here?
                break;
            case R.id.k:
                current = "K";
                doJudge("K");
                // count ++ ?
                // do judge here?
                break;
            case R.id.l:
                current = "L";
                doJudge("L");
                // count ++ ?
                // do judge here?
                break;
            case R.id.m:
                current = "M";
                doJudge("M");
                // count ++ ?
                // do judge here?
                break;
            case R.id.n:
                current = "N";
                doJudge("N");
                // count ++ ?
                // do judge here?
                break;
            case R.id.o:
                current = "O";
                doJudge("O");
                // count ++ ?
                // do judge here?
                break;
            case R.id.p:
                current = "P";
                doJudge("P");
                // count ++ ?
                // do judge here?
                break;
            case R.id.q:
                current = "Q";
                doJudge("Q");
                // count ++ ?
                // do judge here?
                break;
            case R.id.r:
                current = "R";
                doJudge("R");
                // count ++ ?
                // do judge here?
                break;
            case R.id.s:
                current = "S";
                doJudge("S");
                // count ++ ?
                // do judge here?
                break;
            case R.id.t:
                current = "T";
                doJudge("T");
                // count ++ ?
                // do judge here?
                break;
            case R.id.u:
                current = "U";
                doJudge("U");
                // count ++ ?
                // do judge here?
                break;
            case R.id.v:
                current = "V";
                doJudge("V");
                // count ++ ?
                // do judge here?
                break;
            case R.id.w:
                current = "W";
                doJudge("W");
                // count ++ ?
                // do judge here?
                break;
            case R.id.x:
                current = "X";
                doJudge("X");
                // count ++ ?
                // do judge here?
                break;
            case R.id.y:
                current = "Y";
                doJudge("Y");
                // count ++ ?
                // do judge here?
                break;
            case R.id.z:
                current = "Z";
                doJudge("Z");
                // count ++ ?
                // do judge here?
                break;
            default:
                break;



        }
    }
}