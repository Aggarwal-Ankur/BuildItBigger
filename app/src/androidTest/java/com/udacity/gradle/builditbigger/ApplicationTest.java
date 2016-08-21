package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.udacity.gradle.builditbigger.communication.EndpointsAsyncTask;

import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    String mFetchedJoke = null;
    Exception mError = null;
    CountDownLatch mCountDownLatch = null;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        mCountDownLatch = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        mCountDownLatch.countDown();
    }

    public void testGCEAsyncTask() throws InterruptedException {

        EndpointsAsyncTask task = new EndpointsAsyncTask();
        try{
            task.execute(new EndpointsAsyncTask.OnJokeFetchedListener() {
                @Override
                public void OnJokeFetched(String loadedJoke) {
                    mFetchedJoke = loadedJoke;
                    mCountDownLatch.countDown();
                }
            });
        }catch (Exception e){
            mError = e;
        }

        mCountDownLatch.await();

        assertNull(mError);
        assertNotNull(mFetchedJoke);
        assertTrue(!mFetchedJoke.isEmpty());
        assertFalse("Check your internet connection", mFetchedJoke.contains("Unable to resolve host"));
    }
}