package com.example.sokoban;

import javafx.scene.image.Image;

public abstract class Field {
    public Field(){
    }

    public abstract Image getIamge();

    public abstract char toChar();
}
