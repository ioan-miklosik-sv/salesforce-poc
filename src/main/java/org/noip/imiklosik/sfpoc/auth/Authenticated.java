package org.noip.imiklosik.sfpoc.auth;


import com.sforce.ws.ConnectorConfig;

public class Authenticated<T> {

    private ConnectorConfig config;
    private T connection;

    public Authenticated(ConnectorConfig config, T connection) {
        this.config = config;
        this.connection = connection;
    }

    public ConnectorConfig getConfig() {
        return config;
    }

    public T getConnection() {
        return connection;
    }
}
