package com.example.sokoban;

import javafx.scene.image.Image;

public class Box extends Field{
    private boolean onGoal = false;
    Image crate = new Image(getClass().getResource("/images/crate.png").toExternalForm());
    Image goal = new Image(getClass().getResource("/images/crate_on_target.png").toExternalForm());
    public Box(boolean onGoal){
        this.onGoal = onGoal;
    }

    @Override
    public Image getIamge() {
        if(!onGoal) return crate;
        else return goal;
    }

    public boolean isOnGoal() {
        return onGoal;
    }

    public String toCode(){
        return "c";
    }
}
