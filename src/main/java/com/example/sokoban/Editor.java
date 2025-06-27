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
    private static Field[][] fields = new Field[xVal][yVal];
    private static Field current = new Ground();
    private static Player player = new Player(3);
    private static Image curImage = current.getIamge();
    private static int goals = 0;
    private static int crates = 0;


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

        for(int i = 0; i < fields.length; i++){
            for (int j = 0; j < fields[i].length; j++) {
                fields[i][j] = new Ground();
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
                        if(fields[finalI][finalJ] instanceof Wall){
                            fields[finalI][finalJ] = new Ground();
                        } else if(fields[finalI][finalJ] instanceof Box){
                            fields[finalI][finalJ] = new Ground();
                            crates--;
                        }
                        player.setPosX(finalI);
                        player.setPosY(finalJ);
                    }else if(current instanceof Box){
                        if(fields[finalI][finalJ] instanceof Player){
                            player.setPosX(-1);
                            player.setPosY(-1);
                        } else if (fields[finalI][finalJ] instanceof Goal) {
                            goals--;
                        }
                        fields[finalI][finalJ] = new Box(false);
                        crates++;
                    }else if(current instanceof Goal){
                        if (fields[finalI][finalJ] instanceof Box) {
                            crates--;
                        }
                        fields[finalI][finalJ] = new Goal();
                        goals++;

                    }else if(current instanceof Ground){
                        if (fields[finalI][finalJ] instanceof Box) {
                            crates--;
                        } else if (fields[finalI][finalJ] instanceof Goal) {
                            goals--;
                        }
                        fields[finalI][finalJ] = new Ground();
                    } else if(current instanceof Wall) {
                        if (fields[finalI][finalJ] instanceof Box) {
                            crates--;
                        } else if (fields[finalI][finalJ] instanceof Goal) {
                            goals--;
                        }
                        if(fields[finalI][finalJ] instanceof Player){
                            player.setPosX(-1);
                            player.setPosY(-1);
                        }
                        fields[finalI][finalJ] = new Wall();
                    }
                    gc.drawImage(ground, 64 * finalI, 64 * finalJ, 64, 64);
                    gc.drawImage(fields[finalI][finalJ].getIamge(), 64 * finalI, 64 * finalJ, 64, 64);
                    if(player.getPosX() >= 0) gc.drawImage(player.getIamge(), 64 * player.getPosX(), 64 * player.getPosY(), 64, 64);
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
            if(player.getPosX() < 0) return;
            if(xVal <= 0 || yVal <= 0) return;
            if(crates < goals || goals == 0) return;
            for(int i = 0; i < fields.length; i++){
                for(int j = 0; j < fields[i].length; j++){
                    if(fields[i][j] == null) return;
                }
            }
            try(FileWriter writer = new FileWriter("C:\\Users\\linus\\Develope\\Uni\\FP25\\Sokoban\\levels.txt", true)) {
                writer.write("+++\n");
                writer.write(xVal + "x" + yVal + "\n");
                writer.write(player.toCode() + "\n");
                for (int j = 0; j < fields[0].length; j++) {
                    StringBuilder row = new StringBuilder();
                    for (int i = 0; i < fields.length; i++) {
                        row.append(fields[i][j].toCode());
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