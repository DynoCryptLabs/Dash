package com.dynocryptlabs.Dash.Server.Utilities;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by rohanpanchal on 1/29/17.
 *
 * A JSON serializer usable by Spark services.
 */
public class SparkJsonTransformer implements ResponseTransformer {

    private Gson gson = new Gson();

    @Override
    public String render(Object o) throws Exception {
        return this.gson.toJson(o);
    }
}
