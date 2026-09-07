package org.daviipkp.retime;

import org.daviipkp.retime.dto.ReminderRecord;
import org.daviipkp.retime.reminder.ReminderLink;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class ServerManager {

    private Javalin server;
    
    private int PORT;
    

    public ServerManager(int arg0) {
        this.PORT = arg0;
        initializeServer();
    }


    private void initializeServer() {
        server = Javalin.create(config -> {
            config.routes.get("/health", ctx -> {
                ctx.status(200).result("Server healthy!");
            });
            
            config.routes.apiBuilder(() -> {
                ApiBuilder.path("/api/create", () -> {
                    ApiBuilder.post(ctx -> {
                        ReminderRecord r = ctx.bodyAsClass(ReminderRecord.class);
                        ReminderLink link = Retime.getDatabaseManager().insertReminder(r);
                        if(RetimeExecutorThread.firstThanNext(r)) {
                            RetimeExecutorThread.insertNewNext(link);
                        }
                    });
                });
            });
            

        }).start(PORT);
        
    }

    public Javalin getServer() {
        return server;
    }

}
