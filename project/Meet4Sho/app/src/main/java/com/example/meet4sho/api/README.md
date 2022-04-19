Usages:
1. SearchFilter filter = new SearchFilter();
2. filter.add(key,value)
3. new TMRequest(new TMListener()).execute(filter);
4. implement private class TMListener (see code below)

Example Code:
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
