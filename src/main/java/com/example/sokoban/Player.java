package com.example.sokoban;

import javafx.scene.image.Image;

public class Player extends Field{
    private Image u = new Image(getClass().getResource("/images/player_up.png").toExternalForm());
    private Image r = new Image(getClass().getResource("/images/player_right.png").toExternalForm());
    private Image d = new Image(getClass().getResource("/images/player_down.png").toExternalForm());
    private Image l = new Image(getClass().getResource("/images/player_left.png").toExternalForm());

    public int dir;
    public Player(int dir){
        this.dir = dir;
    }

    @Override
    public Image getIamge() {
        if(this.dir == 1){
            return u;
        } else if(this.dir == 2){
            return r;
        } else if(this.dir == 3){
            return d;
        } else if(this.dir == 4){
            return l;
        }
        return null;
    }

    public void setDir(int i){
        if(i >= 0 && i < 5){
            this.dir = i;
        }
    }

    @Override
    public char toChar() {
        return 0;
    }
}
