package org.daviipkp.retime;

import org.daviipkp.retime.dto.ReminderRecord;
import org.daviipkp.retime.reminder.ReminderLink;

public class RetimeExecutorThread extends Thread {

    private static ReminderLink next;
    private static RetimeExecutorThread instance;

    public RetimeExecutorThread(ReminderLink link) {
        next = link;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(next.getMillisUntilTrigger());
            Retime.callDeadline(next);
        } catch (InterruptedException e) {
            Retime.debug("Interrupted Exception. The executor thread was interrupted while waiting for the Reminder with id " + next.getId() );
        }
    }

    public static boolean firstThanNext(ReminderRecord r) {
        return !r.recall_at().isAfter(next.getRecallAt());
    }

    public static void insertNewNext(ReminderLink link) {
        next = link;
        instance.interrupt();
        instance = new RetimeExecutorThread(link);
        instance.start();
    }

}
