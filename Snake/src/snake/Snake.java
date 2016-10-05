/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;
 
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
 
/**
 *
 * @author beh01
 */
public class Snake extends Application {
    
    //Alert aler = new Alert(Alert.AlertType.ERROR);
    
    private Random rd = new Random();
    private Point food = new Point();
   
    private final int grid_x = 5; //number of rows
    private final int grid_y = 5; //number of columns
    private int size = grid_x * grid_y;
 
    private Rectangle[][] grid;
    private Timeline action;
 
    private final Point direction = new Point(1, 0);
    private final ArrayList<Point> points = new ArrayList<>();
 
    public Snake() {
        points.add(new Point(grid_x / 2,grid_y / 2));    
    }
    
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "Guess what? - " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        System.exit(2);
    }
 
    @Override
    public void start(Stage primaryStage) {
        AnchorPane basePane = new AnchorPane();
        
        Button score = new Button();
        
        Button btnStart = new Button();
        btnStart.setText("Start game");
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                if (action.getStatus() == Animation.Status.RUNNING) {
                    action.stop();
                    btnStart.setText("Start game");
                } else {
                    action.play();
                    btnStart.setText("Stop game");
                }
            }
        });
 
        basePane.getChildren().add(btnStart);
        AnchorPane.setTopAnchor(btnStart, 1.0);
        AnchorPane.setLeftAnchor(btnStart, 1.0);
        AnchorPane.setRightAnchor(btnStart, 1.0);
        
        basePane.getChildren().add(score);
        AnchorPane.setTopAnchor(score, 30.0);
        AnchorPane.setLeftAnchor(score, 250.0);
        AnchorPane.setRightAnchor(score, 250.0);
 
        Pane root = new Pane();
        basePane.getChildren().add(root);
        AnchorPane.setBottomAnchor(root, 4.0);
        AnchorPane.setLeftAnchor(root, 4.0);
        AnchorPane.setRightAnchor(root, 4.0);
        AnchorPane.setTopAnchor(root, 60.0);
        NumberBinding rectSize
                = Bindings.min(root.heightProperty().divide(grid_y),
                        root.widthProperty().divide(grid_x));
 
        grid = new Rectangle[grid_x][grid_y];
        for (int x = 0; x < grid_x; x++) {
            for (int y = 0; y < grid_y; y++) {
                grid[x][y] = new Rectangle();
                grid[x][y].setStroke(Color.WHITE);
                grid[x][y].setFill(Color.BLACK);
 
                grid[x][y].xProperty().bind(rectSize.multiply(x));
                grid[x][y].yProperty().bind(rectSize.multiply(y));
 
                grid[x][y].heightProperty().bind(rectSize);
                grid[x][y].widthProperty().bind(grid[x][y].heightProperty());
 
                root.getChildren().add(grid[x][y]);
            }
                     
        }
 
        Scene scene = new Scene(basePane, 800, 630);
 
        scene.addEventHandler(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.RIGHT) {
                    direction.x = 1;
                    direction.y = 0;
                }
                if (event.getCode() == KeyCode.LEFT) {
                    direction.x = -1;
                    direction.y = 0;
                }
                if (event.getCode() == KeyCode.UP) {
                    direction.x = 0;
                    direction.y = -1;
                }
                if (event.getCode() == KeyCode.DOWN) {
                    direction.x = 0;
                    direction.y = 1;
                }
            }
 
        });
        
        food.x = rd.nextInt(grid_x);
        food.y = rd.nextInt(grid_y);   
        grid[food.x][food.y].setFill(Color.LIGHTGRAY);
        
     
        action = new Timeline(
                new KeyFrame(Duration.seconds(0.5),
                        new EventHandler<ActionEvent>() {
 
                    @Override
                    public void handle(ActionEvent event) {
 
                        Point first = new Point(points.get(points.size() -1));
                        first.x = (first.x + direction.x + grid_x) % grid_x;
                        first.y = (first.y + direction.y + grid_y) % grid_y;
                        points.add(first);
                        grid[first.x][first.y].setFill(Color.DARKRED);
 
                        Point last = points.get(0);
                        grid[last.x][last.y].setFill(Color.BLACK);
                        points.remove(0);
                       
                        if (first.x == food.x && first.y == food.y){
                        points.add(first);
                        score.setText("Score je : "+(points.size()-1));
                        
                        food.x = rd.nextInt(grid_x);
                        food.y = rd.nextInt(grid_y);                        
                        while(true) {
                            if(grid[food.x][food.y].getFill().equals(Color.DARKRED)) {
                                food.x = rd.nextInt(grid_x);
                                food.y = rd.nextInt(grid_y);
                            } else {
                                grid[food.x][food.y].setFill(Color.LIGHTGRAY);
                                break;
                            }
                        }
                          
                        }
                       
                        if (points.size()== size+2)
                        {
                            System.out.println("YOU ARE WINNER");
                            action.stop();
                            infoBox("You have won this shit !!!", "CONGRATULATIONS");
                        }
                        
                        
                        for (int i = points.size()-3; i >= 0; i--) 
                        {
                        if (points.get(points.size()-1).x == points.get(i).x && points.get(points.size()-1).y == points.get(i).y)
                            {
                               System.out.println("Game over");
                               action.stop();
                               infoBox("You are noob !", "GAME OVER");
                               btnStart.setText("Start game");
                            }
                        }               
                    }
                }));
        
        action.setCycleCount(Timeline.INDEFINITE);
 
        primaryStage.setScene(scene);
        primaryStage.show();

    }
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
 
}