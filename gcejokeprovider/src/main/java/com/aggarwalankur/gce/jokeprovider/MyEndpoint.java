/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.aggarwalankur.gce.jokeprovider;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "jokeprovider.gce.aggarwalankur.com",
                ownerName = "jokeprovider.gce.aggarwalankur.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    private List<String> mJokesArchive;
    private int mJokesArchiveSize;
    private static Random mRandom;

    public MyEndpoint(){
        mJokesArchive = new ArrayList<>();

        mJokesArchive.add("This is a joke");
        mJokesArchive.add("This is a funny joke");
        mJokesArchive.add("This is a hilarious joke");
        mJokesArchive.add("This is a totally funny joke");
        mJokesArchive.add("This is a lol joke!");
        mJokesArchive.add("This is a rofl joke!");
        mJokesArchive.add("This is a bone-tickling joke !!");
        mJokesArchive.add("This is a poor joke. :(");
        mJokesArchive.add("This is a knock-knock joke :)");

        mJokesArchiveSize = mJokesArchive.size();

        mRandom = new Random();
    }

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    /**
     * Method to get a random joke from the list of jokes
     */
    @ApiMethod(name = "getJoke")
    public MyBean getJoke(){
        MyBean response = new MyBean();
        response.setData(mJokesArchive.get(mRandom.nextInt(mJokesArchiveSize)));

        return response;
    }

}
