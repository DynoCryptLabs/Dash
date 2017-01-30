package com.dynocryptlabs.Dash.Server;


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

        // Default Routes
        this.service.get("/", (request, response) -> {
            response.status(200);
            return null;
        }, this.jsonTransformer);

    }

    /**
     * Stops the HTTP Service.
     */
    public void stop() {
        this.service.stop();
    }

}
