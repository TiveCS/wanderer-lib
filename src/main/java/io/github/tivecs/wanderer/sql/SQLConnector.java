package io.github.tivecs.wanderer.sql;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SQLConnector {

    private String classPath;
    private String driver;

    public SQLConnector(String classPath, String driver){
        this.classPath = classPath;
        this.driver = driver;
    }

    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

    protected String getClassPath() {
        return classPath;
    }

    protected String getDriver() {
        return driver;
    }
}
