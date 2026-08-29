package org.daviipkp.deadline;

public class HttpPostDeadlineAction extends DeadlineAction {

    private String url;
    private String endpoint;
    private String content;

    public HttpPostDeadlineAction(String arg0, String arg1, String arg2) {
        url = arg0;
        endpoint = arg1;
        content = arg2;
        
        Runnable r = () -> {
            //Implement a HTTP post here
        };
        
        super(r);
    }
    
    
}
