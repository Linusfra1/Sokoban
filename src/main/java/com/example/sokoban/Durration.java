package com.example.sokoban;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Durration implements Runnable {
    private Label time;
    volatile boolean running = false;
    private long startTime;
    private long endTime;
    private long pauseTime;
    private long TotalPauseTime = 0;
    private volatile boolean paused = false;

    public Durration(Label time) {
        this.time = time;
    }

    @Override
    public void run() {
        running = true;
        startTime = System.currentTimeMillis();
        while (running) {
            if(!paused) {
                endTime = System.currentTimeMillis();
                final String timeText = getString();

                Platform.runLater(() -> time.setText(timeText));


            }
            try {
                Thread.sleep(100); // Reduziert CPU-Last
            } catch (InterruptedException _) {
            }
        }
    }

    private String getString() {
        int seconds = (int) ((endTime - startTime - TotalPauseTime) / 1000) % 60;
        int minuets = (int) ((endTime - startTime - TotalPauseTime) / (1000 * 60)) % 60;
        String secondsText = "";
        String minuetsText = "";
        if (seconds < 10) {
            secondsText = "0" + seconds;
        }else{
            secondsText = "" + seconds;
        }
        if (minuets < 10) {
            minuetsText = "0" + minuets;
        }
        else{
            minuetsText = "" + minuets;
        }
        final String timeText = "Time: " + minuetsText + ":" + secondsText;
        return timeText;
    }

    public void stop(){
        running = false;
    }

    public void pause(){
        pauseTime = System.currentTimeMillis();
        paused = true;
    }

    public void resume(){
        TotalPauseTime += System.currentTimeMillis() - pauseTime;
        paused = false;
    }
}