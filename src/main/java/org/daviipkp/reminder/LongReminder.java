package org.daviipkp.reminder;

import java.time.LocalDate;
import java.time.LocalTime;

public class LongReminder extends Reminder {

    
    private LocalDate date;
    private LocalTime time;

    @Override
    public Reminder create() {
        return new LongReminder();
    }

    @Override
    public void submitCheck() {
        if(date == null || time == null){
            throw new RuntimeException("Invalid reminder submission.");
        }
    }
    

    
}
