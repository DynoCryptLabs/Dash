package com.dynocryptlabs.Dash.Server.Utilities;

import com.dynocryptlabs.Dash.Database.Models.User;
import spark.FilterImpl;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Base64;

/**
 * Created by rohanpanchal on 1/31/17.
 *
 * A filter which takes a token and parses a User object
 * and attaches it to the request session.
 */
public class AuthFilter extends FilterImpl {

    public AuthFilter(String path) {
        super(path, "*");
    }

    //================================================================================
    // FilterImple Methods
    //================================================================================

    @Override
    public void handle(Request request, Response response) throws Exception {
        String accessToken = request.headers("Authorization");

        User user = User.findFromAccessToken(accessToken);

        if (user != null) {
            request.session().attribute("user", user);
        } else {
            Spark.halt(401);
        }
    }

    private String decodeHeader(String encodedHeader) {
        return new String(Base64.getDecoder().decode(encodedHeader));
    }
}
