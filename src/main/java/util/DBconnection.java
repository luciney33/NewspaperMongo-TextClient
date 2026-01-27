package util;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    private Configuration conf;

   @Inject
    public DBconnection(Configuration conf) {
        this.conf = conf;
    }

    public Connection getConnection() throws SQLException {
        Connection myConnection = DriverManager.getConnection(conf.getProperty("urlDB"),conf.getProperty("user_name"),conf.getProperty("password"));
        return myConnection;
    }
}
