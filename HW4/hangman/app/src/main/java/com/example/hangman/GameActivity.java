package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    public String current;
    public TextView wordtextView;
    public String answer;
    public ImageView imageView;
    public int errorCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        wordtextView = (TextView) findViewById(R.id.wordtextView);
        imageView = (ImageView) findViewById(R.id.imageView);
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
    private void doJudge(String s){
        // first edition.
        // 1. click and change:
//        wordtextView.setText(s);
        // second edition, check if is in the string, than change string.
        // third edition. based on second one, first check if exists.
        // if not exists: do change image.
        // else: do second condition.
        if (answer.indexOf(current,0)==-1){
            errorCount += 1;
            if(errorCount>=7){
                // TODO
                // output defeat! like: set textview to visible.
                return;
            }
            changeImage();
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
            default:
                break;



        }
    }
}