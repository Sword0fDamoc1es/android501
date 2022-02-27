package com.example.cipher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    public TextView input;
    public TextView output;
    public TextView outputText;
    public Button btn2;
    public EditText inputText;
    public String regex = "[a-z]+";
    private int shiftKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        input = (TextView) findViewById(R.id.input);
        output = (TextView) findViewById(R.id.output);
        outputText = (TextView) findViewById(R.id.outputText);
        inputText = (EditText) findViewById(R.id.inputText);
        Intent i = getIntent();
        String str = i.getStringExtra("shift");
        shiftKey = Integer.valueOf(str);
        input.setText(input.getText().toString()+" with cipher key : "+str);

    }
    public void onClick(View view){
        String editTextString = inputText.getText().toString();
        if(editTextString.matches(regex)){
            char[] clist = editTextString.toCharArray();
            for(int i = 0 ; i < clist.length;i++){
                int c1 = clist[i];
                int c2 = 0;
                if(c1+shiftKey<=122){
                    c2 = c1+shiftKey;
                }else{
                    c2 = c1+shiftKey-26;
                }
                char cc = (char) c2;
                clist[i] = cc;
            }
            String newstr = String.valueOf(clist);
            outputText.setText(newstr);
        }else{
            outputText.setText("wrong input");
        }

    }
}