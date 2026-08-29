package org.daviipkp;

import io.javalin.Javalin;

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
                ctx.status(200).result("Up!");
            });


        }).start(PORT);
    }

    public Javalin getServer() {
        return server;
    }

}
