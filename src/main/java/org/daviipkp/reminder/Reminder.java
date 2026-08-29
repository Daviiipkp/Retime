package org.daviipkp.reminder;

import org.daviipkp.deadline.DeadlineAction;

public abstract class Reminder {
    private int id;
    private String title;
    private DeadlineAction action;

    public abstract Reminder create();
    public abstract void submitCheck();
    
    public void submit() {
        submitCheck();
        if(title == null || title.isEmpty() || action == null) {
            throw new RuntimeException("Invalid reminder submission.");
        }

        //submit reminder logic
    }

    public int getId() {
        return id;
    }

    public Reminder setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Reminder setTitle(String title) {
        this.title = title;
        return this;
    }

    public DeadlineAction getAction() {
        return action;
    }

    public Reminder setAction(DeadlineAction action) {
        this.action = action;
        return this;
    }

}
