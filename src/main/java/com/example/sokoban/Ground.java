package com.example.sokoban;

import javafx.scene.image.Image;

public class Ground extends Field{
    private Image ground = new Image(getClass().getResource("/images/ground.png").toExternalForm());
    public Ground(){
    }

    @Override
    public Image getIamge() {
        return ground;
    }

    public char toChar(){
        return 'g';
    }
}
