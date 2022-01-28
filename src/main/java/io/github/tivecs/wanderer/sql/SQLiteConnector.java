package io.github.tivecs.wanderer.sql;

import javax.annotation.Nonnull;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector extends SQLConnector {

    private Connection connection = null;

    public SQLiteConnector(@Nonnull File databaseFile) {
        super("org.sqlite.JDBC", "jdbc:sqlite:" + databaseFile);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        if (this.connection != null && !this.connection.isClosed()) return this.connection;

        Class.forName(getClassPath());
        setConnection(DriverManager.getConnection(getDriver()));

        return this.connection;
    }
}
