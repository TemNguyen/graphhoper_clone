package com.graphhopper.traffic.demo;

import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    Thread thread;

    MyTimerTask(Thread thread) {
        this.thread = thread;
    }

    @Override
    public void run() {
        thread.start();
    }
}
