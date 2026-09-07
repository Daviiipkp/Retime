package org.daviipkp.retime.reminder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ReminderLink {

    // this should be the object on RAM, which only knows WHERE the reminder info is (holds the ID) and knows when to look for it
    private Instant recallAt;
    private int id;

    public ReminderLink(Instant recallAt, int id) {
        this.recallAt = recallAt;
        this.id = id;
    }


    public long getMillisUntilTrigger() {
        return Instant.now().until(recallAt, ChronoUnit.MILLIS);
    }

    public ReminderLink getFirst(ReminderLink arg0) {
        return (this.recallAt.isAfter(arg0.recallAt))?arg0:this;
    }

    public Instant getRecallAt() {
        return recallAt;
    }

    public int getId() {
        return id;
    }



}
