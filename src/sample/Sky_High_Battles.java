package sample;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Sky_High_Battles extends Application {


    boolean aPressed = false;
    boolean dPressed = false;
    boolean spacePressed = false;
    boolean fire = false;
    double fireSpeed = 10;
    int counter = 0;
    private Pane root;
    private List<CntrlblObjct> bullets = new ArrayList<CntrlblObjct>();
    private List<CntrlblObjct> enemies = new ArrayList<CntrlblObjct>();
    private List<CntrlblObjct> walls = new ArrayList<CntrlblObjct>();
    private CntrlblObjct player;

    static void main(String[] args) {
        launch(args);
    }

    //spawning

    private Parent createContent() {
        root = new Pane(new ImageView(new Image("kwadraty.jpg")));
        root.setPrefSize(600, 600);

        player = new Player();
        player.setVelocity(new Point2D(1, 0));

        addCntrlblObjct(player, root.getPrefWidth() / 2, root.getPrefHeight() / 2);
        addWall(new Wall(), 10, 0, false);
        addWall(new Wall(), 590, 0, false);
        addWall(new Wall(), 300, -290, true);
        addWall(new Wall(), 300, 290, true);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!player.isNotAlive()) onUpdate();
            }
        };
        timer.start();

        return root;
    }

    private void addBullet(CntrlblObjct bullet, double x, double y) {
        bullets.add(bullet);
        addCntrlblObjct(bullet, x, y);
    }

    private void addEnemy(CntrlblObjct enemy, double x, double y) {
        enemies.add(enemy);
        addCntrlblObjct(enemy, x, y);
    }

    private void addWall(CntrlblObjct wall, double x, double y, boolean isRotated) {
        if (isRotated) wall.setRotateObj();
        walls.add(wall);
        addCntrlblObjct(wall, x, y);
    }


    private void addCntrlblObjct(CntrlblObjct obj, double x, double y) {
        obj.getView().setTranslateX(x);
        obj.getView().setTranslateY(y);
        //add obj to root Node
        root.getChildren().add(obj.getView());
    }

    private void onUpdate() { //60times per sec
        for (CntrlblObjct enemy : enemies) {
            if (player.isColliding(enemy)) {
                player.setAlive(false);
                //from view
                root.getChildren().removeAll(player.getView());
            }
            for (CntrlblObjct bullet : bullets) {
                if (bullet.isColliding(enemy)) {
                    bullet.setAlive(false);
                    enemy.setAlive(false);

                    //remove from view
                    root.getChildren().removeAll(bullet.getView(), enemy.getView());
                }
            }
        }
        for (CntrlblObjct wall : walls) {
            if (player.isColliding(wall)) {
                player.setAlive(false);
                //from view
                root.getChildren().remove(player.getView());
            } else {
                for (CntrlblObjct bullet : bullets) {
                    if (bullet.isColliding(wall)) {
                        bullet.setAlive(false);
                        root.getChildren().remove(bullet.getView());
                    }
                }
            }
        }

        //remove from model
        bullets.removeIf(CntrlblObjct::isNotAlive); //if is dead - remove it from list
        enemies.removeIf(CntrlblObjct::isNotAlive);

        bullets.forEach(CntrlblObjct::update);
        enemies.forEach(CntrlblObjct::update);

        player.update();
        if (Math.random() < 0.02) {//how often it spawns
            addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
        counter++;
        if (counter >= 60 / fireSpeed) fire = true;

        if (aPressed) player.rotateLeft();
        if (dPressed) player.rotateRight();
        if (spacePressed == true && fire == true) {
            Bullet bullet = new Bullet();
            //speed up bullet
            bullet.setVelocity(player.getVelocity().normalize().multiply(5));
            addBullet(bullet, player.getView().getTranslateX() + 20, player.getView().getTranslateY() + 20);
            fire = false;
            counter = 0;
        }


    }

    @Override
    public void start(Stage theStage) {

        theStage.setScene(new Scene(createContent()));
        theStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.SPACE) spacePressed = true;
                if (keyEvent.getCode() == KeyCode.A) aPressed = true;
                if (keyEvent.getCode() == KeyCode.D) dPressed = true;
            }
        });
        theStage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.SPACE) spacePressed = false;
                if (keyEvent.getCode() == KeyCode.A) aPressed = false;
                if (keyEvent.getCode() == KeyCode.D) dPressed = false;
            }
        });
        theStage.setResizable(false);
        theStage.show();
    }

    private static class Player extends CntrlblObjct {
        Player() {
            super(new Polygon(40, 20,
                    0, 40,
                    10, 25,
                    0, 20,
                    10, 15,
                    0, 0));
        }
    }

    private static class Enemy extends CntrlblObjct {
        Enemy() {
            super(new Circle(20, 20, 20, Color.GOLD));
        }
    }

    private static class Bullet extends CntrlblObjct {
        Bullet() {
            super(new Circle(2, 2, 2, Color.BLACK));
        }
    }

    private static class Wall extends CntrlblObjct {
        Wall() {
            super(new Line(0, 0, 0, 600));
        }
    }
}
