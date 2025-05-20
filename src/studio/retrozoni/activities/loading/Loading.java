package studio.retrozoni.activities.loading;

import studio.retrozoni.engine.ActionConsumer;
import studio.retrozoni.engine.Activity;

import java.awt.*;
import java.util.List;

public class Loading implements Activity, Runnable {

    private final Thread thread;

    private final List<Activity> stack;
    private final Activity next;
    private final ActionConsumer action;
    private volatile boolean finish;
    private boolean start;

    public Loading(List<Activity> stack, Activity next, ActionConsumer action) {
        this.thread = new Thread(this);
        this.stack = stack;
        this.next = next;
        this.action = action;
        this.finish = false;
        this.start = false;
    }

    @Override
    public synchronized void tick() {
        if(!start) {
            start = true;
            this.thread.start();
        }
        if(finish) {
            stack.remove(this);
            stack.add(next);
        }
    }

    @Override
    public void render(Graphics2D g) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void run() {
        action.action();
        finish = true;
    }
}
