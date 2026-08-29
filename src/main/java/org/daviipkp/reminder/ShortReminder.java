package org.daviipkp.reminder;

import java.time.LocalTime;

public class ShortReminder extends Reminder {

    private LocalTime time;

    @Override
    public Reminder create() {
        return new ShortReminder();
    }

    @Override
    public void submitCheck() {
        if(time == null){
            throw new RuntimeException("Invalid reminder submission.");
        }
    }
    
}
