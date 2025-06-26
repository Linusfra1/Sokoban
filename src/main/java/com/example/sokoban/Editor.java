package com.example.sokoban;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.io.FileWriter;
import java.io.IOException;

public class Editor {
    private static Pane root;
    private static Integer xVal = 16;
    private static Integer yVal = 10;
    private static Button[][] Bfield = new Button[xVal][yVal];
    private static Field[][] field = new Field[xVal][yVal];
    private static Field current = new Ground();
    private static Integer playerX = -1;
    private static Integer playerY = -1;
    private static Image curImage = current.getIamge();


    private static Image player = new Image(Editor.class.getResource("/images/player_down.png").toExternalForm());
    private static Image ground = new Image(Editor.class.getResource("/images/ground.png").toExternalForm());

    public Editor(){

    }

    public static void startEditor(Stage stage) {
        stage.setTitle("Sokoban Editor");
        root = new Pane();
        stage.setScene(new javafx.scene.Scene(root, 1344, 640));

        HBox main = new HBox();

        Canvas canvas = new Canvas(64 * xVal, 64 * yVal);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for(int i = 0; i < field.length; i++){
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Ground();
                gc.drawImage(ground, 64 * i, 64 * j, 64, 64);
            }
        }

        GridPane grid = new GridPane();
        int buttonWidth = 64;
        int buttonHeight = 64;

        for(int i = 0; i < Bfield.length; i++){
            for(int j = 0; j < Bfield[i].length; j++){
                Bfield[i][j] = new Button(" ");
                Bfield[i][j].setPrefSize(buttonWidth, buttonHeight);
                Bfield[i][j].setMinSize(buttonWidth, buttonHeight);
                Bfield[i][j].setMaxSize(buttonWidth, buttonHeight);
                Bfield[i][j].setFocusTraversable(false);
                Bfield[i][j].setStyle("-fx-background-color: transparent;"); // macht den Button unsichtbar
                Bfield[i][j].setMouseTransparent(false); // erlaubt Mausinteraktionen
                Bfield[i][j].setStyle("-fx-border-color: lightgray; " +
                        "-fx-border-width: 0.5px; " +
                        "-fx-background-color: transparent;");



                int finalI = i;
                int finalJ = j;
                Bfield[i][j].setOnAction(e -> {
                    if (gc == null) {
                        System.out.println("GraphicsContext ist null!");
                        return;
                    }

                    if(current instanceof Player){
                        if(field[finalI][finalJ] instanceof Wall || field[finalI][finalJ] instanceof Box){
                            field[finalI][finalJ] = new Ground();
                        }
                        playerX = finalI;
                        playerY = finalJ;
                    }else if(current instanceof Box){
                        if(field[finalI][finalJ] instanceof Player){
                            playerX = null;
                            playerY = null;
                        }
                        field[finalI][finalJ] = new Box(false);
                    }else if(current instanceof Goal){
                        field[finalI][finalJ] = new Goal();
                    }else if(current instanceof Ground){
                        field[finalI][finalJ] = new Ground();
                    } else if(current instanceof Wall) {
                        if(field[finalI][finalJ] instanceof Player){
                            playerX = null;
                            playerY = null;
                        }
                        field[finalI][finalJ] = new Wall();
                    }
                    gc.drawImage(ground, 64 * finalI, 64 * finalJ, 64, 64);
                    gc.drawImage(field[finalI][finalJ].getIamge(), 64 * finalI, 64 * finalJ, 64, 64);
                    if(playerX != null) gc.drawImage(player, 64 * playerX, 64 * playerY, 64, 64);
                });

                grid.add(Bfield[i][j], i, j);
            }
        }

// Optional: Entfernen Sie Abstände zwischen den Buttons
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setPadding(new Insets(0));

        canvas.setLayoutX(0);  // Setze X-Position auf 0
        canvas.setLayoutY(0);  // Setze Y-Position auf 0

        javafx.scene.layout.StackPane gameArea = new javafx.scene.layout.StackPane();
        gameArea.getChildren().addAll(canvas, grid);  // Canvas zuerst, dann Grid


        //VBox erstell in denen die verschiedenen Optionen für current und das aktuell ausgewählte Feld stehen

        VBox fieldOptions = new VBox();
        Field[] sub = {new Box(false), new Goal(), new Ground(), new Player(3), new Wall()};

        Button[] buttons = new Button[sub.length];

        HBox cur = new HBox();
        Label curLabel = new Label("Current: ");
        curImage = current.getIamge();
        cur.getChildren().addAll(curLabel, new ImageView(curImage));

        for(int i = 0; i < sub.length; i++){
            try {
                buttons[i] = new Button("", new ImageView(sub[i].getIamge()));
                
                // Finale Kopie für den Lambda-Ausdruck
                final Field finalField = sub[i];
                buttons[i].setOnAction(e -> {
                    current = finalField;
                    // Aktualisiere das aktuelle Bild
                    curImage = finalField.getIamge();
                    ((ImageView)cur.getChildren().get(1)).setImage(curImage);
                });
                buttons[i].setPrefSize(64, 64);
            } catch (Exception e) {
                System.err.println("Fehler beim Erstellen des Buttons: " + e.getMessage());
                // Oder Logger verwenden
                // logger.error("Fehler beim Erstellen des Buttons", e);
            }
        }

        Button save = new Button("Save");
        save.setOnAction(e -> {
            if(playerX < 0 || playerY < 0) return;
            if(xVal <= 0 || yVal <= 0) return;
            for(int i = 0; i < field.length; i++){
                for(int j = 0; j < field[i].length; j++){
                    if(field[i][j] == null) return;
                }
            }
            try(FileWriter writer = new FileWriter("C:\\Users\\linus\\Develope\\Uni\\FP25\\Sokoban\\levels.txt", true)) {
                writer.write("+++\n");
                writer.write(xVal + "x" + yVal + "\n");
                writer.write(playerY + "," + playerX + "\n");
                for (int j = 0; j < field[0].length; j++) {
                    StringBuilder row = new StringBuilder();
                    for (int i = 0; i < field.length; i++) {
                        row.append(field[i][j].toChar());
                    }
                    writer.write(row + "\n");
                }
                writer.write("---\n");
                writer.write("\n");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        fieldOptions.getChildren().add(cur);
        fieldOptions.getChildren().addAll(buttons);
        fieldOptions.getChildren().add(save);




        main.getChildren().add(gameArea);
        main.getChildren().add(fieldOptions);
        root.getChildren().add(main);

        stage.setScene(new javafx.scene.Scene(root, 1024, 640));
        stage.show();


    }
}