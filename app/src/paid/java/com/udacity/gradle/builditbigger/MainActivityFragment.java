package com.udacity.gradle.builditbigger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aggarwalankur.jokes.JokeProvider;
import com.aggarwalankur.jokeviewer.JokeActivity;
import com.udacity.gradle.builditbigger.communication.EndpointsAsyncTask;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.OnJokeFetchedListener{

    private String mFetchedJoke;

    private Button mTellJokeButton;

    private ProgressDialog mDialog;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        //Build the dialog
        mDialog = new ProgressDialog(getActivity());
        mDialog.setIndeterminate(true);
        mDialog.setMessage(getResources().getString(R.string.fetching_joke));

        mTellJokeButton = (Button) root.findViewById(R.id.tell_joke_button);
        mTellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                loadJokeFromGCE();
            }
        });

        return root;
    }


    private void launchJokeActivity(){
        Intent jokeIntent = new Intent(getActivity(), JokeActivity.class);
        jokeIntent.putExtra(JokeActivity.JOKE_KEY, mFetchedJoke);
        startActivity(jokeIntent);
    }

    /**
     * This was used in 'Step 1: Create a Java library', but is now replaced by GCE
     */
    private void loadJokeFromLocalProvider(){
        JokeProvider mJokeProvider = new JokeProvider();
        mFetchedJoke = mJokeProvider.getJoke();
    }

    private void loadJokeFromGCE(){
        new EndpointsAsyncTask().execute(this);
    }

    @Override
    public void OnJokeFetched(String loadedJoke) {
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mFetchedJoke = loadedJoke;

        if(loadedJoke != null){
            launchJokeActivity();
        }else{
            Toast.makeText(getActivity(), getString(R.string.error_fetching_joke), Toast.LENGTH_LONG).show();
        }
    }
}
