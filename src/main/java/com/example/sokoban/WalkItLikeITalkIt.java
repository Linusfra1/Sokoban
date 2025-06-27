package com.example.sokoban;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;

public class WalkItLikeITalkIt implements Runnable{
    private int playerX;
    private int playerY;
    private GraphicsContext gc;
    private Field[][] fields;
    private int dir = 1;
    private HelloApplication app;
    private int frames = 10;

    private Image g = new Image(Objects.requireNonNull(getClass().getResource("/images/ground.png")).toExternalForm());
    private Image u1 = new Image(Objects.requireNonNull(getClass().getResource("/images/player_anim_up1.png")).toExternalForm());
    private Image u2 = new Image(Objects.requireNonNull(getClass().getResource("/images/player_anim_up2.png")).toExternalForm());
    private Image r1 = new Image(Objects.requireNonNull(getClass().getResource("/images/player_anim_right1.png")).toExternalForm());
    private Image r2 = new Image(Objects.requireNonNull(getClass().getResource("/images/player_anim_right2.png")).toExternalForm());
    private Image d1 = new Image(Objects.requireNonNull(getClass().getResource("/images/player_anim_down1.png")).toExternalForm());
    private Image d2 = new Image(Objects.requireNonNull(getClass().getResource("/images/player_anim_down2.png")).toExternalForm());
    private Image l1 = new Image(Objects.requireNonNull(getClass().getResource("/images/player_anim_left1.png")).toExternalForm());
    private Image l2 = new Image(Objects.requireNonNull(getClass().getResource("/images/player_anim_left2.png")).toExternalForm());

    public WalkItLikeITalkIt(int playerX, int playerY, int dir, GraphicsContext gc, Field[][] fields, HelloApplication app){
        this.playerX = playerX;
        this.playerY = playerY;
        this.dir = dir;
        this.gc = gc;
        this.fields = fields;
        this.app = app;
    }

    @Override
    public void run() {
        System.out.println("runing k: " + dir);
        int size = (64/frames);
        int delay = 200/frames;
        for(int k = 0; k < frames; k++){
            int finalK = k + 1;
            if(dir == 1){
                if(k % 2 == 0){
                    Platform.runLater(() -> {
                    gc.drawImage(g, playerX * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(g, playerX * 64, (playerY-1) * 64 + 20, 64, 64);
                    if(fields[playerX][playerY] instanceof Goal) gc.drawImage(fields[playerX][playerY].getIamge(), playerX * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX][playerY-1] instanceof Goal) gc.drawImage(fields[playerX][playerY-1].getIamge(), playerX * 64, (playerY-1) * 64 + 20, 64, 64);
                    gc.drawImage(u1, playerX * 64, playerY * 64 + 20 - finalK * size, 64, 64);});
                }else{
                    Platform.runLater(() -> {
                    gc.drawImage(g, playerX * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(g, playerX * 64, (playerY-1) * 64 + 20, 64, 64);
                    if(fields[playerX][playerY] instanceof Goal) gc.drawImage(fields[playerX][playerY].getIamge(), playerX * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX][playerY-1] instanceof Goal) gc.drawImage(fields[playerX][playerY-1].getIamge(), playerX * 64, (playerY-1) * 64 + 20, 64, 64);
                    gc.drawImage(u2, playerX * 64, playerY * 64 + 20 - finalK * size, 64, 64);});
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if(dir == 2){
                if(k % 2 == 0){
                    Platform.runLater(() -> {
                    gc.drawImage(g, playerX * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(g, (playerX+1) * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX][playerY] instanceof Goal) gc.drawImage(fields[playerX][playerY].getIamge(), playerX * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX+1][playerY] instanceof Goal) gc.drawImage(fields[playerX+1][playerY].getIamge(), (playerX+1) * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(r1, playerX * 64+ finalK * size, playerY * 64 + 20 , 64, 64);});
                }else{
                    Platform.runLater(() -> {
                    gc.drawImage(g, playerX * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(g, (playerX+1) * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX][playerY] instanceof Goal) gc.drawImage(fields[playerX][playerY].getIamge(), playerX * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX+1][playerY] instanceof Goal) gc.drawImage(fields[playerX+1][playerY].getIamge(), (playerX+1) * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(r2, playerX * 64 + finalK * size, playerY * 64 + 20, 64, 64);});
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if(dir == 3){
                if(k % 2 == 0){
                    Platform.runLater(() -> {
                    gc.drawImage(g, playerX * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(g, playerX * 64, (playerY+1) * 64 + 20, 64, 64);
                    if(fields[playerX][playerY] instanceof Goal) gc.drawImage(fields[playerX][playerY].getIamge(), playerX * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX][playerY+1] instanceof Goal) gc.drawImage(fields[playerX][playerY+1].getIamge(), playerX * 64, (playerY+1) * 64 + 20, 64, 64);
                    gc.drawImage(d1, playerX * 64, playerY * 64 + 20 + finalK * size, 64, 64);});
                }else{
                    Platform.runLater(() -> {
                    gc.drawImage(g, playerX * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(g, playerX * 64, (playerY+1) * 64 + 20, 64, 64);
                    if(fields[playerX][playerY] instanceof Goal) gc.drawImage(fields[playerX][playerY].getIamge(), playerX * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX][playerY+1] instanceof Goal) gc.drawImage(fields[playerX][playerY+1].getIamge(), playerX * 64, (playerY+1) * 64 + 20, 64, 64);
                    gc.drawImage(d2, playerX * 64, playerY * 64 + 20 + finalK * size, 64, 64);});
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if(dir == 4){
                if(k % 2 == 0){
                    Platform.runLater(() -> {
                    gc.drawImage(g, playerX * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(g, (playerX-1) * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX][playerY] instanceof Goal) gc.drawImage(fields[playerX][playerY].getIamge(), playerX * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX-1][playerY] instanceof Goal) gc.drawImage(fields[playerX-1][playerY].getIamge(), (playerX-1) * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(l1, playerX * 64 - finalK * size, playerY * 64 + 20, 64, 64);});
                }else{
                    Platform.runLater(() -> {
                    gc.drawImage(g, playerX * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(g, (playerX-1) * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX][playerY] instanceof Goal) gc.drawImage(fields[playerX][playerY].getIamge(), playerX * 64, playerY * 64 + 20, 64, 64);
                    if(fields[playerX-1][playerY] instanceof Goal) gc.drawImage(fields[playerX-1][playerY].getIamge(), (playerX-1) * 64, playerY * 64 + 20, 64, 64);
                    gc.drawImage(l2, playerX * 64 - finalK * size, playerY * 64 + 20, 64, 64);});
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        Platform.runLater(() -> {
            app.draw(fields, gc);
            app.inMove = false;
        });
    }
}
