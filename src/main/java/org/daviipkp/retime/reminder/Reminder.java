package org.daviipkp.retime.reminder;

import org.daviipkp.retime.deadline.DeadlineAction;

public class Reminder {

    protected String title;
    protected DeadlineAction action;
    protected ReminderLink link;

    public Reminder(String title, DeadlineAction action, ReminderLink link) {
        this.title = title;
        this.action = action;
        this.link = link;
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
