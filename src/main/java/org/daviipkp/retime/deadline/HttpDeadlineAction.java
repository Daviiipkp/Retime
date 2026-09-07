package org.daviipkp.retime.deadline;

import org.daviipkp.retime.Retime;
import org.daviipkp.retime.dto.HttpRecallRecord;

import io.javalin.json.JavalinJackson;

@Action(id = 1, name = "http_request")
public class HttpDeadlineAction extends DeadlineAction {

    private HttpRecallRecord rec;

    public HttpDeadlineAction() {
        Runnable r = () -> {
            //Implement a HTTP post here
        };
        super(r);
    }

    @Override
    public void setupFromMetadata(String metadata) {
        rec = Retime.fromJsonString(metadata, HttpRecallRecord.class);
    }

    
    
}
