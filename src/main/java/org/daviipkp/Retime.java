package org.daviipkp;

public class Retime {

    private static final boolean DEBUG = true;
    private static final int PORT = 8080;
    private static ServerManager serverManager;
    private static DatabaseManager databaseManager;

    //should have a list of the reminders of the day
    //this list should be organized by ID, in a way that every search by id should be really quick
    //time between checks should be configurable
    //some trigger should dump the reminders into local database
    //

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
        debug("Javalin server setup sucessfully. Starting database setup.");
        
        databaseManager = new DatabaseManager();


        debug("Startup complete. Retime is now running on port " + PORT + ".");
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static ServerManager getServerManager() {
        return serverManager;
    }



}
