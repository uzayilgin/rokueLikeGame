/**
 * CustomTimer is a thread-based timer implementation for managing timed events in the game.
 * It tracks the remaining time, supports pausing and resuming, and notifies a listener when the time is up.
 */
package domain.threads;


import ui.swing.TimerDisplay;

import java.util.Timer;
import java.util.TimerTask;

public class CustomTimer implements Runnable {


    private int timeRemaining;
    private boolean isActive = true;
    private boolean isDead = false;
    private int delay;
    private TimerDisplay.TimerListener listener;
    private Runnable task;

    public CustomTimer(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public CustomTimer(int timeRemaining, int delay, Runnable aVoid) {
        this.timeRemaining = timeRemaining;
        this.delay = delay;
        this.task = aVoid;
    }



    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        isActive = false;
    }
    public void _continue() {
        isActive = true;
    }
    public int getTimeRemaining() {
        return timeRemaining;
    }
    public boolean isRunning() {
        return isDead;
    }
    public void setListener(TimerDisplay.TimerListener listener) {
        this.listener = listener;
    }
    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public void incrementTimeRemaining(int timeIncrement) {
        this.timeRemaining += timeIncrement;
    }

    public void kill() {
        stop();
    }
    /**
     * The main execution logic of the timer.
     * Decrements the remaining time, executes the task periodically, and notifies the listener when time is up.
     */
    @Override
    public void run() {
        while (timeRemaining > 0) {
            if (isDead) {
                break;
            }
            try {
                while (!isActive) {
                    //do nothing
                }
                task.run();
                Thread.sleep(delay);
                timeRemaining--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (listener != null) {
            listener.onTimeUp();  // Notify the listener that the time is up
        }
    }
}