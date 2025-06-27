package com.example.sokoban;

import javafx.scene.image.Image;

public class Goal extends Field{
    private Image goal = new Image(getClass().getResource("/images/crate_target.png").toExternalForm());

    public Goal(){}

    @Override
    public Image getIamge() {
        return goal;
    }

    public String toCode(){
        return "*";
    }
}
