### IMPORTANT!!!
Be sure to check the dependencies:
```java
dependencies {

    implementation 'androidx.appcompat:appcompat:1.4.1'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.3'
    implementation 'org.json:json:20220320'
    implementation ('com.googlecode.json-simple:json-simple:1.1.1') {
        exclude group: 'junit', module: 'junit'
        exclude group: 'org.hamcrest', module: 'hamcrest-core'
    }
    implementation 'commons-io:commons-io:20030203.000550'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
}
```

### Usages for TicketMaster:
1. SearchFilter filter = new SearchFilter();
2. filter.add(key,value)
3. new TMRequest(new TMListener()).execute(filter);
4. implement private class TMListener (see code below)

```java
// imports ...
public class MainActivity extends AppCompatActivity {
    // views
    private Button btSubmit;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btSubmit = (Button) findViewById(R.id.btSubmit);
        tvResult = (TextView) findViewById(R.id.tvResult);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFilter filter = new SearchFilter();
                filter.add("city", "boston");
                filter.add("segmentName", "sports");
                new TMRequest(new TMListener()).execute(filter);
            }
        });
    }

    private class TMListener implements RequestListener {
        @Override
        public void updateViews(List events) {
            // reference: https://stackoverflow.com/questions/17176655/android-error-only-the-original-thread-that-created-a-view-hierarchy-can-touch
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String output = "";
                    for (int i = 0; i < events.size(); i++) {
                        TMEvent event = (TMEvent) events.get(i);
                        output += event.getName() + "\n";
                    }
                    tvResult.setText(output);
                }
            });
        }
    }
}
```

### Usages for MovieGlu:
1. SearchFilter filter = new SearchFilter();
2. filter.add("query", "batman");
3. filter.add("date", "2022-04-26");
4. Double latitude = 42.350444;
5. Double longtitude = -71.105377;
6. new MGRequest(new MGListener(), latitude, longtitude).execute(filter);
7. implement private class MGListener (see code below)

```java
public class MainActivity extends AppCompatActivity {
    // views
    private Button btSubmit;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btSubmit = (Button) findViewById(R.id.btSubmit);
        tvResult = (TextView) findViewById(R.id.tvResult);


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                SearchFilter filter = new SearchFilter();
                filter.add("query", "batman");
                filter.add("date", "2022-04-26");
                Double latitude = 42.350444;
                Double longtitude = -71.105377;
                new MGRequest(new MGListener(), latitude, longtitude).execute(filter);
            }
        });
    }

    private class MGListener implements RequestListener {
        @Override
        public void updateViews(List events) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String output = "";
                    for (int i = 0; i < events.size(); i++) {
                        MGCinema cinema = (MGCinema) events.get(i);
                        output += cinema.getCinema_name() + "\n";
                    }
                    tvResult.setText(output);
                }
            });
        }
    }
}
```
