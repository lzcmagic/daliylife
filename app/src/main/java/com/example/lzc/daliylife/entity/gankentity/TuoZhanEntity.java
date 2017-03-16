package com.example.lzc.daliylife.entity.gankentity;

import java.util.ArrayList;

/**
 * Created by lzc on 2017/3/15.
 */

public class TuoZhanEntity {
    String error;
    ArrayList<Result> results;

    public ArrayList<Result> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "AndroidEntity{" +
                "error='" + error + '\'' +
                ", results=" + results +
                '}';
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
