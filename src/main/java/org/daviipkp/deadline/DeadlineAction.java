package org.daviipkp.deadline;

import java.util.concurrent.ForkJoinPool;

public class DeadlineAction {

    private static final ForkJoinPool POOL = new ForkJoinPool(
        Runtime.getRuntime().availableProcessors()
    );

    private boolean hasExpired = false;
    private Runnable action;

    public void call() {
        if(hasExpired) {
            throw new RuntimeException();
        }

        POOL.submit(action);

        hasExpired=true;

    }

    public DeadlineAction(Runnable arg0) {
        this.action = arg0;
    }

}
