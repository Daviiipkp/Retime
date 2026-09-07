package org.daviipkp.retime.dto;

import java.time.Instant;

public record ReminderDatabaseRecord(
    int id,
    String name,
    String metadata,
    int action_type,
    Instant created_at,
    Instant recall_at
) {

}
