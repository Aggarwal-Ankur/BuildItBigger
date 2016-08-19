package com.aggarwalankur.jokeviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    public static final String JOKE_KEY = "joke_key";

    private TextView mJokeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        mJokeTextView = (TextView) findViewById(R.id.joke_tv);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            String receivedJoke = extras.getString(JOKE_KEY);

            if(receivedJoke != null && !receivedJoke.isEmpty()){
                mJokeTextView.setText(receivedJoke);
            }
        }
    }
}
