package com.example.sokoban;

import javafx.scene.image.Image;

public class Wall extends Field{
    private Image wall = new Image(getClass().getResource("/images/wall.png").toExternalForm());
    public Wall(){
    }

    @Override
    public Image getIamge() {
         return wall;
    }

    public String toCode(){
        return "w";
    }
}
