package com.dynocryptlabs.Dash.Database;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Created by rohanpanchal on 1/31/17.
 *
 * Encapsulates logic for retrieving JDBC Connections.
 */
public class ConnectionFactory {

    private static ConnectionSource connectionSource;

    private static Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

    public static synchronized ConnectionSource getConnectionSource() throws SQLException {
        if (ConnectionFactory.connectionSource == null) {
            String databaseURL = System.getenv("DATABASE_URL");
            String databaseUsername = System.getenv("DATABASE_USERNAME");
            String databasePassword = System.getenv("DATABASE_PASSWORD");

            logger.debug("DATABASE_URL: " + databaseURL);
            logger.debug("DATABASE_USERNAME: " + databaseUsername);

            JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(databaseURL, databaseUsername, databasePassword);
            connectionSource.setMaxConnectionsFree(5);
            ConnectionFactory.connectionSource = connectionSource;
        }
        return ConnectionFactory.connectionSource;
    }
}
