package com.example.sokoban;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class Level {
    private Field[][] gamefield;
    private int playerX;
    private int playerY;

    //construcor
    public Level (Field[][] gamefield, int playerX, int playerY){
        this.gamefield = gamefield;
        this.playerX = playerX;
        this.playerY = playerY;
    }

    public static Level[] fromFile() throws IOException {
        String fileName = "C:\\Users\\linus\\Develope\\Uni\\FP25\\Sokoban\\levels.txt";
        ArrayList<Level> levels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(fileName))){
            String line;
            while((line = br.readLine()) != null){
                if(line.equals("+++")){
                    Level tmp;
                    line = br.readLine();
                    String[] dim = line.split("x");
                    line = br.readLine();
                    String[] pStart = line.split(",");
                    Field[][] gamefield = new Field[Integer.parseInt(dim[0])][Integer.parseInt(dim[1])];
                    int row = 0;
                    while(!(line = br.readLine()).equals("---")){
                        String[] col = line.split("");
                        for(int i = 0; i < col.length; i++){
                            if(col[i].equals("w")){
                                gamefield[i][row] = new Wall();
                            }else if(col[i].equals("g")){
                                gamefield[i][row] = new Ground();
                            }else if(col[i].equals("c")){
                                gamefield[i][row] = new Box(false);
                            }else if(col[i].equals("*")){
                                gamefield[i][row] = new Goal();
                            }
                        }
                        row++;
                    }
                    tmp = new Level(gamefield, Integer.parseInt(pStart[1]), Integer.parseInt(pStart[0]));
                    levels.add(tmp);
                }
            }
            Class<?> rFL = Class.forName("com.example.sokoban.ReflectionLevel");
            Object oRFL = rFL.getDeclaredConstructor().newInstance();

            Method methode = rFL.getMethod("getLine", int.class);
            int count = 0;
            try {
                while (true) {
                    String[] result = (String[]) methode.invoke(oRFL, count);
                    if (result == null) break;
                    count++;
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            Field[][] gamefield = new Field[((String[]) methode.invoke(oRFL, 0)).length][count];
            for (int i = 0; i < count; i++) {
                String[] col = (String[]) methode.invoke(oRFL, i);
                for(int j = 0; j < col.length; j++){
                    if(col[j].equals("w")){
                        gamefield[j][i] = new Wall();
                    }else if(col[j].equals("g")){
                        gamefield[j][i] = new Ground();
                    }else if(col[j].equals("c")){
                        gamefield[j][i] = new Box(false);
                    }else if(col[j].equals("*")){
                        gamefield[j][i] = new Goal();
                    }
                }
            }
            int playerY = (int) (rFL.getMethod("getPlayerPosY")).invoke(oRFL);
            int playerX = (int) (rFL.getMethod("getPlayerPosX")).invoke(oRFL);
            levels.add(new Level(gamefield, playerY, playerX));



        }catch (IOException e){
            throw new IIOException("Level file not found");
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return levels.toArray(new Level[0]);
    }

    //getter
    public Field[][] getGamefield(){
        return this.gamefield;
    }
    public int getPlayerX(){
        return this.playerX;
    }
    public int getPlayerY(){
        return this.playerY;
    }


}
