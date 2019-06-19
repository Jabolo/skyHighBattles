package sample;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Sky_High_Battles extends Application {


    boolean aPressed = false;
    boolean dPressed = false;
    boolean spacePressed = false;
    boolean fire = false;
    int counterFireSpeed = 0;
    int counterEnemySpeed = 0;

    private Pane root;
    private List<CntrlblObjct> bullets = new ArrayList<CntrlblObjct>();
    private List<CntrlblObjct> enemies = new ArrayList<CntrlblObjct>();
    private List<CntrlblObjct> walls = new ArrayList<CntrlblObjct>();
    private List<CntrlblObjct> bulletsEnemies = new ArrayList<CntrlblObjct>();
    private CntrlblObjct player;

    static void main(String[] args) {
        launch(args);
    }


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

        Label labelPoints = new Label("0");
        labelPoints.setTranslateX(550);
        labelPoints.setTranslateY(20);
        root.getChildren().add(labelPoints);

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

    private void addBulletEnemie(CntrlblObjct bullet, double x, double y) {
        bulletsEnemies.add(bullet);
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
            //collision enemy : player
            if (player.isColliding(enemy)) {
                player.setAlive(false);
                //from view
                root.getChildren().removeAll(player.getView());
            }
            //collision bullet enemy
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
            //collision wall player
            if (player.isColliding(wall)) {
                player.setAlive(false);
                //from view
                root.getChildren().remove(player.getView());
            } else
            //collision bullet : wall
            {
                for (CntrlblObjct bullet : bullets) {
                    if (bullet.isColliding(wall)) {
                        bullet.setAlive(false);
                        root.getChildren().remove(bullet.getView());
                    }
                }
            }
            for (CntrlblObjct bulletFromEnemy : bulletsEnemies) {
                if (bulletFromEnemy.isColliding(wall)) {
                    bulletFromEnemy.setAlive(false);
                    root.getChildren().remove(bulletFromEnemy.getView());
                }
            }
        }
        //collision bulletEnemie : player
        for (CntrlblObjct bulletFromEnemie : bulletsEnemies) {
            if (player.isColliding(bulletFromEnemie)) {
                player.setAlive(false);
                bulletFromEnemie.setAlive(false);
                //from view
                root.getChildren().remove(player.getView());
                root.getChildren().removeAll(bulletFromEnemie.getView());
            }
        }
//spawning
        if (Math.random() < 0.02) {//how often it spawns
            addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
        counterFireSpeed++;
        if (counterFireSpeed >= 60 / player.getFireSpeed()) fire = true;
        if (aPressed) player.rotateLeft();
        if (dPressed) player.rotateRight();
        //fire
        if (spacePressed == true && fire == true) {
            Bullet bullet = new Bullet();
            bullet.setVelocity(player.getVelocity().normalize().multiply(bullet.getBulletSpeed()));
            addBullet(bullet, player.getView().getTranslateX() + 20, player.getView().getTranslateY() + 20);
            fire = false;
            counterFireSpeed = 0;
        }
        //enemy vel
        for (CntrlblObjct enemy : enemies) {
            Point2D enemyVel = new Point2D(player.getView().getTranslateX(), player.getView().getTranslateY())
                    .subtract(enemy.getView().getTranslateX(), enemy.getView().getTranslateY())
                    .normalize()
                    .multiply(0.5);

            enemy.setVelocity(enemyVel);
            //enemy bullet
            if (Math.random() < enemy.getFireSpeed() / 100) {
                Bullet bullet = new Bullet();
                bullet.setVelocity(enemyVel.multiply(bullet.getBulletSpeed()));
                addBulletEnemie(bullet, enemy.getView().getTranslateX() + 20, enemy.getView().getTranslateY() + 20);
            }
        }
        counterEnemySpeed = 0;


        //remove from model
        bullets.removeIf(CntrlblObjct::isNotAlive); //if is dead - remove it from list
        enemies.removeIf(CntrlblObjct::isNotAlive);
        bulletsEnemies.removeIf(CntrlblObjct::isNotAlive);

        bullets.forEach(CntrlblObjct::update);
        enemies.forEach(CntrlblObjct::update);
        bulletsEnemies.forEach(CntrlblObjct::update);

        player.update();
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
}
