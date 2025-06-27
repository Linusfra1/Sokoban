package com.example.sokoban;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.lang.reflect.*;

import java.io.IOException;

public class HelloApplication extends Application {

    private Stage stage;
    private Scene scene;
    private Pane root;
    private Canvas c1;

    private Thread t;
    private int steps = 0;
    private int time = 0;
    private final Label timeLabel = new Label("Time: " + time);
    private final Label scoreLabel = new Label("Score: " + steps);
    private Boolean paused;
    private Durration durration;

    private final Player player = new Player(3);
    private int goalsize = 0;
    private int onGoals = 0;
    private Field[][] fields;
    public boolean inMove = false;

    private int lvl = 1;

    //draw
    public void draw(Field[][] fields, GraphicsContext gc) {
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] instanceof Goal) {
                    gc.drawImage(new Image(getClass().getResource("/images/ground.png").toExternalForm()) , i * 64, j * 64 + 20, 64, 64);
                    gc.drawImage(fields[i][j].getIamge(), i * 64, j * 64 + 20, 64, 64);
                }else {
                    gc.drawImage(fields[i][j].getIamge(), i * 64, j * 64 + 20, 64, 64);
                }
            }
        }
        gc.drawImage(player.getIamge(), player.getPosX() * 64, player.getPosY() * 64 + 20, 64, 64);
    }
    public void initalize(Field[][] fields, GraphicsContext gc) {
        paused = false;
        for (int i = 0 ; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] instanceof Goal) {
                    gc.drawImage(new Image(getClass().getResource("/images/ground.png").toExternalForm()) , i * 64, j * 64 + 20, 64, 64);
                    gc.drawImage(fields[i][j].getIamge(), i * 64, j * 64 + 20, 64, 64);
                    goalsize++;
                }else {
                    gc.drawImage(fields[i][j].getIamge(), i * 64, j * 64 + 20, 64, 64);
                }
            }
        }
        gc.drawImage(player.getIamge(), player.getPosX() * 64, player.getPosY() * 64 + 20, 64, 64);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        root = new Pane();

        //Canvas
        c1 = new Canvas(1024, 640);
        root.getChildren().add(c1);
        scene = new Scene(root, 1024, 640);

        GraphicsContext gc = c1.getGraphicsContext2D();

        //Scoreboard
        HBox scoreBoard = new HBox(10);
        Button back = new Button("Back");
        back.setOnAction(e -> {
            abortLevel(scoreBoard, gc);
        });
        Button startPause = new Button("Pause");
        startPause.setOnAction(e -> {
            if(!paused){
                startPause.setText("resume");
                paused = true;
                durration.pause();
            }else{
                startPause.setText("play");
                paused = false;
                durration.resume();
            }
        });
        back.setFocusTraversable(false);
        startPause.setFocusTraversable(false);
        scoreBoard.getChildren().addAll(back, startPause);
        scoreBoard.getChildren().addAll(timeLabel, scoreLabel);

        startMenu(scoreBoard, gc);

        scene.setOnKeyPressed(event -> {
            if(paused) return;
            if(inMove) return;
            Thread walkThread;
            int offsetX = 0;
            int offsetY = 0;
            int dir = 3;
            switch (event.getCode()) {
                case UP://Y-1
                    offsetX = 0;
                    offsetY = -1;
                    dir = 1;
                    break;
                case RIGHT://X+1
                    offsetX = 1;
                    offsetY = 0;
                    dir = 2;
                    break;
                case DOWN://Y+1
                    offsetX = 0;
                    offsetY = 1;
                    dir = 3;
                    break;
                case LEFT://X-1
                    offsetX = -1;
                    offsetY = 0;
                    dir = 4;
                    break;
            }
            if(player.getPosX() + offsetX >= 0 && player.getPosY() + offsetY >= 0 && player.getPosX() + offsetX < fields.length && player.getPosY() + offsetY < fields[0].length && !(fields[player.getPosX() + offsetX][player.getPosY() + offsetY] instanceof Wall)){
                //Box schieben
                if(fields[player.getPosX() + offsetX][player.getPosY() + offsetY] instanceof Box && (fields[player.getPosX() + 2 * offsetX][player.getPosY() + 2 * offsetY] instanceof Ground || fields[player.getPosX() + 2 * offsetX][player.getPosY() + 2 * offsetY] instanceof Goal)){
                    System.out.println("Movable Box");
                    WalkItLikeITalkIt walk = new WalkItLikeITalkIt(player.getPosX(), player.getPosY(), dir, gc, fields, this);
                    walkThread = new Thread(walk);
                    inMove = true;
                    walkThread.start();
                    steps++;
                    //teste ob Box auf goal Stand
                    boolean check = ((Box) fields[player.getPosX() + offsetX][player.getPosY() + offsetY]).isOnGoal();
                    player.setPosX(player.getPosX() + offsetX);
                    player.setPosY(player.getPosY() + offsetY);
                    //wenn box auf goal gerückt dann merken
                    if(fields[player.getPosX() + offsetX][player.getPosY() + offsetY] instanceof Goal){
                        fields[player.getPosX() + offsetX][player.getPosY() + offsetY] = new Box(true);
                        onGoals++;
                    }else fields[player.getPosX() + offsetX][player.getPosY() + offsetY]=new Box(false);
                    //gucken ob box auf goal war
                    if(check){
                        fields[player.getPosX()][player.getPosY()] = new Goal();
                        onGoals--;
                    }else{
                        fields[player.getPosX()][player.getPosY()] = new Ground();
                    }
                }else  if(fields[player.getPosX() + offsetX][player.getPosY() + offsetY] instanceof Ground || fields[player.getPosX() + offsetX][player.getPosY() + offsetY] instanceof Goal){
                    System.out.println("Normal move");
                    WalkItLikeITalkIt walk = new WalkItLikeITalkIt(player.getPosX(), player.getPosY(), dir, gc, fields, this);
                    walkThread = new Thread(walk);
                    inMove = true;
                    walkThread.start();
                    steps++;
                    player.setPosX(player.getPosX() + offsetX);
                    player.setPosY(player.getPosY() + offsetY);
                }
            }
            System.out.println(player.getPosX() + " " + offsetX + " " + player.getPosY() + " " + offsetY + " " + fields[player.getPosX() + offsetX][player.getPosY() + offsetY]);
            scoreLabel.setText("Score: " + steps);
            player.setDir(dir);
            draw(fields, gc);
            if(onGoals == goalsize){
                t.interrupt();
                durration.stop();
                Alert result = new Alert(Alert.AlertType.INFORMATION);
                result.setContentText("You won!");
                result.showAndWait();
                System.exit(0);
            }
        });

        stage.setTitle("Sokoban");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    private void startMenu(HBox scoreBoard, GraphicsContext gc) throws IOException {
        root.getChildren().clear();
        root.getChildren().add(c1);
        gc.clearRect(0, 0, c1.getWidth(), c1.getHeight());


        //Hauptmenü
        VBox vb = new VBox();

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            root.getChildren().remove(vb);
            root.getChildren().add(scoreBoard);
            startGame(gc);
        });

        Button editorButton = new Button("Editor");
        editorButton.setOnAction(e -> {
           root.getChildren().remove(vb);
           Editor.startEditor(stage);
        });

        HBox options = new HBox();
        options.getChildren().addAll(startButton, editorButton);

        HBox levelBox = new HBox();
        Button levelDownButton = new Button("<");
        Button levelUpButton = new Button(">");
        Label levelLabel = new Label("Level " + lvl);
        levelDownButton.setOnAction(e -> {
            if(lvl > 1){
                lvl--;
                levelLabel.setText("Level " + lvl);
            }
        });
        levelUpButton.setOnAction(e -> {
            try {
                if(lvl < Level.fromFile().length) {
                    lvl++;
                    levelLabel.setText("Level " + lvl);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        levelBox.getChildren().addAll(levelDownButton, levelLabel, levelUpButton);
        vb.getChildren().addAll(options, levelBox);

        Level[] levels = Level.fromFile();

        //einbindung
        root.getChildren().add(vb);
    }

    private void startGame(GraphicsContext gc) {
        try {
            Level currentLevel = Level.fromFile()[lvl-1];
            player.setPosX(currentLevel.getPlayerX());
            player.setPosY(currentLevel.getPlayerY());
            fields = currentLevel.getGamefield();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        durration = new Durration(timeLabel);
        t = new Thread(durration);
        t.start();
        c1.setHeight(64 * fields[0].length + 20);
        c1.setWidth(64 * fields.length);
        stage.setWidth(c1.getWidth()+20);
        stage.setHeight(c1.getHeight()+40);
        stage.centerOnScreen();

        initalize(fields, gc);
    }

    private void abortLevel(HBox vb, GraphicsContext gc){
        if(durration != null){
            durration.stop();
        }
        steps = 0;
        time = 0;
        goalsize = 0;
        onGoals = 0;

        Platform.runLater(() -> {
            try {
                startMenu(vb, gc);
            } catch (IOException _) {
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}