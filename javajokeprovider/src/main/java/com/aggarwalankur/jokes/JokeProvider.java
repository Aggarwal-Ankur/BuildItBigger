package com.aggarwalankur.jokes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JokeProvider {

    private List<String> mJokesArchive;
    private static Random mRandom;

    public JokeProvider(){
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

        mRandom = new Random();
    }

    public String getJoke(){
        return mJokesArchive.get(mRandom.nextInt(mJokesArchive.size()));

    }
}
