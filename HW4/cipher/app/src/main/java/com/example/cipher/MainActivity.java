package com.example.cipher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public TextView textView;
    public EditText shift;
    public Button btn1;
    public String regex = "\\d+";
    public static final String PREFS_NAME = "myPrefsFile";
    private String sharedstring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        shift = (EditText) findViewById(R.id.shift);
        btn1 = (Button) findViewById(R.id.btn1);
        SharedPreferences setting = getSharedPreferences(PREFS_NAME,0);
        sharedstring = setting.getString("shift",sharedstring);
        shift.setText(sharedstring);

    }
    public void onClick(View view){
        String str = shift.getText().toString();
//        if (str.length()>1 || str.length()==0){
//            return;
//        }
        if(str.matches(regex)){
            int i = Integer.valueOf(str);
            if(1<=i && i<=25){
                Intent nextpage = new Intent(getApplicationContext(), MainActivity2.class);
                nextpage.putExtra("shift", str);
                SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = setting.edit();
                editor.putString("shift", str);
                editor.commit();
                startActivity(nextpage);
            }
        }
    }
}