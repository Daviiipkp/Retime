package org.daviipkp.retime;

import org.daviipkp.retime.reminder.Reminder;
import org.daviipkp.retime.reminder.ReminderLink;

import io.javalin.json.JavalinJackson;

public class Retime {

    private static final boolean DEBUG = true;
    private static final int PORT = 8080;
    private static ServerManager serverManager;
    private static DatabaseManager databaseManager;
    private static final JavalinJackson j = new JavalinJackson();
    

    public static void debug(Object... args) {
        if(!DEBUG) return;
        StringBuilder sb = new StringBuilder();
        for(Object obj : args) {
            sb.append(obj.toString());
        }
        System.out.println(sb.toString());
    }

    public static void main( String[] args ) {
        debug("Starting Retime...");
        debug("Trying to setup Javalin Server...");
        serverManager = new ServerManager(PORT);
        debug("Javalin server setup sucessfully. Starting database setup...");
        databaseManager = new DatabaseManager();
        debug("Database started sucessfully.");

        
        mock();


        debug("Startup complete. Retime is now running on port " + PORT + ".");
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static ServerManager getServerManager() {
        return serverManager;
    }

    public static void mock() {
        System.out.println("Creating mock reminders");

    }

    public static void callDeadline(ReminderLink link) {
        Reminder r = getDatabaseManager().retrieveReminder(link.getId());
        r.getAction().call();
    }

    public static <T> T fromJsonString(String s, Class<T> c) {
        return j.fromJsonString(s, c);
    } 
}
