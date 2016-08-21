package com.udacity.gradle.builditbigger.communication;

import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.aggarwalankur.gce.jokeprovider.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Ankur on 8/17/2016.
 */
public class EndpointsAsyncTask extends AsyncTask<EndpointsAsyncTask.OnJokeFetchedListener, Void, String> {
    public interface OnJokeFetchedListener{
        void OnJokeFetched(String loadedJoke);
    }

    private static MyApi myApiService = null;
    private OnJokeFetchedListener mListener;

    @Override
    protected String doInBackground(EndpointsAsyncTask.OnJokeFetchedListener... params) {
        if(myApiService == null) {  // Only do this once

            //Uncomment this for the actual deployed GCE server
            /*MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://jokeprovider-nanodegree.appspot.com/_ah/api/");*/


            //This is for the local development server
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        mListener = params[0];

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mListener.OnJokeFetched(result);
    }
}