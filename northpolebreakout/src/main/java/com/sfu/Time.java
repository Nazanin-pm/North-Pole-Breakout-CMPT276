package com.sfu;

public class Time {
    private long startTime; 
    private long elapsedTime;
    private boolean running;
    private double delta;
    
    public Time() {
        this.startTime = 0;
        this.elapsedTime = 0;

        this.running = false;
    }

    /**
     * Starts or resumes the timer.
     */
    public void start() {
        if (!running) {
            startTime = System.nanoTime() - elapsedTime;
            running = true;
        }
    }

    /**
     * Pauses the timer.
     */
    public void stop() {
        if (running) {
            elapsedTime = System.nanoTime() - startTime;
            running = false;
        }
    }

    public void setTime(long time) {
        elapsedTime = time;
    }

    /**
     * Resets the timer to zero.
     */
    public void reset() {
        startTime = 0;
        elapsedTime = 0;
        running = false;
    }

    /**
     * Retrieves the elapsed time in seconds.
     * @return Elapsed time in seconds as a double.
     */
    public double getElapsedTimeInSeconds() {
        if (running) {
            return (System.nanoTime() - startTime) / 1_000_000_000.0;
        } else {
            return elapsedTime / 1_000_000_000.0;
        }
    }

    /**
     * Retrieves the elapsed time in minutes and seconds as a formatted string.
     * @return Time as a string in "mm:ss" format.
     */
    public String getFormattedTime() {
        double totalSeconds = getElapsedTimeInSeconds();
        int minutes = (int) (totalSeconds / 60);
        int seconds = (int) (totalSeconds % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}
