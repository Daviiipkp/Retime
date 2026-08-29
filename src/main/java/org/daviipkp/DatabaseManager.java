package org.daviipkp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {

    private HikariDataSource datasource;

    public DatabaseManager() {
        setupDatabase();
    }

    private void setupDatabase() {

        String host = System.getenv().getOrDefault("DB_HOST", "localhost");
        String port = System.getenv().getOrDefault("DB_PORT", "5432");
        String database = System.getenv().getOrDefault("DB_NAME", "retime_db");
        String user = System.getenv().getOrDefault("DB_USER", "postgres");
        String password = System.getenv().getOrDefault("DB_PASS", "postgres");

        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setDriverClassName("org.postgresql.Driver");

        datasource = new HikariDataSource(config);

        String sql = "CREATE TABLE IF NOT EXISTS reminders (" +
                "\n    id SERIAL PRIMARY KEY," +
                "\n    name VARCHAR(100) NOT NULL," +
                "\n    metadata VARCHAR(2048) UNIQUE NOT NULL," +
                "\n    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "\n    recall_at TIMESTAMP NOT NULL" +
                "\n);";

        try{
            Statement statement = getConnection().createStatement();
            statement.execute(sql);
        } catch(SQLException e) {
            throw new RuntimeException("An SQLException was thrown when trying to init tables on database.", e);
        }catch(NullPointerException e) {
            throw new RuntimeException("An NullPointerException was thrown when trying to init tables on database.");
        }catch(Exception e) {
            throw new RuntimeException("Exception thrown while trying to init tables on database.", e);
        }

    }

    public Connection getConnection() {
        try {
            return datasource.getConnection();
        }
        catch(SQLException e) {
            throw new RuntimeException("An SQLException was thrown when getting database connection.", e);
        }catch(NullPointerException e) {
            throw new RuntimeException("An NullPointerException was thrown when getting database connection.");
        }
    }

    public void close() {
        try {
            datasource.close();
        }
        catch(Exception e) {
            throw new RuntimeException("An Exception was thrown trying to close connection.", e);
        }
    }

}
