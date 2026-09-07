package org.daviipkp.retime.dto;

import java.time.Instant;

public record ReminderRecord(
    String name,
    Instant recall_at,
    String action_type,
    String recall_data

) {

    public ReminderRecord getFirst(ReminderRecord arg0) {
        return (this.recall_at.isAfter(arg0.recall_at))?arg0:this;
    }

}
