package org.daviipkp.retime;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.daviipkp.retime.deadline.DeadlineAction;
import org.daviipkp.retime.dto.ReminderDatabaseRecord;
import org.daviipkp.retime.dto.ReminderRecord;
import org.daviipkp.retime.reminder.Reminder;
import org.daviipkp.retime.reminder.ReminderLink;

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

        String sql = """
                     CREATE TABLE IF NOT EXISTS reminders (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         metadata VARCHAR(4096) NOT NULL,
                         action_type INT NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         recall_at TIMESTAMP NOT NULL
                     );""";

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
        //SHOULD CHECK IF THERE ARE REMINDERS IN THE DATABASE AND LOAD THEM TO THE THREAD!
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

    public int insert(String title, String metadata, int actionType, Instant recallAt) {
        String sql = """
        INSERT INTO reminders (name, metadata, action_type, recall_at)
        VALUES (?, ?, ?, ?);
        """;

        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setString(2, metadata);
            stmt.setInt(3, actionType);
            
        
            LocalDateTime a = LocalDateTime.ofInstant(recallAt, ZoneOffset.UTC);
            
            stmt.setObject(4, a);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        }catch(Exception e) {

        }
        return -1;
    }

    public ReminderLink insertReminder(ReminderRecord record) {
        int actionType = DeadlineAction.getIdByName(record.action_type());

        int id = insert(record.name(), record.recall_data(), actionType, record.recall_at());
        if(id == -1) {
            throw new RuntimeException("Database didn't return a proper ID. Check database health.");
        }
        return new ReminderLink(record.recall_at(), id);
    }

    public Reminder retrieveReminder(int id) {
        try {
            ReminderDatabaseRecord rec = findById(getConnection(), id).orElseThrow();
            Class<? extends DeadlineAction> actionClazz = DeadlineAction.getActionById((short)rec.action_type());
            DeadlineAction actionInstance = actionClazz.getDeclaredConstructor().newInstance();
            actionInstance.setupFromMetadata(rec.metadata());
            ReminderLink link = new ReminderLink(rec.recall_at(),id);
            return new Reminder(rec.name(), actionInstance, link);
        } catch (SQLException e) {
            throw new RuntimeException("Database couldn't retrieve object by ID. Check database health.");
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("No valid constructor defined on action.");
        } catch (SecurityException ex) {
            throw new RuntimeException("SecurityException.");
        } catch (InstantiationException ex) {
            throw new RuntimeException("Can't instantiate action..");
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Constructor is private on action.");
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Constructor requires parameters.");
        } catch (InvocationTargetException ex) {
            throw new RuntimeException("InvocationTargetException.");
        }
        
    }

    public ReminderDatabaseRecord recordFromResultSet(ResultSet rs) throws SQLException {
        Timestamp createdAtTs = rs.getTimestamp("created_at");
        Timestamp recallAtTs = rs.getTimestamp("recall_at");

        return new ReminderDatabaseRecord(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("metadata"),
            rs.getInt("recall_type"),
            createdAtTs != null ? createdAtTs.toInstant() : null,
            recallAtTs != null ? recallAtTs.toInstant() : null
        );
    }

    public Optional<ReminderDatabaseRecord> findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM reminders WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(recordFromResultSet(rs));
                }
            }
        }
        return Optional.empty();
    }

}
