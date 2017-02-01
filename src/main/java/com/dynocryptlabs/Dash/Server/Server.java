package com.dynocryptlabs.Dash.Server;


import com.dynocryptlabs.Dash.Database.Models.User;
import com.dynocryptlabs.Dash.Server.Requests.RegisterUserRequest;
import com.dynocryptlabs.Dash.Server.Utilities.AuthFilter;
import com.dynocryptlabs.Dash.Server.Utilities.SparkJsonTransformer;
import spark.Service;

/**
 * Created by rohanpanchal on 1/29/17.
 *
 * The Server which encapsulates all HTTP endpoints for the service.
 */
public class Server {

    private Service service;
    private SparkJsonTransformer jsonTransformer = new SparkJsonTransformer();

    /**
     * Default constructor. Sets the service port to 5000.
     */
    public Server() {
        this(5000);
    }

    /**
     * Constructor which takes in a port number.
     *
     * @param port A port number.
     */
    public Server(int port) {
        this.service = Service.ignite();
        this.service.port(port);

        this.start();
    }

    /**
     * Starts the HTTP Service.
     */
    private void start() {

        this.service.before(new AuthFilter("/@me"));
        this.service.before(new AuthFilter("/@me/*"));

        // Default Routes
        this.service.get("/", (request, response) -> {
            response.status(200);
            return null;
        }, this.jsonTransformer);

        this.service.post("/user", (request, response) -> {
            RegisterUserRequest registerUserRequest = this.jsonTransformer.parseRequest(RegisterUserRequest.class, request);

            return User.registerUser(registerUserRequest.getEmail(),
                    registerUserRequest.getPassword(),
                    registerUserRequest.getFirstName(),
                    registerUserRequest.getLastName(),
                    false);
        }, this.jsonTransformer);

        this.service.get("/@me", (request, response) -> {
            return request.session().attribute("user");
        }, this.jsonTransformer);
    }

    /**
     * Stops the HTTP Service.
     */
    public void stop() {
        this.service.stop();
    }

}
