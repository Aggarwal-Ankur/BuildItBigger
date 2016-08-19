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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.communication.EndpointsAsyncTask;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.OnJokeFetchedListener{

    private InterstitialAd mInterstitialAd;

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


        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getActivity().getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                launchJokeActivity();
            }
        });

        requestNewInterstitial();

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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
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
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                launchJokeActivity();
            }
        }else{
            Toast.makeText(getActivity(), getString(R.string.error_fetching_joke), Toast.LENGTH_LONG).show();
        }
    }
}
